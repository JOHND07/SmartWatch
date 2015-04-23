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
import com.guogee.smartwatch.dao.SportDao;
import com.guogee.smartwatch.utils.Constant;
import com.guogee.smartwatch.utils.Util;

import android.R.integer;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SportCharView extends Activity {
	
	public GraphicalView mChartView ;
	private List<String> options = new ArrayList<String>(); 
    private List<Integer> values = new ArrayList<Integer>();  
    
//	public static String LABLE_TEXT[] = {"", "1月", "2月", "3月", "4月", "5月","6月","7月","8月","9月" }; 
//    private int first[] = new int[] {10,20,30, 40, 50, 60, 70,80,90 }; 
	
    private SportDao sportDao;
    
    private TextView stepNum;
    private TextView calorieNum;
    private TextView mileNum;
    private TextView currentMonth;
    private TextView cokeNum;
    private TextView playgroundNum;
    
    private TextView meileageUnit;
    private SharedPreferences sp;
    private ImageButton backBtn;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sport_chart_view);
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		sportDao = new SportDao(this);
		
		initData();
		
		backBtn = (ImageButton) findViewById(R.id.left_Btn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		stepNum = (TextView) findViewById(R.id.step_num);
		stepNum.setTypeface(Util.overrideViewFonts(SportCharView.this, stepNum));
		
		calorieNum = (TextView) findViewById(R.id.calorite_num);
		calorieNum.setTypeface(Util.overrideViewFonts(SportCharView.this, calorieNum));
		
		mileNum = (TextView) findViewById(R.id.mile_num);
		mileNum.setTypeface(Util.overrideViewFonts(SportCharView.this, mileNum));
		
		currentMonth = (TextView) findViewById(R.id.current_month);
		meileageUnit = (TextView) findViewById(R.id.meileage_unit);
		
		cokeNum = (TextView) findViewById(R.id.coke_num);
		cokeNum.setTypeface(Util.overrideViewFonts(SportCharView.this, cokeNum));
		
		playgroundNum = (TextView) findViewById(R.id.playground_num);
		playgroundNum.setTypeface(Util.overrideViewFonts(SportCharView.this, playgroundNum));
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.chartlayout); 
		
		//get dataset
//	     XYMultipleSeriesDataset dataset=new XYMultipleSeriesDataset();
//	     for(int i=0;i<2;i++){
////	         CategorySeries series = new CategorySeries("Demo Series"+i);
//	    	 CategorySeries series = new CategorySeries("");
//	         for(int k=0;k<5;k++){
//	             series.add(k*(i+2));
//	         }dataset.addSeries(series.toXYSeries());
//	     }
		
		
//		XYMultipleSeriesDataset dataset=new XYMultipleSeriesDataset();
//	     for(int i=0;i<2;i++){
//	    	 CategorySeries series = new CategorySeries("");
//	    	 double [] v = values.get(0);
//	    	 int seriesLength = v.length;
//	         for(int k=0;k<seriesLength;k++){
//	             series.add(v[k]);
//	         }dataset.addSeries(series.toXYSeries());
//	     }
		
	     
	     XYMultipleSeriesRenderer render =new XYMultipleSeriesRenderer();
	     SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	     r.setColor(Color.WHITE);
	     render.addSeriesRenderer(r);
	     
//	     r=new SimpleSeriesRenderer();
////	     r.setColor(Color.RED);
//	     r.setColor(getResources().getColor(R.color.background_color));
//	     render.addSeriesRenderer(r);
	     
	     
	     r = new SimpleSeriesRenderer();  
         r.setChartValuesTextAlign(Align.RIGHT);  
         r.setChartValuesTextSize(25);//柱状体顶部的字体大小  
         r.setDisplayChartValues(false);  //不显示柱体上的数字
         r.setColor(getResources().getColor(R.color.background_color));
         render.addSeriesRenderer(r);  
	     
//	     render.setChartTitle("bar chart demo");
//	     render.setXTitle("x轴");
//	     render.setYTitle("y轴");
//	     render.setXAxisMin(0.5);
//	     render.setXAxisMax(10.5);
//	     render.setYAxisMin(0);
//	     render.setYAxisMax(210);
//	     render.setBarSpacing(1);
	     
	     
	     render.setAxesColor(Color.LTGRAY);      //Color.LTGRAY 
	     render.setLabelsColor(Color.LTGRAY);  //Color.LTGRAY
	     render.setXLabels(0);  
	     render.setYLabels(0);
