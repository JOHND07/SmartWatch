package com.guogee.smartwatch.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.adapter.GiftAdapter;
import com.guogee.smartwatch.service.RemoteGameService;
import com.guogee.smartwatch.utils.Log;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class GiftFragment extends Fragment implements OnClickListener{
	
	private ImageButton backBtn;
	private iSmartApplication isApp;
	private RadioGroup radioGroup;
//	private ListView listView;
	private GiftAdapter mAdapter;
	
	private List<Map<String,Object>> eventList = new ArrayList<Map<String,Object>>();
	private List<Map<String,Object>> giftList = new ArrayList<Map<String,Object>>();
	private boolean isInGift = false;
	
	///
	private ListView mListView;
    private PullToRefreshListView mPullListView;
//    private ArrayAdapter<String> mAdapter;
    private LinkedList<String> mListItems;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private boolean mIsStart = true;
    private int mCurIndex = 0;
    private static final int mLoadDataCount = 100;
	///
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isApp = (iSmartApplication)getActivity().getApplication();	
	}
	
/**	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.gift_layout, container, false);
		
		backBtn = (ImageButton) root.findViewById(R.id.left_Btn);
		backBtn.setOnClickListener(this);
		
		radioGroup = (RadioGroup) root.findViewById(R.id.main_radio);
		radioGroup.setOnCheckedChangeListener(checkedChangeListener);
		
		mAdapter = new GiftAdapter(getActivity());	
//		mAdapter.setData(getListData3());
		
		listView = (ListView) root.findViewById(R.id.listView);
//		listView.setAdapter(mAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				
				if(!isInGift){
					Map<String,Object> data = eventList.get(position);
					Intent intent = new Intent(getActivity(), ExchangeActivity.class);
					
					Bundle bundle = new Bundle();
					bundle.putString("actbegindate", (String)data.get("actbegindate"));
					bundle.putString("actenddate", (String)data.get("actenddate"));
					bundle.putString("actmemo", (String)data.get("actmemo"));
					bundle.putString("actid", (String)data.get("actid"));
					
					bundle.putString("actbeanbag", (String)data.get("actbeanbag"));
					
					intent.putExtras(bundle);
					startActivity(intent);	
				}else{
					
				}
			}
		});
		
		return root;
	}
	
**/	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.gift_layout, container, false);

		backBtn = (ImageButton) root.findViewById(R.id.left_Btn);
		backBtn.setOnClickListener(this);
		
		radioGroup = (RadioGroup) root.findViewById(R.id.main_radio);
		radioGroup.setOnCheckedChangeListener(checkedChangeListener);
		
		mAdapter = new GiftAdapter(getActivity());	
		
		mPullListView = (PullToRefreshListView) root.findViewById(R.id.listView);
		mPullListView.setPullLoadEnabled(false);
	    mPullListView.setScrollLoadEnabled(false);
	        
//	    mCurIndex = mLoadDataCount;
	    
        mListView = mPullListView.getRefreshableView();
//        mListView.setAdapter(mAdapter);
        
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
//                String text = mListItems.get(position) + ", index = " + (position + 1);
//                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            	
            	if(!isInGift){
					Map<String,Object> data = eventList.get(position);
					Intent intent = new Intent(getActivity(), ExchangeActivity.class);
					
					Bundle bundle = new Bundle();
					bundle.putString("actbegindate", (String)data.get("actbegindate"));
					bundle.putString("actenddate", (String)data.get("actenddate"));
					bundle.putString("actgiftbagmemo", (String)data.get("actgiftbagmemo"));
					bundle.putString("actid", (String)data.get("actid"));
					bundle.putString("actbeanbag", (String)data.get("actbeanbag"));
					bundle.putString("actmemo", (String)data.get("actmemo"));
					
					
					intent.putExtras(bundle);
					startActivity(intent);	
				}else{
					showCodeDialog((String)giftList.get(position).get("actgamename"), (String)giftList.get(position).get("gbcode"), (String)giftList.get(position).get("actgiftbagmemo"));
				}
            }
        });
        
        mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mIsStart = true;
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mIsStart = false;
                new GetDataTask().execute();
            }
        });
        setLastUpdateTime();
        
        mPullListView.doPullRefreshing(true, 500);
	    
		return root;
	}
	
	
    private class GetDataTask extends AsyncTask<Void, Void, List<Map<String, Object>>> {

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params) {
            // Simulates a background job.
        	
        	Log.d("TAG", "GiftFragment doInBackground .................... "+isInGift);
        	List<Map<String,Object>> list ;
        	
        	if(!isInGift){
        		getActivityList();	
        		list = eventList;
        	}else{
        		getGiftList();
        		list = giftList;
        	}
        	
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
//            return eventList;
            
            return list;
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> result) {
        	
        	Log.d("TAG", "GiftFragment onPostExecute .................... ");
        	
            boolean hasMoreData = true;
//            if (mIsStart) {
//                mListItems.addFirst("Added after refresh...");
//            } else {
//                int start = mCurIndex;
//                int end = mCurIndex + mLoadDataCount;
//                if (end >= mStrings.length) {
//                    end = mStrings.length;
//                    hasMoreData = false;
//                }
//                
//                for (int i = start; i < end; ++i) {
//                    mListItems.add(mStrings[i]);
//                }
//                
//                mCurIndex = end;
//            }
            
            if(!isInGift){
				Message msg = uihandler.obtainMessage(MSG_UPDATE_LIST);
				uihandler.sendMessage(msg);
            }else{
				Message msg = uihandler.obtainMessage(MSG_UPDATE_GIFT);
				uihandler.sendMessage(msg);
            }
            
            
            mAdapter.notifyDataSetChanged();
            mPullListView.onPullDownRefreshComplete();
            mPullListView.onPullUpRefreshComplete();
            mPullListView.setHasMoreData(hasMoreData);
            setLastUpdateTime();

            super.onPostExecute(result);
        }
    }

    
    private void setLastUpdateTime() {
        String text = formatDateTime(System.currentTimeMillis());
        mPullListView.setLastUpdatedLabel(text);
    }
    
    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        
        return mDateFormat.format(new Date(time));
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
		Log.d("TAG", "GiftFragment 's onResume ................. ");
		//获取活动列表.
