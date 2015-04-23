package com.guogee.smartwatch.ui;

import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.widget.CircularTouch;
import com.guogee.smartwatch.widget.CircularTouch.OnListener;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

/**
 * "跑步" 设置目标界面
 * 
 * @author john
 *
 */
public class SportTarget extends Activity implements OnClickListener{

	private ImageButton leftBtn;
	private iSmartApplication isApp;
	private CircularTouch circularTouch;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.sport_target_layout);  
        
		isApp = (iSmartApplication)getApplication();
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		initView();
	}

	private void initView(){
		leftBtn = (ImageButton) findViewById(R.id.left_Btn);
		leftBtn.setOnClickListener(this);

		circularTouch = (CircularTouch) findViewById(R.id.circularTouch);

		int[] location  = new int[2];
		location[0] = sp.getInt(Constant.POINT_LOCATION_X, -1);
		location[1] = sp.getInt(Constant.POINT_LOCATION_Y, -1);
		
//		Log.d("TAG","pointLocationX =================== "+location[0]);
//		Log.d("TAG","pointLocationY =================== "+location[1]);
//		Log.d("TAG","tempNumKey =================== "+sp.getInt(Constant.NUM_KEY, 10000));
		
		circularTouch.setLocation(location);
		circularTouch.setTemperature(sp.getInt(Constant.NUM_KEY, 10000));

//		circularTouch.setLocation(location);
//		circularTouch.setTemperature(sharedPreferences.getInt(tempNumKey, 23));
		circularTouch.setOnListener(new OnListener() {
			@Override
			public boolean OnLongClickListener(View view) {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public void OnClickListener(View view) {
				// TODO Auto-generated method stub
				Editor editor = sp.edit();
				editor.putInt(Constant.NUM_KEY, Integer.parseInt(view.getTag().toString()));
				editor.putInt(Constant.POINT_LOCATION_X, circularTouch.getLocation()[0]);
				editor.putInt(Constant.POINT_LOCATION_Y, circularTouch.getLocation()[1]);
				editor.commit();
			}
		});
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
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view.equals(leftBtn)){
			finish();
		}
	}
	
}
