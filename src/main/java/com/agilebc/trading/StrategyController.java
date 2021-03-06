package com.agilebc.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.data.config.StrategyConfig;
import com.agilebc.data.trade.StrategyInstancePool;
import com.agilebc.data.trade.TradeGenSetOrders;
import com.agilebc.data.vo.OperationStat;
import com.agilebc.trading.services.MarketDataService;
import com.agilebc.util.config.GenericConfigLoader;

public abstract class StrategyController implements Runnable {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(StrategyController.class);

	protected StrategyInstancePool stratPool = null;
	protected OperationStat optStat = null;
	protected TradeExecutor trdExec = null;
	protected StrategyConfig stratConfig = null;
	protected MarketDataService mktDtSrvc = null;
	
	/**
	 *   runs each strategy in a thread
	 * @return
	 */
	public abstract TradeGenSetOrders runStrategy ();
	
	
	
	
	public void run() {
		String threadName = Thread.currentThread().getName();
		applog.info("=== Thread [{}] is running .....", threadName);
		TradeGenSetOrders tsSet = null;
		boolean trdAns = false;
		while (optStat.isRunning() ) {
			tsSet = runStrategy();
			
			if (tsSet != null) {
				trdAns = trdExec.executeOrder(tsSet);
			}
		}
	}

	
	public OperationStat getOptStat() {
		return optStat;
	}

	public void setOptStat(OperationStat optStat) {
		this.optStat = optStat;
	}


	public StrategyInstancePool getStratPool() {
		return stratPool;
	}

	public void setStratPool(StrategyInstancePool stratPool) {
		this.stratPool = stratPool;
	}

	public StrategyConfig getStratConfig() {
		return stratConfig;
	}


	public void setStratConfig(StrategyConfig stratConfig) {
		this.stratConfig = stratConfig;
	}




	public MarketDataService getMktDtSrvc() {
		return mktDtSrvc;
	}




	public void setMktDtSrvc(MarketDataService mktDtSrvc) {
		this.mktDtSrvc = mktDtSrvc;
	}
}
