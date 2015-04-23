package com.guogee.smartwatch.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Animation.AnimationListener;

/**
 *  ����������
 * @author john
 */
public class DehooAnimationUtils {
	
	/**
	 * 3D��ת����
	 * @param start
	 * 			��ʼ��ת�ĽǶ�
	 * @param end
	 * 			������ת�ĽǶ�
	 * @param view
	 * 			Ҫ��ת��View
	 */
	public static void apply3DRotation(float start, float end, final View view) {
		// Find the center of the container
		final float centerX = view.getWidth() / 2.0f;
		final float centerY = view.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true); // 310.0f �����������ת��ʱ����Z�᷽���������
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());

		// ��������
		rotation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				view.post(new Runnable() {
					@Override
					public void run() {
						final float centerX = view.getWidth() / 2.0f;
						final float centerY = view.getHeight() / 2.0f;
						Rotate3dAnimation rotation;
						// mImageView.requestFocus();
						rotation = new Rotate3dAnimation(270, 360, centerX, centerY, 310.0f, false); // 180,  360  ��Ϊ��������������ʵ��360��ת����
						rotation.setDuration(200);
						rotation.setFillAfter(true);
						rotation.setInterpolator(new DecelerateInterpolator());
						view.startAnimation(rotation);
					}
				});
				
			}
		});
		// ��������
		view.startAnimation(rotation);
	}
	
	/**
	 * View�Ľ��뽥������
	 * @param context ���ô˷�����������
	 * @param view ����������View
	 * @param animationType �������ͣ�ָ���������� 
	 * 			����MessageModel�Ķ�Ӧ��������
	 */
	public static void slideAnimation(Context context, final View view , int animationType){}
	
	/**
	 * ��ת����
	 * @param fromDegrees ��ʼ�Ƕ�
	 * @param toDegrees �����Ƕ�
	 */
	public static void myAnim(Context context, float fromDegrees, float toDegrees, AnimationListener listener){
		RotateAnimation anim = null;
		//��������
		anim=new RotateAnimation(fromDegrees, toDegrees,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
		//���ö���ִ��ʱ��
		anim.setDuration(1000);
		//���ö���ִ�д���
		anim.setRepeatCount(0);
		//���ö�����Ⱦ��
		anim.setInterpolator(AnimationUtils.loadInterpolator(context, android.R.anim.accelerate_interpolator));
		//���ö�������ʱֹͣ�ڽ�����λ��
		anim.setFillAfter(true);
		//��������
		anim.setAnimationListener(listener);
	}
	
}
