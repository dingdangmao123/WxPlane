package com.example.administrator.wxplane;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.administrator.wxplane.Model.Bullet;
import com.example.administrator.wxplane.Model.OtherP;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class MySurface extends SurfaceView implements SurfaceHolder.Callback,Runnable {

   final  int BULLET_FIRE=1000;



    boolean stop=false;
    SurfaceHolder holder;


    Paint p;
    Paint scorep;
    Bitmap bmyp;
    Bitmap bbullet;
    Bitmap botherp;
    Bitmap[] otherpbit;
    int[] fire;
    int score=0;


    Random r=new Random();


    LinkedList<Bullet> bullet=new LinkedList<>();
    LinkedList<OtherP> otherp=new LinkedList<>();

    LinkedList<Bullet> bullet_cache=new LinkedList<>();
    LinkedList<OtherP> otherp_cache=new LinkedList<>();

    float x=0;
    float y=0;

    float w;
    float h;


    boolean touch=false;

    public MySurface(Context context) {
        this(context,null);
    }

    public MySurface(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MySurface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    public void init(Context context, AttributeSet attrs){

        holder=getHolder();
        holder.addCallback(this);
        p=new Paint();
        p.setStrokeWidth(1);

        scorep=new Paint();
        scorep.setColor(Color.parseColor("#ff6473"));
        scorep.setTextSize(50);
        scorep.setStrokeWidth(10);
        bmyp= BitmapFactory.decodeResource(getResources(), R.drawable.plane);
        bbullet= BitmapFactory.decodeResource(getResources(), R.drawable.bullet);
        botherp= BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
        otherpbit=new Bitmap[]{botherp};
        fire=new int[]{2000};

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        stop=false;
        y=getHeight()-bmyp.getHeight();
        x=getWidth()/2;
        w=bmyp.getWidth();
        h=bmyp.getHeight();

        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop=true;
        Log.i("Unit","destroy");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x=event.getX();
        float y=event.getY();
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(x>=this.x&&x<=this.x+w&&y>=this.y&&y<=this.y+h)
                touch = true;
        }else if(event.getAction()==MotionEvent.ACTION_MOVE){
            if(touch){
                this.x=x;
                this.y=y;
            }
        }else
            touch=false;
        return true;
    }

    @Override
    public void run() {

        while(!stop){
            Canvas canvas=null;
            try{
                canvas=holder.lockCanvas(null);
                if(canvas==null)
                    break;

                clear(canvas);
                drawBullet(canvas);
                drawPlane(canvas);
                check(canvas);

            }catch(Exception e){
                Log.i("Unit", e.toString());

            }finally {
                if(canvas!=null)
                    holder.unlockCanvasAndPost(canvas);
            }

        }

    }
    private void clear(Canvas canvas){
        canvas.drawColor(Color.parseColor("#FFFFFF"));
        canvas.drawBitmap(bmyp,x,y,p);
        canvas.drawText(String.valueOf(score),20,60,scorep);
    }
    private void check(Canvas canvas){

        for(Bullet btmp:bullet){
            for(OtherP otmp:otherp){
                float l=otmp.getX();
                float t=otmp.getY();
                float r=otmp.getX()+otherpbit[otmp.getType()].getWidth();
                float b=otmp.getY()+otherpbit[otmp.getType()].getHeight();

                float bx=btmp.getX();
                float by=btmp.getY();

                if(bx>=l&&bx<=r&&by>=t&&by<=b) {
                    if(otmp.getCurrent()-BULLET_FIRE<=0) {
                        score += BULLET_FIRE;
                        otmp.setCurrent(0);
                    }else{
                        otmp.setCurrent(otmp.getCurrent()-BULLET_FIRE);
                    }
                }
            }
        }
    }
    private void drawPlane(Canvas canvas){
            if(otherp.size()<6) {

                int num = 1;
                int i = 1 + r.nextInt(num);
                for (int j = 0; j < i; j++) {
                    OtherP cache;
                    if (otherp_cache.size() > 0) {
                        cache = otherp_cache.remove(0);
                        cache.init(r.nextInt((int) getWidth() - otherpbit[0].getWidth()), r.nextInt(50), 5 + r.nextInt(10), fire[0], 0);
                    } else
                        cache = new OtherP(r.nextInt((int) getWidth() - otherpbit[0].getWidth()), r.nextInt(50), 5 + r.nextInt(10), fire[0], 0);

                    otherp.add(cache);

                }
            }
        int l=0;
        OtherP tmp;
        int h=getHeight();
        while(otherp.size()>0){
            tmp=otherp.get(0);
            if(tmp.getY()>=h)
                otherp_cache.add(otherp.remove(0));
            else
                break;
        }
        l=otherp.size();
        for(int i=0;i<l;i++){
            tmp=otherp.get(i);
            if(tmp.getCurrent()<=0) {
                otherp_cache.add(otherp.remove(i));
                i--;
                l--;
                continue;
            }
            canvas.drawBitmap(otherpbit[tmp.getType()],tmp.getX(),tmp.getY(),p);
            tmp.setY(tmp.getY()+tmp.getSpeed());
        }


    }
    private void drawBullet(Canvas canvas){
        if(bullet.size()==0){
            bullet.add(new Bullet(x +w / 2, y + 10));
            return ;
        }

        if(bullet.size()>0) {

                Bullet cache;
                cache=bullet.getLast();
                if(y>cache.getY()+150){
                    if(bullet_cache.size()>0)
                    {
                        cache=bullet_cache.remove(0);
                        cache.init(x +w / 2, y + 10);

                    }else
                        cache=new Bullet(x +w / 2, y + 10);

                    bullet.add(cache);
                }
        }
        int l=0;
        Bullet tmp;
        while(bullet.size()>0){
            tmp=bullet.get(0);
            if(tmp.getY()<=0)
                bullet_cache.add(bullet.remove(0));
            else
                break;
        }

        l=bullet.size();
        for(int i=0;i<l;i++){
            tmp=bullet.get(i);
            canvas.drawBitmap(bbullet,tmp.getX(),tmp.getY(),p);
            //tmp.setY(tmp.getY()-100);
            tmp.y-=15;
        }

      //  Log.i("Unit",String.valueOf(bullet.size()));
    }
}
