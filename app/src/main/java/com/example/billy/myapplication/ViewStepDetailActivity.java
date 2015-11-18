package com.example.billy.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewStepDetailActivity extends Activity {
    TextView text_date,text_step,text_kcal,text_progressBar;
    ProgressBar progressBar;
    Step step;
    SharedPreferences settings;
    int targetStep;
    String planDate;
    final private float stepCal = 0.044f;
    final private int calPerKg = 7700;
    String dateString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_step_detail);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        text_date = (TextView)findViewById(R.id.text_Date);
        text_step = (TextView)findViewById(R.id.text_step);
        text_kcal = (TextView)findViewById(R.id.text_kcal);
        text_progressBar = (TextView)findViewById(R.id.text_progressBar);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        settings = getSharedPreferences("fitness_plan", MODE_PRIVATE);
        targetStep = settings.getInt("targetStepDay", 0);
        planDate = settings.getString("startDate","ERROR");
        Intent i = getIntent();
        step = new Step();
        step.setDate(i.getStringExtra("stepDate"));
        step.setStep(i.getIntExtra("stepRecord", 0));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        Date dateOld = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayNew = dateFormat.format(dateOld);
        Date finalDate = new Date();
        try {
            finalDate = dateFormat.parse(dayNew);
        }catch (Exception e)
        {

        }
        if(finalDate.compareTo(step.getConvertToDate())==0)
        {
            dateString=" today";
        }
        else
        {
            dateString=" on that day";
        }
        text_date.setText(step.getDate());
        text_step.setText("Step walked: " + step.getStep());
        int kcal = (int)(step.getStep()*stepCal);
        text_kcal.setText("Burned " + kcal +" kcal"+dateString+" by walking");
        int result = (int)(((float)step.getStep()/(float)targetStep)*100);
        if((step.getConvertToDate().compareTo(stringToDate(planDate))>-1)) {
            if (result < 100) {
                text_progressBar.setText("Achieved " + result + "% of target steps"+dateString);
            } else {
                result = 100;
                text_progressBar.setText("Reached the target"+dateString+". Keep it on!");
            }
            progressBar.setProgress(result);
        }
        else
        {
            if(finalDate.compareTo(step.getConvertToDate())==0)
            {
                text_progressBar.setText("You haven't set any plan yet.");
            }
            else
            {
                text_progressBar.setText("You haven't set any plan on that day yet");
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    public Date stringToDate(String date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();
        try{
            convertedDate = dateFormat.parse(date);
            return convertedDate;
        }catch(Exception e)
        {
            return null;
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
