package com.example.mirko.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by mirko on 03/03/2016.
 */
public class Background {
    private Bitmap image;
    private double x, y, dx;
    private double bob;
    private int maxBob, bobOffset;

    public Background(Bitmap res) {
        image = res;
        maxBob = 60;
        bobOffset = 50;
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
    }


    public void draw(Canvas canvas ) {
        canvas.drawBitmap(image, (int)x, (int)y, null);
        if (x < 0) {
            canvas.drawBitmap(image, (int)x + GamePanel.WIDTH, (int)y, null);
        }
    }

    public void setVector(int dx){
        this.dx = dx;
    }
}
