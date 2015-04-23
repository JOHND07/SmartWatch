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
		mHolder.setFormat(PixelFormat.TRANSPARENT);// 设置背景透明
		setZOrderOnTop(true);
		paint.setAntiAlias(true);//消除锯齿
		


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
		
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR); // 清除画布
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
//		 * 画最外层的大圆环
//		 */
//		int centre = getWidth()/2;//获取圆心的x坐标
//		
//		int radius = (int)(centre - roundBackgroudWidth / 2); //圆环的半径
//	
//		paint.setColor(roundColor);//设置圆环的颜色
//		paint.setStyle(Paint.Style.STROKE);//设置空心
//		paint.setStrokeWidth(roundBackgroudWidth); //设置圆环的宽度
////		canvas.drawCircle(centre, centre, radius, paint); //画出圆环
//		
//		RectF rect = new RectF(centre - radius, centre - radius, centre
//				+ radius, centre + radius);  //用于定义的圆弧的形状和大小的界限
//		paint.setStyle(Paint.Style.STROKE);
//		canvas.drawArc(rect, startAngle, sweepAngle, false, paint);  //根据进度画圆弧
//		
//		/**
//		 * 画进度百分比
//		 */
//		paint.setStrokeWidth(0);
//		paint.setColor(textColor);
//		paint.setTypeface(Typeface.DEFAULT);//设置字体
//		int percent = (int)(((float)progress/ (float)max)*100f);//中间的进度百分比，先转换成float在进行除法运算，不然都为0
//		
//		if(textIsDisplayable && style != FILL){
//			if(title != null){
//				paint.setTextSize(titleTextSize);
//				float titleTextWidth = paint.measureText(title);
//				canvas.drawText(title, centre - titleTextWidth / 2, centre - textSize/2, paint); 
//			}
//		}
//	//	paint.setTypeface(Typeface.DEFAULT_BOLD);//设置字体
//		paint.setTextSize(textSize);
//		if(textIsDisplayable && style == STROKE){
//			float textWidth = paint.measureText(percent + uintString);
//			canvas.drawText(percent + uintString, centre - textWidth / 2, centre + textSize/2, paint); //画出进度百分比
//		}
//		
//		if(textIsDisplayable && style == STROKE_BLOCK){
//			float textWidth = paint.measureText(progressNum + uintString);
//			canvas.drawText( progressNum + uintString, centre - textWidth / 2, centre + textSize/2, paint); //画出进度百分比
//		}
//		
//		/**
//		 * 画圆弧 ，画圆环的进度
//		 */
//		
//		//设置进度是实心还是空心
//		paint.setStrokeWidth(roundWidth);
//		paint.setColor(roundProgressColor);
//		RectF oval = new RectF(centre - radius , centre - radius, centre
//				+ radius, centre + radius);  //用于定义的圆弧的形状和大小的界限
//		
//		switch (style) {
//		case STROKE:
//			paint.setStyle(Paint.Style.STROKE);
//			canvas.drawArc(oval, startAngle, sweepAngle * progress / max, false, paint);  //根据进度画圆弧
//			break;
//		case FILL:
//			paint.setStyle(Paint.Style.FILL_AND_STROKE);
//			if(progress !=0)
//				canvas.drawArc(oval, startAngle, sweepAngle * progress / max, true, paint);  //根据进度画圆弧
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
//			canvas.drawArc(oval, startAngleNum, roundBackgroudWidth, false, paint);  //根据进度画圆弧
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
