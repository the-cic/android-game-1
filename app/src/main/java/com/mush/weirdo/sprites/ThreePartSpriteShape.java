package com.mush.weirdo.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

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
    private Point pivot;

    public ThreePartSpriteShape(Resources resources, int resourceLeft, int resourceMid, int resourceRight, int segments) {
        this.imageLeft = BitmapFactory.decodeResource(resources, resourceLeft);
        this.imageMid = BitmapFactory.decodeResource(resources, resourceMid);
        this.imageRight = BitmapFactory.decodeResource(resources, resourceRight);
        this.segments = segments;
        this.width = imageLeft.getWidth() + imageRight.getWidth() + segments * imageMid.getWidth();
        this.height = imageMid.getHeight();
        this.pivot = new Point(0, this.getHeight());
    }

    public ThreePartSpriteShape(Resources resources, int resource, int widthLeft, int widthMid, int widthRight, int segments) {
        Bitmap source = BitmapFactory.decodeResource(resources, resource);
        this.width = widthLeft + widthRight + segments * widthMid;
        this.height = source.getHeight();
        this.segments = segments;
        this.imageLeft = Bitmap.createBitmap(source, 0, 0, widthLeft, height);
        this.imageMid = Bitmap.createBitmap(source, widthLeft, 0, widthMid, height);
        this.imageRight = Bitmap.createBitmap(source, widthLeft + widthMid, 0, widthRight, height);
        this.pivot = new Point(0, this.getHeight());
    }

    @Override
    public void update(double secondsPerFrame) {}

    @Override
    public void draw(double x0, double y0, Canvas canvas) {
        float x = (float)(x0 - pivot.x);
        float y = (float)(y0 - pivot.y);
        canvas.drawBitmap(imageLeft, x, y, null);
        x += imageLeft.getWidth();
        for (int i = 0; i < segments; i++) {
            canvas.drawBitmap(imageMid, x, y, null);
            x += imageMid.getWidth();
        }
        canvas.drawBitmap(imageRight, x, y, null);
    }

    @Override
    public Point getPivot() {
        return this.pivot;
    }

    @Override
    public void setPivot(Point pivot) {
        this.pivot = pivot;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

}
