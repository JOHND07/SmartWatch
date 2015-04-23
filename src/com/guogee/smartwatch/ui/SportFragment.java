package com.guogee.smartwatch.ui;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.achartengine.chartdemo.demo.chart.AverageCubicTemperatureChart;
import org.achartengine.chartdemo.demo.chart.IDemoChart;
import org.achartengine.chartdemo.demo.chart.SportCharView;

import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.ble.PlusDotBLEDevice;
import com.guogee.smartwatch.ble.StatusChangedCallback;
import com.guogee.smartwatch.dao.BindDeviceDao;
import com.guogee.smartwatch.dao.SportDao;
import com.guogee.smartwatch.utils.ByteDataConvert;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.utils.Log;
import com.guogee.smartwatch.utils.Util;
import com.guogee.smartwatch.widget.CustomProgressDialog;
import com.guogee.smartwatch.widget.DataUtil;
import com.guogee.smartwatch.widget.SportProgressBarSurfaceView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SportFragment extends Fragment implements OnClickListener {

	private ImageButton rightBtn;
	private ImageButton leftBtn;
	private ImageButton timePre;
	private ImageButton timeNext;
	private TextView timeData;
	
	public Handler handler;
	private SportProgressBarSurfaceView sportView;
	private PlusDotBLEDevice plusdotDevice;
	private SportDao sportDao;
	
	public int w;
	public int h;
	private iSmartApplication isApp;
	protected static final int MESSAGE = 123;

	private TextView calorieText;
	private TextView distanceText;
//	private TextView timeText;
	private TextView mainTitle;
	private TextView connectTipText;
	
	private TextView calorieUnit;
	private TextView distanceUnit;
	private TextView sportTimeUnit;
	
	private TextView hourText;
	private TextView minText;
	private TextView hourUnit;
	private TextView minUnit;
	
	private TextView sportNum;
	private TextView targetNum;
	private ProgressBar searchProgress;
	
	private ImageView bateryImg;
	private TextView  bateryText;
	private ProgressBar syncBtn;
	private ImageButton syncImgBtn;
	
	private TextView    syncText;
	private View dividerLine;
	private LinearLayout syncLayout;
	
	private ImageButton sportTargetBtn;
	private ImageButton sportHistoryBtn;
	
	private String nDataNoWeek = null; // 当前日期
	private String aData = null; // 当前选中日期
	private String currentdate = null; // 当前日期
	private DataUtil uData = new DataUtil();
	private SharedPreferences sp;
	
	private LinearLayout bleStatueLy;
	
//	private TasksCompletedView sportViews;
	private List<LinkedHashMap<String, String>> sportData;
	
	private String mCurrentSelectDay ;
	
	private boolean isJump = false;
	
	private TextView sportTextStr;
	private TextView calaliTextStr;
	private TextView distanceTextStr;
	private TextView timeTextStr;
	
	public static SportFragment instantiation(int position) {
		SportFragment fragment = new SportFragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isApp = (iSmartApplication)getActivity().getApplication();
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		Log.e("TAG","SportFragment 's onCreate .................................");
		
		plusdotDevice = PlusDotBLEDevice.getInstance();
		uData.StringData();
		aData = uData.StringData();
		nDataNoWeek = uData.StringDataNoWeek();
		
		//遍历数据库
		sportDao = new SportDao(getActivity());
		sportData = sportDao.listStepMaps(null);
		
//		Log.d("TAG", "SportFragment 's data ======================= "+sportData);
	
		SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy/MM/dd");     
		Date curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
		mCurrentSelectDay = formatter.format(curDate); 
	}
	
	private void updateSportViewBySelectDay(String strDay){
		
//		String sql = "insert into sport (date,stepNum,calorie, distance, stepTime, orders) values (?,?,?,?,?,?)";
		Log.d("TAG", "updateSportViewBySelectDay 's strDay ======================== "+strDay);
		
		String[] params = new String[]{ strDay };	
		LinkedHashMap<String, String> data = sportDao.listStepFromDays(params);
		
		
		if(data.size()>0){
			calorieText.setText(data.get("calorie"));
			sportNum.setText(data.get("stepNum"));
			distanceText.setText(data.get("distance"));
			
//			timeText.setText(data.get("stepTime"));
			
			int currentDuration = Integer.parseInt(data.get("stepTime"));
			if(currentDuration > 60){
				int hour = currentDuration / 60;
				int min  = currentDuration % 60;
				
				hourText.setVisibility(View.VISIBLE);
				hourText.setText(Integer.toString(hour));
				
				hourUnit.setVisibility(View.VISIBLE);
				minText.setText(Integer.toString(min));
			}else{
				hourText.setVisibility(View.GONE);
				hourUnit.setVisibility(View.GONE);
				minText.setText(Integer.toString(currentDuration));
			}
			
			setViewNum(Integer.parseInt(data.get("stepNum")));
			targetNum.setText(data.get("sportTarget"));

		}else{
			calorieText.setText("0");
			sportNum.setText("0");
			distanceText.setText("0");
			
			hourText.setVisibility(View.GONE);
			hourUnit.setVisibility(View.GONE);
			minText.setText("0");
			
			setViewNum(0);
		}

		
/**		
		sportData = sportDao.listStepMaps(null);
		for(Map<String, String> tempData : sportData){
			if(tempData.get("date") != null && tempData.get("date").equals(strDay)){
				
				calorieText.setText(tempData.get("calorie"));
				sportNum.setText(tempData.get("stepNum"));
				distanceText.setText(tempData.get("distance"));
				
//				timeText.setText(tempData.get("stepTime"));
				
				int currentDuration = Integer.parseInt(tempData.get("stepTime"));
				if(currentDuration > 60){
					int hour = currentDuration / 60;
					int min  = currentDuration % 60;
					
					hourText.setVisibility(View.VISIBLE);
					hourText.setText(Integer.toString(hour));
					
					hourUnit.setVisibility(View.VISIBLE);
					minText.setText(Integer.toString(min));
				}else{
					hourText.setVisibility(View.GONE);
					hourUnit.setVisibility(View.GONE);
					minText.setText(Integer.toString(currentDuration));
				}
				
				setViewNum(Integer.parseInt(tempData.get("stepNum")));
				targetNum.setText(tempData.get("sportTarget"));
				
				break;
			}
			else{
				calorieText.setText("0");
				sportNum.setText("0");
				distanceText.setText("0");
				
				hourText.setVisibility(View.GONE);
				hourUnit.setVisibility(View.GONE);
				minText.setText("0");
				
				setViewNum(0);
			}
		}
**/		
		
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Log.e("TAG","SportFragment 's onCreateView .................................");
		
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sport_layout, container, false);
		leftBtn = (ImageButton) root.findViewById(R.id.left_Btn);
		leftBtn.setOnClickListener(this);

		rightBtn = (ImageButton) root.findViewById(R.id.right_Btn);
		rightBtn.setOnClickListener(this);
		
		bateryImg = (ImageView) root.findViewById(R.id.batery_img);
		
		bateryText = (TextView) root.findViewById(R.id.batery_text);
		bateryText.setTypeface(Util.overrideViewFonts(getActivity(), bateryText));
		
		syncBtn = (ProgressBar) root.findViewById(R.id.sync_btn);
		syncBtn.setOnClickListener(this);
		
		syncImgBtn = (ImageButton) root.findViewById(R.id.sync_img_btn);
		syncImgBtn.setOnClickListener(this);
		
		syncText = (TextView) root.findViewById(R.id.sync_text);
		syncText.setTypeface(Util.plusdotTitleFonts(getActivity(), syncText));
		syncText.setOnClickListener(this);
		
		dividerLine = (View) root.findViewById(R.id.divide_line);
		syncLayout = (LinearLayout) root.findViewById(R.id.sync_layout);
		syncLayout.setOnClickListener(this);
		
		calorieText = (TextView) root.findViewById(R.id.calorie_text);
		calorieText.setTypeface(Util.overrideViewFonts(getActivity(), calorieText));
		
		distanceText = (TextView) root.findViewById(R.id.distance_text);
		distanceText.setTypeface(Util.overrideViewFonts(getActivity(), distanceText));
		
		sportTextStr = (TextView) root.findViewById(R.id.sport_text);
		sportTextStr.setTypeface(Util.plusdotTitleFonts(getActivity(), sportTextStr));
		
		calaliTextStr = (TextView) root.findViewById(R.id.calali_text_str);
		calaliTextStr.setTypeface(Util.plusdotTitleFonts(getActivity(), calaliTextStr));
		
		distanceTextStr = (TextView) root.findViewById(R.id.distance_text_str);
		distanceTextStr.setTypeface(Util.plusdotTitleFonts(getActivity(), distanceTextStr));
		
		timeTextStr = (TextView) root.findViewById(R.id.time_text_str);
		timeTextStr.setTypeface(Util.plusdotTitleFonts(getActivity(), timeTextStr));
		
		
//		timeText = (TextView) root.findViewById(R.id.time_text);
//		timeText.setTypeface(Util.overrideViewFonts(getActivity(), timeText));
		
		mainTitle = (TextView) root.findViewById(R.id.mainTitle);
		mainTitle.setTypeface(Util.overrideViewFonts(getActivity(), mainTitle));
		
		connectTipText = (TextView) root.findViewById(R.id.connect_tip);
		connectTipText.setTypeface(Util.plusdotTitleFonts(getActivity(), connectTipText));
		
		sportView = (SportProgressBarSurfaceView) root.findViewById(R.id.sport_progress);
		
		sportNum = (TextView) root.findViewById(R.id.sport_num);
		sportNum.setTypeface(Util.overrideViewFonts(getActivity(), sportNum));
		
		
		calorieUnit   = (TextView) root.findViewById(R.id.calorie_unit);
		calorieUnit.setTypeface(Util.plusdotTitleFonts(getActivity(), calorieUnit));
		
		distanceUnit  = (TextView) root.findViewById(R.id.distance_unit);
		distanceUnit.setTypeface(Util.plusdotTitleFonts(getActivity(), distanceUnit));
		
//		sportTimeUnit = (TextView) root.findViewById(R.id.sport_time_unit);
		
		
		hourText   = (TextView) root.findViewById(R.id.hour_text);
		hourText.setTypeface(Util.overrideViewFonts(getActivity(), hourText));
		
		minText    = (TextView) root.findViewById(R.id.minute_text);
		minText.setTypeface(Util.overrideViewFonts(getActivity(), minText));
		
		hourUnit   = (TextView) root.findViewById(R.id.hour_unit);
		hourUnit.setTypeface(Util.plusdotTitleFonts(getActivity(), hourUnit));
		hourUnit.setVisibility(View.GONE);
		
		minUnit    = (TextView) root.findViewById(R.id.min_unit);
		minUnit.setTypeface(Util.plusdotTitleFonts(getActivity(), minUnit));
		
		
		targetNum = (TextView) root.findViewById(R.id.target_num);
//		sportNum.setTypeface(Util.overrideViewFonts(getActivity(), sportNum));
		targetNum.setTypeface(Util.plusdotTitleFonts(getActivity(), targetNum));
//		targetNum.setText(Integer.toString(sp.getInt(Constant.NUM_KEY, 10000)));
		
//		sportViews = (TasksCompletedView) root.findViewById(R.id.sport_progress);
//		sportViews.setProgress(0);
		
		timePre = (ImageButton) root.findViewById(R.id.time_pre);
		timePre.setOnClickListener(this);
		
		timeNext = (ImageButton) root.findViewById(R.id.time_next);
		timeNext.setOnClickListener(this);
		
		sportTargetBtn = (ImageButton) root.findViewById(R.id.sport_target_btn);
		sportTargetBtn.setOnClickListener(this);
		
		sportHistoryBtn = (ImageButton) root.findViewById(R.id.sport_history_btn);
		sportHistoryBtn.setOnClickListener(this);
		
		timeData = (TextView) root.findViewById(R.id.time_data);
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");    
//		String date=sdf.format(new java.util.Date());
//		timeData.setTypeface(Util.DateTitleFonts(getActivity(), timeData));
		timeData.setText(getResources().getString(R.string.str_today));
		
		searchProgress = (ProgressBar) root.findViewById(R.id.search_progress);
		//if the app connect the smart-watch,hide the progress hint.
//		updateSportView();
		
		bleStatueLy = (LinearLayout) root.findViewById(R.id.ble_statue_ly);
		BindDeviceDao dao = new BindDeviceDao(getActivity());
		List<Map<String, String>> daoList = dao.listBleDevice(null);
		if(daoList.size()!=0){
			bleStatueLy.setVisibility(View.VISIBLE);
		}
		
		isApp.addCallBack(new SportUIStatusChangedCallback());
		
		return root;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("TAG","SportFragment 's onPause ................................."+isJump);
//		if(isJump){
//			isJump = false;
//		}else{
//			sportView.invalidate();
//			sportView.setVisibility(View.INVISIBLE);	
//		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.e("TAG","SportFragment 's onResume ................................."+isApp.getLeftMenuTag());
		
		super.onResume();
//		if(isApp.getLeftMenuTag() == Constant.SPORT_TAG){
//			sportView.setVisibility(View.VISIBLE);
//			targetNum.setText(Integer.toString(sp.getInt(Constant.NUM_KEY, 10000)));			
//		}
		
		targetNum.setText(Integer.toString(sp.getInt(Constant.NUM_KEY, 10000)));	
		
		Log.e("TAG","SportFragment 's onResume ................................."+isApp.getConnectBleDevice());
		
		updateState();
	}	
	
	private void updateState(){
		if(isApp.getConnectBleDevice()){
			bleStatueLy.setVisibility(View.VISIBLE);
			hideProgress();
			updateBatteryState(isApp.getEnerggyNum());
		}else{
			showProgress();
			updateSportViewBySelectDay(mCurrentSelectDay);
		}
		
		syncText.setText(getActivity().getResources().getString(isApp.getConnectBleDevice() == true ? R.string.sync : R.string.syncing));
		
		isApp.addCallBack(new SportUIStatusChangedCallback());
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view.equals(leftBtn)){
			isApp.getSlidingMenu().showMenu();
		}else if(view.equals(rightBtn)){
			isApp.getSlidingMenu().showSecondaryMenu();
		}
		
		else if(view.equals(timePre)){
			currentdate=DataUtil.getDateStrYHD(nDataNoWeek, -1);
			nDataNoWeek = DataUtil.getDateStr(nDataNoWeek, -1);
			aData = uData.getWeek(nDataNoWeek);
			
			mCurrentSelectDay = aData;
			
			if (nDataNoWeek.equals(DataUtil.getDateStr(uData.StringDataNoWeek(),-1))) {
				//update sport 's data
				updateSportViewBySelectDay(DataUtil.getDateStr(uData.StringDataNoWeek(),-1));
				timeData.setText(getActivity().getResources().getString(R.string.str_yesterday));
			} else {
				//update sport 's data
				updateSportViewBySelectDay(aData);
				timeData.setText(aData);
			}
		}
		
		else if(view.equals(timeNext)){
			if (nDataNoWeek.equals(uData.StringDataNoWeek())) {
				Toast toast = Toast.makeText(getActivity(), "已经是最新日期", Toast.LENGTH_SHORT);
			} else {
				currentdate=DataUtil.getDateStrYHD(nDataNoWeek, 1);
				nDataNoWeek = DataUtil.getDateStr(nDataNoWeek, 1);
				aData = uData.getWeek(nDataNoWeek);
				
				mCurrentSelectDay = aData;
				
				if (nDataNoWeek.equals(uData.StringDataNoWeek())) {
					//update sport 's data
					updateSportViewBySelectDay(uData.StringDataNoWeek());
					timeData.setText(getActivity().getResources().getString(R.string.str_today));
					
				} else if (nDataNoWeek.equals(DataUtil.getDateStr(uData.StringDataNoWeek(),-1))) {
					//update sport 's data
					updateSportViewBySelectDay(DataUtil.getDateStr(uData.StringDataNoWeek(),-1));
					timeData.setText(getActivity().getResources().getString(R.string.str_yesterday));
				} else {
					//update sport 's data
					updateSportViewBySelectDay(aData);
					timeData.setText(aData);
				}
			}
		}
		else if(view.equals(sportTargetBtn)){
			//设置跳转
			isJump = true;
			startActivity(new Intent(getActivity(), ExerciseActivity.class));
		}else if(view.equals(sportHistoryBtn)){
            //jump history UI
//			Intent intent = mCharts.execute(getActivity());

			//设置跳转
			isJump = true;
			Intent intent = new Intent(getActivity(), SportCharView.class);
			startActivity(intent);
			
		}else if(view.equals(syncLayout)){
			sentSyncSportCmd();
		}else if(view.equals(syncBtn)){
//			synData();
//			setViewNum();
//			sportViews.setProgress(40);
		}else if(view.equals(syncText)){
			sentSyncSportCmd();
		}else if(view.equals(syncImgBtn)){
			sentSyncSportCmd();
		}
	}
	
	private IDemoChart mCharts = new AverageCubicTemperatureChart();
	
	private int hour0 ;
	private int hour1 ;
	private int hour2 ;
	private int hour3 ;
	private int hour4 ;
	private int hour5 ;
	private int hour6 ;
	private int hour7 ;
	private int hour8 ;
	private int hour9 ;
	private int hour10 ;
	private int hour11 ;
	private int hour12 ;
	private int hour13 ;
	private int hour14 ;
	private int hour15 ;
	private int hour16 ;
	private int hour17 ;
	private int hour18 ;
	private int hour19 ;
	private int hour20 ;
	private int hour21 ;
	private int hour22 ;
	private int hour23 ;
	
	public void updateBleStatueLy(){
		bleStatueLy.setVisibility(View.INVISIBLE);
		Toast toast= Toast.makeText(getActivity(), getActivity().getString(R.string.bind_tip), Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,toast.getYOffset() / 2);
		toast.show();
	}
	
	public void updateSubPackage(byte[] data){
		
		int len = ByteDataConvert.BinToInt(data, 3, 1);
//		Log.d("TAG", "len =============== "+len);
		int serial = ByteDataConvert.BinToIntByLow(data, 4, 2);
//		Log.d("TAG", "serial =============== "+serial);
		int data0 =  ByteDataConvert.BinToIntByLow(data, 6, 2);
//		Log.d("TAG", "data0 =============== "+data0);
		int data1 =  ByteDataConvert.BinToIntByLow(data, 8, 2);
//		Log.d("TAG", "data1 =============== "+data1);
		int data2 =  ByteDataConvert.BinToIntByLow(data, 10, 2);
//		Log.d("TAG", "data2 =============== "+data2);
		int data3 =  ByteDataConvert.BinToIntByLow(data, 12, 2);
//		Log.d("TAG", "data3 =============== "+data3);
		int data4 =  ByteDataConvert.BinToIntByLow(data, 14, 2);
//		Log.d("TAG", "data4 =============== "+data4);
		
		
//		Log.d("TAG", "SportFragment 's len ====== "+len+"  serial ======= "+serial+" data0 ======= "+data0+" data1 ======= "+data1+" data2 ======= "+data2+" data3 ======= "+data3+" data4 ======= "+data4);
		
		
		if(serial == 1){
			hour0 = data0;
			hour1 = data1;
			hour2 = data2;
			hour3 = data3;
			hour4 = data4;
		}else if(serial == 2){
			hour5 = data0;
			hour6 = data1;
			hour7 = data2;
			hour8 = data3;
			hour9 = data4;			
		}else if(serial == 3){
			hour10 = data0;
			hour11 = data1;
			hour12 = data2;
			hour13 = data3;
			hour14 = data4;
		}else if(serial == 4){
			hour15 = data0;
			hour16 = data1;
			hour17 = data2;
			hour18 = data3;
			hour19 = data4;
		}else if(serial == 5){
			hour20 = data0;
			hour21 = data1;
			hour22 = data2;
			hour23 = data3;
			
			hideProgress();
			
			if(sportData.size()==0){
				
				Object[] params = new Object[] { currentTime, monthstr,step, calorieStr,distanceStr,durations, hour0, hour1, hour2, hour3, hour4, 
						hour5, hour6, hour7, hour8, hour9, hour10, hour11, hour12, hour13, hour14, hour15, hour16, hour17, hour18, hour19, 
						hour20, hour21, hour22, hour23 ,sp.getInt(Constant.NUM_KEY, 10000), 0};
				saveSportData(params);
				
			}else{
				sportData = sportDao.listStepMaps(null);
				for(int i=0; i<sportData.size(); i++){
					if(sportData.get(i).get("date").equals(currentTime)){
						Object[] param = new Object[] { currentTime, monthstr,step, calorieStr,distanceStr,durations,hour0, hour1, hour2, hour3, hour4, 
								hour5, hour6, hour7, hour8, hour9, hour10, hour11, hour12, hour13, hour14, hour15, hour16, hour17, hour18, hour19, 
								hour20, hour21, hour22, hour23 , 0, sportData.get(i).get("id")};
						sportDao.updateStep(param);
						break;
					}else {
						if(i == sportData.size()-1){
							Object[] params = new Object[] { currentTime, monthstr, step, calorieStr,distanceStr,durations, hour0, hour1, hour2, hour3, hour4, 
									hour5, hour6, hour7, hour8, hour9, hour10, hour11, hour12, hour13, hour14, hour15, hour16, hour17, hour18, hour19, 
									hour20, hour21, hour22, hour23 , sp.getInt(Constant.NUM_KEY, 10000), 0};
							saveSportData(params);						
						}
					}
				}
			}
		}
		Log.e("TAG", "serial =========== "+serial + " mDays ========= "+mDays+" mTotal ============ "+mTotal);
//		if((serial == mTotal) && (mDays == 1)){
//			Log.e("TAG","serial 's disappearSyncProgressDialog ...............");
//			disappearSyncProgressDialog();
//		}
	}
	
	public void updateBattery(byte[] data){
		int energy = ByteDataConvert.BinToInt(data, 3, 1);
		
		int firmVersion = ByteDataConvert.BinToInt(data, 4, 1);
		
		if(sp.getInt(Constant.FIRMWARE_VERSION, 0)!=firmVersion){
			sp.edit().putInt(Constant.FIRMWARE_VERSION, firmVersion).commit();
//			plusdotDevice.BindCommand(true);
			isApp.setFirmVersion(firmVersion);
			
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					//sync sport data.
				    sentSyncSportCmd();		
				}
			}, 2000);
		
		}else{
			//sync sport data.
		    sentSyncSportCmd();	
		}
		
		
		Log.e("TAG","version =================== "+firmVersion);
		
		bateryText.setText(Integer.toString(energy)+"%");
		if(energy>=60 && energy<=100){
			bateryImg.setBackgroundResource(R.drawable.zq_batery_1);
		}else if(40<energy && energy<60){
			bateryImg.setBackgroundResource(R.drawable.zq_batery_3);
		}else if(10<energy && energy<=40){
			bateryImg.setBackgroundResource(R.drawable.zq_batery_4);
		}else if(10<=energy){
			bateryImg.setBackgroundResource(R.drawable.zq_batery_5);
		}
		isApp.setEnerggyNum(energy);
