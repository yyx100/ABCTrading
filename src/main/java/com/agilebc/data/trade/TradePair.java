package com.agilebc.data.trade;

import java.util.Date;

import com.agilebc.data.AbstractAgilebcData;
import com.agilebc.util.TradeType;

public class TradePair extends AbstractAgilebcData {

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
	private OrderBook orderBk = null;
	
	
	public TradePair (String externalId, Coin primary, Coin secondary ) {
		this.externalId = externalId;
		this.primary = primary;
		this.secondary = secondary;
		this.pairId = createPairId(primary, secondary);
	}
	
	
	public boolean containsCoin (Coin in) {
		return (primary.equals(in) || 
				secondary.equals(in));
	}
	
	
	public static String createPairId (Coin primCoin, Coin secdCoin) {
		StringBuffer mktPair = new StringBuffer(primCoin.getCoinSym()).append("|").append(secdCoin.getCoinSym());
		return mktPair.toString();
	}
	
	
	@Override
	public String toString() {
		StringBuffer bf = new StringBuffer("{").append(getPairId());
		OrderBook bk = getOrderBk();
		if (bk != null) {
			OrderBookElm buy = bk.getBookElementAt(TradeType.BUY, 1);
			OrderBookElm sell = bk.getBookElementAt(TradeType.SELL, 1);
			if (buy != null) {
				bf.append(": [BUY: ").append(buy.getPrice()).append(" X ").append(buy.getQuantity());
			}
			else {
				bf.append(": [BUY: ").append("-?-").append(" X ").append("-?-");
			}
			
			if (sell != null) {
				bf.append(" | SELL: ").append(sell.getPrice()).append(" X ").append(sell.getQuantity()).append("]");
			}
			else {
				bf.append(" | SELL: ").append("-?-").append(" X ").append("-?-").append("]");
			}
		}
		else {
			bf.append(": [!EMPTY!]");
		}
		
		return bf.append("}").toString();
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


	public OrderBook getOrderBk() {
		return orderBk;
	}


	public void setOrderBk(OrderBook orderBk) {
		this.orderBk = orderBk;
	}

	
}
