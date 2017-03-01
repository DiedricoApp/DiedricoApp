package com.diedrico.diedricoapp.vector;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amil101 on 22/07/16.
 */
public class PlaneVector implements Parcelable{

    private PointVector p1;
    private PointVector p2;
    private PointVector p3;
    private PointVector p4;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedArray(new PointVector[]{
                p1,
                p2,
                p3,
                p4
        }, 0);
    }

    public static final Parcelable.Creator<PlaneVector> CREATOR
            = new Parcelable.Creator<PlaneVector>() {
        public PlaneVector createFromParcel(Parcel in) {
            return new PlaneVector(in);
        }

        public PlaneVector[] newArray(int size) {
            return new PlaneVector[size];
        }
    };

    private PlaneVector(Parcel in) {
        PointVector pointVectors[] = in.createTypedArray(PointVector.CREATOR);
        p1 = pointVectors[0];
        p2 = pointVectors[1];
        p3 = pointVectors[2];
        p4 = pointVectors[3];
    }

    public PlaneVector(PointVector p1, PointVector p2, PointVector p3, PointVector p4){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    public PointVector getP1() {
        return p1;
    }

    public PointVector getP2() {
        return p2;
    }

    public PointVector getP3() {
        return p3;
    }

    public PointVector getP4() {
        return p4;
    }
}
