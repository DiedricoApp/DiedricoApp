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
