package com.agilebc.cache;

import static org.junit.Assert.*;

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
import com.agilebc.util.TradeType;
import com.agilebc.util.config.GenericConfigLoader;


public class CacheTest {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(CacheTest.class);

	public EhCacheCacheManager manager1 = null;			

	public CacheTest () {
		setupCache();
	}
	
	public void setupCache() {
		ApplicationContext ctx =  new ClassPathXmlApplicationContext (GenericConfigLoader._SPRING_CONF);
		Configuration conf = new Configuration();
		manager1 = (EhCacheCacheManager) ctx.getBean("abcCacheMan"); //CacheManager.create();  //.create("I:\\export\\GitRepo\\ABCTrading\\src\\main\\resources\\configuration\\ehcache\\abctrading-ehcache.xml");
		
	}
	
	
	@Test
	public void testMarktSubscription () {
		Cache ordCache = manager1.getCache("com.exch.cryptsy.allmktpub");
		Cache sOrdCache = manager1.getCache("com.exch.cryptsy.orderdepth");
		
		AbcSelfMarketDataPopulator ordeh = (AbcSelfMarketDataPopulator) sOrdCache.getNativeCache();
		
		/*
		net.sf.ehcache.CacheManager realCM = manager1.getCacheManager();
		net.sf.ehcache.Cache cx1 =realCM.getCache("com.exch.cryptsy.orderdepth");
		*/
		
		Element ele = null; //new Element("KEY", "YANG");
		//ordCache.put (ele);
		int i = 100;
		while (i> 0) {
			try {
				if (ordeh != null) {
					List <String> keys = ordeh.getKeys();
					int j = 1;
					if (keys != null) {
						for (String key : keys) {
							TradePair tp = (TradePair) ordeh.get(key).getObjectValue();
							
							if (tp != null) {
								//applog.info(" .........Loop [{}] reading cache {}[{}/{}]", i, tp, j, keys.size());
							}
							else {
								applog.warn(" .....Loop [{}] reading cache DOESNOT contain element:[{}] !", i, key);
							}
							j++;
						}
					}
					applog.info(" .........cache:{} read [{}/{}] ", sOrdCache.getName(), j, keys.size());
				}
				else {
					applog.info("=============> nothing in  cache........");
					//ordCache = manager1.getCache("com.exch.cryptsy.orderdepth");
				}
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i--;
		}
		assertTrue(true);
	}
	
	
}
