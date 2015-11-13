package com.example.billy.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by WaiHing on 13/11/2015.
 */
public class FitnessDbHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Fitness_plan.db";
    private static final String FLOAT_TYPE = " FLOAT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FitnessEntry.TABLE_NAME+ " (" +
                    FitnessEntry._ID + " INTEGER PRIMARY KEY," +
                    FitnessEntry.COLUMN_NAME_Height + FLOAT_TYPE + COMMA_SEP +
                    FitnessEntry.COLUMN_NAME_Weight + FLOAT_TYPE+");";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FitnessEntry.TABLE_NAME;

    public FitnessDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
