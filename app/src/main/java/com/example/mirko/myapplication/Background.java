package com.example.mirko.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by mirko on 03/03/2016.
 */
public class Background {
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

    public Background(Bitmap res, Bitmap jenRes) {
        image = res;
        jen = jenRes;
        maxBob = 60;
        bobOffset = 50;
        jenOffset = 100;
    }

    public void update(double secondsPerFrame){
        x += dx * secondsPerFrame;
        if (x < -GamePanel.WIDTH) {
            x = 0;
        }
        bob += 30 * secondsPerFrame;
        if (bob >= maxBob) {
            bob -= maxBob;
        }
        float t = (float)bob / maxBob;
        y = (int)(Math.sin(t * Math.PI * 2) * bobOffset);

        jenU += 3.3 * secondsPerFrame;
        jenV += 11.1 * secondsPerFrame;
        jenX = (int)(Math.sin(jenU) * jenOffset);
        jenY = (int)Math.abs((Math.sin(jenV) * jenOffset / 2));
    }


    public void draw(Canvas canvas ) {
        canvas.drawBitmap(image, (int)x, (int)y, null);
        if (x < 0) {
            canvas.drawBitmap(image, (int)x + GamePanel.WIDTH, (int)y, null);
        }
        canvas.drawBitmap(jen, GamePanel.WIDTH / 2 + jenX, GamePanel.HEIGHT / 2 - 125 + jenY, null);
    }

    public void setVector(int dx){
        this.dx = dx;
    }
}
