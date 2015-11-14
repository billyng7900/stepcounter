package com.example.billy.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.billy.myapplication.R;

/**
 * Created by WaiHing on 13/11/2015.
 */
public class FitnessActivity extends Activity {

    TextView tv_BMI, tv_sweight;
    public FitnessPlan fitness;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inifitness);
        tv_BMI = (TextView)findViewById(R.id.tv_bmistatus);
        tv_sweight = (TextView)findViewById(R.id.tv_sweight);
        fitness = new FitnessPlan();
        tv_BMI.setText("BMI = " + String.format("%.1f",fitness.getBMI()) + "\nYou are in " + fitness.getBMIStatus());
        tv_sweight.setText(fitness.getSuggestedWeightPlan());
    }
}