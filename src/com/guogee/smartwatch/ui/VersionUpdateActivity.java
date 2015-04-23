package com.guogee.smartwatch.ui;

import java.io.File;
import java.util.List;
import java.util.Map;

import no.nordicsemi.android.error.GattError;
import no.nordicsemi.android.nrftoolbox.dfu.DfuService;

import org.json.JSONException;
import org.json.JSONObject;

import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.ble.PlusDotBLEDevice;
import com.guogee.smartwatch.ble.StatusChangedCallback;
import com.guogee.smartwatch.dao.BindDeviceDao;
import com.guogee.smartwatch.playcamera.CameraActivity;
import com.guogee.smartwatch.service.RemoteUserService;
import com.guogee.smartwatch.widget.NetworkHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class VersionUpdateActivity extends Activity implements OnClickListener{
	private ImageButton backBtn;
	private WebView webView;
	private Button downloadButton;
	
//	private Button updateBtn;
	private iSmartApplication isapp;
	private SharedPreferences prefs;
	private DownloadManager downloadManager;
	public static final String DL_ID = "downloadId";  
	
	private TextView errorTip;
	private SharedPreferences sp;
	
	private TextView mTextPercentage;
	private ProgressBar mProgressBar;
	private TextView mTextUploading;
	
	private final BroadcastReceiver mDfuUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			// DFU is in progress or an error occurred 
			final String action = intent.getAction();

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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.version_update_layout);
		
		isapp 			= (iSmartApplication) getApplication();
		
		prefs 			= getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		if(isapp.getDownloadManager() != null ){
			downloadManager = isapp.getDownloadManager();
		}else{
			downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
		}
		
		backBtn 		= (ImageButton)findViewById(R.id.backBtn);
		downloadButton 	= (Button)findViewById(R.id.downloadBtn);
		
//		updateBtn = (Button) findViewById(R.id.updateBtn);
//		updateBtn.setOnClickListener(this);
//		updateBtn.setVisibility(View.GONE);
		
		errorTip = (TextView) findViewById(R.id.error_tip);
		
		mTextPercentage = (TextView) findViewById(R.id.textviewProgress);
		mProgressBar = (ProgressBar) findViewById(R.id.progressbar_file);
		
		mTextUploading = (TextView) findViewById(R.id.textviewUploading);
		
		
		webView 		= (WebView)findViewById(R.id.webView);
		  //设置WebView属性，能够执行Javascript脚本  
//		webView.getSettings().setJavaScriptEnabled(true);  
		
//		if(!isapp.newVersion){
//			downloadButton.setVisibility(View.GONE);
//		}else if (prefs.contains(DL_ID)){
			queryDownloadStatus();
//		}
		
		downloadButton.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		
		isapp.addCallBack(new UIStatusChangedCallback());
		
        //download service.
        IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(receiver, filter);
		
		checkUpdateVersion();
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
//		if(prefs.contains("updateDescrptionUrl")){
//			webView.loadUrl(prefs.getString("updateDescrptionUrl", "http://datastore.guogee.com/"));
//		}else{
//			checkUpdateVersion();
//		}
	
		// We are using LocalBroadcastReceiver instead of normal BroadcastReceiver for optimization purposes
		final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
		broadcastManager.registerReceiver(mDfuUpdateReceiver, makeDfuUpdateIntentFilter());
		
