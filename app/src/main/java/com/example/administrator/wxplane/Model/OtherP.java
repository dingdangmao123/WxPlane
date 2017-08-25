package com.example.administrator.wxplane.Model;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class OtherP {
    private float x;
    private float y;
    private float speed;
    private int fire;
    private int type;
    private int current;

    public OtherP(float x, float y, float speed, int fire, int type) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.fire = fire;
        this.type = type;
        this.current=fire;
    }

    public void init(float x, float y, float speed, int fire, int type) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.fire = fire;
        this.type = type;
        this.current=fire;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSpeed() {
        return speed;
    }

    public int getFire() {
        return fire;
    }

    public int getType() {
        return type;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setFire(int fire) {
        this.fire = fire;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
