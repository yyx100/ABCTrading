package com.agilebc.data.config;

public enum ServerEnvType {
	
	dev,
	uat,
	prd;
	
	
	public static boolean isValidEnv (String env) {
		for (ServerEnvType t: ServerEnvType.values() ){
			if (t.name().equals(env)){
				return true;
			}
		}
		
		return false;
	}

}
