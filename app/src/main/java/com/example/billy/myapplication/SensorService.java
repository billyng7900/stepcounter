package com.example.billy.myapplication;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.*;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    private int numSteps,targetStep;
    private int threshold;
    private Timer myTimer;
    private MyTimerStore myTimerStore;
    private  StepDbHelper dbHelper;
    private Context context;
    Notification.Builder fixedBuilder,reminderBuilder;
    Notification fixedNotification;
    private int remainingStep;
    private SharedPreferences settings;

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
        context = this;
        fixedBuilder = new Notification.Builder(this);
        reminderBuilder = new Notification.Builder(this);
        settings = getSharedPreferences("fitness_plan", MODE_PRIVATE);
        registerReceiver(ReminderNotification,new IntentFilter("com.billyng.MYACTION"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        targetStep = settings.getInt("targetStepDay",0);
        String[] args = {getDate()};
        int dbStep = dbHelper.getDbStep(args, context);
        if(dbStep!=-1)
            stepCounter = dbStep;
        else
            stepCounter = 0;
        setUpFixedNotification();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Counter Not available", Toast.LENGTH_SHORT).show();
        }
        myTimer.scheduleAtFixedRate(myTimerStore,0,900000);//update db each 15 minutes if there is a change.
        setupReminder();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x= event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        currentY = y;

        float resulty = Math.abs(currentY - previousY);
        if(resulty>threshold&&resulty<10)
        {
            stepCounter++;
            //Toast.makeText(this,"AAAAA"+stepCounter,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("stepCounter",stepCounter);
            intent.setAction("android.intent.action.teststepcounter");
            sendBroadcast(intent);
            setUpFixedNotification();
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

    private void setupReminder()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,17);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);
        Intent reminderNotification = new Intent("com.billyng.MYACTION");
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, reminderNotification, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pi);

    }
    private void setUpFixedNotification()
    {
        targetStep = settings.getInt("targetStepDay",0);
        String msgText  = "Today step: "
                + stepCounter;
        if(targetStep>0)
        {
            msgText +="\nTarget step: " +
                    targetStep;
        }
        else
        {
            msgText +="\nSet up your fitness plan now!";
        }

        fixedBuilder.setSmallIcon(R.drawable.app_icon);
        fixedBuilder.setContentTitle("Step Counter");
        fixedBuilder.setContentText("Your step record");
        fixedBuilder.setAutoCancel(true);
        Intent nIntent = new Intent(this, MainActivity.class);
        nIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent intent1 = new Intent(this,FitnessActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, new Intent(this,FitnessPlan.class), PendingIntent.FLAG_UPDATE_CURRENT);
        fixedBuilder.setContentIntent(pendingIntent);
        //fixedBuilder.addAction(R.drawable.view_notification, "Show", pendingIntent1);
        //fixedBuilder.addAction(R.drawable.view_step_record, "Review", pendingIntent2);
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        fixedNotification = new Notification.BigTextStyle(fixedBuilder).bigText(msgText).build();
        fixedNotification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        nManager.notify(0, fixedNotification);
    }
    private class MyTimerStore extends TimerTask {
        @Override
        public void run() {
            boolean hasTodayData = false;
            String[] args = {getDate()};
            int dbStep = dbHelper.getDbStep(args, context);
            if (dbStep != -1) {
                hasTodayData = true;
            }
            if (!hasTodayData) {
                stepCounter = 0; //to ensure yesterday's data is cleared.
                ContentValues values = new ContentValues();
                values.put(StepEntry.COLUMN_NAME_Date, getDate());
                values.put(StepEntry.COLUMN_NAME_Step, stepCounter);
                dbHelper.insertStep(values, context);
            } else {
                if (dbStep != stepCounter) //has value changed, if no change, no update.
                {

                    ContentValues upValues = new ContentValues();
                    upValues.put(StepEntry.COLUMN_NAME_Step, stepCounter);
                    String[] argsUpdate = {getDate()};
                    dbHelper.updateStep(upValues, argsUpdate, context);
                }
            }
        }
    }
    BroadcastReceiver ReminderNotification = new BroadcastReceiver()  {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title,content;
            int remainingStep = targetStep-stepCounter;
            Toast.makeText(context,String.valueOf(remainingStep), Toast.LENGTH_SHORT);
            if(remainingStep>2000)
            {
                title = "Go out for a walk!";
                content = "you still have to walk "+(remainingStep)+" today";
            }
            else if(remainingStep<2000&&remainingStep>0)
            {
                title = "Just little to go";
                content = "you still have to walk "+(remainingStep)+" today";
            }
            else
            {
                title = "You have reached the target";
                content = "Click this to view your step record today";
            }
            PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class),0);
            Notification.Builder reminderBuilder = new Notification.Builder(context);
            reminderBuilder.setSmallIcon(R.drawable.app_icon);
            reminderBuilder.setContentTitle(title);
            reminderBuilder.setContentText(content);
            reminderBuilder.setContentIntent(pi);
            reminderBuilder.setDefaults(Notification.DEFAULT_SOUND);
            reminderBuilder.setAutoCancel(true);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1,reminderBuilder.build());
        }
    };
}
