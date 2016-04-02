package com.mush.weirdo;

import android.graphics.PointF;
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
    private Rect bounds;
    private PointF velocity;
    private WorldObjectRepository objectRepository;

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

    public void setBounds(int l, int t, int r, int b) {
        this.bounds = new Rect(l, t, r, b);
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public boolean isMoving() {
        if (this.velocity == null) {
            return false;
        }
        return this.velocity.x != 0 || this.velocity.y != 0;
    }

    public void applyScreenPan(double screenX, double screenY) {
        if (panEffect != null) {
            panEffect.panWorldObject(screenX, screenY, this);
        }
    }

    public void update(double secondsPerFrame) {
        this.getSprite().getShape().update(secondsPerFrame);

        if (control != null) {
            control.update(this, secondsPerFrame);
        }
        if (this.velocity != null) {
            this.nextX = this.x + this.velocity.x * secondsPerFrame;
            this.nextY = this.y + this.velocity.y * secondsPerFrame;

            if (this.objectRepository != null) {
                checkCollisions(this.objectRepository.getObstacles());
            }
        }
    }

    public void applyUpdate(){
        this.x = this.nextX;
        this.y = this.nextY;
    }

    private void checkCollisions(ArrayList<WorldObject> objects) {
        WorldObject collision = null;
        for (WorldObject other : objects) {
            if (checkCollisionWith(other)) {
                collision = other;
                break;
            }
        }
        if (collision != null) {
            Rect otherBounds = collision.getBounds();
            /*
            boolean xWithin = this.x > collision.getX() + otherBounds.left && this.x < collision.getX() + otherBounds.right;
            boolean yWithin = this.y > collision.getY() + otherBounds.top && this.y < collision.getY() + otherBounds.bottom;
            if (!(xWithin && !yWithin)) {
                this.nextX = x;
            } else
            if (!(yWithin && !xWithin)) {
                this.nextY = this.y;
            }*/
            this.nextX = x;
            this.nextY = this.y;
        }
    }

    private boolean checkCollisionWith(WorldObject other) {
        if (other == this) {
            return false;
        }
        if (other.getBounds() != null) {
            Rect otherBounds = other.getBounds();
            double otherX1 = other.getX() + otherBounds.left;
            double otherY1 = other.getY() + otherBounds.top;
            double otherX2 = other.getX() + otherBounds.right;
            double otherY2 = other.getY() + otherBounds.bottom;

            if (this.bounds == null) {
                return checkPointCollisionWith(this.nextX, this.nextY, otherX1, otherY1, otherX2, otherY2);
            } else {
                boolean hit = false;
                hit |= checkPointCollisionWith(this.nextX + bounds.left, this.nextY + bounds.top, otherX1, otherY1, otherX2, otherY2);
                hit |= checkPointCollisionWith(this.nextX + bounds.right, this.nextY + bounds.top, otherX1, otherY1, otherX2, otherY2);
                hit |= checkPointCollisionWith(this.nextX + bounds.left, this.nextY + bounds.bottom, otherX1, otherY1, otherX2, otherY2);
                hit |= checkPointCollisionWith(this.nextX + bounds.right, this.nextY + bounds.bottom, otherX1, otherY1, otherX2, otherY2);
                return hit;
            }
        } else {
            return false;
        }
    }

    private boolean checkPointCollisionWith(double pX, double pY, double otherX1, double otherY1, double otherX2, double otherY2) {
        boolean xWithin = pX > otherX1 && pX < otherX2;
        boolean yWithin = pY > otherY1 && pY < otherY2;
        return xWithin && yWithin;
    }

    public Rect getBounds() {
        return bounds;
    }

    public void setBounds(Rect bounds) {
        this.bounds = bounds;
    }

    public PointF getVelocity() {
        return velocity;
    }

    public void setVelocity(PointF velocity) {
        this.velocity = velocity;
    }

    public void setObjectRepository(WorldObjectRepository objectRepository) {
        this.objectRepository = objectRepository;
    }
}
