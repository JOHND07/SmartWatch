package com.guogee.smartwatch.ui;

import com.guogee.smartwatch.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class WebViewActivity extends Activity {

	private WebView webview;  
	
	private ImageButton backBtn;
	
	private String weburl ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.web_view);
		
		Bundle bundle=getIntent().getExtras();
		/** 获取Bundle的信息 **/
		if(bundle!=null){
			weburl = bundle.getString("webline");
		}
		
		backBtn = (ImageButton) findViewById(R.id.left_Btn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		webview = (WebView) findViewById(R.id.webview);  
        WebSettings webSettings = webview.getSettings();  
        //设置WebView属性，能够执行Javascript脚本    
        webSettings.setJavaScriptEnabled(true);    
        //设置可以访问文件  
        webSettings.setAllowFileAccess(true);  
         //设置支持缩放  
        webSettings.setBuiltInZoomControls(true);  
        //加载需要显示的网页    
        webview.loadUrl(weburl);    
        //设置Web视图    
        webview.setWebViewClient(new webViewClient());
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override   
    //设置回退    
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法    
    public boolean onKeyDown(int keyCode, KeyEvent event) {    
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {    
            webview.goBack(); //goBack()表示返回WebView的上一页面    
            return true;    
        }    
        finish();//结束退出程序  
        return false;    
    }   
	
	//Web视图    
    private class webViewClient extends WebViewClient {    
        public boolean shouldOverrideUrlLoading(WebView view, String url) {    
            view.loadUrl(url);    
            return true;    
        }    
    } 
	
}
