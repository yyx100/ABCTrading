package com.agilebc.data.trade;

import java.util.HashMap;
import java.util.Map;

import com.agilebc.data.AbstractAgilebcData;
import com.agilebc.util.TradeType;

public class OrderBook extends AbstractAgilebcData {

	protected Map <TradeType, OrderDepth> orderbk = null;
	
	
	public OrderBook () {
		orderbk = new HashMap<TradeType, OrderDepth>(2);
		/*
		OrderDepth buy = new OrderDepth(TradeDirection.BUY);
		OrderDepth sell = new OrderDepth(TradeDirection.SELL);
		
		orderbk.put(TradeDirection.BUY, buy);
		orderbk.put(TradeDirection.SELL, sell);
		*/
	}
	
	
	public void addOrderDepth (OrderDepth depth) {
		orderbk.put(depth.getTradeDirection(), depth);
	}
	
	
	public OrderBookElm getBookElementAt(TradeType tDir, int i) {
		return orderbk.get(tDir).getBookElementAt(i);
	}
	
}
