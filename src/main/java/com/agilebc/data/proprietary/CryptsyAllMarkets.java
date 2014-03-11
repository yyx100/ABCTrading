package com.agilebc.data.proprietary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.agilebc.data.agilebcdata;
import com.agilebc.data.trade.Coin;
import com.agilebc.data.trade.TradePair;
import com.agilebc.data.trade.LinkedCoinCriteria;
import com.agilebc.data.trade.UnifiedMarketData;
import com.google.gson.annotations.SerializedName;

public class CryptsyAllMarkets extends agilebcdata implements UnifiedMarketData {
	Pattern cpsyDeliP = Pattern.compile("\\/");

	
	private int success = -1;
	
	@SerializedName("return")
	private ReturnSet returnSet = null;
	
	
	public Collection<TradePair> getAllTradePairs() {
		Collection< TradePair> rt = null;
		
		if (returnSet != null) {
			HashMap<String, Market> mkts = returnSet.getMarkets();
			if (mkts != null) {
				rt = new HashSet<TradePair>(mkts.size());
				for(String cryptKey : mkts.keySet()) {
					Matcher cpsyDeliM = cpsyDeliP.matcher(cryptKey);
					String interId = cpsyDeliM.replaceFirst("|");
					Market mkt = mkts.get(cryptKey);
					Coin prim = new Coin (mkt.getPrimarycode(), mkt.getPrimaryname());
					Coin secd = new Coin (mkt.getSecondarycode(), mkt.getSecondaryname());
					
					TradePair thisPair = new TradePair(interId, mkt.getMarketid(), prim, secd);
					thisPair.setLastPrice(mkt.getLasttradeprice());
					thisPair.setLastTradeTime(mkt.getLasttradetime());
					thisPair.setVolume(mkt.getVolume());
					
					rt.add(thisPair);
				}
			}
		}
		
		
		return rt;
	}

	
	
	
	public static class ReturnSet {
		private HashMap <String, Market> markets = null;

		public HashMap<String, Market> getMarkets() {
			return markets;
		}

		public void setMarkets(HashMap<String, Market> markets) {
			this.markets = markets;
		}
	}

	public static class Market {
		private String marketid			= null; // "112",
		private String label			= null; // "ASC\/XPM",
		private Double lasttradeprice 	= null; // "0.00021074",
		private Double volume			= null; //": "177165.75181863",
		private Date lasttradetime	= null; //": "2014-02-25 12:52:20",
		private String primaryname		= null; //": "AsicCoin",
		private String primarycode		= null; //": "ASC",
		private String secondaryname	= null; //": "PrimeCoin",
		private String secondarycode	= null; //": "XPM",
		private ArrayList<TradeElem> recenttrades		= null;
		private ArrayList<OrderBookElem> sellorders = null;
		private ArrayList<OrderBookElem> buyorders = null;
		
		public String getMarketid() {
			return marketid;
		}
		public void setMarketid(String marketid) {
			this.marketid = marketid;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public Double getLasttradeprice() {
			return lasttradeprice;
		}
		public void setLasttradeprice(Double lasttradeprice) {
			this.lasttradeprice = lasttradeprice;
		}
		public Double getVolume() {
			return volume;
		}
		public void setVolume(Double volume) {
			this.volume = volume;
		}
		public Date getLasttradetime() {
			return lasttradetime;
		}
		public void setLasttradetime(Date lasttradetime) {
			this.lasttradetime = lasttradetime;
		}
		public String getPrimaryname() {
			return primaryname;
		}
		public void setPrimaryname(String primaryname) {
			this.primaryname = primaryname;
		}
		public String getPrimarycode() {
			return primarycode;
		}
		public void setPrimarycode(String primarycode) {
			this.primarycode = primarycode;
		}
		public String getSecondaryname() {
			return secondaryname;
		}
		public void setSecondaryname(String secondaryname) {
			this.secondaryname = secondaryname;
		}
		public String getSecondarycode() {
			return secondarycode;
		}
		public void setSecondarycode(String secondarycode) {
			this.secondarycode = secondarycode;
		}
		public ArrayList<TradeElem> getRecenttrades() {
			return recenttrades;
		}
		public void setRecenttrades(ArrayList<TradeElem> recenttrades) {
			this.recenttrades = recenttrades;
		}
		public ArrayList<OrderBookElem> getSellorders() {
			return sellorders;
		}
		public void setSellorders(ArrayList<OrderBookElem> sellorders) {
			this.sellorders = sellorders;
		}
		public ArrayList<OrderBookElem> getBuyorders() {
			return buyorders;
		}
		public void setBuyorders(ArrayList<OrderBookElem> buyorders) {
			this.buyorders = buyorders;
		}
	}
	
	public static class TradeElem {
		long id = 0; 
		Date time = null;
		Double price = null;
		Double quantity = null;
		Double total = null;
		
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public Date getTime() {
			return time;
		}
		public void setTime(Date time) {
			this.time = time;
		}
		public Double getPrice() {
			return price;
		}
		public void setPrice(Double price) {
			this.price = price;
		}
		public Double getQuantity() {
			return quantity;
		}
		public void setQuantity(Double quantity) {
			this.quantity = quantity;
		}
		public Double getTotal() {
			return total;
		}
		public void setTotal(Double total) {
			this.total = total;
		}
	}
	
	public static class OrderBookElem {
		Double price = null;
		Double quantity = null;
		Double total = null;
		
		public Double getPrice() {
			return price;
		}
		public void setPrice(Double price) {
			this.price = price;
		}
		public Double getQuantity() {
			return quantity;
		}
		public void setQuantity(Double quantity) {
			this.quantity = quantity;
		}
		public Double getTotal() {
			return total;
		}
		public void setTotal(Double total) {
			this.total = total;
		}
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public ReturnSet getReturnSet() {
		return returnSet;
	}

	public void setReturnSet(ReturnSet returnSet) {
		this.returnSet = returnSet;
	}


}
