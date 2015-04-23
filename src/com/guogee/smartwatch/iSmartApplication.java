package com.guogee.smartwatch;

import java.util.List;
import java.util.Map;

import com.guogee.smartwatch.ble.StatusChangedCallback;
import com.guogee.smartwatch.utils.Constant;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Application;
import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

public class iSmartApplication extends Application {

	private SlidingMenu sm;
	private int leftMenuTag = 0;
	private boolean isConnectedBle ;
	private boolean isScanBle;
	
	private StatusChangedCallback uiCallBack;
	
	private List<Map<String, String>> bindWatch;
	
	private int firmVersion = 0;
	
	
	private boolean isSendBindCommand = false;
	
	public void SendBindCommand(boolean isSend){
		isSendBindCommand = isSend;
	}
	
	public boolean getBindCommand(){
		return isSendBindCommand;
	}
	
	public void setFirmVersion(int version){
		firmVersion = version;
	}
	
	public int getFirmVersion(){
		return firmVersion;
	}
	
	private Handler mainHandler ;
	
	/** OTA Mode **/
	private Boolean isEnternOtaMode;

	public void setOTAMode(boolean otaMode){
		isEnternOtaMode = otaMode;
	}

	public boolean getOTAMode(){
		return isEnternOtaMode;
	}
	/** OTA Mode **/
	
	public void setMainHandler(Handler handler){
		mainHandler = handler;
	}
	
	public Handler getMainHandler(){
		return mainHandler;
	}
	
	//sport handle
	
    private Handler leftMenuHandler ;
	
	public void setLeftMenuHandler(Handler handler){
		leftMenuHandler = handler;
	}
	
	public Handler getLeftMenuHandler(){
		return leftMenuHandler;
	}
	
	//sport handle
	
	public iSmartApplication() {  
	}
	
	private SharedPreferences sp;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
/**		
		Intent serviceIntent = new Intent(getApplicationContext(), SWService.class);
		getApplicationContext().startService(serviceIntent);
**/	
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		firmVersion = sp.getInt(Constant.FIRMWARE_VERSION, 0);
	}
	
	public void setBindWatch(List<Map<String, String>> watch){
		bindWatch = watch;
	}
	
	public List<Map<String, String>> getBindWatch(){
		return bindWatch;
	}
	
	public void setSlidingMenu(SlidingMenu menu){
		sm = menu;
	}
	
	public SlidingMenu getSlidingMenu(){
		return sm;
	}

	public void setLeftMenuTag(int tag){
		leftMenuTag = tag;
	}
	
	public int getLeftMenuTag(){
		return leftMenuTag;
	}
	
	/**
	 * set the "isConnectedBle" ,it means the app whether connect to the smart-watch or not.
	 * 
	 * @param bleDevice 
	 */
	public void setConnectBleDevice(boolean bleDevice){
		Log.d("TAG", "setConnectBleDevice 's bleDevice ========================== "+bleDevice);
		isConnectedBle = bleDevice;
	}

	/**
	 * if the "isConnectedBle" is true,it means the app connect the smart-watch.
	 * 
	 * @param bleDevice 
	 */
	public boolean getConnectBleDevice(){
		return isConnectedBle;
	}
	
	/**
	 * set the "isScanBle" ,it means the app whether scan to the smart-watch or not.
	 * 
	 * @param bleDevice 
	 */
	public void setScanBleDevice(boolean bleDevice){
		Log.d("TAG", "setScanBleDevice 's bleDevice ========================== "+bleDevice);
		isScanBle = bleDevice;
	}

	/**
	 * if the "isScanBle" is true,it means the app scan the smart-watch , but n't connect to it.
	 * 
	 * @param bleDevice 
	 */
	public boolean getScanBleDevice(){
		return isScanBle;
	}
	
	private int mEnergy = 0;
	
	public void setEnerggyNum(int energy){
		mEnergy = energy;
	}
	
    public int getEnerggyNum(){
		return mEnergy;
	}
    
    public void addCallBack(StatusChangedCallback uicallback){
    	uiCallBack = uicallback;
    }
    
    public StatusChangedCallback getUICallback(){
    	return uiCallBack;
    }

    /////////////////////////////////////////////////////////
    /*
	 * 下载服务
	 */
	private DownloadManager downloadManager;
	
	/*
	 * 新版本
	 */
	public boolean newVersion = false;
	
	public void setDownloadManager(DownloadManager dl){
		this.downloadManager = dl;
	}
	
	public DownloadManager getDownloadManager(){
		return downloadManager;
	}
	
	//主Activity 退出了，我们要进行全局的资源释放
	public void onDestroy(){
		downloadManager = null;
	}
	
	private boolean isEnterPicMode = false;
	
	public void enterPicMode(boolean picMode){
		isEnterPicMode = picMode;
	}
	
	public boolean getPicMode(){
		return isEnterPicMode;
	}
	
}
