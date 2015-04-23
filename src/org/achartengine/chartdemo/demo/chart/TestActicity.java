package org.achartengine.chartdemo.demo.chart;

import java.util.ArrayList;
import java.util.List;

import com.guogee.smartwatch.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class TestActicity extends Activity implements OnClickListener{

	public static String LABLE_TEXT[] = { "", "8ÔÂ", "9ÔÂ", "10ÔÂ", "11ÔÂ" };  
    private LinearLayout layoutViewContent;  
    private Button btn_single;  
    private Button btn_total;  
    
    private double first[] = new double[] { 30, 40, 60, 100 };  
    private double second[] = new double[] { 50, 60, 88, 91 };  
    
    private List<String> options = new ArrayList<String>();  
    private boolean isSingleView;  
    private BarChartView view;  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.test);  
        isSingleView = true;  
        for (String tem : LABLE_TEXT) {  
            options.add(tem);  
        }  
        layoutViewContent = (LinearLayout) findViewById(R.id.barview_content);  
        
        view = new BarChartView(TestActicity.this, isSingleView);  
//        view.initData(first, second, options, "Ë«Öù×´Í¼");
        view.initData(first, options, "");
        
        layoutViewContent.setBackgroundColor(0xffffffff);  
        layoutViewContent.addView(view.getBarChartView());  
        
        btn_single = (Button) findViewById(R.id.single_view_btn);  
        btn_total = (Button) findViewById(R.id.double_view_btn);  
        btn_single.setOnClickListener(this);  
        btn_total.setOnClickListener(this);  
    }  
  
    @Override  
    public void onClick(View v) {  
        int id = v.getId();  
  
        switch (id) {  
            case R.id.single_view_btn:  
                isSingleView = true;  
                layoutViewContent.removeAllViews();  
                view = new BarChartView(TestActicity.this, isSingleView);  
                view.initData(first, second, options, "µ¥Öù×´Í¼");  
                layoutViewContent.addView(view.getBarChartView());  
                break;  
            case R.id.double_view_btn:  
                isSingleView = false;  
                layoutViewContent.removeAllViews();  
                view = new BarChartView(TestActicity.this, isSingleView);  
                view.initData(first, second, options, "Ë«Öù×´Í¼");  
                layoutViewContent.addView(view.getBarChartView());  
                break;  
        }  
  
    } 
    
}
