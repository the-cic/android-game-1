package com.mush.weirdo;

import android.graphics.Canvas;

/**
 * Created by mirko on 16/03/2016.
 */
public abstract class SpriteShape {

    public static final int ALIGN_BOTTOM = 1;
    public static final int ALIGN_TOP = 2;
    public static final int ALIGN_LEFT = 4;
    public static final int ALIGN_RIGHT = 8;

    private int alignment = ALIGN_LEFT | ALIGN_TOP;

    public abstract void draw(double x, double y, Canvas canvas);

    public abstract int getWidth();

    public abstract int getHeight();

    public void setAlignment(int align){
        this.alignment = align;
    }

    public boolean isAlignedLeft(){
        return (alignment & ALIGN_LEFT) != 0;
    }

    public boolean isAlignedRight(){
        return (alignment & ALIGN_RIGHT) != 0;
    }

    public boolean isAlignedTop(){
        return (alignment & ALIGN_TOP) != 0;
    }

    public boolean isAlignedBottom(){
        return (alignment & ALIGN_BOTTOM) != 0;
    }

    protected float getX(double x) {
        if (isAlignedLeft()) {
            return (float)x;
        } else {
            return (float)(x - getWidth());
        }
    }

    protected float getY(double y) {
        if (isAlignedTop()) {
            return (float)y;
        } else {
            return (float)(y - getHeight());
        }
    }

}
