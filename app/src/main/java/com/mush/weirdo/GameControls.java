package com.mush.weirdo;

import android.view.MotionEvent;

/**
 * Created by mirko on 20/03/2016.
 */
public class GameControls {

    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int UP = 4;
    private static final int DOWN = 8;

    private int direction;
    private float leftTouchX;
    private float rightTouchY;

    public GameControls(){
        direction = 0;
    }

    public int getDirection() {
        return direction;
    }

    public int getHorizontalDirection(){
        if ((direction & RIGHT) != 0) {
            return 1;
        }
        if ((direction & LEFT) != 0) {
            return -1;
        }
        return 0;
    }

    public int getVerticalDirection(){
        if ((direction & UP) != 0) {
            return 1;
        }
        if ((direction & DOWN) != 0) {
            return -1;
        }
        return 0;
    }

    public void processInput(MotionEvent event, int screenWidth, int screenHeight) {
        //System.out.println("process:" + event);
//        int horizontal = processHorizontal(event.getX(), screenWidth);
//        int vertical = processVertical(event.getY(), screenHeight);
//        direction = 0;
        for (int i = 0; i < event.getPointerCount(); i++) {
            //int id = event.getPointerId(i);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN : case MotionEvent.ACTION_POINTER_DOWN :
//                    if (event.getX(i) < screenWidth / 2) {
//                        leftTouchX = event.getX(i);
//                        direction &= (UP | DOWN);
//                    } else {
//                        rightTouchY = event.getY(i);
//                        direction &= (LEFT | RIGHT);
//                    }
                    leftTouchX = event.getX(i);
                    rightTouchY = event.getY(i);
                    direction = 0;
                    break;
                case MotionEvent.ACTION_UP : case MotionEvent.ACTION_POINTER_UP :
//                    if (event.getX(i) < screenWidth / 2) {
//                        direction &= (UP | DOWN);
//                    } else {
//                        direction &= (LEFT | RIGHT);
//                    }
                    direction = 0;
                    break;
                case MotionEvent.ACTION_MOVE:
//                    if (event.getX(i) < screenWidth / 2) {
//                        float dx = event.getX(i) - leftTouchX;
//                        direction &= UP | DOWN;
//                        if (dx < -10) {
//                            direction |= LEFT;
//                        } else if (dx > 10) {
//                            direction |= RIGHT;
//                        }
//                    } else {
//                        float dy = event.getY(i) - rightTouchY;
//                        direction &= LEFT | RIGHT;
//                        if (dy < -10) {
//                            direction |= UP;
//                        } else if (dy > 10) {
//                            direction |= DOWN;
//                        }
//                    }
                    float dx = event.getX(i) - leftTouchX;
                    float dy = event.getY(i) - rightTouchY;

                    if (dx <= -10) {
                        direction |= LEFT;
                        leftTouchX = event.getX(i) + 10;
                    } else if (dx >= 10) {
                        direction |= RIGHT;
                        leftTouchX = event.getX(i) - 10;
                    } else {
                        direction &= UP | DOWN;
                    }

                    if (dy <= -10) {
                        direction |= UP;
                        rightTouchY = event.getY(i) + 10;
                    } else if (dy >= 10) {
                        direction |= DOWN;
                        rightTouchY = event.getY(i) - 10;
                    } else {
                        direction &= LEFT | RIGHT;
                    }
                    break;
            }
        }

//        if (event.getAction() == MotionEvent.ACTION_UP) {
//
//        } else {
//            direction |= horizontal | vertical;
//        }
    }

    private int processHorizontal(float x, int width){
        if (x < width * 0.25) {
            return LEFT;
        }
        if (x > width * 0.75) {
            return RIGHT;
        }
        return 0;
    }

    private int processVertical(float y, int height){
        if (y < height * 0.4) {
            return UP;
        }
        if (y > height * 0.6) {
            return DOWN;
        }
        return 0;
    }

}
