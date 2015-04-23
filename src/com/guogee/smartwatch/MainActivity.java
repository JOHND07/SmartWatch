package com.guogee.smartwatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.guogee.smartwatch.ble.BluetoothLeService;
import com.guogee.smartwatch.ble.PlusDotBLEDevice;
import com.guogee.smartwatch.dao.BindDeviceDao;
import com.guogee.smartwatch.service.SWService;
import com.guogee.smartwatch.ui.BeanFragment;
import com.guogee.smartwatch.ui.BindWatchFragment;
import com.guogee.smartwatch.ui.GiftFragment;
import com.guogee.smartwatch.ui.RightMenuFragment;
import com.guogee.smartwatch.ui.SetUpFragment;
import com.guogee.smartwatch.ui.SleepFragment;
import com.guogee.smartwatch.ui.SportFragment;
import com.guogee.smartwatch.ui.SportTargetFragment;
import com.guogee.smartwatch.ui.VersionUpdateActivity;
import com.guogee.smartwatch.ui.leftMenuFragment;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.utils.Log;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity implements OnClickListener {
	
    protected static final int MESSAGE = 123;  
    public Handler handler;  
    public TextView tv = null;  
    private SlidingMenu sm;
	public static final int REQUESTCODE = 0;
	 
	private iSmartApplication isApp;
	private SportFragment sportFragment;
	private SleepFragment sleepFragment;
	
	private SportTargetFragment sportTargetFragment;
	private BindWatchFragment bindFragment;
	private SetUpFragment setupFragment;
	
	//新增“好礼兑换”
	private GiftFragment giftFragment;
	private BeanFragment beanFragment;
	 
	private leftMenuFragment leftFragment;
	public Map<String, Fragment> fragments = new HashMap<String, Fragment>();
	private String mCurrentFragmentIndex ;
	private SharedPreferences sp;
	private BindDeviceDao dao;
	 
	private BluetoothAdapter mBluetoothAdapter;
	private boolean mScanning;
	private Handler mHandler;
	private static final int REQUEST_ENABLE_BT = 1;
	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 10000;
	
	// notification ,beigin
	private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
	private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
	private boolean isEnabledNLS = false;
	 
	private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
	
	private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.notifiacation_tip))
                .setTitle(getResources().getString(R.string.notification_warning))
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
                            }
                        })
                .create().show();
    }

	// notification ,end 
	 
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);	
		
		isApp = (iSmartApplication)getApplication();
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		mHandler = new Handler();
		
		initSlidingMenu(savedInstanceState);
		initView();	
		
		sportFragment = new SportFragment();
		sportFragment.setArguments(getIntent().getExtras());
		
		sleepFragment = new SleepFragment();
		sleepFragment.setArguments(getIntent().getExtras());
		
		sportTargetFragment = new SportTargetFragment();
		sportTargetFragment.setArguments(getIntent().getExtras());

		giftFragment = new GiftFragment();
		giftFragment.setArguments(getIntent().getExtras());
		
		beanFragment = new BeanFragment();
		beanFragment.setArguments(getIntent().getExtras());
		
		bindFragment = new BindWatchFragment();
		bindFragment.setArguments(getIntent().getExtras());
		bindFragment.setMainHanlder(mainHandler);

		isApp.setMainHandler(mainHandler);
		
		setupFragment = new SetUpFragment();
		setupFragment.setArguments(getIntent().getExtras());

		fragments.put("SLEEP_TAG",sleepFragment);
		getSupportFragmentManager().beginTransaction().add(R.id.container, sleepFragment).commit();
        
		Message msg = sleepFragment.uihandler.obtainMessage(SleepFragment.MSG_HIDE_SLEEPVIEW); 
		sleepFragment.uihandler.sendMessage(msg);
		getSupportFragmentManager().beginTransaction().hide(sleepFragment).commit();
		
		
		fragments.put("SPORT_TAG",sportFragment);
		getSupportFragmentManager().beginTransaction().add(R.id.container, sportFragment).commit();
		
		mContent = sportFragment;
		
	    // only the ble is open, bind the service 
		dao = new BindDeviceDao(this);
		List<Map<String, String>> daoList = dao.listBleDevice(null);
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        
		if(daoList.size()!=0){
//		    Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
//		    bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);	 
			
			//search ble device.
			// Use this check to determine whether BLE is supported on the device.  Then you can
	        // selectively disable BLE-related features.
	        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
	            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
	            finish();
	        }

	        // Checks if Bluetooth is supported on the device.
	        if (mBluetoothAdapter == null) {
	            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
	            finish();
	            return;
	        }
	        //search ble device.
	        
	        deviceMacAddress = daoList.get(0).get("mac_address");
	        
	        Log.d("TAG", "deviceMacAddress ============= "+deviceMacAddress);
	        
		}else{
			Toast toast= Toast.makeText(this, getString(R.string.bind_tip), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,toast.getYOffset() / 2);
			toast.show();
		}
		
	    serviceIntent = new Intent(this, SWService.class);
	    serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        startService(serviceIntent);
        
        monitorBatteryState();
        //play sound
        initSoundPool();
        
        isEnabledNLS = isEnabled();
        if (!isEnabledNLS) {
            showConfirmDialog();
        }
        
        initService();
        initNotify();
        
//        //download service.
//        IntentFilter filter = new IntentFilter();
//		filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//		registerReceiver(receiver, filter);
		
		 powerManager = (PowerManager)getSystemService(this.POWER_SERVICE);  
         wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");  

