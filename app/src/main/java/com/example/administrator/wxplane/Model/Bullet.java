package com.example.administrator.wxplane.Model;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class Bullet {
    private float x;
    public float y;

    public Bullet(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void init(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
