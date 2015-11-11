package com.example.billy.myapplication;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.*;
import android.view.Gravity;
import android.widget.Toast;

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
    public SensorService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        threshold = 5;
        previousY = 0;
        currentY = 0;
        stepCounter = 0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stepCounter = intent.getIntExtra("stepCounter", 0);
        String test = intent.getStringExtra("test");
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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x= event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        currentY = y;

        float resulty = Math.abs(currentY-previousY);
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
}
