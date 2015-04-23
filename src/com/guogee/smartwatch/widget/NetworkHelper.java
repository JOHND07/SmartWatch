package com.guogee.smartwatch.widget;

import com.guogee.smartwatch.utils.Log;

import android.content.Context;

public class NetworkHelper {
	/**
	 * ������磬�ж�
	 */
	public static int checkNetWork(Context c){
		
		/** �ж������Ƿ���� **/
		boolean isConnectNetWork = NetworkUtils.checkNetWorkEnable(c);
		Log.d("TAG","isConnectNetWork================="+isConnectNetWork);
		/** ���粻���� **/
		if (!isConnectNetWork) return NetworkTypes.NONET;
		
		/** �жϵ�ǰ������������ (wifi / 2G / 3G) **/
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
