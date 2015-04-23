package com.guogee.smartwatch.widget;


import com.guogee.smartwatch.R;
import com.guogee.smartwatch.utils.Util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SleepProgressBarSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
	
	private SurfaceHolder mHolder;
	/**
	 * ���ʶ�������
	 */
	private Paint paint;
	
	private Paint paint2;
	
	
	/**
	 * Բ������ɫ
	 */
	private int roundColor;
	/**
	 * Բ��������ɫ
	 */
	private int roundProgressColor;
	/**
	 * �м���Ȱٷֱ��ַ�����ɫ
	 */
	private int textColor;
	/**
	 * �м���Ȱٷֱ��ִ�������
	 */
	private float textSize;
	/**
	 * title
	 */
	private float titleTextSize;
	/**
	 * Բ���Ŀ��
	 */
	private float roundWidth;
	/**
	 * Բ������ style = STROKE_BLOCK  ��Ч    else ��Ч
	 */
	private float roundLength;
	/**
	 * ����Բ�����
	 */
	private float roundBackgroudWidth;
	/**
	 * �����Ƕ�
	 */
	private float startAngle;
	/**
	 * ���Ƕ�
	 */
	private float sweepAngle;
	/**
	 * ������
	 */
	private int max;
	/**
	 * ���ȵ�λ
	 */
	private String uint;
	/**
	 * ��ǰ����
	 */
	private int progress;
	
	/**
	 * ����ֵ
	 */
	private String progressNum = "0";
	/**
	 * title
	 */
	private String title;
	/**
	 * �Ƿ���ʾ�м�Ľ���
	 */
	private boolean textIsDisplayable;
	/**
	 * ���ȵķ��ʵ�� ���߿���
	 */
	private int style;
	
	public static final int STROKE 			= 0;
	public static final int FILL			= 1;
	public static final int STROKE_BLOCK 	= 2;
	
	
	private int deepSleepColor;
	
	private int lightSleepColor;
	
	private int wakeUpColor;
	
	
	public void setDeepSleepColor(int color){
		deepSleepColor = color;
	}
	
	public void setLightSleepColor(int color){
		lightSleepColor = color;
	}
	
	public void setWakeColor(int color){
		wakeUpColor = color;
	}
	
	public SleepProgressBarSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint 		= new Paint();
		mHolder		= getHolder();
		mHolder.addCallback(this);
		mHolder.setFormat(PixelFormat.TRANSPARENT);// ���ñ���͸��
		setZOrderOnTop(true);
		paint.setAntiAlias(true);//�������
		
		
		paint2 = new Paint();
		paint2.setAntiAlias(true);
		
		
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,R.styleable.RoundProgressBar);
		
		//��ȡ�Զ������Ժ�Ĭ��ֵ
		roundColor 			= mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.WHITE);
		roundProgressColor 	= mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
		textColor 			= mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.GREEN);

		
		
		textSize 			= mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 18);
		roundWidth 			= mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
		roundBackgroudWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundBackgroudWidth, 5);
		roundLength			= mTypedArray.getDimension(R.styleable.RoundProgressBar_roundLength, 20);
