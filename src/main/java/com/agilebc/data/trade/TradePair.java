package com.agilebc.data.trade;

import java.util.Date;

import com.agilebc.data.agilebcdata;

public class TradePair extends agilebcdata {

	/**
	 * internal ID for trade pair.  ID is composed of: PRIMARY_CURR|SECONDARY_CURR
	 */
	private String pairId 	= null;  // PRIMARY_CURR|SECONDARY_CURR
	private String externalId = null; //
	
	private Coin primary 	= null;
	private Coin secondary 	= null;
	
	private double lastPrice = 0;
	private double volume = 0;
	private Date lastTradeTime = null;
	
	public TradePair (String pairId, String externalId, Coin primary, Coin secondary ) {
		this.pairId = pairId;
		this.externalId = externalId;
		this.primary = primary;
		this.secondary = secondary;
	}
	
	
	public boolean containsCoin (Coin in) {
		return (primary.equals(in) || 
				secondary.equals(in));
	}
	
	
	
	
	public String getPairId() {
		return pairId;
	}
	public void setPairId(String pairId) {
		this.pairId = pairId;
	}
	public Coin getPrimary() {
		return primary;
	}
	public void setPrimary(Coin primary) {
		this.primary = primary;
	}
	public Coin getSecondary() {
		return secondary;
	}
	public void setSecondary(Coin secondary) {
		this.secondary = secondary;
	}
	public double getLastPrice() {
		return lastPrice;
	}
	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	public Date getLastTradeTime() {
		return lastTradeTime;
	}
	public void setLastTradeTime(Date lastTradeTime) {
		this.lastTradeTime = lastTradeTime;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	
}
