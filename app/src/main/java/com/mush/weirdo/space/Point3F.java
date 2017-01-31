package com.mush.weirdo.space;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Point3F holds 3 float coordinates
 * Created by Cic on 19.1.2017.
 */
public class Point3F implements Parcelable {
    public float x;
    public float y;
    public float z;

    public Point3F() {}

    public Point3F(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3F(Point3F p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    /**
     * Set the point's x and y coordinates
     */
    public final void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Set the point's x and y coordinates to the coordinates of p
     */
    public final void set(Point3F p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public final void negate() {
        x = -x;
        y = -y;
        z = -z;
    }

    public final void offset(float dx, float dy, float dz) {
        x += dx;
        y += dy;
        z += dz;
    }

    public final void offset(Point3F p) {
        x += p.x;
        y += p.y;
        z += p.z;
    }

    public final void scale(float s) {
        x *= s;
        y *= s;
        z *= s;
    }
    /**
     * Returns true if the point's coordinates equal (x,y)
     */
    public final boolean equals(float x, float y, float z) {
        return this.x == x && this.y == y && this.z == z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point3F point3F = (Point3F) o;

        if (Float.compare(point3F.x, x) != 0) return false;
        if (Float.compare(point3F.y, y) != 0) return false;
        if (Float.compare(point3F.z, z) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PointF(" + x + ", " + y + ", " + z + ")";
    }

    /**
     * Return the euclidian distance from (0,0) to the point
     */
    public final float length() {
        return length(x, y, z);
    }

    /**
     * @return true if all coordinates are 0
     */
    public final boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    /**
     * Returns the euclidian distance from (0,0) to (x,y)
     */
    public static float length(float x, float y, float z) {
        return (float) Math.hypot(x, y);
    }

    /**
     * Parcelable interface methods
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write this point to the specified parcel. To restore a point from
     * a parcel, use readFromParcel()
     * @param out The parcel to write the point's coordinates into
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeFloat(x);
        out.writeFloat(y);
        out.writeFloat(z);
    }

    public static final Parcelable.Creator<Point3F> CREATOR = new Parcelable.Creator<Point3F>() {
        /**
         * Return a new point from the data in the specified parcel.
         */
        public Point3F createFromParcel(Parcel in) {
            Point3F r = new Point3F();
            r.readFromParcel(in);
            return r;
        }

        /**
         * Return an array of rectangles of the specified size.
         */
        public Point3F[] newArray(int size) {
            return new Point3F[size];
        }
    };

    /**
     * Set the point's coordinates from the data stored in the specified
     * parcel. To write a point to a parcel, call writeToParcel().
     *
     * @param in The parcel to read the point's coordinates from
     */
    public void readFromParcel(Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
        z = in.readFloat();
    }
}

