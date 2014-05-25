package com.agilebc.data.trade;

import com.agilebc.util.TradeType;

/**
 *   for performance, this object is NOT thread safe
 *   this object should be used by one thread
 *   this object should be re-used per each strategy set (per each LinedCoin set in one thread) 
 * 
 * @author yyx100
 *
 */
public class TradeGenUnitOrder {


	private String tradePairId 		= null;
	private TradeType tradeType 	= null;
	
	private double price 	= 0;
	private double quantity	= 0;

	private double total	= 0;
	private int orderBookDepth = 0;
	
	
	public void reset () {
		this.price 			= 0;
		this.quantity 		= 0;
		this.total 			= 0;
		this.orderBookDepth = 0;
	}
	
	public String toString() {
		StringBuffer bf = new StringBuffer();
		if (tradePairId != null) {
			bf.append(tradePairId);
		}
		bf.append(":");
		
		if (tradeType != null) {
			bf.append(tradeType.getTradeTypeSym());
		}
		
		bf.append("@").append(price).append("X").append(quantity);
		return bf.toString();
	}
	
	public String getTradePairId() {
		return tradePairId;
	}
	public void setTradePairId(String tradePairId) {
		this.tradePairId = tradePairId;
	}
	public TradeType getTradeType() {
		return tradeType;
	}
	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getOrderBookDepth() {
		return orderBookDepth;
	}
	public void setOrderBookDepth(int orderBookDepth) {
		this.orderBookDepth = orderBookDepth;
	}
	

}
