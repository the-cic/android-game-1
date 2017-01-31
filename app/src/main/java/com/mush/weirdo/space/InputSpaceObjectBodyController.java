package com.mush.weirdo.space;

import com.mush.weirdo.GameControls;

/**
 * Created by mirko on 20/01/2017.
 */
public class InputSpaceObjectBodyController extends SpaceObjectBodyController {

    private GameControls controls;

    public InputSpaceObjectBodyController(GameControls controls, SpaceObject object) {
        super(object);
        this.controls = controls;
    }

    @Override
    public void affectVelocity(Point3F velocity) {
        velocity.x = (float) (controls.getHorizontalDirection() * 30);
        velocity.z = (float) -(controls.getVerticalDirection() * 30);
        //velocity.z = velocity.y;
    }
}
