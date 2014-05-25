package com.agilebc.trading;

import com.agilebc.data.trade.ExchangeFee;
import com.agilebc.data.trade.OrderBook;
import com.agilebc.data.trade.TradeResult;
import com.agilebc.util.TradeType;

public class TradeCalculator {
	
	
	public static TradeResult execWithCommission(TradeType ty, double tradeQuant, OrderBook ob, ExchangeFee fee){
		TradeResult tr = null;
		double feeRate = 0.003;
		double feeMin = fee.getMinFee();
		double actualFee = 0;
		if (TradeType.BUY.equals(ty)) {
			feeRate = fee.getRatioBuyFee();
			
			//buy order reduces buying power before execution
			actualFee = tradeQuant * feeRate;
			tradeQuant = (actualFee > feeMin) ? tradeQuant * (1-feeRate) : (tradeQuant - feeMin);
			tr = ob.calcBuy(tradeQuant);
		}
		else {
			feeRate = fee.getRatioSellFee();
			//sell order reduces proceeds after execution
			tr = ob.calcSell(tradeQuant);
			actualFee = tr.getToCoinTotal() * feeRate;
			double offSet = (actualFee > feeMin)? actualFee  : feeMin;
			tr.setToCoinTotal(tr.getToCoinTotal() - offSet);
		}
		
		return tr;
	}
}
