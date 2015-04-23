package com.guogee.smartwatch.ui;

import java.util.Timer;
import java.util.TimerTask;

import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.ble.PlusDotBLEDevice;
import com.guogee.smartwatch.ble.StatusChangedCallback;
import com.guogee.smartwatch.dao.BindDeviceDao;
import com.guogee.smartwatch.utils.TypefaceUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guogee.smartwatch.utils.Constant;

@SuppressLint("NewApi")
public class BindingScanActivity extends Activity {
	
	private BluetoothAdapter mBluetoothAdapter;
	private boolean mScanning;
	private Handler mHandler;
	private static final int REQUEST_ENABLE_BT = 1;
	private static final String TAG = "TAG";
	
//	private LeDeviceListAdapter mLeDeviceListAdapter;
	// 10���ֹͣ��������.
	private static final long SCAN_PERIOD = 10000;
	private LinearLayout layout_scaning;//����ɨ��
	private LinearLayout  layout_data;//������ʾ 
	private LinearLayout layout_list;//�����б�
	private LinearLayout layout_not_found;//û���ҵ�
	private ImageView iv_return_btn ;
	private TextView titletext,tv_hint;
	private Button butt_rescan;
	
	private TypefaceUtil tfutil = new TypefaceUtil(BindingScanActivity.this);
	public SharedPreferences share = null;
	private iSmartApplication isApp;
	private PlusDotBLEDevice plusdotDevice;
	
	private String deviceName ;
	private String deviceMac;
	public int plusdotType = 0;
	
