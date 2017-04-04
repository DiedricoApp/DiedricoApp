/*Author: Fran Acién
* Copyright (C) 2017  Fran Acién
*
* This file is part of DiedricoApp.
*
* DiedricoApp is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License.
*
* DiedricoApp is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
*/

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
