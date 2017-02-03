package com.mush.weirdo.space;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

import com.mush.weirdo.sprites.SpriteShape;

/**
 * Created by Cic on 20.1.2017.
 */
public class SpaceObject {

    public SpaceNode spaceNode;
    public SpriteShape shape;
    public SpaceObjectBody body;

    private final PointF drawPoint = new PointF();
    private Paint paint;
    private boolean flipShape = false;
    public static boolean shouldDrawMarkers = true;

    public SpaceObject(SpaceNode node) {
        spaceNode = node;

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(75);
    }

    public void setupBody(Rect bounds, boolean isSolid) {
        body = new SpaceObjectBody(this, bounds, isSolid);
    }

    /**
     * Try to limit values to 0, 0.5 and 1
     *
     * @param fl
     * @param ft
     * @param fr
     * @param fb
     */
    public void setupBodyRelativeToShape(float fl, float ft, float fr, float fb, boolean isSolid){
        if (shape != null) {
            body = new SpaceObjectBody(this, new Rect(
                    (int) (shape.getWidth() * fl),
                    (int) (shape.getHeight() * ft),
                    (int) (shape.getWidth() * fr),
                    (int) (shape.getHeight() * fb)),
                    isSolid);
        }
    }

    public void setFlipShape(boolean f) {
        this.flipShape = f;
    }

    public void draw(Canvas canvas, PositionProjection projection) {
        Point3F globalPosition = spaceNode.localToGlobal();
        spaceNode.invalidateGlobalPosition();
        if (projection != null) {
            projection.transform(globalPosition, drawPoint);
        } else {
            drawPoint.x = globalPosition.x;
            drawPoint.y = globalPosition.y;
        }

        if (shape != null) {
            if (flipShape) {
                final int savedState = canvas.save();
                canvas.scale(-1, 1, drawPoint.x, drawPoint.y);

                shape.draw(drawPoint.x, drawPoint.y, canvas);

                canvas.restoreToCount(savedState);
            } else {
                shape.draw(drawPoint.x, drawPoint.y, canvas);
            }
        }
    }

    public void drawMarkers(Canvas canvas){
        if (shouldDrawMarkers) {
            canvas.drawCircle((float) drawPoint.x, (float) drawPoint.y, 1, paint);

            if (body != null) {
                final int savedState = canvas.save();
                canvas.translate(drawPoint.x, drawPoint.y);
                canvas.drawRect(body.boundsRect, paint);
                canvas.restoreToCount(savedState);
            }
        }
    }

    public void updateShape(double secondsPerFrame) {
        if (shape != null) {
            shape.update(secondsPerFrame);
        }
    }

}
