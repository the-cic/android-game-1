package com.mush.weirdo.world;

import android.graphics.PointF;

import com.mush.weirdo.GameControls;
import com.mush.weirdo.sprites.AnimatedSpriteShape;
import com.mush.weirdo.sprites.SpriteShape;

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
        velocity.y = (float) -(controls.getVerticalDirection() * 30);
    }
}
