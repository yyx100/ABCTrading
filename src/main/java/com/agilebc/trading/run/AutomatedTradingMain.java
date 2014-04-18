package com.agilebc.trading.run;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.agilebc.data.config.ApplicationEnvConfig;
import com.agilebc.data.config.StrategyConfig;
import com.agilebc.data.trade.StrategyInstancePool;
import com.agilebc.jetty.StrategyInvokeHandler;
import com.agilebc.trading.StrategyController;
import com.agilebc.util.config.GenericConfigLoader;
import com.agilebc.util.config.SpringContextUtil;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ShutdownHandler;

/**
 *     launch one automated trading strategy 
 * @author yyx100
 *
 */
public class AutomatedTradingMain {
	
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(AutomatedTradingMain.class);
	
	public static Option _OPT_S = new Option("S", "strategy", true, "Strategy_Name"); //map to StrategyConfig
	public static Option _OPT_E = new Option("E", "exchange", true, "Exchange_Name"); //map to StrategyConfig
	
	
	public static Options cmdOpts = new Options();
	static {
		cmdOpts.addOption(_OPT_S);
		cmdOpts.addOption(_OPT_E);
	}
	
	private StrategyConfig stratConfig = null;
	private StrategyInstancePool stratPool = null;
	private StrategyController stratCntl = null;
	private static ApplicationContext ctx;
	
	public AutomatedTradingMain (StrategyConfig stratConfig) {
		this.stratConfig = stratConfig;
		init();
	}
	
	protected void init() {
		this.stratPool = (StrategyInstancePool) ctx.getBean(stratConfig.getStratPoolName());
		this.stratCntl = (StrategyController) ctx.getBean(stratConfig.getStratCntlrName());
	}
	
	public void startServer () throws Exception{
		Server stratServ = new Server(stratConfig.getServerPort());
		
		HandlerList hdls = new HandlerList();
		StrategyInvokeHandler siHdl = new StrategyInvokeHandler(stratConfig, stratPool, stratCntl);
		hdls.addHandler(siHdl);
		hdls.addHandler(new ShutdownHandler(stratConfig.getShutDownToken()));
		stratServ.setHandler(hdls);
		
		stratServ.start();
		stratServ.join();
		
		siHdl.cleanStop();
		
	}
	
	

	
	public static void main (String[] argv) {
		ctx = new ClassPathXmlApplicationContext (GenericConfigLoader._SPRING_CONF);
		
		CommandLineParser cmdPser = new PosixParser();
		String stratConfigXml = null;
		try {
			CommandLine cmdln = cmdPser.parse(cmdOpts, argv);
			stratConfigXml = cmdln.getOptionValue(_OPT_S.getOpt());
			
		} catch (ParseException e) {
			HelpFormatter help = new HelpFormatter();
			help.printHelp("AutomatedTradingMain", cmdOpts);
			e.printStackTrace();
		}
		
		StrategyConfig ssConfig = (StrategyConfig) ctx.getBean(stratConfigXml);
		ApplicationEnvConfig appConfig = (ApplicationEnvConfig) ctx.getBean("appConfig");
		
		applog.info("======>Starting Application Service for: [{}] \n[{}]", ssConfig, appConfig);
		AutomatedTradingMain atm = new AutomatedTradingMain(ssConfig);
		
		try {
			atm.startServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		applog.info("======>STOPPED Application Service for: [{}] \n[{}]", ssConfig, appConfig);
	}
	

	
}
