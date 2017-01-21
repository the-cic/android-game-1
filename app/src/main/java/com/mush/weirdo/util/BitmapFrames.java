package com.mush.weirdo.util;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * Created by mirko on 09/03/2016.
 */
public class BitmapFrames {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    Bitmap[] frames;
    int width;
    int height;
    Direction direction;

    public BitmapFrames(Bitmap source, int tileWidth, int tileHeight, int tileCount) {
        this(source,
                tileWidth,
                tileHeight,
                0,
                0,
                Direction.RIGHT,
                Direction.DOWN,
                tileCount);
    }

    public BitmapFrames(
            Bitmap source,
            int tileWidth,
            int tileHeight,
            int startColumn,
            int startRow,
            Direction direction,
            Direction wrapDirection,
            int tileCount) {

        frames = new Bitmap[tileCount];
        width = tileWidth;
        height = tileHeight;

        int x0 = startColumn * width;
        int y0 = startRow * height;
        int x = x0;
        int y = y0;

        Point ofs = getOffsets(direction);
        Point wOfs = getOffsets(wrapDirection);

        int xStep = ofs.x * width;
        int yStep = ofs.y * height;
        int xWrapStep = wOfs.x * width;
        int yWrapStep = wOfs.y * height;

        for (int i = 0; i < tileCount; i++) {
//            System.out.println("create " + i + " " + x + " " + y);
            frames[i] = Bitmap.createBitmap(source, x, y, width, height);

            if (i + 1 < tileCount) {
                x += xStep;
                y += yStep;

                switch (direction) {
                    case LEFT:
                        if (x < 0) {
                            x = x0;
                            y += yWrapStep;
                        }
                        break;
                    case RIGHT:
                        if (x + width > source.getWidth()) {
                            x = 0;
                            y += yWrapStep;
                        }
                        break;
                    case UP:
                        if (y < 0) {
                            y = y0;
                            x += xWrapStep;
                        }
                        break;
                    case DOWN:
                        if (y + height > source.getHeight()) {
                            y = 0;
                            x += xWrapStep;
                        }
                        break;
                }

                if (x < 0 || x + width > source.getWidth()) {
                    throw new RuntimeException("BitmapFrames source size");
                }
                System.out.println("next x " + x);
                if (y < 0 || y + height > source.getHeight()) {
                    throw new RuntimeException("BitmapFrames source size");
                }
//                System.out.println("next y " + y);
            }
        }
    }

    private Point getOffsets(Direction direction) {
        Point p = new Point();
        switch (direction) {
            case LEFT:
                p.x = -1;
                p.y = 0;
                break;
            case RIGHT:
                p.x = 1;
                p.y = 0;
                break;
            case UP:
                p.x = 0;
                p.y = -1;
                break;
            case DOWN:
                p.x = 0;
                p.y = 1;
                break;
        }
        return p;
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

    public Bitmap[] getFrames() {
        return frames;
    }
}
