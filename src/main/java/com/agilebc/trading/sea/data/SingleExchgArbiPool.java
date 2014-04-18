package com.agilebc.trading.sea.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.agilebc.data.config.StrategyConfig;
import com.agilebc.data.trade.Coin;
import com.agilebc.data.trade.Exchange;
import com.agilebc.data.trade.StrategyInstancePool;
import com.agilebc.data.trade.TradingCriteria;
import com.agilebc.data.trade.LinkedCoinElm;
import com.agilebc.data.trade.TradePair;
import com.agilebc.feed.dao.ExchangeTradingAPI;
import com.agilebc.util.TradeType;
import com.agilebc.util.config.GenericConfigLoader;

/**
 *   Single Exchange Arbitrage Pool is used as part of my first trading strategy.  This pool contains a collection
 * of trade paths for one coin (root) to trade against others and back to that coin to see if there is any arbitrage opportunity.
 *   This class is in charge of maintaining a pool of all possible paths for one given coin and in one exchange.  
 *   
 *   Thread safe is guaranteed!!!
 *   
 * @author yyx100
 *
 */
public class SingleExchgArbiPool implements StrategyInstancePool <LinkedCoinElm>, ApplicationContextAware {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(SingleExchgArbiPool.class);

	@Autowired
	private ApplicationContext ctx = null;
	@Autowired 
	private CacheManager abcCacheMan = null;
	
	private ExchangeTradingAPI api = null;

	private Coin root = null;
	private Collection<TradePair> allPairs = new HashSet<TradePair>();
	private TradingCriteria criteria = null; 
	
	private Map <String /*pair_id*/, TradePair> tradePairs = new HashMap<String, TradePair>();
	private Map <Coin /*coin*/ , Set<TradePair>> coinPath = new HashMap<Coin, Set<TradePair>>();
	
	private Map<String /*tradePathId*/, LinkedCoinElm> tradePaths = new HashMap<String, LinkedCoinElm>();
	private Map<LinkedCoinElm /*head of trade path*/, String /* leasor thread id*/> leasedArbitragePath = new HashMap<LinkedCoinElm, String>(); 
	
	private double _defaultTradeQuantity = 1;
	private StrategyConfig stratConfig = null;
	
	public SingleExchgArbiPool (StrategyConfig stratConfig, Coin root,  TradingCriteria criteria) {
		this.stratConfig = stratConfig;
		this.root = root;
		//this.allPairs = allpPairs;
		if (criteria == null) {
			this.criteria = new TradingCriteria();
		}
		else {
			this.criteria = criteria;
		}
	}

	
	public boolean initPool () {
		init();
		return true;
	}
	
	public synchronized void init () {
		String daoChoser = stratConfig.getExeExch().getExchName() + "Dao";  
		this.api = (ExchangeTradingAPI) ctx.getBean(daoChoser); 
		this.allPairs = api.getAllTradePairs();
		
		//---- initialize market data ---
		initMarketData();
		LinkedCoinElm headCoin = new LinkedCoinElm(root, root);
		recursivePathfinder(headCoin);
	}

	
	public synchronized Set<String> getAllTradePathIds () {
		return tradePaths.keySet();
	}
	
	
	public synchronized Collection<LinkedCoinElm> getAllTradePathHeads () {
		return tradePaths.values();
	}
	
	/**
	 *   
	 * @param threadId optional for tracking purples.  can be any string
	 * @return head of arbitrage path. null if all path are leased out
	 */
	public synchronized LinkedCoinElm getNextArbitragePath (String threadId) {
		//TODO performance is pretty bad since it does a linear scan for available path per each request.  but I guess it is ok since only used at thread creation time (for now).
		LinkedCoinElm out = null;
		Set < String > keys = tradePaths.keySet();
		
		for (String key : keys) {
			if (! leasedArbitragePath.containsKey(tradePaths.get(key))) {
				out = tradePaths.get(key);
				leasedArbitragePath.put(out, threadId);
				break;
			}
		}
		
		return out;
	}
	
	
	public synchronized String releaseArbitragePath (LinkedCoinElm tradePath) {
		if (tradePath != null) {
			if (leasedArbitragePath.containsKey(tradePath)) {
				return leasedArbitragePath.remove(tradePath);
			}
		}
		
		return null;
	}
	
	
	
