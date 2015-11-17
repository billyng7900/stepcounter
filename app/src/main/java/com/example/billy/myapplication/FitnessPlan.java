package com.example.billy.myapplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by WaiHing on 14/11/2015.
 */
public class FitnessPlan {
    final private float stepCal = 0.044f;
    final private int calPerKg = 7700;
    private float height;
    private float weight;
    private float BMI_value;
    private int targetStep;
    private String startDate;
    private int accumulatedStep;

    public FitnessPlan(Float h, Float w) {
        height = h;
        weight = w;
        BMI_value = 0.00f;
        targetStep = 0;
        getBMI();
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
        String BMI_status = "";
        getBMI();

        if(getBMI() < 18.5){
            BMI_status = "Underweight";
        }
        if(getBMI() >= 18.5 && BMI_value < 25){
            BMI_status = "Healthy Weight";
        }
        if(getBMI() >=25 && BMI_value < 30){
            BMI_status = "Overweight";
        }
        if(getBMI() >= 30){
            BMI_status = "Obese";
        }
        return BMI_status;
    }

    public float getSuggestedWeight(float bmi){
        Float sWeight;
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
        if(getBMI()>=25) {
            float targetCalBurn = (weight - (int) getSuggestedWeight(21)) * calPerKg;
            targetStep = (int) (targetCalBurn / stepCal);
        }
        return targetStep;
    }

    public int getHealthyStyle(){
        if(getBMI()>=25){
            return 10000;
        }else{
            return 6000;
        }
    }

    public void setStartDate(String s){
        startDate = s;
    }

    public String getStartDate(){
        return startDate;
    }

    public void setAccumulatedStep(int i){
        accumulatedStep = i;
    }

    public int getAccumulatedStep(){
        return accumulatedStep;
    }

    public String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
