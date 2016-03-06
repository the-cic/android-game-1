package com.example.mirko.myapplication;

import android.graphics.Bitmap;

/**
 * Created by mirko on 06/03/2016.
 */
public class BackgroundObject {
    public Bitmap image;
    private double x;
    private double y;
    public double factor;

    public BackgroundObject(Bitmap image, double factor, double baseY){
        this.image = image;
        this.factor = factor;
        this.y = baseY - image.getHeight();
    }

    public double getScreenX(double screenX) {
        return (getX() - screenX) * factor;
    }

    public double getScreenY(){
        return y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }
}
