package com.guogee.smartwatch.ui;

import java.util.ArrayList;

import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.ble.PlusDotBLEDevice;
import com.guogee.smartwatch.ble.StatusChangedCallback;
import com.guogee.smartwatch.ui.SleepFragment.SleepUIStatusChangedCallback;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.utils.Log;
import com.guogee.smartwatch.widget.NumericWheelAdapter;
import com.guogee.smartwatch.widget.Switch;
import com.guogee.smartwatch.widget.WheelView;
import com.guogee.smartwatch.widget.Switch.OnCheckedChangeListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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
 * “活动提醒” 界面
 * 
 * @author john
 *
 */
public class ExerciseActivity extends Activity implements OnClickListener{

	private ImageButton leftBtn;
	private iSmartApplication isApp;
	
	private RelativeLayout startLy;
	private RelativeLayout endLy;
	private RelativeLayout intervalLy;
	private RelativeLayout repeatLy;
	
	private TextView startTimeStr;
	private TextView endTimeStr;
	private TextView intervalTimeStr;
	private Switch swBtn;
	private SharedPreferences sp;
	
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
	
	public static int RESULT_CODE = 1;
	
	private PopupWindow stWindow;
	private PopupWindow etWindow;
	private PopupWindow reptWindow;
	
	private PlusDotBLEDevice plusdotBLEDevice;
	private TextView syncText;
	
	private boolean isChange = false;
	
	private void setChange(){
		isChange = true;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.exercise_layout);  
		
		isApp = (iSmartApplication)getApplication();
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		plusdotBLEDevice = PlusDotBLEDevice.getInstance();
		
		isSelectDay1 = sp.getBoolean(Constant.EXERCISE_CLOCK_DAY1, false);
		isSelectDay2 = sp.getBoolean(Constant.EXERCISE_CLOCK_DAY2, false);
		isSelectDay3 = sp.getBoolean(Constant.EXERCISE_CLOCK_DAY3, false);
		isSelectDay4 = sp.getBoolean(Constant.EXERCISE_CLOCK_DAY4, false);
		isSelectDay5 = sp.getBoolean(Constant.EXERCISE_CLOCK_DAY5, false);
		isSelectDay6 = sp.getBoolean(Constant.EXERCISE_CLOCK_DAY6, false);
		isSelectDay7 = sp.getBoolean(Constant.EXERCISE_CLOCK_DAY7, false);
		
		initView();
		
