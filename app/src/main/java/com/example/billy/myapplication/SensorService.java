package com.example.billy.myapplication;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.*;
import android.view.Gravity;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Billy on 10/11/2015.
 */
public class SensorService extends Service implements SensorEventListener {
    private int stepCounter;
    private SensorManager sensorManager;
    private String isRunning;
    private float previousY;
    private float currentY;
    private int numSteps;
    private int threshold;
    private Timer myTimer;
    private MyTimerStore myTimerStore;
    private  StepDbHelper dbHelper;
    private Context context = this;

    public SensorService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        threshold = 5;
        previousY = 0;
        currentY = 0;
        myTimer = new Timer();
        myTimerStore = new MyTimerStore();
        dbHelper = new StepDbHelper(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String[] args = {getDate()};
        int dbStep = dbHelper.getDbStep(args, context);
        if(dbStep!=-1)
            stepCounter = dbStep;
        else
            stepCounter = 0;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Counter Not available", Toast.LENGTH_SHORT).show();
        }
        myTimer.scheduleAtFixedRate(myTimerStore,0,900000);//update db each 15 minutes if there is a change.
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x= event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        currentY = y;

        float resulty = Math.abs(currentY - previousY);
        if(resulty>threshold&&resulty<30)
        {
            stepCounter++;
            //Toast.makeText(this,"AAAAA"+stepCounter,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("stepCounter",stepCounter);
            intent.setAction("android.intent.action.teststepcounter");
            sendBroadcast(intent);
        }
        previousY = y;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private String getDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private class MyTimerStore extends TimerTask
    {
        @Override
        public void run() {
            boolean hasTodayData = false;
            String[] args = {getDate()};
            int dbStep = dbHelper.getDbStep(args, context);
            if(dbStep!=-1)
            {
                hasTodayData = true;
            }
            if(!hasTodayData)
            {
                stepCounter = 0; //to ensure yesterday's data is cleared.
                ContentValues values = new ContentValues();
                values.put(StepEntry.COLUMN_NAME_Date, getDate());
                values.put(StepEntry.COLUMN_NAME_Step, stepCounter);
                dbHelper.insertStep(values,context);
            }
            else
            {
                if(dbStep!=stepCounter) //has value changed, if no change, no update.
                {
                    ContentValues upValues = new ContentValues();
                    upValues.put(StepEntry.COLUMN_NAME_Step, stepCounter);
                    String[] argsUpdate = {getDate()};
                    dbHelper.updateStep(upValues, argsUpdate, context);
                }
            }
        }
    }
}
