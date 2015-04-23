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
	/**��Camera
	 * @param callback
	 */
	public void doOpenCamera(CamOpenOverCallback callback){
		Log.i(TAG, "Camera open....");
		mCamera = Camera.open();
		Log.i(TAG, "Camera open over....");
		callback.cameraHasOpened();
	}
	/**����Ԥ��
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
			mParams.setPictureFormat(PixelFormat.JPEG);//�������պ�洢��ͼƬ��ʽ
			CamParaUtil.getInstance().printSupportPictureSize(mParams);
			CamParaUtil.getInstance().printSupportPreviewSize(mParams);
			//����PreviewSize��PictureSize
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
				mCamera.startPreview();//����Ԥ��
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			isPreviewing = true;
			mPreviwRate = previewRate;

			mParams = mCamera.getParameters(); //����getһ��
			Log.i(TAG, "��������:PreviewSize--With = " + mParams.getPreviewSize().width
					+ "Height = " + mParams.getPreviewSize().height);
			Log.i(TAG, "��������:PictureSize--With = " + mParams.getPictureSize().width
					+ "Height = " + mParams.getPictureSize().height);
		}
	}
	/**
	 * ֹͣԤ�����ͷ�Camera
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
	 * ����
	 */
	public void doTakePicture(ContentResolver resolver,Context context){
		
		mResolver = resolver;
		
		mContext = context;
		
		if(isPreviewing && (mCamera != null)){
			mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
		}
	}

	/*Ϊ��ʵ�����յĿ������������ձ�����Ƭ��Ҫ���������ص�����*/
	ShutterCallback mShutterCallback = new ShutterCallback() 
	//���Ű��µĻص������������ǿ����������Ʋ��š����ꡱ��֮��Ĳ�����Ĭ�ϵľ������ꡣ
	{
		public void onShutter() {
			// TODO Auto-generated method stub
			Log.i(TAG, "myShutterCallback:onShutter...");
		}
	};
	PictureCallback mRawCallback = new PictureCallback() 
	// �����δѹ��ԭ���ݵĻص�,����Ϊnull
	{
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Log.i(TAG, "myRawCallback:onPictureTaken...");

		}
	};
	PictureCallback mJpegPictureCallback = new PictureCallback() 
	//��jpegͼ�����ݵĻص�,����Ҫ��һ���ص�
	{
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Log.i(TAG, "myJpegCallback:onPictureTaken...");
			Bitmap b = null;
			if(null != data){
				b = BitmapFactory.decodeByteArray(data, 0, data.length);//data���ֽ����ݣ����������λͼ
				mCamera.stopPreview();
				isPreviewing = false;
			}
			//����ͼƬ��sdcard
			if(null != b)
			{
				//����FOCUS_MODE_CONTINUOUS_VIDEO)֮��myParam.set("rotation", 90)ʧЧ��
				//ͼƬ��Ȼ������ת�ˣ�������Ҫ��ת��
				Bitmap rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
				
//				MediaStore.Images.Media.insertImage(mResolver, rotaBitmap, "", ""); 
//				Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);     
//				Uri uri = Uri.fromFile(new File("/sdcard/image.jpg"));     
//				intent.setData(uri);      
//				mContext.sendBroadcast(intent); 
				
				FileUtil.saveBitmap(rotaBitmap);
			}
			//�ٴν���Ԥ��
			mCamera.startPreview();
			isPreviewing = true;
		}
	};
	
	//add by john
	private int cameraPosition = 1;//0����ǰ������ͷ��1������������ͷ
	
	private SurfaceHolder mHolder;
	
	public void switchCamera(){
		 int cameraCount = 0;
         CameraInfo cameraInfo = new CameraInfo();
         cameraCount = Camera.getNumberOfCameras();//�õ�����ͷ�ĸ���

         for(int i = 0; i < cameraCount; i++   ) {
             Camera.getCameraInfo(i, cameraInfo);//�õ�ÿһ������ͷ����Ϣ
             if(cameraPosition == 1) {
                 //�����Ǻ��ã����Ϊǰ��
                 if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {//��������ͷ�ķ�λ��CAMERA_FACING_FRONTǰ��      CAMERA_FACING_BACK����  
                	 mCamera.stopPreview();//ͣ��ԭ������ͷ��Ԥ��
                	 mCamera.release();//�ͷ���Դ
                	 mCamera = null;//ȡ��ԭ������ͷ
                	 mCamera = Camera.open(i);//�򿪵�ǰѡ�е�����ͷ
                     try {
                    	 mCamera.setDisplayOrientation(90);
                    	 mCamera.setPreviewDisplay(mHolder);//ͨ��surfaceview��ʾȡ������
                     } catch (IOException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                     }
                     mCamera.startPreview();//��ʼԤ��
                     cameraPosition = 0;
                     break;
                 }
             } else {
                 //������ǰ�ã� ���Ϊ����
                 if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {//��������ͷ�ķ�λ��CAMERA_FACING_FRONTǰ��      CAMERA_FACING_BACK����  
                	 mCamera.stopPreview();//ͣ��ԭ������ͷ��Ԥ��
                	 mCamera.release();//�ͷ���Դ
                	 mCamera = null;//ȡ��ԭ������ͷ
                     mCamera = Camera.open(i);//�򿪵�ǰѡ�е�����ͷ
                     try {
                    	 mCamera.setDisplayOrientation(90);
                    	 mCamera.setPreviewDisplay(mHolder);//ͨ��surfaceview��ʾȡ������
                     } catch (IOException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                     }
                     mCamera.startPreview();//��ʼԤ��
                     cameraPosition = 1;
                     break;
                 }
             }

	 }
	}


}