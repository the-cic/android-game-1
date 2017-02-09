package com.mush.weirdo.space;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by mirko on 20/01/2017.
 */
public class SpaceObjectBody {

    public final Rect boundsRect;

    private Point3F velocity;
    private final SpaceNode node;
    private Point3F nextGlobalPosition;
    private Point3F velocityOffset;
    private InputSpaceObjectBodyController controller;
    private boolean isSolid;

    public SpaceObjectBody(SpaceObject spaceObject, Rect b, boolean blocking) {
        this.node = spaceObject.spaceNode;
        this.boundsRect = new Rect(b);
        this.velocity = null;
        this.isSolid = blocking;
    }

    public void offsetBounds(int dLeft, int dTop, int dRight, int dBottom) {
        this.boundsRect.left += dLeft;
        this.boundsRect.top += dTop;
        this.boundsRect.right += dRight;
        this.boundsRect.bottom += dBottom;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public void setController(InputSpaceObjectBodyController c) {
        this.controller = c;
    }

    public void setVelocity(Point3F v) {
        if (velocity == null) {
            velocity = new Point3F(v);
            velocityOffset = new Point3F();
            nextGlobalPosition = new Point3F();
        } else {
            velocity.set(v);
        }
    }

    public Point3F getVelocity() {
        if (velocity == null) {
            velocity = new Point3F();
            velocityOffset = new Point3F();
            nextGlobalPosition = new Point3F();
        }
        return velocity;
    }

    // step 1
    public void update(double secondsPerFrame) {
        if (this.controller != null) {
            this.controller.update(secondsPerFrame);
        }
        if (this.velocity != null) {
            this.velocityOffset.set(this.velocity);
            this.velocityOffset.scale((float) secondsPerFrame);

            this.nextGlobalPosition.set(this.node.localToGlobal());
            this.nextGlobalPosition.offset(this.velocityOffset);
        }
    }

    public SpaceNode getNode() {
        return node;
    }

    public Point3F getNextGlobalPosition() {
        return nextGlobalPosition;
    }

    // step 3
    public void applyPositionUpdate() {
        if (this.velocityOffset == null) {
            return;
        }
        this.node.localPosition.offset(this.velocityOffset);
    }

    public boolean isMoving() {
        if (velocity == null) {
            return false;
        }
        return !velocity.isZero();
    }

    public void clearNextPosition() {
        this.velocityOffset.set(0, 0, 0);
    }

    //  step 2
    public boolean willIntersect(SpaceObjectBody other) {
        if (!isMoving()) {
            return false;
        }

        RectF nextGlobalBounds = new RectF(boundsRect);
        nextGlobalBounds.offset(nextGlobalPosition.x, nextGlobalPosition.z);

        RectF otherGlobalBounds = new RectF(other.boundsRect);

        if (other.isMoving()) {
            Point3F otherNextGlobalPosition = other.getNextGlobalPosition();
            otherGlobalBounds.offset((int) otherNextGlobalPosition.x, (int) otherNextGlobalPosition.z);
        } else {
            Point3F otherGlobalPosition = other.getNode().localToGlobal();
            otherGlobalBounds.offset((int) otherGlobalPosition.x, (int) otherGlobalPosition.z);
        }

//        return nextGlobalBounds.intersects(otherGlobalBounds.left, otherGlobalBounds.top, otherGlobalBounds.right, otherGlobalBounds.bottom);

        PointF collisionOffset = collide(nextGlobalBounds, otherGlobalBounds);
        if (collisionOffset != null) {
//            Log.i("COL", collisionOffset.toString());
            this.velocityOffset.offset(collisionOffset.x, 0, collisionOffset.y);
            this.nextGlobalPosition.offset(collisionOffset.x, 0, collisionOffset.y);
            return true;
        } else {
//            Log.i("COL", "no col");
            return false;
        }
    }


    public static PointF collide(RectF a, RectF b) {
        float bRight = b.right - a.left;
        float aRight = a.right - b.left;
        float bBottom = b.bottom - a.top;
        float aBottom = a.bottom - b.top;

        if (bRight > 0 && aRight > 0 && bBottom > 0 && aBottom > 0) {
            float dx = aRight < bRight ? -aRight : bRight;
            float dy = aBottom < bBottom ? -aBottom : bBottom;
            boolean dxGTdy = Math.abs(dx) > Math.abs(dy);
            return new PointF(
                    dxGTdy ? 0 : dx,
                    dxGTdy ? dy : 0
            );
        } else {
            return null;
        }


        //return a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom;
    }
}