	public void recursivePathfinder (LinkedCoinElm currLink) {
		if (validateLinkCondition(currLink)) {
			Set<TradePair> possibleTargets = coinPath.get(currLink.getCurrCoin());
			
			if (!currLink.isFullCircled() && 
					possibleTargets != null && possibleTargets.size() > 0) {
				possibleTargets = removeSymmetricLink(possibleTargets, currLink);
				Iterator<TradePair> tpItr = possibleTargets.iterator();
				TradePair tradeTo = null;

				while(tpItr.hasNext()) {
					tradeTo = tpItr.next();
					
					TradeType direc = null;
					Coin tradeCoin = null;
					
					//--- always buy direction of secondary, sell direction of primary ---
					if (tradeTo.getPrimary().equals(currLink.getCurrCoin())) {
						direc = TradeType.SELL;
						tradeCoin = tradeTo.getSecondary();
					}
					else if (currLink.getCurrCoin().equals(tradeTo.getSecondary() )) {
						direc = TradeType.BUY;
						tradeCoin = tradeTo.getPrimary();
					}
					else {
						applog.warn("===Impossible to determine tradePair direction ===CurrentLink:{} ===>Matched TradePair:{}", currLink, tradeTo);
					}
					
					LinkedCoinElm pointer = currLink;
					if (possibleTargets.size() > 1) { //--- more elements, need to build more path, clone a branch and fork off!  
						pointer = currLink.cloneDeep();
					}
					LinkedCoinElm next = pointer.insertNextCoin(tradeCoin, direc);
					recursivePathfinder(next);
				}
			}
			else {
				if (currLink.isHead()) {
					applog.warn("The following coin has no trade path! Coin:{}", currLink);
				}
				
				applog.info("====== ----end of possible path----");
				LinkedCoinElm head = currLink.getHead();
				String headKey = generateTradePathId(head);
				if (! currLink.isFullCircled()) {
					applog.info("====== --- This path can't return to root --- full links:{}", headKey);
				}
				else {
					applog.info("====== --- This path has fully circled back to root --- {}", headKey);
					double est = estimateProfit(head, _defaultTradeQuantity);
					headKey = est + "|" + headKey;
					head.setChainStr(headKey);
					tradePaths.put(headKey, head);
				}
			}
		}
		else {
			applog.warn("linkage disqualified!");
		}
	}
	
	
	public double estimateProfit(LinkedCoinElm currLnk, double inQuant ) {
		TradeType trdTy = currLnk.getNextAction();
		Coin primCoin = null;  
		Coin secdCoin = null;    
		double outQuant = 0;

		if (currLnk.hasNext()) {
			if ( trdTy.getTradeTypeInt() > 0 ) { //Buy direction
				// if from current to next coin is in buy direction,  then primary coin must be the next coin
				primCoin = currLnk.getNextCoinElm().getCurrCoin();
				secdCoin = currLnk.getCurrCoin();
				TradePair mktTrd = tradePairs.get(TradePair.createPairId(primCoin, secdCoin));
				outQuant = inQuant / mktTrd.getLastPrice() * 0.998;
			}
			else {
				// if from current to next coin is in sell direction, the primary coin must be the current coin
				primCoin = currLnk.getCurrCoin();
				secdCoin = currLnk.getNextCoinElm().getCurrCoin();
				TradePair mktTrd = tradePairs.get(TradePair.createPairId(primCoin, secdCoin));
				outQuant = inQuant * mktTrd.getLastPrice() * 0.997;
			}
			
			return estimateProfit(currLnk.getNextCoinElm(), outQuant);
		}
		
		return inQuant;
	}
	
