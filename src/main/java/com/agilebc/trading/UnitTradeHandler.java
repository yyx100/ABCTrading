package com.agilebc.trading;

import com.agilebc.data.trade.TradePair;
import com.agilebc.util.TradeType;



/**
 *    conducts one trade for each coin pair.  this class is designed for performance.  
 *  NOT thread safe.  Should be reused within on thread
 *  
 * @author yyx100
 *
 */
public class UnitTradeHandler {

	protected TradePair tradePair = null;
	protected TradeType tradeType 	= null;
	
	
	public TradePair getTradePair() {
		return tradePair;
	}
	public void setTradePair(TradePair tradePair) {
		this.tradePair = tradePair;
	}
	public TradeType getTradeType() {
		return tradeType;
	}
	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}
	
	
	
	

}
