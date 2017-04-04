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
 * Created by amil101 on 23/03/16.
 */
public class LineVector implements Parcelable {
    float lineYA;
    float lineXA;
    float lineZA;

    float lineYB;
    float lineXB;
    float lineZB;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeFloatArray(new float[]{this.lineYA,
                this.lineXA,
                this.lineZA,
                this.lineYB,
                this.lineXB,
                this.lineZB});
    }

    public static final Creator<LineVector> CREATOR = new Creator<LineVector>() {
        @Override
        public LineVector createFromParcel(Parcel in) {
            return new LineVector(in);
        }

        @Override
        public LineVector[] newArray(int size) {
            return new LineVector[size];
        }
    };

    public LineVector(Parcel in){
        float[] data = new float[6];

        in.readFloatArray(data);
        this.lineYA = data[0];
        this.lineXA = data[1];
        this.lineZA = data[2];

        this.lineYB = data[3];
        this.lineXB = data[4];
        this.lineZB = data[5];

    }

    public LineVector(float LineYA, float LineXA, float LineYB, float LineXB){
        if(lineXA < lineXB){
            this.lineYA = LineYA;
            this.lineXA = LineXA;

            this.lineYB = LineYB;
            this.lineXB = LineXB;
        }
        else{
            this.lineYA = LineYB;
            this.lineXA = LineXB;

            this.lineYB = LineYA;
            this.lineXB = LineXA;
        }


    }
    public LineVector(float LineYA, float LineXA, float LineZA, float LineYB, float LineXB, float LineZB){

        this.lineYA = LineYA;
        this.lineXA = LineXA;
        this.lineZA = LineZA;

        this.lineYB = LineYB;
        this.lineXB = LineXB;
        this.lineZB = LineZB;
    }

    public double getModuleTwoDimensionalVector(){
        float x = lineXB - lineXA;
        float y = lineYB - lineYA;

        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public double getYEquation(double x){
        return (((x - lineXA)/(lineXB-lineXA))*(lineYB-lineYA)) + lineYA;
    }

    public float getLineZB() {
        return lineZB;
    }

    public void setLineZB(float lineZB) {
        this.lineZB = lineZB;
    }

    public float getLineZA() {
        return lineZA;
    }

    public void setLineZA(float lineZA) {
        this.lineZA = lineZA;
    }

    public float getLineYB() {
        return lineYB;
    }

    public void setLineYB(float lineYB) {
        this.lineYB = lineYB;
    }

    public float getLineYA() {
        return lineYA;
    }

    public void setLineYA(float lineYA) {
        this.lineYA = lineYA;
    }

    public float getLineXB() {
        return lineXB;
    }

    public void setLineXB(float lineXB) {
        this.lineXB = lineXB;
    }

    public float getLineXA() {
        return lineXA;
    }

    public void setLineXA(float lineXA) {
        this.lineXA = lineXA;
    }
}
