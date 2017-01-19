package com.mush.weirdo.screenpan;

import com.mush.weirdo.WorldObject;
import com.mush.weirdo.screenpan.ScreenPanEffect;

/**
 * Created by mirko on 20/03/2016.
 */
public class ParallaxScreenPanEffect implements ScreenPanEffect {

    private double factor;

    public ParallaxScreenPanEffect(double factor) {
        this.factor = factor;
    }

    public double getFactor(){
        return this.factor;
    }

    @Override
    public void panWorldObject(double screenX, double screenY, WorldObject object) {
        object.getSprite().setPosition((object.getX() - screenX) * factor, object.getY() - screenY);
    }
}
