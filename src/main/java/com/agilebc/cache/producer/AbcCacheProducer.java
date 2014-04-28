package com.agilebc.cache.producer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.agilebc.cache.AbcSelfMarketDataPopulator;
import com.agilebc.data.trade.TradePair;
import com.agilebc.data.vo.WorkQueue;
import com.agilebc.util.config.GenericConfigLoader;
import com.agilebc.util.config.SpringContextUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 *   this is an abstract class.  real implementation is depended on the !!!cache name!!!.
 *   
 *   for example:  cache name of com.exch.cryptsy.orderdepth will have
 *   AbcCP_orderdepthImpl.java 
 * @author yyx100
 *
 */
public abstract class AbcCacheProducer {
	
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(AbcCacheProducer.class);
	
	protected final static String _subsCacheName = GenericConfigLoader._ABC_SUBSCRIBED; 

	protected Ehcache underlyingCache = null;
	protected ApplicationContext ctx;
	protected String cacheName = null;
	protected String exchgName = null;
	protected String exchgDaoName = null;
	
	protected Cache subCache = null;
	protected WorkQueue<TradePair> workQueue = null;
	
	public abstract void preparQueue(); 
	public abstract void produceCacheElements();
	public abstract Object produceCacheElement (Object key); 
	

	
	
	

	public String getCacheName() {
		return cacheName;
	}


	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}


	public String getExchgName() {
		return exchgName;
	}


	public void setExchgName(String exchgName) {
		this.exchgName = exchgName;
		this.exchgDaoName = exchgName + "Dao";
	}





	public ApplicationContext getCtx() {
		if (ctx == null) {
			ctx = SpringContextUtil.getSpringContext();
		}
		
		return ctx;
	}





	public void setCtx(ApplicationContext ctx) {
		this.ctx = ctx;
	}





	public String getExchgDaoName() {
		return exchgDaoName;
	}





	public Ehcache getUnderlyingCache() {
		return underlyingCache;
	}





	public void setUnderlyingCache(Ehcache underlyingCache) {
		this.underlyingCache = underlyingCache;
	}


	public WorkQueue<TradePair> getWorkQueue() {
		return workQueue;
	}


	public void setWorkQueue(WorkQueue<TradePair> workQueue) {
		this.workQueue = workQueue;
	}
	
	
}
