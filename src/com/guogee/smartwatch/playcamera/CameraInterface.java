package com.guogee.smartwatch.playcamera;

import java.io.IOException;
import java.util.List;

import no.nordicsemi.android.log.LogContract.Session.Content;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.hardware.Camera.CameraInfo;

public class CameraInterface {
	private static final String TAG = "yanzi";
	private Camera mCamera;
	private Camera.Parameters mParams;
	private boolean isPreviewing = false;
	private float mPreviwRate = -1f;
	private static CameraInterface mCameraInterface;

	public interface CamOpenOverCallback{
		public void cameraHasOpened();
	}

	private CameraInterface(){

	}
	public static synchronized CameraInterface getInstance(){
		if(mCameraInterface == null){
			mCameraInterface = new CameraInterface();
		}
		return mCameraInterface;
	}
	/**打开Camera
	 * @param callback
	 */
	public void doOpenCamera(CamOpenOverCallback callback){
		Log.i(TAG, "Camera open....");
		mCamera = Camera.open();
		Log.i(TAG, "Camera open over....");
		callback.cameraHasOpened();
	}
	/**开启预览
	 * @param holder
	 * @param previewRate
	 */
	public void doStartPreview(SurfaceHolder holder, float previewRate){
		Log.i(TAG, "doStartPreview...");
		if(isPreviewing){
			mCamera.stopPreview();
			return;
		}
		if(mCamera != null){

			mParams = mCamera.getParameters();
			mParams.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式
			CamParaUtil.getInstance().printSupportPictureSize(mParams);
			CamParaUtil.getInstance().printSupportPreviewSize(mParams);
			//设置PreviewSize和PictureSize
			Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
					mParams.getSupportedPictureSizes(),previewRate, 800);
			mParams.setPictureSize(pictureSize.width, pictureSize.height);
			Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
					mParams.getSupportedPreviewSizes(), previewRate, 800);
			mParams.setPreviewSize(previewSize.width, previewSize.height);

			mCamera.setDisplayOrientation(90);

			CamParaUtil.getInstance().printSupportFocusMode(mParams);
			List<String> focusModes = mParams.getSupportedFocusModes();
			if(focusModes.contains("continuous-video")){
				mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			}
			mCamera.setParameters(mParams);	

			try {
				mHolder = holder;
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();//开启预览
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			isPreviewing = true;
			mPreviwRate = previewRate;

			mParams = mCamera.getParameters(); //重新get一次
			Log.i(TAG, "最终设置:PreviewSize--With = " + mParams.getPreviewSize().width
					+ "Height = " + mParams.getPreviewSize().height);
			Log.i(TAG, "最终设置:PictureSize--With = " + mParams.getPictureSize().width
					+ "Height = " + mParams.getPictureSize().height);
		}
	}
	/**
	 * 停止预览，释放Camera
	 */
	public void doStopCamera(){
		if(null != mCamera)
		{
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview(); 
			isPreviewing = false; 
			mPreviwRate = -1f;
			mCamera.release();
			mCamera = null;     
		}
	}
	
	private ContentResolver mResolver;
	
	private Context mContext;
	
	/**
	 * 拍照
	 */
	public void doTakePicture(ContentResolver resolver,Context context){
		
		mResolver = resolver;
		
		mContext = context;
		
		if(isPreviewing && (mCamera != null)){
			mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
		}
	}

	/*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
	ShutterCallback mShutterCallback = new ShutterCallback() 
	//快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
	{
		public void onShutter() {
			// TODO Auto-generated method stub
			Log.i(TAG, "myShutterCallback:onShutter...");
		}
	};
	PictureCallback mRawCallback = new PictureCallback() 
	// 拍摄的未压缩原数据的回调,可以为null
	{
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Log.i(TAG, "myRawCallback:onPictureTaken...");

		}
	};
	PictureCallback mJpegPictureCallback = new PictureCallback() 
	//对jpeg图像数据的回调,最重要的一个回调
	{
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Log.i(TAG, "myJpegCallback:onPictureTaken...");
			Bitmap b = null;
			if(null != data){
				b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
				mCamera.stopPreview();
				isPreviewing = false;
			}
			//保存图片到sdcard
			if(null != b)
			{
				//设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
				//图片竟然不能旋转了，故这里要旋转下
				Bitmap rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
				
//				MediaStore.Images.Media.insertImage(mResolver, rotaBitmap, "", ""); 
//				Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);     
//				Uri uri = Uri.fromFile(new File("/sdcard/image.jpg"));     
//				intent.setData(uri);      
//				mContext.sendBroadcast(intent); 
				
				FileUtil.saveBitmap(rotaBitmap);
			}
			//再次进入预览
			mCamera.startPreview();
			isPreviewing = true;
		}
	};
	
	//add by john
	private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头
	
	private SurfaceHolder mHolder;
	
	public void switchCamera(){
		 int cameraCount = 0;
         CameraInfo cameraInfo = new CameraInfo();
         cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

         for(int i = 0; i < cameraCount; i++   ) {
             Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
             if(cameraPosition == 1) {
                 //现在是后置，变更为前置
                 if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置  
                	 mCamera.stopPreview();//停掉原来摄像头的预览
                	 mCamera.release();//释放资源
                	 mCamera = null;//取消原来摄像头
                	 mCamera = Camera.open(i);//打开当前选中的摄像头
                     try {
                    	 mCamera.setDisplayOrientation(90);
                    	 mCamera.setPreviewDisplay(mHolder);//通过surfaceview显示取景画面
                     } catch (IOException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                     }
                     mCamera.startPreview();//开始预览
                     cameraPosition = 0;
                     break;
                 }
             } else {
                 //现在是前置， 变更为后置
                 if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置  
                	 mCamera.stopPreview();//停掉原来摄像头的预览
                	 mCamera.release();//释放资源
                	 mCamera = null;//取消原来摄像头
                     mCamera = Camera.open(i);//打开当前选中的摄像头
                     try {
                    	 mCamera.setDisplayOrientation(90);
                    	 mCamera.setPreviewDisplay(mHolder);//通过surfaceview显示取景画面
                     } catch (IOException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                     }
                     mCamera.startPreview();//开始预览
                     cameraPosition = 1;
                     break;
                 }
             }

	 }
	}


}
