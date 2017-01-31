package com.mush.weirdo.space;

import android.graphics.PointF;

/**
 * Created by Cic on 20.1.2017.
 */
public class TilingPositionProjection extends PositionProjection {

    float width;
    float height;
    float width2;
    float height2;

    public TilingPositionProjection(float w, float h) {
        width = w;
        height = h;
        width2 = w * 2;
        height2 = h * 2;
    }

    @Override
    public void transform(float x, float y, float z, PointF dest) {
        float mx = x % width;
        float mx2 = x % width2;
        float my = y % width;
        float my2 = y % width2;

        float u = mx;
        float v = my;

        if (u < 0) {
            if (mx2 < -width) {
                u = mx + width;
            }
        } else {
            if (mx2 > width) {
                u = mx + width;
            }
        }

        if (v < 0) {
            if (my2 < -height) {
                v = my + height;
            }
        } else {
            if (my2 > height) {
                v = my + height;
            }
        }

        dest.set(u, v);
    }
}
