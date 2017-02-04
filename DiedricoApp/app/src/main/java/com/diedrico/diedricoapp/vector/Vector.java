package com.diedrico.diedricoapp.vector;

/**
 * Created by amil101 on 5/02/16.
 */
public class Vector {

    private double x;           //the x of the vector
    private double y;           //the y of the vector

    PointVector firstPoint;
    PointVector secondPoint;

    public Vector(PointVector firstPoint, PointVector secondPoint) {
        this.x = (secondPoint.getPointX()) - (firstPoint.getPointX());
        this.y = (secondPoint.getPointY()) - (firstPoint.getPointY());

        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
    }


    public double getModule(){
        double module = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

        return module;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
