package com.mush.weirdo;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by mirko on 16/03/2016.
 */
public class WorldObject {

    public Sprite sprite;
    private double x;
    private double y;
    private double nextX;
    private double nextY;
    private ScreenPanEffect panEffect;
    private WorldObjectControl control;
    private Double boundWidth;
    private Double boundHeight;

    public WorldObject(Sprite sprite, double x, double y, ScreenPanEffect panEffect, WorldObjectControl control) {
        setSprite(sprite);
        setX(x);
        setY(y);
        this.panEffect = panEffect;
        this.control = control;
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

    public WorldObjectControl getControl(){
        return this.control;
    }

    public void setX(double x) {
        this.x = x;
        this.nextX = x;
    }
    public void setY(double y) {
        this.y = y;
        this.nextY = y;
    }

    public void setNextX(double x) {
        this.nextX = x;
    }
    public void setNextY(double y) {
        this.nextY = y;
    }

    public void setBounds(double w, double h) {
        this.boundWidth = w;
        this.boundHeight = h;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

//    public double getNextX() {
//        return this.nextX;
//    }
//
//    public double getNextY() {
//        return this.nextY;
//    }

    public Double getBoundWidth(){
        return this.boundWidth;
    }

    public Double getBoundHeight(){
        return this.boundHeight;
    }

    public void applyScreenPan(double screenX, double screenY) {
        if (panEffect != null) {
            panEffect.panWorldObject(screenX, screenY, this);
        }
    }

    public void update(double secondsPerFrame, ArrayList<WorldObject> objects) {
        if (control != null) {
            control.update(this, secondsPerFrame);
            boolean collision = false;
            for (WorldObject other : objects) {
                if (checkCollisionWith(other)) {
                    collision = true;
                    break;
                }
            }
            if (!collision) {
                this.x = this.nextX;
                this.y = this.nextY;
            }
        }
    }

    private boolean checkCollisionWith(WorldObject other) {
        if (other == this) {
            return false;
        }
        if (other.getBoundWidth() != null) {
            boolean xWithin = this.nextX > other.getX() && this.nextX < other.getX() + other.getBoundWidth();
            boolean yWithin = this.nextY > other.getY() - other.getBoundHeight() && this.nextY < other.getY();
            return xWithin && yWithin;
        } else {
            return false;
        }
    }

    /*
    public void applyScreenPosition(double screenX, double screenY) {
        sprite.setPosition(x - screenX, y - screenY);
        //return (getX() + ofs - screenX) * factor;
    }*/
}
