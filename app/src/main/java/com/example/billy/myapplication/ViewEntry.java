package com.example.billy.myapplication;

import android.provider.BaseColumns;

public abstract class ViewEntry implements BaseColumns {

    public static final String TABLE_NAME="Persona lData";
    public static final String COL_NAME_VIEW_NAME = "name";
    public static final String COL_NAME_VIEW_AGE = "age";
    public static final String COL_NAME_VIEW_GENDER = "gender";
    public static final String COL_NAME_VIEW_HEIGHT = "height";
    public static final String COL_NAME_VIEW_WEIGHT = "weight";

    private ViewEntry(){}
}