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
public class LineVectorEquation {
    PointVector point;
    SpatialVector vector;

    private float lambda;

    public LineVectorEquation(PointVector point, SpatialVector vector) {
        this.point = point;
        this.vector = vector;
    }

    public float getX(float lambda) {
        return (point.getPointX() + (vector.getX() * lambda));
    }

    public float getY(float lambda) {
        return (point.getPointY() + (vector.getY() * lambda));
    }

    public float getZ(float lambda) {
        return (point.getPointZ() + (vector.getZ() * lambda));
    }
}
