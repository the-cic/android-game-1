package com.example.mirko.myapplication;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by mirko on 03/03/2016.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    private MainThread mainThread;
    private Background background;
    private ArrayList<MotionEvent> eventQueue;

    public GamePanel(Context context) {
        super(context);

        System.out.println("New game panel");
        getHolder().addCallback(this);

        eventQueue = new ArrayList<>();

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("Surface Created");

        if (background == null) {
            System.out.println("Create background");
            background = new Background(
                    BitmapFactory.decodeResource(getResources(), R.drawable.bg),
                    BitmapFactory.decodeResource(getResources(), R.drawable.jen)
            );
            background.setVector(-250);
        }

        if (mainThread == null) {
            System.out.println("Create main thread");
            mainThread = new MainThread(getHolder(), this);
            mainThread.setRunning(true);
            mainThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println(MessageFormat.format("Surface Changed f:{0} w:{1} h:{2}", format, width, height));
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
        eventQueue.add(event);
        return super.onTouchEvent(event);
    }

    public void processInput(){
        ArrayList<MotionEvent> queue = eventQueue;
        eventQueue = new ArrayList<>();

        for (MotionEvent event : queue) {
            processInput(event);
        }
    }

    private void processInput(MotionEvent event){
        System.out.println("Process event " + event);
        if (mainThread != null) {
            switch (mainThread.FPS) {
                case 30 :
                    mainThread.FPS = 60;
                    break;
                case 60 :
                    mainThread.FPS = 15;
                    break;
                case 15 :
                    mainThread.FPS = 30;
                    break;
                default:
                    mainThread.FPS = 30;
            }
        }
    }

    public void update(double secondsPerFrame) {
        background.update(secondsPerFrame);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        final float scaleFactorX = (float)getWidth() / WIDTH;
        final float scaleFactorY = (float)getHeight() / HEIGHT;
        final float scaleFactor = Math.min(scaleFactorX, scaleFactorY);
        final float offsetX;
        final float offsetY;

        if (scaleFactorX > scaleFactorY) {
            offsetX = ((float)getWidth() - WIDTH * scaleFactor) / 2;
            offsetY = 0;
        } else {
            offsetX = 0;
            offsetY = ((float)getHeight() - HEIGHT * scaleFactor) / 2;
        }

        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.translate(offsetX, offsetY);
            canvas.clipRect(0, 0, getWidth() - offsetX*2, getHeight() - offsetY*2);
            canvas.scale(scaleFactor, scaleFactor);
            background.draw(canvas);
            canvas.restoreToCount(savedState);
        }
    }

}