//	     renderer.setYLabels(10);  
	     render.setLabelsTextSize(25);  

	     // renderer.setXLabelsColor(0xff000000);//设置X轴上的字体颜�?  
	     render.setYLabelsColor(0,0xffffffff);//设置Y轴上的字体颜�?  

		 render.setXAxisMin(0);  //0
		 render.setXAxisMax(6);  //6
		 render.setYAxisMin(0);  //0 
		 render.setYAxisMax(getMaxStepNum());  //100
		     
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

//	     render.setBarWidth(62);  
//	     render.setBarSpacing(100);  
//	     render.setAxisTitleTextSize(16);  
//	     render.setChartTitleTextSize(20);  
	     render.setLabelsTextSize(25);  //x lable 's size.
//	     render.setLegendTextSize(15);
	     
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
//	           Toast.makeText(AchartEngineBar.this, "No chart element was clicked", Toast.LENGTH_SHORT)
//	               .show();
	         } else {
//	           Toast.makeText(
//	        		   SportCharView.this,
//	               "Click position ====== "+seriesSelection.getXValue() ,Toast.LENGTH_SHORT).show();
	           
	             calSportInfo((int)seriesSelection.getXValue());
	         }
	       }
	     });
	}
	 
	
	private XYMultipleSeriesDataset buildBarDataset( List<Integer> values) {  
		//get dataSet
	     XYMultipleSeriesDataset dataset=new XYMultipleSeriesDataset();
	     for(int i=0;i<2;i++){
	    	 CategorySeries series = new CategorySeries("");
//	    	 double [] v = values.get(0);
	    	 int seriesLength = values.size();
	         for(int k=0;k<seriesLength;k++){
	             series.add(values.get(k));
	         }dataset.addSeries(series.toXYSeries());
	     }
	     return dataset;
	     
//	     XYMultipleSeriesDataset dataset=new XYMultipleSeriesDataset();
//	     for(int i=0;i<2;i++){
////	         CategorySeries series = new CategorySeries("Demo Series"+i);
//	    	 CategorySeries series = new CategorySeries("");
//	         for(int k=0;k<5;k++){
//	             series.add(k*(i+2));
//	         }dataset.addSeries(series.toXYSeries());
//	     }
	     
	}
	
	private int getMaxStepNum(){
		int maxNum = 0;
		for(int i=0;i<values.size();i++){
			if(values.get(i)>maxNum){
				maxNum = values.get(i);
			}
		}
		return maxNum;
	}
	
	private void initData(){
		//init month string
		String LABLE_TEXT[] = getResources().getStringArray(R.array.monthStrings);
		for (String tem : LABLE_TEXT) {  
		      options.add(tem);  
		}
		
		int totalMonthStep = 0;
		
		for(int i=0;i<12;i++){
			String[] params = new String[] { String.valueOf(i+1) };
			List<LinkedHashMap<String, String>> data = sportDao.listMonthStepMaps(params);
			if(data.size()>0){
				for(int index=0;index<data.size();index++){
					totalMonthStep += Integer.parseInt(data.get(index).get("stepNum")) ;
				}
			}
			values.add(totalMonthStep);
			totalMonthStep = 0;
		}
//		Log.d("TAG", "values =================== "+values);
	}
	
	private void calSportInfo(int index){
        stepNum.setText(String.valueOf(values.get(index-1)));
        calDistance(values.get(index-1));
        calCalorie(values.get(index-1));
        currentMonth.setText(Integer.toString(index));
	}
	
	public void calDistance(int steps){
		float distance = 0;
		int height = Integer.parseInt(sp.getString(Constant.USER_HEIGHT, "175"));
		if(sp.getString(Constant.GENDER, "true").equals("true")){
			distance = (float)(0.415 * height)*steps; 
		}else {
			distance = (float)(0.413 * height)*steps;
		}
//		return distance/100000;
	
		int num =0;
		if(distance>100000){
			num = (int)(distance/100000);
			mileNum.setText(String.valueOf(num));
			meileageUnit.setText(R.string.kilometer_unit);
		}else{
			num = (int)(distance/100);
			mileNum.setText(String.valueOf(num));
			meileageUnit.setText(R.string.meter_unit);
		}
		
		int pgNum = num*1000 / 400;
		playgroundNum.setText(Integer.toString(pgNum));
		
//		double pgNum = 10.0 / 3;
//		playgroundNum.setText(Double.toString(pgNum));
	}

	public void calCalorie(int step){
		float calorie = 0;
		int weight = Integer.parseInt(sp.getString(Constant.USER_WEIGHT, "60"));
		calorie = (float) ((weight-15)*0.000693+0.005895);
//		return calorie*step;
		
		int calNum = (int)(calorie*step);
		calorieNum.setText(String.valueOf(calNum));
		
		int coke = calNum/215;
		cokeNum.setText(Integer.toString(coke));
	}
	
}
