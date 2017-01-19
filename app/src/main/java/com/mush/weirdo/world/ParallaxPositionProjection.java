package com.mush.weirdo.world;

import android.graphics.PointF;

/**
 * Created by Cic on 19.1.2017.
 */
public class ParallaxPositionProjection extends PositionProjection {

    float halfScaleZ = 1;

    public ParallaxPositionProjection(float halfScaleZ1) {
        this.halfScaleZ = halfScaleZ1;
    }

    @Override
    public void transform(float x, float y, float z, PointF dest) {
        float scale = (float) Math.pow(2, -z / halfScaleZ);
        float u = x * scale;
        float v = y * scale;
        dest.set(u, v);
    }
}