/**		 camera = Camera.open();   **/
	}
	
	private  Intent serviceIntent;
	
	private BroadcastReceiver listener;
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("TAG", "MainActivity 's onResume ..................... ");
		
		isApp.setMainHandler(mainHandler);
		
	    // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        //已经开启蓝牙才才去搜索
        else{
    		List<Map<String, String>> daoList = dao.listBleDevice(null);
    		if(daoList.size()!=0 && !(isApp.getScanBleDevice()) && !(isApp.getConnectBleDevice())){
    			scanLeDevice(true);			
    		}
        }
        
        ///
     // 蓝牙开闭状态接收器
        listener = new BluetoothStateListener();
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(listener, filter);
	}
	  
	@Override
	protected void onPause() {
	     super.onPause();
	     
	     Log.d("TAG","MainActivity onPause .......................");
	     
	     scanLeDevice(false);
	     //cancel the listener
	     unregisterReceiver(listener);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.d("TAG","onActivityResult.......................");
		
		switch(requestCode){
		case REQUEST_ENABLE_BT:
			List<Map<String, String>> daoList = dao.listBleDevice(null);
    		if(daoList.size()!=0){
    			scanLeDevice(true);			
    		}
			break;
		}
	}
	
	///////////////////////
	private class BluetoothStateListener extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
			String msg = null;
			switch(state){
			case BluetoothAdapter.STATE_TURNING_ON:
				msg = "turning on";
				break;
			case BluetoothAdapter.STATE_ON:
				msg = "on";
				List<Map<String, String>> daoList = dao.listBleDevice(null);
	    		if(daoList.size()!=0){
	    			scanLeDevice(true);			
	    		}
				break;
			case BluetoothAdapter.STATE_TURNING_OFF:
				msg = "turning off";
				break; 
			case BluetoothAdapter.STATE_OFF:
				msg = "off";
				break;
			}
		}
	}
	
	//////////////////////
	
	private void initSlidingMenu(Bundle savedInstanceState) {
		setBehindContentView(R.layout.menu_frame);
		
		leftFragment = new leftMenuFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, leftFragment).commit();	
		isApp.setLeftMenuTag(Constant.SPORT_TAG);
		//recorder the current fragment 's index
		
		mCurrentFragmentIndex = "SPORT_TAG" ;
		
		sm = getSlidingMenu();
		isApp.setSlidingMenu(sm);
		
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
//		sm.setTouchModeAbove(SlidingMenu.LEFT);
		sm.setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
				// TODO Auto-generated method stub
