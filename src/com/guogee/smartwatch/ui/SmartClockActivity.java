package com.guogee.smartwatch.ui;

import java.util.ArrayList;

import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.ble.PlusDotBLEDevice;
import com.guogee.smartwatch.ble.StatusChangedCallback;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.widget.NumericWheelAdapter;
import com.guogee.smartwatch.widget.Switch;
import com.guogee.smartwatch.widget.WheelView;
import com.guogee.smartwatch.widget.Switch.OnCheckedChangeListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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

/**
 * "智能闹钟" 对应的界面
 * 
 * @author john
 *
 */
public class SmartClockActivity extends Activity implements OnClickListener{
	
	private ImageButton leftBtn;
	private iSmartApplication isApp;
	
	private RelativeLayout startLy;
	private RelativeLayout intervalLy;
	
	private TextView startTimeStr;
	private TextView intervalTimeStr;
	private Switch swBtn;
	private SharedPreferences sp;
	
	public static int RESULT_CODE = 1;
	private PopupWindow smartTimeWindow;
	private PopupWindow intervalWindow;
	
	private Button day7;
	private Button day1;
	private Button day2;
	private Button day3;
	private Button day4;
	private Button day5;
	private Button day6;
	
	private boolean isSelectDay1 = false;
	private boolean isSelectDay2 = false;
	private boolean isSelectDay3 = false;
	private boolean isSelectDay4 = false;
	private boolean isSelectDay5 = false;
	private boolean isSelectDay6 = false;
	private boolean isSelectDay7 = false;
	
	private PlusDotBLEDevice plusdotBLEDevice;
	private TextView syncText;
	
    private boolean isChange = false;
	
	private void setChange(){
		isChange = true;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.smart_clock_layout);
		
		isApp = (iSmartApplication)getApplication();
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		plusdotBLEDevice = PlusDotBLEDevice.getInstance();
		
		isSelectDay1 = sp.getBoolean(Constant.SMART_CLOCK_DAY1, false);
		isSelectDay2 = sp.getBoolean(Constant.SMART_CLOCK_DAY2, false);
		isSelectDay3 = sp.getBoolean(Constant.SMART_CLOCK_DAY3, false);
		isSelectDay4 = sp.getBoolean(Constant.SMART_CLOCK_DAY4, false);
		isSelectDay5 = sp.getBoolean(Constant.SMART_CLOCK_DAY5, false);
		isSelectDay6 = sp.getBoolean(Constant.SMART_CLOCK_DAY6, false);
		isSelectDay7 = sp.getBoolean(Constant.SMART_CLOCK_DAY7, false);
		
