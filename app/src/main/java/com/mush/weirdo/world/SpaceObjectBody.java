package com.mush.weirdo.world;

import android.graphics.Rect;

/**
 * Created by mirko on 20/01/2017.
 */
public class SpaceObjectBody {

    public final Rect boundsRect;

    private Point3F velocity;
    private final SpaceNode node;
    private Point3F nextLocalPosition;
    private InputSpaceObjectBodyController controller;

    public SpaceObjectBody(SpaceObject spaceObject, Rect b) {
        this.node = spaceObject.spaceNode;
        this.boundsRect = new Rect(b);
        this.velocity = null;
    }

    public void setController(InputSpaceObjectBodyController c) {
        this.controller = c;
    }

    public void setVelocity(Point3F v) {
        if (velocity == null) {
            velocity = new Point3F(v);
            nextLocalPosition = new Point3F();
        } else {
            velocity.set(v);
        }
    }

    public Point3F getVelocity() {
        if (velocity == null) {
            velocity = new Point3F();
            nextLocalPosition = new Point3F();
        }
        return velocity;
    }

    public void update(double secondsPerFrame) {
        if (this.controller != null) {
            this.controller.update(secondsPerFrame);
        }
        if (this.velocity != null) {
            this.nextLocalPosition.set(this.node.localPosition);
            this.nextLocalPosition.offset(
                    (float) (this.velocity.x * secondsPerFrame),
                    (float) (this.velocity.y * secondsPerFrame),
                    (float) (this.velocity.z * secondsPerFrame)
            );

//            if (this.objectRepository != null) {
//                checkCollisions(this.objectRepository.getObstacles());
//            }
        }
    }

    public SpaceNode getNode() {
        return node;
    }

    public Point3F getNextLocalPosition() {
        return nextLocalPosition;
    }

    public void applyPositionUpdate() {
        if (this.nextLocalPosition == null) {
            return;
        }
        this.node.localPosition.set(this.nextLocalPosition);
    }

    public boolean isMoving() {
        if (velocity == null) {
            return false;
        }
        return !velocity.isZero();
    }

    public void clearNextPosition(){
        this.nextLocalPosition.set(this.node.localPosition);
//        this.velocity = null;
    }

    public boolean willIntersect(SpaceObjectBody other) {
        if (nextLocalPosition == null) {
            return false;
        }
        Rect nextRealBounds = new Rect(boundsRect);
        nextRealBounds.offset((int)nextLocalPosition.x, (int)nextLocalPosition.z);

        Rect otherRealBounds = new Rect(other.boundsRect);
        otherRealBounds.offset((int)other.getNode().localPosition.x, (int)other.getNode().localPosition.z);

        return nextRealBounds.intersects(otherRealBounds.left, otherRealBounds.top, otherRealBounds.right, otherRealBounds.bottom);
    }

}
