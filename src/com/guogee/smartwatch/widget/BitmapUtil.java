package com.guogee.smartwatch.widget;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

public class BitmapUtil {

	public byte[] getBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	public static Bitmap smallBitmap(Bitmap bitmap , float scale) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	public static Bitmap scaleBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	public static Bitmap getBitmap(String name) {
		String path = Environment.getExternalStorageDirectory().getPath()+ "/Chaoke/" + name;
		System.out.println(Environment.getExternalStorageDirectory().getPath()+ "/Chaoke/" + name);
		return BitmapFactory.decodeFile(path);
	}

	public static void save(Bitmap bm, String path) {
		if (bm == null)
			return;
		try {
			File file = new File(path);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists())
				file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			bm.compress(CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getScalBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		float scale = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;
		matrix.postScale(scale, scale);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	public static String getSDPath() {
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			String sdDir = Environment.getExternalStorageDirectory().getPath();
			return sdDir;
		}
		return null;
	}

	public static void saveBitmap(Bitmap bitmap) {
		String sdStatus = Environment.getExternalStorageDirectory().getPath();
		FileOutputStream b = null;
		File file = new File(sdStatus + "/Chaoke");
		file.mkdirs();// 创建文件夹，名称为Chaoke
		String fileName = sdStatus + "/Chaoke/" + "share.jpg";
		try {
			b = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}