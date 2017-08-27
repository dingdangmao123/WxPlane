package com.example.administrator.wxplane;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.administrator.wxplane.Adapter.scoreAdapter;
import com.example.administrator.wxplane.SQL.Helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Score extends Base {

    RecyclerView  list;
    Button delete;
    Button left;
    ProgressBar bar;
    boolean sort=false;
    scoreAdapter adapter;

    Helper helper;

    List<String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);

        helper = new Helper(this);
        data=new LinkedList<>();

        list=(RecyclerView)findViewById(R.id.list);
        left=(Button)findViewById(R.id.left);
        delete=(Button)findViewById(R.id.delete);
        bar=(ProgressBar)findViewById(R.id.bar);

        adapter=new scoreAdapter(data.toArray(new String[data.size()]));
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sort){
                    ((Button)v).setText("时间排序");
                    getScore(false);
                }else{
                    ((Button)v).setText("分数排序");
                    getScore(true);
                }
               update();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    delete();
            }
        });
        getScore(false);
        update();
    }
    private  void getScore(boolean value){

        data.clear();
        SQLiteDatabase  read= helper.getReadableDatabase();
        String sql;
        if(value)
            sql = "select * from score order by value DESC limit 0,30";
        else
            sql = "select * from score order by id DESC limit 0,30";

        Cursor cursor = read.rawQuery(sql, null);
        cursor.moveToFirst();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        do{
            long unix=cursor.getInt(cursor.getColumnIndex("unix"))*1000l;
            int v=cursor.getInt(cursor.getColumnIndex("value"));
            data.add(df.format(new Date(unix))+" - "+v);
        }while(cursor.moveToNext());

        cursor.close();
        read.close();
    }
    private void delete(){
        SQLiteDatabase  write= helper.getWritableDatabase();
        write.execSQL("delete from score");
        write.close();
        adapter.setData(new String[0]);
        adapter.notifyDataSetChanged();
    }
    private void update(){
        bar.setVisibility(View.VISIBLE);
        sort=!sort;
        String[] a=null;
        adapter.setData(data.toArray(new String[data.size()]));
        adapter.notifyDataSetChanged();
        bar.setVisibility(View.GONE);
    }
}
