package com.agilebc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.data.trade.SingleExchgArbiPool;

public class Test1 {
	public static Logger applog = LoggerFactory.getLogger(Test1.class);

	
	public static void main(String[] args) {
		Set<String> test = new HashSet<String>();
		test.add("A");
		test.add("B");
		test.add("C");
		
		/*
		for (String x: test) {
			if (x.equals("A")) {
				test.remove(x);
			}
		}
		*/
		
		Iterator<String> it = test.iterator();
		while (it.hasNext()) {
			String cur = it.next();
			if (cur.equals("A") || true) {
				it.remove();
			}
			
			applog.info(" ===> current size={}, set:{}", test.size(), test);
		}
		
		applog.info("===> the output is:{}", test);
	}

}