	private Timer       scanTimer;
	private TimerTask 	scanTimertask;//����ͬ������
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.r_clouds_dialog2);
		
		isApp = (iSmartApplication)getApplication();	
		share = PreferenceManager.getDefaultSharedPreferences(this);
		
		Bundle data = getIntent().getExtras();
		if(data!=null){
			plusdotType = data.getInt("plusdot_type");
		}
		
		plusdotDevice = PlusDotBLEDevice.getInstance();
		isApp.addCallBack(new UIStatusChangedCallback());
		
		layout_scaning=(LinearLayout) findViewById(R.id.layout_scaning);
		layout_data=(LinearLayout) findViewById(R.id.layout_data);
		layout_list=(LinearLayout) findViewById(R.id.layout_list);
		layout_not_found=(LinearLayout) findViewById(R.id.layout_not_found);
		
		if(!initBle())return;
		mHandler = new Handler();
	
		initView();
	}
	
	private LinearLayout linItem;
	private ImageView imgState;
	private TextView textManual;
	private ImageView imgYun;
	private ImageView imgArrow;
	private ImageView imgRemote;
	private TextView textRmote;
	private Button btnOk;
	private Animation alphAnimOpen;
	private Animation alphAnimClose;
	
	private void initView(){
		imgState 		= (ImageView)findViewById(R.id.img_match_state);
		textManual 		= (TextView)findViewById(R.id.text_manual);
		imgRemote		= (ImageView)findViewById(R.id.img_remote);
		textRmote 		= (TextView)findViewById(R.id.text_remote);
		btnOk 			= (Button)findViewById(R.id.btn_ok);
		
		textRmote.setText(R.string.close_to_watch);
		btnOk.setText(getResources().getString(android.R.string.cancel));
		
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    if(!isBind){
	                
//			    	if(btnOk.getText().equals(getResources().getString(R.string.rescale))){
//			    		btnOk.setText(R.string.cancle);
//			    		scanLeDevice(true);
//			    	}else 
			    	
			    	Log.d("TAG","btnOK 's text ========= "+btnOk.getText());
			    	
			    	if(btnOk.getText().equals(getResources().getString(R.string.cancle))){
		                
			    		scanLeDevice(false);
					    finish();
			    	}
			    	
			    }else{
			    	setResult(1);
			    	
				    scanLeDevice(false);
				    finish();
			    }
			    
//			    finish();
//			    scanLeDevice(false);
			}
		});	
	}
	
	private boolean initBle(){
		// ��鵱ǰ�ֻ��Ƿ�֧��ble ����,�����֧���˳�����
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast toast = Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,toast.getYOffset() / 2);
			toast.show();
			finish();
			return false;
		}
		// ��ʼ�� Bluetooth adapter, ͨ�������������õ�һ���ο�����������(API����������android4.3�����ϺͰ汾)
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

		// ����豸���Ƿ�֧������
		if (mBluetoothAdapter == null) {
			Toast toast = Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,toast.getYOffset() / 2);
			toast.show();
			finish();
			return false;
		}
		return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// �û�ѡ������������
		if (requestCode == REQUEST_ENABLE_BT
				&& resultCode == Activity.RESULT_CANCELED) {
			finish();
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onPause();
		isApp.addCallBack(new UIStatusChangedCallback());
		
		// Ϊ��ȷ���豸��������ʹ��, �����ǰ�����豸û����,�����Ի������û�Ҫ������Ȩ��������
		if (!mBluetoothAdapter.isEnabled()) {
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
		scanLeDevice(true);
	}

	
	// ɨ��
//	private void scanLeDevice(final boolean enable) {
//		if (enable) {
//			// ����һ��Ԥ�����ɨ���ڼ�ֹͣɨ�衣
//			mHandler.postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					mScanning = false;
//					mBluetoothAdapter.stopLeScan(mLeScanCallback);
//					
////					textRmote.setText("��ʱ,����������!");
//				}
//			}, SCAN_PERIOD);
//			mScanning = true;
//			mBluetoothAdapter.startLeScan(mLeScanCallback);
//		} else {
//			mScanning = false;
//			mBluetoothAdapter.stopLeScan(mLeScanCallback);
//		}
//	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	
		scanLeDevice(false);
	}

	private void scanLeDevice(final boolean enable){
		if (!enable) {
			if(scanTimer!=null){
				scanTimer.cancel();
				scanTimer = null;
			}
//			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		} else {
			
			if(scanTimer!=null){
				return;
			}
			scanTimertask = new TimerTask() {  
			    public void run() {
//			    	mScanning = true;
			    	mBluetoothAdapter.stopLeScan(mLeScanCallback);
			    	mBluetoothAdapter.startLeScan(mLeScanCallback);
			    }  
			}; 
			scanTimer = new Timer(); 
			scanTimer.schedule(scanTimertask,100,3000);
		}
	}

	// �豸ɨ��ص���
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {
			
			Log.i(TAG, device.getAddress()+"  rssi ======="+rssi);
			
			StringBuilder stringBuilder = new StringBuilder(scanRecord.length);
			for (byte byteChar : scanRecord)
				stringBuilder.append(String.format("%02X ", byteChar));
			Log.i(TAG, device.getName() + "-length:" + scanRecord.length
					+ "-String: " + stringBuilder.toString());
			
			if((device.getName().contains("plusdot") || device.getName().contains("plusdout")) && rssi > -60){
			runOnUiThread(new Runnable() {
				public void run() {
//					Log.i(TAG, device.getName());
//					butt_rescan.setVisibility(View.GONE);
					
					textRmote.setText(R.string.binding_watch);
					mCurrentDevice = device;
					isApp.setScanBleDevice(true);
					
//					Bundle bundle = new Bundle();
//					bundle.putString("DeviceName", device.getName());
//					bundle.putString("DeviceMac", device.getAddress());

					deviceName = device.getName();
					deviceMac  = device.getAddress();
					
//					Message msg = isApp.getMainHandler().obtainMessage(MainActivity.MSG_BIND_BLE); 
//					msg.setData(bundle);
//					isApp.getMainHandler().sendMessage(msg);
				
					isApp.getUICallback().callbackCall(MSG_SCAN_BLE, null);
				}
			});
			}
		}
	};
	
	private BluetoothDevice mCurrentDevice;

	// ������ͨ���ٰ�ɨ�跢�ֵ��豸��

	    ///////////////////////////////////////////////////////////////////
		
	    public final static int MSG_BINE_BLE_SUCCESS = 2;
		
		public final static int MSG_BIND_BLE_FAILURE = 3;
		
		public final static int MSG_BINE_UNBLE_SUCCESS = 4;
		
		public final static int MSG_UPDATE_LISTVIEW = 5;
		
		public final static int MSG_SCAN_BLE = 6;
		
		public boolean isBind = false;
		
		public Handler uihandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MainActivity.MSG_BLE_CONNECTED:  
					Log.d("TAG", "BindingScanActivity 's sent bind command .............. ");
					
					plusdotDevice.BindCommand(true);
					
					//�����̣߳�10���ڣ�ûȷ������أ�Ӧ�Ͽ���������
