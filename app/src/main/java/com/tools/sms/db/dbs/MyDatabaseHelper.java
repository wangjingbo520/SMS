package com.tools.sms.db.dbs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tools.sms.base.Constants;

/**
 * @author wjb
 * describe
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public MyDatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_1 = "create table " + Constants.TABBLE_NAME_TEMPLATE + "(" + Constants.ID + " integer primary key autoincrement," + Constants.CONTENT + "," + Constants.TIME + ")";
        String sql_2 = "create table " + Constants.TABBLE_RESULT_SEND_ +
                "(" + Constants.ID + " integer primary key autoincrement," + Constants.CONTENT + "," + Constants.TIME + "," + Constants.TAG + "," + Constants.PHOTO_NUMBER + ")";
        db.execSQL(sql_1);
        db.execSQL(sql_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}