package com.guogee.smartwatch.ui;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.achartengine.chartdemo.demo.chart.SleepCharView;
import org.achartengine.chartdemo.demo.chart.TestActicity;

import com.guogee.smartwatch.MainActivity;
import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;
import com.guogee.smartwatch.ble.BluetoothLeService;
import com.guogee.smartwatch.ble.PlusDotBLEDevice;
import com.guogee.smartwatch.ble.StatusChangedCallback;
import com.guogee.smartwatch.dao.BindDeviceDao;
import com.guogee.smartwatch.dao.SleepDao;
import com.guogee.smartwatch.utils.ByteDataConvert;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.utils.Log;
import com.guogee.smartwatch.utils.Util;
import com.guogee.smartwatch.widget.CustomProgressDialog;
import com.guogee.smartwatch.widget.DataUtil;
import com.guogee.smartwatch.widget.SleepProgressBarSurfaceView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SleepFragment extends Fragment implements OnClickListener{

	private iSmartApplication isApp;
	private ImageButton rightBtn;
	private ImageButton leftBtn;
	
	private TextView deepSleepHourText;
	private TextView deepSleepMinuteText;
	private TextView deepSleepHourTextUnit;
	private TextView deepSleepMinuteTextUnit;
	
	
	private TextView lightSleepHourText;
	private TextView lightSleepMinText;
	private TextView lightSleepHourTextUnit;
	private TextView lightSleepMinTextUnit;
	
	private TextView wakeUpHourText;
	private TextView wakeUpMinText;
	private TextView wakeUpHourTextUnit;
	private TextView wakeUpMinTextUnit;
	
	private TextView mainTitle;
	private TextView timeData;
	private TextView hourNum;
	private TextView minuteNum;
	
	private ImageView bateryImg;
	private TextView  bateryText;
	private ProgressBar syncBtn;
	
	private TextView    syncText;
	private View dividerLine;
	private TextView connectTipText;
	private ProgressBar searchProgress;
	private LinearLayout syncLayout;
	
	private ImageButton sleepRemindBtn;
	private ImageButton sleepHistoryBtn;
	private SleepProgressBarSurfaceView sleepView;
	
	private String nDataNoWeek = null; // 当前日期
	private String aData = null; // 当前选中日期
	private String currentdate = null; // 当前日期
	private DataUtil uData = new DataUtil();
	
	private ImageButton timePre;
	private ImageButton timeNext;
	private PlusDotBLEDevice plusdotDevice;
	private ImageButton syncImgBtn;
	private String mCurrentSelectDay ;
	
	private SleepDao sleepDao;
	private List<Map<String, String>> sleepData;
	private String currentTime;
	
	private boolean isJump = false;
	
	private LinearLayout bleStatueLy;
	
	private TextView sportTextStr;
	private TextView deepSleepStr;
	private TextView lightSleepStr;
	private TextView wakeUpStr;
	
	private TextView hourNumStr;
	private TextView minuteNumStr;
	
	public static SleepFragment instantiation(int position) {
		SleepFragment fragment = new SleepFragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isApp = (iSmartApplication)getActivity().getApplication();
		Log.e("TAG","SleepFragment 's onCreate .................................");
		
		uData.StringData();
		aData = uData.StringData();
		nDataNoWeek = uData.StringDataNoWeek();
		
		SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy/MM/dd");     
		Date curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
		mCurrentSelectDay = formatter.format(curDate); 
		
		//遍历数据库
		sleepDao = new SleepDao(getActivity());
//		sleepData = sleepDao.listSleepMaps(null);
		
//		Log.d("TAG", "sleepData ======================== "+sleepData);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e("TAG","SleepFragment 's onCreateView .................................");
		
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sleep_layout, container, false);
		
		leftBtn = (ImageButton) root.findViewById(R.id.left_Btn);
		leftBtn.setOnClickListener(this);

		rightBtn = (ImageButton) root.findViewById(R.id.right_Btn);
		rightBtn.setOnClickListener(this);
		
		timePre = (ImageButton) root.findViewById(R.id.time_pre);
		timePre.setOnClickListener(this);
		
		timeNext = (ImageButton) root.findViewById(R.id.time_next);
		timeNext.setOnClickListener(this);
		
		
		mainTitle = (TextView) root.findViewById(R.id.mainTitle);
		mainTitle.setTypeface(Util.overrideViewFonts(getActivity(), mainTitle));
		
		bateryImg = (ImageView) root.findViewById(R.id.batery_img);
		
		bateryText = (TextView) root.findViewById(R.id.batery_text);
		bateryText.setTypeface(Util.plusdotTitleFonts(getActivity(), bateryText));
		
		syncBtn = (ProgressBar) root.findViewById(R.id.sync_btn);
		
		syncText = (TextView) root.findViewById(R.id.sync_text);
		syncText.setTypeface(Util.plusdotTitleFonts(getActivity(), syncText));
		
		dividerLine = (View) root.findViewById(R.id.divide_line);
		
		connectTipText = (TextView) root.findViewById(R.id.connect_tip);
		connectTipText.setTypeface(Util.plusdotTitleFonts(getActivity(), connectTipText));
		
		deepSleepStr = (TextView) root.findViewById(R.id.deep_sleep_str);
		deepSleepStr.setTypeface(Util.plusdotTitleFonts(getActivity(), deepSleepStr));

		lightSleepStr = (TextView) root.findViewById(R.id.light_sleep_str);
		lightSleepStr.setTypeface(Util.plusdotTitleFonts(getActivity(), lightSleepStr));

		wakeUpStr = (TextView) root.findViewById(R.id.wake_up_str);
		wakeUpStr.setTypeface(Util.plusdotTitleFonts(getActivity(), wakeUpStr));

		sportTextStr = (TextView) root.findViewById(R.id.sport_text);
		sportTextStr.setTypeface(Util.plusdotTitleFonts(getActivity(), sportTextStr));
		
		hourNumStr = (TextView) root.findViewById(R.id.hour_num_str);
		hourNumStr.setTypeface(Util.plusdotTitleFonts(getActivity(), hourNumStr));
		
		minuteNumStr = (TextView) root.findViewById(R.id.minute_num_str);
		minuteNumStr.setTypeface(Util.plusdotTitleFonts(getActivity(), minuteNumStr));

		
		searchProgress = (ProgressBar) root.findViewById(R.id.search_progress);
		syncLayout = (LinearLayout) root.findViewById(R.id.sync_layout);
		syncLayout.setOnClickListener(this);
		
		deepSleepHourText = (TextView) root.findViewById(R.id.deepsleep_hour_text);
		deepSleepHourText.setTypeface(Util.overrideViewFonts(getActivity(), deepSleepHourText));
		
		deepSleepHourTextUnit = (TextView) root.findViewById(R.id.deepsleep_hour_text_unit);
		deepSleepHourTextUnit.setTypeface(Util.plusdotTitleFonts(getActivity(), deepSleepHourTextUnit));
		
		deepSleepMinuteText = (TextView) root.findViewById(R.id.deepsleep_minute_text);
		deepSleepMinuteText.setTypeface(Util.overrideViewFonts(getActivity(), deepSleepMinuteText));
		
		deepSleepMinuteTextUnit = (TextView) root.findViewById(R.id.deepsleep_minute_text_unit);
		deepSleepMinuteTextUnit.setTypeface(Util.plusdotTitleFonts(getActivity(), deepSleepMinuteTextUnit));
		
		lightSleepHourText = (TextView) root.findViewById(R.id.lightsleep_hour_text);
		lightSleepHourText.setTypeface(Util.overrideViewFonts(getActivity(), lightSleepHourText));
		
		lightSleepHourTextUnit = (TextView) root.findViewById(R.id.lightsleep_hour_text_unit);
		lightSleepHourTextUnit.setTypeface(Util.plusdotTitleFonts(getActivity(), lightSleepHourTextUnit));
		
		lightSleepMinText = (TextView) root.findViewById(R.id.lightsleep_minute_text);
		lightSleepMinText.setTypeface(Util.overrideViewFonts(getActivity(), lightSleepMinText));
		
		lightSleepMinTextUnit = (TextView) root.findViewById(R.id.lightsleep_minute_text_unit);
		lightSleepMinTextUnit.setTypeface(Util.plusdotTitleFonts(getActivity(), lightSleepMinTextUnit));
		
		wakeUpHourText = (TextView) root.findViewById(R.id.wakeup_hour_text);
		wakeUpHourText.setTypeface(Util.overrideViewFonts(getActivity(), wakeUpHourText));
		
		wakeUpHourTextUnit = (TextView) root.findViewById(R.id.wakeup_hour_text_unit);
		wakeUpHourTextUnit.setTypeface(Util.plusdotTitleFonts(getActivity(), wakeUpHourTextUnit));
		
		wakeUpMinText = (TextView) root.findViewById(R.id.wakeup_minute_text);
		wakeUpMinText.setTypeface(Util.overrideViewFonts(getActivity(), wakeUpMinText));
		
		wakeUpMinTextUnit = (TextView) root.findViewById(R.id.wakeup_minute_text_unit);
		wakeUpMinTextUnit.setTypeface(Util.plusdotTitleFonts(getActivity(), wakeUpMinTextUnit));
		
		hourNum = (TextView) root.findViewById(R.id.hour_num);
		hourNum.setTypeface(Util.overrideViewFonts(getActivity(), hourNum));
		
		minuteNum = (TextView) root.findViewById(R.id.minute_num);
		minuteNum.setTypeface(Util.overrideViewFonts(getActivity(), minuteNum));
		
		sleepRemindBtn = (ImageButton) root.findViewById(R.id.sleep_remind_btn);
		sleepRemindBtn.setOnClickListener(this);
		
		sleepHistoryBtn = (ImageButton) root.findViewById(R.id.sleep_history_btn);
		sleepHistoryBtn.setOnClickListener(this);
		sleepView = (SleepProgressBarSurfaceView) root.findViewById(R.id.sleep_progress);
		
		
		sleepView.setDeepSleepColor(getActivity().getResources().getColor(R.color.deeplightSleep));
//		sleepView.setLightSleepColor(0xF3EC83);
//		sleepView.setWakeColor(0x76F5FF);
		
		
		timeData = (TextView) root.findViewById(R.id.time_data);
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");    
//		String date=sdf.format(new java.util.Date());
//		timeData.setTypeface(Util.DateTitleFonts(getActivity(), timeData));
		timeData.setText(getResources().getString(R.string.str_today));
		
		syncImgBtn = (ImageButton) root.findViewById(R.id.sync_img_btn);
		syncImgBtn.setOnClickListener(this);
		
		bleStatueLy = (LinearLayout) root.findViewById(R.id.ble_statue_ly);
		BindDeviceDao dao = new BindDeviceDao(getActivity());
		List<Map<String, String>> daoList = dao.listBleDevice(null);
		if(daoList.size()!=0){
			bleStatueLy.setVisibility(View.VISIBLE);
		}
		
//		if(isApp.getConnectBleDevice()){
//			plusdotDevice = PlusDotBLEDevice.getInstance();
//			plusdotDevice.synchronizationDeviceInfo(true);
//		}
		
		isApp.addCallBack(new SleepUIStatusChangedCallback());
//		sentSyncSleepCmd();
//		sentSyncBatteryInfo();
		
		return root;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("TAG","SleepFragment 's onPause .................................");
//		if(isJump){
//			isJump = false;
//		}else{
//			sleepView.invalidate();
//			sleepView.setVisibility(View.INVISIBLE);
//		}
	 }

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("TAG","SleepFragment 's onResume ................................."+isApp.getLeftMenuTag());
		
		Log.e("TAG","SleepFragment 's onResume ................................."+isApp.getConnectBleDevice());
		
