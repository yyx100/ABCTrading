package com.agilebc.jetty;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.data.config.StrategyConfig;
import com.agilebc.data.trade.StrategyInstancePool;
import com.agilebc.data.vo.OperationStat;
import com.agilebc.trading.StrategyController;
import com.agilebc.util.config.GenericConfigLoader;

public class StrategyInvokeHandler extends AbstractHandler {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(StrategyInvokeHandler.class);
	
	protected StrategyConfig stratConfig = null;
	protected static OperationStat optStat = new OperationStat();
	protected StrategyController stratCntlr = null;
	protected StrategyInstancePool stratPool = null;
	protected ExecutorService srvc = null;
	
	public StrategyInvokeHandler (StrategyConfig stratConfig, StrategyInstancePool stratPool, StrategyController stratCntlr) {
		this.stratConfig = stratConfig;
		this.stratPool = stratPool;
		this.stratCntlr = stratCntlr;
		
		applog.info("=====[Construct] StrategyInvokeHandler for [{}] ===>", stratConfig.getStrategyName());
		startStrategyPool();
	}
	
	
	
	
	/**
	 *   uses java threadpool to start strategy instances
	 */
	public void startStrategyPool () {
		optStat.startRun();
		stratPool.initPool();
		stratCntlr.setOptStat(optStat);
		stratCntlr.setStratPool(stratPool);
		
		int max= (stratConfig.getMaxThreads() < stratPool.getStrategyInstanceSize())? stratConfig.getMaxThreads() : stratPool.getStrategyInstanceSize(); 
		this.srvc = Executors.newFixedThreadPool(max);
				
		for (int i=0; i< max; i++) {
			srvc.execute(stratCntlr);
		}
	}
	
	
	
	public void cleanStop() {
		optStat.stopRun();
		applog.info("=====[Clean STOP] StrategyInvokeHandler ===>");
		srvc.shutdown();
		
		//TODO finish pending trading .... 
	}
	 
	
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		applog.info("==============StrategyInvokeHandler.handle is called target:[{}]======>", target);
	}

}
