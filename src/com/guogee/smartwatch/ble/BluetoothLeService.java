/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.guogee.smartwatch.ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.dao.BindDeviceDao;
import com.guogee.smartwatch.ui.BindingScanActivity;
import com.guogee.smartwatch.ui.VersionUpdateActivity;
import com.guogee.smartwatch.utils.ByteDataConvert;
import com.guogee.smartwatch.utils.Constant;


/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
	
    private final static String TAG = BluetoothLeService.class.getSimpleName();
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;
    private boolean mScanning;
    
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    
    private iSmartApplication isApp;
    private PlusDotBLEDevice plusdotBLEDevice;

    public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";

    public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
    
	public final static int FLAG_MSG_BLUETOOTH_OFF=0;
	public final static int FLAG_MSG_BLUETOOTH_NOBLE=1;
	public final static int FLAG_MSG_BLUETOOTH_NOBLUETOOTH=2;
	public final static int FLAG_MSG_SCAN=3;
	
//	public BluetoothDevice mDevice;
	private boolean joinresults=false;
	public static Map<String, PlusDotBLEDevice> youdyBLEDevices = new HashMap<String, PlusDotBLEDevice>();
	
    private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Toast toast = null;
			switch (msg.what) {
			case FLAG_MSG_BLUETOOTH_OFF:
				 toast=Toast.makeText(BluetoothLeService.this, "您未开启蓝牙！", Toast.LENGTH_SHORT);
				break;
			case FLAG_MSG_BLUETOOTH_NOBLE:
				toast=Toast.makeText(BluetoothLeService.this, "您 的设备不支持低功耗蓝牙！", Toast.LENGTH_SHORT);
				break;
			case FLAG_MSG_BLUETOOTH_NOBLUETOOTH:
				toast=Toast.makeText(BluetoothLeService.this, "您 的设备不支持蓝牙！", Toast.LENGTH_SHORT);
				break;
			case FLAG_MSG_SCAN:
				toast=Toast.makeText(BluetoothLeService.this, "正在连接设备……", Toast.LENGTH_SHORT);
				break;
			default:
				break;
			}
			toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,toast.getYOffset() / 2);
			toast.show();
		}
	};
	
	private Handler mainHandle ;
	
	public void setMainHandle(Handler handle){
		mainHandle = handle;
	}
	
	private SharedPreferences share = null;
	private SharedPreferences.Editor edit = null;
	private BindDeviceDao dao;
	
    @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		share = PreferenceManager.getDefaultSharedPreferences(this);
		edit = share.edit(); // 编辑文件

        isApp = (iSmartApplication)getApplication();
        
		//遍历数据库
        dao = new BindDeviceDao(this);
				
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		
//		connect(MainActivity.deviceMacAddress);
    }

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}
	
    //////////////////////////////////////////////////////////////////////////
	
	// Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction = null;
            
            //connected to ble device..
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                Log.i("TAG", "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i("TAG", "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
                isApp.setConnectBleDevice(true);
    			
                Message msg = mainHandle.obtainMessage(MainActivity.MSG_BLE_CONNECTED); 
                mainHandle.sendMessage(msg); 
                
                //连接成功后，获取设备的单例
                plusdotBLEDevice = PlusDotBLEDevice.getInstance();
                plusdotBLEDevice.setSharedPreferencesInfo(share, isApp);
//    			plusdotBLEDevice.setAddress(mDevice.getAddress());
//    			plusdotBLEDevice.setDevice_name(mDevice.getName());
    			
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i("TAG", "Disconnected from GATT server...........");
             
                isApp.setConnectBleDevice(false);
                plusdotBLEDevice.closeAllTask();
                
                //close ble gatt
                close();
                
                Message msg = mainHandle.obtainMessage(MainActivity.MSG_BLE_DISCONNECTED); 
                mainHandle.sendMessage(msg);
            
                isApp.getUICallback().callbackFail(MainActivity.MSG_BLE_DISCONNECTED, null);
            }
            broadcastUpdate(intentAction);
        }

        /***
		 * 回调函数调用时的远程服务，特点和描述符的远程设备列表已更新，即新的服务被发现。
		 */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
