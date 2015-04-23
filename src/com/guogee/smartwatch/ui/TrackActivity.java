package com.guogee.smartwatch.ui;

import java.util.ArrayList;

import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.ble.PlusDotBLEDevice;
import com.guogee.smartwatch.ble.StatusChangedCallback;
import com.guogee.smartwatch.ui.ExerciseActivity.UIStatusChangedCallback;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.utils.Log;
import com.guogee.smartwatch.widget.NumericWheelAdapter;
import com.guogee.smartwatch.widget.Switch;
import com.guogee.smartwatch.widget.WheelView;
import com.guogee.smartwatch.widget.Switch.OnCheckedChangeListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TrackActivity extends Activity implements OnClickListener,OnCheckedChangeListener{

	private ImageButton backBtn;
	private TextView synText;
	private iSmartApplication isApp;
	private PlusDotBLEDevice plusdotBleDevice;
	private SharedPreferences sp;
	private Switch trackerBtn;
	private RelativeLayout intervalLayout;
	private TextView intervalText;
	
	private boolean isChange = false;
	
	private void setChange(){
		isChange = true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);  
	    setContentView(R.layout.track_layout);  
	    
	    isApp = (iSmartApplication)getApplication(); 
	    plusdotBleDevice = PlusDotBLEDevice.getInstance();
	    sp = PreferenceManager.getDefaultSharedPreferences(this);
	    
	    initView();
	    
	    isApp.addCallBack(new UIStatusChangedCallback());
	}
	
	private void initView(){
		backBtn = (ImageButton) findViewById(R.id.left_Btn);
		backBtn.setOnClickListener(this);
		
		synText = (TextView) findViewById(R.id.syn_text);
		synText.setOnClickListener(this);

		intervalText = (TextView) findViewById(R.id.interval_text);
		intervalText.setOnClickListener(this);
		intervalText.setText(sp.getString(Constant.TRACKER_INTERVAL, "20")+"s");
		Log.d("TAG", "interval 's ================ "+sp.getString(Constant.TRACKER_INTERVAL, "20"));
		
		trackerBtn = (Switch) findViewById(R.id.tracker_btn);
		trackerBtn.setOnCheckedChangeListener(this);
		trackerBtn.setChecked(sp.getBoolean(Constant.TRACKER_FLAG, false));
		
		intervalLayout = (RelativeLayout) findViewById(R.id.interval_layout);
		intervalLayout.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.left_Btn:
			
			if(isApp.getConnectBleDevice() && isChange){
				showConfirmDialog();
			}else{
				finish();
			}
			
			break;
		case R.id.syn_text:
			if(isApp.getConnectBleDevice()){
				plusdotBleDevice.synchronizationTracker(true);	
			}
			break;
		case R.id.interval_layout:
			showPopupWindow(false,sp.getInt(Constant.TRACKER_TIME_FLAG, 20));
			break;
		}
	}

	private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.sync_tip))
                .setTitle(getResources().getString(R.string.notification_warning))
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            	plusdotBleDevice.synchronizationTracker(true);	
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
                            	
                            	finish();
                            }
                        })
                .create().show();
    }
	@Override
	public void onCheckedChanged(Switch switchView, boolean isChecked) {
		// TODO Auto-generated method stub
		Editor editor = sp.edit();
		switch(switchView.getId()){
		case R.id.tracker_btn:
			if(isApp.getConnectBleDevice()){
				editor.putBoolean(Constant.TRACKER_FLAG, isChecked);
				editor.commit();
				
				setChange();
				
//				plusdotBleDevice = PlusDotBLEDevice.getInstance();
//				plusdotBleDevice.synchronizationTracker(true);
			}else {
				trackerBtn.setChecked(!isChecked);
				Toast.makeText(TrackActivity.this, getResources().getString(R.string.disconnect), Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	//////////////////////////
	private int[] intervalTimes = {5,10,20,35};
	
	private PopupWindow popWindow;
	
	private void showPopupWindow(final boolean isstartTime,int intervalTime) {
		if (popWindow == null) {
			LayoutInflater layoutIn = (LayoutInflater) getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutIn.inflate(R.layout.popup_wheel, null);
			popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

			final WheelView time = (WheelView) view.findViewById(R.id.hour);
			final ArrayList<Integer> arrayData = new ArrayList<Integer>();
			
			for(int interval:intervalTimes){
				arrayData.add(interval);
			}
			
			time.setAdapter(new NumericWheelAdapter(arrayData));
			Button saveBtn = (Button) view.findViewById(R.id.save_btn);
			saveBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					popWindow.dismiss();
					popWindow = null;
					intervalText.setText(Integer.toString(arrayData.get(time.getCurrentItem()))+"s");

					sp.edit().putString(Constant.TRACKER_INTERVAL, Integer.toString(arrayData.get(time.getCurrentItem()))).commit();
					sp.edit().putInt(Constant.TRACKER_TIME_FLAG, arrayData.get(time.getCurrentItem())).commit();
					
					setChange();
				}
			});

			for(int index = 0; index < intervalTimes.length; index++){
				if(intervalTimes[index] == intervalTime){
					time.setCurrentItem(index);
				}
			}
			
			popWindow.setBackgroundDrawable(new BitmapDrawable());
			popWindow.setAnimationStyle(R.style.popuStyle);
			popWindow.setOutsideTouchable(true);
		}

		popWindow.showAtLocation(findViewById(R.id.track_main_layout),
				Gravity.BOTTOM, 0, 0);
	}
	
	//////////////////////
	public Handler uihandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 11:
				Toast toast= Toast.makeText(TrackActivity.this, getResources().getString(R.string.sync_success), Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,toast.getYOffset() / 2);
				toast.show();
				
				finish();
				break;
			case MainActivity.MSG_BLE_DISCONNECTED:
				Toast toast2= Toast.makeText(TrackActivity.this, getResources().getString(R.string.sync_error), Toast.LENGTH_SHORT);
				toast2.setGravity(Gravity.CENTER, toast2.getXOffset() / 2,toast2.getYOffset() / 2);
				toast2.show();				
				break;
			}
		}
    };

	class UIStatusChangedCallback implements StatusChangedCallback{
		@Override
		public void callbackCall(int command, byte[] dataPacket) {
			// TODO Auto-generated method stub
			Message msg = uihandler.obtainMessage(command); 
			uihandler.sendMessage(msg);
		}

		@Override
		public void callbackFail(int command, byte[] dataPacket) {
			// TODO Auto-generated method stub
			Message msg = uihandler.obtainMessage(command); 
			uihandler.sendMessage(msg);
		}
	}
	
}
