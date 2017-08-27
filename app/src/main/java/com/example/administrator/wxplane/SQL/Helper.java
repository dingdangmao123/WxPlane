package com.example.administrator.wxplane.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/8/27 0027.
 */

public class Helper extends SQLiteOpenHelper {

    private static final String name = "WxPlane";
    private static final int version = 1;
    public Helper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table score(id integer PRIMARY KEY autoincrement,unix integer,value integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}