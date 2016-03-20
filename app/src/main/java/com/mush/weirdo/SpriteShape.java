package com.mush.weirdo;

import android.graphics.Canvas;

/**
 * Created by mirko on 20/03/2016.
 */
public interface SpriteShape {

    void draw(double x, double y, Canvas canvas);

    int getWidth();

    int getHeight();

    void setAlignment(SpriteShapeAlignment alignment);

}
