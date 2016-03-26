package com.mush.weirdo;

import android.graphics.Canvas;
import android.graphics.Point;

/**
 * Created by mirko on 20/03/2016.
 */
public interface SpriteShape {

    void draw(double x, double y, Canvas canvas);

    Point getPivot();

    void setPivot(Point pivot);

    int getWidth();

    int getHeight();

}
