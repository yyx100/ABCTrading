package com.agilebc.data.trade;

import com.agilebc.data.AbstractAgilebcData;

public class TradeGenSetOrders extends AbstractAgilebcData {
	private TradeGenUnitOrder[] orders = null;

	public TradeGenUnitOrder[] getOrders() {
		return orders;
	}

	public void setOrders(TradeGenUnitOrder[] orders) {
		this.orders = orders;
	}
	
	
}
