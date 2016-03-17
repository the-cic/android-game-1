package com.mush.weirdo;

import android.graphics.Bitmap;

/**
 * Created by mirko on 09/03/2016.
 */
public class SpriteFrames {
    Bitmap[] frames;
    int width;
    int height;

    public SpriteFrames(Bitmap source, int tileWidth, int tileHeight, int tileCount) {
        frames = new Bitmap[tileCount];
        width = tileWidth;
        height = tileHeight;

        int x = 0;
        int y = 0;

        for (int i = 0; i < tileCount; i++) {
            System.out.println("create " + i + " " + x + " " + y);
            frames[i] = Bitmap.createBitmap(source, x, y, width, height);
            x += width;
            if (i < tileCount - 1) {
                if (x + width > source.getWidth()) {
                    x = 0;
                    y += height;
                }
                System.out.println("next x " + x);
                if (y + height > source.getHeight()) {
                    throw new RuntimeException("SpriteFrames source size");
                }
                System.out.println("next y " + y);
            }
        }
    }

    public int getFrameCount() {
        return frames.length;
    }

    public Bitmap getFrame(int i) {
        return frames[i];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
