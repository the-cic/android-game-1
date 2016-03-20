package com.mush.weirdo;

import android.graphics.Canvas;

/**
 * Created by mirko on 20/03/2016.
 */
public class SpriteShapeAlignment {

    public static final int ALIGN_TOP = 1;
    public static final int ALIGN_BOTTOM = 2;
    public static final int ALIGN_LEFT = 4;
    public static final int ALIGN_RIGHT = 8;

    public static final SpriteShapeAlignment SSA_TOP_LEFT =
            new SpriteShapeAlignment(ALIGN_TOP | ALIGN_LEFT);

    public static final SpriteShapeAlignment SSA_TOP_RIGHT =
            new SpriteShapeAlignment(ALIGN_TOP | ALIGN_RIGHT);

    public static final SpriteShapeAlignment SSA_BOTTOM_LEFT =
            new SpriteShapeAlignment(ALIGN_BOTTOM | ALIGN_LEFT);

    public static final SpriteShapeAlignment SSA_BOTTOM_RIGHT =
            new SpriteShapeAlignment(ALIGN_BOTTOM | ALIGN_RIGHT);

    private int alignment;
    private int vertAlignment;
    private int horizAlignment;

    public SpriteShapeAlignment(int align){
        this.alignment = align;
        horizAlignment = (alignment & ALIGN_LEFT) != 0 ? ALIGN_LEFT : ALIGN_RIGHT;
        vertAlignment =  (alignment & ALIGN_TOP) != 0 ? ALIGN_TOP : ALIGN_BOTTOM;
    }

    protected float getX(double x, int width) {
        if (horizAlignment == ALIGN_LEFT) {
            return (float)x;
        } else {
            return (float)(x - width);
        }
    }

    protected float getY(double y, int height) {
        if (vertAlignment == ALIGN_TOP) {
            return (float)y;
        } else {
            return (float)(y - height);
        }
    }
}