//				leftFragment.setListBackground(isApp.getLeftMenuTag());
			}
		});
		
		
		sm.setOnDragListener(new OnDragListener() {
			@Override
			public boolean onDrag(View arg0, DragEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		/**		
		sm.setOnCloseListener(new OnCloseListener() {
			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				Log.d("TAG", "mCurrentFragmentIndex= ========before============ "+mCurrentFragmentIndex);
				
				FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
				
				switch(isApp.getLeftMenuTag()){
				case Constant.SPORT_TAG:
//					getSupportFragmentManager().beginTransaction().replace(R.id.container, sportFragment).commit();

					if(mCurrentFragmentIndex!="SPORT_TAG"){
						
						fragments.get(mCurrentFragmentIndex).onPause();	
						
						if(mCurrentFragmentIndex=="SLEEP_TAG"){
							Message msg = sleepFragment.uihandler.obtainMessage(SleepFragment.MSG_HIDE_SLEEPVIEW); 
							sleepFragment.uihandler.sendMessage(msg);	
						}
						
//						if(fragments.get("GIFT_TAG")!=null){
//							trans.remove(giftFragment);//.commit();
//							fragments.remove("GIFT_TAG");
//						}
						
						if(fragments.get("SPORT_TARGET_TAG")!=null){
							trans.remove(sportTargetFragment);//.commit();
							fragments.remove("SPORT_TARGET_TAG");
						}
						
						if(fragments.get("BIND_TAG")!=null){
							trans.remove(bindFragment);//.commit();
							fragments.remove("BIND_TAG");
						}
						if(fragments.get("SETUP_TAG")!=null){
							trans.remove(setupFragment);//.commit();
							fragments.remove("SETUP_TAG");
						}	
						Log.e("TAG","after remove fragment ' size ========================"+fragments.size());
						trans.hide(sleepFragment).show(sportFragment).commit();
						sportFragment.onResume();
						
						Message msg = sportFragment.uihandler.obtainMessage(SportFragment.MSG_SHOW_SPORTVIEW); 
						sportFragment.uihandler.sendMessage(msg);
						
						if(isApp.getScanBleDevice()){
//							sportFragment.sentSyncBatteryInfo();
							sportFragment.sentSyncTime();
							isApp.setScanBleDevice(false);
						}
						mCurrentFragmentIndex = "SPORT_TAG";						
					}
					
//					else if(mCurrentFragmentIndex == "SPORT_TARGET_TAG"){
//						getSupportFragmentManager().beginTransaction().remove(fragments.get(mCurrentFragmentIndex)).commit();
//					}
//					getSupportFragmentManager().beginTransaction().hide(sleepFragment).show(sportFragment).commit();
					break;
					
				case Constant.SLEEP_TAG:
//					getSupportFragmentManager().beginTransaction().replace(R.id.container, sleepFragment).commit();
					
					if(mCurrentFragmentIndex!="SLEEP_TAG"){
						
						if(mCurrentFragmentIndex == "SPORT_TAG"){
							Message msg = sportFragment.uihandler.obtainMessage(SportFragment.MSG_HIDE_SPORTVIEW); 
							sportFragment.uihandler.sendMessage(msg);
						}
						
						if(fragments.get("SPORT_TARGET_TAG")!=null){
							trans.remove(sportTargetFragment);//.commit();
							fragments.remove("SPORT_TARGET_TAG");
						}
						if(fragments.get("BIND_TAG")!=null){
							trans.remove(bindFragment);//.commit();
							fragments.remove("BIND_TAG");
						}
						if(fragments.get("SETUP_TAG")!=null){
							trans.remove(setupFragment);//.commit();
							fragments.remove("SETUP_TAG");
						}
						Log.e("TAG","after remove fragment ' size ========================"+fragments.size());

						if(fragments.get("SLEEP_TAG")!=null){
							trans.hide(sportFragment).show(sleepFragment).commit();	
							sleepFragment.onResume();
							
							Message msg = sleepFragment.uihandler.obtainMessage(SleepFragment.MSG_SHOW_SLEEPVIEW); 
							sleepFragment.uihandler.sendMessage(msg);
						}else{
							fragments.put("SLEEP_TAG", sleepFragment);
							trans.add(R.id.container, sleepFragment).commit();
//							getSupportFragmentManager().popBackStack();
						}
						
						if(isApp.getScanBleDevice()){
//							sleepFragment.sentSyncBatteryInfo();
							sleepFragment.sentSyncTime();
							isApp.setScanBleDevice(false);
						}
						
						mCurrentFragmentIndex = "SLEEP_TAG";
					}
					break;
					
					
//				case Constant.GIFT_TAG:
//					if(mCurrentFragmentIndex!="GIFT_TAG"){
//						
//						Message msg = sleepFragment.uihandler.obtainMessage(SleepFragment.MSG_HIDE_SLEEPVIEW); 
//						sleepFragment.uihandler.sendMessage(msg);	
//						
//						Message msg2 = sportFragment.uihandler.obtainMessage(SportFragment.MSG_HIDE_SPORTVIEW); 
//						sportFragment.uihandler.sendMessage(msg2);
//						
//						if(fragments.get("GIFT_TAG")!=null){
//							trans.hide(fragments.get(mCurrentFragmentIndex)).show(giftFragment).commit();
//							giftFragment.onResume();
//						}else{
//							fragments.put("GIFT_TAG", giftFragment);
//							trans.add(R.id.container, giftFragment).commit();
//						}
//						mCurrentFragmentIndex = "GIFT_TAG";
//					}
//					break;
					
					
				case Constant.SPORT_TARGET_TAG:
//					getSupportFragmentManager().beginTransaction().replace(R.id.container, new SportTargetFragment()).commit();

					if(mCurrentFragmentIndex!="SPORT_TARGET_TAG"){
//						fragments.get(mCurrentFragmentIndex).onPause();
						
//						if(mCurrentFragmentIndex=="SLEEP_TAG"){
							Message msg = sleepFragment.uihandler.obtainMessage(SleepFragment.MSG_HIDE_SLEEPVIEW); 
							sleepFragment.uihandler.sendMessage(msg);	
//						}
						
//						if(mCurrentFragmentIndex == "SPORT_TAG"){
							Message msg2 = sportFragment.uihandler.obtainMessage(SportFragment.MSG_HIDE_SPORTVIEW); 
							sportFragment.uihandler.sendMessage(msg2);
//						}
						
						if(fragments.get("SPORT_TARGET_TAG")!=null){
							trans.hide(fragments.get(mCurrentFragmentIndex)).show(sportTargetFragment).commit();
							sportTargetFragment.onResume();
						}else{
							fragments.put("SPORT_TARGET_TAG", sportTargetFragment);
							trans.add(R.id.container, sportTargetFragment).commit();
						}
						mCurrentFragmentIndex = "SPORT_TARGET_TAG";
					}
					break;
				case Constant.BIND_TAG:
//					getSupportFragmentManager().beginTransaction().replace(R.id.container, new BindWatchFragment()).commit();
					
					if(mCurrentFragmentIndex!="BIND_TAG"){
//						fragments.get(mCurrentFragmentIndex).onPause();
						
//						if(mCurrentFragmentIndex=="SLEEP_TAG"){
							Message msg = sleepFragment.uihandler.obtainMessage(SleepFragment.MSG_HIDE_SLEEPVIEW); 
							sleepFragment.uihandler.sendMessage(msg);	
//						}
						
//						if(mCurrentFragmentIndex == "SPORT_TAG"){
							Message msg2 = sportFragment.uihandler.obtainMessage(SportFragment.MSG_HIDE_SPORTVIEW); 
							sportFragment.uihandler.sendMessage(msg2);
//						}
						
						if(fragments.get("BIND_TAG")!=null){
							trans.hide(fragments.get(mCurrentFragmentIndex)).show(bindFragment).commit();
							bindFragment.onResume();
						}else{
							fragments.put("BIND_TAG", bindFragment);
							trans.add(R.id.container, bindFragment).commit();
						}
						mCurrentFragmentIndex = "BIND_TAG";
					}
					break;
					
//				case Constant.SETUP_TAG:
////					getSupportFragmentManager().beginTransaction().replace(R.id.container, new SetUpFragment()).commit();
//					if(mCurrentFragmentIndex!="SETUP_TAG"){
////						fragments.get(mCurrentFragmentIndex).onPause();
////						if(mCurrentFragmentIndex=="SLEEP_TAG"){
//							Message msg = sleepFragment.uihandler.obtainMessage(SleepFragment.MSG_HIDE_SLEEPVIEW); 
//							sleepFragment.uihandler.sendMessage(msg);	
////						}
//						
////						if(mCurrentFragmentIndex == "SPORT_TAG"){
//							Message msg2 = sportFragment.uihandler.obtainMessage(SportFragment.MSG_HIDE_SPORTVIEW); 
//							sportFragment.uihandler.sendMessage(msg2);
////						}
//						
//						if(fragments.get("SETUP_TAG")!=null){
//							trans.hide(fragments.get(mCurrentFragmentIndex)).show(setupFragment).commit();
//							setupFragment.onResume();
//						}else{
//							fragments.put("SETUP_TAG", setupFragment);
//							trans.add(R.id.container, setupFragment).commit();
//						}
//						mCurrentFragmentIndex = "SETUP_TAG";
//					}
//					break;
					
				}
			}
		});
		 **/		
		
		
		sm.setOnCloseListener(new OnCloseListener() {
			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
				switch(isApp.getLeftMenuTag()){
				case Constant.SPORT_TAG:
					Message msg = sportFragment.uihandler.obtainMessage(SportFragment.MSG_SHOW_SPORTVIEW); 
					sportFragment.uihandler.sendMessage(msg);
					
					msg = sleepFragment.uihandler.obtainMessage(SleepFragment.MSG_HIDE_SLEEPVIEW); 
					sleepFragment.uihandler.sendMessage(msg);
					
					switchContent(mContent,sportFragment);
					mContent = sportFragment;
					
					if(isApp.getScanBleDevice()){
//						sportFragment.sentSyncBatteryInfo();
						sportFragment.sentSyncTime();
						isApp.setScanBleDevice(false);
					}
					break;
					
				case Constant.SLEEP_TAG:
					Message msg6 = sportFragment.uihandler.obtainMessage(SportFragment.MSG_HIDE_SPORTVIEW); 
					sportFragment.uihandler.sendMessage(msg6);
					
					msg6 = sleepFragment.uihandler.obtainMessage(SleepFragment.MSG_SHOW_SLEEPVIEW); 
					sleepFragment.uihandler.sendMessage(msg6);
					
					switchContent(mContent,sleepFragment);
					mContent = sleepFragment;
					break;
					
				case Constant.GIFT_TAG:
					Message msg2 = sportFragment.uihandler.obtainMessage(SportFragment.MSG_HIDE_SPORTVIEW); 
					sportFragment.uihandler.sendMessage(msg2);
					
					msg2 = sleepFragment.uihandler.obtainMessage(SleepFragment.MSG_HIDE_SLEEPVIEW); 
					sleepFragment.uihandler.sendMessage(msg2);
					
//					msg2 = giftFragment.uihandler.obtainMessage(GiftFragment.MSG_LOAD_ACTIVITY);
//					giftFragment.uihandler.sendMessage(msg2);
					
					switchContent(mContent,giftFragment);
					mContent = giftFragment;
					break;
				case Constant.SPORT_TARGET_TAG:
					Message msg3 = sportFragment.uihandler.obtainMessage(SportFragment.MSG_HIDE_SPORTVIEW); 
					sportFragment.uihandler.sendMessage(msg3);
					
					msg3 = sleepFragment.uihandler.obtainMessage(SleepFragment.MSG_HIDE_SLEEPVIEW); 
					sleepFragment.uihandler.sendMessage(msg3);
					
					switchContent(mContent,sportTargetFragment);
					mContent = sportTargetFragment;
					break;
				case Constant.BIND_TAG:
					Message msg4 = sportFragment.uihandler.obtainMessage(SportFragment.MSG_HIDE_SPORTVIEW); 
					sportFragment.uihandler.sendMessage(msg4);
					
					msg4 = sleepFragment.uihandler.obtainMessage(SleepFragment.MSG_HIDE_SLEEPVIEW); 
					sleepFragment.uihandler.sendMessage(msg4);
					
					switchContent(mContent,bindFragment);
					mContent = bindFragment;
					break;
				case Constant.BEAN_TAG:
					Message msg5 = sportFragment.uihandler.obtainMessage(SportFragment.MSG_HIDE_SPORTVIEW); 
					sportFragment.uihandler.sendMessage(msg5);
					
					msg5 = sleepFragment.uihandler.obtainMessage(SleepFragment.MSG_HIDE_SLEEPVIEW); 
					sleepFragment.uihandler.sendMessage(msg5);
					
					msg5 = beanFragment.uihandler.obtainMessage(beanFragment.MSG_LOAD_DATA);
					beanFragment.uihandler.sendMessage(msg5);
					
					switchContent(mContent,beanFragment);
					mContent = beanFragment;
					break;
				default:
					break;
				}
			}
		});
	
	}
	
	private Fragment mContent;
	
	public void switchContent(Fragment from, Fragment to) {  
        if (mContent != to) {  
            mContent = to;  
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过  
                transaction.hide(from).add(R.id.container, to);
                transaction.commit(); // 隐藏当前的fragment，add下一个到Activity中  
                getSupportFragmentManager().executePendingTransactions();
            } else {  
                transaction.hide(from).show(to);
                transaction.commit(); // 隐藏当前的fragment，显示下一个  
                getSupportFragmentManager().executePendingTransactions();
            }  
        }  
    }
	
	private void initView() {
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		sm.setSecondaryMenu(R.layout.menu_frame_two);					
		
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_two,new RightMenuFragment()).commit();
		sm.setSecondaryShadowDrawable(R.drawable.shadowright);
		sm.setShadowDrawable(R.drawable.shadow);
	}

	class myThread extends Thread {  
        public void run() {  
            super.run();  
            while (Thread.interrupted() == false) {  
                try {  
                    Thread.sleep(1000);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
                Message m = new Message();  
                m.what = MainActivity.MESSAGE;  
                MainActivity.this.handler.sendMessage(m);  
            }  
        }  
    }

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		}
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    isApp.setLeftMenuTag(0);
	    isApp.setConnectBleDevice(false);
	    
	    Log.d("TAG","MainActivity 's onDestroy ......................."+isBindService);
	    
	    if(isBindService){
		   unbindService(mServiceConnection);	    	
		   mBluetoothLeService = null;
	    }
	    
