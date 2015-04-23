package com.guogee.smartwatch.ui;

import com.guogee.smartwatch.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class FindWatchSurface extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
	
	private Paint paint;
	
	private int backgroundColor;
	
	private Bitmap foundBg;	
	
	private Bitmap foundLight;
	
//	public int insideColor;
	
	private MyThread runnable;
	
	public FindWatchSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		backgroundColor = getResources().getColor(R.color.background_color);
    	foundBg=((BitmapDrawable)getResources().getDrawable(R.drawable.found_search_bg)).getBitmap();
    	foundLight=((BitmapDrawable)getResources().getDrawable(R.drawable.found_search_light)).getBitmap();
    	
		paint 		= new Paint();
		mHolder		= getHolder();
		mHolder.addCallback(this);
		mHolder.setFormat(PixelFormat.TRANSPARENT);// ���ñ���͸��
		setZOrderOnTop(true);
		paint.setAntiAlias(true);//�������
		


		Log.d("TAG", "foundBg 's width ============= "+foundBg.getWidth());
		Log.d("TAG", "foundBg 's height ============= "+foundBg.getHeight());
	
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,  int height) {
		// TODO Auto-generated method stub
		onDrawView();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		runnable = new MyThread();
//		runnable.isRun = true;
		new Thread(runnable).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		runnable.isRun = false;
	}
	
	//////
	private int degrees = 0;
	
	private synchronized void onDrawView(){

		// TODO Auto-generated method stub
		Canvas canvas = null;
		
		Log.d("TAG", "onDrawView ....................... ");
		
		try{
			
		canvas = mHolder.lockCanvas();
		if(canvas == null)return;
		
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR); // �������
		canvas.drawBitmap(foundBg, (FoundActivity.screenWidth-foundBg.getWidth())/2, 0, paint);
		
//		Matrix matrix = new Matrix();
//		matrix.
		
		if(degrees<360){
			degrees++;
		}else{
			degrees = 0;
		}
		canvas.rotate(degrees, (FoundActivity.screenWidth-foundBg.getWidth())/2 + foundBg.getWidth()/2, foundBg.getHeight()/2);
//		canvas.drawBitmap(bitmap, matrix, paint);
		canvas.drawBitmap(foundLight, (FoundActivity.screenWidth-foundBg.getWidth())/2, 0, paint);
		
//		/**
//		 * �������Ĵ�Բ��
//		 */
//		int centre = getWidth()/2;//��ȡԲ�ĵ�x����
//		
//		int radius = (int)(centre - roundBackgroudWidth / 2); //Բ���İ뾶
//	
//		paint.setColor(roundColor);//����Բ������ɫ
//		paint.setStyle(Paint.Style.STROKE);//���ÿ���
//		paint.setStrokeWidth(roundBackgroudWidth); //����Բ���Ŀ��
////		canvas.drawCircle(centre, centre, radius, paint); //����Բ��
//		
//		RectF rect = new RectF(centre - radius, centre - radius, centre
//				+ radius, centre + radius);  //���ڶ����Բ������״�ʹ�С�Ľ���
//		paint.setStyle(Paint.Style.STROKE);
//		canvas.drawArc(rect, startAngle, sweepAngle, false, paint);  //���ݽ��Ȼ�Բ��
//		
//		/**
//		 * �����Ȱٷֱ�
//		 */
//		paint.setStrokeWidth(0);
//		paint.setColor(textColor);
//		paint.setTypeface(Typeface.DEFAULT);//��������
//		int percent = (int)(((float)progress/ (float)max)*100f);//�м�Ľ��Ȱٷֱȣ���ת����float�ڽ��г������㣬��Ȼ��Ϊ0
//		
//		if(textIsDisplayable && style != FILL){
//			if(title != null){
//				paint.setTextSize(titleTextSize);
//				float titleTextWidth = paint.measureText(title);
//				canvas.drawText(title, centre - titleTextWidth / 2, centre - textSize/2, paint); 
//			}
//		}
//	//	paint.setTypeface(Typeface.DEFAULT_BOLD);//��������
//		paint.setTextSize(textSize);
//		if(textIsDisplayable && style == STROKE){
//			float textWidth = paint.measureText(percent + uintString);
//			canvas.drawText(percent + uintString, centre - textWidth / 2, centre + textSize/2, paint); //�������Ȱٷֱ�
//		}
//		
//		if(textIsDisplayable && style == STROKE_BLOCK){
//			float textWidth = paint.measureText(progressNum + uintString);
//			canvas.drawText( progressNum + uintString, centre - textWidth / 2, centre + textSize/2, paint); //�������Ȱٷֱ�
//		}
//		
//		/**
//		 * ��Բ�� ����Բ���Ľ���
//		 */
//		
//		//���ý�����ʵ�Ļ��ǿ���
//		paint.setStrokeWidth(roundWidth);
//		paint.setColor(roundProgressColor);
//		RectF oval = new RectF(centre - radius , centre - radius, centre
//				+ radius, centre + radius);  //���ڶ����Բ������״�ʹ�С�Ľ���
//		
//		switch (style) {
//		case STROKE:
//			paint.setStyle(Paint.Style.STROKE);
//			canvas.drawArc(oval, startAngle, sweepAngle * progress / max, false, paint);  //���ݽ��Ȼ�Բ��
//			break;
//		case FILL:
//			paint.setStyle(Paint.Style.FILL_AND_STROKE);
//			if(progress !=0)
//				canvas.drawArc(oval, startAngle, sweepAngle * progress / max, true, paint);  //���ݽ��Ȼ�Բ��
//			break;
//		case STROKE_BLOCK:
//			paint.setStyle(Paint.Style.STROKE);
//			
//			float tmep			= sweepAngle * progress / max;
//			float sweepAngleNum = startAngle + tmep;
//			float startAngleNum = sweepAngleNum - roundBackgroudWidth;
//			if(startAngleNum > 360){
//				startAngleNum -= 360;
//			}
//			canvas.drawArc(oval, startAngleNum, roundBackgroudWidth, false, paint);  //���ݽ��Ȼ�Բ��
//			break;
//		default:
//			break;
//		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(canvas != null){
				mHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	
	/////////////////////
	
   class MyThread implements Runnable  {
		
        public boolean isRun ;
        
        public  MyThread(){  
            isRun = true;  
        }  

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			while(isRun){
				try {
					onDrawView();
					Thread.sleep(10);
				} catch (InterruptedException e) {
						e.printStackTrace();
				}catch(NullPointerException e){
						e.printStackTrace();
				}
			}
		}  
        
    }

}
