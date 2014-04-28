package com.agilebc.feed.dao.rest;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.jws.WebResult;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.data.config.CryptoCCYExchange;
import com.agilebc.data.proprietary.CryptsyAllMarkets;
import com.agilebc.data.proprietary.CryptsyOrderDepth;
import com.agilebc.data.proprietary.CryptsyServerInfo;
import com.agilebc.data.trade.TradePair;
import com.agilebc.data.trade.TradePairEnh;
import com.agilebc.data.trade.TradeServerInfo;
import com.agilebc.data.vo.Tradable;
import com.agilebc.feed.dao.CccyExchgDao;
import com.agilebc.feed.dao.ExchangeTradingAPI;
import com.agilebc.util.DateTimeUtils;
import com.agilebc.util.EncryptionUtils;
import com.agilebc.util.config.GenericConfigLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;


/**
 *   I designed this Dao to be used by 1 thread at time.  
 * @author yyx100
 *
 */
public class CryptsyDaoImpl  extends CccyExchgDao  implements ExchangeTradingAPI {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(CryptsyDaoImpl.class);
	
	public final static String _METHOD_GENMKTDATA = "method=marketdatav2";
	protected static int cryptsyCntr = 1;

	private ClientConfig cconf = new ClientConfig();
	private String genMktDataUrl = null;
	private JsonDeserializer<Date> cryptsyDate = null;
	
	private Client postClnt = null;
	private WebTarget wtPost = null;
	
	private EncryptionUtils enc = null; 
	private Gson gsonDe = null;

	/**
	 *     
	 * @param exch
	 */
	public CryptsyDaoImpl(CryptoCCYExchange exch) {
		super(exch);
		
		this.genMktDataUrl = exchange.getServerUrl() + "?" + _METHOD_GENMKTDATA;
		
		this.cryptsyDate = new JsonDeserializer <Date>() {
			public Date deserialize(JsonElement json, Type typeOfT,
					JsonDeserializationContext context)
					throws JsonParseException {
				Date out = null;
				try {
					String tmp = json.getAsString();
					out = DateTimeUtils.StringToDate01(tmp); 
				}
				catch (NumberFormatException nue) {
					applog.warn("\n ===!!!=== cant parse date, json input[{}] ===>", json.getAsString());
					nue.printStackTrace();
				}
				return out;
			}
			
		};
		
		
		this.postClnt = ClientBuilder.newClient();
		this.wtPost = postClnt.target(exchange.getServerSSLUrl());
		this.enc = new EncryptionUtils(exchange.getLogin().getPgpPrivate());
		this.gsonDe = new  GsonBuilder().registerTypeAdapter(Date.class, cryptsyDate).create();
	}


	public Collection<TradePairEnh> getAllTradePairs() {
		Gson gsDe = new  GsonBuilder().registerTypeAdapter(Date.class, cryptsyDate).create();
		String cStr = getGenMktDataAsString();
		CryptsyAllMarkets mkts = null;
		
		int cnt = 0;
		int cntMax = 10;
		do {
			try {
				mkts = gsDe.fromJson(cStr, CryptsyAllMarkets.class);
			} catch (Exception e) {
				applog.warn("===!!!=== Can't parse following String: ===>{}", cStr);
				e.printStackTrace();
			}
			cnt ++;
		} while (mkts == null && cnt < cntMax);
		
		return mkts.getAllTradePairs();	
	}


	private String getGenMktDataAsString() {
		Client cryptsyClient = ClientBuilder.newClient();
		WebTarget webTgt = cryptsyClient.target(genMktDataUrl);
		//ex:  WebTarget webTar = cryptsyClient.target("http://pubapi.cryptsy.com/api.php?method=marketdatav2");
		
		Invocation.Builder inc = webTgt.request(MediaType.TEXT_PLAIN_TYPE);
		int respStat = -1;
		String respTxt = "";
		int cntrTotal = 10;
		int cntr = 0;
		Response rs = null;
		
		while ( (respStat != 200 || respTxt.equals("")) && cntr < cntrTotal ) {
			rs =  inc.get();
			respStat = rs.getStatus();
			respTxt = rs.readEntity(String.class);
			cntr --;

			if (cntr > 0) {
				applog.info ("=== Retrying getGenMktDataAsString===>Statu:{}, Retry:{}/{}", respStat, cntr, cntrTotal);
			}

		}
		
		if (cntr ==0 ) {
			applog.warn ("===FATAL Error, max try reached!  getGenMktDataAsString===>Statu:{}, Retry:{}/{}", respStat, cntr, cntrTotal);
		}
		//applog.info("return status:==>{}", respStat);
		//applog.info("return text:===> {}", respTxt);
		return respTxt;
	}


