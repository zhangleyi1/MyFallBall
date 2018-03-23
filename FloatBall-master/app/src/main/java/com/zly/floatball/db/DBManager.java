package com.zly.floatball.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Administrator on 2018/3/23.
 */
public class DBManager {
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase db;
    private String TAG = "DBManager";

    public DBManager(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
        db = mDatabaseHelper.getWritableDatabase();
    }

    public void insert(InitData data) {
        Log.d(TAG, "zly --> insert");
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("id", 1);
            values.put("service_state", data.isServiceRunning);
            values.put("alpha", data.alphaValue);
            values.put("time", data.time);
            values.put("packageOne", data.packageOne);
            values.put("packageOne", data.packageTwo);
            values.put("packageOne", data.packageThree);
            values.put("packageOne", data.packageFour);
            values.put("packageOne", data.packageFive);
            db.insert(DatabaseHelper.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void update(InitData data) {
        Log.d(TAG, "zly --> update");
        ContentValues values = new ContentValues();
        values.put("service_state", data.isServiceRunning);
        values.put("alpha", data.alphaValue);
        values.put("time", data.time);
        values.put("packageOne", data.packageOne);
        values.put("packageTwo", data.packageTwo);
        values.put("packageThree", data.packageThree);
        values.put("packageFour", data.packageFour);
        values.put("packageFive", data.packageFive);

        db.update(DatabaseHelper.TABLE_NAME, values, null, null);
    }

    public InitData query() {
        Log.d(TAG, "zly --> query");
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);
        if (null != c) {
            if (c.moveToFirst()) {
                InitData data = new InitData();
                data.isServiceRunning = c.getInt(c.getColumnIndex("service_state")) == 1;
                data.alphaValue = c.getInt(c.getColumnIndex("alpha"));
                data.time = c.getInt(c.getColumnIndex("time"));
                data.packageOne = c.getString(c.getColumnIndex("packageOne"));
                data.packageTwo = c.getString(c.getColumnIndex("packageTwo"));
                data.packageThree = c.getString(c.getColumnIndex("packageThree"));
                data.packageFour = c.getString(c.getColumnIndex("packageFour"));
                data.packageFive = c.getString(c.getColumnIndex("packageFive"));
                c.close();
                return data;
            }
        }
        return null;
    }
}
