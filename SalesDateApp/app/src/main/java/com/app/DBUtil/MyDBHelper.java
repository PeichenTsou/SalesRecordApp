package com.app.DBUtil;


import android.content.Context;

public class MyDBHelper extends DataBaseHelper {

    private static MyDBHelper mDBHelper;

    private MyDBHelper(Context context) {
        super(context);
    }

    public static MyDBHelper getInstance(Context context) {
        if (mDBHelper == null) {
            synchronized (DataBaseHelper.class) {
                if (mDBHelper == null) {
                    mDBHelper = new MyDBHelper(context);
                    if (mDBHelper.getDB() == null || !mDBHelper.getDB().isOpen()) {
                        mDBHelper.open();
                    }
                }
            }
        }
        return mDBHelper;
    }

    @Override
    protected int getMDbVersion(Context context) {
        return 1;
    }

    @Override
    protected String getDbName(Context context) {
        return "food.db";
    }

    @Override
    protected String[] getDbCreateSql(Context context) {
        String[] a = new String[3];
        a[0] = "CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(50), password VARCHAR(50))";
        a[1] = "CREATE TABLE food (id INTEGER PRIMARY KEY AUTOINCREMENT,userid VARCHAR(50),dbfoodname VARCHAR(50), classification VARCHAR(50), category VARCHAR(50), imagepath VARCHAR(200))";
        //add period & date
        a[2] = "CREATE TABLE count (id INTEGER PRIMARY KEY AUTOINCREMENT, foodid VARCHAR(50), count INTEGER, period VARCHAR(50), date VARCHAR(50))";
        return a;
    }

    @Override
    protected String[] getDbUpdateSql(Context context) {
        return new String[0];
    }
}