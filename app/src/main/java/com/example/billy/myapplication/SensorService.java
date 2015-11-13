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
    int stepCounter;
    SensorManager sensorManager;
    String isRunning;
    private float previousY;
    private float currentY;
    private int numSteps;
    private int threshold;
    Timer myTimer;
    MyTimerStore myTimerStore;
    StepDbHelper dbHelper;
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
        dbHelper.getWritableDatabase();
    }

    public void getDbData()
    {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {StepEntry.COLUMN_NAME_Step};
        String[] args = {getDate()};
        Cursor c = db.query(StepEntry.TABLE_NAME,projection,StepEntry.COLUMN_NAME_Date+"=?",args,null,null,null,null);
        if(c.moveToFirst())
        {
            stepCounter=c.getInt(0);
        }
        else
        {
            stepCounter = 0;
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getDbData();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            Toast toast = Toast.makeText(getApplicationContext(), "Sensor listener registered of type: ", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        } else {
            Toast.makeText(this, "Counter Not available", Toast.LENGTH_SHORT).show();
        }
        myTimer.scheduleAtFixedRate(myTimerStore,0,900000);
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
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] projection = {StepEntry.COLUMN_NAME_Step};
            String[] args = {getDate()};
            Cursor c = db.query(StepEntry.TABLE_NAME, projection, StepEntry.COLUMN_NAME_Date + "=?", args, null, null, null, null);
            if(c.moveToFirst())
            {
                hasTodayData = true;
            }
            SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
            if(!hasTodayData)
            {
                ContentValues values = new ContentValues();
                values.put(StepEntry.COLUMN_NAME_Date, getDate());
                values.put(StepEntry.COLUMN_NAME_Step, stepCounter);
                dbWrite.insert(StepEntry.TABLE_NAME, null, values);
            }
            else
            {
                ContentValues upValues = new ContentValues();
                upValues.put(StepEntry.COLUMN_NAME_Step, stepCounter);
                String[] argsUpdate = {getDate()};
                dbWrite.update(StepEntry.TABLE_NAME,upValues,StepEntry.COLUMN_NAME_Date+"=?",argsUpdate);
            }

        }
    }
}
