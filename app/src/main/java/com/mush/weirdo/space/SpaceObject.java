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
    public static boolean drawMarkers = true;

    public SpaceObject(SpaceNode node) {
        spaceNode = node;

        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(50);
    }

    public void setupBody(Rect bounds) {
        body = new SpaceObjectBody(this, bounds);
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

        if (drawMarkers) {
            canvas.drawCircle((float) drawPoint.x, (float) drawPoint.y, 1, paint);

            if (body != null) {
                final int savedState = canvas.save();
                canvas.translate(drawPoint.x, drawPoint.y);
                canvas.drawRect(body.boundsRect, paint);
                canvas.restoreToCount(savedState);
            }
        }
    }

    public void update(double secondsPerFrame) {
        updateShape(secondsPerFrame);
        updateBody(secondsPerFrame);
    }

    public void updateShape(double secondsPerFrame) {
        if (shape != null) {
            shape.update(secondsPerFrame);
        }
    }

    public void updateBody(double secondsPerFrame) {
        if (body != null) {
            body.update(secondsPerFrame);
            body.applyPositionUpdate();
        }
    }
}
