package com.guogee.smartwatch.utils;

import android.util.Log;

public class DebugUtil {
	
	private static final boolean ISDEBUG = false;
	
	public static void println(String content){
		if(ISDEBUG){
			System.out.println(content);
		}
	}
	
	public static void logd(String tag,String content){
		if(ISDEBUG){
			Log.d(tag, content);
		}
	}
	
	public static void logv(String tag,String content){
		if(ISDEBUG){
			Log.v(tag, content);
		}
	}

}
