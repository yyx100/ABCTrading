package com.agilebc.data.vo;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.cache.AbcSelfMarketDataPopulator;


/**
 *    a queue that is used by multiple threads to get next unit of work
 * @author yyx100
 *
 * @param <T>
 */
public class WorkQueue <T>{
	public static Logger applog = LoggerFactory.getLogger(WorkQueue.class);

	protected final static int _MAINT_CYCLE = 10; //the nth cycle, maintenanceNeed will true
	protected final ArrayList<T> worksQueue = new ArrayList<T>();
	
	protected int pointer = 0;
	protected int maxInd = 0;
	protected long lastMaint = 0;
	protected int totalRuns = 0;
	protected int runCnt = 0;
	protected boolean initialized = false;
	
	public synchronized void rebaseWorks (Collection<T> allWorks) {
		for (T t: allWorks) {
			if (! worksQueue.contains(t)) {
				worksQueue.add(t);
			}
		}
		
		maxInd = worksQueue.size() - 1;
		lastMaint = System.currentTimeMillis();
		totalRuns = maxInd * _MAINT_CYCLE;
	}
	                         
	
	
	public synchronized void addWrok(T oneWk) {
		if (! worksQueue.contains(oneWk)) {
			worksQueue.add(oneWk);
			maxInd ++;
			applog.info("=====> WorkQueue new task added:{} / {}.", oneWk, maxInd);
		}
		
		lastMaint = System.currentTimeMillis();
		totalRuns = maxInd * _MAINT_CYCLE;
	}
	
	
	public synchronized T getNextJob () {
		pointer ++;
		runCnt ++;
		if (pointer >= maxInd) {
			pointer = 0;
		}

		if (maxInd > 0) {
			
			if (pointer <= maxInd) {
				//applog.info("===workQueue get request for{}/{}", pointer , maxInd);
				return worksQueue.get(pointer);
			}
			else {
				applog.warn("===workQueue  get request OUT OF BOUND: {}/{}", pointer , maxInd);
			}
		}
		
		return null;
	}
	
	
	public synchronized boolean maintenanceNeed() {
		boolean rt = false;
		
		if (runCnt > totalRuns && totalRuns > 1) {
			runCnt = 0;
			initialized = true;
			rt = true;
		}
		else if (! initialized) {
			initialized = true;
			rt = true;
		}
		
		applog.info("===> need I do maintenance {}/{}? ==> ANSWER:{}.", runCnt, totalRuns, rt);
		return rt;
	}
	
	public synchronized int queueSize () {
		return maxInd + 1;
	}
	
	
	public synchronized int getCurrentPt () {
		return pointer;
	}
	
	
	public int getMaxInd () {
		return maxInd;
	}
}
