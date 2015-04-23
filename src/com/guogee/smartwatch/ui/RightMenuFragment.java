package com.guogee.smartwatch.ui;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.ble.PlusDotBLEDevice;
import com.guogee.smartwatch.playcamera.CameraActivity;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.widget.Switch;
import com.guogee.smartwatch.widget.Switch.OnCheckedChangeListener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class RightMenuFragment extends Fragment implements OnCheckedChangeListener, OnClickListener{
	
	private Switch phoneBtn;
	private Switch smsBtn;
	private Switch emailBtn;
	private Switch eventBtn;
	private Switch batteryBtn;
	private Switch webchatBtn;
	private Switch qqBtn;
	private Switch skypeBtn;
	private Switch whatsAppBtn;
	private Switch faceBookBtn;
	
//	private Switch trackerBtn;
	
	//模式切换 Switch
	private RelativeLayout voiceLy;
	private RelativeLayout shakeLy;
	private RelativeLayout voiceShakeLy;
	private RelativeLayout trackLayout;
	
	private RelativeLayout clockLayout;
	
	private LinearLayout modeLayout;

	private ImageView voiceImgBtn;
	private ImageView shakeImgBtn;
	private ImageView voiceShakeImgBtn;

	
	private SharedPreferences sp;
	private iSmartApplication isApp;
	private PlusDotBLEDevice plusdotBleDevice;
	
	private String path;
	private String name;
	
	private Uri outUri;
	private Uri inUri;
	
	private RelativeLayout picLayout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		isApp = (iSmartApplication)getActivity().getApplication(); 
		
		path	= Environment.getExternalStorageDirectory().getPath() + "/iSmartAndroid/image/";
		SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy/MM/dd/    HH:mm:ss     ");       
		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
		name    =    formatter.format(curDate);  
		
		mkdir(path, name);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.right_menu_fragment, container, false);
		
		phoneBtn = (Switch) root.findViewById(R.id.phone_Btn);
		phoneBtn.setOnCheckedChangeListener(this);
		phoneBtn.setChecked(sp.getBoolean(Constant.PHONE_FALG, true));
		
		smsBtn = (Switch) root.findViewById(R.id.sms_Btn);
		smsBtn.setOnCheckedChangeListener(this);
		smsBtn.setChecked(sp.getBoolean(Constant.SMS_FALG, true));
		
		emailBtn = (Switch) root.findViewById(R.id.email_Btn);
		emailBtn.setOnCheckedChangeListener(this);
		emailBtn.setChecked(sp.getBoolean(Constant.EMAIL_FALG, true));
		
		eventBtn = (Switch) root.findViewById(R.id.event_Btn);
		eventBtn.setOnCheckedChangeListener(this);
		eventBtn.setChecked(sp.getBoolean(Constant.EVENT_FALG, true));
		
		batteryBtn = (Switch) root.findViewById(R.id.battery_Btn);
		batteryBtn.setOnCheckedChangeListener(this);
		batteryBtn.setChecked(sp.getBoolean(Constant.BATTERY_FALG, true));
		
		webchatBtn = (Switch) root.findViewById(R.id.webchat_Btn);
		webchatBtn.setOnCheckedChangeListener(this);
		webchatBtn.setChecked(sp.getBoolean(Constant.WEBCHAT_FALG, true));
		
		qqBtn = (Switch) root.findViewById(R.id.qq_Btn);
		qqBtn.setOnCheckedChangeListener(this);
		qqBtn.setChecked(sp.getBoolean(Constant.QQ_FALG, true));
		
		skypeBtn = (Switch) root.findViewById(R.id.skype_Btn);
		skypeBtn.setOnCheckedChangeListener(this);
		skypeBtn.setChecked(sp.getBoolean(Constant.SKYPE_FALG, true));
		
		whatsAppBtn = (Switch) root.findViewById(R.id.whatsApp_Btn);
		whatsAppBtn.setOnCheckedChangeListener(this);
		whatsAppBtn.setChecked(sp.getBoolean(Constant.WHATSAPP_FALG, true));
		
		faceBookBtn = (Switch) root.findViewById(R.id.facebook_Btn);
		faceBookBtn.setOnCheckedChangeListener(this);
		faceBookBtn.setChecked(sp.getBoolean(Constant.FACEBOOK_FALG, true));
		
