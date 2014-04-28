package com.agilebc.cache;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.agilebc.cache.producer.AbcCacheProducer;
import com.agilebc.data.trade.TradePair;
import com.agilebc.data.vo.WorkQueue;
import com.agilebc.util.MarketDataServiceUtils;
import com.agilebc.util.config.GenericConfigLoader;

public class AbcSelfMarketDataPopulator extends SelfPopulatingCache implements Runnable {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(AbcSelfMarketDataPopulator.class);
	
	private String exchName = null;
	private String cacheApiName = null;
	private String exchDao = null;
	private String cacheName = null;
	private int refreshRate = 5000;

	private final WorkQueue<TradePair> workQueue = new WorkQueue<TradePair>(); // need an instance variable taht will be shared among threads.
	
	public AbcSelfMarketDataPopulator(Ehcache cache, CacheEntryFactory factory, int refreshRate)
			throws CacheException {
		super(cache, factory);

		this.cacheName = cache.getName();
		this.exchName = MarketDataServiceUtils.getCacheExchange(cacheName);
		this.cacheApiName = MarketDataServiceUtils.getCacheApiName(cacheName);
		this.exchDao = exchName + "Dao";
		this.refreshRate = refreshRate;
	}

	
	public void run() {
		String tName = Thread.currentThread().getName();
		long tId = Thread.currentThread().getId();
		applog.info("==== Exchange:{} <=> Market Data self popultor starts for thread ID{} ({})", exchName, tId, tName);

		AbcCacheProducer dataPrd = getCacheProducer();
		int i = 0;
		while (true && appConf.getOptStat().isRunning() ) {
			try {
				long st = System.currentTimeMillis();
				dataPrd.produceCacheElements();
				long end = System.currentTimeMillis();
				
				long rst= refreshRate - end + st;
				long elap= (end - st) / 1000; //Elapse time in seconds
				if (rst > 0) {
					applog.info("===== {} FULL CYCLE [{}] is done. Total Time=[{}], sleeping={}",cacheApiName, i, elap, rst ) ;
					Thread.sleep(rst);
				}
				else {
					applog.info("===== {} FULL CYCLE [{}] is done. Total Time=[{}], NO SLEEP",cacheApiName, i, elap ) ;
				}
				
				i++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	public AbcCacheProducer getCacheProducer () {
		AbcCacheProducer dataPrd = null;
		try {
			StringBuffer bf = new StringBuffer("com.agilebc.cache.producer.AbcCP_");
			bf.append(cacheApiName).append("Impl");
			Class dataProducerCl = Class.forName(bf.toString());
			dataPrd = (AbcCacheProducer) dataProducerCl.newInstance();
			
			dataPrd.setUnderlyingCache(underlyingCache);
			dataPrd.setCacheName(cacheApiName);
			dataPrd.setExchgName(exchName);
			dataPrd.setWorkQueue(workQueue);
			
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return dataPrd;
	}


}
