package com.example.billy.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ViewDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DATABASE_NAME = "UserData..db";
    private static final String INT_TYPE = " INTEGER";
    private static final String TEXT_TYPE = " TEXT";
    private static final String FLOAT_TYPE = " FLOAT";
    private static final String SQL_CREATE_ENTRES =
            "CREATE TABLE " + ViewEntry.TABLE_NAME + " (" +
                    ViewEntry.COL_NAME_VIEW_NAME + TEXT_TYPE +
                    ViewEntry.COL_NAME_VIEW_AGE + INT_TYPE +
                    ViewEntry.COL_NAME_VIEW_GENDER + TEXT_TYPE +
                    ViewEntry.COL_NAME_VIEW_HEIGHT + FLOAT_TYPE +
                    ViewEntry.COL_NAME_VIEW_WEIGHT + FLOAT_TYPE + ");";

    private static final String SQL_DELETE_ENTRIES=
            "DROP TABLE IF EXISTS "+ViewEntry.TABLE_NAME;

    public ViewDBHelper(Context context){
        super(context,DATABASE_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRES);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public String getDBName(Context context) {

        ViewDBHelper dbHelper = new ViewDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection1 = {ViewEntry.COL_NAME_VIEW_NAME};
        Cursor c = db.query(ViewEntry.TABLE_NAME, projection1,
                null, null, null, null, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        } else {
            return null;
        }
    }

    public int getDBAge(Context context) {

        ViewDBHelper dbHelper = new ViewDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {ViewEntry.COL_NAME_VIEW_AGE};
        Cursor c = db.query(ViewEntry.TABLE_NAME, projection,
                null, null, null, null, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        } else {
            return -1;
        }
    }

    public String getDBGender(Context context) {

        ViewDBHelper dbHelper = new ViewDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {ViewEntry.COL_NAME_VIEW_GENDER};
        Cursor c = db.query(ViewEntry.TABLE_NAME, projection,
            null, null, null, null, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        } else {
            return null;
        }
    }

    public double getDBHeight(Context context){

        ViewDBHelper dbHelper = new ViewDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {ViewEntry.COL_NAME_VIEW_HEIGHT};
        Cursor c = db.query(ViewEntry.TABLE_NAME, projection,
            null, null, null, null, null);
        if (c.moveToFirst()) {
            return c.getDouble(0);
        } else {
            return -1;
        }
    }

    public double getDBWeight(Context context) {

        ViewDBHelper dbHelper = new ViewDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {ViewEntry.COL_NAME_VIEW_WEIGHT};
        Cursor c = db.query(ViewEntry.TABLE_NAME, projection,
                null, null, null, null, null);
        if (c.moveToFirst()) {
            return c.getDouble(0);
        } else {
            return -1;
        }
    }
}
