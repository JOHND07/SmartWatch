package com.guogee.smartwatch.ui;

import com.guogee.smartwatch.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

/**
 * ≤È’“ ÷±Ì
 * @author john
 *
 */
public class FoundActivity extends Activity implements OnClickListener{
	
	private ImageButton backBtn;
	private FindWatchSurface watchSurface;
	
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.found_layout);  
        
//        setContentView(new FindWatchSurface(this));
        
        initView();
    }
    
    public static int screenWidth;
    
    private void initView(){
    	
	    WindowManager windowManager = getWindowManager();    
        Display display = windowManager.getDefaultDisplay();    
        screenWidth = screenWidth = display.getWidth();    
//        int screenHeight = screenHeight = display.getHeight();   
        
    	watchSurface = (FindWatchSurface) findViewById(R.id.watch_sf);
    	
//    	Log.d("TAG", "watchSurface 's width ================== "+watchSurface.getWidth());
    	backBtn = (ImageButton) findViewById(R.id.back_Btn);
    	backBtn.setOnClickListener(this);
    	
//    	foundBg=((BitmapDrawable)getResources().getDrawable(R.drawable.found_search_bg)).getBitmap();
//    	foundLight=((BitmapDrawable)getResources().getDrawable(R.drawable.found_search_light)).getBitmap();
    }

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view == backBtn){
			finish();
		}
//		else if(){
//			
//		}
	}
	
	////////////
	
}
