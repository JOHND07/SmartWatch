package com.guogee.smartwatch.widget;

import com.guogee.smartwatch.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
/**
 * switch控件的使用
 */
public class Switch extends ImageView {
	
    private static final String TAG = "Switch";
    private static final int TEXT_SIZE = 12;//开关按钮上OFF和ON的文字的字体，在不同分辨率的手机上大小都不一样
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private boolean mChecked;
    private Paint mPaint;
    private String mCheckedString, mUnCheckedString;
    public interface OnCheckedChangeListener {
        public void onCheckedChanged(Switch switchView, boolean isChecked);
    }

    public Switch(Context context) {
        this(context, null);
    }

    public Switch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Switch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // init desity dpi，之前引用屏幕密度的时候出现问题，去掉密度，按实际的宽和高的比例来设置文字的位置
//        mCheckedString = context.getResources().getString(R.string.switch_off);//业务功能开启时的图片显示文字字符串：即ON
//        mUnCheckedString = context.getResources().getString(R.string.switch_on );//OFF
//        setBackgroundResource(R.drawable.white_btn);//设置开关按钮的背景
        setChecked(false);//初始化控件为关的状态
        configPaint();//设置画布属性（去掉密度，直接以字体12显示）
    }


    private void configPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//去文字锯齿
        mPaint.setTextSize(TEXT_SIZE);
        mPaint.setColor(Color.WHITE);//画布背景即文字字体的颜色
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
        if (mChecked) {
            setImageResource(R.drawable.setting_switch_on);
        } else {
            setImageResource(R.drawable.setting_switch_off);
        }
        postInvalidate();
            //见http://blog.csdn.net/sdhjob/article/details/6512973
            //View的invalidate和postInvalidate都是更新界面，不同的是
            //invalidate与创建VIEW都是在同一个线程中执行的，即主线程，如果在非主线程中执行的话需要配合Handler使用
            //而postInvalidate可以在非主线程中更新UI界面

    }

    public boolean isChecked() {
        return mChecked;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mChecked) {
        	//canvas.drawText(mCheckedString, (getWidth()*0.6f), getHeight()*0.7f, mPaint);
        } else { 
        	//canvas.drawText(mUnCheckedString, getWidth()*0.1f , getHeight()*0.7f, mPaint);
        }
    //画VIEW的时候开始画的点(0,0)是在左上角，图片从左上往下画，画文字的时候开始画的点是在左下角，文字从左下往上画
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                setChecked(!mChecked);
                if (mOnCheckedChangeListener != null) {
                    mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
                }
                break;
            default:
                // Do nothing
                break;
        }

        return true;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

}

