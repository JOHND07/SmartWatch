package com.guogee.smartwatch.service;

import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.ble.PlusDotBLEDevice;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.utils.Log;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class SWService extends Service {

	private SWBinder swBinder = new SWBinder();
	
	private iSmartApplication isApp;
	
	private SharedPreferences sp;
	
	private PlusDotBLEDevice plusdotBleDevice;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return swBinder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d("TAG","SWService onCreate ................");
		createNotifycation();
		
		isApp = (iSmartApplication) getApplication();
		sp = PreferenceManager.getDefaultSharedPreferences(isApp);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("TAG","SWService onDestroy ................");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.d("TAG","SWService onStart ................");
	}
	
	public class SWBinder extends Binder{
		SWService getService(){
			return SWService.this;
		}
	}
	
	//////////
	
    private NotificationReceiver nReceiver;
	
	private void createNotifycation(){
		
		Log.d("TAG", "createNotifycation....................");
		
		nReceiver = new NotificationReceiver();
	    IntentFilter filter = new IntentFilter();
	    filter.addAction("com.guogee.smartwatch.NOTIFICATION_LISTENER");
	    registerReceiver(nReceiver,filter);
	    
//	    NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
//        ncomp.setContentTitle("My Notification");
//        ncomp.setContentText("Notification Listener Service Example");
//        ncomp.setTicker("Notification Listener Service Example");
//        ncomp.setSmallIcon(R.drawable.ic_launcher);
//        ncomp.setAutoCancel(true);
//        nManager.notify((int)System.currentTimeMillis(),ncomp.build());
	}
	
	class NotificationReceiver extends BroadcastReceiver{
	        @Override
	        public void onReceive(Context context, Intent intent) {
//	            String temp = intent.getStringExtra("notification_event") + "\n" + txtView.getText();
//	            txtView.setText(temp);
	        	
//	        	Log.d("TAG", "receive the NLService message ..................."+intent.getStringExtra("notification_event"));
	        	String temp = intent.getStringExtra("notification_event");
//	        	Log.d("TAG", "temp ============ "+temp);
	        	
	        	boolean isRemoveEvent = intent.getBooleanExtra("isRemoveEvent", false);
//	        	Log.d("TAG", "isRemoveEvent ============ "+isRemoveEvent);
	        	
	        	// TEST
	     		if(temp.equals(Constant.MESSAGE_PACKET)){
//	     			Toast.makeText(getApplicationContext(), "receive the msg", Toast.LENGTH_SHORT).show();
	     			
	     			if(sp.getBoolean(Constant.SMS_FALG, true)){
	     				if(isApp.getConnectBleDevice()){
	     					
//	     					Log.d("TAG","get sms message and sent command................");
	     					
	     					plusdotBleDevice = PlusDotBLEDevice.getInstance();
	     					
	     					if(isRemoveEvent){
	     						plusdotBleDevice.synchronizationSMSRespone(true);
	     					}else{
	     						plusdotBleDevice.synchronizationSMSRemindEvent(true);	
	     					}
	     				}
	     			}
	     			
	     		}else if(temp.equals(Constant.PHONE_PACKET)){
//	     			Toast.makeText(getApplicationContext(), "receive the phone ", Toast.LENGTH_SHORT).show();
	     			
//	     			if(sp.getBoolean(Constant.PHONE_FALG, true)){
//	     				if(isApp.getConnectBleDevice()){
//	     					plusdotBleDevice = PlusDotBLEDevice.getInstance();
//	     					plusdotBleDevice.synchronizationSMSRemindEvent(true);
//	     				}
//	     			}
	     			
	     		}else if(temp.equals(Constant.EMAIL_PACKET)||temp.equals(Constant.OUTLOOK_PACKET)){
//	     			Toast.makeText(getApplicationContext(), "receive the Email", Toast.LENGTH_SHORT).show();
	     			
	     			if(sp.getBoolean(Constant.EMAIL_FALG, true)){
	     				if(isApp.getConnectBleDevice()){
	     					plusdotBleDevice = PlusDotBLEDevice.getInstance();
	     					
	     					if(isRemoveEvent){
	     						plusdotBleDevice.synchronizationEmailRespone();//(true);
	     					}else{
	     						plusdotBleDevice.synchronizatioEmailRemindEvent(true);
	     					}
	     				}
	     			}
	     		}else if(temp.equals(Constant.QQ_PACKET)){
//	     			Toast.makeText(getApplicationContext(), "receive the QQ MSG", Toast.LENGTH_SHORT).show();
	     			
	     			if(sp.getBoolean(Constant.QQ_FALG, true)){
	     				if(isApp.getConnectBleDevice()){
	     					plusdotBleDevice = PlusDotBLEDevice.getInstance();
	     					if(isRemoveEvent){
	     						plusdotBleDevice.synchronizationSMSRespone(true);
	     					}else{
	     						plusdotBleDevice.synchronizationSMSRemindEvent(true);	
	     					}
	     				}
	     			}
	     			
	     		}else if(temp.equals(Constant.QQI_PACKET)){
//	     			Toast.makeText(getApplicationContext(), "receive the QQ MSG", Toast.LENGTH_SHORT).show();
	     			
	     			if(sp.getBoolean(Constant.QQ_FALG, true)){
	     				if(isApp.getConnectBleDevice()){
	     					plusdotBleDevice = PlusDotBLEDevice.getInstance();
	     					
	     					if(isRemoveEvent){
	     						plusdotBleDevice.synchronizationSMSRespone(true);
	     						
//	     						plusdotBleDevice.synchronizationEmailRespone();//(true);
	     						
	     					}else{
	     						plusdotBleDevice.synchronizationSMSRemindEvent(true);	
	     					}
	     					
	     				}
	     			}
	     			
	     		}else if(temp.equals(Constant.WEBCHAT_PACKET)){
//	     			Toast.makeText(getApplicationContext(), "receive the WEICHAT", Toast.LENGTH_SHORT).show();
	     			
	     			if(sp.getBoolean(Constant.WEBCHAT_FALG, true)){
	     				if(isApp.getConnectBleDevice()){
	     					plusdotBleDevice = PlusDotBLEDevice.getInstance();
	     					
	     					if(isRemoveEvent){
//	     						Log.d("TAG","cancle webchart msg ........ ");
	     						plusdotBleDevice.synchronizationSMSRespone(true);
	     						
//	     						plusdotBleDevice.synchronizationEmailRespone();//(true);
	     						
	     					}else{
	     						plusdotBleDevice.synchronizationSMSRemindEvent(true);	
	     					}
	     				}
	     			}
	     			
	     		}else if(temp.equals(Constant.WHATSAPP_PACKET)){
	     			if(sp.getBoolean(Constant.WHATSAPP_FALG, true)){
	     				if(isApp.getConnectBleDevice()){
	     					plusdotBleDevice = PlusDotBLEDevice.getInstance();
	     					if(isRemoveEvent){
	     						plusdotBleDevice.synchronizationSMSRespone(true);
	     					}else{
	     						plusdotBleDevice.synchronizationSMSRemindEvent(true);	
	     					}
	     				}
	     			}
	     			
	     		}else if(temp.equals(Constant.SKYPE_PACKET)){
//	     			Toast.makeText(getApplicationContext(), "receive the FaceBook", Toast.LENGTH_SHORT).show();
	     			if(sp.getBoolean(Constant.SKYPE_FALG, true)){
	     				if(isApp.getConnectBleDevice()){
	     					plusdotBleDevice = PlusDotBLEDevice.getInstance();
	     					if(isRemoveEvent){
	     						plusdotBleDevice.synchronizationSMSRespone(true);
	     					}else{
	     						plusdotBleDevice.synchronizationSMSRemindEvent(true);	
	     					}
	     				}
	     			}
	     		}
	     		else if(temp.equals(Constant.FACEBOOK_PACKET)){
	     			if(sp.getBoolean(Constant.FACEBOOK_FALG, true)){
	     				if(isApp.getConnectBleDevice()){
	     					plusdotBleDevice = PlusDotBLEDevice.getInstance();
	     					if(isRemoveEvent){
	     						plusdotBleDevice.synchronizationSMSRespone(true);
	     					}else{
	     						plusdotBleDevice.synchronizationSMSRemindEvent(true);	
	     					}
	     				}
	     			}
	     		}
	     		// if the phone 's ble is on ,it will sent the message to smart-Watch.
	        }
	    }
	 
}
