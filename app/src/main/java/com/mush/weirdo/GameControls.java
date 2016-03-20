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
//        System.out.println(event);
        int horizontal = processHorizontal(event.getX(), screenWidth);
        int vertical = processVertical(event.getY(), screenHeight);
        direction = 0;

        if (event.getAction() == MotionEvent.ACTION_UP) {

        } else {
            direction |= horizontal | vertical;
        }
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
        if (y < height * 0.25) {
            return UP;
        }
        if (y > height * 0.75) {
            return DOWN;
        }
        return 0;
    }

}
