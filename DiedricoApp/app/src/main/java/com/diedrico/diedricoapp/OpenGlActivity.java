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

package com.diedrico.diedricoapp;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.diedrico.diedricoapp.opengl.MyGLRenderer;
import com.diedrico.diedricoapp.opengl.MyGLRendererCamera;
import com.diedrico.diedricoapp.opengl.MyGLSurfaceView;
import com.diedrico.diedricoapp.opengl.models.Model;
import com.diedrico.diedricoapp.vector.Diedrico;
import com.diedrico.diedricoapp.vector.LineVector;
import com.diedrico.diedricoapp.vector.PlaneVector;
import com.diedrico.diedricoapp.vector.PointVector;

import java.util.List;



public class OpenGlActivity extends Activity {

    private GLSurfaceView mGLView;
    MyGLRendererCamera renderer = new MyGLRenderer(new Diedrico(null, null, null, (List<Model>) null));            //The main renderer, it has to be initialized because the fragment can die. With changeRenderer we will change the renderer

    List<PointVector> pointVectors;
    List<LineVector> lineVectors;
    List<PlaneVector> planeVectors;

    Intent intent;

    float initX;      //Is the value of the X coordenate when we press the screen
    float initY;      //The Y

    float moveX;    //Is the value of the X movement
    float moveY;    //Is the value of the Y movement

    boolean pressed;            //if the OpenGL is pressed
    long currentTime;           //The time of the thread, for rotate the camera if the user don't press the screen

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();

        pointVectors = intent.getParcelableArrayListExtra("points");
        lineVectors = intent.getParcelableArrayListExtra("lines");
        planeVectors = intent.getParcelableArrayListExtra("planes");

        threadTime();               //start the thread, for rotate the camera if the user don't press the screen
        pressed = false;

        Diedrico diedrico = new Diedrico(pointVectors, lineVectors, planeVectors, (List<Model>) null);       //To put the renderer with the points lines and planes (OpenGL)

        mGLView = new MyGLSurfaceView(this, new MyGLRenderer(diedrico));
        mGLView.setOnTouchListener(listenerForCamera());
        setContentView(mGLView);
    }

    public View.OnTouchListener listenerForCamera(){
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        renderer.setNotPressed(false);

                        initX = event.getX();
                        initY = event.getY();

                        Log.i("toco", "X " + event.getX() + " Y " + event.getY());

                        pressed = true;
                        return true;


                    case MotionEvent.ACTION_MOVE:
                        moveX = (event.getX() - initX);
                        moveY = -(event.getY() - initY);

                        renderer.setCamera(moveX, moveY, 0);

                        initX = event.getX();
                        initY = event.getY();

                        return true;

                    case MotionEvent.ACTION_UP:
                        pressed = false;
                        threadTime();
                        return true;
                }
                return false;
            }
        };
    }

    public void threadTime(){
        new Thread(new Runnable() {
            public void run() {
                if(pressed == false) {
                    currentTime = SystemClock.currentThreadTimeMillis();
                    while (pressed == false) {
                        if((SystemClock.currentThreadTimeMillis() - currentTime) > 3000){
                            renderer.setNotPressed(true);
                            break;
                        }
                    }
                }
            }
        }).start();
    }
}