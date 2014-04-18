package com.agilebc.data.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.agilebc.data.AbstractAgilebcData;
import com.agilebc.data.trade.Exchange;
import com.agilebc.data.trade.TradingCriteria;

public class StrategyConfig extends AbstractAgilebcData {
	
	@Autowired
	private Environment env;
	
	private String strategyName = null;
	private String stratPoolName = null;
	private String stratCntlrName = null;
	private CryptoCCYExchange exeExch = null; //execution exchange
	private TradingCriteria tradeCrt = null;
	private String tradingBehaviorName = null; //link to TradingStrat
	
	private int serverPort = 8080;
	private int maxThreads = 100;
	
	private String shutDownToken = "Abcdefg1";
	
	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Port: ").append(serverPort).append(", ");
		String[] profEnvs = env.getActiveProfiles();
		sb.append("Profile: [");
		for (int i=0; i < profEnvs.length; i++) {
			sb.append(profEnvs[i]).append("\t");
		}
		sb.append("]");
		
		
		return sb.toString();
	}
	
	
	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public Environment getEnv() {
		return env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}


	public String getShutDownToken() {
		return shutDownToken;
	}


	public void setShutDownToken(String shutDownToken) {
		this.shutDownToken = shutDownToken;
	}


	public String getStrategyName() {
		return strategyName;
	}


	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}


	public String getStratPoolName() {
		return stratPoolName;
	}


	public void setStratPoolName(String stratPoolName) {
		this.stratPoolName = stratPoolName;
	}


	public String getStratCntlrName() {
		return stratCntlrName;
	}


	public void setStratCntlrName(String stratCntlrName) {
		this.stratCntlrName = stratCntlrName;
	}


	public int getMaxThreads() {
		return maxThreads;
	}


	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}


	public CryptoCCYExchange getExeExch() {
		return exeExch;
	}


	public void setExeExch(CryptoCCYExchange exeExch) {
		this.exeExch = exeExch;
	}


	public TradingCriteria getTradeCrt() {
		return tradeCrt;
	}


	public void setTradeCrt(TradingCriteria tradeCrt) {
		this.tradeCrt = tradeCrt;
	}


	public String getTradingBehaviorName() {
		return tradingBehaviorName;
	}


	public void setTradingBehaviorName(String tradingBehaviorName) {
		this.tradingBehaviorName = tradingBehaviorName;
	}




}
