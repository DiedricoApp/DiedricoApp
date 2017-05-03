package com.diedrico.diedricoapp.opengl.models;

import com.diedrico.diedricoapp.vector.PointVector;

/**
 * Created by amil101 on 3/08/16.
 */
public interface Model {
    public PointVector getCoords();
    public int getNumVerts();
    public float[] getVerts();

}
