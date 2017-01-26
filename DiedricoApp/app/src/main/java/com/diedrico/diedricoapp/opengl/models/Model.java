package com.diedrico.diedricoapp.opengl.models;

/**
 * Created by amil101 on 3/08/16.
 */
public class Model {
    private int NumVerts;

    private float[] Verts;

    public Model(int Numverts, float[] Verts){
        this.NumVerts = Numverts;
        this.Verts = Verts;
    }

    public int getNumVerts() {
        return NumVerts;
    }

    public float[] getVerts() {
        return Verts;
    }
}
