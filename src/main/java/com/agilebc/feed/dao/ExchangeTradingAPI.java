package com.agilebc.feed.dao;

import java.util.Collection;

import com.agilebc.data.trade.TradePair;

public interface ExchangeTradingAPI {

	Collection<TradePair> getAllTradePairs();
}
