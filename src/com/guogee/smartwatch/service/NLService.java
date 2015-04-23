package com.guogee.smartwatch.service;

import com.guogee.smartwatch.InCallReceiver;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.ble.PlusDotBLEDevice;
import com.guogee.smartwatch.utils.Constant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.CallLog.Calls;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NLService extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereciver;
    private SharedPreferences sp;
    private iSmartApplication isApp;
    
	private final ContentObserver mSmsMmsObserver = new SmsMmsContentObserver();
	private final ContentObserver mContactsObserver = new ContactsContentObserver();
	private PlusDotBLEDevice plusdotDevice;

    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.guogee.smartwatch.NOTIFICATION_LISTENER_SERVICE");
        registerReceiver(nlservicereciver,filter);
        
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        
        isApp = (iSmartApplication) getApplication();
        plusdotDevice = PlusDotBLEDevice.getInstance();
        
        //Phone
        InCallReceiver callReceiver = new InCallReceiver();
        callReceiver.setApp(isApp,sp);
        IntentFilter filter2 = new IntentFilter();
        registerReceiver(callReceiver, filter2);
        
        //register sms/phone ob server
        registerObserver();
    	UpdateUnAnsweredCalls(this);
		UpdateUnreadMmsSms(this);
		
        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
        unregisterObserver();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

//        Log.e(TAG,"**********  onNotificationPosted");
//        Log.e(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        
//        if(sbn.getPackageName().equals(arg0)){
//        }
        
        String packName = sbn.getPackageName();
        
        
//        Log.d("TAG", "packName ================== "+packName);

        Intent i = new  Intent("com.guogee.smartwatch.NOTIFICATION_LISTENER");
        
        i.putExtra("isRemoveEvent",false);
        
        i.putExtra("notification_event",sbn.getPackageName());
