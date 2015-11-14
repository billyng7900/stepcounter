package com.example.billy.myapplication;

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
    private float height;
    private float weight;
    private float BMI_value;
    private String BMI_status;

    public void getDbData(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection_h = {FitnessEntry.COLUMN_NAME_Height};
        String[] projection_w = {FitnessEntry.COLUMN_NAME_Weight};
        Cursor c = db.query(FitnessEntry.TABLE_NAME, projection_h, null, null, null, null, null, null);
        if(c.moveToFirst()){
            height = c.getFloat(0);
            weight = c.getFloat(1);
        }else{
            height = 0;
            weight = 0;
        }
    }

    public float getBMI(){
//test value//
        height = 175;
        weight = 52;
//test value//
        //assume height in cm and weight in kg
        if (height > 100) {
            height = (float)height/100;
        }
        BMI_value = (weight/height)/height;
        return BMI_value;
    }

    public String getBMIStatus(){
        BMI_status = "";
        //getDbData();
        getBMI();

        if(BMI_value < 18.5){
            BMI_status = "Underweight";
        }else if(BMI_value >= 18.5 && BMI_value < 25){
            BMI_status = "Healthy Weight";
        }else if(BMI_value >=25 && BMI_value < 30){
            BMI_status = "Overweight";
        }else if(BMI_value >= 30){
            BMI_status = "Obese";
        }
        return BMI_status;
    }
}