//		showSyncProgressDialog();
	}
	
	public void updateBatteryState(int energy){
		bateryText.setText(Integer.toString(energy)+"%");
		if(energy>=60 && energy<=100){
			bateryImg.setBackgroundResource(R.drawable.zq_batery_1);
		}else if(40<energy && energy<60){
			bateryImg.setBackgroundResource(R.drawable.zq_batery_3);
		}else if(10<energy && energy<=40){
			bateryImg.setBackgroundResource(R.drawable.zq_batery_4);
		}else if(10<=energy){
			bateryImg.setBackgroundResource(R.drawable.zq_batery_5);
		}
	}
	
	private int step;
	private int durations;
	private String currentTime, monthstr,calorieStr,distanceStr;
	
	//包的个数
	private int mTotal = 0;
	private int mDays = 0;
	
    public void updateSportData(byte[] data){
    	
    	Log.d("TAG","sportFragment updateSportData ..................... ");
		
    	if(data==null){
    		return;
    	}
    	
    	int year = ByteDataConvert.BinToIntByLow(data, 3, 2);//bug
    	int month = ByteDataConvert.BinToInt(data, 5, 1);
    	String monthStr = String.format("%02d",month); 
		int day = ByteDataConvert.BinToInt(data, 6, 1);
    	String dayStr = String.format("%02d",day); 
	
	    step = ByteDataConvert.BinToIntByLow(data, 7, 4);
		durations = ByteDataConvert.BinToIntByLow(data, 11, 2);		
		int totals = ByteDataConvert.BinToIntByLow(data, 13, 2);
		int days = ByteDataConvert.BinToIntByLow(data, 15, 1);
		
		Log.d("TAG", "year === "+year+"   "+"month === "+month+"  "+"day === "+day+"  "+"step === "+step+"  "+"duration ==="+durations+" "+"totals === "+totals+"  "+"days === "+days);

		mTotal = totals;
		mDays = days;
		
		float calorie = calCalorie(step);
//		float distance = calDistance(step);
		
		float distance = 0;
		
		if(getResources().getConfiguration().locale.getCountry().equals("zh-CN") || 
				getResources().getConfiguration().locale.getCountry().equals("zh-TW") ||
				getResources().getConfiguration().locale.getCountry().equals("CN") ){

			distance = calDistance(step);
		}else{
		
			distance = calDistanceByMi(step);
		}

		
		DecimalFormat decimalFormat=new DecimalFormat("0");
		distanceStr = decimalFormat.format(distance);
		
//		distanceStr = Float.toString(distance);
		
		decimalFormat=new DecimalFormat("0");
		calorieStr = decimalFormat.format(calorie);
		currentTime = Integer.toString(year)+"/"+monthStr+"/"+dayStr;
		monthstr = monthStr;
		
		Log.d("TAG", "mCurrentSelectDay ======= "+mCurrentSelectDay+"  currentTime ======= "+currentTime);

		if(mCurrentSelectDay.equals(currentTime)){
			// test,end
			setViewNum(step);
			
			//
			if(getResources().getConfiguration().locale.getCountry().equals("zh-CN") || 
					getResources().getConfiguration().locale.getCountry().equals("zh-TW") ||
					getResources().getConfiguration().locale.getCountry().equals("CN") ){

				if(Integer.parseInt(distanceStr)>10000){
					int currentDis = Integer.parseInt(distanceStr) / 1000;
					distanceText.setText(Integer.toString(currentDis));
				    distanceUnit.setText(getActivity().getResources().getString(R.string.kilometer_unit));
				}else{
					decimalFormat=new DecimalFormat("#.00");
					distanceStr = decimalFormat.format(distance);
					
					distanceText.setText(distanceStr);	
					distanceUnit.setText(getActivity().getResources().getString(R.string.meter_unit));
				}
				
			}else{
				
				decimalFormat=new DecimalFormat("0.000");
				distanceStr = decimalFormat.format(distance);
				
				if(step < 20){
					distanceText.setText("0");	
				}else{
					distanceText.setText(distanceStr);		
				}
				
				distanceUnit.setText(getActivity().getResources().getString(R.string.meter_unit));
			}
			
			if(durations > 60){
				int hour = durations / 60;
				int min  = durations % 60;
				
				hourText.setVisibility(View.VISIBLE);
				hourText.setText(Integer.toString(hour));
				
				hourUnit.setVisibility(View.VISIBLE);
				minText.setText(Integer.toString(min));
			}else{
//				timeText.setText(Integer.toString(durations));	
				hourText.setVisibility(View.GONE);
				hourUnit.setVisibility(View.GONE);
				minText.setText(Integer.toString(durations));
			}
			
//			timeText.setText(Integer.toString(durations));
			calorieText.setText(calorieStr);
//			mCurrentSelectDay = currentTime;
//			timeData.setText(currentTime);
		}
		
		updateProgresssBar(true);
//		disappearSyncProgressDialog();
    }
	
	public void hideProgress(){
		searchProgress.setVisibility(View.GONE);
		connectTipText.setText(getActivity().getResources().getString(R.string.connected_tip));
		
		dividerLine.setVisibility(View.VISIBLE);
		bateryImg.setVisibility(View.VISIBLE);
//		bateryText.setVisibility(View.VISIBLE);
		syncLayout.setVisibility(View.VISIBLE);
		
//		showSyncProgressDialog();
//		disappearSyncProgressDialog();
	}
	
	public void showProgress(){
		searchProgress.setVisibility(View.VISIBLE);
		connectTipText.setText(getActivity().getResources().getString(R.string.connect_tip));
		
		dividerLine.setVisibility(View.GONE);
		bateryImg.setVisibility(View.GONE);
		bateryText.setVisibility(View.GONE);
		syncLayout.setVisibility(View.GONE);
		
//		disappearSyncProgressDialog();
	}
	
