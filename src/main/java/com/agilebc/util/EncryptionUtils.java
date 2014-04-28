package com.agilebc.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {

	private String secret = null;
	private Mac mac = null;
	
	
	public EncryptionUtils (String secret) {
		this.secret = secret;
		try {
			this.mac = Mac.getInstance("HmacSHA512");
			SecretKeySpec secret_spec = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
	        	
			mac.init(secret_spec);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public String getHmacSHA512 (String inStr) {
		String signature = StringUtils.toHex((mac.doFinal(inStr.getBytes()))) ;
        //System.out.println("encrypted:" + signature);
        return signature;
	}
	
	
	
}
