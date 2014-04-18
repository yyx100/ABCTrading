package com.agilebc.data.config;

import com.agilebc.data.trade.ExchangeFee;


public class CryptoCCYExchange {
	
	private String 	exchName = null;
	private int		exchId = -1;
	
	private String 	serverUrl 	= null;
	private int 	serverPort	= 80;
	
	private String 	serverSSLUrl 	= null;
	private int 	serverSSLPort	= 443;

	private UserCredential login = null;
	private ExchangeFee exchFee = null;

	public String getServerUrl() {
		return serverUrl;
	}


	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}


	public int getServerPort() {
		return serverPort;
	}


	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}


	public String getServerSSLUrl() {
		return serverSSLUrl;
	}


	public void setServerSSLUrl(String serverSSLUrl) {
		this.serverSSLUrl = serverSSLUrl;
	}


	public int getServerSSLPort() {
		return serverSSLPort;
	}


	public void setServerSSLPort(int serverSSLPort) {
		this.serverSSLPort = serverSSLPort;
	}


	public UserCredential getLogin() {
		return login;
	}


	public void setLogin(UserCredential login) {
		this.login = login;
	}


	public ExchangeFee getExchFee() {
		return exchFee;
	}


	public void setExchFee(ExchangeFee exchFee) {
		this.exchFee = exchFee;
	}


	public String getExchName() {
		return exchName;
	}


	public void setExchName(String exchName) {
		this.exchName = exchName;
	}


	public int getExchId() {
		return exchId;
	}


	public void setExchId(int exchId) {
		this.exchId = exchId;
	}


	
	

}
