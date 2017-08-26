package com.example.administrator.wxplane.Model;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class Zone {
    private float l;
    private float t;
    private float r;
    private float b;

    public Zone(float l, float t, float r, float b) {
        this.l = l;
        this.t = t;
        this.r = r;
        this.b = b;
    }
    public void init(float l, float t, float r, float b) {
        this.l = l;
        this.t = t;
        this.r = r;
        this.b = b;
    }
    public float getL() {
        return l;
    }

    public void setL(float l) {
        this.l = l;
    }

    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.t = t;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }
}
