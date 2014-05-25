package com.agilebc.trading.sea.data;

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

import com.agilebc.data.trade.LinkedCoinElm;
import com.agilebc.data.trade.TradePair;
import com.agilebc.data.trade.TradePairEnh;
import com.agilebc.util.TradeType;
import com.agilebc.util.config.GenericConfigLoader;


public class SingleExchgArbiPoolTest {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(SingleExchgArbiPoolTest.class);

	public SingleExchgArbiPool seaPool = null;
	public SingleExchgArbiPoolTest () {
		setupCache();
	}
	
	public void setupCache() {
		ApplicationContext ctx =  new ClassPathXmlApplicationContext (GenericConfigLoader._SPRING_CONF);
		this.seaPool = (SingleExchgArbiPool) ctx.getBean("singleExchgArbiPool");
		seaPool.init();
	}
	
	
	@Test
	public void testSingleExchgArbiPool () {
		Collection <LinkedCoinElm> lks= seaPool.getAllTradePathHeads();
		int i=1;
		for (LinkedCoinElm lk: lks) {
			applog.info("==POOL ELM:{}/{}==>{}", i, lks.size(), lk.getChainStr());
			i++;
		}
		
		assertTrue(true);
	}
	
	
}
