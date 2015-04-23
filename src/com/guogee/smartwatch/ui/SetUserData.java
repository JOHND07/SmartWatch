package com.guogee.smartwatch.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.message.BasicNameValuePair;

import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.R;
import com.guogee.smartwatch.dialog.DialogSetCurrWeight;
import com.guogee.smartwatch.dialog.DialogSetDate;
import com.guogee.smartwatch.dialog.DialogSetHeight;
import com.guogee.smartwatch.dialog.DialogSetULable;
import com.guogee.smartwatch.dialog.DialogSetUName;
import com.guogee.smartwatch.dialog.DialogSetWeight;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.utils.TypefaceUtil;
import com.guogee.smartwatch.widget.BitmapUtil;
import com.guogee.smartwatch.widget.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class SetUserData extends Activity {
	
	private TypefaceUtil tfutil = new TypefaceUtil(this);
	
	private ImageView user_image;// 用户头像
	private TextView user_name; // 用户名字
	private TextView user_lable; // 用户个性签名
	private TextView user_height;// 用户设定身高
	private TextView user_year; // 用户生日年
	private TextView user_month; // 用户生日月
	private TextView user_day; // 用户生日天
	private TextView user_weight;// 用户体重
	private TextView target_weight;// 用户目标体重
	private TextView ssex;
	
	private String name = null ,lable = null ,height = null ,date,weight = null ,targetWeight = null ,sex = null;
	private ImageView return_btn;
	private ImageView login;// 刚注册，点击跳到主界面，保存填写资料
	private ImageButton manbtn;
	private ImageButton womanbtn;
	private RelativeLayout rel;
	private TextView textTitle;
	private String[] items = new String[] { "选择本地图片", "拍照" };
	private static final String IMAGE_FILE_NAME = "/Chaoke/xiaoma.jpg";// 头像名称
	private static final int IMAGE_REQUEST_CODE = 1;// 请求码
	private static final int CAMERA_REQUEST_CODE = 2;
	private static final int RESULT_REQUEST_CODE = 3;
	private Handler handler;
	private String auth_token;// 动态秘钥
	private SharedPreferences share = null;
	private SharedPreferences.Editor edit = null;
	private Bitmap bitmap;
	private RelativeLayout sexheight;//身高
	private LinearLayout l_weight,l_target_weight;//体重
	private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10;
	private String isregist;
	
	private Button saveBtn;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_data_info);
		setupViews();
		
		share = PreferenceManager.getDefaultSharedPreferences(this);
		edit = share.edit(); // 编辑文件
		auth_token = share.getString("auth_token", "");
		
		String str_name=this.getIntent().getStringExtra("str_name");
		Log.d("TAG", "SetUserData ' s str_name ========================= "+str_name);
		if(!(str_name==null||str_name.equals(""))){
			user_name.setText(str_name);
		}else{
			Log.d("TAG","name ============= "+share.getString(Constant.USERNAME, ""));
			user_name.setText(share.getString(Constant.USERNAME, ""));
		}
		
		String str = getIntent().getStringExtra("tag");
		Log.d("TAG", "str.............."+str);
		if(!(str==null||str.equals(""))){
			saveBtn.setVisibility(View.VISIBLE);
			return_btn.setVisibility(View.INVISIBLE);
			return_btn.setClickable(false);
		}else{
		}
		
//		items = {getResources().getString(R.string.main_menu_select_image), getResources().getString(R.string.main_menu_select_image)};
		
//		name = share.getString("showname", "");
//		user_name.setText(name+"");
		
		sex = share.getString(Constant.GENDER, "true");
		height = share.getString(Constant.USER_HEIGHT, user_height.getText().toString().trim());
		weight = share.getString(Constant.USER_WEIGHT, user_weight.getText().toString().trim());
		targetWeight = share.getString(Constant.TARGET_WEIGHT, target_weight.getText().toString().trim());
		date = share.getString(Constant.USER_BIRTHDAY,"1990-6-24");
		
