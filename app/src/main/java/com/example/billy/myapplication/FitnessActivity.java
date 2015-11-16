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
 * Created by WaiHing on 13/11/2015.
 */
public class FitnessActivity extends Activity {

    TextView tv_BMI, tv_sweight, tv_step, tv_target;
    public FitnessPlan fitness;
    Float height, weight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inifitness);

        SharedPreferences settings = getSharedPreferences("user_info", MODE_PRIVATE);

        height = settings.getFloat("height", 0.00f);
        weight = settings.getFloat("weight", 0.00f);

        fitness = new FitnessPlan(height, weight);

        tv_BMI = (TextView)findViewById(R.id.tv_bmistatus);
        tv_sweight = (TextView)findViewById(R.id.tv_sweight);
        tv_step = (TextView)findViewById(R.id.tv_step);
        tv_target = (TextView)findViewById((R.id.tv_target));
        tv_BMI.setText("BMI : " + String.format("%.1f",fitness.getBMI()) + "\n" + fitness.getBMIStatus());
        tv_sweight.setText(fitness.getSuggestedWeightPlan());
        tv_step.setText("Healthy Life Style: " + fitness.getHealthyStyle() + " Steps");
        if(fitness.getBMI()>=25) {
            tv_target.setText("Target Days: " + (fitness.getTargetStep() / 10000));
        }else{
            tv_target.setVisibility(View.INVISIBLE);
        }
    }

    public void onPlanStart(View v){
        SharedPreferences settings = getSharedPreferences("fitness_plan", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("height", fitness.getHeight());
        editor.putFloat("weight", fitness.getWeight());
        editor.putInt("targetTotal", fitness.getTargetStep());
        editor.putInt("targetStepDay", fitness.getHealthyStyle());
        editor.putString("startDate", fitness.getDate());
        editor.commit();
        this.finish();
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