package com.agilebc.data.trade;

import java.util.HashMap;
import java.util.Map;

import com.agilebc.data.AbstractAgilebcData;
import com.agilebc.util.TradeType;

public class OrderBook extends AbstractAgilebcData {

	protected Map <TradeType, OrderDepth> orderbks = null;
	
	
	public OrderBook () {
		orderbks = new HashMap<TradeType, OrderDepth>(2);
		/*
		OrderDepth buy = new OrderDepth(TradeDirection.BUY);
		OrderDepth sell = new OrderDepth(TradeDirection.SELL);
		
		orderbk.put(TradeDirection.BUY, buy);
		orderbk.put(TradeDirection.SELL, sell);
		*/
	}
	
	
	public TradeResult  calcBuyOrSell(double quant, TradeType ty ) {
		if (TradeType.BUY.equals(ty)) { 
			return calcBuy (quant);
		}
		else if (TradeType.SELL.equals(ty)){
			return calcSell (quant);
		}
		
		return null;
	}
	
	
	public TradeResult calcBuy (double secondQuant) {
		
		OrderDepth od = orderbks.get(TradeType.SELL);
		return od.computeIn(secondQuant);
	}

	
	/**
	 * 	 Sell:  Primary to Secondary 
	 *   <BR>	=> SELL direction
	 *   <BR>	=> check against BUY queue
	 *   <BR>	=> result = secondary
	 *   <BR>	=> secondary = primary * rate
	 *   <BR><I>Buy or Sell is ALWAYS: primary * exchange_rate = secondary</I>   
	 * @param primQuant quantity of primary coin
	 * @return trade result that represents secondary coin  
	 */
	public TradeResult calcSell (double primQuant) {
		OrderDepth od = orderbks.get(TradeType.BUY);
		return od.computeOut(primQuant);
	}
	
	
	
	
	public void addOrderDepth (OrderDepth depth) {
		orderbks.put(depth.getTradeDirection(), depth);
	}
	
	
	public OrderBookElm getBookElementAt(TradeType tDir, int i) {
		return orderbks.get(tDir).getBookElementAt(i);
	}
	
	
	public OrderDepth getOrderDepth (TradeType ty) {
		return orderbks.get(ty);
	}
	
	
	//--- auto gen ----
	
	
}
