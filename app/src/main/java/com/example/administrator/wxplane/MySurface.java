package com.example.administrator.wxplane;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.administrator.wxplane.Model.Bg;
import com.example.administrator.wxplane.Model.Bullet;
import com.example.administrator.wxplane.Model.OtherP;
import com.example.administrator.wxplane.Model.Zone;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class MySurface extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    final  int BULLET_FIRE=1000;


    boolean stop=false;
    boolean start=false;

    SurfaceHolder holder;

    Paint p;
    Paint scorep;
    Paint bgp;
    Bitmap bmyp;
    Bitmap bbullet;
    Bitmap botherp;
    Bitmap[] otherpbit;
    Bitmap[] bomb;

    Bitmap go;
    Bitmap back;

    int[] fire;
    Zone[] menu;
    int score=0;


    Random r=new Random();


    LinkedList<Bullet> bullet=new LinkedList<>();
    LinkedList<Bullet> bullet_cache=new LinkedList<>();

    LinkedList<OtherP> otherp=new LinkedList<>();
    LinkedList<OtherP> otherp_cache=new LinkedList<>();

    LinkedList<Bg> bg=new LinkedList<>();
    LinkedList<Bg> bg_cache=new LinkedList<>();

    float x=0;
    float y=0;
    float lastx=0;
    float lasty=0;


    float w;
    float h;


    SoundPool sp;
    int shootID;
    int bombID;
    int overID;


    long boottime=System.currentTimeMillis();

    boolean touch=false;

    public MySurface(Context context) {
        this(context,null);
    }

    public MySurface(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MySurface(Context context, AttributeSet attrs, int defStyleAttr){

        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }
    public void init(Context context, AttributeSet attrs){

        holder=getHolder();
        holder.addCallback(this);
        p=new Paint();
        p.setStrokeWidth(1);

        bgp=new Paint();
        bgp.setAntiAlias(true);
        bgp.setStyle(Paint.Style.STROKE);
        bgp.setStrokeWidth(1);

        scorep=new Paint();
        scorep.setColor(Color.parseColor("#996633"));
        scorep.setTextSize(50);
        scorep.setStrokeWidth(10);
        bmyp= BitmapFactory.decodeResource(getResources(), R.drawable.plane);
        bbullet= BitmapFactory.decodeResource(getResources(), R.drawable.bullet);
        botherp= BitmapFactory.decodeResource(getResources(), R.drawable.small);
        Bitmap tmp= BitmapFactory.decodeResource(getResources(), R.drawable.middle);
        otherpbit=new Bitmap[]{botherp,tmp};

        Bitmap tmp1=BitmapFactory.decodeResource(getResources(), R.drawable.bomb1);
        Bitmap tmp2=BitmapFactory.decodeResource(getResources(), R.drawable.bomb2);

        bomb=new Bitmap[]{tmp1,tmp2};
        fire=new int[]{2000,5000};
        SoundPool.Builder sb= new SoundPool.Builder();
        sb.setMaxStreams(2);
        sp=sb.build();
        shootID=sp.load(getContext(),R.raw.shoot,1);
        bombID=sp.load(getContext(),R.raw.explosion,1);
        overID=sp.load(getContext(),R.raw.over,1);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        stop=false;
        y=getHeight()-bmyp.getHeight();
        x=getWidth()/2;
        w=bmyp.getWidth();
        h=bmyp.getHeight();

        flush();

        new Thread(this).start();
    }
    private void flush(){
        bullet.clear();
        otherp.clear();
        bullet_cache.clear();
        otherp_cache.clear();
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop=true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x=event.getX();
        float y=event.getY();
        if(start){
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                int i=0;
                for(i=0;i<menu.length;i++){
                    if(x>=menu[i].getL()&&x<=menu[i].getR()&&y>=menu[i].getT()&&y<=menu[i].getB()){
                        break;
                    }
                }
                switch(i){
                    case 0:
                        start=false;
                        score=0;
                        break;
                    case 1:
                        if(getContext() instanceof Game){
                            ((Activity)getContext()).finish();
                        }
                        break;
                    default:
                        break;
                }
            }
            return true;
        }

        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(x>=this.x&&x<=this.x+w&&y>=this.y&&y<=this.y+h) {
                lastx=x;
                lasty=y;
                touch = true;

            }
        }else if(event.getAction()==MotionEvent.ACTION_MOVE){
            if(touch){
                float detx=x-lastx;
                float dety=y-lasty;

                this.x+=detx;
                this.y+=dety;

                lastx=x;
                lasty=y;
            }
        }else
            touch=false;
        return true;
    }
    @Override
    public void run() {
        while(System.currentTimeMillis()-boottime<5000){
            Canvas canvas=null;
            try{
               canvas=holder.lockCanvas(null);
                if(canvas==null)
                    break;
                drawBackround(canvas);
            }catch(Exception e){
                Log.i("Unit", e.toString());
            }finally {
                if(canvas!=null)
                    holder.unlockCanvasAndPost(canvas);
            }
        }
        while(!stop){
            Canvas canvas=null;
            try{
                canvas=holder.lockCanvas(null);
                if(canvas==null)
                    break;
                drawBackround(canvas);
                if(start) {
                    drawStart(canvas);
                }else{
                    drawMyp(canvas);
                    drawBullet(canvas);
                    drawPlane(canvas);
                    check(canvas);
                }
            }catch(Exception e){
                Log.i("Unit", e.toString());
            }finally {
                if(canvas!=null)
                    holder.unlockCanvasAndPost(canvas);
            }
        }
    }
    public void drawStart(Canvas canvas){

        int w=getWidth();
        int h=getHeight();

        if(go==null)
            go=BitmapFactory.decodeResource(getResources(), R.drawable.go);
        if(back==null)
            back=BitmapFactory.decodeResource(getResources(), R.drawable.back);

        if(menu==null)
            menu=new Zone[2];

        canvas.drawBitmap(go,w/2-go.getWidth()/2,300,p);
        if(menu[0]==null)
            menu[0]=new Zone(w/2-go.getWidth()/2,300,w/2+go.getWidth()/2,300+go.getHeight());
        else
            menu[0].init(w/2-go.getWidth()/2,300,w/2+go.getWidth()/2,300+go.getHeight());

        int bottom=300+go.getHeight();
        canvas.drawBitmap(back,w/2-back.getWidth()/2,bottom+30,p);
        if(menu[1]==null)
            menu[1]=new Zone(w/2-go.getWidth()/2,bottom+20,w/2+back.getWidth()/2,bottom+30+back.getHeight());
        else
            menu[1].init(w/2-go.getWidth()/2,300+go.getHeight()+20,w/2+back.getWidth()/2,bottom+30+back.getHeight());


    }
    public void drawBackround(Canvas canvas){

        canvas.drawColor(Color.parseColor("#cccccc"));
        canvas.drawText(String.valueOf(score),20,60,scorep);
        Bg cache;
        if(bg.size()==0){
            cache = new Bg(r.nextInt(getWidth()), 0, 20+r.nextInt(100));
            bg.add(cache);
        }else{
            int num =1;
            cache=bg.getLast();
            if(cache.getY()>150) {
                for (int i = 0; i < num; i++) {
                    if (bg_cache.size() > 0) {
                        cache = bg_cache.remove(0);
                        cache.init(r.nextInt(getWidth()), 0, 20+r.nextInt(100));
                    } else {
                        cache = new Bg(r.nextInt(getWidth()), 0, 20+r.nextInt(100));
                   }
                    bg.add(cache);
                }
            }
        }
        int l=0;
        Bg tmp;
        int h=getHeight();
        while(bg.size()>0){
            tmp=bg.get(0);
            if(tmp.getY()-tmp.getRadius()>=h) {
                bg_cache.add(bg.remove(0));
            }
            else
                break;
        }
        l=bg.size();
        for(int i=0;i<l;i++){
            tmp=bg.get(i);
            canvas.drawCircle(tmp.getX(),tmp.getY(),tmp.getRadius(),bgp);
            tmp.setY(tmp.getY()+3);
        }
    }
    public void drawMyp(Canvas canvas){
        canvas.drawBitmap(bmyp,x,y,p);
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
                        score += otmp.getFire();
                        otmp.setCurrent(0);
                    }else{
                        otmp.setCurrent(otmp.getCurrent()-BULLET_FIRE);
                    }
                    sp.play(bombID,1,1,1,0,1);
                }
            }
        }
        float l=x;
        float r=x+bmyp.getWidth();
        float t=y;
        float b=y+bmyp.getHeight();
        for(OtherP op:otherp){
            float x1=op.getX();
            float y1=op.getY()+otherpbit[op.getType()].getHeight();
            float x2=x1+otherpbit[op.getType()].getWidth();
            float y2=y1;
            boolean flag=false;
            if(x1>=l+20&&x1<=r-20&&y1>=t+20&&y1<=b-20)
                flag=true;
            if(x2>=l+20&&x2<=r-20&&y2>=t+20&&y2<=b-20)
                flag=true;
            if(flag){
                sp.play(overID,1,1,1,0,1);
                sp.pause(shootID);
                start=true;
                if(getContext() instanceof Game){
                    ((Game)getContext()).save(score);
                }
                flush();
            }
        }
    }
    private void drawPlane(Canvas canvas){
            if(otherp.size()<2) {
                int num = 1;
                int i = 1 + r.nextInt(num);
                for (int j = 0; j < i; j++) {
                    OtherP cache;
                    int rr=r.nextInt(2);
                    if (otherp_cache.size() > 0) {
                        cache = otherp_cache.remove(0);
                        cache.init(r.nextInt((int) getWidth() - otherpbit[0].getWidth()), r.nextInt(50), 5 + r.nextInt(5), fire[rr], rr);
                    } else
                        cache = new OtherP(r.nextInt((int) getWidth() - otherpbit[0].getWidth()), r.nextInt(50), 5 + r.nextInt(5), fire[rr], rr);

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
            bullet.add(new Bullet(x +w / 2-bbullet.getWidth()/2, y - 10));
            sp.play(shootID,1,1,1,0,1);
            return ;
        }
        if(bullet.size()>0) {
                Bullet cache;
                cache=bullet.getLast();
                if(y>cache.getY()+150){
                    if(bullet_cache.size()>0)
                    {
                        cache=bullet_cache.remove(0);
                        cache.init(x +w / 2-bbullet.getWidth()/2, y - 10);

                    }else
                        cache=new Bullet(x +w / 2-bbullet.getWidth()/2, y - 10);

                    bullet.add(cache);
                    sp.play(shootID,1,1,1,0,1);
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
            tmp.y-=15;
        }
    }
}
