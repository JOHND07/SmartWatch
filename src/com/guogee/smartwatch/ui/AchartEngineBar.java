package com.guogee.smartwatch.ui;

import java.util.ArrayList;
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

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AchartEngineBar extends Activity {

	public GraphicalView mChartView ;
	public static String LABLE_TEXT[] = {"", "1月", "2月", "3月", "4月", "5月","6月","7月","8月","9月" }; 
	private List<String> options = new ArrayList<String>(); 
    private List<double[]> values = new ArrayList<double[]>();  
    private double first[] = new double[] {10,20,30, 40, 50, 60, 70,80,90 }; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.chart);
		
		for (String tem : LABLE_TEXT) {  
            options.add(tem);  
        }
		
		values.add(first); 
		
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
         r.setDisplayChartValues(true);  
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
//	        renderer.setYLabels(10);  
	        render.setLabelsTextSize(25);  

	        // renderer.setXLabelsColor(0xff000000);//设置X轴上的字体颜�?  
	        render.setYLabelsColor(0,0xffffffff);//设置Y轴上的字体颜�?  

		     render.setXAxisMin(0);  //0
		     render.setXAxisMax(6);  //6
		     render.setYAxisMin(0);  //0 
		     render.setYAxisMax(100);  //10
		     
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
	      
	     layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
	             LayoutParams.FILL_PARENT));
	     
	     
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
	           Toast.makeText(
	               AchartEngineBar.this,
//	               "Chart element in series index " + seriesSelection.getSeriesIndex()
//	               "Chart element in series index " + seriesSelection.getSeriesIndex()
//	                   + " data point index " + seriesSelection.getXValue() + " was clicked"
//	                   + " closest point value X=" + seriesSelection.getXValue() + ", Y=" + seriesSelection.getValue()
//	                   + " clicked point value X=" + (float) xy[0] + ", Y=" + (float) xy[1], Toast.LENGTH_SHORT).show();

	               "Click position ====== "+seriesSelection.getXValue() ,Toast.LENGTH_SHORT).show();
	         }
	       }
	     });
	}
	
	
	private XYMultipleSeriesDataset buildBarDataset( List<double[]> values) {  
		//get dataSet
	     XYMultipleSeriesDataset dataset=new XYMultipleSeriesDataset();
	     for(int i=0;i<2;i++){
	    	 CategorySeries series = new CategorySeries("");
	    	 double [] v = values.get(0);
	    	 int seriesLength = v.length;
	         for(int k=0;k<seriesLength;k++){
	             series.add(v[k]);
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
	
	
}
