package com.mush.weirdo;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by mirko on 20/03/2016.
 */
public class InputWorldObjectControl implements WorldObjectControl {

    private GameControls controls;

    public InputWorldObjectControl(GameControls controls) {
        this.controls = controls;
    }

    @Override
    public void update(WorldObject object, double secondsPerFrame) {
        PointF objectVelocity = object.getVelocity();
        if (objectVelocity != null) {
            boolean wasMoving = object.isMoving();

            objectVelocity.x = (float) (controls.getHorizontalDirection() * 30);
            objectVelocity.y = (float) - (controls.getVerticalDirection() * 30);

            boolean isMovingNow = object.isMoving();
            if (isMovingNow != wasMoving) {
                if (isMovingNow) {
                    ((AnimatedSpriteShape)object.getSprite().getShape()).playSequence(1, 0.1);
                } else {
                    ((AnimatedSpriteShape)object.getSprite().getShape()).playSequence(0, 0.1);
                }
            }
            if (objectVelocity.x != 0) {
                if (objectVelocity.x > 0) {
                    object.getSprite().setFlipX(false);
                } else {
                    object.getSprite().setFlipX(true);
                }
            }
        }
    }
}
