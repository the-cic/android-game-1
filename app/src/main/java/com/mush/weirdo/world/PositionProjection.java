package com.mush.weirdo.world;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by Cic on 19.1.2017.
 */
public abstract class PositionProjection {

    public abstract void transform(float x, float y, float z, PointF dest);
}