//		startAngle			= mTypedArray.getDimension(R.styleable.RoundProgressBar_roundStartAngle, 150);
//		sweepAngle			= mTypedArray.getDimension(R.styleable.RoundProgressBar_roundSweepAngle, 240);
		
		startAngle			= mTypedArray.getDimension(R.styleable.RoundProgressBar_roundStartAngle, 140);
		sweepAngle			= mTypedArray.getDimension(R.styleable.RoundProgressBar_roundSweepAngle, 260);
		
		max 				= mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
		textIsDisplayable 	= mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
		style 				= mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
		title				= mTypedArray.getString(R.styleable.RoundProgressBar_title);
		titleTextSize 		= mTypedArray.getDimension(R.styleable.RoundProgressBar_titleTextSize, 10);
		
		mTypedArray.recycle();
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		onDrawView();
//		onDrawBackGround();
//		onDrawDeepSleep();
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		if(style == STROKE_BLOCK)
			progress = getMinProgress();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
	}
	
	
	private synchronized void onDrawBackGround(){
		Canvas canvas = null;
		progress = 140;
		try{
			
			canvas = mHolder.lockCanvas();
			mCanvas = canvas;
			if(canvas == null)return;
			
//			canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR); // �������
			
			/**
			 * �������Ĵ�Բ��
			 */
			int centre = getWidth()/2;//��ȡԲ�ĵ�x����
			int radius = (int)(centre - roundBackgroudWidth / 2); //Բ���İ뾶
			paint.setColor(roundColor);//����Բ������ɫ
			paint.setStyle(Paint.Style.STROKE);//���ÿ���
			paint.setStrokeWidth(roundBackgroudWidth); //����Բ���Ŀ��
			
			RectF rect = new RectF(centre - radius, centre - radius, centre	+ radius, centre + radius);  //���ڶ����Բ������״�ʹ�С�Ľ���
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(rect, startAngle, sweepAngle, false, paint);  //���ݽ��Ȼ�Բ��
			
			
			//draw deep sleep
			progress = 80;
//			if(progress<=60){
				paint.setColor(deepSleepColor);
				canvas.drawArc(rect, startAngle, sweepAngle * progress / max, false, paint);  //���ݽ��Ȼ�Բ��
//			}
			
			//draw light sleep
//			if(progress > 60){
				progress = 80;
				startAngle = 140+sweepAngle/3;
				paint.setColor(roundProgressColor);
				canvas.drawArc(rect, startAngle, sweepAngle * progress / max, false, paint);  //���ݽ��Ȼ�Բ��
//			}

				progress = 80;
				startAngle = 140+sweepAngle*160/240;
				paint.setColor(textColor);
				canvas.drawArc(rect, startAngle, sweepAngle * progress / max, false, paint);  //���ݽ��Ȼ�Բ��
				
			//draw wake up
			
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(canvas != null){
					mHolder.unlockCanvasAndPost(canvas);
				}
			}
	}
	
	private Canvas mCanvas ;
	
	private synchronized void onDrawDeepSleep(){
		Canvas canvas = null;
		try{
			
			progress = 60;
			
			canvas = mHolder.lockCanvas();
			if(canvas == null)return;
			
//			canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR); // �������
			
//			/**
//			 * �������Ĵ�Բ��
//			 */
			/****/
			int centre = getWidth()/2;//��ȡԲ�ĵ�x����
			int radius = (int)(centre - roundBackgroudWidth / 2); //Բ���İ뾶
			
//			paint2.setColor(textColor);//����Բ������ɫ
			paint2.setStyle(Paint.Style.STROKE);//���ÿ���
			paint2.setStrokeWidth(roundBackgroudWidth); //����Բ���Ŀ��
			
//			RectF rect = new RectF(centre - radius-10, centre - radius-10, centre	+ radius, centre + radius);  //���ڶ����Բ������״�ʹ�С�Ľ���
			paint2.setStyle(Paint.Style.STROKE);
//			canvas.drawArc(rect, startAngle, sweepAngle, false, paint);  //���ݽ��Ȼ�Բ��
			
			RectF oval2 = new RectF(centre - radius , centre - radius , centre
					+ radius, centre + radius);  //���ڶ����Բ������״�ʹ�С�Ľ���

			paint2.setColor(textColor);
			canvas.drawArc(oval2, startAngle, sweepAngle * progress / max, false, paint2);  //���ݽ��Ȼ�Բ��	
			
			
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(canvas != null){
					mHolder.unlockCanvasAndPost(canvas);
				}
			}
	}
	
	private synchronized void onDrawLightSleep(){
		Canvas canvas = null;
		try{
			
            progress = 80;
			
			canvas = mHolder.lockCanvas();
			if(canvas == null)return;
			
			canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR); // �������
			
//			/**
//			 * �������Ĵ�Բ��
//			 */
			int centre = getWidth()/2;//��ȡԲ�ĵ�x����
			int radius = (int)(centre - roundBackgroudWidth / 2); //Բ���İ뾶
			
//			paint.setColor(textColor);//����Բ������ɫ
			paint.setStyle(Paint.Style.STROKE);//���ÿ���
			paint.setStrokeWidth(roundBackgroudWidth); //����Բ���Ŀ��
			
//			RectF rect = new RectF(centre - radius-10, centre - radius-10, centre	+ radius, centre + radius);  //���ڶ����Բ������״�ʹ�С�Ľ���
			paint.setStyle(Paint.Style.STROKE);
//			canvas.drawArc(rect, startAngle, sweepAngle, false, paint);  //���ݽ��Ȼ�Բ��
			
			RectF oval2 = new RectF(centre - radius , centre - radius , centre
					+ radius, centre + radius);  //���ڶ����Բ������״�ʹ�С�Ľ���

			paint.setColor(textColor);
			canvas.drawArc(oval2, startAngle, sweepAngle * progress / max, false, paint);  //���ݽ��Ȼ�Բ��
			
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(canvas != null){
					mHolder.unlockCanvasAndPost(canvas);
				}
			}
	}
	
	private synchronized void onDrawWakeUpSleep(){
		Canvas canvas = null;
		try{
			
			canvas = mHolder.lockCanvas();
			if(canvas == null)return;
			
			canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR); // �������
			
			/**
			 * �������Ĵ�Բ��
			 */
			int centre = getWidth()/2;//��ȡԲ�ĵ�x����
			int radius = (int)(centre - roundBackgroudWidth / 2); //Բ���İ뾶
			paint.setColor(roundColor);//����Բ������ɫ
			paint.setStyle(Paint.Style.STROKE);//���ÿ���
			paint.setStrokeWidth(roundBackgroudWidth); //����Բ���Ŀ��
			
			RectF rect = new RectF(centre - radius-30, centre - radius-30, centre	+ radius, centre + radius);  //���ڶ����Բ������״�ʹ�С�Ľ���
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(rect, startAngle, sweepAngle, false, paint);  //���ݽ��Ȼ�Բ��
			
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(canvas != null){
					mHolder.unlockCanvasAndPost(canvas);
				}
			}
	}


	private synchronized void onDrawView() {
		// TODO Auto-generated method stub
		Canvas canvas = null;
		try{
			
		canvas = mHolder.lockCanvas();
		if(canvas == null)return;
		
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR); // �������
		
		String uintString = (uint == null) ? "%" : uint; 
		
		/**
		 * �������Ĵ�Բ��
		 */
		int centre = getWidth()/2;//��ȡԲ�ĵ�x����
		int radius = (int)(centre - roundBackgroudWidth / 2); //Բ���İ뾶
		paint.setColor(roundColor);//����Բ������ɫ
		paint.setStyle(Paint.Style.STROKE);//���ÿ���
		paint.setStrokeWidth(roundBackgroudWidth); //����Բ���Ŀ��
//		canvas.drawCircle(centre, centre, radius, paint); //����Բ��
		
		RectF rect = new RectF(centre - radius, centre - radius, centre	+ radius, centre + radius);  //���ڶ����Բ������״�ʹ�С�Ľ���
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawArc(rect, startAngle, sweepAngle, false, paint);  //���ݽ��Ȼ�Բ��
		
//		paint.setStyle(Paint.Style.FILL);
//		canvas.drawCircle(77, 380, (float)24.5, paint);
//		canvas.drawCircle(404, 380, (float)24.5, paint);
		
		/**
		 * �����Ȱٷֱ�
		 */
		paint.setStrokeWidth(0);
		paint.setColor(textColor);
//		paint.setTypeface(Typeface.DEFAULT);//��������
		int percent = (int)(((float)progress/ (float)max)*100f);//�м�Ľ��Ȱٷֱȣ���ת����float�ڽ��г������㣬��Ȼ��Ϊ0
		
		//draw "�ҵ�˯��" 
		if(textIsDisplayable && style != FILL){
			if(title != null){
				paint.setStyle(Paint.Style.FILL);//���ÿ���
				paint.setTextSize(32);
				float titleTextWidth = paint.measureText(title);
//				canvas.drawText(title, centre - titleTextWidth / 2, centre - textSize*2, paint); 
			}
		}
		
		if(textIsDisplayable && style == STROKE){
			
			paint.setStyle(Paint.Style.FILL);//���ÿ���
			paint.setTextSize(textSize);
		}
		
		if(textIsDisplayable && style == STROKE_BLOCK){
			float textWidth = paint.measureText(progressNum + uintString);
			canvas.drawText( progressNum + uintString, centre - textWidth / 2, centre + textSize/2, paint); //�������Ȱٷֱ�
		}
		
		/**
		 * ��Բ�� ����Բ���Ľ���
		 */
		//���ý�����ʵ�Ļ��ǿ���
		paint.setStrokeWidth(roundWidth);
		paint.setColor(roundProgressColor);
		RectF oval = new RectF(centre - radius , centre - radius, centre
				+ radius, centre + radius);  //���ڶ����Բ������״�ʹ�С�Ľ���
		
		switch (style) {
		case STROKE:
			paint.setStyle(Paint.Style.STROKE);
//			Log.d("TAG", "progress ================ "+progress);
//			Log.d("TAG", "max ================ "+max);
//			Log.d("TAG", "startAngle ================ "+startAngle);
//			Log.d("TAG", "sweepAngle ================ "+sweepAngle * progress / max);
			canvas.drawArc(oval, startAngle, sweepAngle * progress / max, false, paint);  //���ݽ��Ȼ�Բ��

			
//			if(progress<100){
//				RectF oval2 = new RectF(centre - radius - 20 , centre - radius - 20, centre
//						+ radius, centre + radius);  //���ڶ����Բ������״�ʹ�С�Ľ���
//
//				paint.setColor(textColor);
//				canvas.drawArc(oval2, startAngle, sweepAngle * progress / max, false, paint);  //���ݽ��Ȼ�Բ��	
//			}else {
//				RectF oval3 = new RectF(centre - radius , centre - radius, centre
//						+ radius, centre + radius);  //���ڶ����Բ������״�ʹ�С�Ľ���
//				
//				paint.setColor(roundProgressColor);
//				canvas.drawArc(oval3, startAngle, sweepAngle * progress / max, false, paint);  //���ݽ��Ȼ�Բ��	
//			}
			
			
//			if(progress>0 && progress<240){
//				paint.setColor(textColor);
//				paint.setStyle(Paint.Style.FILL);//���ÿ���
//				canvas.drawCircle(77, 380, (float)20, paint);
//			}else if(progress==240){
//				paint.setStyle(Paint.Style.FILL);//���ÿ���
//				canvas.drawCircle(404, 380, (float)20, paint);
//				canvas.drawCircle(77, 380, (float)20, paint);
//			}
			break;
		case FILL:
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			if(progress !=0)
				canvas.drawArc(oval, startAngle, sweepAngle * progress / max, true, paint);  //���ݽ��Ȼ�Բ��
			break;
		case STROKE_BLOCK:
			paint.setStyle(Paint.Style.STROKE);
			
			float tmep			= sweepAngle * progress / max;
			float sweepAngleNum = startAngle + tmep;
			float startAngleNum = sweepAngleNum - roundBackgroudWidth;
			if(startAngleNum > 360){
				startAngleNum -= 360;
			}
			canvas.drawArc(oval, startAngleNum, roundBackgroudWidth, false, paint);  //���ݽ��Ȼ�Բ��
			break;
		default:
			break;
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(canvas != null){
				mHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	//add by john
	public void rotate3D(){
		Canvas canvas = null;
		try{
		canvas = mHolder.lockCanvas();
		if(canvas == null)return;
		
//		Paint paint = new Paint();  
//        //canvas.drawBitmap(rotateImg, 0, 0, paint); //������ת�ı���  
//        //�����������ͼƬ��ת��ƽ��  
//        Matrix matrix = new Matrix();  
//        //������ת�Ƕ�  
//        matrix.postRotate(360,  
//        		canvas.getWidth() / 2, canvas.getHeight() / 2);  
//        //������߾���ϱ߾�  
//        matrix.postTranslate(0, 0);  
//        //������תͼƬ  
        
		int centre = getWidth()/2;
        canvas.rotate(360, centre/2, centre/2);
//        canvas.drawBitmap(rotateImg, matrix, paint); 
        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(canvas != null){
				mHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	//add by john
	
	public synchronized int getMax(){
		return max;
	}
	/**
	 * ���ý��ȵ����ֵ
	 * @param max
	 */
	public synchronized void setMax(int max){
		if(max < 0){
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}
	
	/**
	 * ��ȡ����.��Ҫͬ��
	 * @return
	 */
	public synchronized int getProgress() {
		return progress;
	}
	/**
	 * ���ý��ȣ���Ϊ�̰߳�ȫ�ؼ������ڿ��Ƕ��ߵ����⣬��Ҫͬ��
	 * ˢ�½������postInvalidate()���ڷ�UI�߳�ˢ��
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
		
		//test 
//		progress = 240;
		if(progress < 0){
			throw new IllegalArgumentException("progress not less than 0");
		}
		
		if(progress > max){
			progress = max;
		}
		if(progress <= max){
			this.progress = progress;
			onDrawView();
		}
	}
	
	public String getTitle(){
		return this.title;
	}

	public int getCricleColor() {
		return roundColor;
	}

	public void setCricleColor(int cricleColor) {
		this.roundColor = cricleColor;
	}

	public int getCricleProgressColor() {
		return roundProgressColor;
	}

	public void setCricleProgressColor(int cricleProgressColor) {
		this.roundProgressColor = cricleProgressColor;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public float getRoundWidth() {
		return roundWidth;
	}

	public void setRoundWidth(float roundWidth) {
		this.roundWidth = roundWidth;
	}
	/*
	 * 	STROKE 			= 0;
	 *  FILL			= 1; 
	 *  STROKE_BLOCK 	= 2;
	 */
	public void setStyle(int style){
		this.style = style;
	}
	public int getStyle(){
		return this.style;
	}
	/**
	 * style = STROKE_BLOCK  ����Ч
	 * @return
	 */
	public int getMinProgress(){
		float num = sweepAngle / (float)max;
		
		return (int)(roundBackgroudWidth / num)+1;
	}
	
	/**
	 * ����ֵ��
	 * style = STROKE_BLOCK  ����Ч
	 * @param num
	 */
	public void setProgressNum(int num){
		this.progressNum = num + "";
	}
	/**
	 * style = STROKE_BLOCK  ����Ч
	 * style = STROKE  ����Ч
	 * @param title
	 */
	public void setTitle(String title){
		this.title = title;
	}
	/**
	 * ���ȵ�λ
	 * @param uint
	 */
	public void setProgressUnit(String uint){
		this.uint = uint;
	}
}
