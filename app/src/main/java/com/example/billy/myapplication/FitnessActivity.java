package com.example.billy.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by WaiHing on 13/11/2015.
 */
public class FitnessActivity extends Activity {

    TextView tv_BMI, tv_sweight, tv_step, tv_target;
    Button btn_start;
    public FitnessPlan fitness;
    Float height, weight;
    String gender;
    SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inifitness);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        tv_BMI = (TextView)findViewById(R.id.tv_bmistatus);
        tv_sweight = (TextView)findViewById(R.id.tv_sweight);
        tv_step = (TextView)findViewById(R.id.tv_step);
        tv_target = (TextView)findViewById((R.id.tv_target));
        btn_start = (Button)findViewById(R.id.btn_start);
    }

    public void onPlanStart(View v){
        settings = getSharedPreferences("fitness_plan", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("height", fitness.getHeight());
        editor.putFloat("weight", fitness.getWeight());
        editor.putInt("targetTotal", fitness.getTargetCalBurn());
        editor.putInt("targetStepDay", fitness.getHealthyStyle());
        editor.putString("startDate", fitness.getDate());
        editor.putBoolean("planStarted", true);
        editor.commit();
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings = getSharedPreferences("user_info", MODE_PRIVATE);
        height = settings.getFloat("height", 0.00f);
        weight = settings.getFloat("weight", 0.00f);
        gender = settings.getString("gender", "gender");
        if(height==0.00f || weight==0.00f){
            Intent intent = new Intent(this, UserInfoActivity.class);
            startActivity(intent);
        }
        fitness = new FitnessPlan(height, weight);
        fitness.setGender(gender);

        tv_BMI.setText("BMI : " + String.format("%.1f", fitness.getBMI()) + "\n" + fitness.getBMIStatus());
        tv_sweight.setText(fitness.getSuggestedWeightPlan());
        tv_step.setText("Healthy Life Style: " + fitness.getHealthyStyle() + " Steps");
        if(fitness.getBMI()>=24 || settings.getBoolean("planStarted",false)) {
            tv_target.setText("Target Days: " + (fitness.getTargetDays()));
        }else{
            tv_target.setVisibility(View.INVISIBLE);
            btn_start.setVisibility(View.INVISIBLE);
        }
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