//		Drawable drawable = new BitmapDrawable(photo);
//		user_image.setImageDrawable(drawable);
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(BitmapUtil.getSDPath() + "/Chaoke/xiaoma.jpg");
			Bitmap bitmap  = BitmapFactory.decodeStream(fis);
			user_image.setImageBitmap(bitmap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setPersonInfo();
	}

	private void setupViews() {
		user_image = (ImageView) findViewById(R.id.userImage);
		manbtn = (ImageButton) findViewById(R.id.man);
		womanbtn = (ImageButton) findViewById(R.id.woman);
		user_height = (TextView) findViewById(R.id.height);
		ssex = (TextView) findViewById(R.id.selectsex);
		rel = (RelativeLayout) findViewById(R.id.userday);
		user_year = (TextView) findViewById(R.id.year);
		user_month = (TextView) findViewById(R.id.month);
		user_day = (TextView) findViewById(R.id.day);
		target_weight = (TextView) findViewById(R.id.targetweight);
		user_name = (TextView) findViewById(R.id.username);
		
		user_lable = (TextView) findViewById(R.id.userlable);
		return_btn = (ImageView) findViewById(R.id.return_btn);
		user_weight = (TextView) findViewById(R.id.weight);
		sexheight = (RelativeLayout) findViewById(R.id.sexheight);
		l_weight = (LinearLayout) findViewById(R.id.l_weight);
		l_target_weight = (LinearLayout) findViewById(R.id.l_targetWeight);
		
		saveBtn = (Button) findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(user_name.getText().equals("") || user_name.getText()==null){
					Toast.makeText(SetUserData.this, getResources().getString(R.string.user_name_tip), Toast.LENGTH_SHORT).show();
				}else{
					startActivity(new Intent(SetUserData.this, MainActivity.class));
					SetUserData.this.finish();					
				}
			}
		}); 
		
		tv1 = (TextView) findViewById(R.id.kunlun1);
		tv2 = (TextView) findViewById(R.id.kunlun2);
		tv3 = (TextView) findViewById(R.id.kunlun3);
		tv4 = (TextView) findViewById(R.id.kunlun4);
		tv5 = (TextView) findViewById(R.id.kunlun5);
		tv6 = (TextView) findViewById(R.id.kunlun6);
		tv7 = (TextView) findViewById(R.id.kunlun7);
		tv8 = (TextView) findViewById(R.id.kunlun8);
		tv9 = (TextView) findViewById(R.id.kunlun9);
		tv10 = (TextView) findViewById(R.id.kunlun10);
		
		
		user_name.setTypeface(tfutil.getmFaceKunlun());
		ssex.setTypeface(tfutil.getmFaceKunlun());
		tv1.setTypeface(tfutil.getmFaceKunlun());
		tv2.setTypeface(tfutil.getmFaceKunlun());
		tv3.setTypeface(tfutil.getmFaceKunlun());
		tv4.setTypeface(tfutil.getmFaceKunlun());
		tv5.setTypeface(tfutil.getmFaceKunlun());
		tv6.setTypeface(tfutil.getmFaceKunlun());
		tv7.setTypeface(tfutil.getmFaceKunlun());
		tv8.setTypeface(tfutil.getmFaceKunlun());
		tv9.setTypeface(tfutil.getmFaceKunlun());
		tv10.setTypeface(tfutil.getmFaceKunlun());
		
		user_lable.setTypeface(tfutil.getmFaceKunlun());
		user_height.setTypeface(tfutil.getFaceZhang());
		user_year.setTypeface(tfutil.getFaceZhang());
		user_month.setTypeface(tfutil.getFaceZhang());
		user_day.setTypeface(tfutil.getFaceZhang());
		user_weight.setTypeface(tfutil.getFaceZhang());
		target_weight.setTypeface(tfutil.getFaceZhang());

		user_lable.setOnClickListener(new OnClickListenerImpl());
		user_image.setOnClickListener(new OnClickListenerImpl());
		rel.setOnClickListener(new OnClickListenerImpl());
		manbtn.setOnClickListener(new OnClickListenerImpl());
		womanbtn.setOnClickListener(new OnClickListenerImpl());
		user_name.setOnClickListener(new OnClickListenerImpl());
		sexheight.setOnClickListener(new OnClickListenerImpl());
		l_weight.setOnClickListener(new OnClickListenerImpl());
		l_target_weight.setOnClickListener(new OnClickListenerImpl());

		return_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SetUserData.this.finish();
			}
		});
	}

	private class OnClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = null;
			switch (v.getId()) {
			case R.id.userlable:
				intent = new Intent(SetUserData.this, DialogSetULable.class);
				intent.putExtra("lable", user_lable.getText().toString().trim()+"");
				startActivityForResult(intent, 80);
				break;
			case R.id.userImage:
				showDialog();
				break;
			case R.id.userday:// 生日按钮
				intent = new Intent(SetUserData.this, DialogSetDate.class);
				intent.putExtra("date", date);
				startActivityForResult(intent, 200);
				break;
			case R.id.man:
				savaGender("true");
				setMan();
				break;
			case R.id.woman:
				savaGender("false");
				setWoman();
				break;
			case R.id.sexheight:
				intent = new Intent(SetUserData.this, DialogSetHeight.class);
				intent.putExtra("height", height);
				startActivityForResult(intent, 100);
				break;
			case R.id.username:
				intent = new Intent(SetUserData.this, DialogSetUName.class);
				intent.putExtra("name", user_name.getText().toString().trim()+"");
				startActivityForResult(intent, 100);
				break;
			case R.id.l_weight:
				intent = new Intent(SetUserData.this, DialogSetCurrWeight.class);
				intent.putExtra("weight", weight);
				startActivityForResult(intent, 100);
				break;
			case R.id.l_targetWeight:
				intent = new Intent(SetUserData.this, DialogSetWeight.class);
				intent.putExtra("targetWeight", targetWeight);
				startActivityForResult(intent, 100);
				break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("head", "resultCode is " + resultCode);
		ArrayList<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("auth_token", auth_token));
		
		if (20 == resultCode) { // 身高
			height = data.getExtras().getString("height");
			user_height.setText(height);
			list.add(new BasicNameValuePair("height", height));
			
//			MyTaskSave tasks = new MyTaskSave(list);
//			tasks.execute(Constants.URL+Constants.SAVE);
			
			edit.putString(Constant.USER_HEIGHT, height);
			edit.commit();
		} else if (40 == resultCode) {// 生日
			String uyear = data.getExtras().getString("year");
			String umonth = data.getExtras().getString("month");
			String uday = data.getExtras().getString("day");
			user_year.setText(uyear);
			user_month.setText(umonth);
			user_day.setText(uday);
			date = uyear+"-"+umonth+"-"+uday;
			list.add(new BasicNameValuePair("birthday", date));
			
//			MyTaskSave tasks = new MyTaskSave(list);
//			tasks.execute(Constants.URL+Constants.SAVE);
			
			Calendar calendar=Calendar.getInstance();
			int age=calendar.get(Calendar.DAY_OF_YEAR)-Integer.parseInt(uyear);
			edit.putInt("age", age);//年龄
			edit.putString(Constant.USER_BIRTHDAY, date);
			edit.commit();
		} else if (60 == resultCode) {
			weight = data.getExtras().getString("weight");
			user_weight.setText(weight);
			list.add(new BasicNameValuePair("weight", weight));
			
//			MyTaskSave tasks = new MyTaskSave(list);
//			tasks.execute(Constants.URL+Constants.SAVE);
			
			edit.putString(Constant.USER_WEIGHT, weight);
			edit.commit();
		} else if (70 == resultCode) {
			targetWeight = data.getExtras().getString("weight");
			target_weight.setText(targetWeight);
			list.add(new BasicNameValuePair("weight_goal", targetWeight));
			
//			MyTaskSave tasks = new MyTaskSave(list);
//			tasks.execute(Constants.URL+Constants.SAVE);
			
			edit.putString(Constant.TARGET_WEIGHT, targetWeight);
			edit.commit();
		} else if (80 == resultCode) {
			name = data.getExtras().getString("nickname");
			user_name.setText(name);
		} else if (90 == resultCode) {
			lable = data.getExtras().getString("bio");
			user_lable.setText(lable);
		} else if (IMAGE_REQUEST_CODE == requestCode) {
			Log.i("head", "IMAGE_REQUEST_CODE");
			if (data != null) {
				startPhotoZoom(data.getData());
			}
		}else if (CAMERA_REQUEST_CODE == requestCode) {
			if (resultCode == 0) {
			} else {
				Log.i("wen", "CAMERA_REQUEST_CODE");
				if (Tools.hasSdcard()) {
					File temp = new File(Environment.getExternalStorageDirectory()+ IMAGE_FILE_NAME);
					Log.i("wen", Environment.getExternalStorageDirectory()+ IMAGE_FILE_NAME+"");
					startPhotoZoom(Uri.fromFile(temp));
				} else {
					Toast toast = Toast.makeText(SetUserData.this, SetUserData.this.getResources().getString(R.string.main_menu_notcard),Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,toast.getYOffset() / 2);
					toast.show();
				}
			}
		} else if (RESULT_REQUEST_CODE == requestCode) {
			Log.i("wen", "RESULT_REQUEST_CODE");
			if (data == null)
				Log.i("wen", "data is null");
			if (data != null) {
				getImageToView(data);
			}
		} else {
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void savaGender(String gender) {
		edit.putString(Constant.GENDER, gender);
		edit.commit();
	}

	public void setWoman() {
		manbtn.setImageResource(R.drawable.man);
		womanbtn.setImageResource(R.drawable.select_woman);
		ssex.setText( SetUserData.this.getResources().getString(R.string.female));
	}

	public void setMan() {
		manbtn.setImageResource(R.drawable.select_man);
		womanbtn.setImageResource(R.drawable.woman);
		ssex.setText(SetUserData.this.getResources().getString(R.string.male));
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 206);
		intent.putExtra("outputY", 206);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * @param picdata
	 */
	@SuppressWarnings("deprecation")
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			user_image.setImageDrawable(drawable);
			pushImage(photo);
			BitmapUtil.save(photo,BitmapUtil.getSDPath() + "/Chaoke/xiaoma.jpg");
		}
	}

	private void pushImage(Bitmap bitmap) {
		System.out.println(bitmap+"");
		ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            byte[] imgBytes = out.toByteArray();
            String avatar = Base64.encodeToString(imgBytes, Base64.DEFAULT);
            ArrayList<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
    		list.add(new BasicNameValuePair("auth_token", auth_token));
    		list.add(new BasicNameValuePair("avatar", avatar));
    		
//    		MyTaskSave tasks = new MyTaskSave(list);
//    		tasks.execute(Constants.URL+Constants.SAVEIMAGE);
        } catch (Exception e) {
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {}
        }
	}

	private void showDialog() {
		new AlertDialog.Builder(this).setTitle(SetUserData.this.getResources().getString(R.string.main_menu_set_head))
				.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							dialog.dismiss();
							Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
							intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
							Log.i("head", "IMAGE_REQUEST_CODE"+ IMAGE_REQUEST_CODE);
							startActivityForResult(intentFromGallery, 1);
							break;
						case 1:
							// 判断存储卡是否可以用，可用进行存储
							if (Tools.hasSdcard()) {
								Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								// 下面这句指定调用相机拍照后的照片存储的路径
								intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME)));
								startActivityForResult(intentFromCapture,CAMERA_REQUEST_CODE);
								break;
							}
						}
					}
				}).show();
	}

