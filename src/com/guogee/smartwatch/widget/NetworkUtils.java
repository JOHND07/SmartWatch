package com.guogee.smartwatch.widget;

import com.guogee.smartwatch.utils.Log;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 网络管理工具 
 * @author john-deng
 *
 */
public class NetworkUtils {
	
	public static final int TYPE_UNKNOWN = 0;
	
	public static final int TYPE_WIFI = 1;
	
	public static final int TYPE_3G = 2;
	
	public static final int TYPE_EDGE = 3;
	
	public static final int TYPE_GPRS = 4;
	

	/**
	 * 检查网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean checkNetWorkEnable(Context context) {
		ConnectivityManager mConnectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
    
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null || !info.isAvailable()) {
			Log.d("NetworkUtils", "checkNetWorkEnable:false");
			return false;
		} else {
			Log.d("NetworkUtils", "checkNetWorkEnable:true");
			return true;
		}
	}
	
	/**
	 * 检查网路类型
	 * @param context
	 * @return 返回网络类型在NetworkUtils中定义
	 */
	public static int checkNetType(Context context) {
		TelephonyManager mTelephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		ConnectivityManager mConnectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = mConnectivity.getActiveNetworkInfo();

		if (info == null ){//|| !mConnectivity.getBackgroundDataSetting()) {
			return TYPE_UNKNOWN;
		}
		
		int netType = info.getType();
		int netSubtype = info.getSubtype();
		if (netType == ConnectivityManager.TYPE_WIFI) {
			return TYPE_WIFI;
		}
		else if (netType == ConnectivityManager.TYPE_MOBILE
				&& netSubtype == TelephonyManager.NETWORK_TYPE_EDGE
				&& !mTelephony.isNetworkRoaming()) {
			return TYPE_EDGE;
		} else if (netType == ConnectivityManager.TYPE_MOBILE
				&& netSubtype == TelephonyManager.NETWORK_TYPE_GPRS
				&& !mTelephony.isNetworkRoaming()) {
			return TYPE_GPRS;
		} 
		else if (netType == ConnectivityManager.TYPE_MOBILE
				&& !mTelephony.isNetworkRoaming()) {
			return TYPE_3G;
		}
		else {
			return TYPE_UNKNOWN;
		}
	}
	
}
