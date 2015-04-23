package com.guogee.smartwatch.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.guogee.smartwatch.R;
import com.guogee.smartwatch.service.RemoteUserService;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.utils.Log;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SignActivity extends Activity implements OnClickListener{

	private EditText emailEdit;
	private EditText nameEdit;
	private EditText passwordEdit;
	private EditText confirmPasswordEdit;
	private Button loginBtn;
	private RelativeLayout loginLayout;
	private SharedPreferences sp;
	private ProgressBar login_bar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sign_layout);
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		initView();
	}
	
	private void initView(){
		emailEdit = (EditText) findViewById(R.id.email_edit);
		nameEdit  = (EditText) findViewById(R.id.username_edit);
		passwordEdit = (EditText) findViewById(R.id.password_edit);
		confirmPasswordEdit = (EditText) findViewById(R.id.confirm_password_edit);
		
		loginBtn = (Button) findViewById(R.id.login_btn);
		loginBtn.setOnClickListener(this);
		
		loginLayout = (RelativeLayout) findViewById(R.id.login_layout);
		loginLayout.setOnClickListener(this);
		
		login_bar = (ProgressBar)findViewById(R.id.login_bar);
	}
	
	private void gotoActivity(){
		overridePendingTransition(R.anim.push_left_out,R.anim.push_left_in);
		finish();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.login_layout:
			gotoActivity();
			break;
		case R.id.login_btn:
			login_bar.setVisibility(View.VISIBLE);
			registerAndlogin();
			break;
		}
	}
	
	private void registerAndlogin(){
		((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))
		.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		
		
		Log.d("TAG","email ==== "+emailEdit.getText().toString());
		Log.d("TAG","name ==== "+nameEdit.getText().toString());
		Log.d("TAG","password ==== "+passwordEdit.getText().toString());
		
		RemoteUserService.RegisterUser(emailEdit.getText().toString(), nameEdit.getText().toString(), 
				passwordEdit.getText().toString(), new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(Throwable error, String content) {
				login_bar.setVisibility(View.GONE);
				Toast.makeText(getBaseContext(), R.string.register_fail, 6000).show();
			}

			@Override
			public void onSuccess(String content) {
				try {
					boolean resultflag = new JSONObject(content).getBoolean("result");
					Log.d("TAG", "Login Status onSuccess. content = " + content);
					if (!resultflag) {
						login_bar.setVisibility(View.GONE);
						// result fase
						Toast.makeText(getBaseContext(), R.string.register_exist, 6000).show();
						return;
					}
					
					JSONObject returnval = new JSONObject(content).getJSONObject("value");
					// 是否允许登录
					boolean allowLogin = returnval.getBoolean("AllowLogin");				
					if(allowLogin){
						//存储用户名和密码
						Editor editor = sp.edit();
						editor.putString(Constant.LOGIN_NAME,     nameEdit.getText().toString());
						editor.putString(Constant.LOGIN_PASSWORD, passwordEdit.getText().toString());
						editor.commit();//提交修改
						Toast.makeText(getBaseContext(), R.string.register_successful, 6000).show();
						// login
						login();
					}else{
						Toast.makeText(getBaseContext(), R.string.register_fail, 6000).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void login(){
		((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))
		.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		
		RemoteUserService.CheckLogin(nameEdit.getText().toString(),  passwordEdit.getText().toString(), new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(Throwable error, String content) {
				login_bar.setVisibility(View.GONE);
				Toast.makeText(getBaseContext(), R.string.login_fail, 6000).show();
			}

			@Override
			public void onSuccess(String content) {
				try {
					boolean resultflag = new JSONObject(content).getBoolean("result");
					login_bar.setVisibility(View.GONE);
					if (!resultflag) {
						// result fase
						Toast.makeText(getBaseContext(), R.string.login_err, 6000).show();
						return;
					}
					
					JSONObject returnval = new JSONObject(content).getJSONObject("value");
					
					// 是否允许登录
					boolean allowLogin = returnval.getBoolean("AllowLogin");				
					if(allowLogin){
						//记录用户
//						iSmartUser iUser = new iSmartUser();
//						iUser.setEmail(returnval.getString("Email"));
//						iUser.setLoginID(returnval.getString("id"));
//						iUser.setPassword(pwdEdit.getText().toString());
//						iUser.setUserName(loginNameEdit.getText().toString());
//						isapp.setIsmartuser(iUser);
						
						Editor editor = sp.edit();
						editor.putBoolean(Constant.LOGIN_FALG, true);
						editor.commit();
						
						Toast.makeText(getBaseContext(), R.string.login_successful, 6000).show();
						setResult(1);
						finish();
					}else{
						Toast.makeText(getBaseContext(), R.string.login_fail, 6000).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
