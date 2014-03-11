package com.agilebc.data.config;


public class CryptoCCYExchange {
	
	private String 	serverUrl 	= null;
	private int 	serverPort	= 80;
	
	private String 	serverSSLUrl 	= null;
	private int 	serverSSLPort	= 443;


	private UserCredential login = null;


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
	
	

}
