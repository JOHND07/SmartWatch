package com.guogee.smartwatch.ui;

import com.guogee.smartwatch.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class BeanTipActivity extends Activity implements OnClickListener{

	private ImageButton backBtn;
	
	private TextView textView;
	
	private TextView beanNum;
	
	private ListView mListView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bean_tip_layout);
		
		initView();
	}
	
	private void initView(){
		backBtn = (ImageButton) findViewById(R.id.back_Btn);
		backBtn.setOnClickListener(this);
		beanNum = (TextView) findViewById(R.id.bean_num);
		mListView = (ListView) findViewById(R.id.list);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.back_Btn:
			finish();
			break;
		}
	}

}
