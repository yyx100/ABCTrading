package com.agilebc.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.trading.sea.SingleExchgArbiSeeker;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

public class AbcCacheEventListenerImpl implements CacheEventListener {
	public static Logger applog = LoggerFactory.getLogger(SingleExchgArbiSeeker.class);

	
	public void notifyElementRemoved(Ehcache cache, Element element)
			throws CacheException {
		// TODO Auto-generated method stub

	}

	public void notifyElementPut(Ehcache cache, Element element)
			throws CacheException {
		applog.info("Cache Added:{}", element);
	}

	public void notifyElementUpdated(Ehcache cache, Element element)
			throws CacheException {
		// TODO Auto-generated method stub

	}

	public void notifyElementExpired(Ehcache cache, Element element) {
		// TODO Auto-generated method stub

	}

	public void notifyElementEvicted(Ehcache cache, Element element) {
		// TODO Auto-generated method stub

	}

	public void notifyRemoveAll(Ehcache cache) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}
	
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException ();
	}

}
