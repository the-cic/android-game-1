package com.mush.weirdo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by mirko on 17/03/2016.
 */
public class ThreePartSpriteShape implements SpriteShape {

    private Bitmap imageLeft;
    private Bitmap imageMid;
    private Bitmap imageRight;
    private int segments;
    private int width;
    private int height;
    private SpriteShapeAlignment alignment;

    public ThreePartSpriteShape(Resources resources, int resourceLeft, int resourceMid, int resourceRight, int segments) {
        this.imageLeft = BitmapFactory.decodeResource(resources, resourceLeft);
        this.imageMid = BitmapFactory.decodeResource(resources, resourceMid);
        this.imageRight = BitmapFactory.decodeResource(resources, resourceRight);
        this.segments = segments;
        this.width = imageLeft.getWidth() + imageRight.getWidth() + segments * imageMid.getWidth();
        this.height = imageMid.getHeight();
        setAlignment(SpriteShapeAlignment.SSA_BOTTOM_LEFT);
    }

    @Override
    public void draw(double x0, double y0, Canvas canvas) {
        float x = alignment.getX(x0, width);
        float y = alignment.getY(y0, height);
        canvas.drawBitmap(imageLeft, x, y, null);
        x += imageLeft.getWidth();
        for (int i = 0; i < segments; i++) {
            canvas.drawBitmap(imageMid, x, y, null);
            x += imageMid.getWidth();
        }
        canvas.drawBitmap(imageRight, x, y, null);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setAlignment(SpriteShapeAlignment alignment) {
        this.alignment = alignment;
    }
}
