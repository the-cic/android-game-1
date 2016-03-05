package com.example.mirko.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by mirko on 03/03/2016.
 */
public class GameContent {
    private Bitmap image;
    private Bitmap jen;
    private double x, y, dx;
    private double bob;
    private int maxBob, bobOffset;
    private double jenU;
    private double jenV;
    private int jenX;
    private int jenY;
    private int jenOffset;

    public GameContent(Resources resources) {
        image = BitmapFactory.decodeResource(resources, R.drawable.bg);
        jen = BitmapFactory.decodeResource(resources, R.drawable.jen);

        maxBob = 60;
        bobOffset = 50;
        jenOffset = 100;

        setVector(-250);
    }

    public void processInput(MotionEvent event) {

    }

    public void update(double secondsPerFrame) {
        x += dx * secondsPerFrame;
        if (x < -GamePanel.WIDTH) {
            x = 0;
        }
        bob += 30 * secondsPerFrame;
        if (bob >= maxBob) {
            bob -= maxBob;
        }
        float t = (float) bob / maxBob;
        y = (int) (Math.sin(t * Math.PI * 2) * bobOffset);

        jenU += 3.3 * secondsPerFrame;
        jenV += 11.1 * secondsPerFrame;
        jenX = (int) (Math.sin(jenU) * jenOffset);
        jenY = (int) Math.abs((Math.sin(jenV) * jenOffset / 2));
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, (int) x, (int) y, null);
        if (x < 0) {
            canvas.drawBitmap(image, (int) x + GamePanel.WIDTH, (int) y, null);
        }
        canvas.drawBitmap(jen, GamePanel.WIDTH / 2 + jenX, GamePanel.HEIGHT / 2 - 125 + jenY, null);
    }

    public void setVector(int dx) {
        this.dx = dx;
    }
}