//	    unregisterReceiver(receiver);//download service
	    unregisterReceiver(batteryLevelRcvr);
	    stopService(serviceIntent);
//	    android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	//////////////////////---- Load cursor 's data -----/////////////////////////////
	
	public class CursorData {
		public String title;
//		public Alarm alarm ;
		public boolean isCheck;
		public boolean day1;
		public boolean day2;
		public boolean day3;
		public boolean day4;
		public boolean day5;
		public boolean day6;
		public boolean day7;
	}
		
	//////////////////////---- Load cursor 's data -----/////////////////////////////
	
    private long firstime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(isApp.getLeftMenuTag()==Constant.SPORT_TAG){
//				sportFragment.disappearSyncProgressDialog();
			}else if(isApp.getLeftMenuTag()==Constant.SLEEP_TAG){
//				sleepFragment.disappearSyncProgressDialog();
			}
			
			long secondtime = System.currentTimeMillis();
			if (secondtime - firstime > 3000) {
				Toast.makeText(MainActivity.this, getResources().getString(R.string.exit_tip), Toast.LENGTH_SHORT).show();
				firstime = System.currentTimeMillis();
				return true;
			} else {
//				finish();
				moveTaskToBack(true);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	//////////////---------------Ble service--------------------//////////////////
	// Code to manage Service lifecycle.
	private boolean isBindService = false;
	private BluetoothLeService mBluetoothLeService;
//	private String deviceMacAddress ;
	
	public static String deviceMacAddress ="";
	
	private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            mBluetoothLeService.setMainHandle(mainHandler);
            isBindService = true;
            // Automatically connects to the device upon successful start-up initialization.
            
        	List<Map<String, String>> daoList = dao.listBleDevice(null);
//        	if(daoList.size()>0){
//            	Log.d("TAG", "connect mac 's address ================= "+daoList.get(0).get("mac_address"));
//                mBluetoothLeService.connect(daoList.get(0).get("mac_address"));
//        	}
        	
//        	new Handler().postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					mBluetoothLeService.connect(deviceMacAddress);
//				}
//			}, 3000);
        	
            mBluetoothLeService.connect(deviceMacAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
            isBindService = false;
        }
    };
    
    //////////////---------------Ble service--------------------//////////////////
    public final static int MSG_BLE_CONNECTED = 0;
    
    public final static int MSG_BLE_DISCONNECTED = 1;
    
    public final static int MSG_UPDATE_SPORT_DATA = 2;
    
    public final static int MSG_UPDATE_SLEEP_DATA = 3;
    
    public final static int MSG_UPDATE_SLEEP_DATA_ERROR = 7;
    
    public final static int MSG_UPDATE_SLEEP_DATA2 = 6;
    
    public final static int MSG_SUB_PACKAGE = 4;
    
    public final static int MSG_BLE_DEVICE_INFO = 5;
    
    public final static int MSG_CONNECT_BLE_SERVICE = 8;
    
    public final static int MSG_PLAY_SOUND = 9;
    
    public final static int MSG_BLE_SYNC_TIME = 10;
    
    public final static int MSG_DISCONNECT = 11;
    
    public final static int MSG_TAKE_PIC = 12;
    
    public final static int MSG_UPDATE_SPORT = 13;
    
    public final static int MSG_UPDATE_SLEEP = 14;
    
    public final static int MSG_BIND_BLE = 15;
    
    public final static int MSG_UNBIND_BLE = 16;
    
    public final static int MSG_BLE_DFU_FINISH_CONNECT_BLE = 17;
    
    public Handler mainHandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			switch (msg.what) {
			case MSG_BLE_CONNECTED:
				Log.e("TAG", "receive ble 's connected msg .......................");
				scanLeDevice(false);	
//				updateProgressStatus();
				
//				showConnectStateNotify(R.string.connected_tip);
				
				break;
			case MSG_BLE_DISCONNECTED:
				Log.e("TAG", "receive ble 's disconnected msg .......................");
				
//				unbindService(mServiceConnection);
				
				if(isBindService){
					unbindService(mServiceConnection);
					mBluetoothLeService = null;
					isBindService = false;
					scanLeDevice(false);	
				}
				
				//Fix Bug , 这里可能用户自己关掉蓝牙
				if (mBluetoothAdapter.isEnabled()) {
					List<Map<String, String>> daoList = dao.listBleDevice(null);
					if(daoList.size()!=0){
						scanLeDevice(true);	//Bug.--- 应该判断是否绑定
					}	
				}
				
//				showConnectStateNotify(R.string.disconnected_tip);
				
				break;
			case MSG_CONNECT_BLE_SERVICE:
			    Intent gattServiceIntent = new Intent(MainActivity.this, BluetoothLeService.class);
			    bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);	    
