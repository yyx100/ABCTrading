package com.agilebc.util.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.agilebc.data.config.ServerEnvType;
import com.agilebc.data.vo.ExecutionState;
import com.agilebc.data.vo.OperationStat;
import com.agilebc.util.StringUtils;

public class GenericConfigLoader {

	public final static boolean _DEBUG = false;
	public final static Logger applog = LoggerFactory.getLogger(GenericConfigLoader.class);

	public final static TimeZone _DEFAUKT_TIMEZONE = TimeZone.getTimeZone("EST"); //default timezone to est, (NY)!
	public final static String _SPRING_PROF = "spring.profiles.active"; 	
	public final static String _SPRING_CONF = "spring/AgileBusinessCenterConfig.xml";

	public final static String _ABC_TRADE_EXCH 	= "com.agilebc.exchange";
	public final static String _ABC_SUBSCRIBED	= "com.agilebc.subscription";  //this is the cache name for subscribed marketdata in abctrading-ehcache.xml "com.agilebc.subscription";

	public final static String _CACHE_TRADEPAIR = "tradePair";
	
	public final static int _DEFAULT_RETRY_SLEEP = 1000;

	public final static String _PREFIX_EXCHANGE = "com.exch";
	private static GenericConfigLoader configService = null;
	
	//--- instance variables ----
	private String myCurrServer = null;
	private ServerEnvType myCurrEnv = ServerEnvType.dev; //default to dev
	private Properties envs = null;
	private OperationStat optStat = new OperationStat(ExecutionState.RUN);  //--- default operation status to run state at start.  application can override this value later
	
	public synchronized static GenericConfigLoader getInstance () {
		if (configService == null) {
			configService = new GenericConfigLoader();
		}
		
		return configService;
	}
	
	private GenericConfigLoader () {
		try {
			this.envs = loadInJarProperties("properties/all-server-config.properties");
			whereAmI();
			
			if (System.getProperty(_SPRING_PROF) == null ||
					! ServerEnvType.isValidEnv(System.getProperty(_SPRING_PROF)) ){
				System.setProperty(_SPRING_PROF, myCurrEnv.name());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		applog.info("***===============================================================================================***");
		applog.info("***==Service Started In:[{}]============================================***", myCurrEnv);
		applog.info("***===============================================================================================***");
	}
	
	
	
	public static Properties loadInJarProperties (String pkgName) throws IOException {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream inProp =  cl.getResourceAsStream(pkgName);
		Properties newProp = new Properties();
		newProp.load(inProp);
		
		return newProp;
	}
	
	
	public void whereAmI () {
		try {
			InetAddress inet = InetAddress.getLocalHost();
			this.myCurrServer = inet.getHostName();
			applog.info("=============Starting on Server: [{}]", myCurrServer);
			
			String uat = envs.getProperty("com.agilebc.execution.server.uat");
			String prd = envs.getProperty("com.agilebc.execution.server.prd");
			
			Pattern pt= Pattern.compile(".*" + myCurrServer + ".*", Pattern.CASE_INSENSITIVE);
			Matcher mtr= pt.matcher(prd);
			
			if (mtr.matches()) {
				this.myCurrEnv = ServerEnvType.prd;
			}
			else {
				mtr = pt.matcher(uat);
				if (mtr.matches()){
					this.myCurrEnv = ServerEnvType.uat;
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			applog.warn("error when determining server execution envirorment.  err:[{}]", e.getMessage());
		}
		
	}
	
	

	
	
	
	@Override
	public String toString() {
		return StringUtils.toString(this);
	}

	public OperationStat getOptStat() {
		return optStat;
	}

	public void setOptStat(OperationStat optStat) {
		this.optStat = optStat;
	}
	
}
