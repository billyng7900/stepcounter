package com.example.billy.myapplication;

import android.provider.BaseColumns;

/**
 * Created by WaiHing on 14/11/2015.
 */
public class FitnessEntry implements BaseColumns{
    public static final String TABLE_NAME = "Fitness_plan";
    public static final String COLUMN_NAME_Height = "height";
    public static final String COLUMN_NAME_Weight = "weight";
    public static final String COLUMN_NAME_Target = "target";
    public static final String COLUMN_NAME_Date = "start_date";
    private FitnessEntry(){};
}
