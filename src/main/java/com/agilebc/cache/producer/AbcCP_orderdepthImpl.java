package com.agilebc.cache.producer;

import java.util.List;

import net.sf.ehcache.Element;

import com.agilebc.data.trade.TradePair;
import com.agilebc.feed.dao.ExchangeTradingAPI;
import com.agilebc.util.config.GenericConfigLoader;

public class AbcCP_orderdepthImpl extends AbcCacheProducer {
	
	private ExchangeTradingAPI tradeApi = null;
	private int prePt = 0;
	private int curPt = 0;
	
	
	public AbcCP_orderdepthImpl () {
	}
	
	public void preparQueue() {
		if (underlyingCache != null && subCache == null) {
			this.subCache = underlyingCache.getCacheManager().getCache(_subsCacheName);
		}
		List<String> ckeys =  null;
		
		while (ckeys == null || ckeys.size() ==0) {
			ckeys = subCache.getKeys();

			for(String ckey: ckeys) {
				Element elm = subCache.get(ckey);
				workQueue.addWrok((TradePair) elm.getObjectValue());
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void  produceCacheElements() {
		if (tradeApi == null) {
			this.tradeApi = (ExchangeTradingAPI) getCtx().getBean(exchgDaoName);
		}
		
		if (workQueue.maintenanceNeed()) {
			applog.info("====Starting work queue maintainence (detected new subscriptions)====>");
			preparQueue();
		}

		TradePair tp = null;
		boolean cont = true;
		while (cont) { //i only want to do one loop at time.  if pointer circles back to 0, stop.
			curPt = workQueue.getCurrentPt();
			tp = workQueue.getNextJob();
			if (curPt < prePt) {
				cont = false;
			}
			
			produceCacheElementTP(tp);
			//applog.info(" ====running for price ==={}/{}_{} status:{} continue:{}", curPt, prePt, workQueue.getMaxInd(), (rt != null), cont );
		}
	
	}

	
	public Object produceCacheElement (Object key) {
		if (underlyingCache != null && subCache == null) {
			this.subCache = underlyingCache.getCacheManager().getCache(_subsCacheName);
		}
		
		Element tmp = subCache.get(key);
		TradePair tp = (TradePair) tmp.getObjectValue();
		
		return produceCacheElementTP(tp);
	}
	
	
	private Object produceCacheElementTP (TradePair key) {
		if (tradeApi == null) {
			this.tradeApi = (ExchangeTradingAPI) getCtx().getBean(exchgDaoName);
		}
		
		boolean procStat = false;
		TradePair tp = null;
		
		if (key != null) {
			tp = (TradePair) key;
			procStat = tradeApi.updateTradePair(tp);
			//applog.info("=====updating Tradepair==>{}", tp );
			if (procStat) {
				underlyingCache.put(new Element(tp.getPairId(), tp));
			}
			else {
				applog.warn("===!!ERROR!!: not able to update:{}", tp);
			}
			prePt = curPt;
		}
		else {
			try {
				Thread.sleep(GenericConfigLoader._DEFAULT_RETRY_SLEEP);
				applog.warn("===== NO WORK in work_queue, sleeping for{} miliseconds.", GenericConfigLoader._DEFAULT_RETRY_SLEEP);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (procStat)
			return tp;
		else 
			return null;
	}
}
