package com.example.billy.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class StepChartActivity extends Activity implements OnChartGestureListener, OnChartValueSelectedListener{
    private LineChart lineChart;
    private SharedPreferences settings;
    private int targetStep;
    StepDbHelper dbHelper;
    ArrayList<Step> stepList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_chart_activity);
        ActionBar ab = getActionBar();
        getActionBar().setDisplayHomeAsUpEnabled(true);

        lineChart = (LineChart)findViewById(R.id.chart);
        dbHelper = new StepDbHelper(this);
        settings = getSharedPreferences("fitness_plan", MODE_PRIVATE);
        targetStep = settings.getInt("targetStepDay",0);
        lineChart.setOnChartGestureListener(this);
        lineChart.setOnChartValueSelectedListener(this);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription("");
        lineChart.setNoDataText("No Step Record.");
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        //lineChart.setBackgroundColor(Color.GRAY);

        XAxis xaxis = lineChart.getXAxis();
        //Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        /*LimitLine llx = new LimitLine(-30f,"Lower Limit");
        llx.setLineWidth(4f);
        llx.enableDashedLine(10f, 10f, 0f);
        llx.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llx.setTextSize(10f);
        llx.setTypeface(tf);*/
        LimitLine llx_upper = new LimitLine(targetStep,"Step target");
        llx_upper.setLineWidth(4f);
        llx_upper.enableDashedLine(10f, 10f, 0f);
        llx_upper.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        llx_upper.setTextSize(10f);
        //llx_upper.setTypeface(tf);
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.removeAllLimitLines();
        yAxis.addLimitLine(llx_upper);
        yAxis.setAxisMaxValue(12000f);
        yAxis.setAxisMinValue(0f);
        yAxis.setStartAtZero(true);
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setDrawLimitLinesBehindData(true);
        lineChart.getAxisRight().setEnabled(false);
        setData();
        lineChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subclass, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertRecord(int s,String date)
    {
        //test function!!!!! delete it before launch!
        ContentValues upValues = new ContentValues();
        upValues.put(StepEntry.COLUMN_NAME_Date, date);
        upValues.put(StepEntry.COLUMN_NAME_Step, s);
        dbHelper.insertStep(upValues,this);

    }
    private void setData()
    {
        /*for(int i=14;i>0;i--)
        {
        //gen November step
            int genStep = (int)(Math.random()*10000);
            insertRecord(genStep,"2015-11-"+i);
        }*/
        /*for(int i=30;i>0;i--)
        {
            //gen October step
            int genStep = (int)(Math.random()*10000);
            insertRecord(genStep,"2015-10-"+i);
        }*/
        stepList = dbHelper.getAllStepRecord(this);
        if(stepList.size()>30)
        {
            stepList = getLatest30DaysResut();
        }
        ArrayList<String> xVals = new ArrayList<>();
        for(int i=0;i<stepList.size();i++)
        {
            xVals.add("Day "+(i+1));
        }
        ArrayList<Entry> yVals = new ArrayList<>();
        for(int i=0;i<stepList.size();i++)
        {
            int val = stepList.get(i).getStep();
            yVals.add(new Entry(val,i));
        }
        LineDataSet set = new LineDataSet(yVals,"Step Count Set");
        set.enableDashedLine(10f, 5f, 0f);
        set.enableDashedHighlightLine(10f, 5f, 0f);
        set.setColor(Color.BLACK);
        set.setCircleColor(Color.BLACK);
        set.setLineWidth(1f);
        set.setCircleSize(2f);
        set.setDrawCircleHole(false);
        set.setValueTextSize(9f);
        set.setFillAlpha(65);
        set.setFillColor(Color.BLACK);
        ArrayList<LineDataSet> finalData = new ArrayList<>();
        finalData.add(set);
        LineData data = new LineData(xVals,finalData);
        lineChart.setData(data);
    }

    private ArrayList<Step> getLatest30DaysResut()
    {
        ArrayList<Step> newStepList = new ArrayList<>();
        Date maxDate = stepList.get(0).getConvertToDate();
        for(Step s:stepList)
        {
            int result = maxDate.compareTo(s.getConvertToDate());
            if(result<0)
                maxDate = s.getConvertToDate();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(maxDate);
        cal.add(Calendar.DATE,-30);
        Date date30Before = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateBefore30Days = dateFormat.format(date30Before);
        Date finalDate = new Date();
        try {
            finalDate = dateFormat.parse(dateBefore30Days);
        }catch (Exception e)
        {

        }
        Collections.sort(stepList, new Comparator<Step>() {
            @Override
            public int compare(Step lhs, Step rhs) {
                return (lhs).getConvertToDate().compareTo(rhs.getConvertToDate());
            }
        });
        for(Step s:stepList)
        {
            int result = finalDate.compareTo(s.getConvertToDate());
            if(result<1)
            {
                newStepList.add(s);
            }
        }
        return newStepList;
    }

    @Override
    public void onChartGestureStart(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent motionEvent) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent motionEvent) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent motionEvent) {

    }

    @Override
    public void onChartFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

    }

    @Override
    public void onChartScale(MotionEvent motionEvent, float v, float v1) {

    }

    @Override
    public void onChartTranslate(MotionEvent motionEvent, float v, float v1) {

    }

    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {

    }

    @Override
    public void onNothingSelected() {

    }
}
