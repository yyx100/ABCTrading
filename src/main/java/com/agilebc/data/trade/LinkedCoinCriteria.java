package com.agilebc.data.trade;


public class LinkedCoinCriteria {

	private Double minTradeVolume = null;	//normalized to bitcoin volume 
	private Double minTradePrice = null;  	//normalized to USD 
	private int maxLinkLength = 6; //default to 20 
	
	
	
	public Double getMinTradeVolume() {
		return minTradeVolume;
	}
	public void setMinTradeVolume(Double minTradeVolume) {
		this.minTradeVolume = minTradeVolume;
	}
	public Double getMinTradePrice() {
		return minTradePrice;
	}
	public void setMinTradePrice(Double minTradePrice) {
		this.minTradePrice = minTradePrice;
	}
	public int getMaxLinkLength() {
		return maxLinkLength;
	}
	public void setMaxLinkLength(int maxLinkLength) {
		this.maxLinkLength = maxLinkLength;
	}
	
	
	
	
	
}
