package com.example.billy.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
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

    public FitnessPlan(Float h, Float w) {
        height = h;
        weight = w;
        BMI_value = 0.00f;
        targetStep = 0;
    }

    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public float getBMI(){
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
        if(BMI_value>=25) {
            float targetCalBurn = (weight - (int) getSuggestedWeight(21)) * calPerKg;
            targetStep = (int) (targetCalBurn / stepCal);
        }
        return targetStep;
    }

    public int getHealthyStyle(){
        if(BMI_value>=25){
            return 10000;
        }else {
            return 6000;
        }
    }

    public String getDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
