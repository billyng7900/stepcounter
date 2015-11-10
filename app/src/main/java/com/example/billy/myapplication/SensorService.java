package com.example.billy.myapplication;

import android.app.Activity;
import android.app.IntentService;
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
public class SensorService extends IntentService implements SensorEventListener {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    int stepCounter;
    SensorManager sensorManager;
    boolean isRunning;
    private float previousY;
    private float currentY;
    private int numSteps;
    private int threshold;

    public SensorService() {
        super("test-service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        threshold = 5;
        previousY = 0;
        currentY = 0;
        numSteps = 0;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(sensor!=null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            Toast toast = Toast.makeText(getApplicationContext(), "Sensor listener registered of type: ", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
        else
        {
            Toast.makeText(this,"Counter Not available",Toast.LENGTH_SHORT).show();
        }
        stepCounter = intent.getIntExtra("stepCounter", 0);
        isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isRunning)
        {
            isRunning = true;
        }
        else
        {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String test = intent.getStringExtra("test");

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
            /*Bundle bundle = new Bundle();
            bundle.putInt("stepCounter",stepCounter);
            rec.send(Activity.RESULT_OK, bundle);
            */
            Toast.makeText(this,"AAAAA"+stepCounter,Toast.LENGTH_SHORT).show();
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
}
