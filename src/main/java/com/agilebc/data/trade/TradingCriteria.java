package com.agilebc.data.trade;

import java.util.HashMap;
import java.util.HashSet;


public class TradingCriteria {

	/** 0=no limit; normalized to bitcoin equivalence*/
	private double minTradeVolume = 0;	
	
	/** 0=no limit; normalized to USD */
	private double minTradePrice = 0;  	
	
	/** after this value risk level will be too high to accept (default to 6) */
	private int maxLinkLength = 6; 

	/** 
	 * 0=no buffer check.  To reduce risk after getting price at optimum order depth, 
	 * check with price of beyond order depth to see if still profitable. 
	 */
	private int orderDepthBuffer = 0;   
	
	/**
	 *  LTC,BTC,DOGE , delimited
	 */
	private String allowedCoin = null;
	private boolean allowedDone = false;
	private HashSet<Coin> allowedHash = null;
	
	
	public synchronized boolean isAllowed (Coin cnk) {
		boolean ans = false;

		if (allowedCoin == null || "".equals(allowedCoin)) {
			ans = true;
		}
		else {
			if (!allowedDone) {
				parseAllowedCoins();
			}
	
			ans = allowedHash.contains(cnk);
		}
		return ans;
	}
	
	
	public synchronized void parseAllowedCoins () {
		String[] tks = allowedCoin.toUpperCase().split(",");
		allowedHash = new HashSet<Coin>(tks.length);
		for (String tk : tks) {
			allowedHash.add(new Coin(tk.trim(), ""));
		}
	}
	
	//--- auto generated ---
	
	public double getMinTradeVolume() {
		return minTradeVolume;
	}
	public void setMinTradeVolume(double minTradeVolume) {
		this.minTradeVolume = minTradeVolume;
	}
	public double getMinTradePrice() {
		return minTradePrice;
	}
	public void setMinTradePrice(double minTradePrice) {
		this.minTradePrice = minTradePrice;
	}
	public int getMaxLinkLength() {
		return maxLinkLength;
	}
	public void setMaxLinkLength(int maxLinkLength) {
		this.maxLinkLength = maxLinkLength;
	}
	
	
	public int getOrderDepthBuffer() {
		return orderDepthBuffer;
	}
	
	public void setOrderDepthBuffer(int orderDepthBuffer) {
		this.orderDepthBuffer = orderDepthBuffer;
	}
	public String getAllowedCoin() {
		return allowedCoin;
	}
	public void setAllowedCoin(String allowedCoin) {
		this.allowedCoin = allowedCoin;
	}
	
	
	
	
	
}