		isApp.addCallBack(new UIStatusChangedCallback());
	}
	
	private void initView(){
		
		leftBtn = (ImageButton) findViewById(R.id.left_Btn);
		leftBtn.setOnClickListener(this);
		
		startLy = (RelativeLayout) findViewById(R.id.start_time_layout);
		startLy.setOnClickListener(this);

		endLy = (RelativeLayout) findViewById(R.id.end_layout);
		endLy.setOnClickListener(this);
		
		intervalLy = (RelativeLayout) findViewById(R.id.interval_layout);
		intervalLy.setOnClickListener(this);
		
		syncText = (TextView) findViewById(R.id.syn_Btn);
		syncText.setOnClickListener(this);
		
		swBtn = (Switch) findViewById(R.id.switch_activity_Btn);
		swBtn.setChecked(sp.getBoolean(Constant.ACTIVITY_REMINDER_HINT, false));
		swBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(Switch switchView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isApp.getConnectBleDevice()){
					sp.edit().putBoolean(Constant.ACTIVITY_REMINDER_HINT, isChecked).commit();
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
		startTimeStr.setText(sp.getString(Constant.EXERCISE_START_TIME, "09:00"));
		
		endTimeStr = (TextView) findViewById(R.id.end_time_text);
		endTimeStr.setText(sp.getString(Constant.EXERCISE_END_TIME, "18:00"));
		
		intervalTimeStr = (TextView) findViewById(R.id.interval_time_text);
		intervalTimeStr.setText(Integer.toString(sp.getInt(Constant.EXERCISE_INTERVAL_TIME, 15)));
	}

//	String[] ary 	= item.time.split(":");
	
	 @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
		case R.id.start_time_layout:
			showSTPopupWindow(false,sp.getString(Constant.EXERCISE_START_TIME, "09:00"));
			break;
		case R.id.end_layout:
			showETPopupWindow(true,sp.getString(Constant.EXERCISE_END_TIME, "18:00"));
			break;
		case R.id.interval_layout:
			showRepPopupWindow(false,sp.getInt(Constant.EXERCISE_INTERVAL_TIME, 15));
			break;
		case R.id.day1:
			isSelectDay1 = !isSelectDay1;
			sp.edit().putBoolean(Constant.EXERCISE_CLOCK_DAY1, isSelectDay1).commit();
			day1.setBackgroundResource(isSelectDay1 ? R.drawable.day_background_b : R.drawable.day_background_a);
			setChange();
			break;
		case R.id.day2:
			isSelectDay2 = !isSelectDay2;
			sp.edit().putBoolean(Constant.EXERCISE_CLOCK_DAY2, isSelectDay2).commit();
			day2.setBackgroundResource(isSelectDay2 ? R.drawable.day_background_b : R.drawable.day_background_a);
			setChange();
			break;
		case R.id.day3:
			isSelectDay3 = !isSelectDay3;
			sp.edit().putBoolean(Constant.EXERCISE_CLOCK_DAY3, isSelectDay3).commit();
			day3.setBackgroundResource(isSelectDay3 ? R.drawable.day_background_b : R.drawable.day_background_a);
			setChange();
			break;
		case R.id.day4:
			isSelectDay4 = !isSelectDay4;
			sp.edit().putBoolean(Constant.EXERCISE_CLOCK_DAY4, isSelectDay4).commit();
			day4.setBackgroundResource(isSelectDay4 ? R.drawable.day_background_b : R.drawable.day_background_a);
			setChange();
			break;
		case R.id.day5:
			isSelectDay5 = !isSelectDay5;
			sp.edit().putBoolean(Constant.EXERCISE_CLOCK_DAY5, isSelectDay5).commit();
			day5.setBackgroundResource(isSelectDay5 ? R.drawable.day_background_b : R.drawable.day_background_a);
			setChange();
			break;
		case R.id.day6:
			isSelectDay6 = !isSelectDay6;
			sp.edit().putBoolean(Constant.EXERCISE_CLOCK_DAY6, isSelectDay6).commit();
			day6.setBackgroundResource(isSelectDay6 ? R.drawable.day_background_b : R.drawable.day_background_a);
			setChange();
			break;
		case R.id.day7:
			isSelectDay7 = !isSelectDay7;
			sp.edit().putBoolean(Constant.EXERCISE_CLOCK_DAY7, isSelectDay7).commit();
			day7.setBackgroundResource(isSelectDay7 ? R.drawable.day_background_b : R.drawable.day_background_a);
			setChange();
			break;
		}
	}
	
	// /////////
	private void showSTPopupWindow(boolean isCyclic,String selectTime) {
		
		if (stWindow == null) {
			LayoutInflater layoutIn = (LayoutInflater) getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutIn.inflate(R.layout.popup_wheel, null);
			stWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);

			final WheelView time = (WheelView) view.findViewById(R.id.hour);
			time.setAdapter(new NumericWheelAdapter(0, 23));
			time.setCyclic(isCyclic);

			Button saveBtn = (Button) view.findViewById(R.id.save_btn);
			saveBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					stWindow.dismiss();
					stWindow = null;
					String timeStr = String.format("%02d", time.getCurrentItem());
					timeStr = timeStr + ":00";
					startTimeStr.setText(timeStr);

					sp.edit().putString(Constant.EXERCISE_START_TIME, timeStr).commit();
					
					setChange();
				}
			});

			String[] ary 	= selectTime.split(":");
			time.setCurrentItem(Integer.parseInt(ary[0]));
			
			stWindow.setBackgroundDrawable(new BitmapDrawable());
			stWindow.setAnimationStyle(R.style.popuStyle);
			stWindow.setOutsideTouchable(true);
		}

		stWindow.showAtLocation(findViewById(R.id.exercise_main_layout), Gravity.BOTTOM, 0, 0);
	}

	private int[] intervalTimes = {15,30,45,60,75,90,105,120};
	
	private void showRepPopupWindow(final boolean isstartTime,int intervalTime) {
		if (reptWindow == null) {
			LayoutInflater layoutIn = (LayoutInflater) getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutIn.inflate(R.layout.popup_wheel, null);
			reptWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

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
					reptWindow.dismiss();
					reptWindow = null;
					intervalTimeStr.setText(Integer.toString(arrayData.get(time.getCurrentItem())));

					sp.edit().putInt(Constant.EXERCISE_INTERVAL_TIME, arrayData.get(time.getCurrentItem()))
							.commit();
					
					setChange();
				}
			});

			for(int index = 0; index < intervalTimes.length; index++){
				if(intervalTimes[index] == intervalTime){
					time.setCurrentItem(index);
				}
			}
			
			reptWindow.setBackgroundDrawable(new BitmapDrawable());
			reptWindow.setAnimationStyle(R.style.popuStyle);
			reptWindow.setOutsideTouchable(true);
		}

		reptWindow.showAtLocation(findViewById(R.id.exercise_main_layout),
				Gravity.BOTTOM, 0, 0);
	}

	private void showETPopupWindow(boolean isCyclic,String selectTime) {
		if (etWindow == null) {
			LayoutInflater layoutIn = (LayoutInflater) getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutIn.inflate(R.layout.popup_wheel, null);
			etWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);

			final WheelView startTime = (WheelView) view.findViewById(R.id.hour);
			startTime.setAdapter(new NumericWheelAdapter(0, 23));
			startTime.setCyclic(true);

			Button saveBtn = (Button) view.findViewById(R.id.save_btn);
			saveBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					etWindow.dismiss();
					String timeStr = String.format("%02d", startTime.getCurrentItem());
					timeStr = timeStr + ":00";
					endTimeStr.setText(timeStr);
					sp.edit().putString(Constant.EXERCISE_END_TIME, timeStr).commit();
					
					setChange();
				}
			});
			
			String[] ary 	= selectTime.split(":");
			startTime.setCurrentItem(Integer.parseInt(ary[0]));

			etWindow.setBackgroundDrawable(new BitmapDrawable());
			etWindow.setAnimationStyle(R.style.popuStyle);
			etWindow.setOutsideTouchable(true);
		}

		etWindow.showAtLocation(findViewById(R.id.exercise_main_layout), Gravity.BOTTOM, 0, 0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			syncClock();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void syncClock() {
		if (isApp.getConnectBleDevice()) {
			// 设置闹钟
			plusdotBLEDevice.synchronizationSportClockData(true); 
		}
	}

	/////////////////////////////////////////////////////////////////////////
	public Handler uihandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 11:
				Toast toast= Toast.makeText(ExerciseActivity.this, getResources().getString(R.string.sync_success), Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,toast.getYOffset() / 2);
				toast.show();
				
				finish();
				break;
			case MainActivity.MSG_BLE_DISCONNECTED:
				Toast toast2= Toast.makeText(ExerciseActivity.this, getResources().getString(R.string.sync_error), Toast.LENGTH_SHORT);
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
			Log.d("TAG", "ExerciseActivity 's callbackCall..................."+command);
			Message msg = uihandler.obtainMessage(command); 
			uihandler.sendMessage(msg);
		}

		@Override
		public void callbackFail(int command, byte[] dataPacket) {
			// TODO Auto-generated method stub
			Log.d("TAG", "ExerciseActivity 's callbackFail..................."+command);
			Message msg = uihandler.obtainMessage(command); 
			uihandler.sendMessage(msg);
		}
	}
}
