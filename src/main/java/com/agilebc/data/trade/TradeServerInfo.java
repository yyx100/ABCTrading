package com.agilebc.data.trade;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.agilebc.util.DateTimeUtils;

public class TradeServerInfo {
	private String tradeServerId = null;
	
	private long serverTimeStamp = -1L;
	private String serverTimeZone = null;
	private Date serverDatetime = null;
	private int openOrderCount = 0;
	
	private Map <Coin, Double> availableBal = null;

	
	public TradeServerInfo (String serverId) {
		this.tradeServerId = serverId;
		this.availableBal = new HashMap<Coin, Double> ();
	}
	
	public void setAvailableBal (Coin coin, Double bal) {
		availableBal.put(coin, bal);
	}
	
	
	@Override 
	public String toString () {
		StringBuffer bf = new StringBuffer("[").append(tradeServerId)
				.append(">>>time:(").append(DateTimeUtils.DateToString01(serverDatetime)).append(" ")
				.append(serverTimeZone).append(") open_orders:(").append(openOrderCount).append(") balance:(");
			if (availableBal.size() > 0) {
				int j = 0;
				for (Coin cn: availableBal.keySet()) {
					if (j > 0) {
						bf.append("/");
					}
					else {
						j++;
					}
					bf.append(cn.getCoinSym()).append(":").append(availableBal.get(cn));
				}
			}
			bf.append(")]");
		return bf.toString();
	}
	
	
	//-----------------auto generated -------------
	public long getServerTimeStamp() {
		return serverTimeStamp;
	}

	public void setServerTimeStamp(long serverTimeStamp) {
		this.serverTimeStamp = serverTimeStamp;
	}

	public String getServerTimeZone() {
		return serverTimeZone;
	}

	public void setServerTimeZone(String serverTimeZone) {
		this.serverTimeZone = serverTimeZone;
	}

	public Date getServerDatetime() {
		return serverDatetime;
	}

	public void setServerDatetime(Date serverDatetime) {
		this.serverDatetime = serverDatetime;
	}

	public int getOpenOrderCount() {
		return openOrderCount;
	}

	public void setOpenOrderCount(int openOrderCount) {
		this.openOrderCount = openOrderCount;
	}

	public Map<Coin, Double> getAvailableBal() {
		return availableBal;
	}

	public void setAvailableBal(Map<Coin, Double> availableBal) {
		this.availableBal = availableBal;
	}
	
}
