package com.mush.weirdo;

import android.graphics.Bitmap;

/**
 * Created by mirko on 16/03/2016.
 */
public class WorldObject {

    public Sprite sprite;
    private double x;
    private double y;

    public WorldObject(Sprite sprite, double x, double y) {
        setSprite(sprite);
        setX(x);
        setY(y);
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public void applyScreenPosition(double screenX, double screenY) {
        sprite.setPosition(x - screenX, y - screenY);
        //return (getX() + ofs - screenX) * factor;
    }
}
