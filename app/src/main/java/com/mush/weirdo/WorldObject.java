package com.mush.weirdo;

import android.graphics.Canvas;

/**
 * Created by mirko on 16/03/2016.
 */
public class WorldObject {

    public Sprite sprite;
    private double x;
    private double y;
    private ScreenPanEffect panEffect;

    public WorldObject(Sprite sprite, double x, double y, ScreenPanEffect panEffect) {
        setSprite(sprite);
        setX(x);
        setY(y);
        this.panEffect = panEffect;
        sprite.setPosition(x, y);
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }
    public ScreenPanEffect getPanEffect() {
        return this.panEffect;
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

    public void applyScreenPan(double screenX, double screenY) {
        if (panEffect != null) {
            panEffect.panWorldObject(screenX, screenY, this);
        }
    }

    /*
    public void applyScreenPosition(double screenX, double screenY) {
        sprite.setPosition(x - screenX, y - screenY);
        //return (getX() + ofs - screenX) * factor;
    }*/
}
