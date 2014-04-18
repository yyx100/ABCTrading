package com.agilebc.data.trade;

public enum FatalityLevel {
	FATAL,	//stopped, no retry 
	PAUSE,  //stopped, auto-retry
	WARN,	//warning only, continues
	INFO	//trivial status
}
