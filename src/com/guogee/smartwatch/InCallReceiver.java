package com.guogee.smartwatch;

import com.guogee.smartwatch.ble.PlusDotBLEDevice;
import com.guogee.smartwatch.utils.Log;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

public class InCallReceiver extends BroadcastReceiver {

	private static Object obj;
	private iSmartApplication isApp;
	private PlusDotBLEDevice plusdotBleDevice;
	private SharedPreferences sp;
	
	public InCallReceiver(){
	}
	
	public InCallReceiver(iSmartApplication app){
		isApp = app;
	}
	
	public void setApp(iSmartApplication app,SharedPreferences sp){
		isApp = app;
		this.sp = sp;
	}
	
	public static void showToast(Context context, String msg){
	}
	
    public static void closeToast(){
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
        //获取电话管理服务，以便获取电话的状态
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
		Log.d("TAG", "tm.getCallState() ================="+tm.getCallState());
		
		switch(tm.getCallState()){
		case TelephonyManager.CALL_STATE_RINGING:
			Log.d("TAG", "call ring ......................... ");
			plusdotBleDevice = PlusDotBLEDevice.getInstance();
			plusdotBleDevice.synchronizationPhoneRemindEvent(true);
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:  //接听
			Log.d("TAG", "answer ......................... ");
			plusdotBleDevice = PlusDotBLEDevice.getInstance();
			plusdotBleDevice.synchronizationPhoneRespone(true);						
			break;
		case TelephonyManager.CALL_STATE_IDLE: //挂断 -- 自己挂断 / 对方挂断
			Log.d("TAG", "hang up ......................... ");
//			plusdotBleDevice = PlusDotBLEDevice.getInstance();
//			plusdotBleDevice.synchronizationPhoneRespone(true);
			break;
		case TelephonyManager.DATA_SUSPENDED:
			Log.d("TAG", "DATA_SUSPENDED ......................... ");
			break;
		}
		//if the phone ' s ble is on,sent the msg to the smart-Watch.
	}

}
