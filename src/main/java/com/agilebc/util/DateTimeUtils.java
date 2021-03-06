package com.agilebc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.util.config.GenericConfigLoader;

public class DateTimeUtils {
	public static GenericConfigLoader appConf = GenericConfigLoader.getInstance();
	public static Logger applog = LoggerFactory.getLogger(DateTimeUtils.class);
	
	public final static String _dfstr01 = "yyyy-MM-dd HH:mm:ss"; //2014-02-24 21:47:18
	
	
	/**
	 * 
	 * @param dateStr "yyyy-MM-dd HH:mm:ss" //2014-02-24 21:47:18
	 * @return
	 */
	public final static Date StringToDate01 (String dateStr) {
		return String2Date(new SimpleDateFormat(_dfstr01), dateStr);
	}
	
	/**
	 * @param Date
	 * @return dateStr "yyyy-MM-dd HH:mm:ss" //2014-02-24 21:47:18
	 */
	public final static String DateToString01 (Date date) {
		return Date2String(new SimpleDateFormat(_dfstr01), date);
	}
	
	
	
	
	public static Date String2Date(SimpleDateFormat df, String dateStr) {
		Date rt = null;
		try {
			rt =  df.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rt;
	}
	
	
	public static String Date2String (SimpleDateFormat df, Date date) {
		String rt = null;
		rt =  df.format(date);
		
		return rt;
	}
	
	

}
