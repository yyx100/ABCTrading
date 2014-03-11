package com.agilebc.data.trade;

public class Fee {
	
	private double ratioBuyFee 		= 0.002;
	private double ratioSellFee 	= 0.003;
	
	private double fixedBuyFee		= 0;
	private double fixedSellFee		= 0;
	
	
	
	public double getRatioBuyFee() {
		return ratioBuyFee;
	}
	public void setRatioBuyFee(double ratioBuyFee) {
		this.ratioBuyFee = ratioBuyFee;
	}
	public double getRatioSellFee() {
		return ratioSellFee;
	}
	public void setRatioSellFee(double ratioSellFee) {
		this.ratioSellFee = ratioSellFee;
	}
	public double getFixedBuyFee() {
		return fixedBuyFee;
	}
	public void setFixedBuyFee(double fixedBuyFee) {
		this.fixedBuyFee = fixedBuyFee;
	}
	public double getFixedSellFee() {
		return fixedSellFee;
	}
	public void setFixedSellFee(double fixedSellFee) {
		this.fixedSellFee = fixedSellFee;
	}
	
	
	
	

}
