package com.agilebc.trading.services;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.agilebc.data.trade.TradePair;
import com.agilebc.data.trade.TradePairEnh;
import com.agilebc.util.TradeType;
import com.agilebc.util.config.GenericConfigLoader;


public class SeaMktDataServiceTest {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(SeaMktDataServiceTest.class);

	public MarketDataService srvc = null;
	public SeaMktDataServiceTest () {
		setupCache();
	}
	
	public void setupCache() {
		ApplicationContext ctx =  new ClassPathXmlApplicationContext (GenericConfigLoader._SPRING_CONF);
		this.srvc = (MarketDataService) ctx.getBean("seaMarketDataService");
	}
	
	
	@Test
	public void testMarktSubscription () {
		for (int i=1; i < 30; i++) {
			Collection<TradePairEnh> tpes = srvc.getAllMarketTradePairs();
			
			if (tpes != null) {
				int j = 1;
				for (TradePairEnh tpe: tpes) {
					//applog.info("===OUTPUT:{}-{}/{}==>{}", i, j, tpes.size(), tpe);
					
					if (tpe.getRecentTrades() != null && 
							tpe.getMostRecent() != null && 
							tpe.getMostRecent().getQuantity() > 0) {
						srvc.subscribe(tpe.getTradePair());
					}
					j++;
				}
			}
			else {
				applog.info("===OUTPUT:{} returns nothing!", i);
			}
			
			List<String> subs = srvc.getAllSubscribedKeys();
			if (subs != null) {
				int k = 1;
				for (String key: subs) {
					TradePair tp = srvc.getOrderDepth(srvc.getSubscribed(key));
					applog.info(".....from mktData LOOP:{}-{}/{}, {}<=>{}", i, k, subs.size(), key, tp );
					k++;
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		assertTrue(true);
	}
	
	
}
