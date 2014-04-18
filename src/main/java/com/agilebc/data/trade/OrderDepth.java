package com.agilebc.data.trade;

import com.agilebc.util.TradeType;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.map.TDoubleDoubleMap;
import gnu.trove.map.hash.TDoubleDoubleHashMap;

public class OrderDepth {

	private TradeType tradeDirection = null;
	private TDoubleDoubleMap orderDepth = new TDoubleDoubleHashMap();
	private TDoubleList priceRef = new TDoubleArrayList();
	private boolean sorted = false;
	
	public OrderDepth (TradeType tDir) {
		this.tradeDirection = tDir;
	}
	
	public void addOrder(double price, double quantity) {
		priceRef.add(price);
		orderDepth.adjustOrPutValue(price, quantity, quantity);
	}
	
	public void sort () {
		priceRef.sort();
	}
	
	
	
	/**
	 *   get the order book element object at specified index.  OrderBook 
	 * is guaranteed to be sorted buy order descending and sell order ascending.
	 * Thus, the first element of buy book will have the largest price, the first 
	 * element of sellbook will have the lowest price.  
	 * @param index  (1-n, where 1 is the first element, n is the last element)
	 * @return orderbook element
	 */
	public OrderBookElm getBookElementAt (int index) {
		if (! sorted) {
			sort();
		}
		
		int offsetIdx = 1;
		if (tradeDirection.getTradeTypeInt() > 0 ) {
			offsetIdx = priceRef.size() - index;
		}
		else {
			offsetIdx = index - offsetIdx;
		}
		
		double tgtPrice = priceRef.get(offsetIdx);
		 return new OrderBookElm(tgtPrice, orderDepth.get(tgtPrice));
	}

	public TradeType getTradeDirection() {
		return tradeDirection;
	}

	public void setTradeDirection(TradeType tradeDirection) {
		this.tradeDirection = tradeDirection;
	}
}