//			    startService(gattServiceIntent);
			    break;
			case MSG_PLAY_SOUND:
				playSound(1,0);
				break;
			case MSG_DISCONNECT:
				if(mBluetoothLeService!=null){
					mBluetoothLeService.disconnect();
//					showConnectStateNotify(R.string.disconnected_tip);
				}
				break;
			case MSG_BIND_BLE:
				data.getString("DeviceName");
				deviceMacAddress = data.getString("DeviceMac");
			    Intent gattServiceIntent2 = new Intent(MainActivity.this, BluetoothLeService.class);
			    bindService(gattServiceIntent2, mServiceConnection, BIND_AUTO_CREATE);				
				break;
			case MSG_UNBIND_BLE:
				break;
			case MSG_BLE_DFU_FINISH_CONNECT_BLE:
				if(isBindService){
					unbindService(mServiceConnection);
					mBluetoothLeService = null;
					isBindService = false;
					scanLeDevice(false);	
				}
				
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						scanLeDevice(true);	//Bug.--- 应该判断是否绑定
					}
				}, 3000);
				
				break;
			}
		}
	};
	
	///////////// ------------------- 检测电池 --------------------- //////////////
//	private TextView batterLevel;
    private BroadcastReceiver batteryLevelRcvr;
    private IntentFilter batteryLevelFilter;
    
    public static boolean isSendBattery20 = false;
    public static boolean isSendBattery15 = false;
    public static boolean isSendBattery10 = false;
    public static boolean isSendBattery5 = false;
    public static boolean isSendBattery1 = false;
    
	private void monitorBatteryState() {
        batteryLevelRcvr = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                StringBuilder sb = new StringBuilder();
                int rawlevel = intent.getIntExtra("level", -1);
                int scale = intent.getIntExtra("scale", -1);
                int status = intent.getIntExtra("status", -1);
                int health = intent.getIntExtra("health", -1);
                int level = -1; // percentage, or -1 for unknown
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                sb.append("The phone");
                if (BatteryManager.BATTERY_HEALTH_OVERHEAT == health) {
                    sb.append("'s battery feels very hot!");
                } else {
                	
                	int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                	boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                	
                	//在充电状态，不提示....
                	if(usbCharge){
//                		Toast.makeText(MainActivity.this, "usb charge.................", Toast.LENGTH_SHORT).show();	
                		return;
                	}
                	
                    switch (status) {
                        case BatteryManager.BATTERY_STATUS_CHARGING:
                            sb.append("'s battery");
                            switch(level){
                            case 20:
                            	if(isApp.getConnectBleDevice() && sp.getBoolean(Constant.BATTERY_FALG, true) && !isSendBattery20){
                            		isSendBattery20 = true;
                              	    PlusDotBLEDevice plusdot = PlusDotBLEDevice.getInstance();
                              	    plusdot.synchronizatioEmailRemindEvent(true);
                              	    showNotify("20");
                                    sb.append(" is charging, battery level is low" + "[" + level + "]");
                              	}
                            	break;
                            case 15:
                            	if(isApp.getConnectBleDevice() && sp.getBoolean(Constant.BATTERY_FALG, true) && !isSendBattery15){
                            		isSendBattery15 = true;
                              	    PlusDotBLEDevice plusdot = PlusDotBLEDevice.getInstance();
                              	    plusdot.synchronizatioEmailRemindEvent(true);
                              	    showNotify("15");
                                    sb.append(" is charging, battery level is low" + "[" + level + "]");
                              	}
                            	break;
                            case 10:
                            	if(isApp.getConnectBleDevice() && sp.getBoolean(Constant.BATTERY_FALG, true) && !isSendBattery10){
                            		isSendBattery10 = true;
                              	    PlusDotBLEDevice plusdot = PlusDotBLEDevice.getInstance();
                              	    plusdot.synchronizatioEmailRemindEvent(true);
                              	    showNotify("10");
                                    sb.append(" is charging, battery level is low" + "[" + level + "]");
                              	}
                            	break;
                            case 5:
                            	if(isApp.getConnectBleDevice() && sp.getBoolean(Constant.BATTERY_FALG, true) && !isSendBattery5){
                            		isSendBattery5 = true;
                              	    PlusDotBLEDevice plusdot = PlusDotBLEDevice.getInstance();
                              	    plusdot.synchronizatioEmailRemindEvent(true);
                              	    showNotify("5");
                                    sb.append(" is charging, battery level is low" + "[" + level + "]");
                              	}
                            	break;
                            case 1:
                            	if(isApp.getConnectBleDevice() && sp.getBoolean(Constant.BATTERY_FALG, true) && !isSendBattery1){
                            		isSendBattery1 = true;
                              	    PlusDotBLEDevice plusdot = PlusDotBLEDevice.getInstance();
                              	    plusdot.synchronizatioEmailRemindEvent(true);
                              	    showNotify("1");
                                    sb.append(" is charging, battery level is low" + "[" + level + "]");
                              	}
                            	break;
                            }
                            break;
                        default:
                        	isSendBattery20 = false;
                            isSendBattery15 = false;
                            isSendBattery10 = false;
                            isSendBattery5  = false;
                            isSendBattery1  = false;
                            sb.append("'s battery is indescribable!");
                            break;
                    }
                }
                sb.append(' ');
//                Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
//                Log.d("TAG", "battery level is low ....................");
            }
        };
        batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelRcvr, batteryLevelFilter);
    }
    ///////////// ------------------- 检测电池 --------------------- //////////////
	
	
    ///////////// ------------------- BLE 搜索 --------------------- //////////////
	 private void scanLeDevice(final boolean enable) {
		 Log.d("TAG", "MainActivity 's scanLeDevice................."+enable);
	        if (enable) {
	            // Stops scanning after a pre-defined scan period.
//	            mHandler.postDelayed(new Runnable() {
//	                @Override
//	                public void run() {
//	                    mScanning = false;
//	                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//	                }
//	            }, 3000);

	            mScanning = true;
	            mBluetoothAdapter.startLeScan(mLeScanCallback);
	        } else {
	            mScanning = false;
	            mBluetoothAdapter.stopLeScan(mLeScanCallback);
	        }
	    }
	
	 // Device scan callback.
	    private BluetoothAdapter.LeScanCallback mLeScanCallback =
	            new BluetoothAdapter.LeScanCallback() {
	        @Override
	        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
	            runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                	
//	                	if(isApp.getOTAMode())//|| device.getName().equals("DfuTarg")){
//	                	{
//	                		return;
//	                	}
	                	
	                	List<Map<String, String>> daoList = dao.listBleDevice(null);
	                	for(int i=0;i<daoList.size();i++){ //null bug
	                		
	                		Log.d("TAG","scan device.getAddress() ================= "+device.getAddress());
	                		
	                		if(device.getAddress().equals(daoList.get(i).get("mac_address"))){
//	                			connect(device);
	                			mHandler.post(new Runnable() {
	            					@Override
	            					public void run() {
	            						//bug , 服务绑定后不会再绑定
	            						Log.d("TAG","onLeScan .........bind service ............");
	            						Intent gattServiceIntent = new Intent(MainActivity.this, BluetoothLeService.class);
	            						bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
//	            						startService(gattServiceIntent);
	            					}
	            				});
	                		}
	                	}
	                }
	            });
	        }
	    };

	    
	    
	    
	    