//	private ProgressDialog progressDialog;
	
	private CustomProgressDialog progressDialog = null;
	
	public void showSyncProgressDialog(){
//		progressDialog = ProgressDialog.show(getActivity(), "正在同步", "请稍等", true, false);
		if(progressDialog == null){
			progressDialog = CustomProgressDialog.createDialog(getActivity());
	    	progressDialog.setMessage(getActivity().getResources().getString(R.string.loading));
	    	progressDialog.show();			
		}
	}
	
	public void disappearSyncProgressDialog(){
		if(progressDialog!=null){
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	
	private void saveSportData(Object[] params){
		sportDao.addStep(params);
	}
	
	////////////////////////////////////////////////////////////
	private ProgressBarRunnable runnable;
	private int strokeBlockPercent 		= 0;
	private int strokeBlockPercentMax 	= 20;
	private int strokeBlockPropressNum	= 8;
	private int goal					= 0;
	
	/**
	 * 进度到num，
	 * style = RoundProgressBar.STROKE_BLOCK num 无效
	 * @param num
	 */
	private synchronized void setProgressNum(int num, int style){
		
		if(runnable == null){
			runnable = new ProgressBarRunnable(num, style);
			new Thread(runnable).start();
		}else{
			runnable.stop();
			runnable = new ProgressBarRunnable(num, style);
			new Thread(runnable).start();
		}
	}
	
	/**
	 * 进度到num ，，线程刷新
	 * @param num
	 */
	private class ProgressBarRunnable implements Runnable{

		private int num = 0;
		private int progress;
		private int progressMax = 0;
		private boolean isStop = false;
		private boolean forward = true;
		private int style = 0;
		private int delay	= 10;
		private int count;
		
		public ProgressBarRunnable(int progress, int style){
			this.progress 		= progress;
			this.style			= style;
			this.progressMax	= sportView.getMax();
			sportView.setStyle(style);
			if(style == sportView.STROKE_BLOCK){
				this.num 	= sportView.getMinProgress();
				this.delay 	= 5;
//				sportView.setTitle(getString(R.string.router_complete_prepress));
//				sportView.setProgressUnit("%");
			}else{
//				sportView.setTitle(getString(R.string.router_health_index));
//				sportView.setProgressUnit(getString(R.string.router_mins));
			}
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			while(!isStop){
			try {
				if(sportView == null)return ;
				if((style != sportView.STROKE_BLOCK)&& (num > progressMax)){
					break;
				}
				if(style != sportView.STROKE_BLOCK){
					num += 3;
				}else{
					if(forward){
						num += 4;
						if(num > progressMax){
							num 	= progressMax;
							forward = false;
						}
					}else{
						num -= 4;
						if(num < sportView.getMinProgress()){
							num = sportView.getMinProgress();
							forward = true;
						}
					}
				}
				
				if(style != sportView.STROKE_BLOCK){
					if(progress > num){
						sportView.setProgress(num);
					}else{
						sportView.setProgress(progress);
						break;
					}
				}else{
					sportView.setProgress(num);
					count++;
					if(count > strokeBlockPropressNum){
						count = 0;
						strokeBlockPercent++;
						if(strokeBlockPercent > strokeBlockPercentMax){
							strokeBlockPercent = strokeBlockPercentMax;
						}
					}
					sportView.setProgressNum(strokeBlockPercent);
				}
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}catch(NullPointerException e){
					e.printStackTrace();
				}
			}
		}
		
		public void stop(){
			isStop = true;
		}
	}
	
    private void setViewNum(final int num){
		int delay = 500; //默认延迟1秒
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
//				Log.d("TAG","setViewNum ======================= "+(int)((float)goal * ((float)sportView.getMax() / 100.0)));
	
				//demo test
				if(isApp.getLeftMenuTag()==Constant.SPORT_TAG){
					sportNum.setText(Integer.toString(num));
					int percent = num*240 / sp.getInt(Constant.NUM_KEY, 10000);
					setProgressNum(percent, sportView.STROKE);	
				}
				//demo test
			}
		}, delay);
	}
    
    ///////////////////////////////////////////////////////////////
