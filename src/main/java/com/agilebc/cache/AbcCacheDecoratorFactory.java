package com.agilebc.cache;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.constructs.CacheDecoratorFactory;

public class AbcCacheDecoratorFactory  extends CacheDecoratorFactory {

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
		AbcSelfMarketDataPopulator abcSelf = new AbcSelfMarketDataPopulator(cache, cacheEnt);
		
		//ThreadFactory tf = new ThreadFactoryBuilder().setNameFormat("selfwkr_%d").setDaemon(true).build();
		ThreadFactory tf = new ThreadFactoryBuilder().setNameFormat("selfwkr_%d").build();
		
		this.refreshRate = Integer.parseInt( (String) properties.get("refresh") );
		this.concurrentThreads = Integer.parseInt( (String) properties.get("threads") );

		this.exesrvc = Executors.newFixedThreadPool(concurrentThreads, tf);
		for (int i=0; i<concurrentThreads; i++) {
			exesrvc.execute(abcSelf);
		}
		
		
		return abcSelf;
	}

}
