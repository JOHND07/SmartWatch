package com.guogee.smartwatch.ui;

import com.guogee.smartwatch.R;
import com.guogee.smartwatch.utils.Constant;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class AccountInfoFragment extends Fragment implements OnClickListener{

	private SharedPreferences sp;
	private Button loginOutBtn;
	private ImageButton backBtn;
//	private RelativeLayout changePwdLy;
//	private RelativeLayout forgetPwdLy;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.account_info_layout, container, false);

//		loginOutBtn = (Button) root.findViewById(R.id.login_out_btn);
//    	loginOutBtn.setOnClickListener(this);
    	
    	backBtn = (ImageButton) root.findViewById(R.id.back_Btn);
    	backBtn.setOnClickListener(this);
		
		return root;
	}
	
	 @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}	
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view == loginOutBtn){
			Editor editor = sp.edit();
			editor.putBoolean(Constant.LOGIN_FALG, false);
			editor.commit();
		}else if(view == backBtn){
			
		}
	}


}
