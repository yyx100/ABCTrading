package com.agilebc.data.proprietary;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agilebc.data.trade.Coin;
import com.agilebc.data.trade.OrderBook;
import com.agilebc.data.trade.OrderDepth;
import com.agilebc.data.trade.TradeServerInfo;
import com.agilebc.util.TradeType;
import com.google.gson.annotations.SerializedName;

public class CryptsyServerInfo {

	private int success = -1;
	@SerializedName("return")
	private ReturnSet returnSet = null;
	
	
	public TradeServerInfo transfer () {
		TradeServerInfo out = new TradeServerInfo("cryptsy");
		
		out.setServerTimeStamp(returnSet.getServertimestamp());
		out.setServerDatetime(returnSet.getServerdatetime());
		out.setServerTimeZone(returnSet.getServertimezone());
		
		Map<String, Double> bals = returnSet.getBalances_available();
		for (String key : bals.keySet()) {
			Double bal = bals.get(key);
			if (bal.doubleValue() != 0 ) {
				Coin inCn = new Coin(key, "");
				out.setAvailableBal(inCn, bal);
			}
		}
		return out;
	}
	
	
	
	public static class ReturnSet {
		private HashMap<String, Double> balances_available = null;
		private HashMap<String, Double> balances_available_btc = null;
		
		private long servertimestamp = -1L;
		private String servertimezone = null;
		private Date serverdatetime = null;
		private int openordercount = 0;
		

		public long getServertimestamp() {
			return servertimestamp;
		}
		public void setServertimestamp(long servertimestamp) {
			this.servertimestamp = servertimestamp;
		}
		public String getServertimezone() {
			return servertimezone;
		}
		public void setServertimezone(String servertimezone) {
			this.servertimezone = servertimezone;
		}
		public Date getServerdatetime() {
			return serverdatetime;
		}
		public void setServerdatetime(Date serverdatetime) {
			this.serverdatetime = serverdatetime;
		}
		public int getOpenordercount() {
			return openordercount;
		}
		public void setOpenordercount(int openordercount) {
			this.openordercount = openordercount;
		}
		public HashMap<String, Double> getBalances_available() {
			return balances_available;
		}
		public void setBalances_available(HashMap<String, Double> balances_available) {
			this.balances_available = balances_available;
		}
		public HashMap<String, Double> getBalances_available_btc() {
			return balances_available_btc;
		}
		public void setBalances_available_btc(
				HashMap<String, Double> balances_available_btc) {
			this.balances_available_btc = balances_available_btc;
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
