package com.agilebc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.agilebc.util.config.GenericConfigLoader;

public class MarketDataServiceUtils {
	/**
	 *   a utitlity method to parse out the exchange name from the cachen name, which is in format
	 *   of com.exch.(exchange_name).cache_name
	 * @param cacheName
	 * @return
	 */
	public static String getCacheExchange (String cacheName) {
		Pattern ptnExch = Pattern.compile(GenericConfigLoader._PREFIX_EXCHANGE+ "\\.(\\w+?)\\.(\\w*)?$");
		Matcher m = ptnExch.matcher(cacheName);
		if (m.matches()) {
			return m.group(1);
		}
		else {
			return null;
		}
	}
	
	
	/**
	 *   a utitlity method to parse out the cache api name from the cache name, which is in format
	 *   of com.exch.exchange_name.(cache_api_name)
	 * @param cacheName
	 * @return
	 */
	public static String getCacheApiName (String cacheName) {
		Pattern ptnExch = Pattern.compile(GenericConfigLoader._PREFIX_EXCHANGE+ "\\.(\\w+?)\\.(\\w*)?$");
		Matcher m = ptnExch.matcher(cacheName);
		if (m.matches()) {
			return m.group(2);
		}
		else {
			return null;
		}
	}
	
	
	/**
	 * 
	 * @param exchgName
	 * @param apiName
	 * @return  full cache name com.exch.(exchange_name).(cache_api_name) 
	 */
	public static String getCacheFullName(String exchgName, String apiName) {
		StringBuffer bf = new StringBuffer(GenericConfigLoader._PREFIX_EXCHANGE).append(".").append(exchgName).append(".").append(apiName);
		return bf.toString();
	}
}
