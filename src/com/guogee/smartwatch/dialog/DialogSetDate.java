package com.guogee.smartwatch.dialog;

import com.guogee.smartwatch.R;
import com.guogee.smartwatch.widget.NumberPicker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class DialogSetDate extends Activity {
	private Button mPickerok = null;
	private Button mPickeresc = null;
	int srcyear = 0;
	int srcmonth = 0;
	int srcday = 0;
	int newyear = 0;
	int newmonth = 0;
	int[] month1 = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	int[] month2 = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	NumberPicker npyear = null;
	NumberPicker npmonth = null;
	NumberPicker npday = null;
	private Thread newThread = new Thread(new Runnable() {
		@Override
		public void run() {
			Log.i("wen", "THREAD RUN");
			if (npyear != null && npmonth != null) {
				Log.i("wen", "!=null");
				if (newyear != npmonth.getValue()
						|| newmonth != npday.getValue()) {
					Log.i("wen", "!=null2");
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
					Log.i("wen", "handle send");
				}
			}
			handler.postDelayed(newThread, 5000);
		}
	});

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				Log.i("wen", "已经接收");
				newyear = npyear.getValue();
				newmonth = npmonth.getValue();
				if ((newyear % 4 == 0 && newyear % 100 != 0)
						|| newyear % 400 == 0) {
					Log.i("wen", "" + newmonth);
					Log.i("wen", "" + month1.length);
					npday.setMaxValue(month2[newmonth - 1]);
				} else {
					Log.i("wen", "" + newmonth);
					Log.i("wen", "" + month1.length);
					npday.setMaxValue(month1[newmonth - 1]);
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setdate);
		mPickerok = (Button) findViewById(R.id.numberPickerok);
		mPickeresc = (Button) findViewById(R.id.numberPickercanle);
		npyear = (NumberPicker) findViewById(R.id.year);
		npmonth = (NumberPicker) findViewById(R.id.month);
		npday = (NumberPicker) findViewById(R.id.day);
		// 设置年
		npyear.setMaxValue(2100);
		npyear.setMinValue(1900);
		npyear.setFocusable(true);
		npyear.setFocusableInTouchMode(true);
		// 设置月
		npmonth.setMaxValue(12);
		npmonth.setMinValue(1);
		npmonth.setFocusable(true);
		npmonth.setFocusableInTouchMode(true);
		// 设置日
		npday.setMinValue(1);
		npday.setMaxValue(31);
		npday.setFocusable(true);
		npday.setFocusableInTouchMode(true);
		String date[] = getIntent().getStringExtra("date").split("-");
		if(date!=null&&date.length>2){
			npyear.setValue(Integer.parseInt(date[0]));
			npmonth.setValue(Integer.parseInt(date[1]));
			npday.setValue(Integer.parseInt(date[2]));
			System.out.println(date[2]+"date");
		}
		
		//newThread.start(); //启动线程
		handler.post(newThread);

		mPickeresc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.removeCallbacks(newThread);
				newThread = null;
				Intent intent = new Intent();
				setResult(50, intent); // 50 为空
				
				// 关闭掉这个Activity
				finish();
			}
		});

		mPickerok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.removeCallbacks(newThread);
				newThread = null;
				srcyear = npyear.getValue();
				srcmonth = npmonth.getValue();
				srcday = npday.getValue();
				Intent intent = new Intent();
				intent.putExtra("year", "" + srcyear);
				intent.putExtra("month", "" + srcmonth);
				intent.putExtra("day", "" + srcday);
				setResult(40, intent);
				
				finish();
			}
		});
	}
}
