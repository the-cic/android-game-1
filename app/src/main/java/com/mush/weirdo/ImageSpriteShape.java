package com.mush.weirdo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by mirko on 16/03/2016.
 */
public class ImageSpriteShape implements SpriteShape {

    private Bitmap image;
    private int width;
    private int height;
    private SpriteShapeAlignment alignment;

    public ImageSpriteShape(Resources resources, int resourceId, SpriteShapeAlignment align) {
        this.image = BitmapFactory.decodeResource(resources, resourceId);
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.setAlignment(align);
    }

    @Override
    public void draw(double x, double y, Canvas canvas) {
        canvas.drawBitmap(image, alignment.getX(x, width), alignment.getY(y, height), null);
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
