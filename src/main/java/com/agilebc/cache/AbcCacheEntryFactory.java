package com.agilebc.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.cache.producer.AbcCacheProducer;
import com.agilebc.util.config.GenericConfigLoader;

import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import net.sf.ehcache.constructs.blocking.UpdatingCacheEntryFactory;

public class AbcCacheEntryFactory implements CacheEntryFactory,
		UpdatingCacheEntryFactory {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(AbcCacheEntryFactory.class);

	private AbcCacheProducer selfProducer = null;
	
	public Object createEntry(Object key) throws Exception {
		applog.info("=========== blocking create called {}=====>", key);
		return selfProducer.produceCacheElement(key);
	}
	
	
	public void updateEntryValue(Object key, Object value) throws Exception {
		applog.info("=========== blocking update called {}=====>", key);
	}


	public void setSelfProducer(AbcCacheProducer selfProducer) {
		this.selfProducer = selfProducer;
	}

	
}