//		checkUpdateVersion();
	}
	
	private static IntentFilter makeDfuUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(DfuService.BROADCAST_PROGRESS);
		intentFilter.addAction(DfuService.BROADCAST_ERROR);
		intentFilter.addAction(DfuService.BROADCAST_LOG);
		return intentFilter;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	
		final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
		broadcastManager.unregisterReceiver(mDfuUpdateReceiver);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backBtn:
			finish();
			break;
		case R.id.downloadBtn:
			if(isapp.getConnectBleDevice()){
				if(NetworkHelper.checkNetWork(isapp.getApplicationContext()) == 0 ){
					Toast.makeText(getBaseContext(), R.string.net_err, Toast.LENGTH_LONG).show();
					return;
				} 
				
				downloadManager();
				Toast.makeText(getBaseContext(), R.string.downloading, Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(VersionUpdateActivity.this, getResources().getString(R.string.disconnect), Toast.LENGTH_SHORT).show();
			}
			
			break;
//		case R.id.updateBtn:
//			if(isapp.getConnectBleDevice()){
//				PlusDotBLEDevice plusdotDevice = PlusDotBLEDevice.getInstance();
//				plusdotDevice.OTACommand(true);
//			}
//			break;
		default:
			break;
		}
	}
	
	private void queryDownloadStatus(){
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(prefs.getLong(DL_ID, 0));
		Cursor cursor = downloadManager.query(query);
		if(cursor.moveToFirst()){
			int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)); 
			switch (status) {
			case DownloadManager.STATUS_RUNNING:  //正在下载
				Log.d("TAG", "STATUS_RUNNING........");  
//				downloadButton.setVisibility(View.GONE);
				break;
			 case DownloadManager.STATUS_FAILED:	
				 Log.d("TAG", "STATUS_FAILED........"); 
				 downloadManager.remove(prefs.getLong(DL_ID, 0));
				 prefs.edit().remove(DL_ID).commit();
				 downloadButton.setVisibility(View.VISIBLE);
				 break;
			 case DownloadManager.STATUS_SUCCESSFUL:
				 Log.d("TAG", "STATUS_SUCCESSFUL.......");
				 downloadButton.setVisibility(View.VISIBLE);
				 break;
			default:
				break;
			}
		}
	}
	
	private void checkUpdateVersion(){
		RemoteUserService.CheckVersion(new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(Throwable error, String content) {
				Log.e("checkVersionUpdate==>fail", error.getMessage());
//				webView.loadUrl("http://www.youduoyun.com/api/firmwares/version?device_id=180");
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				try {
					int resultflag = new JSONObject(content).getInt("success");
					if (resultflag==1) {
						iSmartApplication isapp = (iSmartApplication) getApplication();
//						PackageManager manager = getApplication().getPackageManager();
//						PackageInfo info = manager.getPackageInfo(getApplication().getPackageName(), 0);
						SharedPreferences mPreferences = getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
						
//						JSONObject data = new JSONArray(content).getJSONObject(1);
						
						JSONObject data = new JSONObject(content).getJSONObject("data");
						String name = data.getString("name");
						int    verCode = data.getInt("version");
						String desc = data.getString("desc");
						String url     = data.getString("url");
						Log.d("TAG", "name === "+name+"  verCode === "+verCode+" desc === "+desc+" url ========="+url);
						
						isapp.setFirmVersion(verCode);
						
						
//						errorTip.setText(desc);
						
						String urlStr = url;
						String[] strs = urlStr.split("[/]");
						Log.d("TAG", "strs ================ "+strs[5]);
						
						
						Editor editor = mPreferences.edit();
//						editor.putString("name", name);
						editor.putString("firmName", strs[5]);
						editor.putString("url", url);
//						editor.putString("verName", verName);
//						editor.putString("updateDescrptionUrl", updateDescrptionUrl);
//						editor.putInt("verCode", verCode);
						editor.commit();//提交修改		
						
				/**		
						JSONArray jsonArray = new JSONObject(content).getJSONArray("data");
						//只有一个
						for(int i = 0; i < jsonArray.length(); i++){		
							
							JSONObject returnval = jsonArray.getJSONObject(i);
							String name = returnval.getString("name");
							int    verCode = returnval.getInt("version");
							String verName = returnval.getString("desc");
							String url     = returnval.getString("url");
							
							Log.d("TAG", "name === "+name+"  verCode === "+verCode+" verName === "+verName);
							
//							String updateDescrptionUrl = returnval.getString("updateDescrptionUrl");

							Editor editor = mPreferences.edit();
							editor.putString("name", name);
//							editor.putString("appname", appname);
							editor.putString("url", url);
//							editor.putString("verName", verName);
//							editor.putString("updateDescrptionUrl", updateDescrptionUrl);
//							editor.putInt("verCode", verCode);
							editor.commit();//提交修改		
							
//							if(updateDescrptionUrl != null){
//								webView.loadUrl(updateDescrptionUrl);
//							}else{
//								webView.loadUrl("http://datastore.guogee.com/");
//							}
							
//							if(info.versionCode >= verCode){//不是最新版本
//								return ;
//							}
							isapp.newVersion = true;
							downloadButton.setVisibility(View.VISIBLE);
						}
						**/
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	private void downloadManager(){
		String url = prefs.getString("url", null);
//		Log.d("TAG", "downloadManager url =================== "+url);
		if(url != null){
			Uri uri = Uri.parse(url);
			DownloadManager.Request request = new DownloadManager.Request(uri);
			request.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);
			request.setAllowedOverRoaming(false);
			//设置文件类型
			MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
			String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
			request.setMimeType(mimeString);
			//在通知栏中显示
			request.setShowRunningNotification(true);
			request.setVisibleInDownloadsUi(true);
			//sdcard 
			String path = Environment.getExternalStorageDirectory().getPath() + "/SmartWatch/"+ prefs.getString("firmName", "plusdot.hex");
//			Log.d("TAG", "downloadManager path =================== "+path);
			
			File file = new File(path);
			if(file.exists()){
				file.delete();
			}
			request.setDestinationInExternalPublicDir("/SmartWatch/", prefs.getString("firmName", "plusdot.hex"));
			request.setTitle(prefs.getString("firmName", "plusdot.hex"));
			long id = downloadManager.enqueue(request);
			prefs.edit().putLong(DL_ID, id).commit();
			isapp.setDownloadManager(downloadManager);
			
//			updateBtn.setVisibility(View.VISIBLE);
			downloadButton.setVisibility(View.GONE);
			
//			if(isapp.getConnectBleDevice()){
//				PlusDotBLEDevice plusdotDevice = PlusDotBLEDevice.getInstance();
//				plusdotDevice.OTACommand(true);
//			}
			
		}
	}
	
	
	////////////////////////////////////////////
	
	public static final int MSG_ENTER_OTA = 2;
	
	
	public Handler uihandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ENTER_OTA:
//				Toast toast= Toast.makeText(VersionUpdateActivity.this, getResources().getString(R.string.sync_success), Toast.LENGTH_SHORT);
//				toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,toast.getYOffset() / 2);
//				toast.show();
//				finish();
				Log.d("TAG", "enter the OTA mode ............");
				break;
				
			case MainActivity.MSG_BLE_DISCONNECTED:
				if(isapp.getOTAMode()){
					//开始升级
					Log.d("TAG", "开始升级............");
				
					final Intent service = new Intent(VersionUpdateActivity.this, DfuService.class);
					BindDeviceDao dao = new BindDeviceDao(VersionUpdateActivity.this);
					List<Map<String, String>> daoList = dao.listBleDevice(null);
		        	
					service.putExtra(DfuService.EXTRA_DEVICE_ADDRESS, daoList.get(0).get("mac_address"));
//					Log.d("TAG", "device's address ================ "+daoList.get(0).get("mac_address"));
					
					service.putExtra(DfuService.EXTRA_DEVICE_NAME, "DfuTarg");
//					Log.d("TAG", "device's name ================ "+daoList.get(0).get("device_name"));
					
					service.putExtra(DfuService.EXTRA_FILE_MIME_TYPE,  DfuService.MIME_TYPE_HEX);
//					Log.d("TAG", "file type ================ "+DfuService.MIME_TYPE_HEX);
					service.putExtra(DfuService.EXTRA_FILE_TYPE, DfuService.MIME_TYPE_HEX);
					
					String path = Environment.getExternalStorageDirectory().getPath() + "/SmartWatch/"+ prefs.getString("firmName", "plusdot.hex");
					service.putExtra(DfuService.EXTRA_FILE_PATH, path);
//					Log.d("TAG", "file path ================ "+path);
					
					Uri mFileStreamUri = null;
					service.putExtra(DfuService.EXTRA_FILE_URI, mFileStreamUri);

					startService(service);
					
					isConenctedAgain = true;
				}
				break;
				
//			case MainActivity.MSG_BLE_DISCONNECTED:
//				Toast toast2= Toast.makeText(VersionUpdateActivity.this, getResources().getString(R.string.sync_error), Toast.LENGTH_SHORT);
//				toast2.setGravity(Gravity.CENTER, toast2.getXOffset() / 2,toast2.getYOffset() / 2);
//				toast2.show();				
//				break;
			}
		}
    };
	
    
    private boolean isConenctedAgain = false;
    
    
	class UIStatusChangedCallback implements StatusChangedCallback{
		@Override
		public void callbackCall(int command, byte[] dataPacket) {
			// TODO Auto-generated method stub
			Log.d("TAG", "VersionUpdateActivity 's callbackCall..................."+command);
			Message msg = uihandler.obtainMessage(command); 
			uihandler.sendMessage(msg);
		}

		@Override
		public void callbackFail(int command, byte[] dataPacket) {
			// TODO Auto-generated method stub
			Log.d("TAG", "VersionUpdateActivity 's callbackFail..................."+command);
			Message msg = uihandler.obtainMessage(command); 
			uihandler.sendMessage(msg);
		}
	}
	
	
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
            	DownloadManager downloadManager = isapp.getDownloadManager();
            	if(downloadManager == null) return ;
        		query.setFilterById(prefs.getLong(VersionUpdateActivity.DL_ID, 0));
        		Cursor cursor = downloadManager.query(query);
        		if(cursor.moveToFirst()){
        			int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)); 
        			switch (status) {
        			case DownloadManager.STATUS_RUNNING:  //正在下载
        				Log.d("TAG", "VersionUpdateActivity STATUS_RUNNING ...................... ");  
        				break;
        			 case DownloadManager.STATUS_SUCCESSFUL:
        				 Log.d("TAG", "VersionUpdateActivity STATUS_SUCCESSFUL ...................... ");
        				 
        				 if(isapp.getConnectBleDevice()){
        					PlusDotBLEDevice plusdotDevice = PlusDotBLEDevice.getInstance();
        					plusdotDevice.OTACommand(true);
        				 }
        				 
        				 break;
        			default:
        				break;
        			}
        		}
            }
    	}
    }   
};  




