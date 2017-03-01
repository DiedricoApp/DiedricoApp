package com.diedrico.diedricoapp;

import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.diedrico.diedricoapp.opengl.MyGLRenderer;
import com.diedrico.diedricoapp.opengl.MyGLRendererCamera;
import com.diedrico.diedricoapp.opengl.MyGLSurfaceView;
import com.diedrico.diedricoapp.vector.Diedrico;
import com.diedrico.diedricoapp.vector.LineVector;
import com.diedrico.diedricoapp.vector.PlaneVector;
import com.diedrico.diedricoapp.vector.PointVector;

import java.util.List;


/**
 * Created by amil101 on 12/08/16.
 */
public class ProjectionFragment extends Fragment {

    private GLSurfaceView mGLView;          //SurfaceView of OpenGL
    MyGLRendererCamera renderer = new MyGLRenderer(new Diedrico(null, null, null));            //The main renderer, it has to be initialized because the fragment can die. With changeRenderer we will change the renderer

    LinearLayout layoutForGL;

    float initX;      //Is the value of the X coordenate when we press the screen
    float initY;      //The Y

    float moveX;    //Is the value of the X movement
    float moveY;    //Is the value of the Y movement

    boolean pressed;            //if the OpenGL is pressed
    long currentTime;           //The time of the thread, for rotate the camera if the user don't press the screen


    public ProjectionFragment(){
        //Empty construct
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGLView != null){
            mGLView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGLView != null){
            mGLView.onResume();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle sis) {
        super.onCreate(sis);


        final View view = inflater.inflate(R.layout.fragment_projection, parent, false);

        layoutForGL = (LinearLayout) view.findViewById(R.id.layoutForSurfaceViewtabs);

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null) {
            List<PointVector> comingPointVectors = extras.getParcelableArrayList("pointVectors");
            List<LineVector> comingLineVectors = extras.getParcelableArrayList("lineVectors");
            List<PlaneVector> comingPlaneVectors = extras.getParcelableArrayList("planeVectors");

            Diedrico diedrico = new Diedrico(comingPointVectors, comingLineVectors, comingPlaneVectors);       //To put the renderer with the points lines and planes (OpenGL)

            mGLView = new MyGLSurfaceView(getContext(), new MyGLRenderer(diedrico));
        }
        else{
            mGLView = new MyGLSurfaceView(getContext(), renderer);
        }

        threadTime();               //start the thread, for rotate the camera if the user don't press the screen
        pressed = false;

        mGLView.setOnTouchListener(listenerForCamera());

        layoutForGL.addView(mGLView);

        mGLView.requestRender();

        return view;
    }

    public static ProjectionFragment newInstance() {
        final ProjectionFragment fragment = new ProjectionFragment();

        return fragment;
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


    public void changeRenderer(MyGLRendererCamera renderer){

        this.renderer = renderer;           // To change the renderer if it dies.

        mGLView = new MyGLSurfaceView(getContext(), renderer);
        mGLView.setOnTouchListener(listenerForCamera());

        //Put the diedrico projection to the layout and the renderer
        layoutForGL.removeAllViews();
        layoutForGL.addView(mGLView);

        mGLView.requestRender();
    }

}
