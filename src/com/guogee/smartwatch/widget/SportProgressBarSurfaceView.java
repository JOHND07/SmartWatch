package com.guogee.smartwatch.widget;


import com.guogee.smartwatch.R;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.utils.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SportProgressBarSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
	
	private SurfaceHolder mHolder;
	/**
	 * 画笔对像引用
	 */
	private Paint paint;
	/**
	 * 圆环的着色
	 */
	private int roundColor;
	/**
	 * 圆环进度着色
	 */
	private int roundProgressColor;
	/**
	 * 中间进度百分比字符串着色
	 */
	private int textColor;
	/**
	 * 中间进度百分比字串的字体
	 */
	private float textSize;
	/**
	 * title
	 */
	private float titleTextSize;
	/**
	 * 圆环的宽度
	 */
	private float roundWidth;
	/**
	 * 圆环长度 style = STROKE_BLOCK  有效    else 无效
	 */
	private float roundLength;
	/**
	 * 背景圆环宽度
	 */
	private float roundBackgroudWidth;
	/**
	 * 起启角度
	 */
	private float startAngle;
	/**
	 * 最大角度
	 */
	private float sweepAngle;
	/**
	 * 最大进度
	 */
	private int max;
	/**
	 * 进度单位
	 */
	private String uint;
	/**
	 * 当前进度
	 */
	private int progress;
	
	/**
	 * 进度值
	 */
	private String progressNum = "0";
	/**
	 * title
	 */
	private String title;
	/**
	 * 是否显示中间的进度
	 */
	private boolean textIsDisplayable;
	/**
	 * 进度的风格，实心 或者空心
	 */
	private int style;
	
	public static final int STROKE 			= 0;
	public static final int FILL			= 1;
	public static final int STROKE_BLOCK 	= 2;
	private SharedPreferences sp;
	
	public SportProgressBarSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		
		paint 		= new Paint();
		mHolder		= getHolder();
		mHolder.addCallback(this);
		mHolder.setFormat(PixelFormat.TRANSPARENT);// 设置背景透明
		setZOrderOnTop(true);
		paint.setAntiAlias(true);//消除锯齿
		
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,R.styleable.RoundProgressBar);
		
		//获取自定义属性和默认值
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

	private synchronized void onDrawView() {
		// TODO Auto-generated method stub
		Canvas canvas = null;
		try{
			
		canvas = mHolder.lockCanvas();
		if(canvas == null)return;
		
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR); // 清除画布
		String uintString = (uint == null) ? "%" : uint; 
		
		/**
		 * 画最外层的大圆环
		 */
		int centre = getWidth()/2;//获取圆心的x坐标
		int radius = (int)(centre - roundBackgroudWidth / 2); //圆环的半径
	
		paint.setColor(roundColor);//设置圆环的颜色
		paint.setStyle(Paint.Style.STROKE);//设置空心
		paint.setStrokeWidth(roundBackgroudWidth); //设置圆环的宽度
//		canvas.drawCircle(centre, centre, radius, paint); //画出圆环

		RectF rect = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawArc(rect, startAngle, sweepAngle, false, paint);  //根据进度画圆弧

//		paint.setStyle(Paint.Style.FILL);
//		canvas.drawCircle(77, 380, (float)24.5, paint);
//		canvas.drawCircle(404, 380, (float)24.5, paint);
		
		/**
		 * 画进度百分比
		 */
		paint.setStrokeWidth(0);
		paint.setColor(textColor);
//		paint.setTypeface(Typeface.DEFAULT);//设置字体
		int percent = (int)(((float)progress/ (float)max)*100f);//中间的进度百分比，先转换成float在进行除法运算，不然都为0
		
		//draw "我的运动" 
		if(textIsDisplayable && style != FILL){
			if(title != null){
//				title = "我的运动";
				title = getResources().getString(R.string.my_sport);
				paint.setStyle(Paint.Style.FILL);//设置空心
				paint.setTextSize(32);
				float titleTextWidth = paint.measureText(title);
//				canvas.drawText(title, centre - titleTextWidth / 2, centre - textSize*2, paint); 
			}
		}
		
//		//draw “目标 1000 步” 上面的那条横向
//		float titleTextWidth2 = paint.measureText(title);
//		canvas.drawLine(centre - titleTextWidth2 / 2, centre + textSize, centre + titleTextWidth2 / 2, centre + textSize, paint);
		
		//draw “目标 1000 步”
		if(textIsDisplayable && style != FILL){
			if(title != null){

				title = "目标:"+sp.getInt(Constant.NUM_KEY, 10000)+"步";
				paint.setStyle(Paint.Style.FILL);//设置空心
				paint.setTextSize(34);
				float titleTextWidth = paint.measureText(title);
				
				//draw “目标 1000 步” 上面的那条横向
//				float titleTextWidth2 = paint.measureText(title);
//				canvas.drawLine(centre - titleTextWidth / 2, centre + textSize , centre + titleTextWidth / 2, centre + textSize, paint);
//				canvas.drawText(title, centre - titleTextWidth / 2, centre + textSize*2, paint); 
			}
		}
		
	//	paint.setTypeface(Typeface.DEFAULT_BOLD);//设置字体
		paint.setTextSize(textSize);
		if(textIsDisplayable && style == STROKE){
//			float textWidth = paint.measureText(percent + uintString);
//			canvas.drawText(percent + uintString, centre - textWidth / 2, centre + textSize/2, paint); //画出进度百分比
			
			//demo test  - 100
			paint.setTextSize(textSize*2);
			float textWidth = paint.measureText("412步");
			paint = Util.overridePaintFonts(getContext(), paint);
//			canvas.drawText("412", centre - textWidth / 2 + 70, centre + textSize/2, paint); //画出进度百分比
			paint.setTextSize(textSize/2);
			float textWidth2 = paint.measureText("412");
//			canvas.drawText("步", centre + textWidth / 2 - textWidth2 - 55, centre + textSize/2, paint); //画出进度百分比
			
			//demo test  10
//			paint.setTextSize(textSize*2);
//			float textWidth = paint.measureText("41步");
//			paint = Util.overridePaintFonts(getContext(), paint);
//			canvas.drawText("41", centre - textWidth / 2 + 70, centre + textSize/2, paint); //画出进度百分比
//			paint.setTextSize(textSize/2);
//			float textWidth2 = paint.measureText("41");
//			canvas.drawText("步", centre + textWidth / 2 - textWidth2 - 55, centre + textSize/2, paint); //画出进度百分比

			
			//demo test 1000
//			paint.setTextSize(textSize*2);
//			float textWidth = paint.measureText("4120步");
//			paint = Util.overridePaintFonts(getContext(), paint);
//			canvas.drawText("4120", centre - textWidth / 2 + 80, centre + textSize/2, paint); //画出进度百分比
//			paint.setTextSize(textSize/2);
//			float textWidth2 = paint.measureText("4120");
//			canvas.drawText("步", centre + textWidth / 2 - textWidth2 - 55, centre + textSize/2, paint); //画出进度百分比

			
			//demo test 10000
//			paint.setTextSize(textSize*2);
//			float textWidth = paint.measureText("12000步");
//			paint = Util.overridePaintFonts(getContext(), paint);
//			canvas.drawText("12000", centre - textWidth / 2 +80, centre + textSize/2, paint); //画出进度百分比
//			paint.setTextSize(textSize/2);
//			float textWidth2 = paint.measureText("12000");
//			canvas.drawText("步", centre + textWidth / 2 - textWidth2-60, centre + textSize/2, paint); //画出进度百分比
			
			//demo test
		}
		
		if(textIsDisplayable && style == STROKE_BLOCK){
			float textWidth = paint.measureText(progressNum + uintString);
//			canvas.drawText( progressNum + uintString, centre - textWidth / 2, centre + textSize/2, paint); //画出进度百分比
		}
		
		/**
		 * 画圆弧 ，画圆环的进度
		 */
		//设置进度是实心还是空心
		paint.setStrokeWidth(roundWidth);
		paint.setColor(roundProgressColor);
		RectF oval = new RectF(centre - radius , centre - radius, centre + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限
		
		switch (style) {
		case STROKE:
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(oval, startAngle, sweepAngle * progress / max, false, paint);  //根据进度画圆弧
			
//			if(progress>0 && progress<240){
//				paint.setStyle(Paint.Style.FILL);//设置空心
//				canvas.drawCircle(77, 380, (float)20, paint);
//			}else if(progress==240){
//				paint.setStyle(Paint.Style.FILL);//设置空心
//				canvas.drawCircle(404, 380, (float)20, paint);
//				canvas.drawCircle(77, 380, (float)20, paint);
//			}
			
			break;
		case FILL:
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			if(progress !=0)
				canvas.drawArc(oval, startAngle, sweepAngle * progress / max, true, paint);  //根据进度画圆弧
			break;
		case STROKE_BLOCK:
			paint.setStyle(Paint.Style.STROKE);
			
			float tmep			= sweepAngle * progress / max;
			float sweepAngleNum = startAngle + tmep;
			float startAngleNum = sweepAngleNum - roundBackgroudWidth;
			if(startAngleNum > 360){
				startAngleNum -= 360;
			}
			canvas.drawArc(oval, startAngleNum, roundBackgroudWidth, false, paint);  //根据进度画圆弧
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
	
	public synchronized int getMax(){
		return max;
	}
	/**
	 * 设置进度的最大值
	 * @param max
	 */
	public synchronized void setMax(int max){
		if(max < 0){
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}
	
	/**
	 * 获取进度.需要同步
	 * @return
	 */
	public synchronized int getProgress() {
		return progress;
	}
	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
	 * 刷新界面调用postInvalidate()能在非UI线程刷新
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
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
	/**
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
	 * style = STROKE_BLOCK  才有效
	 * @return
	 */
	public int getMinProgress(){
		float num = sweepAngle / (float)max;
		
		return (int)(roundBackgroudWidth / num)+1;
	}
	
	/**
	 * 进度值，
	 * style = STROKE_BLOCK  才有效
	 * @param num
	 */
	public void setProgressNum(int num){
		this.progressNum = num + "";
	}
	/**
	 * style = STROKE_BLOCK  才有效
	 * style = STROKE  才有效
	 * @param title
	 */
	public void setTitle(String title){
		this.title = title;
	}
	/**
	 * 进度单位
	 * @param uint
	 */
	public void setProgressUnit(String uint){
		this.uint = uint;
	}
	
}
