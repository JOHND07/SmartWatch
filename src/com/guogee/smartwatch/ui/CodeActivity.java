package com.guogee.smartwatch.ui;

import com.guogee.smartwatch.R;

import android.app.Activity;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CodeActivity extends Activity implements OnClickListener{

	private TextView gameCodeText;
	private String accode;
	private Button copyCodeBtn;
	private ImageButton backBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.exchange_layout);
	
		Bundle bundle = getIntent().getExtras(); 
		accode = bundle.getString("accode");
		
		initView();
	}
	
	private void initView(){
		gameCodeText = (TextView) findViewById(R.id.exchange_code);
		gameCodeText.setText(accode);
		
		backBtn = (ImageButton) findViewById(R.id.back_Btn);
		backBtn.setOnClickListener(this);
		
		copyCodeBtn = (Button) findViewById(R.id.copy_code_btn);
		copyCodeBtn.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.back_Btn:
			finish();
			break;
		case R.id.copy_code_btn:
			ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			cmb.setText(accode);
			Toast.makeText(CodeActivity.this, "¸´ÖÆ³É¹¦", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
}
