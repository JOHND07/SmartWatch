package com.guogee.smartwatch.ui;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.service.RemoteGameService;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.utils.Log;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class ExchangeActivity extends Activity implements OnClickListener{

	private ImageButton backBtn;
	private TextView beanNum;
	
	private TextView howtogetBean;
	private Button exchangeBtn;
	private iSmartApplication isApp;
	
	private String actbegindate;
	private String actenddate;
	private String actmemo;
	private String actbeanbag;
	
	private TextView limitedTime;
	private TextView actBeanNumText;
	private TextView acInfor1;
	private TextView acInfor2;
	private TextView acInfor3;
	private TextView acInfor4;
	
	private SharedPreferences sp;
	private String actid;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.exchange_layout);
		setContentView(R.layout.exchange_info_layout);
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		Bundle bundle = getIntent().getExtras(); 
		actbegindate = bundle.getString("actbegindate");
		actenddate = bundle.getString("actenddate");
		actmemo = bundle.getString("actmemo");
		actbeanbag = bundle.getString("actbeanbag");
		actid = bundle.getString("actid");
		
		Log.d("TAG", "actmemo ============ "+actmemo);
		
		isApp = (iSmartApplication) getApplication();	
//		List<Map<String, String>> watchMacAddress = isApp.getBindWatch();
		
		initView();
	}
		
	private void initView(){
		backBtn = (ImageButton) findViewById(R.id.back_Btn);
		backBtn.setOnClickListener(this);
		
		howtogetBean = (TextView) findViewById(R.id.how_to_get_bean);
		howtogetBean.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //下划线
		howtogetBean.getPaint().setAntiAlias(true);//抗锯齿
		howtogetBean.setOnClickListener(this);
		
		exchangeBtn = (Button) findViewById(R.id.exchange_btn);
		exchangeBtn.setOnClickListener(this);
		
		limitedTime = (TextView) findViewById(R.id.activity_time);
		limitedTime.setText(actbegindate+"--"+actenddate);
		
		actBeanNumText = (TextView) findViewById(R.id.act_bean_num);
		actBeanNumText.setText(actbeanbag);
		
		beanNum = (TextView) findViewById(R.id.bean_num);
		beanNum.setText(Integer.toString(sp.getInt(Constant.BEAN_NUM, 0)));
		
		acInfor1 = (TextView) findViewById(R.id.text1);
		acInfor1.setText(actmemo.substring(0, 15));
		Log.d("TAG","acInfor1 =========== "+actmemo.substring(0, 15));

		acInfor2 = (TextView) findViewById(R.id.text2);
		acInfor2.setText(actmemo.substring(17, 35));
		Log.d("TAG","acInfor2 =========== "+actmemo.substring(17, 35));
		
		acInfor3 = (TextView) findViewById(R.id.text3);
		acInfor3.setText(actmemo.substring(37, 52));
		Log.d("TAG","acInfor3 =========== "+actmemo.substring(37, 52));
		
		acInfor4 = (TextView) findViewById(R.id.text4);
		acInfor4.setText(actmemo.substring(53, actmemo.length()));

//		acInfor = (TextView) findViewById(R.id.ac_infor);
//		acInfor.setText(actmemo);
		
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.how_to_get_bean:
			startActivity(new Intent(ExchangeActivity.this, BeanTipActivity.class));
			break;
		case R.id.back_Btn:
			finish();
			break;
		case R.id.exchange_btn:
			if(sp.getInt(Constant.BEAN_NUM, 0) > Integer.parseInt(actbeanbag)){

				final ProgressDialog dialog = ProgressDialog.show(ExchangeActivity.this, "", "请稍等...", true);
				
				RemoteGameService.getGifBag(MainActivity.deviceMacAddress, actid, new AsyncHttpResponseHandler(){
					@Override
					public void onFailure(Throwable error, String content) {
						Log.d("TAG", "getGifBag Failure.......................");
//						Toast.makeText(ExchangeActivity.this,"连接服务器失败", 6000).show();
						dialog.dismiss();
					}

					@Override
					public void onSuccess(String content) {
//						JSONObject returnval = new JSONObject(content).getJSONObject("data");
						Log.d("TAG", "content ============ "+content);
						
						try{
							String codeval = new JSONObject(content).getString("code");
						    if(Integer.parseInt(codeval)==201){
						    	Toast.makeText(ExchangeActivity.this,"您已兑换过礼包,每天只能兑换一次.", 6000).show();
						    	dialog.dismiss();
						    	return;
						    }
						}catch(JSONException e){
							e.printStackTrace();
						}
						
						
//						if(data==null){
//							dialog.dismiss();
//							return;
//						}
//						try {
//							JSONArray array = new JSONObject(content).getJSONArray("data");
//							
//							if(array==null){
//								dialog.dismiss();							
//							}
//						} catch (JSONException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
						
						
						try {
							JSONObject returnval = new JSONObject(content).getJSONObject("datas");
							String accode = returnval.getString("accode");
							
							Intent intent = new Intent(ExchangeActivity.this, CodeActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("accode", accode);
							intent.putExtras(bundle);
							
							int bean =  sp.getInt(Constant.BEAN_NUM, 0) - Integer.parseInt(actbeanbag);
							sp.edit().putInt(Constant.BEAN_NUM, bean).commit();
							beanNum.setText(Integer.toString(sp.getInt(Constant.BEAN_NUM, 0)));

							dialog.dismiss();
							startActivity(intent);
							finish();
							
//							startActivity(new Intent(ExchangeActivity.this, CodeActivity.class));
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}else{
				Toast.makeText(ExchangeActivity.this,"您的能量豆不足，快去运动吧.", 6000).show();
			}
			break;
		}
	}
	
	
	private void commitData(){
		
	}
	
}