//		getActivityList();
		
//		getGiftList();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_Btn:
			isApp.getSlidingMenu().showMenu();
			break;
		}
	}	
	
/**	
 * 测试数据
 * 
 * **/
	private List<Map<String, Object>> getListData3(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("title", "龙武游戏兑换好礼活动");
		map.put("img", R.drawable.longwu_logo);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "远征游戏兑换好礼活动");
		map.put("img", R.drawable.longwu_logo);
		list.add(map);
    
		return list;
    }
     
	
    private List<Map<String, Object>> getListData2(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("title", "龙武游戏兑换好礼活动2");
		map.put("img", R.drawable.longwu_logo);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "远征游戏兑换好礼活动2");
		map.put("img", R.drawable.longwu_logo);
		list.add(map);
    
		return list;
    }

	////////////////////
	
	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup grou, int checkedId) {
			switch (checkedId) {
			case R.id.exchange_gift:
//				mAdapter.setData(list);
//				mAdapter.notifyDataSetChanged();
				
				isInGift = false;
				
				mAdapter.setData(eventList,isInGift);
				mAdapter.notifyDataSetChanged();

				break;
			case R.id.my_gift:
				if(giftList.size()==0){
					mPullListView.doPullRefreshing(true, 500);	
				}
				
				isInGift = true;
				
				mAdapter.setData(giftList,isInGift);
				mAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};
	
	////////////////---- 列表数据 ----/////////////////
	
	private void getGiftList(){
		RemoteGameService.GetGiftList(MainActivity.deviceMacAddress, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(Throwable error, String content) {
				Log.d("TAG", "get gift List Failure.......................");
				Toast.makeText(getActivity(),"连接服务器失败", 6000).show();
			}

			@Override
			public void onSuccess(String content) {
				try{
					JSONArray array = new JSONObject(content).getJSONArray("data");
					
					Log.d("TAG", "array 's length ============== "+array.length());
					
					giftList.clear();
					
					for(int i=0;i<array.length();i++){
						JSONObject object = array.getJSONObject(i);
						
						JSONObject giftbagInfo = object.getJSONObject("giftbag");
						String gbcode = giftbagInfo.getString("gbcode");
						String gbdate = giftbagInfo.getString("gbdate");
						
						JSONObject activityinfo = object.getJSONObject("activityinfo");
						
						String actgamename = activityinfo.getString("actgamename");
						String actgiftbagmemo = activityinfo.getString("actgiftbagmemo");
						String actlogo = activityinfo.getString("actlogo");
						
						
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("gbcode", gbcode);
						map.put("gbdate", gbdate);
						map.put("actgamename", actgamename);
						map.put("actgiftbagmemo", actgiftbagmemo);
						map.put("actlogo", "http://182.92.157.146:8080/gambler/image/"+actlogo);
						
						
//						map.put("img", R.drawable.longwu_logo);
						giftList.add(map);
						
						
//						String actbegindate = object.getString("actbegindate");
//						String actenddate = object.getString("actenddate");
//						String actgamename = object.getString("actgamename");
//						String actid = object.getString("actid");
//						String actmemo = object.getString("actmemo");
//						String actname = object.getString("actname");
//						String actbeanbag = object.getString("actbeanbag");

						
//						String actlogo = object.getString("actlogo");
//						Map<String,Object> map = new HashMap<String, Object>();
//						map.put("actgamename", actgamename);
//						map.put("actbegindate", actbegindate);
//						map.put("actenddate", actenddate);
//						map.put("actid", actid);
//						map.put("actmemo", actmemo);
//						map.put("actbeanbag", actbeanbag);
//						map.put("actlogo", "http://182.92.157.146:8080/gambler/image/longwu_logo.png");
						
//						map.put("img", R.drawable.longwu_logo);
//						giftList.add(map);
					}
					
				}catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void getActivityList(){
		RemoteGameService.GetActivityList(new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(Throwable error, String content) {
				Log.d("TAG", "get Activity List Failure.......................");
				Toast.makeText(getActivity(),"连接服务器失败", 6000).show();
//				Log.d("TAG", "error ============ ");
			}

			@Override
			public void onSuccess(String content) {
				try {
					JSONArray array = new JSONObject(content).getJSONArray("list");
//					Log.d("TAG", "array 's size =============== "+array.length());
					
					Log.d("TAG", "array ================ "+array);
					
//					list = new ArrayList<Map<String,Object>>();
					
					eventList.clear();
					
					for(int i=0;i<array.length();i++){
						JSONObject object = array.getJSONObject(i);
						
						String actbegindate = object.getString("actbegindate");
						String actenddate = object.getString("actenddate");
						String actgamename = object.getString("actgamename");
						String actid = object.getString("actid");
						String actmemo = object.getString("actmemo");
						String actname = object.getString("actname");
						String actbeanbag = object.getString("actbeanbag");
						String actgiftbagmemo = object.getString("actgiftbagmemo");
						
						String actlogo = object.getString("actlogo");
//						Log.d("TAG", "actlogo ========== "+actlogo);

						Map<String,Object> map = new HashMap<String, Object>();
						map.put("actgamename", actgamename);
						
						map.put("actbegindate", actbegindate);
						map.put("actenddate", actenddate);
						map.put("actid", actid);
						map.put("actmemo", actmemo);
						map.put("actbeanbag", actbeanbag);
						map.put("actgiftbagmemo", actgiftbagmemo);
						
//						map.put("actlogo", "http://182.92.157.146:9527/gambler/image/longwu_logo.png");
						map.put("actlogo", "http://182.92.157.146:8080/gambler/image/"+actlogo);
						
						
//						map.put("img", R.drawable.longwu_logo);
						eventList.add(map);
					}

					Message msg = uihandler.obtainMessage(MSG_UPDATE_LIST);
					uihandler.sendMessage(msg);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
    ////////////////---- 列表数据 ----/////////////////

	public final static int MSG_LOAD_ACTIVITY = 0;
	
	public final static int MSG_LOAD_GIFT = 1;
	
	public final static int MSG_UPDATE_LIST = 2;
	
	public final static int MSG_UPDATE_GIFT = 3;
	
	public Handler uihandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			switch (msg.what) {
			case MSG_LOAD_ACTIVITY:
				Log.d("TAG","load activity data ..............");
//				getActivityList();
				break;
			case MSG_LOAD_GIFT:
				Log.d("TAG","load gift data ..............");
				mAdapter.setData(giftList,isInGift);
//				listView.setAdapter(mAdapter);
				
				mListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				
				break;
			case MSG_UPDATE_LIST:
				Log.d("TAG","update list ..............");
				
				mAdapter.setData(eventList,isInGift);
//				listView.setAdapter(mAdapter);
				
				mListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				
				break;
			case MSG_UPDATE_GIFT:
				break;
			}
		}
    };
    
    
    //////////////////////////////////////////////////
/**    
    private void showCodeDialog(final String title,final String code,final String content) {
        new AlertDialog.Builder(getActivity())
//                .setMessage(getResources().getString(R.string.notifiacation_tip))
        
                .setMessage(code)
                .setTitle(title)
//                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setCancelable(true)
                .setPositiveButton(R.string.copy_code,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                            	
                    			ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                    			cmb.setText(code);
                    			Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
                            }
                        })
                .create().show();
    }
**/
    
    private AlertDialog myDialog = null;
    
    private void showCodeDialog(final String title,final String code,final String content) {
    	myDialog = new AlertDialog.Builder(getActivity()).create();  
        myDialog.show();  
        myDialog.getWindow().setContentView(R.layout.code_dialog);  
        
        myDialog.getWindow()  
            .findViewById(R.id.btn_sure)  
            .setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
    			ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
    			cmb.setText(code);
    			Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_SHORT).show();
    			myDialog.dismiss();
            }  
        });
        
        myDialog.getWindow()  
            .findViewById(R.id.btn_cancel)  
            .setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                myDialog.dismiss();  
            }  
        });
        
        TextView titleTx = (TextView) myDialog.getWindow().findViewById(R.id.title);
        titleTx.setText(title);
        
        TextView codeTx = (TextView) myDialog.getWindow().findViewById(R.id.code_text);
        codeTx.setText(code);
        
        TextView contentTx = (TextView) myDialog.getWindow().findViewById(R.id.content_text);
        contentTx.setText(content);
    }
    
    
}
