package com.guogee.smartwatch.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.adapter.BeanAdapter;
import com.guogee.smartwatch.dao.SleepDao;
import com.guogee.smartwatch.dao.SportDao;
import com.guogee.smartwatch.service.RemoteGameService;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.utils.Log;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BeanFragment extends Fragment implements OnClickListener{

	private ListView listView;
    private TextView beanNumText;
	private ImageButton backBtn;
	
	private Button getBeanBtn;
	private String mCurrentSelectDay;
	
	//运动数据.
	private SportDao sportDao;
    private int todayStep;
	
	//睡眠数据.
    private SleepDao sleepDao;
	private int todaySleep;
	
	private RelativeLayout beanBtnLy;
	
	private RelativeLayout reConnectTipLy;
	
	private BeanAdapter mAdapter;
	private SharedPreferences sp;
	private iSmartApplication isApp;
	private List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isApp = (iSmartApplication)getActivity().getApplication();	
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
	
		SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy/MM/dd");     
		Date curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
		mCurrentSelectDay = formatter.format(curDate); 
		
//		sp.edit().putString(Constant.TODAY, mCurrentSelectDay).commit();
		
		if(!mCurrentSelectDay.equals(sp.getString(Constant.TODAY, ""))){
			sp.edit().putString(Constant.TODAY, mCurrentSelectDay).commit();
			sp.edit().putInt(Constant.TODAY_STEP, 0).commit();
			sp.edit().putInt(Constant.TODAY_SLEEP, 0).commit();
		}

		sportDao = new SportDao(getActivity());
		String[] params = new String[]{ mCurrentSelectDay };	
		LinkedHashMap<String, String> sportData = sportDao.listStepFromDays(params);
		if(sportData.size()>0){
			todayStep = Integer.parseInt(sportData.get("stepNum"));
			Log.d("TAG", "todayStep =============== "+todayStep);
		}
		
		sleepDao = new SleepDao(getActivity());
		LinkedHashMap<String, String> sleepData = sleepDao.listSelectDaySleepMaps(params);
		if(sleepData.size() > 0){
			todaySleep = Integer.parseInt(sleepData.get("totalTime"));
			Log.d("TAG", "todaySleep =============== "+todaySleep);
		}
		

		//test.
//		sp.edit().putInt(Constant.BEAN_NUM, 100).commit();
		sp.edit().putInt(Constant.TODAY_STEP, 0).commit();
		
		
//		RemoteGameService.GetUserBean("F3:7E:38:AF:F8:49", new AsyncHttpResponseHandler(){
//		@Override
//		public void onFailure(Throwable error, String content) {
//			Log.d("TAG", "get user bean List Failure.......................");
//			Toast.makeText(getActivity(),"连接服务器失败", 6000).show();
//		}
//
//		@Override
//		public void onSuccess(String content) {
//			try {
//
//				JSONObject returnval = new JSONObject(content).getJSONObject("data");
//				if(returnval.getString("code").equals("200")){
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//	});
	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.bean_layout, container, false);
		
		backBtn = (ImageButton) root.findViewById(R.id.back_Btn);
		backBtn.setOnClickListener(this);
		
		beanNumText = (TextView) root.findViewById(R.id.bean_num);
		beanNumText.setText(Integer.toString(sp.getInt(Constant.BEAN_NUM, 0)));
//		beanNumText.setText(Integer.toString(todayStep));
		
		getBeanBtn = (Button) root.findViewById(R.id.get_bean_btn);
		getBeanBtn.setOnClickListener(this);
		
		mAdapter = new BeanAdapter(getActivity());	
//		mAdapter.setData(getListData());
		
		listView = (ListView) root.findViewById(R.id.listView);
