package com.agilebc.util.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.agilebc.data.config.ServerEnvType;
import com.agilebc.util.StringUtils;

public class GenericConfigLoader {

	public final static boolean _DEBUG = false;
	public final static Logger applog = LoggerFactory.getLogger(GenericConfigLoader.class);
	
	public final static String _SPRING_PROF = "spring.profiles.active"; 	
			
	
	private final static GenericConfigLoader configService = new GenericConfigLoader();
	
	//--- instance variables ----
	private String myCurrServer = null;
	private ServerEnvType myCurrEnv = ServerEnvType.dev; //default to dev
	private Properties envs = null;
	
	public static GenericConfigLoader getInstance () {
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
		
		GenericXmlApplicationContext tx = new GenericXmlApplicationContext();
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
	
}
