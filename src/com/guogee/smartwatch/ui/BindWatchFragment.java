package com.guogee.smartwatch.ui;

import java.util.List;
import java.util.Map;

import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.ble.PlusDotBLEDevice;
import com.guogee.smartwatch.ble.StatusChangedCallback;
import com.guogee.smartwatch.dao.BindDeviceDao;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.widget.CustomProgressDialog;

import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BindWatchFragment extends Fragment implements OnClickListener{

	private iSmartApplication isApp;
	private List<Map<String, String>> bindWatch;
	private RelativeLayout bindWatchLy;
	private ImageView bindIcon;
	private BindDeviceDao dao;
//	private int watchSize = 0;
	
	public static int watchSize = 0;
	
	private Handler mainHandle;
	private ImageButton backBtn;
	
	private PlusDotBLEDevice plusdotDevice;
	
	private ImageView plusdotOne;
	private ImageView plusdotTwo;
	
	private SharedPreferences sp;
	
	private TextView bindtx;
	private TextView bindtx2;
	
	public void setMainHanlder(Handler handler){
		mainHandle = handler;
	}
	
	public static BindWatchFragment instantiation(int position) {
		BindWatchFragment fragment = new BindWatchFragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isApp = (iSmartApplication)getActivity().getApplication();	
		
		dao = new BindDeviceDao(getActivity());
		bindWatch = dao.listBleDevice(null);
		
		plusdotDevice = PlusDotBLEDevice.getInstance();
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
//		isApp.addCallBack(new UIStatusChangedCallback());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//bug --- null point
//		ViewGroup root = (ViewGroup) inflater.inflate(bindWatch.size() > 0 ?
//				R.layout.binded_watched_layout  : R.layout.bind_watch_layout, container, false);
		watchSize = bindWatch.size();
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.binded_watched_layout , container, false);
		bindWatchLy = (RelativeLayout) root.findViewById(R.id.bind_watch_layout);
		bindWatchLy.setOnClickListener(this);
		
		bindIcon = (ImageView) root.findViewById(R.id.bind_icon);
		bindIcon.setVisibility(watchSize > 0 ? View.VISIBLE : View.INVISIBLE);
		
		backBtn = (ImageButton) root.findViewById(R.id.left_Btn);
		backBtn.setOnClickListener(this);
		
		plusdotOne = (ImageView) root.findViewById(R.id.plusdot_one);
		plusdotOne.setOnClickListener(this);
		
//		plusdotTwo = (ImageView) root.findViewById(R.id.plusdot_two);
//		plusdotTwo.setOnClickListener(this);
		
		bindtx = (TextView) root.findViewById(R.id.bind_tx);
//		bindtx2 = (TextView) root.findViewById(R.id.bind_tx2);
		
		return root;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.d("TAG","BindWatchFragment onResume ..........................");
		
//		isApp.addCallBack(new UIStatusChangedCallback());
		
		//check the ble whether is bind or not.
		bindWatch = dao.listBleDevice(null);
		watchSize = bindWatch.size();
		bindIcon.setVisibility(watchSize > 0 ? View.VISIBLE : View.INVISIBLE);

		if(sp.getInt(Constant.PLUSDOT_TYPE, 1)==1){
			bindtx.setText(getResources().getString(watchSize > 0 ? R.string.smart_watch_p1_paired : R.string.smart_watch_p1));
			bindtx.setTextColor(getResources().getColor(watchSize > 0 ? android.R.color.holo_red_light : R.color.text_color));	
		}
//		else if(sp.getInt(Constant.PLUSDOT_TYPE, 1)==2){
//			bindtx2.setText(getResources().getString(watchSize > 0 ? R.string.smart_watch_p2_paired : R.string.smart_watch_p2));
//			bindtx2.setTextColor(getResources().getColor(watchSize > 0 ? android.R.color.holo_red_light : R.color.text_color));
//		}
		
		if(watchSize>0){
			isApp.setBindWatch(bindWatch);
			//bind service.
	        Message msg = mainHandle.obtainMessage(MainActivity.MSG_CONNECT_BLE_SERVICE); 
            mainHandle.sendMessage(msg);
		}
		
		else{
	        Message msg = mainHandle.obtainMessage(MainActivity.MSG_DISCONNECT); 
            mainHandle.sendMessage(msg);			
		}
		
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		final int viewId = view.getId(); 
		final Editor editor = sp.edit();
		
		switch(view.getId()){
		case R.id.bind_watch_layout:
		case R.id.plusdot_one:
//		case R.id.plusdot_two:
			if(watchSize > 0){
				
				
				
				if(isApp.getConnectBleDevice()){
					
					new AlertDialog.Builder(getActivity())
					.setTitle(getResources().getString(R.string.app_name))  
					.setMessage(getActivity().getString(R.string.cancle_bind_watch))  
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface d, int w) {
							// TODO Auto-generated method stub
							
							plusdotDevice.unBindCommand();	
							
							bindWatch = dao.listBleDevice(null);
							String id = bindWatch.get(0).get("id");
							Object[] params = new Object[] {id};
							dao.deleteBleDevice(params);// delete database
							bindIcon.setVisibility(View.GONE);
							watchSize = 0;
							
//							if(viewId == R.id.plusdot_one){
//								editor.putInt(Constant.PLUSDOT_TYPE, 1).commit();
//								bindtx.setText(getResources().getString(R.string.smart_watch_p1));	
//								bindtx.setTextColor(getResources().getColor(R.color.text_color));
//							}else if(viewId == R.id.plusdot_two){
//								editor.putInt(Constant.PLUSDOT_TYPE, 2).commit();
//								bindtx2.setText(getResources().getString(R.string.smart_watch_p2));
//								bindtx2.setTextColor(getResources().getColor(R.color.text_color));
//							}
							
							bindtx.setText(getResources().getString(R.string.smart_watch_p1));
							bindtx.setTextColor(getResources().getColor(R.color.text_color));
							
//							bindtx2.setText(getResources().getString(R.string.smart_watch_p2));
//							bindtx2.setTextColor(getResources().getColor(R.color.text_color));
							
							List<Map<String, String>> daoList = dao.listBleDevice(null);
							isApp.setBindWatch(daoList);
							
							
//							new Handler().postDelayed(new Runnable() {
//								@Override
//								public void run() {
//					                Message msg = mainHandle.obtainMessage(MainActivity.MSG_DISCONNECT); 
//					                mainHandle.sendMessage(msg);
//								}
//							}, 1000);
							
							
							isApp.SendBindCommand(false);
						}
					})  
					.setNegativeButton(android.R.string.no,null)  
					.show();
					
				}else{
				
					new AlertDialog.Builder(getActivity())
					.setTitle(getResources().getString(R.string.app_name))  
					.setMessage(getActivity().getString(R.string.unbind_tip))  
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface d, int w) {
							// TODO Auto-generated method stub
							
//							plusdotDevice.unBindCommand();	
							
							bindWatch = dao.listBleDevice(null);
							String id = bindWatch.get(0).get("id");
							Object[] params = new Object[] {id};
							dao.deleteBleDevice(params);// delete database
							bindIcon.setVisibility(View.GONE);
							watchSize = 0;
							
							bindtx.setText(getResources().getString(R.string.smart_watch_p1));
							bindtx.setTextColor(getResources().getColor(R.color.text_color));
							
							List<Map<String, String>> daoList = dao.listBleDevice(null);
							isApp.setBindWatch(daoList);
							
			                Message msg = mainHandle.obtainMessage(MainActivity.MSG_DISCONNECT); 
			                mainHandle.sendMessage(msg);
							
						}
					})  
					.setNegativeButton(android.R.string.no,null)  
					.show();
				}
				
			}else{
//				startActivity(new Intent(getActivity(), BindingScanActivity.class));	
				Intent intent = new Intent(getActivity(), BindingScanActivity.class);
//				Intent intent = new Intent(getActivity(), BindingListScanActivity.class);
				Bundle bundle = new Bundle();
				if(viewId == R.id.plusdot_one){
					bundle.putInt("plusdot_type", 1);
				}
//				else if(viewId == R.id.plusdot_two){
//					bundle.putInt("plusdot_type", 2);	
//				}
				intent.putExtras(bundle);
				startActivityForResult(intent, 0);
				isApp.SendBindCommand(true);
			}
			break;
		case R.id.left_Btn:
			isApp.getSlidingMenu().showMenu();
			break;
		}
	}	
	
	private String deviceName ;
	private String deviceMac;
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==1){
			
//			showProgressDialog();
//			Bundle extraData = data.getExtras();
//			if(extraData!=null){
//				deviceName = extraData.getString("DeviceName");
//				deviceMac  = extraData.getString("DeviceMac");
//				
//			    Message msg = mainHandle.obtainMessage(MainActivity.MSG_BIND_BLE); 
//			    msg.setData(extraData);
//		        mainHandle.sendMessage(msg);
//			}
			
			
//			isApp.getSlidingMenu().showMenu();
//			isApp.getSlidingMenu().toggle();
			
//			isApp.setLeftMenuTag(Constant.SPORT_TAG);
//			isApp.getSlidingMenu().toggle();
			
			isApp.getSlidingMenu().showMenu();
			
//			Message msg = isApp.getLeftMenuHandler().obtainMessage(22); 
//			isApp.getLeftMenuHandler().sendMessage(msg);
		
		
		}else{
		
			
			
		}
	}

	/////////////////////////////////////////////////////////////////////////
	public final static int MSG_BINE_BLE_SUCCESS = 2;
	
	public final static int MSG_BIND_BLE_FAILURE = 3;
	
	public final static int MSG_BINE_UNBLE_SUCCESS = 4;
	
	public boolean isBind = false;
	
	public Handler uihandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MainActivity.MSG_BLE_CONNECTED:
				Log.d("TAG", "BindWatchFragment 's sent bind command .............. ");
