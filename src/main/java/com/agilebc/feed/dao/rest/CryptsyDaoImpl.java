package com.agilebc.feed.dao.rest;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.data.config.CryptoCCYExchange;
import com.agilebc.data.proprietary.CryptsyAllMarkets;
import com.agilebc.data.trade.TradePair;
import com.agilebc.data.vo.Tradable;
import com.agilebc.feed.dao.CccyExchgDao;
import com.agilebc.feed.dao.ExchangeTradingAPI;
import com.agilebc.util.DateTimeUtils;
import com.agilebc.util.config.GenericConfigLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class CryptsyDaoImpl  extends CccyExchgDao  implements ExchangeTradingAPI {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(CryptsyDaoImpl.class);
	
	public final static String _METHOD_GENMKTDATA = "method=marketdatav2";
	private ClientConfig cconf = new ClientConfig();
	private String genMktDataUrl = null;
	private JsonDeserializer<Date> cryptsyDate = null;
	
	//inheriting CryptoCCYExchange exchange
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
	}


	public Collection<TradePair> getAllTradePairs() {
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




	

}