package com.guogee.smartwatch.widget;

import com.guogee.smartwatch.utils.Log;

import android.content.Context;

public class NetworkHelper {
	/**
	 * 检查网络，判断
	 */
	public static int checkNetWork(Context c){
		
		/** 判断网络是否可用 **/
		boolean isConnectNetWork = NetworkUtils.checkNetWorkEnable(c);
		Log.d("TAG","isConnectNetWork================="+isConnectNetWork);
		/** 网络不可用 **/
		if (!isConnectNetWork) return NetworkTypes.NONET;
		
		/** 判断当前网络连接类型 (wifi / 2G / 3G) **/
		int type = NetworkUtils.checkNetType(c);
		Log.d("TAG","type==========="+type);		

		switch (type) {
		case NetworkUtils.TYPE_WIFI:
			return NetworkTypes.WIFI;
			// sentAckToBox();
			// break;
		case NetworkUtils.TYPE_3G:
		case NetworkUtils.TYPE_GPRS:
		case NetworkUtils.TYPE_EDGE:
			return NetworkTypes.NOWIFI;
			// sentCMDToServ();
			// break;
		case NetworkUtils.TYPE_UNKNOWN:
			return NetworkTypes.NONET;
			// break;
		default:
			return NetworkTypes.NONET;
		}
	}
}
