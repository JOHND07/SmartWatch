package com.guogee.smartwatch.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.utils.Log;
import com.guogee.smartwatch.widget.BitmapUtil;
import com.guogee.smartwatch.widget.CircularImage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class leftMenuFragment extends Fragment implements OnClickListener{
	
	private iSmartApplication isApp;
	
	private RelativeLayout sportLy;
	private RelativeLayout sleepLy;
	private RelativeLayout sportTargetLy;
	private RelativeLayout bindLy;
	private RelativeLayout gifyLy;
	private RelativeLayout beanLy;
	
	private ImageView setupImg;
//	private ImageView useImage;
	private TextView useName;
	private SharedPreferences sp;
	private String strLoginName = "";
	private String strLoginPwd = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isApp = (iSmartApplication)getActivity().getApplication();
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		strLoginName = sp.getString(Constant.USERNAME, "");
		strLoginPwd  = sp.getString(Constant.PASSWORD, "");
		
		isApp.setLeftMenuHandler(uihandler);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.left_menu_fragment, container, false);

		sportLy = (RelativeLayout) root.findViewById(R.id.sport_layout);
		sportLy.setOnClickListener(this);
		
		sleepLy = (RelativeLayout) root.findViewById(R.id.sleep_layout);
        sleepLy.setOnClickListener(this);
		
        sportTargetLy = (RelativeLayout) root.findViewById(R.id.sport_target_layout);
        sportTargetLy.setOnClickListener(this);
		
		bindLy = (RelativeLayout) root.findViewById(R.id.bind_layout);
		bindLy.setOnClickListener(this);
		
		gifyLy = (RelativeLayout) root.findViewById(R.id.gift_layout);
		gifyLy.setOnClickListener(this);
		
		beanLy = (RelativeLayout) root.findViewById(R.id.bean_layout);
		beanLy.setOnClickListener(this);
		
		setupImg = (ImageView) root.findViewById(R.id.setup_img);
		setupImg.setOnClickListener(this);
		
//		useImage = (ImageView) root.findViewById(R.id.use_image);
//		useImage.setOnClickListener(this);
		
		useName = (TextView) root.findViewById(R.id.use_name);
//		useName.setText(sp.getString(Constant.USER_NAME, ""));
		
//		Log.d("TAG","leftMenuFragment 's left Menu Tag==================="+isApp.getLeftMenuTag());
		
		cover_user_photo = (CircularImage) root.findViewById(R.id.cover_user_photo);
//		cover_user_photo.setImageResource(R.drawable.face);
		cover_user_photo.setOnClickListener(this);
		
		//如果是英文版本，屏蔽“好礼兑换”,‘能量豆’.
//		if(getResources().getConfiguration().locale.getCountry().equals("zh-CN") || 
//				getResources().getConfiguration().locale.getCountry().equals("zh-TW") ||
//				getResources().getConfiguration().locale.getCountry().equals("CN") ){
//			gifyLy.setVisibility(View.VISIBLE);
//			gifyLy.setClickable(true);
//			beanLy.setVisibility(View.VISIBLE);
//			beanLy.setClickable(true);
//		}else{
//			gifyLy.setVisibility(View.INVISIBLE);
//			gifyLy.setClickable(false);
//			beanLy.setVisibility(View.INVISIBLE);
//			beanLy.setClickable(false);
//		}
		
		return root;
	}
	
	private CircularImage cover_user_photo;
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//动态更换名字时，应更新user 's name 
		useName.setText(sp.getString(Constant.USERNAME, ""));
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(BitmapUtil.getSDPath() + "/Chaoke/xiaoma.jpg");
			Bitmap bitmap  = BitmapFactory.decodeStream(fis);
//			useImage.setImageBitmap(bitmap);
			cover_user_photo.setImageBitmap(bitmap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.sport_layout:
			isApp.setLeftMenuTag(Constant.SPORT_TAG);
			isApp.getSlidingMenu().toggle();
			break;
		case R.id.sleep_layout:
			isApp.setLeftMenuTag(Constant.SLEEP_TAG);
			isApp.getSlidingMenu().toggle();
			break;
		case R.id.sport_target_layout:
			isApp.setLeftMenuTag(Constant.SPORT_TARGET_TAG);
			isApp.getSlidingMenu().toggle();
			break;
		case R.id.bind_layout:
			isApp.setLeftMenuTag(Constant.BIND_TAG);
			isApp.getSlidingMenu().toggle();
			break;
		//新增"好礼兑换"	
		case R.id.gift_layout:
			isApp.setLeftMenuTag(Constant.GIFT_TAG);
			isApp.getSlidingMenu().toggle();
			break;
		case R.id.setup_img:
//			isApp.setLeftMenuTag(Constant.SETUP_TAG);
//			isApp.getSlidingMenu().toggle();
//	        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
	        
//	        startActivity(new Intent(getActivity(), ExchangeActivity.class));
	        startActivity(new Intent(getActivity(), SettingActivity.class));
	        
			break;
		case R.id.bean_layout:
			isApp.setLeftMenuTag(Constant.BEAN_TAG);
			isApp.getSlidingMenu().toggle();
			break;
//		case R.id.use_image:
		case R.id.cover_user_photo:
			Intent intent = new Intent(getActivity(), SetUserData.class);
			Bundle bundle = new Bundle();
			bundle.putString("str_name", useName.getText().toString());
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}
	}
	
	private void gotoActivity(Class<?> cls){
		Intent intent = new Intent(getActivity(), cls);
		if(cls.equals(LoginActivity.class)){
			Bundle bundle = new Bundle();
			bundle.putBoolean("reLogin", true);
			intent.putExtras(bundle);
		}
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
	}
	
	///////////// handle ////////////
	 public Handler uihandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 22:
					Log.d("TAG",".............22............");
//					sm.showContent();
//					isApp.getSlidingMenu().showMenu();
					
					isApp.setLeftMenuTag(Constant.SPORT_TAG);
					isApp.getSlidingMenu().toggle();
					break;
				}
			}
	 };

	

}
