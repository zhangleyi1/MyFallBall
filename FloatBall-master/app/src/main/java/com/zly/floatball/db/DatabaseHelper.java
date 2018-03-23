package com.zly.floatball.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2018/3/23.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "floatball_database.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "floatball_tab";
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "zly --> onCreate");
        StringBuffer buffer = new StringBuffer();
        buffer.append("CREATE TABLE [" + TABLE_NAME + "] (");
        buffer.append("[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
        buffer.append("[service_state] INTEGER,");
        buffer.append("[alpha] INTEGER,");
        buffer.append("[time] TEXT,");
        buffer.append("[packageOne] TEXT,");
        buffer.append("[packageTwo] TEXT,");
        buffer.append("[packageThree] TEXT,");
        buffer.append("[packageFour] TEXT,");
        buffer.append("[packageFive] TEXT)");
        db.execSQL(buffer.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "zly --> onUpgrade oldVersion:" + oldVersion + " newVersion:" + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
