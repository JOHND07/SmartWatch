package com.guogee.smartwatch.utils;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceUtil {
	private static Typeface mFace;// ������������
	private static Typeface zhsFace;// �ź�ɽ��������
	private Context context;

	public TypefaceUtil(Context context) {
		if (context != null)
			this.context = context;
	}

	/***
	 * ����ϸԲ_bak_999");//����
	 * @return 
	 */
	public Typeface getmFaceKunlun() {
		if (mFace == null) {
			return mFace = Typeface.createFromAsset(context.getAssets(),
					Constant.KUNLUN);
		}
		return mFace;
	}

	/***
	 * �ź�ɽ������� ���ֺ�Ӣ��
	 * 
	 * @param context
	 * @return
	 */
	public Typeface getFaceZhang() {
		if (zhsFace == null) {
			return zhsFace = Typeface.createFromAsset(context.getAssets(),
					Constant.ZHANGHS);
		}
		return zhsFace;
	}
}