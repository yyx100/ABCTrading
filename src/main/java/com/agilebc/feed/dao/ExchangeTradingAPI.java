package com.agilebc.feed.dao;

import java.util.Collection;

import com.agilebc.data.trade.TradePair;
import com.agilebc.data.trade.TradePairEnh;
import com.agilebc.data.trade.TradeServerInfo;

public interface ExchangeTradingAPI {

	public Collection<TradePairEnh> getAllTradePairs();
	public boolean updateTradePair(TradePair tp);
	public TradeServerInfo getExchangeInfo ();
}
