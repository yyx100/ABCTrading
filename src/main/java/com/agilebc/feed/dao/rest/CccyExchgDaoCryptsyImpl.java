package com.agilebc.feed.dao.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.data.vo.Tradable;
import com.agilebc.feed.dao.CccyExchgDao;
import com.agilebc.util.config.GenericConfigLoader;

public class CccyExchgDaoCryptsyImpl extends CccyExchgDao  {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(CccyExchgDaoCryptsyImpl.class);
	
	
	@Override
	public void getOrderBook(Tradable tradePair) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAllOrderBooks() {
		
		ClientConfig cconf = new ClientConfig();
		
		Client cryptsyClient = ClientBuilder.newClient();
		//WebTarget webTgt = cryptsyClient.target(exchange.getServerUrl());
		WebTarget webTar = cryptsyClient.target("http://pubapi.cryptsy.com/api.php?method=marketdatav2");
		

		Invocation.Builder inc = webTar.request(MediaType.TEXT_PLAIN_TYPE);
		Response rs =  inc.get();
		int respStat = rs.getStatus();
		String respTxt = rs.readEntity(String.class);
		applog.info("return status:==>{}", respStat);
		applog.info("return text:===> {}", respTxt);
		
		return respTxt;
	}

	

	public static void main(String[] argv) {
		CccyExchgDaoCryptsyImpl test = new CccyExchgDaoCryptsyImpl();
		test.getAllOrderBooks();
	}
}