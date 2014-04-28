package com.agilebc.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StringUtils {
	private static Gson debugPrint = new GsonBuilder().setPrettyPrinting().create();
	
	
	public static String toString(Object anyObj) {
		return debugPrint.toJson(anyObj);
	}
	
	
	public static String toHex (byte[] a ) {
		StringBuilder sb = new StringBuilder();
		for(byte b:a)
			sb.append(String.format("%02x", b&0xff));
		return sb.toString();
	}

}
