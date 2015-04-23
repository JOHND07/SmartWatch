package com.guogee.smartwatch.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.ble.PlusDotBLEDevice;
import com.guogee.smartwatch.dao.SportDao;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.widget.CircularTouch;
import com.guogee.smartwatch.widget.CircularTouch.OnListener;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class SportTargetFragment extends Fragment implements OnClickListener{
	
	private ImageButton leftBtn;
	private iSmartApplication isApp;
	private CircularTouch circularTouch;
	private SharedPreferences sp;
	private PlusDotBLEDevice plusdotDevice;
	private String mCurrentSelectDay ;
	
	private SportDao sportDao;
	private List<Map<String, String>> sportData;
	private String mId;
	
	private PlusDotBLEDevice plusdotBLEDevice;
	
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	
	public static SportTargetFragment instantiation(int position) {
		SportTargetFragment fragment = new SportTargetFragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isApp = (iSmartApplication)getActivity().getApplication();
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		plusdotBLEDevice = PlusDotBLEDevice.getInstance();
		
		Log.e("TAG","SportTargetFragment 's onCreate .................................");
		
		SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy/MM/dd");     
		Date curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
		mCurrentSelectDay = formatter.format(curDate); 
	
		sportDao = new SportDao(getActivity());
		String[] params = new String[]{ mCurrentSelectDay };	
		Map<String, String> sportData = sportDao.listStepFromDays(params);
		if(sportData.size()!=0){
			mId = sportData.get("id");			
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Log.e("TAG","SportTargetFragment 's onCreateView .................................");
		
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.sport_target_layout, container, false);
		
		leftBtn = (ImageButton) root.findViewById(R.id.left_Btn);
		leftBtn.setOnClickListener(this);
		circularTouch = (CircularTouch)root.findViewById(R.id.circularTouch);

		int[] location  = new int[2];
		location[0] = sp.getInt(Constant.POINT_LOCATION_X, -1);
		location[1] = sp.getInt(Constant.POINT_LOCATION_Y, -1);
		
		
		btn1 = (Button) root.findViewById(R.id.btn1);
		btn1.setOnClickListener(this);
		
		btn2 = (Button) root.findViewById(R.id.btn2);
		btn2.setOnClickListener(this);
		
		btn3 = (Button) root.findViewById(R.id.btn3);
		btn3.setOnClickListener(this);
		
		btn4 = (Button) root.findViewById(R.id.btn4);
		btn4.setOnClickListener(this);
		
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
//				if(isApp.getConnectBleDevice()){
//					plusdotBLEDevice.synchronizationTarget(true);
//
//					Editor editor = sp.edit();
//					editor.putInt(Constant.NUM_KEY, Integer.parseInt(view.getTag().toString()));
//					editor.putInt(Constant.POINT_LOCATION_X, circularTouch.getLocation()[0]);
//					editor.putInt(Constant.POINT_LOCATION_Y, circularTouch.getLocation()[1]);
//					editor.commit();
//					
//					Object[] params = new Object[] {view.getTag().toString(),mId};
//					sportDao.updateStepTarget(params);
//
//				}else{
//					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.disconnect), Toast.LENGTH_SHORT).show();
//				}
				
				Editor editor = sp.edit();
				editor.putInt(Constant.NUM_KEY, Integer.parseInt(view.getTag().toString()));
				editor.putInt(Constant.POINT_LOCATION_X, circularTouch.getLocation()[0]);
				editor.putInt(Constant.POINT_LOCATION_Y, circularTouch.getLocation()[1]);
				editor.commit();
				
				Object[] params = new Object[] {view.getTag().toString(),mId};
				sportDao.updateStepTarget(params);
			}
		});
		return root;
	}
	
	 @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("TAG","SportTargetFragment 's onPause .................................");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("TAG","SportTargetFragment 's onResume ................................."+isApp.getLeftMenuTag());
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view.equals(leftBtn)){
			isApp.getSlidingMenu().showMenu();
		}else if(view.equals(btn1)){
			
			if(isApp.getConnectBleDevice()){
				plusdotBLEDevice.synchronizationCalibration(0);
			}else{
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.disconnect), Toast.LENGTH_SHORT).show();
			}
			
			
		}else if(view.equals(btn2)){
			
			if(isApp.getConnectBleDevice()){
				plusdotBLEDevice.synchronizationCalibration(1);
			}else{
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.disconnect), Toast.LENGTH_SHORT).show();
			}
			
		}else if(view.equals(btn3)){
			
			if(isApp.getConnectBleDevice()){
				plusdotBLEDevice.synchronizationCalibration(2);
			}else{
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.disconnect), Toast.LENGTH_SHORT).show();
			}
			
		}else if(view.equals(btn4)){
			
			if(isApp.getConnectBleDevice()){
				plusdotBLEDevice.synchronizationCalibration(3);
			}else{
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.disconnect), Toast.LENGTH_SHORT).show();
			}
			
		}
	}	
	
}
