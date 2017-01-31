package com.mush.weirdo.space;

import android.graphics.PointF;

/**
 * Created by Cic on 19.1.2017.
 */
public class ParallaxPositionProjection extends PositionProjection {

    float halfScaleZ = 1;
    Point3F reference;

    public ParallaxPositionProjection(float halfScaleZ1, Point3F reference1) {
        this.halfScaleZ = halfScaleZ1;
        if (reference1 != null) {
            reference = reference1;
        }
    }

    @Override
    public void transform(float x0, float y0, float z0, PointF dest) {
        float x = x0;
        float y = y0;
        float z = z0;

        if (reference != null) {
            x -= reference.x;
            y -= reference.y;
            z -= reference.z;
        }

        float scale = (float) Math.pow(2, -z / halfScaleZ);
        float u = x * scale;
        float v = y * scale;

        if (reference != null) {
            u += reference.x;
            v += reference.y;
        }

        dest.set(u, v);
    }
}
