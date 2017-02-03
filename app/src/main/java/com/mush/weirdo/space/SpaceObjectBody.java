package com.mush.weirdo.space;

import android.graphics.Rect;

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

    public void offsetBounds(int dLeft, int dTop, int dRight, int dBottom){
        this.boundsRect.left += dLeft;
        this.boundsRect.top += dTop;
        this.boundsRect.right += dRight;
        this.boundsRect.bottom += dBottom;
    }

    public boolean isSolid(){
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

    public void clearNextPosition(){
        this.velocityOffset.set(0, 0, 0);
    }

    //  step 2
    public boolean willIntersect(SpaceObjectBody other) {
        if (!isMoving()) {
            return false;
        }

        Rect nextGlobalBounds = new Rect(boundsRect);
        nextGlobalBounds.offset((int) nextGlobalPosition.x, (int) nextGlobalPosition.z);

        Rect otherGlobalBounds = new Rect(other.boundsRect);

        if (other.isMoving()) {
            Point3F otherNextGlobalPosition = other.getNextGlobalPosition();
            otherGlobalBounds.offset((int) otherNextGlobalPosition.x, (int) otherNextGlobalPosition.z);
        } else {
            Point3F otherGlobalPosition = other.getNode().localToGlobal();
            otherGlobalBounds.offset((int) otherGlobalPosition.x, (int) otherGlobalPosition.z);
        }

        return nextGlobalBounds.intersects(otherGlobalBounds.left, otherGlobalBounds.top, otherGlobalBounds.right, otherGlobalBounds.bottom);
    }

}