///////////// ------------------- BLE 搜索 --------------------- //////////////
	    
		//play sound
		private SoundPool spool;
		private float volume;
		private int currentID;
		private HashMap<Integer, Integer> hm;
		private PowerManager powerManager = null;  
		private WakeLock wakeLock = null;
        
		private void initSoundPool() {
			spool=new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
	        hm =new HashMap<Integer, Integer>();
	        hm.put(1, spool.load(MainActivity.this, R.raw.binbind, 1));
	    }

//		private MyThead myThead ;
	    private void playSound(final int num,int loop){
	    /**	
	    	myThead = new MyThead();  
	    	Thread thread = new Thread(myThead);  
	        thread.start();  
	    **/    
	    	new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
//					myThead.setSupend(false); // = false;
					spool.stop(currentID);
				}
			}, 8000);
	    	
	    	wakeUpAndUnlock();
	    	Vibrate(8000);
	    	
	        AudioManager am=(AudioManager) getSystemService(this.AUDIO_SERVICE);
	        float currentSound=am.getStreamVolume(AudioManager.STREAM_MUSIC);
	        float maxSound=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	        volume=currentSound/maxSound;
	        currentID=spool.play(hm.get(num), volume, volume, 1, -1, 1.0f);
	    }
	    
	    public  void wakeUpAndUnlock(){  
	        KeyguardManager km= (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);  
	        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");  
	        //解锁  
	        kl.disableKeyguard();  
	        //获取电源管理器对象  
	        PowerManager pm=(PowerManager) getSystemService(Context.POWER_SERVICE);  
	        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag  
	        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");  
	        //点亮屏幕  
	        wl.acquire();  
	        //释放  
	        wl.release();  
	    }  
	    
	    public void Vibrate(long milliseconds) {   
            Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);   
            vib.vibrate(milliseconds);   
        }  
	    
