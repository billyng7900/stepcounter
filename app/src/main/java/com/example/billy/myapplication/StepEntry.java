package com.example.billy.myapplication;

import android.provider.BaseColumns;

/**
 * Created by Billy on 13/11/2015.
 */
public class StepEntry implements BaseColumns {
    public static final String TABLE_NAME="step_counter";
    public static final String COLUMN_NAME_Step="step";
    public static final String COLUMN_NAME_Date="date";
    private StepEntry(){};
}
