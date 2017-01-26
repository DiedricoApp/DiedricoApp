package com.diedrico.diedricoapp;

import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.diedrico.diedricoapp.scrollabletabs.BaseFragment;
import com.diedrico.diedricoapp.vector.Diedrico;


/**
 * Created by amil101 on 12/08/16.
 */
public class ProjectionFragment extends BaseFragment {

    static final String TAG = "tag.ProjectionFragment";
    private static final String FRICTION_PATTERN = "Current: %sF";

    private GLSurfaceView mGLView;          //SurfaceView of OpenGL
    MyGLRendererCamera renderer = new MyGLRenderer(new Diedrico(null, null, null));            //The main renderer, it has to be initialized because the fragment can die. With changeRenderer we will change the renderer

    LinearLayout layoutForGL;

    float initX;      //Is the value of the X coordenate when we press the screen
    float initY;      //The Y

    float moveX;    //Is the value of the X movement
    float moveY;    //Is the value of the Y movement

    boolean pressed;            //if the OpenGL is pressed
    long currentTime;           //The time of the thread, for rotate the camera if the user don't press the screen

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

    public static ProjectionFragment newInstance() {
        final Bundle bundle = new Bundle();

        final ProjectionFragment fragment = new ProjectionFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle sis) {
        super.onCreate(sis);

        final View view = inflater.inflate(R.layout.fragment_projection, parent, false);

        layoutForGL = (LinearLayout) view.findViewById(R.id.layoutForSurfaceViewtabs);

        mGLView = new MyGLSurfaceView(getContext(), renderer);

        threadTime();               //start the thread, for rotate the camera if the user don't press the screen
        pressed = false;

        mGLView.setOnTouchListener(listenerForCamera());

        layoutForGL.addView(mGLView);
        mGLView.requestRender();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle sis) {
        super.onViewCreated(view, sis);

    }


    @Override
    public boolean canScrollVertically(int direction) {
        return mListView != null && mListView.canScrollVertically(direction);
    }

    @Override
    public void onFlingOver(int y, long duration) {
        if (mListView != null) {
            mListView.smoothScrollBy(y, (int) duration);
        }
    }
    @Override
    public CharSequence getTitle(Resources r) {
        return r.getString(R.string.projection);
    }

    @Override
    public String getSelfTag() {
        return TAG;
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