////////////////////////////////
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
		
		if(isConenctedAgain){
			final Intent service = new Intent(VersionUpdateActivity.this, DfuService.class);
			BindDeviceDao dao = new BindDeviceDao(VersionUpdateActivity.this);
			List<Map<String, String>> daoList = dao.listBleDevice(null);
	    	
			service.putExtra(DfuService.EXTRA_DEVICE_ADDRESS, daoList.get(0).get("mac_address"));
			Log.d("TAG", "device's address ================ "+daoList.get(0).get("mac_address"));
			
			service.putExtra(DfuService.EXTRA_DEVICE_NAME, "DfuTarg");
//			Log.d("TAG", "device's name ================ "+daoList.get(0).get("device_name"));
			
			service.putExtra(DfuService.EXTRA_FILE_MIME_TYPE,  DfuService.MIME_TYPE_HEX);
			Log.d("TAG", "file type ================ "+DfuService.MIME_TYPE_HEX);
			service.putExtra(DfuService.EXTRA_FILE_TYPE, DfuService.MIME_TYPE_HEX);
			
			String path = Environment.getExternalStorageDirectory().getPath() + "/SmartWatch/"+ prefs.getString("firmName", "plusdot.hex");
			service.putExtra(DfuService.EXTRA_FILE_PATH, path);
			Log.d("TAG", "file path ================ "+path);
			
			Uri mFileStreamUri = null;
			service.putExtra(DfuService.EXTRA_FILE_URI, mFileStreamUri);

			startService(service);
			isConenctedAgain = false;
		}
		break;
	case DfuService.PROGRESS_COMPLETED:
		mTextPercentage.setText(R.string.dfu_status_completed);
		// let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				onTransferCompleted();
				mProgressBar.setIndeterminate(false);
				
				// if this activity is still open and upload process was completed, cancel the notification
				final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				manager.cancel(DfuService.NOTIFICATION_ID);
				
				Log.e("TAG", "send MSG_BLE_DISCONNECTED one.............");
				
                Message msg = isapp.getMainHandler().obtainMessage(MainActivity.MSG_BLE_DFU_FINISH_CONNECT_BLE); 
                isapp.getMainHandler().sendMessage(msg);
                
                isapp.setOTAMode(false);
                
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
//	clearUI();
	showToast("Upload failed: " + GattError.parse(code) + " (" + (code & ~(DfuService.ERROR_MASK | DfuService.ERROR_REMOTE_MASK)) + ")");
}

public void onUploadCanceled() {
//	clearUI();
//	showToast("升级中断.....");
}

private void onTransferCompleted() {
//	clearUI();
//	showToast("升级成功.....");
}

private void showToast(String str) {
	Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
}


}
