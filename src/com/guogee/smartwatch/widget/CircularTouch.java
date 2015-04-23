package com.guogee.smartwatch.widget;


import com.guogee.smartwatch.R;
import com.guogee.smartwatch.utils.Log;
import com.guogee.smartwatch.utils.Util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

public class CircularTouch extends AbsoluteLayout{
	
	private GestureListenerImpl mGestureListener;

	private GestureDetectorCompat mGestureDetector;
	private OnListener listener;
	//圆点，大圆半径，小圆半径
	private Point mCircularPoint;
	private int maxCircularRadius;
	private int minCircularRadius;
	//指示View  宽， 背景， 指示图偏移角度
	private View pointerView;
	private int pointerViewWidth;
	private Drawable pointerDrawable;
	private int pointerAngle;
	//点击有效否
	private boolean isTouching = false;
	//角度禁区
	private static int limitAngleMin = 361;
	private static int limitAngleMax = -1;
	// 字符串着色 大小
	private int textColor;
	private float textPointerSize;
	private float textSize;
	// 画笔对像引用
	private Paint paint;
	// 温度，默认23
//	private int temperature = 23;
	
	private int temperature = 10000;
	private String hint;
	private int[] location;
	// 1度   平均角度 
	private float averageAngle = 1;
	private boolean isOnLong = false;
	
	//刻度
//	public static final int CALIBRATION = 14;
//	public static final int INITNUM		= 16;
	
//	public static final int CALIBRATION = 14;
//	public static final int INITNUM		= 6;
	
	//以百步为单位
	public static final int CALIBRATION = 140;
	public static final int INITNUM		= 60;
	
	public CircularTouch(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularTouch);
		
		pointerDrawable = a.getDrawable(R.styleable.CircularTouch_pointerBackground);
		limitAngleMax	= a.getInteger(R.styleable.CircularTouch_limitAngleMax, -1);
		limitAngleMin	= a.getInteger(R.styleable.CircularTouch_limitAngleMin, 361);
		textColor 		= a.getColor(R.styleable.CircularTouch_CirculartextColor, Color.BLACK);
		textPointerSize	= a.getDimension(R.styleable.CircularTouch_textPointerSize, 22);
		textSize		= a.getDimension(R.styleable.CircularTouch_CirculartextSize, 18);
		hint			= a.getString(R.styleable.CircularTouch_hint);
		a.recycle();
		
		paint	= new Paint();
		paint.setAntiAlias(true);//消除锯齿
		paint.setColor(textColor);
		mGestureListener = new GestureListenerImpl();
		mGestureDetector = new GestureDetectorCompat(getContext(), mGestureListener);
		mGestureDetector.setIsLongpressEnabled(true);
	}

	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
		this.mCircularPoint		= new Point(w / 2, h / 2);
		this.pointerView 		= new ImageView(getContext());
		this.pointerViewWidth 	= pointerViewWidth();
		this.maxCircularRadius	= w / 2;
		this.minCircularRadius	= maxCircularRadius - pointerViewWidth;
		
//		if(pointerDrawable != null){
//			pointerView.setBackgroundDrawable(pointerDrawable);
//		}else{
//			pointerView.setBackgroundResource(R.drawable.zq_light_color_control);
//		}
		
		pointerView.setBackgroundResource(R.drawable.zq_light_color_control);
		matchLimitAngle(mCircularPoint, new Point(maxCircularRadius - pointerViewWidth/2 + maxCircularRadius, maxCircularRadius - pointerViewWidth/2));
		
		if((location == null) || (location[0] == -1) || (location[0] == -1)){
			location = new int[2];
			location[0] = mCircularPoint.x - pointerViewWidth /2;
			location[1] = 0 + getPaddingTop();
		}
		
		LayoutParams lp = new AbsoluteLayout.LayoutParams(
				AbsoluteLayout.LayoutParams.WRAP_CONTENT,      
				AbsoluteLayout.LayoutParams.WRAP_CONTENT,
				location[0],
				location[1]
        );
		
		pointerView.setLayoutParams(lp);
		addView(pointerView);
		requestLayout();
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		paint.setStrokeWidth(0);
		paint.setTypeface(Typeface.DEFAULT);//设置字体
//		paint.setTextSize(textPointerSize);
		
		paint.setTextSize(textPointerSize*2);
		
//		float tempWidth = paint.measureText(temperature + " ℃");
//		canvas.drawText(temperature + " ℃", maxCircularRadius - tempWidth / 2, maxCircularRadius - textPointerSize/2, paint);
//		paint.setTextSize(textSize);
//		float hintWidth = paint.measureText(hint);
//		canvas.drawText(hint, maxCircularRadius - hintWidth / 2, maxCircularRadius + textSize/2, paint);
		
		removeView(pointerView);
		LayoutParams lp = new AbsoluteLayout.LayoutParams(
				AbsoluteLayout.LayoutParams.WRAP_CONTENT,
				AbsoluteLayout.LayoutParams.WRAP_CONTENT,
				location[0],
				location[1]
        );
		
		pointerView.setLayoutParams(lp);
		addView(pointerView);		
		
		//demo test
