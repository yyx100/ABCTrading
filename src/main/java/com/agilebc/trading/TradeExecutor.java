package com.agilebc.trading;

import com.agilebc.data.trade.TradeSignalSet;

public interface TradeExecutor {

	public boolean executeOrder (TradeSignalSet tsSet);
}
