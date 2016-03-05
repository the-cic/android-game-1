package com.example.mirko.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by mirko on 03/03/2016.
 */
public class Background {
    private Bitmap image;
    private int x, y, dx;
    private int bob;
    private int maxBob, bobOffset;

    public Background(Bitmap res) {
        image = res;
        maxBob = 60;
        bobOffset = 50;
    }

    public void update(){
        x += dx;
        if (x < -GamePanel.WIDTH) {
            x = 0;
        }
        bob ++;
        if (bob == maxBob) {
            bob = 0;
        }
        float t = (float)bob / maxBob;
        y = (int)(Math.sin(t * Math.PI * 2) * bobOffset);
    }


    public void draw(Canvas canvas ) {
        canvas.drawBitmap(image, x, y, null);
        if (x < 0) {
            canvas.drawBitmap(image, x + GamePanel.WIDTH, y, null);
        }
    }

    public void setVector(int dx){
        this.dx = dx;
    }
}