//	private class MyTask extends AsyncTask<String, Integer, String> {
//		final ProgressDialog dialog = ProgressDialog.show(SetUserData.this, "",
//				SetUserData.this.getResources().getString(R.string.userinfo_load_info), true);
//
//		@Override
//		protected String doInBackground(String... params) {
//			ArrayList<BasicNameValuePair> lists = new ArrayList<BasicNameValuePair>();
//			lists.add(new BasicNameValuePair("auth_token", auth_token));
//			String result = null;
//			try {
//				result = HttpService.toString(HttpService.getEntity(params[0],lists, Constants.METHOD_GET));
//			} catch (ConnectTimeoutException e) {
//				handler.sendEmptyMessage(Constants.CONNECTTIMEOUT);
//			} catch (IOException e) {
//				handler.sendEmptyMessage(Constants.FAIL);
//			}
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			dialog.cancel();
//			if (result == null)
//				return;
//			System.out.println(result);
//			try {
//				JSONObject json = new JSONObject(result);
//				String code = json.getString("success");
//				if ("1".equals(code)) {
//					JSONObject jsonn = new JSONObject(json.getString("data"));
//					name = jsonn.getString("nickname");
//					lable = jsonn.getString("bio");
//					height = jsonn.getString("height");
//					date = jsonn.getString("birthday");
//					weight = jsonn.getString("weight");
//					targetWeight = jsonn.getString("weight_goal");
//					sex = jsonn.getString("gender");//true表示男，false表示女
//					setPersonInfo();
//					
//					String avatar_url = jsonn.getString("avatar");
//					getBitmap(avatar_url);
//					
//					edit.putString("user_name", jsonn.getString("nickname"));
//					edit.putString("user_lable", jsonn.getString("bio"));
//					edit.putString("gender", jsonn.getString("gender"));
//					edit.putString("user_birthday", jsonn.getString("birthday"));
//					edit.putString("user_height", jsonn.getString("height"));
//					edit.putString("avatar_url", jsonn.getString("avatar"));
//					edit.putString("user_weight", jsonn.getString("weight"));
//					edit.putString("target_weight", jsonn.getString("weight_goal"));
//					edit.commit();
//				} else {
//					JSONObject jsons = new JSONObject(json.getString("error"));
//					Toast toast = Toast.makeText(SetUserData.this,jsons.getString("message"), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,toast.getYOffset() / 2);
//					toast.show();
//				}
//			} catch (JSONException e) {
//				Toast toast = Toast.makeText(SetUserData.this, SetUserData.this.getResources().getString(R.string.main_menu_data_parsing), Toast.LENGTH_SHORT);
//				toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,toast.getYOffset() / 2);
//				toast.show();
//			}
//		}
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}
//
//		@Override
//		protected void onProgressUpdate(Integer... values) {
//			super.onProgressUpdate(values);
//		}
//	}
	
