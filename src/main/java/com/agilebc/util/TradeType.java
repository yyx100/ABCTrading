package com.agilebc.util;

public enum TradeType {

		BUY (1, "+"),
		SELL (-1, "-");
		
	private int direction = 0;
	private String symb = null;
	
	private TradeType (int dir, String code) {
		this.direction = dir;
		this.symb = code;
	}


	public String getTradeTypeSym () {
		return symb;
	}
	
	
	public int getTradeTypeInt() {
		return direction;
	}


}
