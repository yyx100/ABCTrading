package com.agilebc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.data.proprietary.CryptsyAllMarkets;
import com.agilebc.data.trade.Coin;
import com.agilebc.data.trade.TradingCriteria;
import com.agilebc.data.trade.TradePair;
import com.agilebc.feed.dao.rest.CryptsyDaoImpl;
import com.agilebc.trading.sea.data.SingleExchgArbiPool;
import com.agilebc.util.DateTimeUtils;
import com.agilebc.util.config.GenericConfigLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class Test {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(Test.class);
	
	public static void main (String[] args) {
		applog.info("\n\nStarted!");
		applog.warn("testing message in RED");
		
		JsonDeserializer<Date> cryptsyDate = new JsonDeserializer <Date>() {
			public Date deserialize(JsonElement json, Type typeOfT,
					JsonDeserializationContext context)
					throws JsonParseException {
				return DateTimeUtils.StringToDate01(json.getAsString());
			}
			
		};
		//CryptsyDaoImpl cryptsy = new CryptsyDaoImpl();
		String cStr = null;
		//cStr = cryptsy.getAllOrderBooks();
		
		try {
			BufferedReader bfRead = null;
			if (cStr == null) {
				StringBuffer bfStr = new StringBuffer();
				bfRead = new BufferedReader(new FileReader ("I:\\export\\GitRepo\\ABCTrading\\src\\test\\resources\\Cryptsy-marketdatav2-resp-simple.json") );
				String line = bfRead.readLine();
				while (line != null  ) {
					bfStr.append(line);
					line = bfRead.readLine();
				}
				cStr = bfStr.toString();
			}
			//BufferedReader 
			Gson gsDe = new  GsonBuilder().registerTypeAdapter(Date.class, cryptsyDate).create();
			CryptsyAllMarkets mkts = gsDe.fromJson(cStr, CryptsyAllMarkets.class);
			
			//Collection<TradePair> tradePairs = mkts.getAllTradePairs();
			//applog.info("====> output: {}", tradePairs);
			
			//SingleExchgArbiPool seap = new SingleExchgArbiPool(new Coin("BTC", "BitCoin"), tradePairs, null);
			//Set<String> tradePathIds = seap.getAllTradePathIds();
			//applog.info("====>  Identified Trade Path: \n{}", tradePathIds);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
