package com.example.administrator.wxplane.Model;

//import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/8/26 0026.
 */

public class Result   {
    long time;
    int score;

    public Result(long time, int score) {
        this.time = time;
        this.score = score;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
