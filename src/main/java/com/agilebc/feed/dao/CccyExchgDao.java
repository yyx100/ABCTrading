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
	

	public abstract void getOrderBook (Tradable tradePair);
	public abstract String getAllOrderBooks();
	
	
	
	public CryptoCCYExchange getExchange() {
		return exchange;
	}

	public void setExchange(CryptoCCYExchange exchange) {
		this.exchange = exchange;
	}
		
}
