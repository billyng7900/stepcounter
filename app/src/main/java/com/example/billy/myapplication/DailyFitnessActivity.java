package com.example.billy.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Created by WaiHing on 15/11/2015.
 */
public class DailyFitnessActivity extends Activity {

    TextView tv_dailyStep, tv_targetDays;
    FitnessPlan fitness;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyfitness);

        SharedPreferences settings = getSharedPreferences("fitness_plan", MODE_PRIVATE);

        Float height = settings.getFloat("height", 0.00f);
        Float weight = settings.getFloat("weight", 0.00f);
        int targetTotal = settings.getInt("targetTotal", 0);
        int targetStepDay = settings.getInt("targetStepDay", 0);
        String startDate = settings.getString("startDate", "ERROR");

        fitness = new FitnessPlan(height, weight);

        tv_dailyStep = (TextView)findViewById(R.id.tv_dailyStep);
        tv_targetDays = (TextView)findViewById(R.id.tv_targetDays);

        tv_dailyStep.setText("Target Steps for Today: " + fitness.getHealthyStyle());
        if(fitness.getBMI()>=25) {
            tv_targetDays.setText("Target Days: " + targetTotal / targetStepDay);
        }
    }

    public void onViewStep(View v){
        switchIntent(MainActivity.class);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void switchIntent(Class<?> activityClass)
    {
        Intent intent = new Intent(this,activityClass);
        startActivity(intent);
    }

}