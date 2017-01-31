package com.mush.weirdo.space;

import android.graphics.PointF;

/**
 * Created by Cic on 19.1.2017.
 */
public class IsometricPositionProjection extends PositionProjection {

    float xu;
    float xv;
    float zu;
    float zv;

    /**
     * Plain square
     */
    public IsometricPositionProjection() {
        xu = 1;
        zu = 0;
        xv = 0;
        zv = 1;
    }

    /**
     * u = a * x + b * z;
     * v = c * x + y + d * z;
     * @param a
     * @param b
     * @param c
     * @param d
     */
    public IsometricPositionProjection(float a, float b, float c, float d) {
        xu = a;
        zu = b;
        xv = c;
        zv = d;
    }

    @Override
    public void transform(float x, float y, float z, PointF dest) {
        float u = x * xu + z * zu;
        float v = x * xv + y + z * zv;
        dest.set(u, v);
    }
}
