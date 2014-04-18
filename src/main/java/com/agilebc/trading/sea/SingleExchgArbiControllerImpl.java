package com.agilebc.trading.sea;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;

import com.agilebc.data.config.CryptoCCYExchange;
import com.agilebc.data.config.StrategyConfig;
import com.agilebc.data.trade.LinkedCoinElm;
import com.agilebc.data.trade.TradeGenUnitOrder;
import com.agilebc.data.trade.TradeSignalSet;
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
	
	@Autowired
	private CacheManager abcCacheMan = null;
	
	public SingleExchgArbiControllerImpl () {
	}
	

	@Override
	public TradeSignalSet runStrategy() {
		LinkedCoinElm startCoin = (LinkedCoinElm) stratPool.getNextStratInstance(); 
		try {
			SingleExchgArbiSeeker seaSeeker = new SingleExchgArbiSeeker(startCoin, startOrder, 
					stratConfig.getExeExch().getExchFee(), 
					stratConfig.getTradeCrt(), 
					TradingBehavior.valueOf( stratConfig.getTradingBehaviorName()),
					abcCacheMan) ;
			
			while (optStat.getExecutionState().equals(ExecutionState.RUN)) {
				seaSeeker.seekingAlpha();
				applog.info("=== [running strategy] instance:[{}] with startOrder:[{}]", startCoin.getChainStr(), startOrder);
				
				Thread.sleep(3000);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			String out = stratPool.releaseStratInstance(startCoin);
			applog.info("===> Strategy instance:[{}] returned.", startCoin.getChainStr());
		}
			
		return null;
	}



	
	

}
