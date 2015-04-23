package com.guogee.smartwatch.ble;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.ui.GuideActivity;
import com.guogee.smartwatch.ui.SettingActivity;
import com.guogee.smartwatch.ui.SplashActivity;
import com.guogee.smartwatch.utils.ByteDataConvert;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.utils.ConvertUTC;
import com.guogee.smartwatch.utils.Crc16Util;
import com.guogee.smartwatch.utils.UtcTime;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;

/**
 * ʹ�� ������ģʽ��
 * 
 * @author john
 *
 */
public class PlusDotBLEDevice {

	public static final String PLUSDOT_SERVICE = "00000a60-0000-1000-8000-00805f9b34fb";
	public static final String UUID_WRITE = "00000a66-0000-1000-8000-00805f9b34fb";
	public static final String UUID_READ_NOTIFICATION = "00000a67-0000-1000-8000-00805f9b34fb";
	
	private String address;
	private String deviceType;
	private String device_id;
	private String company_id;
	public long lasedataTime=0;
	
	private SharedPreferences share = null;
//	private SharedPreferences.Editor edit = null;
	
	private iSmartApplication isApp;
	
	//////////////////////-----------ʹ�õ���ģʽ--------------///////////////////////////
	
	public void setSharedPreferencesInfo(SharedPreferences share, iSmartApplication app){
		this.share = share;
//		this.edit = edit;
		isApp = app;
	}
	
	private PlusDotBLEDevice (){
//		bleMessageQueueManagement = new BLEMessageQueueManagement();
//		new SyncDataThread().start();//�½�������CustomThreadʵ��  
	}
	
	private static final PlusDotBLEDevice singleDevice = new PlusDotBLEDevice();
	
	public static PlusDotBLEDevice getInstance(){
		return singleDevice;
	}
	
    //////////////////////-----------ʹ�õ���ģʽ--------------///////////////////////////
	
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	private String device_name;
	private String partners;
	
	private BluetoothGatt gatt;
	private BluetoothGattCharacteristic writeGattCharacteristic;
	private BluetoothGattCharacteristic read_notificationGattCharacteristic;
	private  static final  String TAG = PlusDotBLEDevice.class.getSimpleName();
	
//	private  Timer eventtimer ;  //���Ӷ�ʱ��
//	private TimerTask 	eventtask;//����������֤����

	private  Timer emailTimer ;  
	private TimerTask 	emailtask;
	
	private  Timer phoneTimer ;  
	private TimerTask 	phonetask;
	
	private  Timer smsTimer ;  
	private TimerTask 	smstask;
	
	
	private  Timer connverifytimer ;  //���Ӷ�ʱ��
	private TimerTask 	connverifytask;//����������֤����
	
	private   Timer synchronizationTimeTimer;
	private TimerTask 	synchronizationTimeTimertask;//ʱ��ͬ������

	private   Timer synchronizationTargetTimer;
	private TimerTask 	synchronizationTargetTimertask;//ʱ��ͬ������
	
	private   Timer synchronizationInfoTimer;
	private TimerTask 	synchronizationInfoTimertask;//������Ϣͬ������
	
	private   Timer synchronizationDataTimer;
	private TimerTask 	synchronizationDataTimertask;
	
	private   Timer synchronizationDeviceTimer;
	private TimerTask 	synchronizationDeviceTimertask;//�豸��Ϣͬ������
	
	// �˶�
	private   Timer synchronizationSportBeginTimer;
	private TimerTask 	synchronizationSportBeginTimertask;//����ͬ������
	
	private   Timer synchronizationSportTimer;
	private TimerTask 	synchronizationSportTimertask;//����ͬ������
	
	private   Timer synchronizationSportEndTimer;
	private TimerTask 	synchronizationSportEndTimertask;//����ͬ������
	//�˶�
	
	// ˯��
	private   Timer synchronizationSleepBeginTimer;
	private TimerTask 	synchronizationSleepBeginTimertask;//����ͬ������
	
	private   Timer synchronizationSleepBeginOneTimer;
	private TimerTask 	synchronizationSleepBeginOneTimertask;//����ͬ������
	
	private   Timer synchronizationSleepTimer;
	private TimerTask 	synchronizationSleepTimertask;//����ͬ������
	
	private   Timer synchronizationSleepEndTimer;
	private TimerTask 	synchronizationSleepEndTimertask;//����ͬ������
	// ˯��
	
	//����
	private   Timer synchronizationClockTimer;
	private TimerTask 	synchronizationClockTimertask;//����ͬ������
	
	//�����˶����� (0x08)
	private   Timer synchronizationSportClockTimer;
	private TimerTask 	synchronizationSportClockTimertask;//����ͬ������
	
	//����
	private   Timer synchronizationTrackerTimer;
	private TimerTask 	synchronizationTrackerTimertask;//����ͬ������
	
	//phone respone.
	private   Timer synchronizationPhoneResponeTimer;
	private TimerTask 	synchronizationPhoneResponeTimertask;//����ͬ������

	//SMS respone.
	private   Timer synchronizationSMSResponeTimer;
	private TimerTask 	synchronizationSMSResponeTimertask;//����ͬ������

