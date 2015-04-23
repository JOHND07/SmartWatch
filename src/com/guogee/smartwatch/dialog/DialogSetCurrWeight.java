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

public class DialogSetCurrWeight extends Activity{
	 private Button mPickerok = null;
	 private Button mPickeresc = null;
	 int src =0;
	 NumberPicker np = null;
	 private int n = 0;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

       setContentView(R.layout.activity_light);
       mPickerok = (Button) findViewById(R.id.numberPickerok);
       mPickeresc = (Button) findViewById(R.id.numberPickercanle);
       np = (NumberPicker) findViewById(R.id.numberPicker);
       
       np.setMaxValue(250);
       np.setMinValue(30);
       String weight = getIntent().getStringExtra("weight");
       String targetWeight = getIntent().getStringExtra("targetWeight");
       if(!"".equals(weight)&&weight!=null&&(targetWeight==null||"".equals(targetWeight))){
    	   np.setValue((int)Double.parseDouble(weight));
    	   n = 60;
       }
       if(!"".equals(targetWeight)&&targetWeight!=null&&(weight==null||"".equals(weight))){
    	   np.setValue((int)Double.parseDouble(targetWeight));
    	   n = 70;
       }
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
			    intent.putExtra("weight", ""+src);  
			    setResult(60, intent);  
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

	
	

