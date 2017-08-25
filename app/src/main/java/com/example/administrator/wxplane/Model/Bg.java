package com.example.administrator.wxplane.Model;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class Bg {
    private float x;
    private float y;
    private float radius;
    public Bg(float x, float y,float radius) {
        this.x = x;
        this.y = y;
        this.radius=radius;
    }

    public void init(float x, float y,float radius) {
        this.x = x;
        this.y = y;
        this.radius=radius;
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

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
