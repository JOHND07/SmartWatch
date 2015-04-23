package com.guogee.smartwatch.dialog;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.guogee.smartwatch.R;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.utils.TypefaceUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("HandlerLeak")
public class DialogSetUName extends Activity {
	private TypefaceUtil util = new TypefaceUtil(this);
	private EditText nameEdit = null;
	private ImageButton return_view;
	private TextView savebtn = null;
	private Handler handler;
	private String name;//昵称
	private String auth_token;//动态秘钥
	private static final String FILENAME = "chaoke"; // 文件名称
	private SharedPreferences sp = null;
//	private TextView titleview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setusername);
		setupView();
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
//		auth_token = sp.getString("auth_token", "");
		
//		handler = new Handler() {
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case Constants.CONNECTTIMEOUT:
//					Toast.makeText(DialogSetUName.this, "网络连接超时,请检查网络是否畅通...",Toast.LENGTH_SHORT).show();
//					break;
//				case Constants.FAIL:
//					Toast.makeText(DialogSetUName.this, "网络连接异常...",Toast.LENGTH_SHORT).show();
//					break;
//				}
//			}
//		};

	}

	private void setupView() {
//		titleview = (TextView) findViewById(R.id.titleview);
		
		nameEdit = (EditText) findViewById(R.id.name);
		savebtn = (TextView) findViewById(R.id.savebutton);
		return_view = (ImageButton) findViewById(R.id.return_btn);
		return_view.setOnClickListener(new OnClickListenerIpml());
		savebtn.setOnClickListener(new OnClickListenerIpml());
		nameEdit.setText(getIntent().getStringExtra("name")+"");
		nameEdit.setTypeface(util.getmFaceKunlun());
		
//		savebtn.setTypeface(util.getmFaceKunlun());
//		titleview.setTypeface(util.getmFaceKunlun());
	}

	private class OnClickListenerIpml implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_btn:
				DialogSetUName.this.finish(); 
				break;
			case R.id.savebutton:
				if (checked()) {
//					saveName();
					Editor editor = sp.edit();
					editor.putString(Constant.USERNAME, name);
					editor.commit();
					
					Intent intent=new Intent();  
				    intent.putExtra("nickname", name);  
				    setResult(80, intent);  
					DialogSetUName.this.finish();
				}
				break;
			}

		}

	}

	public boolean checked() {
		name = nameEdit.getText().toString().trim();
		if ("".equals(name) || name == null){
			Toast.makeText(DialogSetUName.this, getResources().getString(R.string.name_null_tip), Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

//	public void saveName() {
//		MyTask task = new MyTask();
//		task.execute(Constants.URL+Constants.SAVE);
//	}
//
//	private class MyTask extends AsyncTask<String, Integer, String> {
//		final ProgressDialog dialog = ProgressDialog.show(DialogSetUName.this, "", "正在提交数据,请稍后...", true);
//
//		@Override
//		protected String doInBackground(String... params) {
//			ArrayList<BasicNameValuePair> lists = new ArrayList<BasicNameValuePair>();
//			lists.add(new BasicNameValuePair("auth_token", auth_token));
//			lists.add(new BasicNameValuePair("nickname", name));
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
//			dialog.cancel();
//			if(result==null)
//				return;
//			try {
//				JSONObject json = new JSONObject(result);
//				String code = json.getString("success");
//				if("1".equals(code)){
//					edit.putString("user_name", name);
//					edit.commit();
//					Toast.makeText(DialogSetUName.this, "修改成功!", Toast.LENGTH_SHORT).show();
//					Intent intent=new Intent();  
//				    intent.putExtra("nickname", name);  
//				    setResult(80, intent);  
//					DialogSetUName.this.finish();
//				}else{
//					JSONObject jsons = new JSONObject(json.getString("error"));
//					Toast.makeText(DialogSetUName.this, jsons.getString("message"), Toast.LENGTH_SHORT).show();
//				}
//			} catch (JSONException e) {
//				Toast.makeText(DialogSetUName.this, "json解析异常", Toast.LENGTH_SHORT).show();
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
	
}