package com.mush.weirdo;

/**
 * Created by mirko on 17/03/2016.
 */
public class ParallaxWorldObject extends WorldObject {
    private double factor;

    public ParallaxWorldObject(Sprite sprite, double x, double y, double factor) {
        super(sprite, x, y);
        this.factor = factor;
    }

    public void applyScreenPosition(double screenX, double screenY) {
        sprite.setPosition((getX() - screenX) * factor, getY() - screenY);
    }

    public double getFactor(){
        return factor;
    }

}
