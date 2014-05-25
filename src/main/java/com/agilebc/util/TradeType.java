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

	
	/**
	 *     
	 * @return the opposite direction of current type (eg Buy->Sell; Sell->Buy)
	 */
	public TradeType getOpposite () {
		if (direction > 0) {
			return SELL;
		}
		else {
			return BUY;
		}
	}

	
	public String getTradeTypeSym () {
		return symb;
	}
	
	
	public int getTradeTypeInt() {
		return direction;
	}


}
