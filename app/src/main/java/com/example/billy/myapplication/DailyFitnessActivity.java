package com.example.billy.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by WaiHing on 15/11/2015.
 */
public class DailyFitnessActivity extends Activity {

    TextView tv_dailyStep, tv_targetDays;
    FitnessPlan fitness;
    Float height, weight;
    int targetTotal,targetStepDay;
    String startDate;
    ArrayList<Step> stepArrayList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyfitness);
        ActionBar ab = getActionBar();
        getActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences settings = getSharedPreferences("fitness_plan", MODE_PRIVATE);

        height = settings.getFloat("height", 0.00f);
        weight = settings.getFloat("weight", 0.00f);
        targetTotal = settings.getInt("targetTotal", 0);
        targetStepDay = settings.getInt("targetStepDay", 0);
        startDate = settings.getString("startDate", "ERROR");

        fitness = new FitnessPlan(height, weight);
        fitness.setStartDate(startDate);
        fitness.setAccumulatedStep(getStepRecord());
        stepArrayList = new ArrayList<>();

        tv_dailyStep = (TextView)findViewById(R.id.tv_dailyStep);
        tv_targetDays = (TextView)findViewById(R.id.tv_targetDays);

        tv_dailyStep.setText("Target Steps for Today: " + fitness.getHealthyStyle());
        if(fitness.getBMI()>=25) {
            String s = "My Fitness Plan from: " + fitness.getStartDate()
                    + "\nAccumulated Steps: " + fitness.getAccumulatedStep()
                    + "\nTarget Steps: " + targetTotal
                    + "\nTarget Days: " + (targetTotal-fitness.getAccumulatedStep())/targetStepDay;
            tv_targetDays.setText(s);
        }else{
            tv_targetDays.setVisibility(View.INVISIBLE);
        }
    }

    public int getStepRecord(){
        int accumulatedSteps = 0;
        StepDbHelper dbHelper = new StepDbHelper(this);
        stepArrayList = dbHelper.getAllStepRecord(this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
        Date planStartDate = dateFormat.parse(startDate);
            for (int i=0; i<stepArrayList.size();i++){
                String s = stepArrayList.get(i).getDate();
                Date recordDate = dateFormat.parse(s);
            if(recordDate.equals(planStartDate) || recordDate.after(planStartDate)){
                accumulatedSteps += stepArrayList.get(i).getStep();
            }
        }
        }catch (ParseException e){
            e.printStackTrace();
        }
        return accumulatedSteps;
    }

    public void onViewStep(View v){
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
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

}