//    public static Handler mHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 0:
////				hideProgress();
//				break;
//			}
//		}
//	};
	
	/**
	 * syn data
	 */
	public void sentSyncSportCmd(){
		if(isApp.getConnectBleDevice()){
//			plusdotDevice.synchronizationDeviceInfo(true);
			plusdotDevice.synchronizationSportBeginData(true);
			
			updateProgresssBar(false);
//			showSyncProgressDialog();
		}
	}
	
	public void sentSyncTime(){
		if(isApp.getConnectBleDevice()){
			plusdotDevice.synchronizationTime(true);
		}
	}
	
	public void sentSyncBatteryInfo(){
		if(isApp.getConnectBleDevice()){
			plusdotDevice.synchronizationDeviceInfo(true);
		}
	}
	
	/**
	 * 更新 progressbar state
	 * @param state
	 */
	public void updateProgresssBar(boolean state){
		syncBtn.setVisibility(state == true ? View.GONE : View.VISIBLE);
		syncImgBtn.setVisibility(state ==  true ? View.VISIBLE : View.GONE);	
		syncText.setText(getActivity().getResources().getString(state ==  true ?  R.string.sync : R.string.syncing));
	}
	
	/**
	* 距离计算
	* 步幅:stride = (0.415 x Height for male), (0.413 x Height for female) 距离: distances = stride*steps
	* 1mile = 63360inch
	* 1km = 100000cm
	*/
	public float calDistance(int steps){
		float distance = 0;
		int height = Integer.parseInt(sp.getString(Constant.USER_HEIGHT, "175"));
		if(sp.getString(Constant.GENDER, "true").equals("true")){
			distance = (float)(0.415 * height)*steps; 
		}else {
			distance = (float)(0.413 * height)*steps;
		}
		//转换米
		return distance/100;
	}
	
	/**
	 * 距离转化英里.
	 * @param steps
	 * @return
	 */
	public float calDistanceByMi(int steps){
		float distance = 0;
		int height = Integer.parseInt(sp.getString(Constant.USER_HEIGHT, "175"));
		if(sp.getString(Constant.GENDER, "true").equals("true")){
			distance = (float)(0.415 * height)*steps; 
		}else {
			distance = (float)(0.413 * height)*steps;
		}
		//转换英里
//		return distance/100;
		
		float ss = distance / 100;
		
		return (float) (ss / 1609.344);
	}
	

	public float calCalorie(int step){
		float calorie = 0;
		int weight = Integer.parseInt(sp.getString(Constant.USER_WEIGHT, "60"));
		calorie = (float) ((weight-15)*0.000693+0.005895);
		return calorie*step;
	}
	
	// UI CallBack
