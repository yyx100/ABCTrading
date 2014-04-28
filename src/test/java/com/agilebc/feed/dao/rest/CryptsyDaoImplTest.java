package com.agilebc.feed.dao.rest;

import java.util.Collection;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.agilebc.data.config.CryptoCCYExchange;
import com.agilebc.data.trade.Coin;
import com.agilebc.data.trade.TradePair;
import com.agilebc.data.trade.TradePairEnh;
import com.agilebc.data.trade.TradeServerInfo;
import com.agilebc.util.config.GenericConfigLoader;

public class CryptsyDaoImplTest {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(CryptsyDaoImplTest.class);

	private CryptoCCYExchange exchg = null;
	
	
	public CryptsyDaoImplTest () {
		setupTest();
	}
	
	public void setupTest() {
		ApplicationContext ctx =  new ClassPathXmlApplicationContext (GenericConfigLoader._SPRING_CONF);

		this.exchg = (CryptoCCYExchange) ctx.getBean("cryptsyExchange");
		//TradePair test = new TradePair("3", new Coin("DUM", "DUMM!"), new Coin("XDA", "XHYD"));
		//CryptsyDaoImpl dao = new CryptsyDaoImpl(exchg);
		//dao.updateTradePair(test);
		
		//applog.info(" ========== Order Book is ============{}", test);
	}
	
	
	@Test
	public void singleMktTest () {
		CryptsyDaoImpl dao = new CryptsyDaoImpl(exchg);
		TradeServerInfo tinfo = dao.getExchangeInfo();
		applog.info(" ==== server information====>{}", tinfo);
		
		Collection<TradePairEnh> tps = dao.getAllTradePairs();
		for (TradePairEnh tp: tps) {
			applog.info("==tradepair==>{}", tp);
		}
	}
	
}