//            } else {
//                Log.w("TAG", "onServicesDiscovered received: " + status);
//            }
//        	Log.d("TAG", gatt.getDevice().getAddress() + "---------" + status + "--------" + gatt.getServices().size());
        	
        	BluetoothGattService bluetoothGattService = gatt.getService(UUID.fromString(PlusDotBLEDevice.PLUSDOT_SERVICE));
        	
        	if(bluetoothGattService!=null){
        		final BluetoothGattCharacteristic read_notification = bluetoothGattService
						.getCharacteristic(UUID
								.fromString(plusdotBLEDevice.UUID_READ_NOTIFICATION));//通道7
        		plusdotBLEDevice
						.setRead_notificationGattCharacteristic(read_notification);
				
        		
        		
				BluetoothGattCharacteristic writeGattCharacteristic = bluetoothGattService
						.getCharacteristic(UUID.fromString(plusdotBLEDevice.UUID_WRITE));//通道6

				//writeGattCharacteristic 有可能为NULL，蓝牙底层问题 ？
				if(writeGattCharacteristic!=null){
					plusdotBLEDevice.setWriteGattCharacteristic(writeGattCharacteristic);
					plusdotBLEDevice.setGatt(gatt);
					plusdotBLEDevice.setCharacteristicNotification(true);//使能通知
					
					isApp.getUICallback().callbackCall(MainActivity.MSG_BLE_CONNECTED, null);

					//遍历数据库，是否已经绑定了手表
					List<Map<String, String>> daoList = dao.listBleDevice(null);
					if(isApp.getConnectBleDevice() && daoList.size()>0 && !isApp.getOTAMode()){  //bug
						if(share.getInt(Constant.FIRMWARE_VERSION, 0)!=isApp.getFirmVersion()){
							share.edit().putInt(Constant.FIRMWARE_VERSION, isApp.getFirmVersion()).commit();
//							plusdotBLEDevice.OTACommand(true);
							plusdotBLEDevice.BindCommand(true);
						}
						plusdotBLEDevice.synchronizationTime(true); // 0x01,sync time
					}
				}else{
					gatt.close();
					disconnect();
				}
				//writeGattCharacteristic 有可能为NULL，蓝牙底层问题 ？
        	} else {
				gatt.close();
			}
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			// TODO Auto-generated method stub
			super.onReadRemoteRssi(gatt, rssi, status);
		}
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
    
	/**
	 * 初始化一个引用本地蓝牙适配器。
	 * 
	 * @ return返回true，如果初始化成功。
	 */
	public boolean initBle() {
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				Log.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}
        // 为了确保设备上蓝牙能使用, 如果当前蓝牙设备没启用,弹出对话框向用户要求授予权限来启用
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
            	mHandler.sendEmptyMessage(FLAG_MSG_BLUETOOTH_OFF);
            	return false;
            }
        }
		  // 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
        	mHandler.sendEmptyMessage(FLAG_MSG_BLUETOOTH_NOBLE);
        	return false;
        }
        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // 检查设备上是否支持蓝牙
        if (mBluetoothAdapter == null) {
        	mHandler.sendEmptyMessage(FLAG_MSG_BLUETOOTH_NOBLUETOOTH);
        	return false;
        }
		return true;
	}
	
    /***
	 * 接收到的数据处理
	 * 
	 * @param action
	 * @param characteristic
	 */
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        this.plusdotBLEDevice.lasedataTime=new Date().getTime();
		// 对于所有其它配置文件，写入十六进制格式的数据。
		final byte[] data = characteristic.getValue();
			
		Log.d("TAG","BluetoothLeService's broadcastUpdate ===================== "+data[2]);
		
		/** 收到 “设置时间cmd (0x01)” **/
		if(data[2]==0x01){
			plusdotBLEDevice.synchronizationTime(false);
			isApp.getUICallback().callbackCall(MainActivity.MSG_BLE_SYNC_TIME, null);
		}
		/** 收到 “设置个人信息 (0x02)” **/
		else if(data[2]==0x02){
			plusdotBLEDevice.synchronizationInfoTask(false);
		}
		/** 收到 “设置闹钟 (0x03)” **/
		else if(data[2]==0x03){
			plusdotBLEDevice.synchronizationClockData(false);
			isApp.getUICallback().callbackCall(11, null);
		}
		/** 收到 “设置目标 (0x04)” **/
		else if(data[2]==0x04){
			plusdotBLEDevice.synchronizationTarget(false);
		}
		/** 收到 “提醒设置 (0x05)” **/
		else if(data[2]==0x05){
			plusdotBLEDevice.synchronizationReminderMode(null, false);
		}
		/** 收到 “获取设备电池电量，软件版本号，当前步数，运动时长数据，睡眠结束时间和睡眠持续时长 (0x0a)” **/
		else if(data[2]==0x0a){
			plusdotBLEDevice.synchronizationDeviceInfo(false);
            isApp.getUICallback().callbackCall(MainActivity.MSG_BLE_DEVICE_INFO, data);
		}
		/** 收到 “提醒设置 (0x05)” **/
		else if(data[2]==0x0b){
		}
		
		//receive the bind command or unbind command.
		else if(data[2]==0x07){
			if(data[3]==0x00 ){

/**				
				if(BindWatchFragment.watchSize == 0){
	                Message msg = mainHandle.obtainMessage(MainActivity.MSG_DISCONNECT); 
	                mainHandle.sendMessage(msg);					
				}
				else{
					isApp.getUICallback().callbackCall(BindingScanActivity.MSG_BINE_BLE_SUCCESS, null);	
					plusdotBLEDevice.BindCommand(false);					
				
					Log.e("TAG", "Service close sent bind command .............. ");
				}
**/
			
				if(isApp.getBindCommand()){
					isApp.getUICallback().callbackCall(BindingScanActivity.MSG_BINE_BLE_SUCCESS, null);	
					plusdotBLEDevice.BindCommand(false);					
				}else{
					Message msg = mainHandle.obtainMessage(MainActivity.MSG_DISCONNECT); 
	                mainHandle.sendMessage(msg);
				}
			}
		}
		
        else if(data[2]==0x08){
			plusdotBLEDevice.synchronizationSportClockData(false);
			isApp.getUICallback().callbackCall(11, null);
		}
		
        else if(data[2]==0x09){
			plusdotBLEDevice.synchronizationTracker(false);
			isApp.getUICallback().callbackCall(11, null);
		}
		
		// 同步每小时的跟踪数据(准备 0x13-同步过程 0x14-结束 0x15)
		/** 收到 “准备同步 (0x13)” **/
		else if(data[2]==(byte) 0x13 ){
//			int totals = ByteDataConvert.BinToIntByLow(data, 13, 2);
			int days = ByteDataConvert.BinToIntByLow(data, 15, 1);
			isApp.getUICallback().callbackCall(MainActivity.MSG_UPDATE_SPORT_DATA, data);

            mTotals = 5; 
            mDays = days;
            Log.d("TAG", "mTotals ======= "+mTotals+" mDays ======== "+mDays);
            
            plusdotBLEDevice.synchronizationSportBeginData(false);
            plusdotBLEDevice.synchronizationSportData(1);
		}
		/** 收到 “同步过程 (0x14)” **/
		else if(data[2]==0x14){
			int serial = ByteDataConvert.BinToIntByLow(data, 4, 2);
			Log.d("TAG", "serial =============== "+serial);
			isApp.getUICallback().callbackCall(MainActivity.MSG_SUB_PACKAGE, data);

			if(serial < mTotals){
				serial++;
//				plusdotBLEDevice.synchronizationSportData(true,serial);	
				plusdotBLEDevice.synchronizationSportData(serial);	
			}else{
				mTotals = 0;
//				plusdotBLEDevice.synchronizationSportData(false,0);
				plusdotBLEDevice.synchronizationSportEndData(true);				
			}
		}
		/** 收到 “同步结束 (0x15)” **/
		else if(data[2]==0x15){
			plusdotBLEDevice.synchronizationSportEndData(false);
			Log.d("TAG", " mDays ================= "+mDays);
			if(mDays-- > 1){
				plusdotBLEDevice.synchronizationSportBeginData(true);
			}else{
				mDays = 1;
			}
		}
		
		//同步睡眠数据(准备 0x16-同步过程 0x17-结束 0x18)
		/** 收到 “准备同步 (0x16)” **/
		else if(data[2]==0x16){
			plusdotBLEDevice.synchronizationSleepBeginData(false);
			
			int awake_count = ByteDataConvert.BinToInt(data, 13, 1);
			int totals = ByteDataConvert.BinToIntByLow(data, 14, 2);
			
			mTotals = totals;
			
			int year = ByteDataConvert.BinToIntByLow(data, 3, 2);
			int month = ByteDataConvert.BinToIntByLow(data, 5, 1);
			int day = ByteDataConvert.BinToIntByLow(data, 6, 1);
			
			Log.d("TAG", "awake_count === "+awake_count+"   "+"totals === "+totals+"  year ====="+year+"  month ======== "+month+"  day =========" +day);
			
			mYear = year;
			mDays = day;
			
			if(year!=0){
				//call back
	            isApp.getUICallback().callbackCall(MainActivity.MSG_UPDATE_SLEEP_DATA, data);
				plusdotBLEDevice.synchronizationSleepBeginOneData(true);
			}else{
	            //call back
	            isApp.getUICallback().callbackCall(MainActivity.MSG_UPDATE_SLEEP_DATA_ERROR, data); 
				plusdotBLEDevice.synchronizationSleepEndData(true);
			}
		}
		
		else if(data[2] == 0x19){
            isApp.getUICallback().callbackCall(MainActivity.MSG_UPDATE_SLEEP_DATA2, data);
            
			plusdotBLEDevice.synchronizationSleepBeginOneData(false);
			plusdotBLEDevice.synchronizationSleepData(1);
		}
		
		/** 收到 “同步过程 (0x17)” **/
		else if(data[2]==0x17){
			int serial = ByteDataConvert.BinToIntByLow(data, 4, 2);
			Log.d("TAG", "sleep serial =============== "+serial);
			
            if(serial < mTotals){
				serial++;
				plusdotBLEDevice.synchronizationSleepData(serial);	
			}else{
				mTotals = 0;
				plusdotBLEDevice.synchronizationSleepEndData(true);				
			}
		}
		/** 收到 “同步结束 (0x18)” **/
		else if(data[2]==0x18){
			plusdotBLEDevice.synchronizationSleepEndData(false);
			if(mYear!=0){
				plusdotBLEDevice.synchronizationSleepBeginData(true);
			}
		}
		/** 收到提醒时间（电话 0x01，短信 0x02，邮件 0x03）,end **/
		else if(data[2]==0x20){
			if(data[3]==0x01){
				
//				if(!SettingActivity.testMode){
//					plusdotBLEDevice.synchronizationPhoneRemindEvent(false);	
//				}
				
				plusdotBLEDevice.synchronizationPhoneRemindEvent(false);
			}else if(data[3] == 0x02){
				plusdotBLEDevice.synchronizationSMSRemindEvent(false);
			}else if(data[3] == 0x03){
				plusdotBLEDevice.synchronizatioEmailRemindEvent(false);
			}
		}
		
		/** 接受/挂断 电话，关掉 “灯，声音，震动” **/
		else if(data[2] == 0x21){
			if(data[3]==0x01){
				plusdotBLEDevice.synchronizationPhoneRespone(false);	
			}else if(data[3]==0x02){
				plusdotBLEDevice.synchronizationSMSRespone(false);
			}
		}
		
		/** 收到 “其它 (0xf0)” **/
		else if(data[2]==-16){
			Log.d("TAG", "f0 ’s data[3] .................. "+data[3]);
			if(data[3]==1){
				Message msg = mainHandle.obtainMessage(MainActivity.MSG_PLAY_SOUND); 
	            mainHandle.sendMessage(msg);
			}
			else if(data[3] == 0x02){
				Log.d("TAG", "take pic ............");
				isApp.getUICallback().callbackCall(MainActivity.MSG_TAKE_PIC, data);
			}
		}
		
		/** take photograph **/
		else if(data[2] == (byte)0xf2){
			Log.d("TAG", "accept the command ,and enter the photograp mode............");
		}
		
		/** OTA **/
		else if(data[2] == (byte)0x30){
			if(data[3]==0x00){
				Log.d("TAG", "accept the OTA command ............");	
				isApp.getUICallback().callbackCall(VersionUpdateActivity.MSG_ENTER_OTA, data);
				plusdotBLEDevice.OTACommand(false);
				
//				plusdotBLEDevice.synchronizationTime(true);
			}
		}
		
        sendBroadcast(intent);
    }
    
    private int mTotals = 0;
    private int mDays = 0;
    private int mYear;
    
    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        return true;
    }
    
    private boolean isFirstConnect = false;
    
    public boolean connect(String address) {
    	
        if (mBluetoothAdapter == null || address == null) {
            Log.d("TAG", "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d("TAG", "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
//                isFirstConnect = false; 
                return true;
            } else {
                return false;
            }
        }
        
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        
        Log.e("TAG","ble's name ========== "+device.getName()); //null
        if(device.getName()==null){
        	return false;
        }
        
        
        if(device.getName().equals("DfuTarg")){
//        	isApp.getUICallback().callbackFail(MainActivity.MSG_BLE_CONNECTED, null);
        	isApp.getUICallback().callbackCall(MainActivity.MSG_BLE_DISCONNECTED, null);
        	isApp.setOTAMode(true);
        	return false;
        }else{
        	isApp.setOTAMode(false);
        }
        
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d("TAG", "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        
        Log.e("TAG","ble's name ========== "+device.getName());
        
        
//        if(isFirstConnect==false){
//        	isFirstConnect = true;	
//        }
        
        return true;
    }
    
    ////////    connect test ///////////

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;
        return mBluetoothGatt.getServices();
    }
}
