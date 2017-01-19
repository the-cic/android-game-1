package com.mush.weirdo.world;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by Cic on 19.1.2017.
 */
public abstract class PositionProjection {

    private PointF pan = new PointF(0, 0);

    public void setPan(float x, float y) {
        this.pan.set(x, y);
    }

    protected abstract void project(float x, float y, float z, PointF dest);

    public void transform(float x, float y, float z, PointF dest) {
        project(x, y, z, dest);
        dest.set(dest.x + pan.x, dest.y + pan.y);
    }
}