//        sendBroadcast(i);
        
        // webChat --- com.tencent.mm
        // QQ --- com.tencent.mobileqqi
        // QQ --- com.tencent.mobileqq
        // Email -- com.android.email
        // message -- com.android.mms
        // phone -- com.android.phone
        
        if(packName.equals(Constant.MESSAGE_PACKET)){
        	if(sp.getBoolean(Constant.SMS_FALG, true)){
        		sendBroadcast(i);
        	}
        }else if(packName.equals(Constant.EMAIL_PACKET) || packName.equals(Constant.OUTLOOK_PACKET)){
        	if(sp.getBoolean(Constant.EMAIL_FALG, true)){
        		sendBroadcast(i);
        	}
        }else if(packName.equals(Constant.QQI_PACKET)){
        	if(sp.getBoolean(Constant.QQ_FALG, true)){
        		sendBroadcast(i);
        	}
        }else if(packName.equals(Constant.QQ_PACKET)){
        	if(sp.getBoolean(Constant.QQ_FALG, true)){
        		sendBroadcast(i);
        	}
        }else if(packName.equals(Constant.WEBCHAT_PACKET)){
        	if(sp.getBoolean(Constant.WEBCHAT_FALG, true)){
        		sendBroadcast(i);
        	}
        }else if(packName.equals(Constant.SKYPE_PACKET)){
        	if(sp.getBoolean(Constant.SKYPE_FALG, true)){
        		sendBroadcast(i);
        	}
        }else if(packName.equals(Constant.WHATSAPP_PACKET)){
        	if(sp.getBoolean(Constant.WHATSAPP_FALG, true)){
        		sendBroadcast(i);
        	}
        }else if(packName.equals(Constant.FACEBOOK_PACKET)){
        	if(sp.getBoolean(Constant.FACEBOOK_FALG, true)){
        		sendBroadcast(i);
        	}
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
//        Log.e(TAG,"********** onNOtificationRemoved");
//        Log.e(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText +"\t" + sbn.getPackageName());
        
        String packName = sbn.getPackageName();
        
        Intent i = new  Intent("com.guogee.smartwatch.NOTIFICATION_LISTENER");
        
        i.putExtra("isRemoveEvent",true);
        
//        i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "\n");
        
        i.putExtra("notification_event",sbn.getPackageName());
        
        
     // webChat --- com.tencent.mm
        // QQ --- com.tencent.mobileqqi
        // QQ --- com.tencent.mobileqq
        // Email -- com.android.email
        // message -- com.android.mms
        // phone -- com.android.phone
        
        if(packName.equals(Constant.MESSAGE_PACKET)){
        	if(sp.getBoolean(Constant.SMS_FALG, true)){
        		sendBroadcast(i);
        	}
        }else if(packName.equals(Constant.EMAIL_PACKET) || packName.equals(Constant.OUTLOOK_PACKET)){
        	if(sp.getBoolean(Constant.EMAIL_FALG, true)){
        		sendBroadcast(i);
        	}
        }else if(packName.equals(Constant.QQI_PACKET)){
        	if(sp.getBoolean(Constant.QQ_FALG, true)){
        		sendBroadcast(i);
        	}
        }else if(packName.equals(Constant.QQ_PACKET)){
        	if(sp.getBoolean(Constant.QQ_FALG, true)){
        		sendBroadcast(i);
        	}
        }else if(packName.equals(Constant.WEBCHAT_PACKET)){
        	if(sp.getBoolean(Constant.WEBCHAT_FALG, true)){
        		sendBroadcast(i);
        	}
        }else if(packName.equals(Constant.SKYPE_PACKET)){
        	if(sp.getBoolean(Constant.SKYPE_FALG, true)){
        		sendBroadcast(i);
        	}
        }else if(packName.equals(Constant.WHATSAPP_PACKET)){
        	if(sp.getBoolean(Constant.WHATSAPP_FALG, true)){
        		sendBroadcast(i);
        	}
        }else if(packName.equals(Constant.FACEBOOK_PACKET)){
        	if(sp.getBoolean(Constant.FACEBOOK_FALG, true)){
        		sendBroadcast(i);
        	}
        }
       sendBroadcast(i);
    }

    class NLServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("clearall")){
                    NLService.this.cancelAllNotifications();
            }
            else if(intent.getStringExtra("command").equals("list")){
                Intent i1 = new  Intent("com.guogee.smartwatch.NOTIFICATION_LISTENER");
                i1.putExtra("notification_event","=====================");
                sendBroadcast(i1);
                int i=1;
                for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
                    Intent i2 = new  Intent("com.guogee.smartwatch.NOTIFICATION_LISTENER");
                    i2.putExtra("notification_event",i +" " + sbn.getPackageName() + "\n");
                    sendBroadcast(i2);
                    i++;
                }
                Intent i3 = new  Intent("com.guogee.smartwatch.NOTIFICATION_LISTENER");
                i3.putExtra("notification_event","===== Notification List ====");
                sendBroadcast(i3);
            }
        }
    }
    
    
    /////////////////////
    private static int SMS_NUM;
	
    private static int PHONE_NUM;
	
	/**
	 * 注册数据库监听
	 * 
	 */
	private void registerObserver() {
		// 介个Uri是你要监听的数据库
		// Uri.parse("content://l-message_summary") 我们公司特有的 可略过
		// android 原生api 应该为  content://sms 和 content://mms/inbox	
		getContentResolver().registerContentObserver(Uri.parse("content://sms"), true,mSmsMmsObserver);
		getContentResolver().registerContentObserver(Calls.CONTENT_URI, true,mContactsObserver);
	}

	/**
	 * 取消注册数据库监听
	 * 
	 */
	private void unregisterObserver() {
		getContentResolver().unregisterContentObserver(mSmsMmsObserver);
		getContentResolver().unregisterContentObserver(mContactsObserver);
	}
	
	/**
	 * 获取 未读短信数量
	 * 
	 * @param context
	 * @return
	 */
	public static void UpdateUnreadMmsSms(Context context) {
		Cursor cur = null;
		
		try {
			cur = context.getContentResolver().query(
					Uri.parse("content://sms"),
					null, "type = 1 and read = 0", null, null);
			if (null != cur) {
				SMS_NUM = cur.getCount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
//		Log.d("TAG","SMS_NUM ========= "+SMS_NUM);
	}

	/**
	 * 获取 未接电话
	 * 
	 * @param context
	 * @return
	 */
	public static void UpdateUnAnsweredCalls(Context context) {
		Cursor cur = null;
		try {
			cur = context.getContentResolver().query(Calls.CONTENT_URI, null,
					"type = 3 and new = 1", null, null);
			if (null != cur) {
				PHONE_NUM = cur.getCount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
	}

	
	/**
	 * 短信数据库监听
	 * 
	 */
	private class SmsMmsContentObserver extends ContentObserver {

		public SmsMmsContentObserver() {
			super(new Handler());
		}

		@Override
		public void onChange(boolean selfChange) {
//			Log.i("TAG", "SmsMmsContentObserver , onChange");
			// 大数据操作 在线程中 进行
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					UpdateUnreadMmsSms(getApplicationContext());
					mHandler.sendEmptyMessage(0);
				}
			});
			super.onChange(selfChange);
		}
	}
	
	/**
	 * 联系人数据库监听 -- 未接电话
	 * 
	 */
	private class ContactsContentObserver extends ContentObserver {

		public ContactsContentObserver() {
			super(new Handler());
		}

		@Override
		public void onChange(boolean selfChange) {
//			Log.i("TAG", "ContactsContentObserver , onChange");
			// 大数据操作 在线程中 进行
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					UpdateUnAnsweredCalls(getApplicationContext());
					mHandler.sendEmptyMessage(0);
				}
			});
			super.onChange(selfChange);
		}
	}
    
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
//			Log.i(TAG, "msg == " + msg);
//			updateUI();
//			Log.d("TAG", "短信："+SMS_NUM+"   未接："+PHONE_NUM);
			
			if(PHONE_NUM == 0){
				//取消表上灯
			    if(isApp.getConnectBleDevice()){
			    	plusdotDevice.synchronizationPhoneRespone(true);
			    }
			}
			
		    if(SMS_NUM == 0){
		    	//取消表上灯
			    if(isApp.getConnectBleDevice()){
			    	plusdotDevice.synchronizationSMSRespone(true);
			    }
		    }
			
			super.handleMessage(msg);
		}
	};

}