	/**
	 *   creates a unique id for each trade path.  useful for maintaining all pathes.
	 * @param pt
	 * @return
	 */
	private String generateTradePathId (LinkedCoinElm pt) {
		StringBuffer sb = new StringBuffer();
		
		while (pt != null) {
			sb.append(pt.getCurrCoin().getCoinSym()).append( (pt.getNextAction() != null)? pt.getNextAction().getTradeTypeSym(): "?");
			pt = pt.getNextCoinElm();
		}
		
		return sb.toString();
	}
	
	
	
	/**
	 *     to void exchange from and to the currency back in a symmetric way, we remove them from potential 
	 *   trade targets.  the elements in Set will be cleaned.
	 *   @return a new set of cleaned TradePairs
	 */
	private Set<TradePair> removeSymmetricLink(Set<TradePair> possibleTargets, LinkedCoinElm currLink ) {
		Set<TradePair> rt = new HashSet<TradePair>();
		Coin noPath = (currLink.getPrevCoinElm() != null ) ? currLink.getPrevCoinElm().getCurrCoin() : null;
		int elmCnt = currLink.getSeqId() + 1;
		
		Iterator<TradePair> itr = possibleTargets.iterator();
		while (itr.hasNext()) {
			TradePair currP = itr.next();
			if (noPath!= null &&
					! currP.containsCoin(noPath) ) {
				if (criteria.getMaxLinkLength() == elmCnt) { //already at the last tier, convert back to root only
					if (currP.containsCoin(currLink.getRootCoin())) {
						rt.add(currP);
					}
				}
				else {
					rt.add(currP);
				}
			}
			else if (noPath == null) {
				rt.add(currP);
			}
		}

		
		return rt;
	}
	
	
	private void initMarketData() {
		Cache tpCh = abcCacheMan.getCache(GenericConfigLoader._CACHE_TRADEPAIR);
		
		for (TradePair tp: allPairs) {
			tradePairs.put(tp.getPairId(), tp);
						
			tpCh.put(tp.getPairId(), tp);
			
			Coin pc = tp.getPrimary();
			Coin sc = tp.getSecondary();
			
			//--- primary coin---
			Set <TradePair> inPairs = null;
			if (coinPath.containsKey(pc)){
				inPairs = coinPath.get(pc);
			}
			else {
				inPairs = new HashSet<TradePair>();
				coinPath.put(pc, inPairs);
			}
			inPairs.add(tp);
			//--- secondary coin---
			Set <TradePair> outPairs = null;
			if (coinPath.containsKey(sc)){
				outPairs = coinPath.get(sc);
				
			}
			else {
				outPairs = new HashSet<TradePair>();
				coinPath.put(sc, outPairs);
			}
			outPairs.add(tp); 
		}
	}
	
	
	private boolean validateLinkCondition (LinkedCoinElm currLink) {
		if (criteria != null) {
			if (currLink.getSeqId() >= criteria.getMaxLinkLength() ) {
				applog.info("===> Max trade depth reached at: {} ", criteria.getMaxLinkLength());
				return false;
			}
		}
		return true;
	}


	public int getStrategyInstanceSize() {
		return tradePaths.size();
	}


	public LinkedCoinElm getNextStratInstance() {
		return getNextArbitragePath(Thread.currentThread().getName());
	}


	public boolean hasMoreInstance() {
		return (leasedArbitragePath.size() < tradePaths.size()) ;
	}


	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ctx = applicationContext;
	}


	public String releaseStratInstance(LinkedCoinElm stratInst) {
		return releaseArbitragePath(stratInst);
	}


	public ApplicationContext getCtx() {
		return ctx;
	}


	public void setCtx(ApplicationContext ctx) {
		this.ctx = ctx;
	}


}
