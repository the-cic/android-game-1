package com.mush.weirdo;

import android.graphics.Bitmap;
import android.graphics.Canvas;

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
        return getScreenX(screenX, 0);
    }

    public double getScreenX(double screenX, int ofs) {
        return (getX() + ofs - screenX) * factor;
    }

    public double getScreenY(){
        return y;
    }

    public double getX() {
        return x;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public void setX(double x) {
        this.x = x;
    }

    public void draw(Canvas canvas, double baseX){
        canvas.drawBitmap(image,
                (float) getScreenX(baseX),
                (float) getScreenY(),
                null);
    }
}
