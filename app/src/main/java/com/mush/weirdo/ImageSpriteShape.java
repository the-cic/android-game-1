package com.mush.weirdo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by mirko on 16/03/2016.
 */
public class ImageSpriteShape extends SpriteShape {

    private Bitmap image;
    private int width;
    private int height;

    public ImageSpriteShape(Bitmap image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public ImageSpriteShape(Resources resources, int resourceId) {
        this.image = BitmapFactory.decodeResource(resources, resourceId);
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    @Override
    public void draw(double x, double y, Canvas canvas) {
        canvas.drawBitmap(image, getX(x), getY(y), null);
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
