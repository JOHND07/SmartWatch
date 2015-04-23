package com.guogee.smartwatch.adapter;

import java.util.List;
import java.util.Map;

import com.guogee.smartwatch.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BeanAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Map<String,Object>> mData;
	
	public BeanAdapter(Activity context){
		this.inflater 	= LayoutInflater.from(context);
	}
	
	public void setData(List<Map<String,Object>> data){
		mData = data;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			
			convertView = this.inflater.inflate(R.layout.bean_list_item, null);
			
			holder.text = (TextView) convertView.findViewById(R.id.title);
			holder.imageView = (ImageView)convertView.findViewById(R.id.icon);
			holder.beanNum = (TextView) convertView.findViewById(R.id.bean_num);  
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		if(mData.isEmpty()){
			return null;
		}
		
		holder.imageView.setBackgroundResource((Integer)mData.get(position).get("img"));
		holder.text.setText((String)mData.get(position).get("bereventcount"));
		holder.beanNum.setText('+'+(String)mData.get(position).get("beanNum"));
		
		return convertView;
	}

	private final class ViewHolder {
		TextView text;
		ImageView imageView;
		TextView beanNum;
	}

}