//				plusdotDevice.BindCommand();
				showProgressDialog2();
				disappearSyncProgressDialog();
				
				//开启线程，10秒内，没确认命令返回，应断开蓝牙连接
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if(!isBind){
							bindIcon.setVisibility(View.GONE);
			                Message msg = mainHandle.obtainMessage(MainActivity.MSG_DISCONNECT); 
			                mainHandle.sendMessage(msg);
						}
					}
				}, 60000);
				
				break;
			case MainActivity.MSG_BLE_DISCONNECTED:
				disappearSyncProgressDialog2();
				break;
			case MSG_BINE_BLE_SUCCESS:
				isBind = true;
				disappearSyncProgressDialog2();
				
				BindDeviceDao dao = new BindDeviceDao(getActivity());
				Object[] params = new Object[] { deviceName, deviceMac, 0, 0 };
				dao.addBleDevice(params);
				
				bindIcon.setVisibility(View.VISIBLE);
				watchSize = 1;
				break;
			case MSG_BIND_BLE_FAILURE:
				break;
			case 10:
//				isApp.getSlidingMenu().showContent(true);
				break;

			}
		}
    };
	
	class UIStatusChangedCallback implements StatusChangedCallback{
		@Override
		public void callbackCall(int command, byte[] dataPacket) {
			// TODO Auto-generated method stub
//			Log.d("TAG", "BindWatchFragment 's callbackCall..................."+command);
//			Message msg = uihandler.obtainMessage(command); 
//			uihandler.sendMessage(msg);
		}

		@Override
		public void callbackFail(int command, byte[] dataPacket) {
			// TODO Auto-generated method stub
//			Log.d("TAG", "BindWatchFragment 's callbackFail..................."+command);
//			Message msg = uihandler.obtainMessage(command); 
//			uihandler.sendMessage(msg);
		}
	}	
	
	
	////////////////////////////////
	private CustomProgressDialog progressDialog = null;
	
	private CustomProgressDialog progressDialog2 = null;
	
	public void showProgressDialog(){
//		progressDialog = ProgressDialog.show(getActivity(), "正在同步", "请稍等", true, false);
		if(progressDialog == null){
			progressDialog = CustomProgressDialog.createDialog(getActivity());
	    	progressDialog.setMessage(getResources().getString(R.string.connecting));
	    	progressDialog.show();
		}
	}
	
	public void disappearSyncProgressDialog(){
		if(progressDialog!=null){
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	
	public void showProgressDialog2(){
//		progressDialog = ProgressDialog.show(getActivity(), "正在同步", "请稍等", true, false);
		if(progressDialog2 == null){
			progressDialog2 = CustomProgressDialog.createDialog(getActivity());
			progressDialog2.setMessage(getActivity().getResources().getString(R.string.bind_watch_tip));
			progressDialog2.show();
		}
	}
	
	public void disappearSyncProgressDialog2(){
		if(progressDialog2!=null){
			progressDialog2.dismiss();
			progressDialog2 = null;
		}
	}
	
}
