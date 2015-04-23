package com.guogee.smartwatch.service;

import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RemoteGameService {

	private static AsyncHttpClient client = new AsyncHttpClient(); 
	private static String remoteUrlHeader = "http://182.92.157.146:8080/gambler/";
//	http://182.92.157.146:8080/gambler/activity/list
	
	/**
	 * 发送HTTP请求
	 * @param url
	 */
	private static void SendUserToRemote(String url,RequestParams params,AsyncHttpResponseHandler handler)
	{
		client.setTimeout(10000);
		client.get(url, params, handler);
		
	}
	
	/**
	 * 发送HTTP请求
	 * @param url
	 */
	private static void PostUserToRemote(String url,RequestParams params,AsyncHttpResponseHandler handler)
	{
		client.post(url, params, handler);
	}
	
	public static void GetActivityList(AsyncHttpResponseHandler handler){
//		String url = remoteUrlHeader + "activity/list";
		String url = "http://115.28.165.93:9527/gambler/activity/list";
		
//		String url = "http://192.168.199.106:8080/gambler/activity/list";
		SendUserToRemote(url, null, handler);
	}
	
//	public static void GetActivityInfo(String actId,AsyncHttpResponseHandler handler){
//		String url = remoteUrlHeader + "activity/getInfo";
//		RequestParams params = new RequestParams();
//		params.add("actid", actId);
//		SendUserToRemote(url, null, handler);
//	}
	
	public static void GetGiftList(String macAddress, AsyncHttpResponseHandler handler){
//		String url = remoteUrlHeader + "giftbag/userlist";
		
        String url = "http://115.28.165.93:9527/gambler/giftbag/userlist";
		
//		String url = "http://192.168.199.106:8080/gambler/giftbag/userlist";
		RequestParams params = new RequestParams();
		params.add("gbuserkey", macAddress);
		SendUserToRemote(url, params, handler);
	}
	
    public static void GetUserBean(String macAddress, AsyncHttpResponseHandler handler){
//		String url = remoteUrlHeader + "userbean/getUserBean";
    	
    	String url = "http://115.28.165.93:9527/gambler/userbean/getUserBean";
    	
//    	String url = "http://192.168.199.106:8080/gambler/userbean/getUserBean";
    	
		RequestParams params = new RequestParams();
		params.add("usbuserkey", macAddress);
		SendUserToRemote(url, params, handler);
	}
	
    public static void getGifBag(String macAddress, String actid, AsyncHttpResponseHandler handler){
//		String url = remoteUrlHeader + "userbean/getUserBean";
    	
    	String url = "http://115.28.165.93:9527/gambler/giftbag/getBag";
    	
//    	String url = "http://192.168.199.106:8080/gambler/giftbag/getBag";
    	
		RequestParams params = new RequestParams();
		params.add("gbuserkey", macAddress);
		params.add("actid", actid);
		params.add("skey", "guogee_shoubiao");
//		SendUserToRemote(url, null, handler);
		
		PostUserToRemote(url,params,handler);
	}
    
    
    //
    public static void postCurrentStep(String stepNum, String state, String macAddress, AsyncHttpResponseHandler handler){
    	String url = "http://115.28.165.93:9527/gambler/userbean/saveUserBean";
    	
		RequestParams params = new RequestParams();
		params.add("stepnumber", stepNum);
		params.add("state", state);
		
//		usbuserkey=123&usbbeancount=0&skey=guogee_shoubiao&type=1
		params.add("usbuserkey", macAddress);
		params.add("usbbeancount", "0");
		params.add("skey", "guogee_shoubiao");
		params.add("type", "1");
		
		PostUserToRemote(url,params,handler);
    }
    
    
    
    public static void getBeanRule(AsyncHttpResponseHandler handler){
//    	String url = remoteUrlHeader + "userbean/getBeanExchangeRule";
    	String url = "http://115.28.165.93:9527/gambler/userbean/getBeanExchangeRule";
    	
//    	String url = "http://192.168.199.106:8080/gambler/userbean/getBeanExchangeRule";
    	
		SendUserToRemote(url, null, handler);
    }
    
    public static void saveUserBean(String macAddress, String usbbeancount, AsyncHttpResponseHandler handler){
    	String url = "http://115.28.165.93:9527/gambler/userbean/saveUserBean";
    	
//    	String url = "http://192.168.199.106:8080/gambler/userbean/saveUserBean";
    	
    	
		RequestParams params = new RequestParams();
//		params.add("usbuserkey", macAddress);
//		params.add("usbbeancount", usbbeancount);

		params.put("usbuserkey", macAddress);
		params.put("usbbeancount", usbbeancount);
		params.put("skey", "guogee_shoubiao");
		
		PostUserToRemote(url,params,handler);
    }
    
    
    
}
