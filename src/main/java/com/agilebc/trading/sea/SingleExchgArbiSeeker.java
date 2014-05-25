package com.agilebc.trading.sea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.agilebc.data.trade.Coin;
import com.agilebc.data.trade.ExchangeFee;
import com.agilebc.data.trade.LinkedCoinElm;
import com.agilebc.data.trade.OrderBook;
import com.agilebc.data.trade.OrderDepth;
import com.agilebc.data.trade.TradeGenUnitOrder;
import com.agilebc.data.trade.TradePair;
import com.agilebc.data.trade.TradePairEnh;
import com.agilebc.data.trade.TradeResult;
import com.agilebc.data.trade.TradeGenSetOrders;
import com.agilebc.data.trade.TradingCriteria;
import com.agilebc.data.trade.TradingBehavior;
import com.agilebc.trading.TradeCalculator;
import com.agilebc.trading.UnitTradeHandler;
import com.agilebc.trading.exception.TradeGenException;
import com.agilebc.trading.sea.data.SingleExchgArbiPool;
import com.agilebc.trading.services.MarketDataService;
import com.agilebc.util.TradeType;
import com.agilebc.util.config.GenericConfigLoader;


/**
 *   handles ONE pass of one strategy.  
 *   one instance of this per each executing thread
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
	private MarketDataService mktDataSrvc = null;
	
	//----  init ------
	private UnitTradeHandler[] tradeActions = null; 
	private int tradeCount = 0;
	//private TradeGenUnitOrder[] generatedOrders = null;
	private TradeResult[] trs = null;

	
	public SingleExchgArbiSeeker (LinkedCoinElm headTradePath, TradeGenUnitOrder startOrder, ExchangeFee fee, TradingCriteria tc, TradingBehavior tb, MarketDataService mktDtSrvc) {
		this.headTradePath  = headTradePath;
		this.startOrder = startOrder;
		this.fee = fee;
		this.tc = tc;
		this.tb = tb;
		this.mktDataSrvc = mktDtSrvc;
		init();
	}
	
	
	public void init () {
		int chainLen = headTradePath.getChainLength();
		this.tradeCount = chainLen - 1;
		
		tradeActions = new UnitTradeHandler [tradeCount]; 
		//generatedOrders = new TradeGenUnitOrder [tradeCount];
		trs = new TradeResult[tradeCount];

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
			
			TradePairEnh tpeh =  mktDataSrvc.getMarketDataTradePair(pairKey);
			mktDataSrvc.subscribe(tpeh.getTradePair());
			TradePair tp = mktDataSrvc.getOrderDepth(pairKey); 
					
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
	public TradeGenSetOrders seekingAlpha () throws TradeGenException {
		if (stratSimpleFill(startOrder.getQuantity())) {
			//---  is profitable ---
			double finalQt =trs[tradeCount - 1].getToCoinTotal(); 
			double earn =  finalQt - startOrder.getQuantity();
			
			if (earn > 0) {
				applog.info("$$$$$$$$$===SARESULT_GAIN: <+++++>:[{}]/[{}], instance:{}", earn, finalQt, headTradePath.getChainStr());
			}
			else {
				applog.info("#########===SARESULT_LOSS: <----->:[{}]/[{}], instance:{}", earn, finalQt,  headTradePath.getChainStr());
			}
		}
		
		return null;
		
	}
	
	
	/** 
	 *   
	 */
	public boolean stratSimpleFill (double tradeQuant) {
		boolean ans = false;
		
		TradePair tp = null;
		TradeType ty = null;
		OrderBook ob = null;
		
		for (int i=0; i< tradeCount; i++) {
			tp = tradeActions[i].getTradePair();
			ty = tradeActions[i].getTradeType();
			ob = mktDataSrvc.getOrderDepth(tp).getOrderBk();
			applog.info(">>>>>>executing {}/{} ==> [{}]^[{}]@[{}]", i, tradeCount, tradeActions[i].getTradePair().getPairId(), tradeActions[i].getTradeType().toString(), tp.getOrderBk().getBookElementAt(tradeActions[i].getTradeType(),1),  i);

			trs[i] = TradeCalculator.execWithCommission(ty, tradeQuant, ob, fee);
			if (trs[i].isOrderFillable()) {
				ans = true;
			}
			else {
				break;
			}
			
			tradeQuant = trs[i].getToCoinTotal();
		}
		
		return ans;
	}

	
	
	
	
	

	public TradeGenUnitOrder getInitialOrder() {
		return startOrder;
	}


	public void setInitialOrder(TradeGenUnitOrder initialOrder) {
		this.startOrder = initialOrder;
	}

}
