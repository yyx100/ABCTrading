package com.agilebc.data.trade;

public class OrderBookElm  {
	private double price 	=	0;
	private double quantity = 	0;
	private double total	= 	-0.000135711;
	
	
	public OrderBookElm (double price, double quantity) {
		this.price = price;
		this.quantity = quantity;
	}
	
	
	@Override
	public String toString() {
		StringBuffer bf= new StringBuffer().append(price).append("X").append(quantity).append("=").append(total);
		return bf.toString();
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getTotal() {
		if (total == - 0.000135711) {
			total = price * quantity;
		}
		
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	
	
	
}
