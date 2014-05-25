package com.agilebc.trading;

import com.agilebc.data.trade.TradeGenSetOrders;

public interface TradeExecutor {

	public boolean executeOrder (TradeGenSetOrders tsSet);
}
