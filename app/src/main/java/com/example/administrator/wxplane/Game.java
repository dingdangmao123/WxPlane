package com.example.administrator.wxplane;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import com.example.administrator.wxplane.SQL.Helper;


public class Game extends Base {


    Helper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        helper = new Helper(this);
    }

    public void save(int score){
        SQLiteDatabase write=helper.getWritableDatabase();
        int time=(int)(System.currentTimeMillis()/1000);
        write.execSQL("insert into score(unix,value) values(?,?)", new Object[]{time, score});
        write.close();
        Log.i("Unit",String.valueOf(time));

    }
}
