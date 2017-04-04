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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amil101 on 13/03/16.
 */
public class PointVector implements Parcelable {

    private float pointY;
    private float pointX;
    private float pointZ;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeFloatArray(new float[]{this.pointY,
                                          this.pointX,
                                          this.pointZ});
    }

    public static final Creator<PointVector> CREATOR
            = new Creator<PointVector>() {
        public PointVector createFromParcel(Parcel in) {
            return new PointVector(in);
        }
        public PointVector[] newArray(int size) {
            return new PointVector[size];
        }
    };

    public PointVector(Parcel in) {
        float[] data = new float[3];

        in.readFloatArray(data);
        this.pointY = data[0];
        this.pointX = data[1];
        this.pointZ = data[2];
    }

    public PointVector(float pointX, float pointY, float pointZ) {
        this.pointX = pointX;
        this.pointY = pointY;
        this.pointZ = pointZ;
    }

    public PointVector(float pointX, float pointY){
        this.pointX = pointX;
        this.pointY = pointY;
    }

    public PointVector getMidPoint(PointVector secondPoint){
        float resultX = (this.pointX + secondPoint.getPointX())/2;
        float resultY = (this.pointY + secondPoint.getPointY())/2;
        float resultZ = (this.pointZ + secondPoint.getPointZ())/2;

        return new PointVector(resultX, resultY, resultZ);
    }

    public float getPointX() {
        return pointX;
    }

    public void setPointX(float pointX) {
        this.pointX = pointX;
    }

    public float getPointY() {
        return pointY;
    }

    public void setPointY(float pointY) {
        this.pointY = pointY;
    }

    public float getPointZ() {
        return pointZ;
    }

    public void setPointZ(float pointZ) {
        this.pointZ = pointZ;
    }
}
