package com.agilebc.util;

public enum TradeDirection {

		BUY (1, "+"),
		SELL (-1, "-");
		
	private int direction = 0;
	private String code = null;
	
	private TradeDirection (int dir, String code) {
		this.direction = dir;
		this.code = code;
	}


	public String getDirectionCode () {
		return code;
	}
	
	
	public int getDirectionInt() {
		return direction;
	}


}
