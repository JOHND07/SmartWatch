package com.guogee.smartwatch.playcamera;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class FileUtil {
	private static final  String TAG = "FileUtil";
	private static final File parentPath = Environment.getExternalStorageDirectory();
	private static   String storagePath = "";
//	private static final String DST_FOLDER_NAME = "PlayCamera";

	/**初始化保存路径
	 * @return
	 */
	private static String initPath(){
		if(storagePath.equals("")){
//			storagePath = parentPath.getAbsolutePath()+"/" + DST_FOLDER_NAME;
			storagePath = parentPath.getAbsolutePath()+"/" + "/"+"DCIM"+"/"+"Camera";
			File f = new File(storagePath);
			if(!f.exists()){
				f.mkdir();
			}
		}
		return storagePath;
	}

	/**保存Bitmap到sdcard
	 * @param b
	 */
	public static void saveBitmap(Bitmap b){

		String path = initPath();
//		long dataTake = System.currentTimeMillis();
		String jpegName = path + "/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +".jpg";
		Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			Log.i(TAG, "saveBitmap成功");
			
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);     
			Uri uri = Uri.fromFile(new File(jpegName));     
			intent.setData(uri);      
//			mContext.sendBroadcast(intent); 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "saveBitmap:失败");
			e.printStackTrace();
		}

	}


}
