package com.diedrico.diedricoapp.vector;

/**
 * Created by amil101 on 25/04/16.
 */
public class SpatialVector {
    private float x;
    private float y;
    private float z;


    public SpatialVector(PointVector firstPoint, PointVector secondPoint) {
        this.x = secondPoint.getPointX() - firstPoint.getPointX();
        this.y = secondPoint.getPointY() - firstPoint.getPointY();
        this.z = secondPoint.getPointZ() - firstPoint.getPointZ();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
