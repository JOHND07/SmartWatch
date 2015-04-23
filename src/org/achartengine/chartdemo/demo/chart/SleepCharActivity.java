package org.achartengine.chartdemo.demo.chart;

import com.guogee.smartwatch.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

public class SleepCharActivity extends Activity implements OnClickListener{

	
    @Override  
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);  
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sleep_char_layout);  
    }

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		
	}
    
}