/**	    
	    Camera  camera;
	    private void openLed(){
//	    	camera = Camera.open(); 
	    	Parameters parameter = camera.getParameters();  
	    	parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
	    	camera.setParameters(parameter);	
	    }
	    
	    private void closeLed(){
//			camera = Camera.open(); 
	    	Parameters parameter = camera.getParameters();  
			parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(parameter);
//			camera.release();
	    }
	    
	    class MyThead implements Runnable  
	    {  
	    	
	        private boolean suspend = true;  
	      
	        public void setSupend(boolean isSuspend) {  
	        	suspend = isSuspend;
	        }  
	    	
	        public void run()  
	        {  
	        	while(suspend){
//		            System.out.println("我休息了！");  
//	        		openLed(); 
		        	closeLed();	
		            try  
		            {  
		                Thread.sleep(1000);  
		            }  
		            catch (InterruptedException e)  
		            {  
		            }  
//		            System.out.println("一秒后在叫我吧！");  
		            openLed();  	
		        	closeLed();
	        	}
	        }  
	    }  

	    
	    //灯光闪烁
		private   Timer ledTimer;

		private TimerTask 	ledTimertask;//数据同步任务

		private  boolean isOpenCamera = false;
		public void controlLed(boolean enabled){
			if(ledTimer!=null){
				return;
			}
			final Camera  camera = Camera.open();
			final Parameters parameter = camera.getParameters();  
			ledTimertask = new TimerTask() {  
				public void run() {
					if(!isOpenCamera){
				    	camera.startPreview();
//				    	Parameters parameter = camera.getParameters();  
				    	parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
				    	camera.setParameters(parameter);
				    	isOpenCamera = true;
				    	camera.release();
					}
					else if(isOpenCamera){
						Parameters parameter = camera.getParameters();  
						parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
						camera.setParameters(parameter);
						isOpenCamera = false;
						camera.release();
					}
				}  
			}; 
			ledTimer = new Timer(); 
			ledTimer.schedule(ledTimertask,100,1000);
		}
	    
	    //灯光闪烁
**/	    
	    
