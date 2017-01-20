package com.mush.weirdo.world;

import android.graphics.Rect;

/**
 * Created by mirko on 20/01/2017.
 */
public class SpaceObjectBody {

    public final Rect bounds;

    private Point3F velocity;
    private final SpaceNode node;
    private Point3F nextLocalPosition;
    private InputSpaceObjectBodyController controller;

    public SpaceObjectBody(SpaceObject spaceObject, Rect b) {
        this.node = spaceObject.spaceNode;
        this.bounds = new Rect(b);
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

    public void applyPositionUpdate(){
        this.node.localPosition.set(this.nextLocalPosition);
    }

    public boolean isMoving() {
        if(velocity == null){
            return false;
        }
        return !velocity.isZero();
    }
}
