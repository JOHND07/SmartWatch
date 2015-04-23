package com.guogee.smartwatch.dialog;

import com.guogee.smartwatch.R;
import com.guogee.smartwatch.widget.NumberPicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

//import com.youduoyun.chaoke.numberpi.NumberPicker;
//import com.youduoyun.chaoke.ui.R;


public class DialogSetHeight extends Activity {
	 private Button mPickerok = null;
	 private Button mPickeresc = null;
	 int src =0;
	 NumberPicker np = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_light);
        mPickerok = (Button) findViewById(R.id.numberPickerok);
        mPickeresc = (Button) findViewById(R.id.numberPickercanle);
        np = (NumberPicker) findViewById(R.id.numberPicker);
        String height = getIntent().getStringExtra("height");
        np.setMaxValue(220);
        np.setMinValue(100);
        if(height!=null&&!"".equals(height))
        	np.setValue(Integer.parseInt(height));
        np.setFocusable(true);
        np.setFocusableInTouchMode(true);
        np.setMinimumWidth(100);
       
        mPickerok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				src =np.getValue();
				Intent intent=new Intent();  
				Log.i("wen", ""+src);
			    intent.putExtra("height", ""+src);  
			    setResult(20, intent);  
			    //关闭掉这个Activity  
			    finish();

			            

			}
		});
        mPickeresc.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();  
				
			    
			    setResult(50, intent);  //50 为空
			    //关闭掉这个Activity  
			    finish();
			}
		});
    }
}
