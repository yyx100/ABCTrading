package com.agilebc.trading;

import java.util.Collection;

import com.agilebc.data.trade.Coin;
import com.agilebc.data.trade.LinkedCoinElm;
import com.agilebc.data.trade.TradePair;
import com.agilebc.data.trade.TradingCriteria;

public class TradeLogostics {

	/**
	 *    useful for coin arbitrate.  this method draws out all possible routes start from one coin and trade through others and back to itself 
	 * @param root
	 * @param tpairs
	 * @param filter
	 * @return the head of all possible trade chain
	 */
	public Collection<LinkedCoinElm> constructTradeChain (Coin root, TradePair tpairs, TradingCriteria filter) {
		Collection<LinkedCoinElm> arbiPool = null;
		
		
		return arbiPool;
	}
}