//	private Handler uihandler=null;
	
	 public Handler uihandler = new Handler() {
			public void handleMessage(Message msg) {
				Bundle data = msg.getData();
				switch (msg.what) {
				case MainActivity.MSG_BLE_CONNECTED:
		        	hideProgress();
//		        	//sync  battery info if the app is connect to ble device.
//					showSyncProgressDialog();
					
					
//				    sentSyncSportCmd();					
//					plusdotDevice.synchronizationTime(true); // 0x01,sync time
					break;
				case MainActivity.MSG_BLE_DISCONNECTED:
					//更新界面  
		        	showProgress();
					break;
				case MainActivity.MSG_UPDATE_SPORT_DATA:
					updateSportData(data.getByteArray("data"));
					break;
				case MainActivity.MSG_SUB_PACKAGE:
					updateSubPackage(data.getByteArray("data"));
					break;
				case MainActivity.MSG_BLE_DEVICE_INFO:
					updateBattery(data.getByteArray("data"));
					//sync sport data.
//				    sentSyncSportCmd();		
					break;
				case MainActivity.MSG_BLE_SYNC_TIME:
					sentSyncBatteryInfo();	
		        	//sync  battery info if the app is connect to ble device.
//					showSyncProgressDialog();
					break;
				case MSG_SHOW_SPORTVIEW:
					if(isApp.getLeftMenuTag() == Constant.SPORT_TAG){
						sportView.setVisibility(View.VISIBLE);
						targetNum.setText(Integer.toString(sp.getInt(Constant.NUM_KEY, 10000)));			
					}
					
					updateState();
					
					break;
				case MSG_HIDE_SPORTVIEW:
					sportView.invalidate();
					sportView.setVisibility(View.INVISIBLE);
					break;
				}
			}
	 };
	
	 
	class SportUIStatusChangedCallback implements StatusChangedCallback{
		@Override
		public void callbackCall(int command, byte[] dataPacket) {
			// TODO Auto-generated method stub
			Log.d("TAG", "sportFragment 's callbackCall..................."+command);
			
			Message msg = uihandler.obtainMessage(command); 
			Bundle sportdata = new Bundle();
            sportdata.putByteArray("data", dataPacket);
            msg.setData(sportdata);
            
			uihandler.sendMessage(msg);
		}

		@Override
		public void callbackFail(int command, byte[] dataPacket) {
			// TODO Auto-generated method stub
			Log.d("TAG", "sportFragment 's callbackFail..................."+command);

			Message msg = uihandler.obtainMessage(command); 
			uihandler.sendMessage(msg);
		}
	}
	
	////////////////////// test /////////////////////////
	public final static int MSG_HIDE_SPORTVIEW = 20;
	
	public final static int MSG_SHOW_SPORTVIEW = 21;
	
	
	
}
