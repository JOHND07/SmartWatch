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
 * switch�ؼ���ʹ��
 */
public class Switch extends ImageView {
	
    private static final String TAG = "Switch";
    private static final int TEXT_SIZE = 12;//���ذ�ť��OFF��ON�����ֵ����壬�ڲ�ͬ�ֱ��ʵ��ֻ��ϴ�С����һ��
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
        // init desity dpi��֮ǰ������Ļ�ܶȵ�ʱ��������⣬ȥ���ܶȣ���ʵ�ʵĿ�͸ߵı������������ֵ�λ��
//        mCheckedString = context.getResources().getString(R.string.switch_off);//ҵ���ܿ���ʱ��ͼƬ��ʾ�����ַ�������ON
//        mUnCheckedString = context.getResources().getString(R.string.switch_on );//OFF
//        setBackgroundResource(R.drawable.white_btn);//���ÿ��ذ�ť�ı���
        setChecked(false);//��ʼ���ؼ�Ϊ�ص�״̬
        configPaint();//���û������ԣ�ȥ���ܶȣ�ֱ��������12��ʾ��
    }


    private void configPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//ȥ���־��
        mPaint.setTextSize(TEXT_SIZE);
        mPaint.setColor(Color.WHITE);//���������������������ɫ
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
        if (mChecked) {
            setImageResource(R.drawable.setting_switch_on);
        } else {
            setImageResource(R.drawable.setting_switch_off);
        }
        postInvalidate();
            //��http://blog.csdn.net/sdhjob/article/details/6512973
            //View��invalidate��postInvalidate���Ǹ��½��棬��ͬ����
            //invalidate�봴��VIEW������ͬһ���߳���ִ�еģ������̣߳�����ڷ����߳���ִ�еĻ���Ҫ���Handlerʹ��
            //��postInvalidate�����ڷ����߳��и���UI����

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
    //��VIEW��ʱ��ʼ���ĵ�(0,0)�������Ͻǣ�ͼƬ���������»��������ֵ�ʱ��ʼ���ĵ��������½ǣ����ִ��������ϻ�
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

