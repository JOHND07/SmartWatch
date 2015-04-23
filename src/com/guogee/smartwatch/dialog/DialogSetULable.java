package com.guogee.smartwatch.dialog;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.guogee.smartwatch.R;
import com.guogee.smartwatch.utils.TypefaceUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
public class DialogSetULable extends Activity {
	private TypefaceUtil util = new TypefaceUtil(this);
	private EditText nameEdit;
	private ImageButton return_btn;
	private ImageButton savebtn;
	private Handler handler;
	private String lable;// 个性签名
	private String auth_token;// 动态秘钥
	private static final String FILENAME = "chaoke"; // 文件名称
	private SharedPreferences share = null;
	private SharedPreferences.Editor edit = null;
//	private TextView title_lable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setuserlable);
		setupView();
//		share = super.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE); // 指定操作的文件名称
		
		share = PreferenceManager.getDefaultSharedPreferences(this);
		edit = share.edit(); // 编辑文件
		auth_token = share.getString("auth_token", "");
		
//		handler = new Handler() {
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case Constants.CONNECTTIMEOUT:
//					Toast.makeText(DialogSetULable.this, "网络连接超时,请检查网络是否畅通...",Toast.LENGTH_SHORT).show();
//					break;
//				case Constants.FAIL:
//					Toast.makeText(DialogSetULable.this, "网络连接异常...",Toast.LENGTH_SHORT).show();
//					break;
//				}
//			}
//		};
		
	}

	private void setupView() {
//		title_lable = (TextView) findViewById(R.id.title_lable);
		nameEdit = (EditText) findViewById(R.id.lable);
		return_btn = (ImageButton) findViewById(R.id.return_btn);
		savebtn = (ImageButton) findViewById(R.id.savebutton);
		return_btn.setOnClickListener(new OnClickListenerIpml());
		savebtn.setOnClickListener(new OnClickListenerIpml());
		nameEdit.setText(getIntent().getStringExtra("lable")+"");
		
//		title_lable.setTypeface(util.getmFaceKunlun());
//		savebtn.setTypeface(util.getmFaceKunlun());
		
		nameEdit.setTypeface(util.getmFaceKunlun());
	}

	private class OnClickListenerIpml implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_btn:
				DialogSetULable.this.finish();
				break;
			case R.id.savebutton:
//				if (checked()) {
//					saveName();
//				}
				break;
			}
		}
	}

	public boolean checked() {
		lable = nameEdit.getText().toString().trim();
		if ("".equals(lable) || lable == null){
			Toast.makeText(DialogSetULable.this, getResources().getString(R.string.name_null_tip), Toast.LENGTH_SHORT).show();
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
//		final ProgressDialog dialog = ProgressDialog.show(DialogSetULable.this, "", "正在提交数据,请稍后...", true);
//
//		@Override
//		protected String doInBackground(String... params) {
//			ArrayList<BasicNameValuePair> lists = new ArrayList<BasicNameValuePair>();
//			lists.add(new BasicNameValuePair("auth_token", auth_token));
//			lists.add(new BasicNameValuePair("bio", lable));
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
//			System.out.println(result);
//			try {
//				JSONObject json = new JSONObject(result);
//				String code = json.getString("success");
//				if("1".equals(code)){
//					edit.putString("user_lable", lable);
//					edit.commit();
//					Toast.makeText(DialogSetULable.this, "修改成功!", Toast.LENGTH_SHORT).show();
//					Intent intent=new Intent();  
//				    intent.putExtra("bio", lable);  
//				    setResult(90, intent);  
//					DialogSetULable.this.finish();
//				}else{
//					JSONObject jsons = new JSONObject(json.getString("error"));
//					Toast.makeText(DialogSetULable.this, jsons.getString("message"), Toast.LENGTH_SHORT).show();
//				}
//			} catch (JSONException e) {
//				Toast.makeText(DialogSetULable.this, "json解析异常", Toast.LENGTH_SHORT).show();
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