//		trackerBtn = (Switch) root.findViewById(R.id.tracker_btn);
//		trackerBtn.setOnCheckedChangeListener(this);
//		trackerBtn.setChecked(sp.getBoolean(Constant.TRACKER_FLAG, false));
		
		picLayout = (RelativeLayout) root.findViewById(R.id.take_pic_layout);
		picLayout.setOnClickListener(this);
		
		//模式,begin
		voiceLy = (RelativeLayout) root.findViewById(R.id.voice_ly);
		voiceLy.setOnClickListener(this);
		
		shakeLy = (RelativeLayout) root.findViewById(R.id.shake_ly);
		shakeLy.setOnClickListener(this);
		
		voiceShakeLy = (RelativeLayout) root.findViewById(R.id.voice_shake_ly);
		voiceShakeLy.setOnClickListener(this);
		
		trackLayout = (RelativeLayout) root.findViewById(R.id.track_layout);
		trackLayout.setOnClickListener(this);
		
		clockLayout = (RelativeLayout) root.findViewById(R.id.clock_layout);
		clockLayout.setOnClickListener(this);

		modeLayout = (LinearLayout) root.findViewById(R.id.mode_layout);
//		modeLayout.setOnClickListener(this);
		
//		if(sp.getInt(Constant.PLUSDOT_TYPE, 1)==1){
//			modeLayout.setVisibility(View.VISIBLE);
//		}else if(sp.getInt(Constant.PLUSDOT_TYPE, 1)==2){
//			modeLayout.setVisibility(View.GONE);
//		}
		
		voiceImgBtn = (ImageView) root.findViewById(R.id.voice_img_Btn);
		voiceImgBtn.setOnClickListener(this);
		voiceImgBtn.setVisibility(sp.getBoolean(Constant.REMINDER_VOICE_MODE, false) ? View.VISIBLE : View.INVISIBLE);
		
		shakeImgBtn = (ImageView) root.findViewById(R.id.shake_img_Btn);
		shakeImgBtn.setOnClickListener(this);
		shakeImgBtn.setVisibility(sp.getBoolean(Constant.REMINDER_SHAKE_MODE, false) ? View.VISIBLE : View.INVISIBLE);
		
		voiceShakeImgBtn = (ImageView) root.findViewById(R.id.voice_shake_img_Btn);
		voiceShakeImgBtn.setOnClickListener(this);
		voiceShakeImgBtn.setVisibility(sp.getBoolean(Constant.REMINDER_VOICE_SHAKE_MODE, true) ? View.VISIBLE : View.INVISIBLE);
		//模式,end
		
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
		
		if(sp.getInt(Constant.PLUSDOT_TYPE, 1)==1){
			modeLayout.setVisibility(View.VISIBLE);
		}else if(sp.getInt(Constant.PLUSDOT_TYPE, 1)==2){
			modeLayout.setVisibility(View.GONE);
		}
	}	

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCheckedChanged(Switch switchView, boolean isChecked) {
		// TODO Auto-generated method stub
		Editor editor = sp.edit();
		
		switch(switchView.getId()){
		case R.id.phone_Btn:
			editor.putBoolean(Constant.PHONE_FALG, isChecked);
			editor.commit();
			break;
		case R.id.sms_Btn:
			editor.putBoolean(Constant.SMS_FALG, isChecked);
			editor.commit();
			break;
		case R.id.email_Btn:
			editor.putBoolean(Constant.EMAIL_FALG, isChecked);
			editor.commit();
			break;
		case R.id.event_Btn:
			editor.putBoolean(Constant.EVENT_FALG, isChecked);
			editor.commit();
			break;
		case R.id.battery_Btn:
			editor.putBoolean(Constant.BATTERY_FALG, isChecked);
			editor.commit();
			break;
		case R.id.webchat_Btn:
			editor.putBoolean(Constant.WEBCHAT_FALG, isChecked);
			editor.commit();
			break;
		case R.id.qq_Btn:
			editor.putBoolean(Constant.QQ_FALG, isChecked);
			editor.commit();
			break;
		case R.id.skype_Btn:
			editor.putBoolean(Constant.SKYPE_FALG, isChecked);
			editor.commit();
			break;
		case R.id.whatsApp_Btn:
			editor.putBoolean(Constant.WHATSAPP_FALG, isChecked);
			editor.commit();
			break;
		case R.id.facebook_Btn:
			editor.putBoolean(Constant.FACEBOOK_FALG, isChecked);
			editor.commit();
			break;
		case R.id.mode_layout:
			break;
//		case R.id.tracker_btn:
//			if(isApp.getConnectBleDevice()){
//				editor.putBoolean(Constant.TRACKER_FLAG, isChecked);
//				editor.commit();
//				
//				plusdotBleDevice = PlusDotBLEDevice.getInstance();
//				plusdotBleDevice.synchronizationTracker(true);
//			}else {
//				trackerBtn.setChecked(!isChecked);
//				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.disconnect), Toast.LENGTH_SHORT).show();
//			}
//			break;
		}
	}
	
	private void changeReminderMode(String mode){
		Editor editor = sp.edit();
		plusdotBleDevice = PlusDotBLEDevice.getInstance();
		
		if(mode.equals(Constant.REMINDER_VOICE_MODE)){
			editor.putBoolean(Constant.REMINDER_VOICE_MODE, true);
			editor.putBoolean(Constant.REMINDER_SHAKE_MODE, false);
			editor.putBoolean(Constant.REMINDER_VOICE_SHAKE_MODE, false);
			editor.commit();
		}else if(mode.equals(Constant.REMINDER_SHAKE_MODE)){
			editor.putBoolean(Constant.REMINDER_VOICE_MODE, false);
			editor.putBoolean(Constant.REMINDER_SHAKE_MODE, true);
			editor.putBoolean(Constant.REMINDER_VOICE_SHAKE_MODE, false);
			editor.commit();
		}else if(mode.equals(Constant.REMINDER_VOICE_SHAKE_MODE)){
			editor.putBoolean(Constant.REMINDER_VOICE_MODE, false);
			editor.putBoolean(Constant.REMINDER_SHAKE_MODE, false);
			editor.putBoolean(Constant.REMINDER_VOICE_SHAKE_MODE, true);
			editor.commit();
		}
		plusdotBleDevice.synchronizationReminderMode(mode,true);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.take_pic_layout:
			if(isApp.getConnectBleDevice()){
//				isApp.enterPicMode(true);
//				Message msg = isApp.getMainHandler().obtainMessage(22); 
//				isApp.getMainHandler().sendMessage(msg);
				
				plusdotBleDevice = PlusDotBLEDevice.getInstance();
				plusdotBleDevice.synchronizationPhotograph();

				Intent intent = new Intent(isApp, CameraActivity.class);
				startActivity(intent);
			}else{
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.disconnect), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.voice_ly:
			if(isApp.getConnectBleDevice()){
				plusdotBleDevice = PlusDotBLEDevice.getInstance();

				voiceImgBtn.setVisibility(View.VISIBLE);
				shakeImgBtn.setVisibility(View.INVISIBLE);
				voiceShakeImgBtn.setVisibility(View.INVISIBLE);
				changeReminderMode(Constant.REMINDER_VOICE_MODE);
			}else {
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.disconnect), Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.shake_ly:
			if(isApp.getConnectBleDevice()){
				plusdotBleDevice = PlusDotBLEDevice.getInstance();

				voiceImgBtn.setVisibility(View.INVISIBLE);
				shakeImgBtn.setVisibility(View.VISIBLE);
				voiceShakeImgBtn.setVisibility(View.INVISIBLE);
				changeReminderMode(Constant.REMINDER_SHAKE_MODE);
			}else {
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.disconnect), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.voice_shake_ly:
			if(isApp.getConnectBleDevice()){
				plusdotBleDevice = PlusDotBLEDevice.getInstance();
				
				voiceImgBtn.setVisibility(View.INVISIBLE);
				shakeImgBtn.setVisibility(View.INVISIBLE);
				voiceShakeImgBtn.setVisibility(View.VISIBLE);
				changeReminderMode(Constant.REMINDER_VOICE_SHAKE_MODE);
			}else {
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.disconnect), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.track_layout:
			startActivity(new Intent(getActivity(), TrackActivity.class));
			break;
		case R.id.clock_layout:
			startActivity(new Intent(getActivity(),SmartClockActivity.class));
			break;
		}
	}
	
	//////////////////////////// 拍照 /////////////////////////////////
	/**
	 * 建目录
	 * @param path
	 * @return
	 */
	private boolean mkdir(String path, String name){
		
		File file 		= new File(path+"temp_camera.jpg");
		File outfile 	= new File(path+ name + ".jpg");
		outUri			= Uri.fromFile(outfile);
		inUri			= Uri.fromFile(file);
		
		if(!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            if(!file.getParentFile().mkdirs()) {
                return false;
            }
            File nomedia = new File(path+".nomedia");
			if (!nomedia.exists()) {
				nomedia.mkdir();
			}
        }
		
		  try {
			  if(!outfile.exists()){
				  outfile.createNewFile();
			  }
	      } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	      } 
		
        if(file.exists()) {
            return false;
        }
		
	  try {
		if (file.createNewFile()) {
		      return true;
		}
	  } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  } 
	  
	  return false;
	}

	public static final int TAKE_CAMERA 		= 1;
	public static final int TAKE_PHOTO 		= 2;
	public static final int TAKE_RESET_IMAGE 	= 3;
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == TAKE_CAMERA && resultCode == Activity.RESULT_OK){
//			startImgResize(new Intent("com.android.camera.action.CROP"), inUri, outUri);
		}
	}

}
