package com.agilebc.trading.services;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.agilebc.cache.AbcSelfMarketDataPopulator;
import com.agilebc.data.config.CryptoCCYExchange;
import com.agilebc.data.trade.TradePair;
import com.agilebc.data.trade.TradePairEnh;
import com.agilebc.util.MarketDataServiceUtils;
import com.agilebc.util.config.GenericConfigLoader;


/**
 * 
 * 

 *    most market data service design is a bus architecture that is based on a messaging service such as EMS or ActiveMq.
 *  My design is slightly different.  It still use bus architecture, but I chose to use a memory cache as bus.  
 *  In theory, I believe in memory cache will be faster than messages.         
 * 
 *	  This market data service provides market data api to one exchange.  
 *  It is plain java pojo api to internal apps. It wraps around the Ehcache (used as the bus) for accessing market 
 *  data in exchange.
 *  
 *  //TODO  I have NOT benched the performance using Ehcache vs ActiveMq.  
 *  @author yyx100
 */
public class MarketDataService {
	public final static String _cnameOrderDepth = "orderdepth";
	public final static String _cnameAllmktdata = "allmktdata";

	private CryptoCCYExchange exeExchg = null; //execution exchange
	private CacheManager serviceCacheMan = null;

	private String exchgName = null;

	private Cache subscribedCache = null;
	private AbcSelfMarketDataPopulator allMarketCache = null;
	private AbcSelfMarketDataPopulator orderDepthCache = null;

	
	/**
	 * @param exchg  
	 * @param cachMan
	 */
	public MarketDataService (CryptoCCYExchange exchg, CacheManager cachMan) {
		this.exeExchg = exchg;
		this.serviceCacheMan = cachMan;
		
		this.exchgName = exeExchg.getExchName();
		this.subscribedCache = serviceCacheMan.getCache(GenericConfigLoader._ABC_SUBSCRIBED);
		
		String allMktFullName = MarketDataServiceUtils.getCacheFullName(exchgName, _cnameAllmktdata);
		Cache spAllMktDt = serviceCacheMan.getCache(allMktFullName);
		this.allMarketCache		= (AbcSelfMarketDataPopulator) spAllMktDt.getNativeCache();
		
		String ordDepFullName = MarketDataServiceUtils.getCacheFullName(exchgName, _cnameOrderDepth);
		Cache spOrdDepth = serviceCacheMan.getCache(ordDepFullName);
		this.orderDepthCache 	= (AbcSelfMarketDataPopulator) spOrdDepth.getNativeCache();
	}
	
	
	/**
	 * 
	 * @param pairId internal pairId is in the format of:
	 *   primaryCoin|secondaryCoin ex: "LTC|BTC". 
	 * @return if the traidePair is subscribed 
	 */
	public boolean subscribe (String pairId)  {
		boolean ans = false;

		if (subscribedCache.get(pairId) == null) {
			TradePair dummy = new TradePair(pairId, null, null);
			subscribedCache.put(pairId, dummy);
			
			ans=true;
		}
		
		return ans;
	}
	
	
	public List<String> getAllSubscribedKeys() {
		Ehcache subEh = (Ehcache) subscribedCache.getNativeCache();
		return subEh.getKeys();
	}
	
	public TradePair getSubscribed(String pairId) {
		return subscribedCache.get(pairId, TradePair.class);
	}
	
	
	/**
	 * 
	 * @param trdepair
	 * @return if the traidePair is subscribed 
	 */
	public boolean subscribe (TradePair tp)  {
		boolean ans = false;

		if (tp != null && subscribedCache.get(tp.getPairId()) == null) {
			subscribedCache.put(tp.getPairId(), tp);
			ans=true;
		}
		
		return ans;
	}
	
	
	public TradePair getOrderDepth(TradePair tp) {
		return getOrderDepth(tp.getPairId());
	}
	
	public TradePair getOrderDepth(String pairId) {
		TradePair tp = null;
		Element elm = orderDepthCache.get(pairId);
		if (elm != null) {
			tp = (TradePair) elm.getObjectValue();
		}
		
		return tp;
	}
	
	
	public Collection<TradePairEnh> getAllMarketTradePairs () {
		Collection<TradePairEnh> out = null;
		allMarketCache.get("LTC|BTC"); //a dummy call to ensure blocking call will populate the initial cache 
		List<String> keys = allMarketCache.getKeys();
		
		if (keys != null && keys.size() > 0) {
			out = new ArrayList<TradePairEnh>(keys.size());
			for (String pairId : keys) {
				Element elm = allMarketCache.get(pairId);
				out.add((TradePairEnh) elm.getObjectValue());
			}
		}
		
		return out;
	}


	public TradePairEnh getMarketDataTradePair (String pairId) {
		Element elm = allMarketCache.get(pairId); //a dummy call to ensure blocking call will populate the initial cache
		return (TradePairEnh) elm.getObjectValue();
	}
	//--- auto generated ---
	
}
