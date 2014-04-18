package com.agilebc.data.trade;

import com.agilebc.data.AbstractAgilebcData;

public class OrderBookElm extends AbstractAgilebcData {
	private double price 	=	0;
	private double quantity = 	0;
	private double total	= 	-0.000135711;
	
	
	public OrderBookElm (double price, double quantity) {
		this.price = price;
		this.quantity = quantity;
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
