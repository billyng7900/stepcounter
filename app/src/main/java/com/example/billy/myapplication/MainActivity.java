package com.example.billy.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements SensorEventListener{
    TextView stepText,targetText;
    Button fitnessButton;
    int systemSteps = 0;
    int stepCounter;
    public MyReceiver receiver;
    StepDbHelper dbHelper;
    boolean hasRemainingStep, hasEnteredRecord;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepText = (TextView)findViewById(R.id.text_step);
        targetText = (TextView)findViewById(R.id.text_targetStep);
        fitnessButton = (Button)findViewById(R.id.button_fitness);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.teststepcounter");
        this.registerReceiver(receiver, filter);
        settings = getSharedPreferences("fitness_plan",MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(settings.getFloat("height",0)==0)
            hasEnteredRecord = false;
        else
            hasEnteredRecord = true;

        //get DB data
        dbHelper = new StepDbHelper(this);
        String[] args = {getDate()};
        stepCounter = dbHelper.getDbStep(args,this);
        if(stepCounter!=-1)
        {
            stepText.setText(String.valueOf(stepCounter));
        }
        else
        {
            stepText.setText(String.valueOf(0));
        }
        if(!isMyServiceRunning(SensorService.class))
            onStartService();

        setUpTarget();
        invalidateOptionsMenu();
    }

    public void setUpTarget()
    {
        int targetStep = settings.getInt("targetStepDay",0);
        hasRemainingStep = false;
        if(targetStep!=0)
        {
            fitnessButton.setVisibility(View.INVISIBLE);
            targetText.setVisibility(View.VISIBLE);
            int remainingStep = targetStep - stepCounter;
            if(remainingStep>0) {
                targetText.setText("Today Remaining Step: " + remainingStep);
                hasRemainingStep = true;
            }
            else
                targetText.setText("Today Target Reached! Keep It On!");
        }
        else
        {
            targetText.setVisibility(View.INVISIBLE);
            fitnessButton.setVisibility(View.VISIBLE);
        }
    }

    public void openFitnessPlan(View view)
    {
        switchIntent(FitnessActivity.class);
    }

    public void openStepRecord(View view)
    {
        switchIntent(StepRecordActivity.class);
    }

    public void openStepChart(View view)
    {
        switchIntent(StepChartActivity.class);
    }

    public void onStartService() {
        Intent i = new Intent(this,SensorService.class);
        startService(i);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfo = manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                //check it is here
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem newPlan = menu.findItem(R.id.action_fitness);
        MenuItem viewPlan = menu.findItem(R.id.action_daily_fitness);
        if(settings.getBoolean("planStarted", false)){
            newPlan.setVisible(false);
        }else{
            viewPlan.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_step_record) {
            switchIntent(StepRecordActivity.class);
        }
        else if(id==R.id.action_fitness)
        {
            switchIntent(FitnessActivity.class);
        }
        else if (id==R.id.action_daily_fitness)
        {
            switchIntent(DailyFitnessActivity.class);
        }
        else if(id==R.id.action_add_user_info)
        {
            if(hasEnteredRecord)
                switchIntent(ViewActivity.class);
            else
                switchIntent(UserInfoActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchIntent(Class<?> activityClass)
    {
        Intent intent = new Intent(this,activityClass);
        startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        /*if(isRunning) {
            stepCounter = (int) event.values[0];
            stepText.setText(stepCounter);
        }*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }

    public class MyReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            stepCounter = bundle.getInt("stepCounter");
            stepText.setText(String.valueOf(stepCounter));
            if(hasRemainingStep)
            {
                SharedPreferences settings = getSharedPreferences("fitness_plan",MODE_PRIVATE);
                int targetStep = settings.getInt("targetStepDay", 0);
                int remainingStep = targetStep - stepCounter;
                if(remainingStep>0) {
                    targetText.setText("Today Remaining Step: " + remainingStep);

                }
                else {
                    targetText.setText("Today Target Reached! Keep It On!");
                    hasRemainingStep = false;
                }
            }
        }
    }

    private String getDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
