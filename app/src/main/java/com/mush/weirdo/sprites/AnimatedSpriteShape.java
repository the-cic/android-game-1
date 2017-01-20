package com.mush.weirdo.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.mush.weirdo.util.BitmapFrames;

/**
 * Created by mirko on 02/04/2016.
 */
public class AnimatedSpriteShape implements SpriteShape {

    private int width;
    private int height;
    private Point pivot;

    private BitmapFrames[] framesList;
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
        framesList = new BitmapFrames[sequenceLengths.length];
        Bitmap source = BitmapFactory.decodeResource(resources, resourceId);

        for (int s = 0; s < sequenceLengths.length; s ++) {
            BitmapFrames frames = new BitmapFrames(source, frameWidth, frameHeight, s, 0, BitmapFrames.Direction.DOWN, BitmapFrames.Direction.RIGHT, sequenceLengths[s]);
            framesList[s] = frames;
        }

        playSequence(0, 1);
    }

    @Override
    public void draw(double x, double y, Canvas canvas) {
        try {
            Bitmap image = framesList[sequenceIndex].getFrame(frameIndex);
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
        this.sequenceLength = this.framesList[this.sequenceIndex].getFrameCount();
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
