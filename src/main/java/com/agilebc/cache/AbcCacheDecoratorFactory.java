package com.agilebc.cache;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.cache.producer.AbcCacheProducer;
import com.agilebc.util.MarketDataServiceUtils;
import com.agilebc.util.config.GenericConfigLoader;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.constructs.CacheDecoratorFactory;

public class AbcCacheDecoratorFactory  extends CacheDecoratorFactory {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(AbcCacheDecoratorFactory.class);
	
	
	protected ExecutorService exesrvc = null;
	protected String exchange = null;
	protected int concurrentThreads = 1;
	protected int refreshRate = 1000;
	
	@Override
	public Ehcache createDecoratedEhcache(Ehcache cache, Properties properties) {
		return createDefaultDecoratedEhcache(cache, properties);
	}

	@Override
	public Ehcache createDefaultDecoratedEhcache(Ehcache cache, Properties properties) {
		
		AbcCacheEntryFactory cacheEnt = new AbcCacheEntryFactory();
	
		String cn = MarketDataServiceUtils.getCacheApiName(cache.getName());
		ThreadFactory tf = new ThreadFactoryBuilder().setNameFormat(cn + "_wkr_%d").setDaemon(true).build();
		
		this.refreshRate = Integer.parseInt( (String) properties.get("refresh") );
		this.concurrentThreads = Integer.parseInt( (String) properties.get("threads") );
		
		AbcSelfMarketDataPopulator abcSelf = new AbcSelfMarketDataPopulator(cache, cacheEnt, refreshRate);
		cacheEnt.setSelfProducer(abcSelf.getCacheProducer());
		
		this.exesrvc = Executors.newFixedThreadPool(concurrentThreads, tf);
		for (int i=0; i<concurrentThreads; i++) {
			exesrvc.execute(abcSelf);
		}
		
		return abcSelf;
	}

}
