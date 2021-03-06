package com.mush.weirdo;

import android.view.MotionEvent;

/**
 * Created by mirko on 20/03/2016.
 */
public class GameControls {

    private float firstTouchX;
    private float firstTouchY;
    private double offsetX;
    private double offsetY;
    private double directionX;
    private double directionY;
    private boolean dirty;

    public GameControls() {
        release();
    }

    private void release() {
        offsetX = 0;
        offsetY = 0;
        dirty = true;
    }

    private void press(float x, float y) {
        offsetX = 0;
        offsetY = 0;
        firstTouchX = x;
        firstTouchY = y;
        dirty = true;
    }

    private void pushHorizontal(double dx) {
        offsetX = dx;
        dirty = true;
    }

    private void pushVertical(double dy) {
        offsetY = dy;
        dirty = true;
    }

    private void calculateDirection() {
        if (!dirty) {
            return;
        }
        double length = Math.sqrt(offsetX * offsetX + offsetY * offsetY);
        if (length > 5) {
            directionX = offsetX / length;
            directionY = offsetY / length;
            firstTouchX = (float)((firstTouchX + offsetX) - directionX * 6);
            firstTouchY = (float)((firstTouchY + offsetY) - directionY * 6);
            makeDirection8Way();
        } else {
            directionX = 0;
            directionY = 0;
        }
        dirty = false;
    }

    private void makeDirection8Way() {
        // 1/sqrt(2)
        final double angle = 0.7;
        final double halfAngle = angle * 0.5;
        if (directionX > halfAngle) {
            directionX = 1;
        } else if (directionX < -halfAngle) {
            directionX = -1;
        } else {
            directionX = 0;
        }
        if (directionY > halfAngle) {
            directionY = 1;
        } else if (directionY < -halfAngle) {
            directionY = -1;
        } else {
            directionY = 0;
        }
        if (directionX != 0 && directionY != 0) {
            directionX *= angle;
            directionY *= angle;
        }
    }

    public double getHorizontalDirection(){
        calculateDirection();
        return directionX;
    }

    public double getVerticalDirection(){
        calculateDirection();
        return -directionY;
    }

    public void processInput(MotionEvent event, int screenWidth, int screenHeight) {
        for (int i = 0; i < event.getPointerCount(); i++) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN : case MotionEvent.ACTION_POINTER_DOWN :
                    press(event.getX(i), event.getY(i));
                    break;
                case MotionEvent.ACTION_UP : case MotionEvent.ACTION_POINTER_UP :
                    release();
                    break;
                case MotionEvent.ACTION_MOVE:
                    pushHorizontal(event.getX(i) - firstTouchX);
                    pushVertical(event.getY(i) - firstTouchY);
                    break;
            }
        }
    }

}
