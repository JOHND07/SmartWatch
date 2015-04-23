package org.achartengine.chartdemo.demo.chart;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import com.guogee.smartwatch.R;
import com.guogee.smartwatch.dao.SleepDao;
import com.guogee.smartwatch.utils.Log;
import com.guogee.smartwatch.utils.Util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SleepCharView extends Activity{

	
	public GraphicalView mChartView ;
	private List<String> options = new ArrayList<String>(); 
    private List<Integer> values = new ArrayList<Integer>();

    private SleepDao sleepDao;
    private SharedPreferences sp;
//    private TextView sleepTime;
    private ImageButton backBtn;
    
    private TextView sleepTimeHour;
    private TextView sleepTimeMimute;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sleep_chart_view);
		
        sp = PreferenceManager.getDefaultSharedPreferences(this);
		
        sleepDao = new SleepDao(this);
        
		initData();
		
//		sleepTime = (TextView) findViewById(R.id.sleep_time);
		
		sleepTimeHour = (TextView) findViewById(R.id.sleep_time_hour);
		sleepTimeHour.setTypeface(Util.overrideViewFonts(SleepCharView.this, sleepTimeHour));

		sleepTimeMimute = (TextView) findViewById(R.id.sleep_time_minute);
		sleepTimeMimute.setTypeface(Util.overrideViewFonts(SleepCharView.this, sleepTimeMimute));
		
		backBtn = (ImageButton) findViewById(R.id.left_Btn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.chartlayout); 
	
		XYMultipleSeriesRenderer render =new XYMultipleSeriesRenderer();
		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		r.setColor(Color.WHITE);
		render.addSeriesRenderer(r);
		
		   r = new SimpleSeriesRenderer();  
	         r.setChartValuesTextAlign(Align.RIGHT);  
	         r.setChartValuesTextSize(25);//柱状体顶部的字体大小  
	         r.setDisplayChartValues(false);  //不显示柱体上的数字
	         r.setColor(getResources().getColor(R.color.background_color));
	         render.addSeriesRenderer(r);  
		     
//		     render.setChartTitle("bar chart demo");
//		     render.setXTitle("x轴");
//		     render.setYTitle("y轴");
//		     render.setXAxisMin(0.5);
//		     render.setXAxisMax(10.5);
//		     render.setYAxisMin(0);
//		     render.setYAxisMax(210);
//		     render.setBarSpacing(1);
		     
		     
		     render.setAxesColor(Color.LTGRAY);      //Color.LTGRAY 
		     render.setLabelsColor(Color.LTGRAY);  //Color.LTGRAY
		     render.setXLabels(0);  
		     render.setYLabels(0);
//		     renderer.setYLabels(10);  
		     render.setLabelsTextSize(25);  

		     // renderer.setXLabelsColor(0xff000000);//设置X轴上的字体颜�?  
		     render.setYLabelsColor(0,0xffffffff);//设置Y轴上的字体颜�?  

			 render.setXAxisMin(0);  //0
			 render.setXAxisMax(6);  //6
			 render.setYAxisMin(0);  //0 
			 render.setYAxisMax(getMaxSleepNum());  //100
			     
			 render.setPanLimits(new double[] { 0, 13, 0, 0 });
		     //add by john
		     
		     int size =  options.size();  
		     for (int i = 0; i < size; i++) {  
		        render.addXTextLabel(i, options.get(i));  
		     }
		        
		     render.setMarginsColor(0x00ffffff);  
		     render.setPanEnabled(true, false);  
		     render.setZoomEnabled(false, false);// 设置x，y方向都不可以放大或缩�?  
		     render.setZoomRate(1.0f);  
		     render.setInScroll(false);  
		        
		     render.setBackgroundColor(0x00ffffff);  
		     render.setApplyBackgroundColor(false);  

//		     render.setBarWidth(62);  
//		     render.setBarSpacing(100);  
//		     render.setAxisTitleTextSize(16);  
//		     render.setChartTitleTextSize(20);  
		     render.setLabelsTextSize(25);  //x lable 's size.
//		     render.setLegendTextSize(15);
		     
		     render.setYLabelsAlign(Align.RIGHT);  
		     render.setXLabelsAlign(Align.CENTER);  
		     
		     mChartView = ChartFactory.getBarChartView(this, buildBarDataset(values), render, Type.DEFAULT);
		     render.setClickEnabled(true);
		     layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		     
		     /// render.setSelectableBuffer(100);
		     mChartView.setOnClickListener(new View.OnClickListener() {
		       @Override
		       public void onClick(View v) {
		         SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
		         double[] xy = mChartView.toRealPoint(0);
		         if (seriesSelection == null) {
//		           Toast.makeText(AchartEngineBar.this, "No chart element was clicked", Toast.LENGTH_SHORT)
//		               .show();
		         } else {
//		           Toast.makeText(
//		        		   SleepCharView.this,
//		               "Click position ====== "+seriesSelection.getXValue() ,Toast.LENGTH_SHORT).show();
		           calSleepInfo((int)seriesSelection.getXValue());
		         }
		       }
		     });
	}  
    
	
	private void initData(){
		//init month string
		String LABLE_TEXT[] = getResources().getStringArray(R.array.monthStrings);
		for (String tem : LABLE_TEXT) {  
		      options.add(tem);  
		}
		
		int totalMonthSleep= 0;
		
		for(int i=0;i<12;i++){
			String[] params = new String[] { String.valueOf(i+1) };
			List<LinkedHashMap<String, String>> data = sleepDao.listMonthSleepMaps(params);
			Log.d("TAG", "SleepCharView 's data =================== "+data);
			if(data.size()>0){
				for(int index=0;index<data.size();index++){
					totalMonthSleep += Integer.parseInt(data.get(index).get("totalTime")) ;
				}
			}
			values.add(totalMonthSleep);
			totalMonthSleep = 0;
		}
		
		Log.d("TAG", "values =================== "+values);
	}
	
	private XYMultipleSeriesDataset buildBarDataset( List<Integer> values) {  
		//get dataSet
	     XYMultipleSeriesDataset dataset=new XYMultipleSeriesDataset();
	     for(int i=0;i<2;i++){
	    	 CategorySeries series = new CategorySeries("");
	    	 int seriesLength = values.size();
	         for(int k=0;k<seriesLength;k++){
	             series.add(values.get(k));
	         }dataset.addSeries(series.toXYSeries());
	     }
	     return dataset;
	}
	
	private void calSleepInfo(int index){
		
		String[] params = new String[] { Integer.toString(index) };
		List<LinkedHashMap<String, String>> data = sleepDao.listMonthSleepMaps(params);
	
		if(data.size()>0){
			int average = values.get(index-1)/data.size();
			int hour = average / 60;
			int min = average % 60;
//			String timeStr = hour+getResources().getString(R.string.hour_num)+min+getResources().getString(R.string.minute_num);
//			sleepTime.setText(timeStr);
			
			sleepTimeHour.setText(Integer.toString(hour));
			sleepTimeMimute.setText(Integer.toString(min));
		}
		
	}
	
	private int getMaxSleepNum(){
		int maxNum = 0;
		for(int i=0;i<values.size();i++){
			if(values.get(i)>maxNum){
				maxNum = values.get(i);
			}
		}
		return maxNum;
	}
	
}
