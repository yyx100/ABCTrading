package com.agilebc.data.trade;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.util.TradeDirection;

/**
 *   Single Exchange Arbitrage Pool is used as part of my first trading strategy.  This pool contains a collection
 * of trade paths for one coin (root) to trade against others and back to that coin to see if there is any arbitrage opportunity.
 *   This class is in charge of maintaining a pool of all possible paths for one given coin and in one exchange.  Thread
 * saft is guaranteed.
 * 
 *  
 * @author yyx100
 *
 */
public class SingleExchgArbiPool {
	public static Logger applog = LoggerFactory.getLogger(SingleExchgArbiPool.class);

	private Exchange exchg = null;  //informational, this object has no impact to trading logic
	
	private Coin root = null;
	private Collection<TradePair> allPairs = new HashSet<TradePair>();
	private LinkedCoinCriteria criteria = null; 
	
	private Map <String /*pair id*/, TradePair> tradePairs = new HashMap<String, TradePair>();
	private Map <Coin /*coin*/ , Set<TradePair>> coinPath = new HashMap<Coin, Set<TradePair>>();
	
	private Map<String /*tradePathId*/, LinkedCoinElm> tradePaths = new HashMap<String, LinkedCoinElm>();
	
	
	public SingleExchgArbiPool (Coin root, Collection<TradePair> allpPairs, LinkedCoinCriteria criteria) {
		this.root = root;
		this.allPairs = allpPairs;
		if (criteria == null) {
			this.criteria = new LinkedCoinCriteria();
		}
		else {
			this.criteria = criteria;
		}
		
		init();
	}

	
	public void init () {
		//---- initialize market data ---
		initMarketData();
		LinkedCoinElm headCoin = new LinkedCoinElm(root, root);
		recursivePathfinder(headCoin);
	}

	
	public Set<String> getAllTradePathIds () {
		return tradePaths.keySet();
	}
	
	
	public Collection<LinkedCoinElm> getAllTradePathHeads () {
		return tradePaths.values();
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
					
					TradeDirection direc = null;
					Coin tradeCoin = null;
					
					//--- always buy direction of secondary, sell direction of primary ---
					if (tradeTo.getPrimary().equals(currLink.getCurrCoin())) {
						direc = TradeDirection.SELL;
						tradeCoin = tradeTo.getSecondary();
					}
					else if (currLink.getCurrCoin().equals(tradeTo.getSecondary() )) {
						direc = TradeDirection.BUY;
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
					applog.info("====== --- This path fully circled back to root --- {}", headKey);
					tradePaths.put(headKey, head);
				}
			}
		}
		else {
			
		}
	}
	
	
	private String generateTradePathId (LinkedCoinElm pt) {
		StringBuffer sb = new StringBuffer();
		//String delim = ":";
		
		while (pt != null) {
			sb.append(pt.getCurrCoin().getCoinSym()).append( (pt.getNextAction() != null)? pt.getNextAction().getDirectionCode(): "?");
			pt = pt.getNextCoinElm();
		}
		
		return sb.toString();
	}
	
	
	
	/**
	 *     to void exchange from and to the currency back in a symmetic way, we remove them from potential 
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
		for (TradePair tp: allPairs) {
			tradePairs.put(tp.getPairId(), tp);
			
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
}
