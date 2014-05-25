package com.agilebc.data.trade;

import java.util.ArrayList;


/**
 *     trade result represents the toal value to trade to coin value, and the depth 
 *   expected to fullfill the order 
 * @author yyx100
 *
 */
public class TradeResult {
	private boolean orderFillable ;
	private double toCoinTotal ;
	private int depth ;
	private ArrayList<TradeElement> trades = new ArrayList<TradeElement>();
	
	public TradeResult () {
		
	}
	
	/**
	 * 
	 * @param orderFillable
	 * @param toCoinTotal
	 * @param depth  depth needed to fill the order (Start depth is 1)
	 */
	public TradeResult (boolean orderFillable, double toCoinTotal, int depth) {
		this.orderFillable = orderFillable;
		this.toCoinTotal = toCoinTotal;
		this.depth = depth;
	}

	
	public void setTradeResult (boolean orderFillable, double toCoinTotal, int depth) {
		this.orderFillable = orderFillable;
		this.toCoinTotal = toCoinTotal;
		this.depth = depth;
	}

	
	public void addTradeElement (double price, double quantity) {
		TradeElement telm = new TradeElement(price, quantity);
		trades.add(telm);
	}
	
	
	//-- auto gen ---
	public double getToCoinTotal() {
		return toCoinTotal;
	}

	public int getDepth() {
		return depth;
	}



	public boolean isOrderFillable() {
		return orderFillable;
	}

	public ArrayList<TradeElement> getTrades() {
		return trades;
	}

	public void setToCoinTotal(double toCoinTotal) {
		this.toCoinTotal = toCoinTotal;
	}
	
	
	
}