//	private class MyTaskSave extends AsyncTask<String, Integer, String> {
//		private ArrayList<BasicNameValuePair> lists;
//		
//		public MyTaskSave(ArrayList<BasicNameValuePair> lists){
//			if(lists!=null)
//				this.lists = lists;
//			else
//				lists = new ArrayList<BasicNameValuePair>();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//			String result = null;
//			try {
//				 result = HttpService.toString(HttpService.getEntity(params[0], lists, Constants.METHOD_POST));
//			} catch (ConnectTimeoutException e) {
//				handler.sendEmptyMessage(Constants.CONNECTTIMEOUT);
//			} catch (IOException e) {
//				handler.sendEmptyMessage(Constants.FAIL);
//			}
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			System.out.println(result+"");
//			if(result==null)
//				return;
//			System.out.println(result);
//			try {
//				JSONObject json = new JSONObject(result);
//				String code = json.getString("success");
//				if("1".equals(code)){
//					edit.putString("nickname", name);
//					edit.commit();
//					if ("yes".equals(isregist)) {
//						Toast toast = Toast.makeText(SetUserData.this,  SetUserData.this.getResources().getString(R.string.userinfo_welcome_chaoke), Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,toast.getYOffset() / 2);
//						toast.show();
//						Intent intent = new Intent(SetUserData.this,MenuActivity.class);
//						startActivity(intent);
//						SetUserData.this.finish();
//					}
//				}else{
//					JSONObject jsons = new JSONObject(json.getString("error"));
//					Toast toast = Toast.makeText(SetUserData.this, jsons.getString("message"), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,toast.getYOffset() / 2);
//					toast.show();
//				}
//			} catch (JSONException e) {
//				Toast toast = Toast.makeText(SetUserData.this, SetUserData.this.getResources().getString(R.string.main_menu_data_parsing), Toast.LENGTH_SHORT);
//				toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,toast.getYOffset() / 2);
//				toast.show();
//			}
//		}
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}
//
//		@Override
//		protected void onProgressUpdate(Integer... values) {
//			super.onProgressUpdate(values);
//		}
//	}

	public void setDate(String birthday) {
		String str[] = birthday.split("-");
		user_year.setText(str[0]);
		user_month.setText(str[1]);
		user_day.setText(str[2]);
	}

	public void setPersonInfo() {
		if(name==null){
			name = user_name.getText().toString().trim();
		}
		if(lable==null){
			lable = user_lable.getText().toString().trim();
		}
		if(height==null){
			height = user_height.getText().toString().trim();
		}
		if(weight==null){
			weight = user_weight.getText().toString().trim();
		}
		if(targetWeight==null){
			targetWeight = target_weight.getText().toString().trim();
		}
		if(sex==null){
			sex = "true";
		}
		if(date==null){
			date = user_year.getText().toString().trim()+"-"
					+user_month.getText().toString().trim()+"-"
					+user_day.getText().toString().trim();
		}
		user_name.setText(name);
		user_lable.setText(lable);
		user_height.setText(height);
		user_weight.setText(weight);
		target_weight.setText(targetWeight);
		
		if("true".equals(sex))
			setMan();
		else
			setWoman();
		setDate(date);
	}

//	public void getBitmap(final String avatar_url) {
//		Thread thread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					if("".equals(avatar_url)||avatar_url==null)
//						return;
//					//System.out.println(HttpService.toString(HttpService.getEntity(avatar_url, null, Constants.METHOD_GET)));
//					InputStream in = HttpService.getStream(HttpService.getEntity(avatar_url, null, Constants.METHOD_GET));
//					bitmap = BitmapFactory.decodeStream(in);
//					if(in!=null){
//						handler.sendEmptyMessage(Constants.GET_OK);
//						BitmapUtil.save(bitmap,BitmapUtil.getSDPath() + "/Chaoke/xiaoma.jpg");
//					}
//				} catch (ConnectTimeoutException e) {
//				} catch (IOException e) {}
//			}
//		});
//		thread.start();
//	}
	
}
