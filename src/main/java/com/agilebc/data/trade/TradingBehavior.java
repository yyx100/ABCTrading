package com.agilebc.data.trade;

public enum TradingBehavior {
	
	AQF 	("AllQuantityFilled"),
	FOBD 	("FixedOrderBookDepth");

	
	private String description = null;
	
	private TradingBehavior (String description) {
		this.description = description;
	}
	
	public String getDescription () {
		return description;
	}
}
