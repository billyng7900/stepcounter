package com.example.billy.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by WaiHing on 14/11/2015.
 */
public class FitnessPlan {
    FitnessDbHelper dbHelper;
    final private float stepCal = 0.044f;
    final private int calPerKg = 7700;
    private float height;
    private float weight;
    private float BMI_value;
    private String BMI_status;
    private int targetStep;

    public FitnessPlan(){
        height = 0.00f;
        weight = 0.00f;
        BMI_value = 0.00f;
        targetStep = 0;
        //getDbData();
    }

    public void getDbData(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection_h = {FitnessEntry.COLUMN_NAME_Height};
        Cursor c = db.query(FitnessEntry.TABLE_NAME, projection_h, null, null, null, null, null, null);
        if(c.moveToFirst()) {
            height = c.getFloat(0);
            weight = c.getFloat(1);
            targetStep = c.getInt(2);
        }else{
            height = 0.00f;
            weight = 0.00f;
            targetStep = 0;
        }
    }

    public void setDbData(){
        String[] args = {getDate()};
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FitnessEntry.COLUMN_NAME_Target, targetStep);
        values.put(FitnessEntry.COLUMN_NAME_Date, getDate());
       // dbHelper.insertTargetStep(values, args, this);
    }

    public float getBMI(){
//test value//
        height = 1.55f;
        weight = 72;
//test value//
        BMI_value = (weight/height)/height;
        return BMI_value;
    }

    public String getBMIStatus(){
        BMI_status = "";
        getBMI();

        if(BMI_value < 18.5){
            BMI_status = "Underweight";
        }
        if(BMI_value >= 18.5 && BMI_value < 25){
            BMI_status = "Healthy Weight";
        }
        if(BMI_value >=25 && BMI_value < 30){
            BMI_status = "Overweight";
        }
        if(BMI_value >= 30){
            BMI_status = "Obese";
        }
        return BMI_status;
    }

    public float getSuggestedWeight(float bmi){
        Float sWeight = 0.00f;
        sWeight = (height * height) * bmi;
        return sWeight;
    }

    public String getSuggestedWeightPlan(){
        String plan = "";
        switch(getBMIStatus()) {
            case "Underweight":
                plan = "You should gain weight to " + String.format("%.0f", getSuggestedWeight(18.5f)) + " kg.";
                break;
            case "Healthy Weight":
                plan = "You are fit now. Keep it on!";
                break;
            case "Overweight":
                plan = "You should lose weight to " + (int)getSuggestedWeight(21) + " kg.";
                break;
            case "Obese":
                plan = "You should lose weight to " + (int)getSuggestedWeight(21) + " kg.";
                break;
        }
        return plan;
    }

    public int getTargetStep(){
        float targetCalBurn = (weight - (int)getSuggestedWeight(21)) * calPerKg;
        targetStep = (int)(targetCalBurn/stepCal);
        return targetStep;
    }

    private String getDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
