package com.agilebc.data.trade;

import java.util.Date;

import com.agilebc.util.DateTimeUtils;


public class TradeElement {
	private long tradeId = -1L;
	private String extTradeId = null;
	
	private double price 	= 0;
	private double quantity	= 0;
	private double total	= 0;
	
	private Date execTime = null;
	
	public TradeElement (String extId, double price, double quant, double total, Date execTime) {
		this.extTradeId = extId;
		this.price = price;
		this.quantity = quant;
		this.total = total;
		this.execTime = execTime;
	}
	
	
	public String toString() {
		StringBuffer bf = new StringBuffer(String.valueOf(tradeId));
		bf.append("/").append(extTradeId);
		
		bf.append("@").append(price).append("X").append(quantity).append("[").append(DateTimeUtils.DateToString01(execTime)).append("]");
		return bf.toString();
	}
	
	//--- auto generated ---
	public long getTradeId() {
		return tradeId;
	}
	public void setTradeId(long tradeId) {
		this.tradeId = tradeId;
	}
	public String getExtTradeId() {
		return extTradeId;
	}
	public void setExtTradeId(String extTradeId) {
		this.extTradeId = extTradeId;
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
	public Date getExecTime() {
		return execTime;
	}
	public void setExecTime(Date execTime) {
		this.execTime = execTime;
	}

	
	
	
}
