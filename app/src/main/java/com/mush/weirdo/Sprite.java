package com.mush.weirdo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Any thing displayed on screen
 *
 * Created by mirko on 14/03/2016.
 */
public class Sprite {
    private double x;
    private double y;
    private SpriteShape shape;
    Paint paint;


    public Sprite(SpriteShape shape) {
        this.shape = shape;
        paint = new Paint();

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

    }

    public void setShape(SpriteShape shape){
        this.shape = shape;
    }

    public SpriteShape getShape() {
        return this.shape;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public int getWidth(){
        return shape.getWidth();
    }

    public int getHeight(){
        return shape.getHeight();
    }

    public void draw(Canvas canvas){
        shape.draw(x, y, canvas);
        canvas.drawCircle((float)x, (float)y, 1, paint);
    }

    public boolean isVisible(){
        return x + shape.getWidth() > 0 && x < GamePanel.WIDTH;
    }
}
