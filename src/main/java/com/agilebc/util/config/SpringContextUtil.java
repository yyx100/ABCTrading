package com.agilebc.util.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.agilebc.cache.AbcSelfMarketDataPopulator;

public class SpringContextUtil implements ApplicationContextAware {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(SpringContextUtil.class);

	@Autowired
	public static ApplicationContext applicationContext = null;

	public SpringContextUtil (){
		
	}
	
	
	public static ApplicationContext getSpringContext () {
		int counter = 10;
		while (applicationContext == null && counter >0) {
			try {
				applog.info("===waiting for spring context to be set===retry left: {}", counter);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			counter --;
		}
		
		return applicationContext;
	}
	


	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringContextUtil.applicationContext = applicationContext;
	}
}
