package com.example.billy.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Billy on 13/11/2015.
 */
public class StepDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Step_counter.db";
    private static final String INT_TYPE = " INTEGER";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + StepEntry.TABLE_NAME+ " (" +
                    StepEntry.COLUMN_NAME_Date + TEXT_TYPE + "PRIMARY KEY"+ COMMA_SEP +
                    StepEntry.COLUMN_NAME_Step+ INT_TYPE+");";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + StepEntry.TABLE_NAME;

    public StepDbHelper(Context context)
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

    public int getDbStep(String[] args,Context context)
    {

        StepDbHelper dbHelper = new StepDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {StepEntry.COLUMN_NAME_Step};
        Cursor c = db.query(StepEntry.TABLE_NAME,projection,StepEntry.COLUMN_NAME_Date+"=?",args,null,null,null,null);
        if(c.moveToFirst())
        {
            return c.getInt(0);
        }
        else
        {
            return -1;
        }
    }

    public void insertStep(ContentValues values,Context context)
    {
        StepDbHelper dbHelper = new StepDbHelper(context);
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        dbWrite.beginTransaction();
        try {
            dbWrite.insert(StepEntry.TABLE_NAME, null, values);
            dbWrite.setTransactionSuccessful();
        }catch (Exception e)
        {
            Toast.makeText(context,"Database store error, please contact the system admin",Toast.LENGTH_SHORT);
            return;
        }finally {
            dbWrite.endTransaction();
        }
    }

    public void updateStep(ContentValues values,String[] args,Context context)
    {
        StepDbHelper dbHelper = new StepDbHelper(context);
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        dbWrite.beginTransaction();
        try {
            dbWrite.update(StepEntry.TABLE_NAME, values, StepEntry.COLUMN_NAME_Date + "=?", args);
            dbWrite.setTransactionSuccessful();

        }catch(Exception e)
        {
            Toast.makeText(context,"Database store error, please contact the system admin",Toast.LENGTH_SHORT);
            return;
        }
        finally {
            dbWrite.endTransaction();
        }
    }

    public ArrayList<Step> getAllStepRecord(Context context)
    {
        ArrayList<Step> stepList = new ArrayList<>();
        StepDbHelper dbHelper = new StepDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {StepEntry.COLUMN_NAME_Date,StepEntry.COLUMN_NAME_Step};
        Cursor c = db.query(StepEntry.TABLE_NAME,projection,null,null,null,null,null);
        if(c.moveToFirst())
        {
            do {
                Step step = new Step();
                step.setDate(c.getString(0));
                step.setStep(c.getInt(1));
                stepList.add(step);
            }while (c.moveToNext());
        }
        return stepList;
    }
}
