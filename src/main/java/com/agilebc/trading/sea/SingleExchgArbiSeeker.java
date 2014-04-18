package com.agilebc.trading.sea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.agilebc.data.trade.Coin;
import com.agilebc.data.trade.ExchangeFee;
import com.agilebc.data.trade.LinkedCoinElm;
import com.agilebc.data.trade.TradeGenUnitOrder;
import com.agilebc.data.trade.TradePair;
import com.agilebc.data.trade.TradeSignalSet;
import com.agilebc.data.trade.TradingCriteria;
import com.agilebc.data.trade.TradingBehavior;
import com.agilebc.trading.UnitTradeHandler;
import com.agilebc.trading.exception.TradeGenException;
import com.agilebc.trading.sea.data.SingleExchgArbiPool;
import com.agilebc.util.TradeType;
import com.agilebc.util.config.GenericConfigLoader;


/**
 *   handles ONE pass of one strategy.  
 *   
 *   NOTES TO MYSELF:  i decided to hard wire all trading criteria and strategy at constructor time to gain some performance.  
 *      
 * @author yyx100
 *
 */
public class SingleExchgArbiSeeker {
	public static Logger applog = LoggerFactory.getLogger(SingleExchgArbiSeeker.class);

	private LinkedCoinElm headTradePath = null;
	private ExchangeFee fee = null;
	private TradingCriteria tc = null;
	private TradingBehavior tb = null;
	private TradeGenUnitOrder startOrder = null;
	
	//----  init ------
	private UnitTradeHandler[] tradeActions = null; 
	private int tradeCount = 0;
	private TradeGenUnitOrder[] generatedOrders = null;
	
	private CacheManager abcCacheMan = null;
	private Cache trdPairCh	= null;
	
	public SingleExchgArbiSeeker (LinkedCoinElm headTradePath, TradeGenUnitOrder startOrder, ExchangeFee fee, TradingCriteria tc, TradingBehavior tb, CacheManager cacheMan) {
		this.headTradePath  = headTradePath;
		this.startOrder = startOrder;
		this.fee = fee;
		this.tc = tc;
		this.tb = tb;
		this.abcCacheMan = cacheMan;
		init();
	}
	
	
	public void init () {
		int chainLen = headTradePath.getChainLength();
		this.tradeCount = chainLen - 1;
		this.trdPairCh = abcCacheMan.getCache(GenericConfigLoader._CACHE_TRADEPAIR);
		
		tradeActions = new UnitTradeHandler [tradeCount]; 
		generatedOrders = new TradeGenUnitOrder [tradeCount];

		LinkedCoinElm next = headTradePath;
		for (int i=0; i<tradeCount; i++) {
			tradeActions[i] = new UnitTradeHandler();
			next = prepareTradePairs(next, tradeActions[i]);
		}
	
	}
	
	
	protected LinkedCoinElm prepareTradePairs (LinkedCoinElm currLnk, UnitTradeHandler act) {
		TradeType trdTy = currLnk.getNextAction();
		Coin primCoin = null;  
		Coin secdCoin = null;    

		
		act.setTradeType(trdTy);
		if (currLnk.hasNext()) {
			if ( trdTy.getTradeTypeInt() > 0 ) { //Buy direction
				// if from current to next coin is in buy direction,  then primary coin must be the next coin
				primCoin = currLnk.getNextCoinElm().getCurrCoin();
				secdCoin = currLnk.getCurrCoin();
			}
			else {
				// if from current to next coin is in sell direction, the primary coin must be the current coin
				primCoin = currLnk.getCurrCoin();
				secdCoin = currLnk.getNextCoinElm().getCurrCoin();
			}
			String pairKey = TradePair.createPairId(primCoin, secdCoin);
			TradePair tp =  trdPairCh.get(pairKey, TradePair.class);
			act.setTradePair(tp);
			
			return currLnk.getNextCoinElm();
		}
		
		return null;
	}
	
	
	
	/**
	 * 
	 * @param initOrder
	 * @return
	 * @throws TradeGenException
	 */
	public TradeSignalSet seekingAlpha () throws TradeGenException {
		 
		for (int i=0; i< tradeCount; i++) {
			//applog.info(">>>>>>executing [{}]^[{}]", tradeActions[i].getTradePair().getPairId(), tradeActions[i].getTradeType().toString());
		}
		
		return null;
		
	}


	public TradeGenUnitOrder getInitialOrder() {
		return startOrder;
	}


	public void setInitialOrder(TradeGenUnitOrder initialOrder) {
		this.startOrder = initialOrder;
	}


	public CacheManager getAbcCacheMan() {
		return abcCacheMan;
	}


	public void setAbcCacheMan(CacheManager abcCacheMan) {
		this.abcCacheMan = abcCacheMan;
	}
	

}