		initView();
		isApp.addCallBack(new UIStatusChangedCallback());
	}
	
	private void initView(){
		
		leftBtn = (ImageButton) findViewById(R.id.left_Btn);
		leftBtn.setOnClickListener(this);
		
		startLy = (RelativeLayout) findViewById(R.id.start_layout);
		startLy.setOnClickListener(this);

		intervalLy = (RelativeLayout) findViewById(R.id.interval_layout);
		intervalLy.setOnClickListener(this);
		
		syncText = (TextView) findViewById(R.id.syn_Btn);
		syncText.setOnClickListener(this);
		
		swBtn = (Switch) findViewById(R.id.switch_clock_Btn);
		swBtn.setChecked(sp.getBoolean(Constant.SLEEP_REMINDER_HINT, false));
		swBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(Switch switchView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isApp.getConnectBleDevice()){
					sp.edit().putBoolean(Constant.SLEEP_REMINDER_HINT, isChecked).commit();
					setChange();
				}else{
					swBtn.setChecked(!isChecked);
				}
			}
		});

		day1 = (Button) findViewById(R.id.day1);
		day1.setOnClickListener(this);
		day1.setBackgroundResource(isSelectDay1 ? R.drawable.day_background_b : R.drawable.day_background_a);
		
		day2 = (Button) findViewById(R.id.day2);
		day2.setOnClickListener(this);
		day2.setBackgroundResource(isSelectDay2 ? R.drawable.day_background_b : R.drawable.day_background_a);
		
		day3 = (Button) findViewById(R.id.day3);
		day3.setOnClickListener(this);
		day3.setBackgroundResource(isSelectDay3 ? R.drawable.day_background_b : R.drawable.day_background_a);
		
		day4 = (Button) findViewById(R.id.day4);
		day4.setOnClickListener(this);
		day4.setBackgroundResource(isSelectDay4 ? R.drawable.day_background_b : R.drawable.day_background_a);
		
		day5 = (Button) findViewById(R.id.day5);
		day5.setOnClickListener(this);
		day5.setBackgroundResource(isSelectDay5 ? R.drawable.day_background_b : R.drawable.day_background_a);
		
		day6 = (Button) findViewById(R.id.day6);
		day6.setOnClickListener(this);
		day6.setBackgroundResource(isSelectDay6 ? R.drawable.day_background_b : R.drawable.day_background_a);
		
		day7 = (Button) findViewById(R.id.day7);
		day7.setOnClickListener(this);
		day7.setBackgroundResource(isSelectDay7 ? R.drawable.day_background_b : R.drawable.day_background_a);
		
		startTimeStr = (TextView) findViewById(R.id.start_time_text);
		startTimeStr.setText(sp.getString(Constant.SMART_START_TIME, "09:00"));
		
		intervalTimeStr = (TextView) findViewById(R.id.interval_time_text);
		intervalTimeStr.setText(Integer.toString(sp.getInt(Constant.SMART_INTERVAL_TIME, 10))+getResources().getString(R.string.smart_minute));
	}
	
	private void syncClock(){
		if(isApp.getConnectBleDevice()){
			//设置闹钟
			plusdotBLEDevice.synchronizationClockData(true);
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
                            	syncClock();
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
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.syn_Btn:
			syncClock();
			break;
		case R.id.left_Btn:
			if(isApp.getConnectBleDevice() && isChange){
				showConfirmDialog();
			}else{
				finish();
			}
			break;
		case R.id.start_layout:
			smartTimePopupWindow(false, sp.getString(Constant.SMART_START_TIME, "09:00"));
			break;
		case R.id.interval_layout:
			intervalTimePopupWindow(false,sp.getInt(Constant.SMART_INTERVAL_TIME, 10));
			break;
		case R.id.day1:
			isSelectDay1 = !isSelectDay1;
			sp.edit().putBoolean(Constant.SMART_CLOCK_DAY1, isSelectDay1).commit();
			day1.setBackgroundResource(isSelectDay1 ? R.drawable.day_background_b : R.drawable.day_background_a);
			setChange();
			break;
		case R.id.day2:
			isSelectDay2 = !isSelectDay2;
			sp.edit().putBoolean(Constant.SMART_CLOCK_DAY2, isSelectDay2).commit();
			day2.setBackgroundResource(isSelectDay2 ? R.drawable.day_background_b : R.drawable.day_background_a);
			setChange();
			break;
		case R.id.day3:
			isSelectDay3 = !isSelectDay3;
			sp.edit().putBoolean(Constant.SMART_CLOCK_DAY3, isSelectDay3).commit();
			day3.setBackgroundResource(isSelectDay3 ? R.drawable.day_background_b : R.drawable.day_background_a);
			setChange();
			break;
		case R.id.day4:
			isSelectDay4 = !isSelectDay4;
			sp.edit().putBoolean(Constant.SMART_CLOCK_DAY4, isSelectDay4).commit();
			day4.setBackgroundResource(isSelectDay4 ? R.drawable.day_background_b : R.drawable.day_background_a);
			setChange();
			break;
		case R.id.day5:
			isSelectDay5 = !isSelectDay5;
			sp.edit().putBoolean(Constant.SMART_CLOCK_DAY5, isSelectDay5).commit();
			day5.setBackgroundResource(isSelectDay5 ? R.drawable.day_background_b : R.drawable.day_background_a);
			setChange();
			break;
		case R.id.day6:
			isSelectDay6 = !isSelectDay6;
			sp.edit().putBoolean(Constant.SMART_CLOCK_DAY6, isSelectDay6).commit();
			day6.setBackgroundResource(isSelectDay6 ? R.drawable.day_background_b : R.drawable.day_background_a);
			setChange();
			break;
		case R.id.day7:
			isSelectDay7 = !isSelectDay7;
			sp.edit().putBoolean(Constant.SMART_CLOCK_DAY7, isSelectDay7).commit();
			day7.setBackgroundResource(isSelectDay7 ? R.drawable.day_background_b : R.drawable.day_background_a);
			setChange();
			break;
		}
	}
	
	/////////////////////
	 private void smartTimePopupWindow(boolean isCyclic,String selectTime){
		  if (smartTimeWindow == null){
			LayoutInflater layoutIn = (LayoutInflater) getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutIn.inflate(R.layout.smart_popup_wheel, null);
			smartTimeWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			
			final WheelView hours = (WheelView) view.findViewById(R.id.hour);
			hours.setAdapter(new NumericWheelAdapter(0, 23));
			hours.setCyclic(false);
			
			final WheelView mins = (WheelView) view.findViewById(R.id.mins);
			mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
			mins.setCyclic(true);
//			mins.setCurrentItem(index);
			
			
			Button saveBtn = (Button) view.findViewById(R.id.save_btn);
			saveBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					smartTimeWindow.dismiss();
//					smartTimeWindow = null;
					String timeStr = String.format("%02d",hours.getCurrentItem()); 
					timeStr = timeStr+":"+ String.format("%02d",mins.getCurrentItem());
					startTimeStr.setText(timeStr);
					sp.edit().putString(Constant.SMART_START_TIME, timeStr).commit();
				}
			});
			
			String[] ary 	= selectTime.split(":");
			hours.setCurrentItem(Integer.parseInt(ary[0]));
			mins.setCurrentItem(Integer.parseInt(ary[1]));
			smartTimeWindow.setBackgroundDrawable(new BitmapDrawable());
			smartTimeWindow.setAnimationStyle(R.style.popuStyle);
			smartTimeWindow.setOutsideTouchable(true);
		}
		
		  smartTimeWindow.showAtLocation(findViewById(R.id.clock_main_layout), Gravity.BOTTOM,0, 0);
	}
	 
	 private int[] intervalTimes = {10,20,30};
	 
	 private void intervalTimePopupWindow(final boolean isstartTime,int intervalTime){
		if (intervalWindow == null){
			LayoutInflater layoutIn = (LayoutInflater) getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutIn.inflate(R.layout.popup_wheel, null);
			intervalWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				
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
				    intervalWindow.dismiss();
//				    intervalWindow = null;
					intervalTimeStr.setText(Integer.toString(arrayData.get(time.getCurrentItem()))+getResources().getString(R.string.smart_minute));
					sp.edit().putInt(Constant.SMART_INTERVAL_TIME, arrayData.get(time.getCurrentItem())).commit();
					
					setChange();
			}
			});
			
			for(int index = 0; index < intervalTimes.length; index++){
				if(intervalTimes[index] == intervalTime){
					time.setCurrentItem(index);
				}
			}
				
			intervalWindow.setBackgroundDrawable(new BitmapDrawable());
			intervalWindow.setAnimationStyle(R.style.popuStyle);
			intervalWindow.setOutsideTouchable(true);
		}
			
		intervalWindow.showAtLocation(findViewById(R.id.clock_main_layout), Gravity.BOTTOM,0, 0);
	}
	 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			syncClock();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	/////////////////////////////////////////////////////////////////////////
	public Handler uihandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 11:
				Toast toast= Toast.makeText(SmartClockActivity.this, getResources().getString(R.string.sync_success), Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,toast.getYOffset() / 2);
				toast.show();
				finish();
				break;
			case MainActivity.MSG_BLE_DISCONNECTED:
				Toast toast2= Toast.makeText(SmartClockActivity.this, getResources().getString(R.string.sync_error), Toast.LENGTH_SHORT);
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
			Log.d("TAG", "SmartClockActivity 's callbackCall..................."+command);
			
			Message msg = uihandler.obtainMessage(command); 
			uihandler.sendMessage(msg);
		}

		@Override
		public void callbackFail(int command, byte[] dataPacket) {
			// TODO Auto-generated method stub
			Log.d("TAG", "SmartClockActivity 's callbackFail..................."+command);

			Message msg = uihandler.obtainMessage(command); 
			uihandler.sendMessage(msg);
		}
	}
	
}
