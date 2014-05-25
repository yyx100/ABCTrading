package com.agilebc.trading.sea;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;

import com.agilebc.data.config.CryptoCCYExchange;
import com.agilebc.data.config.StrategyConfig;
import com.agilebc.data.trade.LinkedCoinElm;
import com.agilebc.data.trade.TradeGenUnitOrder;
import com.agilebc.data.trade.TradeGenSetOrders;
import com.agilebc.data.trade.TradingBehavior;
import com.agilebc.data.vo.ExecutionState;
import com.agilebc.trading.StrategyController;
import com.agilebc.util.TradeType;

/**
 * 
 * @author yyx100
 *
 */
public class SingleExchgArbiControllerImpl extends StrategyController {

	@Autowired
	@Qualifier("seaStartOrder")
	private TradeGenUnitOrder startOrder = null;
	
	//@Autowired
	//private CacheManager abcCacheMan = null;
	
	public SingleExchgArbiControllerImpl () {
	}
	

	/**
	 *   runStrategy will run with the same instance of stratege until the strategy find a profitable order then returns
	 */
	@Override
	public TradeGenSetOrders runStrategy() {
		TradeGenSetOrders rtOrds = null;
		LinkedCoinElm startCoin = (LinkedCoinElm) stratPool.getNextStratInstance();
		
		try {
			SingleExchgArbiSeeker seaSeeker = new SingleExchgArbiSeeker(startCoin, startOrder, 
					stratConfig.getExeExch().getExchFee(), 
					stratConfig.getTradeCrt(), 
					TradingBehavior.valueOf( stratConfig.getTradingBehaviorName()),
					mktDtSrvc) ;
			
			while (optStat.getExecutionState().equals(ExecutionState.RUN)) {
				applog.info("=== [running strategy] instance:[{}] with startOrder:[{}]", startCoin.getChainStr(), startOrder);
				rtOrds = seaSeeker.seekingAlpha();
				
				if (rtOrds == null) {
					Thread.sleep(500);
				}
				else {
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			String out = stratPool.releaseStratInstance(startCoin);
			applog.info("===> Strategy instance:[{}] returning. instance returned status:[{}].", startCoin.getChainStr(), out);
		}
			
		return rtOrds;
	}



	
	

}