	//bind command.
	private   Timer bindTimer;
	private TimerTask 	bindTimertask;//����ͬ������
	
//	public static byte[] CONNECTION_VERIFY={(byte) 		 		0xA6,0x00,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//	public static byte[] SYNCHRONIZATION_TIME={(byte)			0xA6,0x00,0x03,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//	public static byte[] SYNCHRONIZATION_REQUEST={(byte) 		0xA6,0x00,0x04,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//	public static byte[] SYNCHRONIZATION_DATA_ANSWER={(byte)	0xA6,0x00,0x05,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//	public static byte[] LIVE_DATA_ANSWER={(byte) 				0xA6,0x00,0x06,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	
	/** ����ʱ��cmd (0x01) **/
	public static byte[] SYNCHRONIZATION_TIME={(byte)			0xA6,0x27,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	/** ���ø�����Ϣ (0x02) **/
	public static byte[] SYNCHRONIZATION_INFO={(byte)			0xA6,0x27,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	/** �������� (0x03) **/
	public static byte[] SYNCHRONIZATION_CLOCK={(byte)			0xA6,0x27,0x03,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	/** ����Ŀ�� (0x04) **/ ////bug -- �������� 0x03
	public static byte[] SYNCHRONIZATION_TARGET={(byte)			0xA6,0x27,0x04,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	
	
	/** ����������(0x05) mode:���ѷ�ʽ,0x00(������), 0x01(������), 0x02(��������), 0x03 (����������) **/
	public static byte[] SYNCHRONIZATION_REMIND={(byte)			0xA6,0x27,0x05,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	/** ����豸��ص���������汾�š���ǰ�������˶�ʱ�����ݡ�˯�߽���ʱ���˯�߳���ʱ��(0x0A) **/ //BUG -- û����
	public static byte[] SYNCHRONIZATION_DEVICE_INFO={(byte)			0xA6,0x27,0x0A,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	/** ��ȡ�豸��ʱ��cmd (0x0B) **/ //BUG -- û����
	public static byte[] SYNCHRONIZATION_DEVICE_TIME={(byte)			0xA6,0x27,0x0B,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	
	/** �����˶����� (0x08) **/
	public static byte[] SYNCHRONIZATION_SPORT_REMIND_CLOCK={(byte)			0xA6,0x27,0x08,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};

	/** ���� **/
	public static byte[] SYNCHRONIZATION_TRACKER = {(byte)			0xA6,0x27,0x09,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	
	/** ����ģʽ **/
	public static byte[] SYNCHRONIZATION_REMINDER_MODE = {(byte)			0xA6,0x27,(byte) 0xf1,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};

	/** OTA ���� **/
	public static byte[] SYNCHRONIZATION_OTA_COMMAND = {(byte)			0xA6,0x27,0x32,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	private   Timer synchronizationOTATimer;
	private   TimerTask 	synchronizationOTATimertask;//����ͬ������
	
	public void OTACommand(boolean enabled){
		if(!enabled){
			if(synchronizationOTATimer!=null){
				synchronizationOTATimer.cancel();
				synchronizationOTATimer = null;
			}
		}else{
			if(synchronizationOTATimer!=null){
				return;
			}
			synchronizationOTATimertask = new TimerTask() {  
			    public void run() {
					byte[] datab =SYNCHRONIZATION_OTA_COMMAND.clone();
					writeCharacteristic(datab); //Bug --  null pointer
			    }  
			}; 
			synchronizationOTATimer = new Timer(); 
			synchronizationOTATimer.schedule(synchronizationOTATimertask,100,2000);
		}
	}
	
	
	/** OTA ���� **/
	private   Timer synchronizationReminderModeTimer;
	private   TimerTask 	synchronizationReminderModeTimertask;//����ͬ������
	
	
	/**
	 *    APP                      Ble device
	 *           ׼��ͬ���������� 0x13 
	 *        -------------------> 
	 *          �ظ�׼��ͬ���������� 0x13
	 *        <-------------------
	 *             ͬ���������� 0x14
	 *        -------------------> 
	 *         �ظ�ͬ��������������� 0x14
	 *        <------------------- 
	 *            ����ͬ���������� 0x15
	 *        ------------------->    
	 *          �ظ�����ͬ���������� 0x15
	 *        <-------------------  
	 *          
	 */
	public static byte[] SYNCHRONIZATION_SPORT_BEGIN ={(byte)			0xA6,0x27,0x13,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	public static byte[] SYNCHRONIZATION_SPORT       ={(byte)			0xA6,0x27,0x14,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	public static byte[] SYNCHRONIZATION_SPORT_END   ={(byte)			0xA6,0x27,0x15,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	
	public static byte[] SYNCHRONIZATION_SLEEP_BEGIN ={(byte)			0xA6,0x27,0x16,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	public static byte[] SYNCHRONIZATION_SLEEP_BEGIN_ONE ={(byte)			0xA6,0x27,0x19,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	
	public static byte[] SYNCHRONIZATION_SLEEP       ={(byte)			0xA6,0x27,0x017,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	public static byte[] SYNCHRONIZATION_SLEEP_END   ={(byte)			0xA6,0x27,0x018,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	
	public static byte[] SYNCHRONIZATION_REQUEST={(byte) 		0xA6,0x27,0x04,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	public static byte[] SYNCHRONIZATION_DATA_ANSWER={(byte)	0xA6,0x27,0x05,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	public static byte[] LIVE_DATA_ANSWER={(byte) 				0xA6,0x27,0x06,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	
	
	// ---------- �����¼� ----------// 
	/** �������� **/  //bug -- ���ص�Ӧ����0x21,�����ص��� 0x20
	public static byte[] SYNCHRONIZATION_PHONE_REMIND={(byte) 				0xA6,0x27,0x20,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	/** ���ţ�΢�ţ�QQ,Twitter,Facebook������ **/
	public static byte[] SYNCHRONIZATION_SMS_REMIND={(byte) 				0xA6,0x27,0x20,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	/** �ʼ����� **/
	public static byte[] SYNCHRONIZATION_EMAIL_REMIND={(byte) 				0xA6,0x27,0x20,0x03,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	/** BLE �ظ� �������� **/
	public static byte[] PHONE_RESPONE_FLAG={(byte) 			0xA6,0x27,0x21,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	/** BLE �ظ� ���ţ�΢�ţ�QQ,Twitter,Facebook������ **/
	public static byte[] SMS_RESPONE_FLAG={(byte) 				0xA6,0x27,0x21,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	/** BLE �ظ� �ʼ����� **/
	public static byte[] EMAIL_RESPONE_FLAG={(byte) 			0xA6,0x27,0x21,0x03,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	// ---------- �����¼� ---------- //
	/** ���� **/  
	public static byte[] SYNCHRONIZATION_PHOTOGRAPH={(byte) 				0xA6,0x27,(byte) 0xf2,0x01,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	
	//Bind command
	public static byte[] BIND_COMMAND = {(byte) 	0xA6,0x27,0x07,0x03,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	//unBind command
	public static byte[] UNBIND_COMMAND = {(byte) 	0xA6,0x27,0x07,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	//upgrade command
	public static byte[] UPGRADE_COMMAND = {(byte) 	0xA6,0x27,0x30,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	
	
	/** ����֮����ܻ���ָ�λ����λ�ᵼ�����Ƶ绰������ ͼ�겻�����󶨵�ʱ������������������ **/
	public static byte[] SYNCHRONIZATION_RESET_MODE={(byte)  0xA6,0x27,(byte) 0xf4,(byte) 0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	private   Timer synchronizationResetModeTimer;
	private   TimerTask 	synchronizationResetModeTimertask;//����ͬ������

	
	
//	public void synchronizationReset(boolean enabled){
//		if(!enabled){
//			if(synchronizationResetModeTimer!=null){
//				synchronizationResetModeTimer.cancel();
//				synchronizationResetModeTimer = null;
//			}
//		}else{
//			if(synchronizationResetModeTimer!=null){
//				return;
//			}
//			synchronizationResetModeTimertask = new TimerTask() {  
//			    public void run() {
//					byte[] datab =SYNCHRONIZATION_RESET_MODE.clone();
//					writeCharacteristic(datab); //Bug --  null pointer
//			    }  
//			}; 
//			synchronizationResetModeTimer = new Timer(); 
//			synchronizationResetModeTimer.schedule(synchronizationResetModeTimertask,2000,5000);
//		}
//	}

	
	public void synchronizationReset(){
		byte[] datab =SYNCHRONIZATION_RESET_MODE.clone();
		writeCharacteristic(datab);
	}

	/** ����֮����ܻ���ָ�λ����λ�ᵼ�����Ƶ绰������ ͼ�겻�����󶨵�ʱ������������������ **/
	
	
	/** ------------- ��оУ׼ ------------- **/
	public static byte[] SYNCHRONIZATION_Calibration={(byte) 	0xA6,0x27,(byte) 0x0f,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	private   Timer synchronizationCalibrationTimer;
	private   TimerTask 	synchronizationCalibrationTimertask;//����ͬ������
	
	public void synchronizationCalibration(int state){
		byte[] datab =SYNCHRONIZATION_Calibration.clone();
		switch(state){
		case 0:
			datab[3] = 0x00;
			break;
		case 1:
			datab[3] = 0x01;
			break;
		case 2:
			datab[3] = 0x02;
			break;
		case 3:
			datab[3] = 0x03;
			break;
		}
		writeCharacteristic(datab); //Bug --  null pointer
	}
	
	/** ------------- ��оУ׼ ------------- **/
	
	public boolean mDeviceRespondOk = false;
	
	public void synchronizationPhotograph(){
		byte[] datab =SYNCHRONIZATION_PHOTOGRAPH.clone();
		writeCharacteristic(datab); //Bug --  null pointer
	}
	
	public void UpgradeCommand(){
		Log.d("TAG","upgrade Command ............... ");
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				byte[] datab =UPGRADE_COMMAND.clone();
				writeCharacteristic(datab); //Bug --  null pointer
			}
		}, 1000);
	}
	
	public void BindCommand(boolean enabled){
		
		Log.d("TAG","Send Bind Command ................... ");
		
		if(!enabled){
			if(bindTimer!=null){
				bindTimer.cancel();
				bindTimer = null;
			}
		}else{
			if(bindTimer!=null){
				return;
			}
			bindTimertask = new TimerTask() {  
			    public void run() {
					byte[] datab =BIND_COMMAND.clone();
					writeCharacteristic(datab); //Bug --  null pointer
			    }  
			}; 
			bindTimer = new Timer(); 
			bindTimer.schedule(bindTimertask,1000,1000);
		}
	}
	
	public void unBindCommand(){
		Log.d("TAG","unBindCommand ............... ");
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				byte[] datab =UNBIND_COMMAND.clone();
				writeCharacteristic(datab); //Bug --  null pointer
			}
		}, 500);
	}
	 
	//phone respone,begin
	/**
	 * synchronization phone respone.
	 * 
	 * @param enabled
	 */
	public void synchronizationPhoneRespone(boolean enabled){
		if(isApp==null){
			return;
		}
		if(share.getBoolean(Constant.PHONE_FALG, true) && isApp.getConnectBleDevice()){
			if(!enabled){
				if(synchronizationPhoneResponeTimer!=null){
					synchronizationPhoneResponeTimer.cancel();
					synchronizationPhoneResponeTimer = null;
				}
			}else{
				if(synchronizationPhoneResponeTimer!=null){
					return;
				}
				synchronizationPhoneResponeTimertask = new TimerTask() {  
				    public void run() {
						byte[] datab =PHONE_RESPONE_FLAG.clone();
						writeCharacteristic(datab); //Bug --  null pointer
				    }  
				}; 
				synchronizationPhoneResponeTimer = new Timer(); 
				synchronizationPhoneResponeTimer.schedule(synchronizationPhoneResponeTimertask,1000,3000);
			}
		}
	}
	//phone respone,end
	
	//sms respone,begin
	/**
	 * synchronization SMS respone.
	 * 
	 * @param enabled
	 */
	public void synchronizationSMSRespone(boolean enabled){
		if(!enabled){
			if(synchronizationSMSResponeTimer!=null){
				synchronizationSMSResponeTimer.cancel();
				synchronizationSMSResponeTimer = null;
			}
		}else{
			if(synchronizationSMSResponeTimer!=null){
				return;
			}
			synchronizationSMSResponeTimertask = new TimerTask() {  
			    public void run() {
					byte[] datab = SMS_RESPONE_FLAG.clone();
					writeCharacteristic(datab); //Bug --  null pointer
			    }  
			}; 
			synchronizationSMSResponeTimer = new Timer(); 
			synchronizationSMSResponeTimer.schedule(synchronizationSMSResponeTimertask,1000,3000);
		}
	}
	//sms respone,end
	
	
	//Email respone,begin
		/**
		 * synchronization email respone.
		 * 
		 * @param enabled
		 */
		public void synchronizationEmailRespone(){                
			byte[] datab =EMAIL_RESPONE_FLAG.clone();
			writeCharacteristic(datab); //Bug --  null pointer
		}
    //Email respone,end
	
	
	// reminder mode
	/**
	 * synchronization reminder mode
	 * 
	 * @param enabled
	 */
	public void synchronizationReminderMode(final String mode,boolean enabled){
		if(!enabled){
			if(synchronizationReminderModeTimer!=null){
				synchronizationReminderModeTimer.cancel();
				synchronizationReminderModeTimer = null;
			}
		}else{
			
			if(synchronizationReminderModeTimer!=null){
				return;
			}
			
			synchronizationReminderModeTimertask = new TimerTask() {  
			    public void run() {
			    	
			    	byte[] datab =SYNCHRONIZATION_REMIND.clone();
			    	
			    	if(mode.equals(Constant.REMINDER_VOICE_MODE)){
			    		ByteDataConvert.IntToBin(2, datab, 3, 1);
			    	}else if(mode.equals(Constant.REMINDER_SHAKE_MODE)){
			    		ByteDataConvert.IntToBin(1, datab, 3, 1);
			    	}else if(mode.equals(Constant.REMINDER_VOICE_SHAKE_MODE)){
			    		ByteDataConvert.IntToBin(3, datab, 3, 1);
			    	}
			    	
			    	int sms = 1;
					int phone = 1;
					int email = 1;
					int qq = 1;
					int webchat = 1;
					int sina = 1;
					int facebook = 1;
					int twitter = 1;
			    	
					byte[] msg = {(byte) sms,(byte) phone,(byte) email,(byte) qq,(byte) webchat,(byte) sina,(byte) facebook,(byte) twitter};
//					SYNCHRONIZATION_REMIND[4] = ByteDataConvert.getByteByArray(msg);
					
					datab[4] = ByteDataConvert.getByteByArray(msg);
					writeCharacteristic(datab); 
			    }   
			}; 
			synchronizationReminderModeTimer = new Timer(); 
			synchronizationReminderModeTimer.schedule(synchronizationReminderModeTimertask,1000,4000);
		}
	}
	
	public void synchronizationInitReminderMode(){
	 	byte[] datab =SYNCHRONIZATION_REMIND.clone();
    	
//    	if(mode.equals(Constant.REMINDER_VOICE_MODE)){
//    		ByteDataConvert.IntToBin(2, datab, 3, 1);
//    	}else if(mode.equals(Constant.REMINDER_SHAKE_MODE)){
//    		ByteDataConvert.IntToBin(1, datab, 3, 1);
//    	}else if(mode.equals(Constant.REMINDER_VOICE_SHAKE_MODE)){
    		ByteDataConvert.IntToBin(3, datab, 3, 1);
//    	}
    	
    	int sms = 1;
		int phone = 1;
		int email = 1;
		int qq = 1;
		int webchat = 1;
		int sina = 1;
		int facebook = 1;
		int twitter = 1;
    	
		byte[] msg = {(byte) sms,(byte) phone,(byte) email,(byte) qq,(byte) webchat,(byte) sina,(byte) facebook,(byte) twitter};
//		SYNCHRONIZATION_REMIND[4] = ByteDataConvert.getByteByArray(msg);
		
		datab[4] = ByteDataConvert.getByteByArray(msg);
		writeCharacteristic(datab); 
	}
	// reminder mode
	
	//clock begin
	/**
	 * synchronization clock data
	 * 
	 * @param enabled
	 */
	public void synchronizationClockData(boolean enabled){
		if(!enabled){
			if(synchronizationClockTimer!=null){
				synchronizationClockTimer.cancel();
				synchronizationClockTimer = null;
			}
		}else{
			
			if(synchronizationClockTimer!=null){
				return;
			}
			
			synchronizationClockTimertask = new TimerTask() {  
			    public void run() {
					byte[] datab =SYNCHRONIZATION_CLOCK.clone();
					
					int intervalTime = share.getInt(Constant.SMART_INTERVAL_TIME, 10);
					int repetitions = share.getBoolean(Constant.SLEEP_REMINDER_HINT, true) == true ? 1 : 0 ;
					
					String time = share.getString(Constant.SMART_START_TIME, "09:00");
					int day1 = share.getBoolean(Constant.SMART_CLOCK_DAY1, false) == true ? 1 : 0;
					int day2 = share.getBoolean(Constant.SMART_CLOCK_DAY2, false) == true ? 1 : 0;
					int day3 = share.getBoolean(Constant.SMART_CLOCK_DAY3, false) == true ? 1 : 0;
					int day4 = share.getBoolean(Constant.SMART_CLOCK_DAY4, false) == true ? 1 : 0;
					int day5 = share.getBoolean(Constant.SMART_CLOCK_DAY5, false) == true ? 1 : 0;
					int day6 = share.getBoolean(Constant.SMART_CLOCK_DAY6, false) == true ? 1 : 0;
					int day7 = share.getBoolean(Constant.SMART_CLOCK_DAY7, false) == true ? 1 : 0;
					int id = 1;

					Log.d("TAG","hour ======= "+time.substring(0, 2)+" minute ======== "+time.substring(3, 5));
					
					int hour = Integer.parseInt(time.substring(0, 2));
					int minute = Integer.parseInt(time.substring(3, 5));
					
					ByteDataConvert.IntToBin(id, datab, 3, 1);
					ByteDataConvert.IntToBin(hour, datab, 4, 1);
					ByteDataConvert.IntToBin(minute, datab, 5, 1);
					
					byte[] week = {(byte) repetitions,(byte) 1,(byte) 1,(byte) 1,(byte) 1,(byte) 1,(byte) 1,(byte) 1};
					
//					byte[] week = {(byte) repetitions,(byte) day1,(byte) day2,(byte) day3,(byte) day4,(byte) day5,(byte) day6,(byte) day7};
					
//					if(day1==0 && day2==0 && day3==0 && day4==0 && day5==0 && day6==0 && day7==0){
//						int currentday = getCurrentDay();
//						switch(currentday){
//						case 1:
//							week[1] = (byte)1;
//							break;
//						case 2:
//							week[2] = (byte)1;
//							break;
//						case 3:
//							week[3] = (byte)1;
//							break;
//						case 4:
//							week[4] = (byte)1;
//							break;
//						case 5:
//							week[5] = (byte)1;
//							break;
//						case 6:
//							week[6] = (byte)1;
//							break;
//						case 7:
//							week[7] = (byte)1;
//							break;
//						}
//					}
					
					datab[6] = ByteDataConvert.getByteByArray(week);

					ByteDataConvert.IntToBin(intervalTime, datab, 7, 1);
					writeCharacteristic(datab); //Bug --  null pointer��solved��
			    }  
			}; 
			synchronizationClockTimer = new Timer(); 
			synchronizationClockTimer.schedule(synchronizationClockTimertask,1000,1000);
		}
	}
	
	
	/**
	 * synchronization sport clock data
	 * 
	 * @param enabled
	 */
	public void synchronizationSportClockData(boolean enabled){
		
		if(!enabled){
			if(synchronizationSportClockTimer!=null){
				synchronizationSportClockTimer.cancel();
				synchronizationSportClockTimer = null;
			}
		}else{
			
			if(synchronizationSportClockTimer!=null){
				return;
			}
			
			synchronizationSportClockTimertask = new TimerTask() {  
			    public void run() {
					byte[] datab =SYNCHRONIZATION_SPORT_REMIND_CLOCK.clone();
					
//					int intervalTime = share.getInt(Constant.SMART_INTERVAL_TIME, 15);
//					String time = share.getString(Constant.SMART_START_TIME, "09:00");
					
					int intervalTime = share.getInt(Constant.EXERCISE_INTERVAL_TIME, 15);
					String exercise_start = share.getString(Constant.EXERCISE_START_TIME, "09:00");
					String exercise_end = share.getString(Constant.EXERCISE_END_TIME, "18:00");
					
					int repetitions = share.getBoolean(Constant.ACTIVITY_REMINDER_HINT, true) == true ? 1 : 0 ;
					int day1 = share.getBoolean(Constant.EXERCISE_CLOCK_DAY1, false) == true ? 1 : 0;
					int day2 = share.getBoolean(Constant.EXERCISE_CLOCK_DAY2, false) == true ? 1 : 0;
					int day3 = share.getBoolean(Constant.EXERCISE_CLOCK_DAY3, false) == true ? 1 : 0;
					int day4 = share.getBoolean(Constant.EXERCISE_CLOCK_DAY4, false) == true ? 1 : 0;
					int day5 = share.getBoolean(Constant.EXERCISE_CLOCK_DAY5, false) == true ? 1 : 0;
					int day6 = share.getBoolean(Constant.EXERCISE_CLOCK_DAY6, false) == true ? 1 : 0;
					int day7 = share.getBoolean(Constant.EXERCISE_CLOCK_DAY7, false) == true ? 1 : 0;
					
					int startTime = Integer.parseInt(exercise_start.substring(0, 2));
					int endTime = Integer.parseInt(exercise_end.substring(0, 2));
					
//					Log.e("TAG", "intervalTime ===="+intervalTime+" startTime ===="+startTime+" endTime ===="+endTime);
//					Log.e("TAG"," repetitions ===="+repetitions+" day1 ===="+day1+" day2 ===="+day2+" day3 ===="+day3+" day4 ===="+day4+" day5 ===="+day5+" day6 ===="+day6+" day7===="+day7);
					
					ByteDataConvert.IntToBinByLow(intervalTime, datab, 3, 2);//(id, datab, 3, 1);
					
					byte[] week = {(byte) repetitions,(byte) 1,(byte) 1,(byte) 1,(byte) 1,(byte) 1,(byte) 1,(byte) 1};	
					
					
//					byte[] week = {(byte) repetitions,(byte) day1,(byte) day2,(byte) day3,(byte) day4,(byte) day5,(byte) day6,(byte) day7};	
					
//					if(day1==0 && day2==0 && day3==0 && day4==0 && day5==0 && day6==0 && day7==0){
//						int currentday = getCurrentDay();
//						switch(currentday){
//						case 1:
//							week[1] = (byte)1;
//							break;
//						case 2:
//							week[2] = (byte)1;
//							break;
//						case 3:
//							week[3] = (byte)1;
//							break;
//						case 4:
//							week[4] = (byte)1;
//							break;
//						case 5:
//							week[5] = (byte)1;
//							break;
//						case 6:
//							week[6] = (byte)1;
//							break;
//						case 7:
//							week[7] = (byte)1;
//							break;
//						}
//					}
					
//					SYNCHRONIZATION_SPORT_REMIND_CLOCK[5] = ByteDataConvert.getByteByArray(week); //BUG.
					
					datab[5] = ByteDataConvert.getByteByArray(week);
					
					ByteDataConvert.IntToBin(startTime, datab, 6, 1);
					ByteDataConvert.IntToBin(endTime, datab, 8, 1);

					writeCharacteristic(datab); //Bug --  null pointer (solved)
			    }  
			}; 
			synchronizationSportClockTimer = new Timer(); 
			synchronizationSportClockTimer.schedule(synchronizationSportClockTimertask,1000,1000);
		}
	}
	
	public void setClockData(){
		
	}
	//clock end
	
	/**
	 * synchronization Sport begin data
	 * 
	 * @param enabled
	 */
	public void synchronizationSportBeginData(boolean enabled){
		if(!enabled){
			if(synchronizationSportBeginTimer!=null){
				synchronizationSportBeginTimer.cancel();
				synchronizationSportBeginTimer = null;
			}
		}else{
			if(synchronizationSportBeginTimer!=null){
				return;
			}
			synchronizationSportBeginTimertask = new TimerTask() {  
			    public void run() {
					byte[] datab =SYNCHRONIZATION_SPORT_BEGIN.clone();
					writeCharacteristic(datab); //Bug --  null pointer
			    }  
			}; 
			synchronizationSportBeginTimer = new Timer(); 
			synchronizationSportBeginTimer.schedule(synchronizationSportBeginTimertask,1000,1000);
		}
	}
	
	/**
	 * synchronization Sport end data
	 * 
	 * @param enabled
	 */
	public void synchronizationSportEndData(boolean enabled){
		if(!enabled){
			if(synchronizationSportEndTimer!=null){
				synchronizationSportEndTimer.cancel();
				synchronizationSportEndTimer = null;
			}
		}else{
			
			if(synchronizationSportEndTimer!=null){
				return;
			}
			
			synchronizationSportEndTimertask = new TimerTask() {  
			    public void run() {
					byte[] datab =SYNCHRONIZATION_SPORT_END.clone();
					writeCharacteristic(datab); //Bug --  null pointer
			    }  
			}; 
			synchronizationSportEndTimer = new Timer(); 
			synchronizationSportEndTimer.schedule(synchronizationSportEndTimertask,1000,1000);
		}
	}
	
	/**
	 * synchronization Sport data
	 * 
	 * @param enabled
	 */
//	private int index = 1;
	
//	public void synchronizationSportData(boolean enabled,final int index){
//		if(!enabled){
//			if(synchronizationSportTimer!=null)synchronizationSportTimer.cancel();
//		}else{
//			synchronizationSportTimertask = new TimerTask() {  
//			    public void run() {
//					byte[] datab =SYNCHRONIZATION_SPORT.clone();
//					
//					Log.d("TAG", "index ======== "+index);
//					if(index == 0){
//						ByteDataConvert.IntToBinByLow(1, datab, 3, 2);						
//					}{
//						ByteDataConvert.IntToBinByLow(index, datab, 3, 2);						
//					}
//					writeCharacteristic(datab); //Bug --  null pointer
//			    }  
//			}; 
//			
//			synchronizationSportTimer = new Timer(); 
//			synchronizationSportTimer.schedule(synchronizationSportTimertask,100,1000);
//		}
//	}
	
	public void synchronizationSportData(final int index){
		byte[] datab =SYNCHRONIZATION_SPORT.clone();
		ByteDataConvert.IntToBinByLow(index, datab, 3, 2);						
		writeCharacteristic(datab); //Bug --  null pointer
	}
	
	// sleep
	/**
	 * synchronization Sleep begin data
	 * 
	 * @param enabled
	 */
	public void synchronizationSleepBeginData(boolean enabled){
		if(!enabled){
			if(synchronizationSleepBeginTimer!=null){
				synchronizationSleepBeginTimer.cancel();
				synchronizationSleepBeginTimer = null;
			}
		}else{
			if(synchronizationSleepBeginTimer!=null){
				return;
			}
			synchronizationSleepBeginTimertask = new TimerTask() {  
			    public void run() {
					byte[] datab =SYNCHRONIZATION_SLEEP_BEGIN.clone();
					writeCharacteristic(datab); //Bug --  null pointer
			    }  
			}; 
			synchronizationSleepBeginTimer = new Timer(); 
			synchronizationSleepBeginTimer.schedule(synchronizationSleepBeginTimertask,100,3000);
		}
	}
	
	/**
	 * synchronization Sleep begin data
	 * 
	 * @param enabled
	 */
	public void synchronizationSleepBeginOneData(boolean enabled){
		if(!enabled){
			if(synchronizationSleepBeginOneTimer!=null){
				synchronizationSleepBeginOneTimer.cancel();
				synchronizationSleepBeginOneTimer = null;
			}
		}else{
			if(synchronizationSleepBeginOneTimer!=null){
				return;
			}
			synchronizationSleepBeginOneTimertask = new TimerTask() {  
			    public void run() {
					byte[] datab =SYNCHRONIZATION_SLEEP_BEGIN_ONE.clone();
					writeCharacteristic(datab); 
			    }  
			}; 
			synchronizationSleepBeginOneTimer = new Timer(); 
			synchronizationSleepBeginOneTimer.schedule(synchronizationSleepBeginOneTimertask,100,300);
		}
	}
	
	/**
	 * synchronization Sleep end data
	 * 
	 * @param enabled
	 */
	public void synchronizationSleepEndData(boolean enabled){
		if(!enabled){
			if(synchronizationSleepEndTimer!=null){
				synchronizationSleepEndTimer.cancel();
				synchronizationSleepEndTimer = null;
			}
		}else{
			if(synchronizationSleepEndTimer!=null){
				return;
			}
			synchronizationSleepEndTimertask = new TimerTask() {  
			    public void run() {
					byte[] datab =SYNCHRONIZATION_SLEEP_END.clone();
					writeCharacteristic(datab); //Bug --  null pointer
			    }  
			}; 
			synchronizationSleepEndTimer = new Timer(); 
			synchronizationSleepEndTimer.schedule(synchronizationSleepEndTimertask,100,1000);
		}
	}
	
	public void synchronizationSleepData(final int index){
		Log.d("TAG", "index ======== "+index);
		byte[] datab =SYNCHRONIZATION_SLEEP.clone();
		ByteDataConvert.IntToBinByLow(index, datab, 3, 2);						
		writeCharacteristic(datab); //Bug --  null pointer
	}
	
	
//	public void synchronizationSleepData(boolean enabled,final int index){
//		if(!enabled){
//			if(synchronizationSleepTimer!=null)synchronizationSleepTimer.cancel();
//		}else{
//			synchronizationSleepTimertask = new TimerTask() {  
//			    public void run() {
//					byte[] datab =SYNCHRONIZATION_SLEEP.clone();
//					
//					Log.d("TAG", "index ======== "+index);
//					if(index==0){
//						ByteDataConvert.IntToBinByLow(1, datab, 3, 2);						
//					}else{
//						ByteDataConvert.IntToBinByLow(index, datab, 3, 2);						
//					}
//					
//					writeCharacteristic(datab); //Bug --  null pointer
//			    }  
//			}; 
//			
//			synchronizationSleepTimer = new Timer(); 
//			synchronizationSleepTimer.schedule(synchronizationSleepTimertask,100,300);
//		}
//	}
	// sleep
	
	
	/**
	 * synchronization Sleep data
	 * 
	 * @param enabled
	 */
//	public void synchronizationSleepData(boolean enabled,final byte state){
//		if(!enabled){
//			if(synchronizationSleepTimer!=null)synchronizationSleepTimer.cancel();
//		}else{
//			synchronizationSleepTimertask = new TimerTask() {  
//			    public void run() {
//					byte[] datab =SYNCHRONIZATION_SLEEP_BEGIN.clone();
//					if(state == 0x16){ //˯��׼��ͬ�� 0x16
//						datab = SYNCHRONIZATION_SLEEP_BEGIN.clone();
//					}else if(state == 0x17){ //˯��ͬ������ 0x17
//						datab = SYNCHRONIZATION_SLEEP.clone();
//					}else if(state == 0x18){ //˯��ͬ������ 0x18
//						datab = SYNCHRONIZATION_SLEEP_END.clone();
//					}
//					writeCharacteristic(datab);
//			    }  
//			}; 
//			synchronizationSleepTimer = new Timer(); 
//			synchronizationSleepTimer.schedule(synchronizationSleepTimertask,1000,3000);
//		}
//	}
	
	// tracker
	/**
	 * synchronization tracker
	 * 
	 * @param enabled
	 */
	public void synchronizationTracker(boolean enabled) {
		if(isApp==null){
			return;
		}
		if (isApp.getConnectBleDevice()) {
			if (!enabled) {
				if (synchronizationTrackerTimer != null)
				{
				   synchronizationTrackerTimer.cancel();
				   synchronizationTrackerTimer = null;
				}
			} else {
				
				if(synchronizationTrackerTimer!=null){
					return;
				}
				
				synchronizationTrackerTimertask = new TimerTask() {
					public void run() {
						byte[] datab = SYNCHRONIZATION_TRACKER.clone();
						byte track;
						if (share.getBoolean(Constant.TRACKER_FLAG, false) == true)
							track = 0x01;
						else
							track = 0x00;
//						Log.d("TAG","track ================ "+track);
						ByteDataConvert.IntToBin(track, datab, 3, 1);
						ByteDataConvert.IntToBin(60, datab, 4, 1);
//						Log.d("TAG","track 's time ================ "+share.getInt(Constant.TRACKER_TIME_FLAG, 20));
						ByteDataConvert.IntToBin(share.getInt(Constant.TRACKER_TIME_FLAG, 20), datab, 5, 1);
						writeCharacteristic(datab); // Bug -- null pointer
					}
				};
				synchronizationTrackerTimer = new Timer();
				synchronizationTrackerTimer.schedule(synchronizationTrackerTimertask, 100, 1000);
			}
		}
	}		
	
	public byte[] getSyncTracker(){
		byte[] datab = SYNCHRONIZATION_TRACKER.clone();
		int track = share.getBoolean(Constant.TRACKER_FLAG, false) == true ? 1 : 0;
		ByteDataConvert.IntToBin(track, datab, 3, 1);
		ByteDataConvert.IntToBin(100, datab, 4, 1);
		return datab;
	}
	
	/**
	 * ����Ŀ��
	 * 
	 * @param enabled
	 */
	public void synchronizationTarget(boolean enabled){
		if(isApp==null){
			return;
		}
		if(!enabled){
			if(synchronizationTargetTimer!=null){
				synchronizationTargetTimer.cancel();
				synchronizationTargetTimer = null;
			}
		}else{
			if(synchronizationTargetTimer!=null){
				return;
			}
			synchronizationTargetTimertask = new TimerTask() {  
			    public void run() {
					byte[] datab=SYNCHRONIZATION_TARGET.clone();
					int target = share.getInt(Constant.NUM_KEY, 10000);
					ByteDataConvert.IntToBinByLow(target, datab, 3, 4);
					
					datab[7] = (byte) 0xff;
					datab[8] = (byte) 0xff;
					datab[9] = (byte) 0xff;
					datab[10] = (byte) 0xff;
					
					writeCharacteristic(datab);
			    }  
			}; 
			synchronizationTargetTimer = new Timer(); 
			synchronizationTargetTimer.schedule(synchronizationTargetTimertask,100,3000);
		}
	}
	
	/**
	 * ����豸��ص���������汾�š���ǰ�������˶�ʱ�����ݡ�˯�߽���ʱ���˯�߳���ʱ��(0x0A)
	 * 
	 * @param enabled
	 */
	public void synchronizationDeviceInfo(boolean enabled){
		Log.d("TAG", "synchronizationDeviceInfo .............................................");
		if(isApp==null){
			return;
		}
		if(!enabled){
			if(synchronizationDeviceTimer!=null){
				synchronizationDeviceTimer.cancel();
				synchronizationDeviceTimer = null;
			}
		}else{
			
			if(synchronizationDeviceTimer!=null){
				return;
			}
			
			synchronizationDeviceTimertask = new TimerTask() {  
			    public void run() {
					byte[] datab=SYNCHRONIZATION_DEVICE_INFO.clone();
					writeCharacteristic(datab);			
			    }  
			}; 
			synchronizationDeviceTimer = new Timer(); 
			synchronizationDeviceTimer.schedule(synchronizationDeviceTimertask,1000,3000);
		}
	}
	
	
	public byte[] getSyncDeviceInfo(){
		return SYNCHRONIZATION_DEVICE_INFO.clone();
	}
	
	
	/**
	 * ���õ绰�����¼� 
	 * 
	 * @param enabled
	 */
	public void synchronizationPhoneRemindEvent(boolean enabled){
		if(isApp==null){
			return;
		}
		if(share.getBoolean(Constant.PHONE_FALG, true) && isApp.getConnectBleDevice()){
			if(!enabled){
				
				
				if(phoneTimer!=null){
					phoneTimer.cancel();
					phoneTimer = null;
				}
			}else{
				if(phoneTimer!=null){
					return;
				}
				phonetask = new TimerTask() {  
				    public void run() {
						byte[] datab=SYNCHRONIZATION_PHONE_REMIND.clone();
						writeCharacteristic(datab);
				    }  
				}; 
				phoneTimer = new Timer(); 
				phoneTimer.schedule(phonetask,100,20000);
			}
		}
	}

	public void synchronizationPhoneRemindEvent(){
		byte[] datab=SYNCHRONIZATION_PHONE_REMIND.clone();
		writeCharacteristic(datab);
	}
	
	/**
	 * ���ö��������¼� 
	 * 
	 * @param enabled
	 */
	public void synchronizationSMSRemindEvent(boolean enabled){
		if(isApp==null){
			return;
		}
		if(!enabled){
			if(smsTimer!=null){
				smsTimer.cancel();
				smsTimer = null;
			}
		}else{
			if(smsTimer!=null){
				return;
			}
			smstask = new TimerTask() {  
			    public void run() {
					byte[] datab=SYNCHRONIZATION_SMS_REMIND.clone();
					writeCharacteristic(datab);
//					Log.d("TAG", "finish sent sms message ................ ");
			    }  
			}; 
			smsTimer = new Timer(); 
			smsTimer.schedule(smstask,1000,2000);
		}
	}
	
	public byte[] getSyncSMSRemindEvent(){
		return SYNCHRONIZATION_SMS_REMIND.clone();
	}
	
	/**
	 * �����ʼ������¼� 
	 * 
	 * @param enabled
	 */
	public void synchronizatioEmailRemindEvent(boolean enabled){
		Log.d("TAG", "synchronizatioEmailRemindEvent ......................... ");
		if(isApp==null){
			return;
		}
		if(!enabled){
			if(emailTimer!=null){
				emailTimer.cancel();
				emailTimer = null;
			}
		}else{
			
			if(emailTimer!=null){
				return;
			}
			
			emailtask = new TimerTask() {  
			    public void run() {
					byte[] datab=SYNCHRONIZATION_EMAIL_REMIND.clone();
					writeCharacteristic(datab);
			    }  
			}; 
			emailTimer = new Timer(); 
			emailTimer.schedule(emailtask,1000,2000);
		}
	}
	
	public byte[] getSyncEmailRemindEvent(){
		return SYNCHRONIZATION_EMAIL_REMIND.clone();
	}
	
	public void destroy(){
		if(connverifytimer!=null)connverifytimer.cancel();
		if(synchronizationTimeTimer!=null)synchronizationTimeTimer.cancel();
		if(synchronizationDataTimer!=null)synchronizationDataTimer.cancel();
		if(gatt!=null){
			gatt.close();
		}
	}
	
	/***
	 * App �� ble �豸�� ������ʱ������ ��ʱ�� ---- (ÿ�����ӵ�ʱ��ͬ��ʱ�䵽 ble device)
	 * 
	 * enabled  true ��������    false ֹͣ
	 */
	public void synchronizationTime(boolean enabled){
		Log.d("TAG", "PlusDotBLEDevice 's synchronizationTime...............................");
		if(isApp==null){
			return;
		}
		//�·�ʱ��ͬ��     ����һ���߳� ���յ��ظ���ֹͣ�����������
		if(!enabled){
			if(synchronizationTimeTimer!=null){
				synchronizationTimeTimer.cancel();
				synchronizationTimeTimer = null;
			}
		}else{
			
			if(synchronizationTimeTimer!=null){
				return;
			}
			
			synchronizationTimeTimer=new Timer();
			synchronizationTimeTimertask = new TimerTask() {  
			    public void run() {
			    	
			    	byte[] datatime=SYNCHRONIZATION_TIME.clone();//{(byte) 0xA6,0x27,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00}};
			    	Time t=new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�  
			    	t.setToNow(); // ȡ��ϵͳʱ�䡣  
			    	int year = t.year;  
			    	int month = t.month+1;  
			    	int date = t.monthDay;
			    	int hour = t.hour; // 0-23  
			    	int minute = t.minute;  
			    	int second = t.second; 
			    	int weekDay =t.weekDay;
			    	
			    	Log.d("TAG", "year ===== "+year+"  month ==== "+month+" date ==== "+date +" hour ==== "+hour+" minute ==== "+minute+" second ==== "+second);
			    	
			    	ByteDataConvert.IntToBinByLow(year, datatime, 3, 2);
			    	ByteDataConvert.IntToBin(month, datatime, 5, 1);
			    	ByteDataConvert.IntToBin(date, datatime, 6, 1);
			    	ByteDataConvert.IntToBin(hour, datatime, 7, 1);
			    	ByteDataConvert.IntToBin(minute, datatime, 8, 1);
			    	ByteDataConvert.IntToBin(second, datatime, 9, 1);
			    	ByteDataConvert.IntToBin(weekDay, datatime, 10, 1);
			    	
					writeCharacteristic(datatime);
			    }  
			}; 
			synchronizationTimeTimer.schedule(synchronizationTimeTimertask,0, 5000l);
		}
	}
	
	public byte[]  getSyncTime(){
		Log.d("TAG", "PlusDotBLEDevice 's synchronizationTime...............................");
		//�·�ʱ��ͬ��     ����һ���߳� ���յ��ظ���ֹͣ�����������
    	byte[] datatime=SYNCHRONIZATION_TIME.clone();//{(byte) 0xA6,0x27,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00}};
    	Time t=new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�  
    	t.setToNow(); // ȡ��ϵͳʱ�䡣  
    	int year = t.year;  
    	int month = t.month+1;  
    	int date = t.monthDay;
    	int hour = t.hour; // 0-23  
    	int minute = t.minute;  
    	int second = t.second; 
    	int weekDay =t.weekDay;
    	
    	Log.d("TAG", "year ===== "+year+"  month ==== "+month+" date ==== "+date +" hour ==== "+hour+" minute ==== "+minute+" second ==== "+second);
    	
    	ByteDataConvert.IntToBinByLow(year, datatime, 3, 2);
    	ByteDataConvert.IntToBin(month, datatime, 5, 1);
    	ByteDataConvert.IntToBin(date, datatime, 6, 1);
    	ByteDataConvert.IntToBin(hour, datatime, 7, 1);
    	ByteDataConvert.IntToBin(minute, datatime, 8, 1);
    	ByteDataConvert.IntToBin(second, datatime, 9, 1);
    	ByteDataConvert.IntToBin(weekDay, datatime, 10, 1);

    	return datatime;
	}

	
	/***
	 * ���� ������Ϣͬ������
	 * 
	 * enabled  true ��������    false ֹͣ
	 */
	public void synchronizationInfoTask(boolean enabled){
		//�·�������Ϣͬ��     ����һ���߳� ���յ��ظ���ֹͣ�����������
//		Log.d("TAG", "PlusDotBLEDevice 's synchronizationInfoTask...............................");
		
		if(!enabled){
			if(synchronizationInfoTimer!=null){
				synchronizationInfoTimer.cancel();
				synchronizationInfoTimer = null;
			}
		}else{
			
			if(synchronizationInfoTimer!=null){
				return;
			}
			
			synchronizationInfoTimertask = new TimerTask() {  
			    public void run() {
			    	byte[] useData=SYNCHRONIZATION_INFO.clone();//{(byte) 0xA6,0x00,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
			    	//��������Ϣ
			    	String weight = share.getString(Constant.USER_WEIGHT, "60");
			    	String height = share.getString(Constant.USER_HEIGHT, "175");
			    	String gender = share.getString(Constant.GENDER, "true");
					String birthday = share.getString(Constant.USER_BIRTHDAY, "1990-6-24");
					
//					Log.d("TAG", "weight ======== "+weight+"  "+"height ========= "+height+" "+" gender ========= "+gender+" birthday ========="+birthday);
					
					String str[] = birthday.split("-"); //Bug --birthday = 0,�����Ĭ��ֵ�������ڿ�ʼ�������û�����
//					Log.d("TAG", "str[0] ======== "+str[0]+"  "+"str[1] ========= "+str[1]+" "+" str[2] ========= "+str[2]);
					
					ByteDataConvert.IntToBinByLow(Integer.parseInt(weight), useData, 3, 2);
					ByteDataConvert.IntToBin(Integer.parseInt(height), useData, 5, 1);
					ByteDataConvert.IntToBin(gender =="true" ? 1:0 , useData, 6, 1);
					ByteDataConvert.IntToBinByLow(Integer.parseInt(str[0]), useData, 7, 2);
					ByteDataConvert.IntToBin(Integer.parseInt(str[1]), useData, 9, 1);
					ByteDataConvert.IntToBin(Integer.parseInt(str[2]), useData, 10, 1);
					ByteDataConvert.IntToBin(0, useData, 11, 1);
					ByteDataConvert.IntToBin(0, useData, 12, 1);
					
					writeCharacteristic(useData);
			    }  
			}; 
			synchronizationInfoTimer=new Timer();
			synchronizationInfoTimer.schedule(synchronizationInfoTimertask,0, 3000);
		}
	}
	
	/***
	 * ����ͬ������
	 * enabled  true ��������    false ֹͣ
	 */
	public void synchronizationRequestTask(boolean enabled){
		//�·�ʱ��ͬ��     ����һ���߳� ���յ��ظ���ֹͣ�����������
		if(!enabled){
			if(synchronizationDataTimer!=null){
				synchronizationDataTimer.cancel();
				synchronizationDataTimer = null;
			}
		}else{
			
			if(synchronizationDataTimer!=null){
				return;
			}
			
			synchronizationDataTimertask = new TimerTask() {  
			    public void run() {
			    	byte[] datatime=SYNCHRONIZATION_REQUEST.clone();//{(byte) 0xA6,0x00,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
					long ltime=ConvertUTC.ConvertUTCSecs(new UtcTime());
					ByteDataConvert.LongToBin(ltime, datatime, 3, 4);
					//				byte[] datab={(byte) 0x49};
					System.out.println("---------synchronizationData-----��������һ������ͬ������----"+datatime.length);
					writeCharacteristic(datatime);
			    }  
			}; 
			synchronizationDataTimer=new Timer();
			synchronizationDataTimer.schedule(synchronizationDataTimertask,0, 300000l);
		}
	}

	
	public byte[]  getSyncRequestTask(){
		byte[] datatime=SYNCHRONIZATION_REQUEST.clone();//{(byte) 0xA6,0x00,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
		long ltime=ConvertUTC.ConvertUTCSecs(new UtcTime());
		ByteDataConvert.LongToBin(ltime, datatime, 3, 4);
		
		return datatime;
	}
	
	/***
	 * ͬ������Ӧ��
	 */
	public void synchronizationDataAnswer(long dataid){
		byte[] datatime=SYNCHRONIZATION_DATA_ANSWER.clone();
		ByteDataConvert.LongToBin(dataid, datatime, 3, 4);
		writeCharacteristic(datatime);
	}
	
	public void closeAllTask(){
	
		synchronizationTime(false); 
		synchronizationDeviceInfo(false);		
		
		synchronizationSportBeginData(false);
		synchronizationSportEndData(false);
		
		synchronizationSleepBeginData(false);
		synchronizationSleepEndData(false);
		
		synchronizationPhoneRemindEvent(false); 
		synchronizationSMSRemindEvent(false); 
		synchronizatioEmailRemindEvent(false);
		
		synchronizationSportClockData(false);
		synchronizationClockData(false);
		synchronizationTracker(false);
		
		BindCommand(false);
		
		OTACommand(false);
	}
	
	/***
	 * ʱʵ����Ӧ��
	 */
	public void liveDataAnswer(long dataid){
		byte[] datatime=LIVE_DATA_ANSWER.clone();
		ByteDataConvert.LongToBin(dataid, datatime, 3, 4);
		writeCharacteristic(datatime);
	}
	
	/***
	 * �·�
	 * @param value
	 */
	public void writeCharacteristic(byte [] value){
		
		try{
			byte [] data=Crc16Util.addCrc(value, 2);
			final StringBuilder stringBuilder = new StringBuilder(data.length);
			for (byte byteChar : data){
				Log.i(TAG, ""+byteChar);
				stringBuilder.append(String.format("%02X", byteChar));
			}
	/*		try {
				fileutil.writeFile("�·�����:" + stringBuilder.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			Log.i(TAG, "�·�����  sb.toString: " + stringBuilder.toString());
			writeGattCharacteristic.setValue(data); //bug -- nullPointException;
			gatt.writeCharacteristic(writeGattCharacteristic);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * ���û����һ��������֪ͨ��
	 * 
	 * @�������� �ص��ȡ�ж���
	 * @�������� ���Ϊtrue������֪ͨ������Ϊfalse��
	 */
	public void setCharacteristicNotification(boolean enable){
		if (gatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		gatt.setCharacteristicNotification(read_notificationGattCharacteristic, enable);
		BluetoothGattDescriptor descriptor = read_notificationGattCharacteristic.getDescriptor(UUID
				.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
		if (descriptor != null) {
			System.out.println("write descriptor");
			descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			gatt.writeDescriptor(descriptor);
		}
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the deviceType
	 */
	public String getDeviceType() {
		return deviceType;
	}
	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	/**
	 * @return the partners
	 */
	public String getPartners() {
		return partners;
	}
	/**
	 * @param partners the partners to set
	 */
	public void setPartners(String partners) {
		this.partners = partners;
	}
	/**
	 * @return the gatt
	 */
	public BluetoothGatt getGatt() {
		return gatt;
	}
	/**
	 * @param gatt the gatt to set
	 */
	public void setGatt(BluetoothGatt gatt) {
		this.gatt = gatt;
	}
	/**
	 * @return the writeGattCharacteristic
	 */
	public BluetoothGattCharacteristic getWriteGattCharacteristic() {
		return writeGattCharacteristic;
	}
	/**
	 * @param writeGattCharacteristic the writeGattCharacteristic to set
	 */
	public void setWriteGattCharacteristic(
			BluetoothGattCharacteristic writeGattCharacteristic) {
		this.writeGattCharacteristic = writeGattCharacteristic;
	}
	/**
	 * @return the read_notificationGattCharacteristic
	 */
	public BluetoothGattCharacteristic getRead_notificationGattCharacteristic() {
		return read_notificationGattCharacteristic;
	}
	/**
	 * @param read_notificationGattCharacteristic the read_notificationGattCharacteristic to set
	 */
	public void setRead_notificationGattCharacteristic(
			BluetoothGattCharacteristic read_notificationGattCharacteristic) {
		this.read_notificationGattCharacteristic = read_notificationGattCharacteristic;
	}
	
	//////////////////////////////////////////////////////////
	
	
	/** ����ʱ��cmd (0x01) **/
	public static int SYNC_TIME = 0;

	/** ����豸��ص���������汾�š���ǰ�������˶�ʱ�����ݡ�˯�߽���ʱ���˯�߳���ʱ��(0x0A) **/ //BUG -- û����
	public static int SYNC_INFO= 1;
	
	
	/** �������� (0x03) **/
//	public static byte[] SYNCHRONIZATION_CLOCK={(byte)			0xA6,0x27,0x03,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//	
//	/** ����Ŀ�� (0x04) **/ ////bug -- �������� 0x03
//	public static byte[] SYNCHRONIZATION_TARGET={(byte)			0xA6,0x27,0x04,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//	
//	/** ����������(0x05) mode:���ѷ�ʽ,0x00(������), 0x01(������), 0x02(��������), 0x03 (����������) **/
//	public static byte[] SYNCHRONIZATION_REMIND={(byte)			0xA6,0x27,0x05,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	
	
//	/** ��ȡ�豸��ʱ��cmd (0x0B) **/ //BUG -- û����
//	public static byte[] SYNCHRONIZATION_DEVICE_TIME={(byte)			0xA6,0x27,0x0B,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//	
//	/** �����˶����� (0x08) **/
//	public static byte[] SYNCHRONIZATION_SPORT_REMIND_CLOCK={(byte)			0xA6,0x27,0x08,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//
//	/** ���� **/
//	public static byte[] SYNCHRONIZATION_TRACKER = {(byte)			0xA6,0x27,0x09,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//	
//	/** ����ģʽ **/
//	public static byte[] SYNCHRONIZATION_REMINDER_MODE = {(byte)			0xA6,0x27,(byte) 0xf1,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};

	
	
	public void sendCmd(int cmd){
		if(cmd == SYNC_TIME){
//			bleMessageQueueManagement.addUDPPacket(cmd, getSyncTime());
//			writeCharacteristic(getSyncTime());
			BLEPacketMap.put(cmd, getSyncTime());
		}else if(cmd == SYNC_INFO){
		}
		
//		new SyncDataThread().start();//�½�������CustomThreadʵ��  
//		isRun = true;
	}
	
	private Handler mHandler;  
	private boolean isRun = false;
	
	/*
	 * ble ���ݰ�����
	 */
	private ConcurrentMap<Integer, byte[]> BLEPacketMap = new ConcurrentHashMap<Integer, byte[]>();
	
	
	private class SyncDataThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
//		    //������Ϣѭ���Ĳ���  
//            Looper.prepare();//1����ʼ��Looper  
//            mHandler = new Handler(){//2����handler��SyncDataThreadʵ����Looper����  
//                public void handleMessage (Message msg) {//3�����崦����Ϣ�ķ���  
//                    switch(msg.what) {  
//                        
//                    }  
//                }  
//            };  
//            Looper.loop();//4��������Ϣѭ��
			
			
			while(isRun){
				isRun = false;
			}
		}
	}
	
	
//	private ArrayList<Integer,byte[]> cmdQueueManager = new ArrayList<Integer, byte[]>();
	
	
	private  Timer datatimer ;  //���Ӷ�ʱ��
	private  TimerTask 	datatask;//����������֤����
	
	public void synchronizationData(boolean enabled,final byte[] data){
		if(!enabled){
			if(datatimer!=null)datatimer.cancel();
		}else{
			datatask = new TimerTask() {  
			    public void run() {
					writeCharacteristic(data);
			    }  
			}; 
			datatimer = new Timer(); 
			datatimer.schedule(datatask,1000,1000);
		}
	}
	
	public int getCurrentDay(){  
		
		int result = 0;
		
        final Calendar c = Calendar.getInstance();  
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));  
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));  
        if("1".equals(mWay)){  
//            mWay ="��";  
            result = 7;
        }else if("2".equals(mWay)){  
//            mWay ="һ";
            result = 1;
        }else if("3".equals(mWay)){  
//            mWay ="��";  
            result = 2;
        }else if("4".equals(mWay)){  
//            mWay ="��";
            result = 3;
        }else if("5".equals(mWay)){  
//            mWay ="��";
            result = 4;
        }else if("6".equals(mWay)){  
//            mWay ="��";
            result = 5;
        }else if("7".equals(mWay)){  
//            mWay ="��";
            result = 6;
        }  
        return result;  
    }  
	
	
}