//		updateEnergy2(isApp.getEnerggyNum());
		
//		if(isApp.getLeftMenuTag() == Constant.SLEEP_TAG){
//			sleepView.setVisibility(View.VISIBLE);
//		}
		
		updateState();
	}

	
	private void updateState(){
		if(isApp.getConnectBleDevice()){
			bleStatueLy.setVisibility(View.VISIBLE);
			hideProgress();
			updateEnergyState(isApp.getEnerggyNum());
		}else{
			showProgress();
			updateSleepViewBySelectDay(mCurrentSelectDay);
		}
		
		syncText.setText(getActivity().getResources().getString(isApp.getConnectBleDevice() == true ? R.string.sync : R.string.syncing));
		
		isApp.addCallBack(new SleepUIStatusChangedCallback());
	}
	
	
	
	/**
	 * 发送睡眠同步command
	 * 
	 */
	public void sentSyncSleepCmd(){
		Log.e("TAG", "sentSyncSleepCmd.....................");
		if(isApp.getConnectBleDevice()){
			plusdotDevice = PlusDotBLEDevice.getInstance();
//			plusdotDevice.synchronizationDeviceInfo(true);
			plusdotDevice.synchronizationSleepBeginData(true);
			
			updateProgresssBar(false);
//			showSyncProgressDialog();
		}
	}
	
	public void sentSyncTime(){
		if(isApp.getConnectBleDevice()){
			plusdotDevice = PlusDotBLEDevice.getInstance();
			plusdotDevice.synchronizationTime(true);
		}
	}
	
	public void sentSyncBatteryInfo(){
		if(isApp.getConnectBleDevice()){
			plusdotDevice = PlusDotBLEDevice.getInstance();
			plusdotDevice.synchronizationDeviceInfo(true);
		}
	}
	
	/**
	 * 没有sleep数据时
	 */
	public void noSleepData(){
		updateProgresssBar(true);
//		disappearSyncProgressDialog();
	}
	
	public void updateBattery(byte[] data){
		Log.i("TAG", "sleepFragment 's updtaeEnergy ...................... ");
		int energy = ByteDataConvert.BinToInt(data, 3, 1);
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
	}
	
	public void updateEnergyState(int energy){
		bateryText.setText(Integer.toString(energy)+"%");
		if(energy>=60 && energy<=100){
			bateryImg.setBackgroundResource(R.drawable.zq_batery_1);
		}else if(40<energy && energy<60){
			bateryImg.setBackgroundResource(R.drawable.zq_batery_3);
		}else if(10<energy && energy<=40){
			bateryImg.setBackgroundResource(R.drawable.zq_batery_4);
		}else if(10>=energy){ //energy<=10.
			bateryImg.setBackgroundResource(R.drawable.zq_batery_5);
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
	
    public void updateSubPackage(byte[] data){
    	
		int len = ByteDataConvert.BinToInt(data, 3, 1);
//		Log.d("TAG", "len =============== "+len);
		int serial = ByteDataConvert.BinToIntByLow(data, 4, 2);
//		Log.d("TAG", "serial =============== "+serial);
		int data0 =  ByteDataConvert.BinToIntByLow(data, 6, 2);
//		Log.d("TAG", "data0 =============== "+data0);
		
		short started = (short) (((short)data[7]) <<8 | (data[6] & 0xff));
		short started_minutes = (short) ((started >> 4) & 0x0fff);
		byte started_status = (byte) (data[6] & 0x0F);
		
//		Log.d("TAG", "started_minutes0 ================ "+started_minutes);
//		Log.d("TAG", "started_status0 ================ "+started_status);
		int data1 =  ByteDataConvert.BinToIntByLow(data, 8, 1);
//		Log.d("TAG", "durations0 =============== "+data1);
		
		
		int data2 =  ByteDataConvert.BinToIntByLow(data, 9, 2);
//		Log.d("TAG", "data1 =============== "+data2);
		started = (short) (((short)data[10]) <<8 | (data[9] & 0xff));
		started_minutes = (short) ((started >> 4) & 0x0fff);
		started_status = (byte) (data[9] & 0x0F);
//		Log.d("TAG", "started_minutes1 ================ "+started_minutes);
//		Log.d("TAG", "started_status1 ================ "+started_status);
		int data3 =  ByteDataConvert.BinToIntByLow(data, 11, 1);
//		Log.d("TAG", "durations1 =============== "+data3);
		
		
		int data4 =  ByteDataConvert.BinToIntByLow(data, 12, 2);
//		Log.d("TAG", "data2 =============== "+data4);
		started = (short) (((short)data[13]) <<8 | (data[12] & 0xff));
		started_minutes = (short) ((started >> 4) & 0x0fff);
		started_status = (byte) (data[12] & 0x0F);
//		Log.d("TAG", "started_minutes2 ================ "+started_minutes);
//		Log.d("TAG", "started_status2 ================ "+started_status);
		int data5 =  ByteDataConvert.BinToIntByLow(data, 14, 1);
//		Log.d("TAG", "durations2 =============== "+data5);
		
//		disappearSyncProgressDialog();
    }
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view.equals(leftBtn)){
			isApp.getSlidingMenu().showMenu();
		}else if(view.equals(rightBtn)){
			isApp.getSlidingMenu().showSecondaryMenu();
		}else if(view.equals(sleepRemindBtn)){
			//设置跳转
			isJump = true;
			startActivity(new Intent(getActivity(), SmartClockActivity.class));
		}else if(view.equals(sleepHistoryBtn)){
			//设置跳转
			isJump = true;
			Intent intent = new Intent(getActivity(), SleepCharView.class);
			startActivity(intent);
		}else if(view.equals(syncLayout)){
			sentSyncSleepCmd();
		}else if(view.equals(syncBtn)){
			sentSyncSleepCmd();
		}else if(view.equals(syncText)){
			sentSyncSleepCmd();
		}else if(view.equals(timePre)){
			currentdate=DataUtil.getDateStrYHD(nDataNoWeek, -1);
			nDataNoWeek = DataUtil.getDateStr(nDataNoWeek, -1);
			aData = uData.getWeek(nDataNoWeek);
			
			mCurrentSelectDay = aData;
			
			if (nDataNoWeek.equals(DataUtil.getDateStr(uData.StringDataNoWeek(),-1))) {
				//update sleep 's data
				updateSleepViewBySelectDay(DataUtil.getDateStr(uData.StringDataNoWeek(),-1));
				timeData.setText(getActivity().getResources().getString(R.string.str_yesterday));
			} else {
				//update sleep 's data
				updateSleepViewBySelectDay(aData);
				timeData.setText(aData);
			}
		}else if(view.equals(timeNext)){
			if (nDataNoWeek.equals(uData.StringDataNoWeek())) {
				Toast toast = Toast.makeText(getActivity(), "已经是最新日期", Toast.LENGTH_SHORT);
			} else {
				currentdate=DataUtil.getDateStrYHD(nDataNoWeek, 1);
				nDataNoWeek = DataUtil.getDateStr(nDataNoWeek, 1);
				aData = uData.getWeek(nDataNoWeek);
				
				mCurrentSelectDay = aData;
				
				if (nDataNoWeek.equals(uData.StringDataNoWeek())) {
					//update sleep 's data
					updateSleepViewBySelectDay(uData.StringDataNoWeek());
					timeData.setText(getActivity().getResources().getString(R.string.str_today));
				} else if (nDataNoWeek.equals(DataUtil.getDateStr(uData.StringDataNoWeek(),-1))) {
					//update sport 's data
					updateSleepViewBySelectDay(DataUtil.getDateStr(uData.StringDataNoWeek(),-1));
					timeData.setText(getActivity().getResources().getString(R.string.str_yesterday));
				} else {
					//update sport 's data
					updateSleepViewBySelectDay(aData);
					timeData.setText(aData);
				}
			}
		}
	}	
	
    private void updateSleepViewBySelectDay(String strDay){
    	
    	Log.d("TAG", "SleepFragment 's updateSleepViewBySelectDay ========================== "+strDay);
    	
		String[] params = new String[]{ strDay };	
		LinkedHashMap<String, String> data = sleepDao.listSelectDaySleepMaps(params);
		
		Log.d("TAG", "SleepFragment 's updateSleepViewBySelectDay ========================== "+data.size());
		
		if(data.size()>0){
//			String sql = "insert into sleep (date,month,totalTime,deepSleep,lightSleep,rouseTime,orders) values (?,?,?,?,?,?,?)";
		    Log.d("TAG", "totalTime ===== "+data.get("totalTime")+"  deepSleep ===== "+data.get("deepSleep")+" lightSleep ==== "+data.get("lightSleep"));
		    
		    
			deepSleepHourTextUnit.setVisibility(View.VISIBLE);
			deepSleepMinuteText.setVisibility(View.VISIBLE);
			deepSleepMinuteTextUnit.setVisibility(View.VISIBLE);
		
			lightSleepHourTextUnit.setVisibility(View.VISIBLE);
		    lightSleepMinText.setVisibility(View.VISIBLE);
		    lightSleepMinTextUnit.setVisibility(View.VISIBLE);
		    
		    wakeUpHourTextUnit.setVisibility(View.VISIBLE);
		    wakeUpMinText.setVisibility(View.VISIBLE);
		    wakeUpMinTextUnit.setVisibility(View.VISIBLE);
		    
		    int durations = Integer.parseInt(data.get("totalTime"));
		    int currentHour = durations / 60;
		    int currentMin = durations % 60;
		    hourNum.setText(Integer.toString(currentHour));
			minuteNum.setText(Integer.toString(currentMin));

			Log.d("TAG", "SleepFragment 's updateSleepViewBySelectDay ========================== "+durations);
			
			int light = Integer.parseInt(data.get("lightSleep"));
			int lighthour = light / 60;
			int lightmin = light % 60;
			lightSleepHourText.setText(Integer.toString(lighthour));
			lightSleepMinText.setText(Integer.toString(lightmin));

			
			int deep = Integer.parseInt(data.get("deepSleep"));
			int deephour = deep / 60;
			int deepmin = deep % 60;
			deepSleepHourText.setText(Integer.toString(deephour));
			deepSleepMinuteText.setText(Integer.toString(deepmin));
			
			
			int awakeTime = Integer.parseInt(data.get("rouseTime"));
			int awakehour = awakeTime / 60;
			int awakemin = awakeTime % 60;
			wakeUpHourText.setText(Integer.toString(awakehour));
			wakeUpMinText.setText(Integer.toString(awakemin));
			
			setViewNum(currentHour);
		
		}else{
		
			deepSleepHourTextUnit.setVisibility(View.GONE);
			deepSleepMinuteText.setVisibility(View.GONE);
			deepSleepMinuteTextUnit.setVisibility(View.GONE);
		
			lightSleepHourTextUnit.setVisibility(View.GONE);
		    lightSleepMinText.setVisibility(View.GONE);
		    lightSleepMinTextUnit.setVisibility(View.GONE);
		    
		    wakeUpHourTextUnit.setVisibility(View.GONE);
		    wakeUpMinText.setVisibility(View.GONE);
		    wakeUpMinTextUnit.setVisibility(View.GONE);
		    
		    hourNum.setText("0");
			minuteNum.setText("0");
			
			lightSleepHourText.setText("0");
			lightSleepMinText.setText("0");
			
			deepSleepHourText.setText("0");
			deepSleepMinuteText.setText("0");
		
			wakeUpHourText.setText("0");
			wakeUpMinText.setText("0");
			
			setViewNum(0);
		}
    }

    
	//////////////////////////////////////////
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
	
	public void hideProgress(){
		searchProgress.setVisibility(View.GONE);
		connectTipText.setText(getActivity().getResources().getString(R.string.connected_tip));
		
		dividerLine.setVisibility(View.VISIBLE);
		bateryImg.setVisibility(View.VISIBLE);
//		bateryText.setVisibility(View.VISIBLE);
		syncLayout.setVisibility(View.VISIBLE);
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
	
    private CustomProgressDialog progressDialog;
	
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
	
	private int mDurations;
	private int mMonth;
	
	public void updateSleepData(byte[] data){
	    Log.d("TAG","sleepFragment updateSleepData ..................... ");
	    
		int year = ByteDataConvert.BinToIntByLow(data, 3, 2);
		mMonth = ByteDataConvert.BinToInt(data, 5, 1);
		String monthStr = String.format("%02d",mMonth); 
		
		int day = ByteDataConvert.BinToInt(data, 6, 1);
		String dayStr = String.format("%02d",day); 
		
		int ended_hour = ByteDataConvert.BinToInt(data, 7, 1);
		int ended_min = ByteDataConvert.BinToInt(data, 8, 1);
		
		int durations = ByteDataConvert.BinToIntByLow(data, 9, 2);	
		mDurations = durations;
		int minutes_asleep = ByteDataConvert.BinToIntByLow(data, 11, 2);
		
		int awake_count = ByteDataConvert.BinToInt(data, 13, 1);
		int totals = ByteDataConvert.BinToIntByLow(data, 14, 2);

		Log.d("TAG", "year === "+year+"   "+"month === "+mMonth+"  "+"day === "+day+"  "+"ended_hour === "+ended_hour+"  "+"ended_min ==="+ended_min+" "+"durations === "+durations+"  "+"minutes_asleep === "+minutes_asleep);
		Log.d("TAG", "awake_count === "+awake_count+"   "+"totals === "+totals);	
		
		currentTime = Integer.toString(year)+"/"+monthStr+"/"+dayStr;
		
		Log.d("TAG", "mCurrentSelectDay ============== "+mCurrentSelectDay+"  currentTime =========== "+currentTime);
		
		if(mCurrentSelectDay.equals(currentTime)){
		    int currentHour = durations / 60;
		    int currentMin = durations % 60;
		
			hourNum.setText(Integer.toString(currentHour));
			minuteNum.setText(Integer.toString(currentMin));
//			wakeUpText.setText(Integer.toString(awake_count));
			setViewNum(currentHour);
		}
		
		updateProgresssBar(true);
	}
	
	public void updateSleepSubPackage(byte[] data){
		int deep = ByteDataConvert.BinToIntByLow(data, 5, 2);
		int light = ByteDataConvert.BinToIntByLow(data, 3, 2);
		int awake = ByteDataConvert.BinToInt(data, 13, 1);
		
		Log.d("TAG", "deep ============ "+deep+"  light ============ "+light+" awake ========= "+awake);	
		
		int deephour = deep / 60;
		int deepmin = deep % 60;
		Log.d("TAG", "deep hour ========= "+deephour+" deep min ========= "+deepmin);
		
		int awakeTime = mDurations - deep - light;
		Object[] params = new Object[] { currentTime,mMonth,mDurations,deep,light, awakeTime,0};
		saveSleepData(params);

		if(mDurations==0){
			deepSleepHourTextUnit.setVisibility(View.GONE);
			deepSleepMinuteText.setVisibility(View.GONE);
			deepSleepMinuteTextUnit.setVisibility(View.GONE);
		
			lightSleepHourTextUnit.setVisibility(View.GONE);
		    lightSleepMinText.setVisibility(View.GONE);
		    lightSleepMinTextUnit.setVisibility(View.GONE);
		    
		    wakeUpHourTextUnit.setVisibility(View.GONE);
		    wakeUpMinText.setVisibility(View.GONE);
		    wakeUpMinTextUnit.setVisibility(View.GONE);
		
		}else{
			deepSleepHourTextUnit.setVisibility(View.VISIBLE);
			deepSleepMinuteText.setVisibility(View.VISIBLE);
			deepSleepMinuteTextUnit.setVisibility(View.VISIBLE);
		
			lightSleepHourTextUnit.setVisibility(View.VISIBLE);
		    lightSleepMinText.setVisibility(View.VISIBLE);
		    lightSleepMinTextUnit.setVisibility(View.VISIBLE);
		    
		    wakeUpHourTextUnit.setVisibility(View.VISIBLE);
		    wakeUpMinText.setVisibility(View.VISIBLE);
		    wakeUpMinTextUnit.setVisibility(View.VISIBLE);
			
		}
		
		if(mCurrentSelectDay.equals(currentTime)){
			deepSleepHourText.setText(Integer.toString(deephour));
			deepSleepMinuteText.setText(Integer.toString(deepmin));
			
			int lighthour = light / 60;
			int lightmin = light % 60;
			
			lightSleepHourText.setText(Integer.toString(lighthour));
			lightSleepMinText.setText(Integer.toString(lightmin));
			
			int awakehour = awakeTime / 60;
			int awakemin = awakeTime % 60;
			
			wakeUpHourText.setText(Integer.toString(awakehour));
			wakeUpMinText.setText(Integer.toString(awakemin));
		}
	}
	
	private void saveSleepData(Object[] params){
		Log.d("TAG", "save sleep data 's currentTime ========================== "+currentTime);
		String[] param = new String[]{ currentTime };	
		LinkedHashMap<String, String> data = sleepDao.listSelectDaySleepMaps(param);
		Log.d("TAG", "save data ======================== "+data+"   sata 'size ============= "+data.size());
		if(data.size()==0){
			sleepDao.addSleep(params);	
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
			this.progressMax	= sleepView.getMax();
			sleepView.setStyle(style);
			if(style == sleepView.STROKE_BLOCK){
				this.num 	= sleepView.getMinProgress();
				this.delay 	= 5;
				sleepView.setTitle(getString(R.string.router_complete_prepress));
				sleepView.setProgressUnit("%");
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
				if(sleepView == null)return ;
				if((style != sleepView.STROKE_BLOCK)&& (num > progressMax)){
					break;
				}
				if(style != sleepView.STROKE_BLOCK){
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
						if(num < sleepView.getMinProgress()){
							num = sleepView.getMinProgress();
							forward = true;
						}
					}
				}
				
				if(style != sleepView.STROKE_BLOCK){
					if(progress > num){
						sleepView.setProgress(num);
					}else{
						sleepView.setProgress(progress);
						break;
					}
				}else{
					sleepView.setProgress(num);
					count++;
					if(count > strokeBlockPropressNum){
						count = 0;
						strokeBlockPercent++;
						if(strokeBlockPercent > strokeBlockPercentMax){
							strokeBlockPercent = strokeBlockPercentMax;
						}
					}
					sleepView.setProgressNum(strokeBlockPercent);
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
	
	//////
    private void setViewNum(final int num){
    	Log.d("TAG", "setViewNum .................... ");
		int delay = 1000; //默认延迟3秒
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				int percent = num*240 / 10;
				setProgressNum(percent, sleepView.STROKE);
			}
		}, delay);
    }

    
	//////////////////////////////////////
	
	public Handler uihandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			switch (msg.what) {
			case MainActivity.MSG_BLE_CONNECTED:
	        	hideProgress();
//	        	showSyncProgressDialog();
	        	
//	        	plusdotDevice = PlusDotBLEDevice.getInstance();
//	        	plusdotDevice.synchronizationTime(true); // 0x01,sync time
				break;
			case MainActivity.MSG_BLE_DISCONNECTED:
				//更新界面  
	        	showProgress();
				break;
			case MainActivity.MSG_UPDATE_SLEEP_DATA:
			    updateSleepData(data.getByteArray("data"));
//			    sleepView.notify();
			    break;
			case MainActivity.MSG_UPDATE_SLEEP_DATA2:
				updateSleepSubPackage(data.getByteArray("data"));
			    break;
			case MainActivity.MSG_BLE_DEVICE_INFO:
				updateBattery(data.getByteArray("data"));
				sentSyncSleepCmd();
				break;
			case MainActivity.MSG_BLE_SYNC_TIME:
				sentSyncBatteryInfo();					
				break;
			case MainActivity.MSG_UPDATE_SLEEP_DATA_ERROR:
				noSleepData();
			case MSG_SHOW_SLEEPVIEW:
				if(isApp.getLeftMenuTag() == Constant.SLEEP_TAG){
					sleepView.setVisibility(View.VISIBLE);
				}
				
				updateState();
				
				break;
			case MSG_HIDE_SLEEPVIEW:
				sleepView.invalidate();
				sleepView.setVisibility(View.INVISIBLE);
				break;
			}
		}
    };
	
	
	class SleepUIStatusChangedCallback implements StatusChangedCallback{
		@Override
		public void callbackCall(int command, byte[] dataPacket) {
			
			Message msg = uihandler.obtainMessage(command); 
			Bundle sportdata = new Bundle();
            sportdata.putByteArray("data", dataPacket);
            msg.setData(sportdata);
          
			uihandler.sendMessage(msg);
		}

		@Override
		public void callbackFail(int command, byte[] dataPacket) {
			// TODO Auto-generated method stub
//			Log.d("TAG", "SleepFragment 's callbackFail..................."+command);
			Message msg = uihandler.obtainMessage(command); 
			uihandler.sendMessage(msg);
		}
	}
	
	////////////////////// test /////////////////////////
	public final static int MSG_HIDE_SLEEPVIEW = 20;
	
	public final static int MSG_SHOW_SLEEPVIEW = 21;
	
}
