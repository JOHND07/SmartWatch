/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.achartengine.chartdemo.demo.chart;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;

/**
 * Average temperature demo chart.
 */
public class AverageCubicTemperatureChart extends AbstractDemoChart {
  /**
   * Returns the chart name.
   * 
   * @return the chart name
   */
  public String getName() {
    return "Average temperature";
//	  return "";
  }

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  public String getDesc() {
    return "The average temperature in 4 Greek islands (cubic line chart)";
  }

  /**
   * Executes the chart demo.
   * 
   * @param context the context
   * @return the built intent
   */
//  public Intent execute(Context context) {
//    String[] titles = new String[] { "Crete", "Corfu", "Thassos", "Skiathos" };
//    List<double[]> x = new ArrayList<double[]>();
//    for (int i = 0; i < titles.length; i++) {
//      x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
//    }
//    List<double[]> values = new ArrayList<double[]>();
//    values.add(new double[] { 12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2,
//        13.9 });
//    values.add(new double[] { 10, 10, 12, 15, 20, 24, 26, 26, 23, 18, 14, 11 });
//    values.add(new double[] { 5, 5.3, 8, 12, 17, 22, 24.2, 24, 19, 15, 9, 6 });
//    values.add(new double[] { 9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10 });
//    int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.CYAN, Color.YELLOW };
//    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND,
//        PointStyle.TRIANGLE, PointStyle.SQUARE };
//    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
//    int length = renderer.getSeriesRendererCount();
//    for (int i = 0; i < length; i++) {
//      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
//    }
//    setChartSettings(renderer, "Average temperature", "Month", "Temperature", 0.5, 12.5, 0, 32,
//        Color.LTGRAY, Color.LTGRAY);
//    renderer.setXLabels(12);
//    renderer.setYLabels(10);
//    renderer.setShowGrid(true);
//    renderer.setXLabelsAlign(Align.RIGHT);
//    renderer.setYLabelsAlign(Align.RIGHT);
//    renderer.setZoomButtonsVisible(true);
//    renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
//    renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
//    Intent intent = ChartFactory.getCubicLineChartIntent(context, buildDataset(titles, x, values),
//        renderer, 0.33f, "Average temperature");
//    return intent;
//  }
  
  public Intent execute(Context context) {
	  
	    String[] titles = new String[] { "Crete", "Corfu", "Thassos"};
	    List<double[]> x = new ArrayList<double[]>();
	    for (int i = 0; i < titles.length; i++) {
	      x.add(new double[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 ,13,14,15,16,17,18,19,20,21,22,23});
	    }
	    
//	    for (int i = 0; i < titles.length; i++) {
//          x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
//        }
	    
	    List<double[]> values = new ArrayList<double[]>();
	    values.add(new double[] { 12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2,
	        13.9,12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2,
	        13.9 });
	    
	    values.add(new double[] { 10, 10, 12, 15, 20, 24, 26, 26, 23, 18, 14, 11, 10, 10, 12, 15, 20, 24, 26, 26, 23, 18, 14, 11 });
	    values.add(new double[] { 5, 5.3, 8, 12, 17, 22, 24.2, 24, 19, 15, 9, 6 , 5, 5.3, 8, 12, 17, 22, 24.2, 24, 19, 15, 9, 6 });
	    
//	    values.add(new double[] { 9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10, 9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10 });
	    
//	    int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.CYAN };
	    int[] colors = new int[] { Color.WHITE, Color.WHITE, Color.WHITE };
	    
	    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND,
	        PointStyle.TRIANGLE };
	    
	    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);

	    int length = renderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
	      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
	    }
	    
	    setChartSettings(renderer, "Average temperature", "Month", "Temperature", 0.5, 12.5, 0, 32,
	        Color.LTGRAY, Color.LTGRAY);
	    
//	    renderer.setMargins(margins);  
//        renderer.setMarginsColor(0x00ffffff);  

	 // 图表部分的背景颜色  
        renderer.setBackgroundColor(Color.parseColor("#49D6E7"));  
        renderer.setApplyBackgroundColor(true);  
        // 图表与屏幕四边的间距颜色  
//        renderer.setMarginsColor(Color.argb(0, 0xF3, 0xF3, 0xF3));  
        renderer.setXLabelsColor(Color.WHITE);//设置X轴刻度颜色
        renderer.setYLabelsColor(0, Color.WHITE);//设置Y轴刻度颜色
        
        renderer.setAxesColor(Color.WHITE);
        renderer.setChartTitleTextSize(30);  
        renderer.setAxisTitleTextSize(25);
        
	    renderer.setXLabels(12);
	    renderer.setYLabels(10);
	    
//	    renderer.setShowGrid(true);
	    renderer.setXLabelsAlign(Align.RIGHT);
	    renderer.setYLabelsAlign(Align.RIGHT);
	    renderer.setZoomButtonsVisible(true);
	    renderer.setPanLimits(new double[] { 0, 20, 0, 40 });
	    renderer.setZoomLimits(new double[] { 0, 20, 0, 40 });
	    
        
//	    Intent intent = ChartFactory.getCubicLineChartIntent(context, buildDataset(titles, x, values),
//	        renderer, 0.33f, "Average temperature");
	    
	    Intent intent = ChartFactory.getCubicLineChartIntent(context, buildDataset(titles, x, values),
		        renderer, 0.33f, "");
	    return intent;
	  }

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return null;
	}

}
