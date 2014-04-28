package com.agilebc.data.trade;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.agilebc.util.config.GenericConfigLoader;

public class TradePairEnh {
	
	private TradePair tp = null;
	private TradeElement mostRecent = null;
	
	private TimeZone timeZone = GenericConfigLoader._DEFAUKT_TIMEZONE;
	private List <TradeElement> recentTrades = null; //store the most recent first (index at 0).
	
	public TradePairEnh (TradePair tp) {
		this.tp = tp;
		this.recentTrades = new ArrayList<TradeElement>();
	}

	
	public TradePairEnh (TradePair tp, List<TradeElement> recentTrades) {
		this.tp = tp;
		this.recentTrades = recentTrades;
		
		if (recentTrades != null) {
			mostRecent = recentTrades.get(0);
		}
	}
	
	public void addRecentTrade (TradeElement telm) {
		recentTrades.add(telm);
		if (mostRecent == null || recentTrades.size() == 0) {
			mostRecent = telm;
		}
	}
	
	public TradePair getTradePair() {
		return tp;
	}
	
	
	@Override
	public String toString (){
		StringBuffer bf = new StringBuffer(tp.toString());
		bf.append("*(");
		if (recentTrades!=null && mostRecent != null) {
			bf.append( recentTrades.size()).append(")").append(mostRecent.toString());
		}
		else {
			bf.append("0)");
		}
		
		return bf.toString();
	}
	//--- auto generated ----


	public TimeZone getTimeZone() {
		return timeZone;
	}


	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}


	public List<TradeElement> getRecentTrades() {
		return recentTrades;
	}


	public void setRecentTrades(List<TradeElement> recentTrades) {
		this.recentTrades = recentTrades;
	}


	public TradeElement getMostRecent() {
		return mostRecent;
	}


	public void setMostRecent(TradeElement mostRecent) {
		this.mostRecent = mostRecent;
	}
	
}
