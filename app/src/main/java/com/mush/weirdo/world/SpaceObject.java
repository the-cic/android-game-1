package com.mush.weirdo.world;

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

    public SpaceObject(SpaceNode node) {
        spaceNode = node;

        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setupBody(Rect bounds) {
        body = new SpaceObjectBody(this, bounds);
    }

    public void setFlipShape(boolean f) {
        this.flipShape = f;
    }

    public void draw(Canvas canvas, PositionProjection projection) {
        Point3F position = spaceNode.localToGlobal();
        if (projection != null) {
            projection.transform(position, drawPoint);
        } else {
            drawPoint.x = position.x;
            drawPoint.y = position.y;
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

        canvas.drawCircle((float) drawPoint.x, (float) drawPoint.y, 1, paint);
    }

    public void update(double secondsPerFrame) {
        if (shape != null) {
            shape.update(secondsPerFrame);
        }
        if (body != null) {
            body.update(secondsPerFrame);
            body.applyPositionUpdate();
        }
    }
}
