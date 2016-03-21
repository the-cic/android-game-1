package com.mush.weirdo;

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
        object.setX(object.getX() + controls.getHorizontalDirection() * secondsPerFrame * 30);
        object.setY(object.getY() - controls.getVerticalDirection() * secondsPerFrame * 30);
    }
}
