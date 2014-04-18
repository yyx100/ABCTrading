package com.agilebc.feed.dao;

import com.agilebc.data.config.CryptoCCYExchange;
import com.agilebc.data.vo.Tradable;


/**
 *     Crypto Currency Exchange
 * @author yyx100
 *
 */
public abstract class CccyExchgDao {
	
	protected CryptoCCYExchange exchange = null;
	
	public CccyExchgDao (CryptoCCYExchange exch) {
		this.exchange = exch;
	}


	
	public CryptoCCYExchange getExchange() {
		return exchange;
	}

	public void setExchange(CryptoCCYExchange exchange) {
		this.exchange = exchange;
	}
		
}