//		listView.setAdapter(mAdapter);
		
		beanBtnLy = (RelativeLayout) root.findViewById(R.id.relativeLayout2);
		
		reConnectTipLy = (RelativeLayout) root.findViewById(R.id.bean_reconnect_tip_ly);
		reConnectTipLy.setOnClickListener(this);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				Log.d("TAG","position ============= "+position);
			}
		});
		
		return root;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.back_Btn:
			isApp.getSlidingMenu().showMenu();
			break;
			
		case R.id.bean_reconnect_tip_ly:
			getListData();
			break;
			
		case R.id.get_bean_btn:
			if(list.size()==0){
				startActivity(new Intent(getActivity(), BeanTipActivity.class));
			}else{
				int beanCount = sp.getInt(Constant.BEAN_NUM, 0);
				//消耗步数
				int cousumeStep = sp.getInt(Constant.TODAY_STEP, 0);
				
				for(int i=0; i<list.size(); i++){
					beanCount += Integer.parseInt((String) list.get(i).get("beanNum"));
//					Log.d("TAG", "consume step =============== "+list.get(i).get("consumeStep"));
					String step = (String) list.get(i).get("consumeStep");
					Log.d("TAG", "consume step =============== "+step);
					cousumeStep += Integer.parseInt(step);
				}
				
//				sp.edit().putInt(Constant.TODAY_STEP, sp.getInt(Constant.TODAY_STEP, 0) + (currentBeanStep/Integer.parseInt(bereventcount) * Integer.parseInt(bereventcount)));
				
				sp.edit().putInt(Constant.TODAY_STEP,cousumeStep).commit();
				
				Log.d("TAG", "已兑换豆对应的步数 =========== "+cousumeStep);
				
//				sp.edit().putInt(Constant.TODAY_STEP, cousumeStep).commit();
				
				sp.edit().putInt(Constant.BEAN_NUM, beanCount).commit();
				beanNumText.setText(Integer.toString(beanCount));
				getBeanBtn.setText("如何获取能量豆?");
				
				list.clear();
				mAdapter.setData(list);
				mAdapter.notifyDataSetChanged();
				
				
//				RemoteGameService.postCurrentStep(stepNum, state, handler);

				
				String currentState = sp.getBoolean(Constant.CURRENT_STEP_RESET, false) ?  "y":"n" ;
				
/**	**/		
				RemoteGameService.postCurrentStep(sp.getString(Constant.CURRENT_STEP_NUM, "0"), currentState, MainActivity.deviceMacAddress, new AsyncHttpResponseHandler(){
					@Override
					public void onFailure(Throwable error, String content) {
						Log.d("TAG", "save user bean Failure.......................");
					}

					@Override
					public void onSuccess(String content) {
						Log.d("TAG", "content ============ "+content);
						
						try{
							String codeval = new JSONObject(content).getString("code");
						    if(Integer.parseInt(codeval)==200){
						    	sp.getBoolean(Constant.CURRENT_STEP_RESET, false);
						    	return;
						    }
						}catch(JSONException e){
							e.printStackTrace();
						}
					}
				});
				
				
				
				RemoteGameService.saveUserBean(MainActivity.deviceMacAddress, Integer.toString(beanCount), new AsyncHttpResponseHandler(){
					@Override
					public void onFailure(Throwable error, String content) {
						Log.d("TAG", "save user bean Failure.......................");
//						Toast.makeText(getActivity(),"连接服务器失败", 6000).show();
					}

					@Override
					public void onSuccess(String content) {
//						JSONObject returnval = new JSONObject(content).getJSONObject("data");
						Log.d("TAG", "content ============ "+content);
					}
				});
				
				Log.d("TAG", "deviceMacAddress =============== "+MainActivity.deviceMacAddress);
				
				
				
				
//				RemoteGameService.GetUserBean(MainActivity.deviceMacAddress, new AsyncHttpResponseHandler(){
//					@Override
//					public void onFailure(Throwable error, String content) {
//						Log.d("TAG", "get user bean List Failure.......................");
//						Toast.makeText(getActivity(),"连接服务器失败", 6000).show();
//					}
//
//					@Override
//					public void onSuccess(String content) {
//						try {
//
//							JSONObject returnval = new JSONObject(content).getJSONObject("data");
////							if(returnval.getString("code").equals("200")){
////							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					}
//				});
				
			}
			break;
		}
	}	
	
	///////////////////--------- ListView ---------///////////////////
	
	private void getListData(){
		RemoteGameService.getBeanRule(new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(Throwable error, String content) {
				Log.d("TAG", "get getListData List Failure.......................");
				Toast.makeText(getActivity(),"连接服务器失败", 6000).show();
			
				listView.setVisibility(View.GONE);
				reConnectTipLy.setVisibility(View.VISIBLE);
			}

			@Override
			public void onSuccess(String content) {

				listView.setVisibility(View.VISIBLE);
				reConnectTipLy.setVisibility(View.GONE);

				
				
//				try{
//					String codeval = new JSONObject(content).getString("code");
//				    if(Integer.parseInt(codeval)==201){
//				    	Toast.makeText(getActivity(),"............", 6000).show();
//				    	return;
//				    }
//				}catch(JSONException e){
//					e.printStackTrace();
//				}
				
				
				
				
				try {
					JSONArray array = new JSONObject(content).getJSONArray("datas");
					
					Log.d("TAG", "array 's size =============== "+array.length());
					
					list.clear();
					
					for(int i=0;i<array.length();i++){
						JSONObject object = array.getJSONObject(i);
						
						String bertype = object.getString("bertype");
						String berid = object.getString("berid");
						String bereventcount = object.getString("bereventcount");
						String berbeancount = object.getString("berbeancount");

						Map<String,Object> map = new HashMap<String, Object>();
						
//						map.put("bereventcount", actgamename);
//						map.put("actbegindate", actbegindate);
//						map.put("actenddate", actenddate);
//						map.put("actid", actid);
						
//						todayStep = 4000;
						
						if(bertype.equals("1")){
							Log.d("TAG", "可用步数==========="+(todayStep - sp.getInt(Constant.TODAY_STEP, 0)));
							
							if(todayStep - sp.getInt(Constant.TODAY_STEP, 0) > Integer.parseInt(bereventcount)){
								//
								int currentBeanStep = todayStep - sp.getInt(Constant.TODAY_STEP, 0);
								
								map.put("currentBeanStep", currentBeanStep);
								
								map.put("bereventcount", "完成"+ currentBeanStep+"步");
								map.put("img", R.drawable.plusdot_set_running);		
//								Log.d("TAG","beanNum ============ "+todayStep/Integer.parseInt(bereventcount));
								map.put("beanNum", Integer.toString(currentBeanStep/Integer.parseInt(bereventcount)));

								map.put("consumeStep", Integer.toString(currentBeanStep/Integer.parseInt(bereventcount) * Integer.parseInt(bereventcount)));
								
//								Log.d("TAG", "已兑换豆对应的步数 =========== "+sp.getInt(Constant.TODAY_STEP, 0) + (currentBeanStep/Integer.parseInt(bereventcount) * Integer.parseInt(bereventcount)));
//								sp.edit().putInt(Constant.TODAY_STEP, sp.getInt(Constant.TODAY_STEP, 0) + (currentBeanStep/Integer.parseInt(bereventcount) * Integer.parseInt(bereventcount)));
							
								list.add(map);
							
								getBeanBtn.setText("领取");
							}else{
								
								getBeanBtn.setText("如何获取能量豆?");
							}
						}else if(bertype.equals("0")){
							
						}
					}
					
					Message msg = uihandler.obtainMessage(MSG_UPDATE_LIST);
					uihandler.sendMessage(msg);
					
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
			}
		});
	}
	
    public final static int MSG_LOAD_DATA = 1;
    
    public final static int MSG_UPDATE_LIST = 2;
	
	public Handler uihandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			switch (msg.what) {
			case MSG_LOAD_DATA:
				Log.d("TAG","load data ..............");
				
				updateTodayData();
				getListData();
				
				RemoteGameService.GetUserBean(MainActivity.deviceMacAddress, new AsyncHttpResponseHandler(){
					@Override
					public void onFailure(Throwable error, String content) {
						Log.d("TAG", "get user bean List Failure.......................");
//						Toast.makeText(getActivity(),"连接服务器失败xxx", 6000).show();
					}

					@Override
					public void onSuccess(String content) {
						try {
//							JSONArray array = new JSONObject(content).getJSONArray("datas");

//							JSONObject object = new JSONObject(content);
//							String usbbeancount = object.getString("usbbeancount");
//							Log.d("TAG", "usbbeancount =============== "+usbbeancount);
							
							
							JSONArray array = new JSONObject(content).getJSONArray("data");
							Log.d("TAG", "mac array 's size =============== "+array.length());
							for(int i=0;i<array.length();i++){
								JSONObject object = array.getJSONObject(i);
								String mac = object.getString("usbuserkey");
								Log.d("TAG","mac address ============ "+mac);
							}
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
				
				
//				mAdapter.setData(list);
//				mAdapter.notifyDataSetChanged();
				break;
			case MSG_UPDATE_LIST:
				mAdapter.setData(list);
				listView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				
				break;
			}
		}
    };
    
    private void updateTodayData(){
		SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy/MM/dd");     
		Date curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
		mCurrentSelectDay = formatter.format(curDate); 

		if(!mCurrentSelectDay.equals(sp.getString(Constant.TODAY, ""))){
			sp.edit().putString(Constant.TODAY, mCurrentSelectDay).commit();
			sp.edit().putInt(Constant.TODAY_STEP, 0).commit();
			sp.edit().putInt(Constant.TODAY_SLEEP, 0).commit();
		}
		
		String[] params = new String[]{ mCurrentSelectDay };	
		LinkedHashMap<String, String> sportData = sportDao.listStepFromDays(params);
		if(sportData.size()>0){
			todayStep = Integer.parseInt(sportData.get("stepNum"));
			Log.d("TAG", "todayStep =============== "+todayStep);
		}
		
		LinkedHashMap<String, String> sleepData = sleepDao.listSelectDaySleepMaps(params);
		if(sleepData.size() > 0){
			todaySleep = Integer.parseInt(sleepData.get("totalTime"));
			Log.d("TAG", "todaySleep =============== "+todaySleep);
		}
		
    }
    
    ///////////////////--------- ListView ---------///////////////////
}
