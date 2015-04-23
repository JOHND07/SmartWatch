package com.guogee.smartwatch.adapter;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.guogee.smartwatch.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GiftAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	
	private List<Map<String,Object>> mData;
	
	private boolean isInGift = false;
	
	public GiftAdapter(Activity context){
		this.inflater 	= LayoutInflater.from(context);
	}
	
	public void setData(List<Map<String,Object>> data,boolean isIngift){
		mData = data;
		isInGift = isIngift;
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
			
			convertView = this.inflater.inflate(R.layout.gift_list_item, null);
			
			holder.text = (TextView) convertView.findViewById(R.id.title);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.imageView = (ImageView)convertView.findViewById(R.id.icon);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

//		holder.imageView.setBackgroundResource((Integer)mData.get(position).get("img"));
		holder.text.setText((String)mData.get(position).get("actgamename"));
		
		if(isInGift){
			holder.date.setVisibility(View.VISIBLE);
			holder.date.setText((String)mData.get(position).get("gbdate"));	
			
			holder.text.setText((String)mData.get(position).get("actgamename"));
		}else{
			holder.date.setVisibility(View.GONE);
		}

		
//		holder.text.setText((String)mData.get(position).get("title"));
		
		LoadImage(holder.imageView,(String)mData.get(position).get("actlogo"));
		
		return convertView;
	}
	
	private void LoadImage(ImageView img, String path){
	    //异步加载图片资源
		AsyncTaskImageLoad async=new AsyncTaskImageLoad(img);
		//执行异步加载，并把图片的路径传送过去
		async.execute(path);
	}
	
	private final class ViewHolder {
		TextView text;
		TextView date;
		ImageView imageView;
	}
	
	/////////////////////////////////////////////////////////////////

	  public class AsyncTaskImageLoad extends AsyncTask<String, Integer, Bitmap> {
	  
	      private ImageView Image=null;
	      
	      public AsyncTaskImageLoad(ImageView img) 
	      {
	          Image=img;
	      }
	      //运行在子线程中
	     protected Bitmap doInBackground(String... params) {
	         try 
	         {
	             URL url=new URL(params[0]);
	             HttpURLConnection conn=(HttpURLConnection) url.openConnection();
	             conn.setRequestMethod("POST");
	             conn.setConnectTimeout(5000);
	             if(conn.getResponseCode()==500)
	             {
	                 InputStream input=conn.getInputStream();
	                 Bitmap map=BitmapFactory.decodeStream(input);
	                 return map;
	             }
	         } catch (Exception e) 
	         {
	             e.printStackTrace();
	         }
	         return null;
	     }
	 
	     protected void onPostExecute(Bitmap result)
	     {
	         if(Image!=null && result!=null)
	         {
	             Image.setImageBitmap(result);
	         }
	         
	         super.onPostExecute(result);
	     }
	 }
	

}