	/**
	 *   go to exchange get the latest trading information for the trade pair
	 * @param tp
	 */
	public boolean updateTradePair(TradePair tp) {
		boolean rt = false;
		Invocation.Builder callUpdate = wtPost.request(MediaType.TEXT_PLAIN_TYPE);

		Map <String, String> req = new HashMap<String, String>();
		req.put("method", "depth");
		req.put("marketid", tp.getExternalId() );
		req.put("nonce", String.valueOf(getNonce()) );

		FormWrapper fmw = new FormWrapper(req);
		//applog.info("====query string ==>{}", fmw.getQueryString() );
		String sign = enc.getHmacSHA512(fmw.getQueryString());
		
		callUpdate.header("Key", exchange.getLogin().getPgpPub());
		callUpdate.header("Sign", sign);
		
		int rtSuccess = 0;
		int retry = 0;
		while (rtSuccess != 1  && retry < 5) {
			Response rs = callUpdate.post(Entity.entity(fmw.getForm(), MediaType.APPLICATION_FORM_URLENCODED_TYPE));
			CryptsyOrderDepth ordDep = null;
			String outStr=rs.readEntity(String.class);
			
			if (rs.getStatus() == 200) {
				//applog.info("==============> returned Stat:{}, value:{}", rs.getStatus(), outStr);
				try {
					ordDep = gsonDe.fromJson(outStr, CryptsyOrderDepth.class);
				}
				catch (Exception e) {
					applog.warn("===[!!!can't parse Cryptsy return!!!]===>Returned String:[{}]", outStr);
					e.printStackTrace();
					
				}
				rtSuccess = ordDep.getSuccess();
			}
			else {
				applog.warn("===[!!! error calling Cryptsy !!! return stat:[{}], call:[{}], return:[{}]", rs.getStatus(), fmw.getQueryString(), outStr);
			}
			
			if (rtSuccess == 1) {
				tp.setOrderBk(ordDep.convert2OrderBook());
				rt = true;
			}
			else {
				applog.warn("====> ERROR calling Cryptsy updateTradePair with [{}].  error:[{}].", fmw.getQueryString(), outStr);
				try {
					Thread.sleep( 1000 );
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//applog.info("========> out of json object is:[{}]", ordDep);
			retry ++;
		}
		
		return rt;
	}

	
	/**
	 *   get server info
	 * @param TradeServerInfo
	 */
	public TradeServerInfo getExchangeInfo () {
		Invocation.Builder callUpdate = wtPost.request(MediaType.TEXT_PLAIN_TYPE);
		TradeServerInfo tInfo = null;
		
		Map <String, String> req = new HashMap<String, String>();
		req.put("method", "getinfo");
		req.put("nonce", String.valueOf(getNonce()) );

		FormWrapper fmw = new FormWrapper(req);
		//applog.info("====query string ==>{}", fmw.getQueryString() );
		String sign = enc.getHmacSHA512(fmw.getQueryString());
		
		callUpdate.header("Key", exchange.getLogin().getPgpPub());
		callUpdate.header("Sign", sign);
		
		int rtSuccess = 0;
		int retry = 0;
		while (rtSuccess != 1 && retry < 5) {
			retry ++;
			Response rs = callUpdate.post(Entity.entity(fmw.getForm(), MediaType.APPLICATION_FORM_URLENCODED_TYPE));
			String outStr=rs.readEntity(String.class);
			applog.info("======getServerInfo========> returned Stat:{}, value:{}", rs.getStatus(), outStr);
	
			CryptsyServerInfo svrInfo = null;
			try {
				svrInfo = gsonDe.fromJson(outStr, CryptsyServerInfo.class);
			}
			catch (Exception e) {
				applog.warn("===[!!!can't parse Cryptsy return!!!]===>Returned String:[{}]", outStr);
				e.printStackTrace();
			}
			rtSuccess = svrInfo.getSuccess();
			
			if (rtSuccess == 1) {
				tInfo = svrInfo.transfer();
			}
			else {
				applog.warn("====> ERROR calling Cryptsy getServerInfo with [{}].  error:[{}].", fmw.getQueryString(), outStr);
				try {
					Thread.sleep( 1000 );
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return tInfo;
	}
	
	
	private static long getNonce() {
		++cryptsyCntr;
		return new Date().getTime() + cryptsyCntr;
	}
	

	public class FormWrapper {
		private Form fm = null;
		private String query = null;
		
		public FormWrapper (Map<String, String> inParam) {

			StringBuffer bf = new StringBuffer();
			this.fm = new Form();
			Set<String> paramKeys = inParam.keySet();
			
			for (String param : paramKeys ) {
				fm.param(param, inParam.get(param));
				if (bf.length() > 1) {
					bf.append("&");
				}
				bf.append(param).append("=").append(inParam.get(param));
			}

			this.query = bf.toString();
		}
		
		public String getQueryString () {
			return query;
		}
		
		
		public Form getForm () {
			return fm;
		}
	}
}