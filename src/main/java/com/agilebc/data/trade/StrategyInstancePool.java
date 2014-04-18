package com.agilebc.data.trade;


/**
 *     for an given strategy, instances of that strategy will be created and maintained in this pool for each thread to work on.
 *  (for example: for a paired arbitrage strategy, an instance will be one specific currency/coin pair) 
 * @author yyx100
 *
 */
public interface StrategyInstancePool <T>  {
	
	/**
	 * 
	 * @return  how many different combinations of given strategy in this pool
	 */
	
	public boolean initPool();
	public int getStrategyInstanceSize() ;
	public T getNextStratInstance();
	public boolean hasMoreInstance();
	public String releaseStratInstance(T stratInst);
}
