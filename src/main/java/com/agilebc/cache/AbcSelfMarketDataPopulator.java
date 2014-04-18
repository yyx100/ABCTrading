package com.agilebc.cache;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.agilebc.data.trade.TradePair;
import com.agilebc.feed.dao.ExchangeTradingAPI;
import com.agilebc.jetty.StrategyInvokeHandler;
import com.agilebc.util.config.GenericConfigLoader;
import com.agilebc.util.config.SpringContextUtil;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import net.sf.ehcache.constructs.blocking.LockTimeoutException;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;
import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.RegisteredEventListeners;

public class AbcSelfMarketDataPopulator extends SelfPopulatingCache implements Runnable {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(AbcSelfMarketDataPopulator.class);
	
	@Autowired
	private static ApplicationContext ctx;
	private String exchName = null;
	private String exchDao = null;
	
	public AbcSelfMarketDataPopulator(Ehcache cache, CacheEntryFactory factory)
			throws CacheException {
		super(cache, factory);

		String cName = cache.getName();
		this.exchName = appConf.getCacheExchange(cName);
		this.exchDao = exchName + "Dao";
	}

	
    public Element get(final Object key) throws LockTimeoutException {

        Element element = super.get(key);

        if (element == null) {
            try {
                // Value not cached - fetch it
                Object value = factory.createEntry(key);
                element = makeAndCheckElement(key, value);
            } catch (final Throwable throwable) {
                // Could not fetch - Ditch the entry from the cache and rethrow
                // release the lock you acquired
                element = new Element(key, null);
                throw new CacheException("Could not fetch object for cache entry with key \"" + key + "\".", throwable);
            } finally {
                put(element);
            }
        }
        return element;
    }
	
	public void run() {
		if (ctx == null) {
			ctx = SpringContextUtil.getSpringContext();
		}

		String tName = Thread.currentThread().getName();
		long tId = Thread.currentThread().getId();
		applog.info("==== Exchange:{} <=> Market Data self popultor starts for thread ID{} ({})", exchName, tId, tName);
		ExchangeTradingAPI tradeApi = (ExchangeTradingAPI) ctx.getBean(exchDao);
		
		while (true) {
			try {
				Collection<TradePair> tp = tradeApi.getAllTradePairs();
				applog.info("  ====> thread: {} get server [{}] returns", tName, tp.size());
			
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	protected void maintainSubscribed() {
		List keys = getKeys();
	}
	


}
