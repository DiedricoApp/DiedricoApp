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

package com.diedrico.diedricoapp.opengl;

import android.opengl.GLSurfaceView;

import com.diedrico.diedricoapp.vector.LineVector;
import com.diedrico.diedricoapp.vector.PlaneVector;
import com.diedrico.diedricoapp.vector.PointVector;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * Created by amil101 on 26/06/16.
 */
public class MyGLRendererCamera implements GLSurfaceView.Renderer{
    public static float viewX = 0.0f;
    public static float viewY = 0.0f;

    public static boolean notPressed;

    public static List<PointVector> pointVectors;
    public static List<LineVector> lineVectors;
    public static List<PlaneVector> planeVectors;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }

    public void setCamera(float x, float y, float z){
        viewX = viewX + x;
        viewY = viewY + y;
        //eyeZ = eyeZ + z;
    }

    public void setNotPressed(boolean notPressed){
        this.notPressed = notPressed;
    }

    //Used for pass the coords from the picture with the camera
    public void setPointCoords(List<PointVector> pointCoords) {
        this.pointVectors = pointCoords;

    }

    //Used for pass the coords from the picture with the camera
    public void setLineVectors(List<LineVector> lineVectors) {
        this.lineVectors = lineVectors;
    }

    //Used for pass the coords from the picture with the camera
    public void setPlaneVectors(List<PlaneVector> planeVectors) {
        this.planeVectors = planeVectors;
    }
}