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
	//Բ�㣬��Բ�뾶��СԲ�뾶
	private Point mCircularPoint;
	private int maxCircularRadius;
	private int minCircularRadius;
	//ָʾView  �� ������ ָʾͼƫ�ƽǶ�
	private View pointerView;
	private int pointerViewWidth;
	private Drawable pointerDrawable;
	private int pointerAngle;
	//�����Ч��
	private boolean isTouching = false;
	//�ǶȽ���
	private static int limitAngleMin = 361;
	private static int limitAngleMax = -1;
	// �ַ�����ɫ ��С
	private int textColor;
	private float textPointerSize;
	private float textSize;
	// ���ʶ�������
	private Paint paint;
	// �¶ȣ�Ĭ��23
//	private int temperature = 23;
	
	private int temperature = 10000;
	private String hint;
	private int[] location;
	// 1��   ƽ���Ƕ� 
	private float averageAngle = 1;
	private boolean isOnLong = false;
	
	//�̶�
//	public static final int CALIBRATION = 14;
//	public static final int INITNUM		= 16;
	
//	public static final int CALIBRATION = 14;
//	public static final int INITNUM		= 6;
	
	//�԰ٲ�Ϊ��λ
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
		paint.setAntiAlias(true);//�������
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
		paint.setTypeface(Typeface.DEFAULT);//��������
//		paint.setTextSize(textPointerSize);
		
		paint.setTextSize(textPointerSize*2);
		
//		float tempWidth = paint.measureText(temperature + " ��");
//		canvas.drawText(temperature + " ��", maxCircularRadius - tempWidth / 2, maxCircularRadius - textPointerSize/2, paint);
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
//		float tempWidth = paint.measureText(temperature + "��");
//		paint = Util.overridePaintFonts(getContext(), paint);
//		canvas.drawText(temperature + "��", maxCircularRadius - tempWidth / 2, maxCircularRadius - textPointerSize/2, paint);
		
		
		float tempWidth = paint.measureText(Integer.toString(temperature));
		paint = Util.overridePaintFonts(getContext(), paint);
		canvas.drawText(Integer.toString(temperature) , maxCircularRadius - tempWidth / 2 + textPointerSize/2, maxCircularRadius + textPointerSize/2, paint);
		
		paint.setTextSize(textPointerSize/2);
//		canvas.drawText("��" , maxCircularRadius - tempWidth / 2+tempWidth*3/4, maxCircularRadius + textPointerSize/2, paint);
//		canvas.drawText("��" , maxCircularRadius - tempWidth / 2+tempWidth*4/5, maxCircularRadius + textPointerSize/2, paint);
		canvas.drawText(getResources().getString(R.string.step1) , maxCircularRadius - tempWidth / 2+tempWidth*4/5, maxCircularRadius + textPointerSize/2, paint);
		
		hint ="";
//	    if(temperature<10000){
//	    	hint = "����ģʽ";
//	    }else {
//	    	hint = "ש��ģʽ";
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
			//�����Ļ�Ӵ��㲻��ҡ�˻Ӷ���Χ��,�򲻴���
			if((len > maxCircularRadius) || (len < minCircularRadius)){
				return false;
			}else{
				angle = pointAngleCouvert(mCircularPoint, new Point((int)x, (int)y));
				if((angle > (limitAngleMin + pointerAngle)) && (angle < (limitAngleMax - pointerAngle))){
					return false; //�㵽�����ڣ�
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
	 * ���ü���
	 * @param listener
	 */
	public void setOnListener(OnListener listener){
		this.listener = listener;
	}
	
	/**
	 * ��ǰ�¶�
	 * @return
	 */
	public int getTemperature(){
		return temperature;
	}
	
	/**
	 * ��ǰ�¶�
	 * @return
	 */
	public void setTemperature(int temperature){
		this.temperature = temperature;
	}
	
	/**
	 * ����Ĭ��λ��
	 * @param location
	 */
	public void setLocation(int[] location){
		this.location = location;
	}
	
	public int[] getLocation(){
		return this.location;
	}
	
	/**
	 * ƥ�����ƽǶ�
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
	 * ����ƫ�ƽǶ�
	 * @param a
	 * @param b
	 * @return
	 */
	private int pointAngleCouvert(Point a, Point b){
		float radian = MathUtilsGetRadian(a, b);
		return MathUtilsGetAngleCouvert(radian);
	}
	
	/**
	 * ��ָʾԲ
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
			return; //�����ڣ�����
		}else if((angle > limitAngleMin)&& (angle <= (limitAngleMin + pointerAngle))){//��С��
			int mX = (int) ((maxCircularRadius - pointerViewWidth/2)*Math.cos(((double)limitAngleMin - (double)180)/(double)180*Math.PI));
			int mY = (int) ((maxCircularRadius - pointerViewWidth/2)*Math.sin(((double)limitAngleMin - (double)180)/(double)180*Math.PI));
			tempPoint.x = mCircularPoint.x - mX;
			tempPoint.y = mCircularPoint.y + mY;
			angle		= limitAngleMin;
		}else if((angle < limitAngleMax)&& (angle >= (limitAngleMax - pointerAngle))){//���
			int mX = (int) ((maxCircularRadius - pointerViewWidth/2)*Math.cos(((double)limitAngleMax - (double)270)/(double)180*Math.PI));
			int mY = (int) ((maxCircularRadius - pointerViewWidth/2)*Math.sin(((double)limitAngleMax - (double)270)/(double)180*Math.PI));
			tempPoint.x = mCircularPoint.x + mX;
			tempPoint.y = mCircularPoint.y + mY;
			angle		= limitAngleMax;
		}
		
		calculateTemperature(angle); //�¶�ֵ
		
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
	 * �����¶�ֵ
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
	 * ����ָʾͼ�Ŀ��
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
	 * ��ȡ�����ֱ�߾���
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
    * ��ȡ�߶���ĳ��������꣬����Ϊa.x - cutRadius
    * @param a ��A
    * @param b ��B
    * @param cutRadius �ضϾ���
    * @return �ضϵ�
    */
	public static Point MathUtilsGetBorderPoint(Point a, Point b, int cutRadius){
		float radian = MathUtilsGetRadian(a, b);
		
		return new Point(a.x + (int)(cutRadius * Math.cos(radian)),
				a.y + (int)(cutRadius * Math.sin(radian)));
		
	}
	
	/**
	 * ��ȡˮƽ���н⻡��
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
	 * ��ȡƫ�ƽǶ� 0-360��
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
	
	//���� 
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
