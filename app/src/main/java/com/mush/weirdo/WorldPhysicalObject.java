package com.mush.weirdo;

import android.graphics.Rect;

/**
 * Created by mirko on 25/03/2016.
 */
public class WorldPhysicalObject {
    private WorldObject worldObject;
    private double x;
    private double y;
    private double xVelocity;
    private double yVelocity;
    private Rect bounds;

    public WorldPhysicalObject(WorldObject object) {
        this.worldObject = object;
    }

    public double getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public double getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public Rect getBounds() {
        return bounds;
    }

    public void setBounds(Rect bounds) {
        this.bounds = bounds;
    }

    public WorldObject getWorldObject() {
        return worldObject;
    }
}
