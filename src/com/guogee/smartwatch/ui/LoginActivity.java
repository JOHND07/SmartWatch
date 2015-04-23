package com.guogee.smartwatch.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.guogee.smartwatch.R;
import com.guogee.smartwatch.service.RemoteUserService;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.utils.Log;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	
//	private EditText editEmail;
//	private EditText passWord;
	private Button logBtn;
	private ImageButton loginArrow;
	private TextView loginText;
	
	private String strLoginName = "";
	private String strLoginPwd = "";
	private ProgressBar login_bar;
	private SharedPreferences sp;
	
	private boolean isReLogin = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_layout);
	
		Bundle bundle=getIntent().getExtras();
		/** 获取Bundle的信息 **/
		if(bundle!=null){
			isReLogin = bundle.getBoolean("reLogin");
		}
		
		initView();
	}
	
	private void initView(){
		
		loginArrow = (ImageButton) findViewById(R.id.login_arrow);
		loginArrow.setOnClickListener(this);
		
		loginText = (TextView) findViewById(R.id.login_text);
		loginText.setOnClickListener(this);
		
		logBtn    = (Button) findViewById(R.id.login_btn);
	    logBtn.setOnClickListener(this);
	    
	    login_bar = (ProgressBar)findViewById(R.id.login_bar);
	    sp = PreferenceManager.getDefaultSharedPreferences(this);
	    
		EditText editEmail = (EditText) findViewById(R.id.edit_Email);
//		if(sp.getString(Constant.USERNAME, "")!=""){
//			editEmail.setText(sp.getString(Constant.USERNAME, ""));
//			strLoginName = sp.getString(Constant.USERNAME, "");
//		}
		
		editEmail.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				strLoginName = s.toString();
//				checkString(strLoginName, strLoginPwd);
			}
		});
		
		EditText passWord  = (EditText) findViewById(R.id.edit_passWord); 
//		if(sp.getString(Constant.PASSWORD, "")!=""){
//			passWord.setText(sp.getString(Constant.PASSWORD, ""));
//			strLoginPwd = sp.getString(Constant.PASSWORD, "");
//		}
		
		passWord.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				strLoginPwd = s.toString();
//				checkString(strLoginName, strLoginPwd);
			}
		});
		
	}

//	private void checkString(String user,String pwd){
//		if(!user.equals("")&&!pwd.equals("")){
//			loginBtn.setClickable(true);
//			loginBtn.setBackgroundResource(R.drawable.zq_public_green_btn);
//		}else{
//			loginBtn.setClickable(false);
//			loginBtn.setBackgroundResource(R.drawable.zq_public_green_btn_c);
//		}
//	}
	
	private void gotoActivity(){
//		startActivity(new Intent(LoginActivity.this, SignActivity.class));
		startActivityForResult(new Intent(LoginActivity.this, SignActivity.class), 1);
		overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.login_arrow:
		case R.id.login_text:
			gotoActivity();
			break;
		case R.id.login_btn:
			login_bar.setVisibility(View.VISIBLE);
			login();
			break;
		}
	}
	
	private void login(){

		((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))
		.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		
		Log.d("TAG", "login ' s strLoginName ============= "+strLoginName);
		Log.d("TAG", "login ' s strLoginPwd ============= "+strLoginPwd);
		
		RemoteUserService.CheckLogin(strLoginName, strLoginPwd, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(Throwable error, String content) {
				login_bar.setVisibility(View.GONE);
				Toast.makeText(getBaseContext(), R.string.login_fail, Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(String content) {
				try {
					boolean resultflag = new JSONObject(content).getBoolean("result");
					login_bar.setVisibility(View.GONE);
					Log.d("TAG", "Login Status onSuccess. content = " + content);
					if (!resultflag) {
						// result fase
						Toast.makeText(getBaseContext(), R.string.login_err, Toast.LENGTH_SHORT).show();
						return;
					}
					
					// 远程查询到了盒子
					JSONObject returnval = new JSONObject(content).getJSONObject("value");
						
					// 是否允许登录
					boolean allowLogin = returnval.getBoolean("AllowLogin");				
					//存储用户名和密码
					if(allowLogin){
						
//						Editor editor = loginPreferences.edit();
//						editor.putString("login_name", strLoginName);
//						editor.putString("login_password", strLoginPwd);
//						editor.commit();//提交修改
//						//记录用户
//						iSmartUser iUser = new iSmartUser();
//						iUser.setEmail(returnval.getString("Email"));
//						iUser.setLoginID(returnval.getString("id"));
//						iUser.setPassword(strLoginPwd);
//						iUser.setUserName(strLoginName);
//						isapp.setIsmartuser(iUser);
						
						Editor editor = sp.edit();
						editor.putString(Constant.LOGIN_NAME, strLoginName);
						editor.commit();
						
						editor.putString(Constant.PASSWORD, strLoginPwd);
						editor.commit();
						
						editor.putBoolean(Constant.LOGIN_FALG, true);
						editor.commit();
						
						Toast.makeText(getBaseContext(), R.string.login_successful, Toast.LENGTH_SHORT).show();
						if(!isReLogin){
//							startActivity(new Intent(LoginActivity.this, SetUserData.class));	
							
//							Intent intent = new Intent(LoginActivity.this, SetUserData.class);
//							intent.putExtra("tag", "bindwatch");
//							startActivity(intent);
//							startActivity(new Intent(LoginActivity.this, BindWatchActivity.class));	
							
							Editor edit = sp.edit();
							edit.putBoolean(Constant.LOGIN_FALG, true);
							edit.commit();
						}
						setResult(1);
						finish();
					}else{
						Toast.makeText(getBaseContext(), R.string.login_fail, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==1){
			setResult(1);
			finish();
		}
	}

}
