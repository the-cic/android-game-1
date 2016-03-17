package com.mush.weirdo;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by mirko on 11/03/2016.
 */
public class ExtendableObject extends BackgroundObject {

    Bitmap left;
    Bitmap right;
    int width;
    int segments;

    public ExtendableObject(Bitmap left, Bitmap middle, Bitmap right, int segments, double factor, double baseY){
        super(middle, factor, baseY);
        this.left = left;
        this.right = right;
        this.segments = segments;
        this.width = this.left.getWidth() + (this.segments * this.image.getWidth()) + this.right.getWidth();
    }

    @Override
    public int getWidth(){
        return width;
    }

    @Override
    public void draw(Canvas canvas, double baseX){
        int xOfs = left.getWidth();
        canvas.drawBitmap(left,
                (float) getScreenX(baseX),
                (float) getScreenY(),
                null);
        for (int i = 0; i < segments; i++) {
            canvas.drawBitmap(image,
                    (float) getScreenX(baseX, xOfs),
                    (float) getScreenY(),
                    null);
            xOfs += image.getWidth();
        }

        canvas.drawBitmap(right,
                (float) getScreenX(baseX, xOfs),
                (float) getScreenY(),
                null);
    }

}
