package com.mush.weirdo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by mirko on 02/04/2016.
 */
public class AnimatedSpriteShape implements SpriteShape {

    private int width;
    private int height;
    private Point pivot;

    private Bitmap[][] sequences;
    private int sequenceIndex;
    private int sequenceLength;
    private int frameIndex;
    private double frameDuration;
    private double frameShownTime;
    private Paint paint;

    public AnimatedSpriteShape(Resources resources, int resourceId, int frameWidth, int frameHeight, int[] sequenceLengths) {
        this.width = frameWidth;
        this.height = frameHeight;
        this.sequenceIndex = 0;
        this.frameIndex = 0;
        sequences = new Bitmap[sequenceLengths.length][];
        Bitmap source = BitmapFactory.decodeResource(resources, resourceId);

        for (int s = 0; s < sequenceLengths.length; s ++) {
            Bitmap[] sequence = new Bitmap[sequenceLengths[s]];
            for (int f = 0; f < sequenceLengths[s]; f ++) {
                sequence[f] = cutFrameFromBitmap(source, s, f);
            }
            sequences[s] = sequence;
        }

        playSequence(0, 1);
    }

    private Bitmap cutFrameFromBitmap(Bitmap source, int xIndex, int yIndex){
        int x = xIndex * width;
        int y = yIndex * height;
        try {
            Bitmap frame = Bitmap.createBitmap(source, x, y, width, height);
            return frame;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void draw(double x, double y, Canvas canvas) {
        try {
            Bitmap image = sequences[sequenceIndex][frameIndex];
            drawFrame(image, x, y, canvas);
        } catch (Exception e) {
            drawFrame(null, x, y, canvas);
        }
    }

    private void drawFrame(Bitmap frame, double x, double y, Canvas canvas) {
        float frameX;
        float frameY;
        if (pivot != null) {
            frameX = (float) (x - pivot.x);
            frameY = (float) (y - pivot.y);
        } else {
            frameX = (float) x;
            frameY = (float) y;
        }
        if (frame != null) {
            canvas.drawBitmap(frame, frameX, frameY, null);
        } else {
            if (paint == null) {
                paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStyle(Paint.Style.STROKE);
            }
            canvas.drawRect(frameX, frameY, frameX+width, frameY+height, paint);
        }
    }

    public void playSequence(int sequenceId, double frameDuration) {
        this.sequenceIndex = sequenceId;
        this.sequenceLength = this.sequences[this.sequenceIndex].length;
        this.frameDuration = frameDuration;
        this.frameIndex = 0;
        this.frameShownTime = 0;
    }

    private void nextFrame() {
        if (this.sequenceLength == 0) {
            return;
        }
        this.frameIndex = (this.frameIndex + 1) % this.sequenceLength;
    }

    @Override
    public void update(double secondsPerFrame){
        this.frameShownTime += secondsPerFrame;
        int maxLoop = 10;
        while (this.frameShownTime > this.frameDuration && maxLoop > 0) {
            nextFrame();
            this.frameShownTime -= this.frameDuration;
            maxLoop --;
        }
    }

    @Override
    public Point getPivot() {
        return pivot;
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
