package com.guogee.smartwatch.ui;

import com.guogee.smartwatch.R;
import com.guogee.smartwatch.iSmartApplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * 设置界面的Fragment
 * 
 * @author john
 *
 */
public class SetUpFragment extends Fragment implements OnClickListener{
	
	private ImageButton leftBtn;
	private iSmartApplication isApp;
	
	public static SetUpFragment instantiation(int position) {
		SetUpFragment fragment = new SetUpFragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isApp = (iSmartApplication)getActivity().getApplication();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.setup_layout, container, false);

		leftBtn = (ImageButton) root.findViewById(R.id.left_Btn);
		leftBtn.setOnClickListener(this);
		
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
		switch(view.getId()){
		case R.id.left_Btn:
			isApp.getSlidingMenu().showMenu();
			break;
		}
	}
}
