package com.agilebc.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StringUtils {
	private static Gson debugPrint = new GsonBuilder().setPrettyPrinting().create();
	
	
	public static String toString(Object anyObj) {
		return debugPrint.toJson(anyObj);
	}

}
