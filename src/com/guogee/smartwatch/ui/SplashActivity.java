package com.guogee.smartwatch.ui;


import java.util.List;
import java.util.Map;

import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.dao.BindDeviceDao;
import com.guogee.smartwatch.utils.Constant;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

/**
 * Splash screen
 * 
 * @author Huihan
 * 
 */
public class SplashActivity extends Activity {
	
	private final int SPLASH_DISPLAY_LENGHT = 3000;
	private boolean isFirst		= false;
	private SharedPreferences sp;
	private iSmartApplication isApp;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash_screen);

		isApp = (iSmartApplication)getApplication();
		
//		isFirst = getSharedPreferences("CONFIG", Context.MODE_PRIVATE).getBoolean("FIRST", true);
		sp = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(sp.getBoolean(Constant.FIRST_FALG, false)){ // false chang to true,bingchuang watch
					gotoActivity();
				}else{
					Editor editor = sp.edit();
					editor.putBoolean(Constant.FIRST_FALG, true);
					editor.commit();
//					startActivity(new Intent(SplashActivity.this, LoginActivity.class));
					startActivity(new Intent(SplashActivity.this, GuideActivity.class));
					finish();
				}
			}
		}, SPLASH_DISPLAY_LENGHT);
	}
   
   private void gotoActivity(){
	   //判断是否绑定,绑定直接跳到主界面，否则跳到搜索界面
		BindDeviceDao dao = new BindDeviceDao(this);
		List<Map<String, String>> daoList = dao.listBleDevice(null);
		isApp.setBindWatch(daoList);
//	    Intent intent = new Intent(SplashActivity.this, daoList.size()==0 ? BindWatchActivity.class : MainActivity.class);
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
	    startActivity(intent);
	    finish();
   }	
   
}
