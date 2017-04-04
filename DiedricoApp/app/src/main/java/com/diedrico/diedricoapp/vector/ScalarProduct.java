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
 * Created by amil101 on 28/03/16.
 */
public class ScalarProduct {
    Vector AB;
    Vector CD;
    public ScalarProduct(Vector AB, Vector CD){
        this.AB = AB;
        this.CD = CD;
    }

    public double angle() {
        double angle = Math.acos(product() / Math.abs(AB.getModule() * CD.getModule()));
        return angle;
    }

    public double product(){
        double product = (AB.getX() * CD.getX()) + (AB.getY() * CD.getY());
        return product;
    }

    public double getHeight(){
        double height = Math.sin(angle()) * CD.getModule();
        return height;
    }

    public double getLength(){
        double length= Math.cos(angle()) * CD.getModule();
        return length;
    }

}
