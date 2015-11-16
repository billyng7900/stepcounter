package com.example.billy.myapplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Billy on 14/11/2015.
 */
public class Step {
    private int step;
    private String date;
    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getConvertToDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();
        try{
            convertedDate = dateFormat.parse(date);
            return convertedDate;
        }catch(Exception e)
        {
            return null;
        }
    }
}
