package com.agilebc.data.trade;

import com.agilebc.data.AbstractAgilebcData;

public class Coin extends AbstractAgilebcData {

	private String coinSym = null;
	private String coinName = null;
	
	
	public Coin (String coinSym, String coinName) {
		this.coinSym = coinSym;
		this.coinName = coinName;
	}
	
	@Override
	public boolean equals (Object cmpr) {
		if (cmpr instanceof Coin) {
			Coin cmpr2 = (Coin) cmpr;
			
			if (this.coinSym.equals(cmpr2.getCoinSym())) {
				return true;
			}
		}
		
		return false;
	}
	
	
	@Override
	public int hashCode () {
		return coinSym.hashCode();
	}
	
	public String getCoinSym() {
		return coinSym;
	}

	public void setCoinSym(String coinSym) {
		this.coinSym = coinSym;
	}

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}
	
	
	
}