///////////// ------------------- 下载 --------------------- //////////////	  
	    private BroadcastReceiver receiver = new BroadcastReceiver() {   
	        @Override   
	        public void onReceive(Context context, Intent intent) {   
	            //这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听  
	        	String action = intent.getAction();
	        	if(action.equals("android.intent.action.DOWNLOAD_COMPLETE")){
		            SharedPreferences prefs = getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
		            if(prefs.contains(VersionUpdateActivity.DL_ID)){
		            	DownloadManager.Query query = new DownloadManager.Query();
		            	DownloadManager downloadManager = isApp.getDownloadManager();
		            	if(downloadManager == null) return ;
		        		query.setFilterById(prefs.getLong(VersionUpdateActivity.DL_ID, 0));
		        		Cursor cursor = downloadManager.query(query);
		        		if(cursor.moveToFirst()){
		        			int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)); 
		        			switch (status) {
		        			case DownloadManager.STATUS_RUNNING:  //正在下载
		        				Log.d("TAG", "MainActivity STATUS_RUNNING ...................... ");  
		        				break;
		        			 case DownloadManager.STATUS_SUCCESSFUL:
//		        				 prefs.edit().remove(VersionUpdateActivity.DL_ID).commit();
//		        					String path = Environment.getExternalStorageDirectory().getPath() + "/iSmartAndroid/"+ prefs.getString("apkname", "iSmartAndroid.apk");
//		        					Intent installApk = new Intent(Intent.ACTION_VIEW);  
//		        					installApk.setDataAndType(Uri.fromFile(new File(path)),   
//		        	                        "application/vnd.android.package-archive");  
//		        					startActivity(installApk); 
		        				
		        				 Log.d("TAG", "MainActivity STATUS_SUCCESSFUL ...................... ");
		        				 
		        				 break;
		        			default:
		        				break;
		        			}
		        		}
		            }
	        	}
	        }   
	    };  
	    
	    
	//----------------------通知栏----------------------//
	/** Notification的ID */
	private int notifyId = 100;
	
	/** Notification构造器 */
	NotificationCompat.Builder mBuilder;
	
	/** Notification管理 */
	public NotificationManager mNotificationManager;
	
	private void initNotify(){
		mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setContentTitle(getResources().getString(R.string.power_warning_title))
				.setContentText(getResources().getString(R.string.power_warning_text))
				.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
//				.setNumber(number)//显示数量
				.setTicker(getResources().getString(R.string.power_warning_title))//通知首次出现在通知栏，带上升动画效果的
				.setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
				.setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
//				.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消  
				.setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
				//Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
				.setSmallIcon(R.drawable.ic_launcher);
	}

	/**
	 * 初始化要用到的系统服务
	 */
	private void initService() {
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}
	
	/** 
	 * 清除当前创建的通知栏 
	 */
	public void clearNotify(int notifyId){
		mNotificationManager.cancel(notifyId);//删除一个特定的通知ID对应的通知
//		mNotification.cancel(getResources().getString(R.string.app_name));
	}
	
	/**
	 * 清除所有通知栏
	 * 
	 */
	public void clearAllNotify() {
		mNotificationManager.cancelAll();// 删除你发的所有通知
	}
	
	/**
	 * @获取默认的pendingIntent,为了防止2.3及以下版本报错
	 * @flags属性:  
	 * 在顶部常驻:Notification.FLAG_ONGOING_EVENT  
	 * 点击去除： Notification.FLAG_AUTO_CANCEL 
	 */
	public PendingIntent getDefalutIntent(int flags){
		PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), flags);
		return pendingIntent;
	}
	
	/** 显示通知栏 */
	public void showNotify(String pos){
		mBuilder.setContentTitle(getResources().getString(R.string.power_warning_title))
				.setContentText(getResources().getString(R.string.power_warning_text_begin)+pos+"%"+getResources().getString(R.string.power_warning_text_end))
//				.setNumber(number)//显示数量
				.setTicker(getResources().getString(R.string.power_warning_title));//通知首次出现在通知栏，带上升动画效果的
		mNotificationManager.notify(notifyId, mBuilder.build());
//		mNotification.notify(getResources().getString(R.string.app_name), notiId, mBuilder.build());
	}

	/** 显示通知栏 */
	public void showConnectStateNotify(int id){
		mBuilder.setContentTitle(getResources().getString(R.string.notification_warning))
				.setContentText(getResources().getString(id))
				.setTicker(getResources().getString(R.string.notification_warning));//通知首次出现在通知栏，带上升动画效果的
		mNotificationManager.notify(notifyId, mBuilder.build());
	}
	
    //----------------------通知栏----------------------//    
}
