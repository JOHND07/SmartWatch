package com.guogee.smartwatch.ui;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import no.nordicsemi.android.error.GattError;
import no.nordicsemi.android.nrftoolbox.dfu.DfuService;

import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.ble.PlusDotBLEDevice;
import com.guogee.smartwatch.ble.StatusChangedCallback;
import com.guogee.smartwatch.dao.BindDeviceDao;
import com.guogee.smartwatch.service.RemoteUserService;
import com.guogee.smartwatch.ui.BindWatchFragment.UIStatusChangedCallback;
import com.guogee.smartwatch.utils.Constant;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnClickListener{

	private ImageButton backBtn;
	private RelativeLayout accountLy;
	
	private LinearLayout syncLy;
	private LinearLayout troubleLayout;
	private LinearLayout problemLayout;
	private LinearLayout fitmodelLayout;
	private LinearLayout updateVersionLayout;
	
	private Button exitBtn;
	private Button testBtn;
	private iSmartApplication isApp;
	
	private TextView loginName;
	private SharedPreferences sp;
	private ProgressBar login_bar;
	
	private Button testBtn1;
	private Button testBtn2;
	
	private ImageView versionHint;
	private TextView versionText;
	private TextView appVersionText;
	private TextView firmVersionText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_layout);
	
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		initView();
		
		isApp = (iSmartApplication) getApplication();	
		isApp.addCallBack(new UIStatusChangedCallback());
	}

	/////////////////////
	
	private final BroadcastReceiver mDfuUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			// DFU is in progress or an error occurred 
			final String action = intent.getAction();

			Log.d("TAG", "action =============== "+action);
			
			if (DfuService.BROADCAST_PROGRESS.equals(action)) {
				
				final int progress = intent.getIntExtra(DfuService.EXTRA_DATA, 0);
				final int currentPart = intent.getIntExtra(DfuService.EXTRA_PART_CURRENT, 1);
				final int totalParts = intent.getIntExtra(DfuService.EXTRA_PARTS_TOTAL, 1);
				updateProgressBar(progress, currentPart, totalParts, false);
			} else if (DfuService.BROADCAST_ERROR.equals(action)) {
				
				final int error = intent.getIntExtra(DfuService.EXTRA_DATA, 0);
				updateProgressBar(error, 0, 0, true);

				// We have to wait a bit before canceling notification. This is called before DfuService creates the last notification.
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// if this activity is still open and upload process was completed, cancel the notification
						final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						manager.cancel(DfuService.NOTIFICATION_ID);
					}
				}, 200);
			}
		}
	};
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
		broadcastManager.unregisterReceiver(mDfuUpdateReceiver);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		// We are using LocalBroadcastReceiver instead of normal BroadcastReceiver for optimization purposes
		final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
		broadcastManager.registerReceiver(mDfuUpdateReceiver, makeDfuUpdateIntentFilter());
		
		loginName = (TextView) findViewById(R.id.login_name);
		
