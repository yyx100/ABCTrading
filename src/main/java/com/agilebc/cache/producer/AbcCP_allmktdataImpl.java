package com.agilebc.cache.producer;

import java.util.Collection;
import java.util.List;

import com.agilebc.data.trade.TradePair;
import com.agilebc.data.trade.TradePairEnh;
import com.agilebc.feed.dao.ExchangeTradingAPI;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class AbcCP_allmktdataImpl extends AbcCacheProducer {

	public void preparQueue() {

	}
	
	@Override
	public void  produceCacheElements() {
		synchronized (workQueue) { //for all markets, we either update all or nothing
			applog.info("===produceCacheElement ALL get called!=====>");
			ExchangeTradingAPI tradeApi = (ExchangeTradingAPI) getCtx().getBean(exchgDaoName);
			Collection<TradePairEnh> tps = tradeApi.getAllTradePairs();
			
			for (TradePairEnh tp: tps ) {
				Element ele = new Element(tp.getTradePair().getPairId(), tp);
				underlyingCache.put(ele);
			}
		}
	}

	
	@Override
	public Object produceCacheElement(Object key) {
		TradePairEnh rt = null;
		synchronized (workQueue) {
			applog.info("===produceCacheElement SINGLE get called!=====>");
			if (underlyingCache.getKeys() == null || underlyingCache.getKeys().size() < 1) {
				applog.info("===produceCacheElement SINGLE>>>ALL get called!=====>");
				produceCacheElements();
			}
			
			Element elm = underlyingCache.getQuiet(key);
			if (elm != null) {
				rt = (TradePairEnh) elm.getObjectValue();
			}
		}
		
		return rt;
	}

}
