package com.agilebc.data.trade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.data.vo.WorkQueue;
import com.agilebc.util.TradeType;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.map.TDoubleDoubleMap;
import gnu.trove.map.hash.TDoubleDoubleHashMap;

/**
 *   a data container that represent orders of a either buy or sell.    
 * @author yyx100
 *
 */
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
		this.sorted = true;
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
	
	
	/**
	 *   in direction: (buy order, loop through sell books for match)
	 * @param inQuant  total secondary coin amount
	 * @return traderesult will be total primary coin can be bought. 
	 */
	protected TradeResult computeIn (double inQuant ) {
		if (! sorted) {
			sort();
		}
		
		boolean ans = false;
		
		int dep = priceRef.size();
		int i = 0;
		
		double total = 0;
		
		double atPrice = 0;
		double atQuant = 0;
		double subTot = 0;
		
		double leftTot = inQuant;
		TradeResult tr = new TradeResult();
		
		while (i<dep && ans == false) {
			atPrice = priceRef.get(i); 
			atQuant =  orderDepth.get(atPrice);
			subTot = atPrice * atQuant;
			
			if (subTot >= leftTot) {
				atQuant = leftTot / atPrice;
				ans = true;
			}
			tr.addTradeElement(atPrice, atQuant);
			
			total += atQuant;
			leftTot -= subTot;
			i++;
		}
		tr.setTradeResult(ans, total, i);
		
		return tr;
	}
	
	
	/**
	 *   out direction: (sell order, loop through buy books for match)
	 * @param inQuant  total primary coin amount
	 * @param tr  traderesult for out direction will be total secondary coin can be obtained. 
	 * @return if order can be filled
	 */
	protected TradeResult computeOut (double inQuant) {
		if (! sorted) {
			sort();
		}
		
		boolean ans = false;
		
		int dep = priceRef.size();
		int i = dep - 1;
		
		double total = 0;
		
		double atPrice = 0;
		double atQuant = 0;
		double subTot = 0;
		
		double leftTot = inQuant;
		TradeResult tr = new TradeResult();
		
		while (i > -1 && ans == false) {
			atPrice = priceRef.get(i); 
			atQuant =  orderDepth.get(atPrice);
			
			if (atQuant >= leftTot) {
				subTot = leftTot * atPrice;
				atQuant = leftTot;
				ans = true;
			}
			else {
				subTot = atPrice * atQuant;
			}
			tr.addTradeElement(atPrice, atQuant);
			
			total += subTot;
			leftTot -= atQuant;
			i--;
		}
		
		i = dep - i - 1; // buy order is stored in asending orde too, reverse it to be more natual buy decending diection
		tr.setTradeResult(ans, total, i);
		
		return tr;
	}
	

	
	//-------------- auto gen --------
	public TradeType getTradeDirection() {
		return tradeDirection;
	}

	public void setTradeDirection(TradeType tradeDirection) {
		this.tradeDirection = tradeDirection;
	}
}
