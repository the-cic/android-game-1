package com.mush.weirdo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

/**
 * Created by mirko on 16/03/2016.
 */
public class ImageSpriteShape implements SpriteShape {

    private Bitmap image;
    private int width;
    private int height;
    private Point pivot;

    public ImageSpriteShape(Resources resources, int resourceId) {
        this.image = BitmapFactory.decodeResource(resources, resourceId);
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    @Override
    public void update(double secondsPerFrame) {}

    @Override
    public void draw(double x, double y, Canvas canvas) {
        if (pivot != null) {
            canvas.drawBitmap(image, (float) (x - pivot.x), (float) (y - pivot.y), null);
        } else {
            canvas.drawBitmap(image, (float) (x), (float) (y), null);
        }
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
