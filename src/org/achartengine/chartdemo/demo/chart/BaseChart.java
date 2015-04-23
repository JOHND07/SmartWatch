package org.achartengine.chartdemo.demo.chart;

import java.util.ArrayList;

import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.view.View;

public abstract class BaseChart
{
    public static final int DETAIL_TIME_STYLE = 0;
    public static final int TIME_STYLE = 1;
    public static final int PING = 2;
    public static final int EMAIL = 3;
    protected int style;

    public String title;
    public int color;
    public int[] colors;
    public PointStyle pointStyle;
    public PointStyle[] styles;

    protected ArrayList<ChartCallBack> mChartCallBacks = new ArrayList<ChartCallBack>();

    public static interface ChartCallBack
    {
        public void refreshChartView();
    }

    protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles)
    {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setRenderer(renderer, colors, styles);
        return renderer;
    }

    protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles)
    {
//        renderer.setAxisTitleTextSize(10);
//        renderer.setChartTitleTextSize(15);
//        renderer.setLabelsTextSize(10);
        // renderer.setLegendTextSize(15);
//        renderer.setPointSize(5f);

        renderer.setAxisTitleTextSize(20);
        renderer.setChartTitleTextSize(25);
        renderer.setLabelsTextSize(20);
        renderer.setPointSize(13f);
        
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setShowGridX(false);
        renderer.setShowGridY(true);
        
        renderer.setXLabelsAlign(Align.LEFT);
        renderer.setYLabelsAlign(Align.RIGHT);
//        renderer.setLabelsColor(Color.BLACK);
//        renderer.setXLabelsColor(Color.BLACK);
//        renderer.setYLabelsColor(0, Color.BLACK);
        
        renderer.setLabelsColor(Color.WHITE);
        renderer.setXLabelsColor(Color.WHITE);
        renderer.setYLabelsColor(0, Color.WHITE);
        
        renderer.setZoomButtonsVisible(false);
        renderer.setZoomEnabled(false, false);
        renderer.setClickEnabled(false);
//        renderer.setMarginsColor(Color.argb(255, 255, 255, 255));
        renderer.setMarginsColor(Color.parseColor("#49D6E7"));
        
        renderer.setPanLimits(new double[] { 0, 23, 0, 40 });
        renderer.setPanEnabled(true, false);
        renderer.setShowLegend(false);
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.WHITE);
        renderer.setShowLabels(true);
        renderer.setShowAxes(true);
        renderer.setMargins(new int[] { 20, 40, 20, 20 });
        int length = colors.length;
        for (int i = 0; i < length; i++)
        {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
    }

    protected DefaultRenderer buildCategoryRenderer(int[] colors)
    {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLabelsTextSize(15);
        renderer.setShowLegend(false);
        renderer.setMargins(new int[] { 40, 40, 40, 80 });
        for (int color : colors)
        {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    public abstract GraphicalView getChartView(Context context, boolean inScroll);

    public abstract DefaultRenderer getRenderer();

    public abstract boolean isEmpty();

    public View getChartLayout(Context context, boolean disableChartTitle)
    {
        return getChartView(context, disableChartTitle);
    }

    public View getChartLayout(Context context)
    {
        return getChartLayout(context, true);
    }

    public DefaultRenderer getRendererScroll()
    {
        DefaultRenderer renderer = getRenderer();
        renderer.setInScroll(true);
        return renderer;
    }

    public void registCallBack(ChartCallBack chartCallBack)
    {
        if (chartCallBack == null)
        {
            return;
        }
        if (mChartCallBacks == null)
        {
            return;
        }
        if (mChartCallBacks.contains(chartCallBack))
        {
            return;
        }
        mChartCallBacks.add(chartCallBack);
    }

    public void removeCallBack(ChartCallBack chartCallBack)
    {
        if (chartCallBack == null)
        {
            return;
        }
        if (mChartCallBacks == null)
        {
            return;
        }
        if (!mChartCallBacks.contains(chartCallBack))
        {
            return;
        }
        mChartCallBacks.remove(chartCallBack);
    }

    public void refreshView()
    {
        if (mChartCallBacks == null)
        {
            return;
        }
        for (ChartCallBack chartCallBack : mChartCallBacks)
        {
            chartCallBack.refreshChartView();
        }
    }
}
