package com.agilebc.data.trade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.data.vo.WorkQueue;
import com.agilebc.util.TradeType;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.map.TDoubleDoubleMap;
import gnu.trove.map.hash.TDoubleDoubleHashMap;

public class OrderDepth {
	public static Logger applog = LoggerFactory.getLogger(OrderDepth.class);

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
		int psize = priceRef.size() -1;
		if (! sorted) {
			sort();
		}
		if (index < 1) { // added for dummy - when you mistake first element to be 0 instead of 1.
			index = 1;
		}
		
		int offsetIdx = 1;
		if (tradeDirection.getTradeTypeInt() > 0 ) {
			offsetIdx = priceRef.size() - index;
		}
		else {
			offsetIdx = index - offsetIdx;
		}
		
		if (psize > 0 && psize >= offsetIdx ) {
			//applog.info("=====OrderDepth fetching [{}] {}/{}", tradeDirection.getTradeTypeSym(), offsetIdx, psize);
			double tgtPrice = priceRef.get(offsetIdx);
			return new OrderBookElm(tgtPrice, orderDepth.get(tgtPrice));
		}
		else {
			//applog.warn("=====OrderDepth fetching [!EMPTY!] PRICE [{}] {}/{}", tradeDirection.getTradeTypeSym(), offsetIdx, psize);
		}
		
		return null;
	}

	public TradeType getTradeDirection() {
		return tradeDirection;
	}

	public void setTradeDirection(TradeType tradeDirection) {
		this.tradeDirection = tradeDirection;
	}
}
