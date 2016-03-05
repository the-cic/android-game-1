package com.example.mirko.myapplication;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by mirko on 03/03/2016.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    private MainThread mainThread;
    private Background background;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

//        mainThread = new MainThread(getHolder(), this);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("Surface Created");

        if (background == null) {
            background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.bg));
            background.setVector(-5);
        }

        mainThread = new MainThread(getHolder(), this);
        mainThread.setRunning(true);
        mainThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("Surface Changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("Surface Destroyed");
        boolean retry = true;
        while (retry) {
            try {
                System.out.println("set running false");
                mainThread.setRunning(false);
                System.out.println("join");
                mainThread.join();
                retry = false;
                System.out.println("running false and thread joined");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mainThread = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        System.out.println("On touch event");
        if (mainThread != null) {
            mainThread.FPS = mainThread.FPS == 30 ? 60 : 30;
        }
        return super.onTouchEvent(event);
    }

    public void update() {
        background.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        final float scaleFactorX = (float)getWidth() / WIDTH;
        final float scaleFactorY = (float)getHeight() / HEIGHT;
//        System.out.println(getWidth()+"x"+getHeight()+" c=null:"+(canvas == null));

        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            background.draw(canvas);
            canvas.restoreToCount(savedState);
        }
    }

}
