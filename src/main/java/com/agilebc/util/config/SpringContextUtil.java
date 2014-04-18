package com.agilebc.util.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtil implements ApplicationContextAware {
	
	//@Autowired
	private static ApplicationContext contxt = null;

	public SpringContextUtil (){
		
	}
	
	
	public static ApplicationContext getSpringContext () {
		int counter = 100;
		while (contxt == null && counter >0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			counter --;
		}
		
		return contxt;
	}
	


	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		contxt = applicationContext;
	}
}