//					new Handler().postDelayed(new Runnable() {
//						@Override
//						public void run() {
//							if(!isBind){
////								bindIcon.setVisibility(View.GONE);
//				                Message msg = isApp.getMainHandler().obtainMessage(MainActivity.MSG_DISCONNECT); 
//				                isApp.getMainHandler().sendMessage(msg);
////				                mLeDeviceListAdapter.notifyDataSetChanged();
//								
//								btnOk.setText(getResources().getString(R.string.rescale));
//				                textRmote.setText(getResources().getString(R.string.timeover));
//							}
//						}
//					}, 10000);
					break;
				case MainActivity.MSG_BLE_DISCONNECTED:
					break;
				case MSG_BINE_BLE_SUCCESS:
					Log.d("TAG", "BindingScanActivity 's  bind success .............. ");
					isBind = true;
					
					plusdotDevice.BindCommand(false);
					scanLeDevice(false);
					
					//��ʼ��ģʽ
					plusdotDevice.synchronizationInitReminderMode();
					/** ����֮����ܻ���ָ�λ����λ�ᵼ�����Ƶ绰������ ͼ�겻�����󶨵�ʱ������������������ **/
//					plusdotDevice.synchronizationReset();
					
					BindDeviceDao dao = new BindDeviceDao(BindingScanActivity.this);
					Object[] params = new Object[] { deviceName, deviceMac, 0, 0 };
					dao.addBleDevice(params);
					
					if(plusdotType==1){
						share.edit().putInt(Constant.PLUSDOT_TYPE, 1).commit();
					}else if(plusdotType==2){
						share.edit().putInt(Constant.PLUSDOT_TYPE, 2).commit();
					}
					
					btnOk.setText(getResources().getString(android.R.string.ok));
					textRmote.setText(getResources().getString(R.string.bind_successfully));
					
//					bindIcon.setVisibility(View.VISIBLE);
//					watchSize = 1;
					break;
				case MSG_BIND_BLE_FAILURE:
					break;
				case MSG_UPDATE_LISTVIEW:
//					mLeDeviceListAdapter.notifyDataSetChanged();
					break;
					
				case MSG_SCAN_BLE:
//					scanLeDevice(false);
					
					Bundle bundle = new Bundle();
					bundle.putString("DeviceName", deviceName);
					bundle.putString("DeviceMac", deviceMac);
					
					Message msg1 = isApp.getMainHandler().obtainMessage(MainActivity.MSG_BIND_BLE); 
					msg1.setData(bundle);
					isApp.getMainHandler().sendMessage(msg1);
					break;
				}
			}
	    };
	    
		class UIStatusChangedCallback implements StatusChangedCallback{
			@Override
			public void callbackCall(int command, byte[] dataPacket) {
				// TODO Auto-generated method stub
				Log.d("TAG", "BindingScanActivity 's callbackCall..................."+command);
				Message msg = uihandler.obtainMessage(command); 
				uihandler.sendMessage(msg);
			}

			@Override
			public void callbackFail(int command, byte[] dataPacket) {
				// TODO Auto-generated method stub
				Log.d("TAG", "BindingScanActivity 's callbackFail..................."+command);
				Message msg = uihandler.obtainMessage(command); 
				uihandler.sendMessage(msg);
			}
		}
	
}
