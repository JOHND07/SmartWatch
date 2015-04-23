package com.guogee.smartwatch.service;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RemoteUserService {
	private static AsyncHttpClient client = new AsyncHttpClient(); 
//	private static String remoteUrlHeader = "http://datastore.guogee.com:86/";
	
	private static String remoteUrlHeader = "http://115.28.165.93:86/";
	
	/**
	 * 发送HTTP请求
	 * @param url
	 */
	private static void SendUserToRemote(String url,RequestParams params,AsyncHttpResponseHandler handler)
	{
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
	
	
	private static void SendToRemote(String url,RequestParams params,AsyncHttpResponseHandler handler)
	{
		AsyncHttpClient clientRemote = new AsyncHttpClient();
		clientRemote.get(url, params, handler);
	}
	
	public static void CheckLogin(String UserName,String UserPwd,AsyncHttpResponseHandler handler){
		
		String url = remoteUrlHeader + "api/user/CheckLogin";
		RequestParams params = new RequestParams();
		params.add("UserName", UserName);
		params.add("UserPwd", UserPwd);
		SendUserToRemote(url,params,handler);
	}
	public static void RegisterUser(String UserEmail, String UserName,String UserPwd,AsyncHttpResponseHandler handler){
		
		String url = remoteUrlHeader + "api/user/AddUser";
		RequestParams params = new RequestParams();
		params.add("UserName", UserName);
		params.add("Email", UserEmail);
		params.add("UserPwd", UserPwd);
		SendUserToRemote(url, params, handler);
	}
	
	public static void UploadUserConfiguration(String configuration, String clientVersion, AsyncHttpResponseHandler handler){
		
		String url = remoteUrlHeader + "api/user/UploadUserConfiguration";
		RequestParams params = new RequestParams();
		params.add("configuration", configuration);
		params.add("clientVersion", clientVersion);
		PostUserToRemote(url, params, handler);
	}
	
	public static void DownloadUserConfiguration(AsyncHttpResponseHandler handler){
		
		String url = remoteUrlHeader + "api/user/DownloadUserConfiguration";
		SendUserToRemote(url, null, handler);
	}
	
	public static void AddUserShared(String sharedUserName, AsyncHttpResponseHandler handler){
		
		String url = remoteUrlHeader + "api/user/AddUserShared";
		RequestParams params = new RequestParams();
		params.add("sharedUserName", sharedUserName);
		SendUserToRemote(url, params, handler);
	}
	
	public static void GetSharedUsers(AsyncHttpResponseHandler handler){
		
		String url = remoteUrlHeader + "api/user/GetSharedUsers";
		SendUserToRemote(url, null, handler);
	}
	
	public static void DeleteUserShared(String sharedUserName, AsyncHttpResponseHandler handler){
		
		String url = remoteUrlHeader + "api/user/DeleteUserShared";
		RequestParams params = new RequestParams();
		params.add("sharedUserName", sharedUserName);
		SendUserToRemote(url, params, handler);
	}
	
	public static void GetUsersWhoSharedToMe(AsyncHttpResponseHandler handler){
		
		String url = remoteUrlHeader + "api/user/GetUsersWhoSharedToMe";
		SendUserToRemote(url, null, handler);
	}
	
	public static void DeleteUserSharedToMe(String sharedToMeUserName, AsyncHttpResponseHandler handler){
		
		String url = remoteUrlHeader + "api/user/DeleteUserSharedToMe";
		RequestParams params = new RequestParams();
		params.add("sharedToMeUserName", sharedToMeUserName);
		SendUserToRemote(url, params, handler);
	}
	
	public static void DownloadSharedUserConfiguration(String sharedUserName, AsyncHttpResponseHandler handler){
		String url = remoteUrlHeader + "api/user/DownloadSharedUserConfiguration";
		RequestParams params = new RequestParams();
		params.add("sharedUserName", sharedUserName);
		SendUserToRemote(url, params, handler);
	}
	
	public static void CheckVersion(AsyncHttpResponseHandler handler){
		
//		String url = remoteUrlHeader + "api/update/CheckAndroidUpdate";
//		String url = "http://www.dongha.cn/apps/firmwares/band_pd100_v7.hex";
		
		String url = "http://www.youduoyun.com/api/firmwares/version?device_id=180";
		SendToRemote(url, null, handler);
	}
}
