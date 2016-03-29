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
            objectVelocity.x = (float) (controls.getHorizontalDirection() * 30);
            objectVelocity.y = (float) - (controls.getVerticalDirection() * 30);
        }
    }
}
