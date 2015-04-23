package com.guogee.smartwatch.utils;

/**
 * 替换原来android的Log类
 * @author john-deng
 */
public class Log {
	/** 是否为发布模式 */
	private final static boolean IS_RELEASE = true;
	/**
	 * Send a DEBUG log message.
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.  
	 */
	public static void d( String tag, String msg)
	{
		if(IS_RELEASE)	return;
		android.util.Log.d(tag, msg);
	}
	/**
	 * Send a VERBOSE log message.
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.  
	 */
	public static void v( String tag, String msg)
	{
		if(IS_RELEASE)	return;
		android.util.Log.v(tag, msg);
	}
	/**
	 * Send an INFO log message.
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.  
	 */
	public static void i( String tag, String msg)
	{
		if(IS_RELEASE)	return;
		android.util.Log.i(tag, msg);
	}
	/**
	 * Send an ERROR log message.
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.  
	 */
	public static void e( String tag, String msg)
	{
		android.util.Log.e(tag, msg);
	}
	/**
	 * Send a WARN log message.
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.  
	 */
	public static void w( String tag, String msg)
	{
		android.util.Log.w(tag, msg);
	}
	public static boolean isRelease()
	{
		return IS_RELEASE;
	}
}
