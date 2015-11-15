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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_step_record){
            switchIntent(StepRecordActivity.class);
        }
        else if(id==R.id.action_fitness){
            switchIntent(FitnessActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchIntent(Class<?> activityClass)
    {
        Intent intent = new Intent(this,activityClass);
        startActivity(intent);
    }

}