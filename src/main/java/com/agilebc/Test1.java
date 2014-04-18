package com.agilebc;

import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.map.TDoubleDoubleMap;
import gnu.trove.map.hash.TDoubleDoubleHashMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.trading.sea.data.SingleExchgArbiPool;

public class Test1 {
	public static Logger applog = LoggerFactory.getLogger(Test1.class);

	
	public static void main(String[] args) {
	
		HashMap test =new HashMap(3000);
		
		applog.info("HashMap size: {}", test.size());
		
	}

}
