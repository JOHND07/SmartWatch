package com.guogee.smartwatch.ui;

import java.io.FileOutputStream;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.view.MotionEvent;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback, Camera.PictureCallback{

	private SurfaceHolder holder;
	private Camera camera;
	private boolean af;

	public CameraView(Context context) {// ���캯��
		super(context);

		holder = getHolder();// ����Surface Holder
		holder.addCallback(this);

		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// ָ��Push Buffer
	}

	public void surfaceCreated(SurfaceHolder holder) {// Surface�����¼��Ĵ���
		try {
			camera = Camera.open();// ����ͷ�ĳ�ʼ��
			camera.setPreviewDisplay(holder);
		} catch (Exception e) {
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {// Surface�ı��¼��Ĵ���
		Camera.Parameters parameters = camera.getParameters();
		parameters.setPreviewSize(width, height);
		camera.setParameters(parameters);// ���ò���
		camera.startPreview();// ��ʼԤ��
	}

	public void surfaceDestroyed(SurfaceHolder holder) {// Surface����ʱ�Ĵ���
		camera.setPreviewCallback(null);
		camera.stopPreview();
		camera.release();
		camera = null;
	}

	@Override
    public boolean onTouchEvent(MotionEvent event) {//��Ļ�����¼�
        if (event.getAction() == MotionEvent.ACTION_DOWN) {//����ʱ�Զ��Խ�
            camera.autoFocus(null);
            af =true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP && af ==true) {//�ſ�������
            camera.takePicture(null, null, this);
            af =false;
        }
        return true;
    }

	public void onPictureTaken(byte[] data, Camera camera) {// ������ɺ󱣴���Ƭ
		try {
			String path = Environment.getExternalStorageDirectory()
					+ "/test.jpg";
			data2file(data, path);
		} catch (Exception e) {
		}
		camera.startPreview();
	}

	private void data2file(byte[] w, String fileName) throws Exception {// ������������ת��Ϊ�ļ��ĺ���
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileName);
			out.write(w);
			out.close();
		} catch (Exception e) {
			if (out != null)
				out.close();
			throw e;
		}
	}
}