//		float tempWidth = paint.measureText(temperature + "步");
//		paint = Util.overridePaintFonts(getContext(), paint);
//		canvas.drawText(temperature + "步", maxCircularRadius - tempWidth / 2, maxCircularRadius - textPointerSize/2, paint);
		
		
		float tempWidth = paint.measureText(Integer.toString(temperature));
		paint = Util.overridePaintFonts(getContext(), paint);
		canvas.drawText(Integer.toString(temperature) , maxCircularRadius - tempWidth / 2 + textPointerSize/2, maxCircularRadius + textPointerSize/2, paint);
		
		paint.setTextSize(textPointerSize/2);
//		canvas.drawText("步" , maxCircularRadius - tempWidth / 2+tempWidth*3/4, maxCircularRadius + textPointerSize/2, paint);
//		canvas.drawText("步" , maxCircularRadius - tempWidth / 2+tempWidth*4/5, maxCircularRadius + textPointerSize/2, paint);
		canvas.drawText(getResources().getString(R.string.step1) , maxCircularRadius - tempWidth / 2+tempWidth*4/5, maxCircularRadius + textPointerSize/2, paint);
		
		hint ="";
//	    if(temperature<10000){
//	    	hint = "菜鸟模式";
//	    }else {
//	    	hint = "砖家模式";
//	    }
		paint.setTextSize(textSize);
		float hintWidth = paint.measureText(hint);
		canvas.drawText(hint, maxCircularRadius - hintWidth / 2, maxCircularRadius + textSize*2, paint);
		//demo test
	}


	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		final float x 	= ev.getX();
		final float y 	= ev.getY();
		final int len 	= MathUtilsGetLength(mCircularPoint.x, mCircularPoint.y, ev.getX(), ev.getY());
		int angle;
		
		if (mGestureDetector.onTouchEvent(ev)) {
			return true;
		}
		
		switch (ev.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			//如果屏幕接触点不在摇杆挥动范围内,则不处理
			if((len > maxCircularRadius) || (len < minCircularRadius)){
				return false;
			}else{
				angle = pointAngleCouvert(mCircularPoint, new Point((int)x, (int)y));
				if((angle > (limitAngleMin + pointerAngle)) && (angle < (limitAngleMax - pointerAngle))){
					return false; //点到禁区内，
				}
				
				isTouching = true;
				drawPointerView((int)x, (int)y);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if(isTouching){
				drawPointerView((int)x, (int)y);
			}
			break;
		case MotionEvent.ACTION_UP:
			if(isTouching){
				drawPointerView((int)x, (int)y);
				if((listener != null) && !isOnLong){
					pointerView.setTag(temperature+"");
					listener.OnClickListener(pointerView);
				}
			}
			isTouching 	= false;
			isOnLong	= false;
			break;
		default:
			break;
		}
		
		return true;
	}
	
	public interface OnListener {
		public boolean OnLongClickListener(View view);
		public void OnClickListener(View view); 
	}
	
	/**
	 * 设置监听
	 * @param listener
	 */
	public void setOnListener(OnListener listener){
		this.listener = listener;
	}
	
	/**
	 * 当前温度
	 * @return
	 */
	public int getTemperature(){
		return temperature;
	}
	
	/**
	 * 当前温度
	 * @return
	 */
	public void setTemperature(int temperature){
		this.temperature = temperature;
	}
	
	/**
	 * 设置默认位置
	 * @param location
	 */
	public void setLocation(int[] location){
		this.location = location;
	}
	
	public int[] getLocation(){
		return this.location;
	}
	
	/**
	 * 匹配限制角度
	 * @param a
	 * @param b
	 */
	private void matchLimitAngle(Point a, Point b){
		pointerAngle = pointAngleCouvert(a, b);
		if((limitAngleMax <0) || (limitAngleMin > 360)){
			return;
		}
		limitAngleMax += pointerAngle;
		limitAngleMin -= pointerAngle;
		
		averageAngle = ((float)limitAngleMin + ((float)360 - (float)limitAngleMax)) / (float)CALIBRATION;
	}
	
	/**
	 * 两点偏移角度
	 * @param a
	 * @param b
	 * @return
	 */
	private int pointAngleCouvert(Point a, Point b){
		float radian = MathUtilsGetRadian(a, b);
		return MathUtilsGetAngleCouvert(radian);
	}
	
	/**
	 * 画指示圆
	 * @param x
	 * @param y
	 */
	public void drawPointerView(int x, int y){
		int angle;
		Point point;
		LayoutParams lp;
		Point tempPoint = new Point((int)x, (int)y);
		
		angle = pointAngleCouvert(mCircularPoint, tempPoint);
		
		if((angle > (limitAngleMin + pointerAngle)) && (angle < (limitAngleMax - pointerAngle))){
			return; //禁区内，不画
		}else if((angle > limitAngleMin)&& (angle <= (limitAngleMin + pointerAngle))){//最小处
			int mX = (int) ((maxCircularRadius - pointerViewWidth/2)*Math.cos(((double)limitAngleMin - (double)180)/(double)180*Math.PI));
			int mY = (int) ((maxCircularRadius - pointerViewWidth/2)*Math.sin(((double)limitAngleMin - (double)180)/(double)180*Math.PI));
			tempPoint.x = mCircularPoint.x - mX;
			tempPoint.y = mCircularPoint.y + mY;
			angle		= limitAngleMin;
		}else if((angle < limitAngleMax)&& (angle >= (limitAngleMax - pointerAngle))){//最大处
			int mX = (int) ((maxCircularRadius - pointerViewWidth/2)*Math.cos(((double)limitAngleMax - (double)270)/(double)180*Math.PI));
			int mY = (int) ((maxCircularRadius - pointerViewWidth/2)*Math.sin(((double)limitAngleMax - (double)270)/(double)180*Math.PI));
			tempPoint.x = mCircularPoint.x + mX;
			tempPoint.y = mCircularPoint.y + mY;
			angle		= limitAngleMax;
		}
		
		calculateTemperature(angle); //温度值
		
		point 	= MathUtilsGetBorderPoint(mCircularPoint, tempPoint, maxCircularRadius - pointerViewWidth/2);
		lp 		= (LayoutParams) pointerView.getLayoutParams();
		lp.x 	= point.x - pointerViewWidth/2;
		lp.y 	= point.y - pointerViewWidth/2;
		pointerView.setLayoutParams(lp);
		location[0] = lp.x;
		location[1] = lp.y;
		invalidate();
	}
	
	/**
	 * 计算温度值
	 * @param angle
	 */
	private void calculateTemperature(int angle){
		int tempAngle = 0;
		
		if(angle <= limitAngleMin){
			tempAngle = limitAngleMin - angle;
		}else if (angle >= limitAngleMax){
			tempAngle = limitAngleMin + (360 - angle);
		}
		
		temperature = (int)(((float)tempAngle / averageAngle) + INITNUM);
//		temperature = temperature*1000;
		
		temperature = temperature*100;
	}
	
	/**
	 * 计算指示图的宽度
	 * @return
	 */
	private int pointerViewWidth(){
		int width = 0;
		
		if(pointerDrawable != null){
			width = pointerDrawable.getIntrinsicWidth();
		}else{
			Bitmap mBitmap 	= BitmapFactory.decodeResource(getResources(), R.drawable.zq_light_color_control);
			width 			= mBitmap.getWidth();
			mBitmap.recycle();
		}
		
		return width;
	}
	
	/**
	 * 获取两点间直线距离
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static int MathUtilsGetLength(float x1, float y1, float x2, float y2){
		return (int)Math.sqrt(Math.pow(x1-x2, 2)+ Math.pow(y1-y2, 2));
	}
	
    /**
    * 获取线段上某个点的坐标，长度为a.x - cutRadius
    * @param a 点A
    * @param b 点B
    * @param cutRadius 截断距离
    * @return 截断点
    */
	public static Point MathUtilsGetBorderPoint(Point a, Point b, int cutRadius){
		float radian = MathUtilsGetRadian(a, b);
		
		return new Point(a.x + (int)(cutRadius * Math.cos(radian)),
				a.y + (int)(cutRadius * Math.sin(radian)));
		
	}
	
	/**
	 * 获取水平经夹解弧度
	 * @param a
	 * @param b
	 * @return
	 */
	public static float MathUtilsGetRadian (Point a, Point b){
		float lenA = b.x - a.x;
		float lenB = b.y - a.y;
		float lenC = (float)Math.sqrt(lenA*lenA + lenB*lenB);
		float ang = (float)Math.acos(lenA/lenC);
		ang = ang *(b.y < a.y? -1 : 1);
		return ang;
	}
	
	/**
	 * 获取偏移角度 0-360°
	 * @param radian
	 * @return
	 */
	public static int MathUtilsGetAngleCouvert(float radian){
		int tmp = (int)Math.round(radian/Math.PI*180);
		if(tmp<0){
			return -tmp;
		}else{
			return 180 + (180 - tmp);
		}
	}
	
	//手势 
	private final class GestureListenerImpl implements OnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}


		@Override
		public void onLongPress(MotionEvent ev) {
			final int len 	= MathUtilsGetLength(mCircularPoint.x, mCircularPoint.y, ev.getX(), ev.getY());
			if((len < maxCircularRadius) && (len > minCircularRadius)){
				if(listener != null){
					isOnLong	= true;
					pointerView.setTag(temperature+"");
					listener.OnLongClickListener(pointerView);
				}
			}
		}


		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}


		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}


		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

	}
}
