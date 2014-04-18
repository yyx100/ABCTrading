package com.agilebc.cache;

import static org.junit.Assert.*;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.Configuration;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.agilebc.util.config.GenericConfigLoader;
import com.agilebc.util.config.SpringContextUtil;


public class CacheTest {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(CacheTest.class);

	public CacheManager manager1 = null;			

	public CacheTest () {
		setupCache();
	}
	
	public void setupCache() {
		ApplicationContext ctx =  new ClassPathXmlApplicationContext (GenericConfigLoader._SPRING_CONF);
		Configuration conf = new Configuration();
		manager1 = CacheManager.create(); //create("I:\\export\\GitRepo\\ABCTrading\\src\\main\\resources\\configuration\\ehcache\\abctrading-ehcache.xml");
		
	}
	
	
	@Test
	public void testMarktSubscription () {
		Cache ordCache = manager1.getCache("com.exch.cryptsy.orderdepth");
		Element ele = new Element("KEY", "YANG");
		//ordCache.put (ele);
		int i = 100;
		while (i> 0) {
			try {
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
