package com.example.billy.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.billy.myapplication.R;

import java.util.ArrayList;

/**
 * Created by WaiHing on 13/11/2015.
 */
public class FitnessActivity extends Activity {

    TextView tv_BMI, tv_sweight, tv_step, tv_target;
    public FitnessPlan fitness;
    Float height, weight, target;
    String startDate;
    SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inifitness);

        settings = getSharedPreferences("fitness_plan", MODE_PRIVATE);
//test value//
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("height", 1.65f);
        editor.putFloat("weight", 60);
        editor.putFloat("target", 0);
        editor.putString("startDate", "test");

        editor.commit();
//test value//

        height = settings.getFloat("height", 0.00f);
        weight = settings.getFloat("weight", 0.00f);
        target = settings.getFloat("target", 0.00f);
        startDate = settings.getString("startDate", "ERROR");

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
        }
    }

    public void onPlanStart(View v){
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("height", fitness.getHeight());
        editor.putFloat("weight", fitness.getWeight());
        editor.putFloat("target", fitness.getTargetStep());
        editor.putString("startDate", fitness.getDate());

        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}