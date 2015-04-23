package com.guogee.smartwatch.playcamera;


import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.ble.StatusChangedCallback;
import com.guogee.smartwatch.playcamera.CameraInterface.CamOpenOverCallback;
import com.guogee.smartwatch.utils.Log;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.Toast;

public class CameraActivity extends Activity implements CamOpenOverCallback {
	private static final String TAG = "TAG";
	CameraSurfaceView surfaceView = null;
	ImageButton shutterBtn;
	ImageButton switterBtn;
	float previewRate = -1f;
	
	private iSmartApplication isApp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Thread openThread = new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				CameraInterface.getInstance().doOpenCamera(CameraActivity.this);
			}
		};
		openThread.start();
		setContentView(R.layout.activity_camera);
		initUI();
		initViewParams();
		
		isApp = (iSmartApplication)getApplication();
        isApp.addCallBack(new UIStatusChangedCallback());
        
		shutterBtn.setOnClickListener(new BtnListeners());
		switterBtn.setOnClickListener(new BtnListeners());
		
//		isApp.enterPicMode(true);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		isApp.enterPicMode(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

	private void initUI(){
		surfaceView = (CameraSurfaceView)findViewById(R.id.camera_surfaceview);
		shutterBtn = (ImageButton)findViewById(R.id.btn_shutter);
		switterBtn = (ImageButton)findViewById(R.id.btn_switter);
	}
	
	private void initViewParams(){
		LayoutParams params = surfaceView.getLayoutParams();
		Point p = DisplayUtil.getScreenMetrics(this);
		params.width = p.x;
		params.height = p.y;
		previewRate = DisplayUtil.getScreenRate(this); //默认全屏的比例预览
		surfaceView.setLayoutParams(params);

		//手动设置拍照ImageButton的大小为120dip×120dip,原图片大小是64×64
		LayoutParams p2 = shutterBtn.getLayoutParams();
		p2.width = DisplayUtil.dip2px(this, 80);
		p2.height = DisplayUtil.dip2px(this, 80);		
		shutterBtn.setLayoutParams(p2);	
	}

	@Override
	public void cameraHasOpened() {
		// TODO Auto-generated method stub
		SurfaceHolder holder = surfaceView.getSurfaceHolder();
		CameraInterface.getInstance().doStartPreview(holder, previewRate);
	}
	
	private class BtnListeners implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btn_shutter:
				CameraInterface.getInstance().doTakePicture(getContentResolver(),CameraActivity.this);  
//				MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "", ""); 
				Toast.makeText(CameraActivity.this, getResources().getString(R.string.pic_path), Toast.LENGTH_SHORT).show();
				break;
			case R.id.btn_switter:
				CameraInterface.getInstance().switchCamera();
				break;
			default:break;
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////////////
	public Handler uihandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MainActivity.MSG_TAKE_PIC:
				Log.d("TAG", "CameraActivity 's take pic...................");
				CameraInterface.getInstance().doTakePicture(getContentResolver(),CameraActivity.this);
				Toast.makeText(CameraActivity.this, getResources().getString(R.string.pic_path), Toast.LENGTH_SHORT).show();
				break;
			case MainActivity.MSG_BLE_DISCONNECTED:
				Toast.makeText(CameraActivity.this, getResources().getString(R.string.disconnect), Toast.LENGTH_SHORT).show();
				break;
			}
		}
    };
	
	class UIStatusChangedCallback implements StatusChangedCallback{
		@Override
		public void callbackCall(int command, byte[] dataPacket) {
			// TODO Auto-generated method stub
			Log.d("TAG", "CameraActivity 's callbackCall..................."+command);
			Message msg = uihandler.obtainMessage(command); 
			uihandler.sendMessage(msg);
		}

		@Override
		public void callbackFail(int command, byte[] dataPacket) {
			// TODO Auto-generated method stub
			Log.d("TAG", "CameraActivity 's callbackFail..................."+command);
			Message msg = uihandler.obtainMessage(command); 
			uihandler.sendMessage(msg);
		}
	}	


}
