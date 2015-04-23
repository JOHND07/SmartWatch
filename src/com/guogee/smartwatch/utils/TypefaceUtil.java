package com.guogee.smartwatch.utils;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceUtil {
	private static Typeface mFace;// 昆仑字体类型
	private static Typeface zhsFace;// 张海山字体类型
	private Context context;

	public TypefaceUtil(Context context) {
		if (context != null)
			this.context = context;
	}

	/***
	 * 昆仑细圆_bak_999");//中文
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
	 * 张海山锐线体简 数字和英文
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