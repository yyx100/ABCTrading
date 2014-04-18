package com.agilebc.data.config;

public class UserCredential {
	private String userName = null;
	private String userPass = null;
	
	private int userId = 0;
	private String tradeKey = null;
	
	private String pgpPrivate  = null;
	private String pgpPub = null;
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getTradeKey() {
		return tradeKey;
	}
	public void setTradeKey(String tradeKey) {
		this.tradeKey = tradeKey;
	}
	public String getPgpPrivate() {
		return pgpPrivate;
	}
	public void setPgpPrivate(String pgpPrivate) {
		this.pgpPrivate = pgpPrivate;
	}
	public String getPgpPub() {
		return pgpPub;
	}
	public void setPgpPub(String pgpPub) {
		this.pgpPub = pgpPub;
	}

	
}