//		if(!sp.getString(Constant.LOGIN_NAME, "").equals("")){
//			loginName.setVisibility(View.VISIBLE);
//			loginName.setText(sp.getString(Constant.LOGIN_NAME, ""));
//			accountLy.setClickable(false);
//		}
		
		if(sp.getBoolean(Constant.LOGIN_FALG, false)){
			loginName.setVisibility(View.VISIBLE);
			loginName.setText(sp.getString(Constant.LOGIN_NAME, ""));
			accountLy.setClickable(false);
			exitBtn.setVisibility(View.VISIBLE);
			
			exitBtn.setText(getResources().getString(R.string.login_out));
		}else{
			exitBtn.setClickable(false);
			exitBtn.setBackground(getResources().getDrawable(R.drawable.person_info_btn_b));
			
		}
		
		checkUpdateVersion();
	}

	private void updateProgressBar(final int progress, final int part, final int total, final boolean error) {
		switch (progress) {
		case DfuService.PROGRESS_CONNECTING:
			mProgressBar.setIndeterminate(true);
			mTextPercentage.setText(R.string.dfu_status_connecting);
			break;
		case DfuService.PROGRESS_STARTING:
			mProgressBar.setIndeterminate(true);
			mTextPercentage.setText(R.string.dfu_status_starting);
			break;
		case DfuService.PROGRESS_VALIDATING:
			mProgressBar.setIndeterminate(true);
			mTextPercentage.setText(R.string.dfu_status_validating);
			break;
		case DfuService.PROGRESS_DISCONNECTING:
			mProgressBar.setIndeterminate(true);
			mTextPercentage.setText(R.string.dfu_status_disconnecting);
			break;
		case DfuService.PROGRESS_COMPLETED:
			mTextPercentage.setText(R.string.dfu_status_completed);
			// let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					onTransferCompleted();

					// if this activity is still open and upload process was completed, cancel the notification
					final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					manager.cancel(DfuService.NOTIFICATION_ID);
				}
			}, 200);
			break;
		case DfuService.PROGRESS_ABORTED:
			mTextPercentage.setText(R.string.dfu_status_aborted);
			// let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					onUploadCanceled();

					// if this activity is still open and upload process was completed, cancel the notification
					final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					manager.cancel(DfuService.NOTIFICATION_ID);
				}
			}, 200);
			break;
		default:
			mProgressBar.setIndeterminate(false);
			if (error) {
				showErrorMessage(progress);
			} else {
				mProgressBar.setProgress(progress);
				mTextPercentage.setText(getString(R.string.progress, progress));
				if (total > 1)
					mTextUploading.setText(getString(R.string.dfu_status_uploading_part, part, total));
				else
					mTextUploading.setText(R.string.dfu_status_uploading);
			}
			break;
		}
	}
	
	private void showErrorMessage(final int code) {
		Toast.makeText(this, "Upload failed: " + GattError.parse(code) + " (" + (code & ~(DfuService.ERROR_MASK | DfuService.ERROR_REMOTE_MASK)) + ")", Toast.LENGTH_SHORT).show();
	}

	public void onUploadCanceled() {
		Toast.makeText(this, "Uploading of the application has been canceled", Toast.LENGTH_SHORT).show();
	}
	
	private static IntentFilter makeDfuUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(DfuService.BROADCAST_PROGRESS);
		intentFilter.addAction(DfuService.BROADCAST_ERROR);
		intentFilter.addAction(DfuService.BROADCAST_LOG);
		return intentFilter;
	}
	
	private void onTransferCompleted() {
//		showToast("升级成功");
		Toast.makeText(this, "升级成功", Toast.LENGTH_SHORT).show();
	}
	
	private void showToast(final int messageResId) {
		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
	}
	/////////////////////

	private ProgressBar mProgressBar;
	
	private TextView mTextPercentage;
	
	private TextView mTextUploading;
	
	private void initView(){
		
		mProgressBar = (ProgressBar) findViewById(R.id.progressbar_file);
		mTextPercentage = (TextView) findViewById(R.id.textviewProgress);
		mTextUploading = (TextView) findViewById(R.id.textviewUploading);
		
		backBtn = (ImageButton) findViewById(R.id.left_Btn);
		backBtn.setOnClickListener(this);
		
		exitBtn = (Button) findViewById(R.id.exit_btn);
		exitBtn.setOnClickListener(this);
		
		testBtn = (Button) findViewById(R.id.test_btn);
		testBtn.setOnClickListener(this);
		
		testBtn1 = (Button) findViewById(R.id.test1_btn);
		testBtn1.setOnClickListener(this);
		
		testBtn2 = (Button) findViewById(R.id.test2_btn);
		testBtn2.setOnClickListener(this);

		versionHint = (ImageView) findViewById(R.id.version_image_hint);
		
		versionText = (TextView) findViewById(R.id.version_text);
		versionText.setVisibility(View.GONE);
		
		accountLy = (RelativeLayout) findViewById(R.id.account_layout);
		accountLy.setOnClickListener(this);
		
		syncLy    = (LinearLayout) findViewById(R.id.sync_layout);
		syncLy.setOnClickListener(this);
		
		troubleLayout    = (LinearLayout) findViewById(R.id.trouble_layout);
		troubleLayout.setOnClickListener(this);
		
		problemLayout    = (LinearLayout) findViewById(R.id.problem_layout);
		problemLayout.setOnClickListener(this);
		
		fitmodelLayout    = (LinearLayout) findViewById(R.id.fit_model_layout);
		fitmodelLayout.setOnClickListener(this);
		
		updateVersionLayout    = (LinearLayout) findViewById(R.id.updateVersion_layout);
		updateVersionLayout.setOnClickListener(this);
		
		login_bar = (ProgressBar)findViewById(R.id.login_bar);
		
		appVersionText = (TextView) findViewById(R.id.app_version_text);
		firmVersionText = (TextView) findViewById(R.id.firm_version_text);
		
		
		PackageManager manager;
		PackageInfo info = null;
		manager = this.getPackageManager();

		try {

		info = manager.getPackageInfo(this.getPackageName(), 0);
		appVersionText.setText(info.versionName);
		
		if(sp.getInt(Constant.FIRMWARE_VERSION, 0)!=0){
			firmVersionText.setText(Integer.toString(sp.getInt(Constant.FIRMWARE_VERSION, 0)));	
		}
		
		} catch (NameNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		Intent intent;
		switch(view.getId()){
		case R.id.left_Btn:
			finish();
			break;
		case R.id.account_layout:
//			startActivity(new Intent(SettingActivity.this,LoginActivity.class));
			startActivityForResult(new Intent(SettingActivity.this,LoginActivity.class), 1);
			break;
		case R.id.trouble_layout:
			break;
		case R.id.problem_layout:
			bundle.putString("webline", "http://www.sense-art.cn/jd/faq.asp");
			intent = new Intent(SettingActivity.this, WebViewActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.fit_model_layout:
//			bundle.putString("webline", "http://www.lepao.com/adaptation-mobile.html");
//			intent = new Intent(SettingActivity.this, WebViewActivity.class);
//			intent.putExtras(bundle);
//			startActivity(intent);			
			break;
		case R.id.updateVersion_layout:
			startActivity(new Intent(SettingActivity.this, VersionUpdateActivity.class));
			break;
		case R.id.exit_btn:
//			PlusDotBLEDevice plusdotDevice = PlusDotBLEDevice.getInstance();
//			plusdotDevice.UpgradeCommand();
			login_bar.setVisibility(View.VISIBLE);
			
			Editor editor = sp.edit();
			editor.putBoolean(Constant.LOGIN_FALG, false);
			editor.commit();
			
			loginName.setText("");
			accountLy.setClickable(true);
//			exitBtn.setVisibility(View.GONE);
			
			login_bar.setVisibility(View.GONE);
			exitBtn.setBackground(getResources().getDrawable(R.drawable.person_info_btn_b));
			exitBtn.setClickable(false);
			
//			new Handler().postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					login_bar.setVisibility(View.GONE);
//					exitBtn.setBackground(getResources().getDrawable(R.drawable.person_info_btn_b));
//					exitBtn.setClickable(false);
//				}
//			}, 3000);
			
			break;
		case R.id.test_btn:
			
			if(isApp.getConnectBleDevice()){
				PlusDotBLEDevice plusdotDevice = PlusDotBLEDevice.getInstance();
				plusdotDevice.OTACommand(true);
			}
			
			final Intent service = new Intent(this, DfuService.class);
			BindDeviceDao dao = new BindDeviceDao(this);
			List<Map<String, String>> daoList = dao.listBleDevice(null);
        	
			service.putExtra(DfuService.EXTRA_DEVICE_ADDRESS, daoList.get(0).get("mac_address"));
//			Log.d("TAG", "device's address ================ "+daoList.get(0).get("mac_address"));
			
			service.putExtra(DfuService.EXTRA_DEVICE_NAME, "DfuTarg");
//			Log.d("TAG", "device's name ================ "+daoList.get(0).get("device_name"));
			
			service.putExtra(DfuService.EXTRA_FILE_MIME_TYPE,  DfuService.MIME_TYPE_HEX);
//			Log.d("TAG", "file type ================ "+DfuService.MIME_TYPE_HEX);
			
			service.putExtra(DfuService.EXTRA_FILE_TYPE, DfuService.MIME_TYPE_HEX);
			service.putExtra(DfuService.EXTRA_FILE_PATH, "/storage/sdcard0/"+sp.getString("firmName", "plusdot.hex"));
			
//			Log.d("TAG", "file type ================ "+"/storage/sdcard0/"+sp.getString("firmName", "plusdot.hex"));
//			service.putExtra(DfuService.EXTRA_FILE_URI, mFileStreamUri);
			startService(service);
			
			break;
			
		case R.id.test1_btn:
			if(isApp.getConnectBleDevice()){
				PlusDotBLEDevice plusdotDevice = PlusDotBLEDevice.getInstance();
				plusdotDevice.synchronizationPhoneRemindEvent(true);
				testMode = true;
			}
			break;
		case R.id.test2_btn:
			if(isApp.getConnectBleDevice()){
				PlusDotBLEDevice plusdotDevice = PlusDotBLEDevice.getInstance();
				plusdotDevice.synchronizationPhoneRemindEvent(true);
				testMode = false;
			}
			break;
		}
	}
	
	
	public static boolean testMode=false;
	
	
	///////////////////////////////////////////
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(resultCode){
		case 1:
			exitBtn.setBackground(getResources().getDrawable(R.drawable.person_info_btn));
			exitBtn.setClickable(true);
			accountLy.setClickable(false);
			break;
		case 2:
			break;
		case MainActivity.MSG_BLE_CONNECTED:
			
			break;
		}
	}
	
	public final static int MSG_BINE_UNBLE_SUCCESS = 4;
	
	public Handler uihandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MainActivity.MSG_BLE_CONNECTED:
				Log.d("TAG","setting 's connect........................");
				break;
			case MainActivity.MSG_BLE_DISCONNECTED:
				Log.d("TAG","setting 's disconnect........................");
				break;
			}
		}
    };
	
	class UIStatusChangedCallback implements StatusChangedCallback{
		@Override
		public void callbackCall(int command, byte[] dataPacket) {
			// TODO Auto-generated method stub
			Log.d("TAG", "BindWatchFragment 's callbackCall..................."+command);
			
			Message msg = uihandler.obtainMessage(command); 
			uihandler.sendMessage(msg);
		}

		@Override
		public void callbackFail(int command, byte[] dataPacket) {
			// TODO Auto-generated method stub
			Log.d("TAG", "BindWatchFragment 's callbackFail..................."+command);
			Message msg = uihandler.obtainMessage(command); 
			uihandler.sendMessage(msg);
		}
	}
	
	
	/////////////////////////////// check the firm version ///////////////////////////////
	
	private void checkUpdateVersion(){
		RemoteUserService.CheckVersion(new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(Throwable error, String content) {
//				Log.e("checkVersionUpdate==>fail", error.getMessage());
//				webView.loadUrl("http://www.youduoyun.com/api/firmwares/version?device_id=180");
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				try {
					int resultflag = new JSONObject(content).getInt("success");
					if (resultflag==1) {
						
						JSONObject data = new JSONObject(content).getJSONObject("data");
//						String name = data.getString("name");
						int    verCode = data.getInt("version");
//						String verName = data.getString("desc");
//						String url     = data.getString("url");
//						Log.d("TAG", "name === "+name+"  verCode === "+verCode+" verName === "+verName+" url ========="+url);
//						
//						String urlStr = url;
//						String[] strs = urlStr.split("[/]");
//						Log.d("TAG", "strs ================ "+strs[5]);
						

						versionHint.setVisibility(View.VISIBLE);
						versionText.setVisibility(View.GONE);
						updateVersionLayout.setClickable(true);
						
//						if(sp.getInt(Constant.FIRMWARE_VERSION, 0) < verCode){
//							versionHint.setVisibility(View.VISIBLE);
//							versionText.setVisibility(View.GONE);
//							updateVersionLayout.setClickable(true);
//						}else{
//							versionHint.setVisibility(View.GONE);
//							versionText.setVisibility(View.VISIBLE);
//							updateVersionLayout.setClickable(false);
//						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
}
