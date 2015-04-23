package com.guogee.smartwatch.ui;

import com.guogee.smartwatch.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

public class BeanActivity extends Activity implements OnClickListener{

    private ImageButton backBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bean_layout);
		
		initView();
	}
	
	private void initView(){
		backBtn = (ImageButton) findViewById(R.id.back_Btn);
		backBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.back_btn:
			finish();
			break;
		}
	}

}
