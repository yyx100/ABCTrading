package com.agilebc.data.proprietary;

import java.util.List;

import com.agilebc.data.trade.OrderBook;
import com.agilebc.data.trade.OrderDepth;
import com.agilebc.util.TradeType;
import com.google.gson.annotations.SerializedName;

public class CryptsyOrderDepth {

	private int success = -1;
	@SerializedName("return")
	private ReturnSet returnSet = null;
	
	
	public static class ReturnSet {
		private List <double []> sell 	= null;
		private List <double []> buy	= null;
		
		public List<double[]> getSell() {
			return sell;
		}
		public void setSell(List<double[]> sell) {
			this.sell = sell;
		}
		public List<double[]> getBuy() {
			return buy;
		}
		public void setBuy(List<double[]> buy) {
			this.buy = buy;
		}
		
		
		
	}

	public OrderBook convert2OrderBook () {
		OrderBook ob = new OrderBook();
		
		OrderDepth buyOD = new OrderDepth(TradeType.BUY);
		OrderDepth sellOD = new OrderDepth(TradeType.SELL);
		ob.addOrderDepth(buyOD);
		ob.addOrderDepth(sellOD);
		
		convert2OrderDepth(returnSet.getBuy(), buyOD);
		convert2OrderDepth(returnSet.getSell(), sellOD);
		
		return ob;
	}
	
	private void convert2OrderDepth (List<double[]> curr, OrderDepth rt) {
		if (curr != null) {
			int imax=curr.size();
			double[] n = null;
			for (int i=0; i<imax; i++) {
				n = curr.get(i);
				rt.addOrder(n[0], n[1]);
			}
		}
	}



	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public ReturnSet getReturnSet() {
		return returnSet;
	}

	public void setReturnSet(ReturnSet returnSet) {
		this.returnSet = returnSet;
	}
	
}
