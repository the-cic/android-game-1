package com.mush.weirdo.world;

import com.mush.weirdo.sprites.AnimatedSpriteShape;

/**
 * Created by mirko on 20/01/2017.
 */
public abstract class SpaceObjectBodyController {

    protected SpaceObject object;
    protected SpaceObjectBody body;

    protected SpaceObjectBodyController(SpaceObject object) {
        this.object = object;
        this.body = object.body;
    }

    public void update(double secondsPerFrame) {
        Point3F objectVelocity = body.getVelocity();
        if (objectVelocity != null) {
            boolean wasMoving = body.isMoving();

            affectVelocity(objectVelocity);

            // this is very specific, don't like it
            if (object.shape != null && object.shape instanceof AnimatedSpriteShape) {
                boolean isMovingNow = body.isMoving();
                if (isMovingNow != wasMoving) {
                    if (isMovingNow) {
                        ((AnimatedSpriteShape) object.shape).playSequence(1, 0.1);
                    } else {
                        ((AnimatedSpriteShape) object.shape).playSequence(0, 0.1);
                    }
                }
            }

            // this is a bit specific, not a huge fan
            if (objectVelocity.x != 0) {
                if (objectVelocity.x > 0) {
                    object.setFlipShape(false);
                } else {
                    object.setFlipShape(true);
                }
            }
        }
    }

    public abstract void affectVelocity(Point3F velocity);
}
