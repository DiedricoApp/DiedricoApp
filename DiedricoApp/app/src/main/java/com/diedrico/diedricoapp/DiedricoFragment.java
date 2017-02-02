package com.diedrico.diedricoapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.diedrico.diedricoapp.vector.Diedrico;

/**
 * Created by amil101 on 15/08/16.
 */
public class DiedricoFragment extends Fragment {

    ImageView diedrico;            //ImageView
    CreateDiedrico createDiedrico;      //For changing the pictures of the imageView

    public DiedricoFragment(){
        //Empty constructor
    }

    public static DiedricoFragment newInstance() {
        final DiedricoFragment fragment = new DiedricoFragment();

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle sis) {

        final View view = inflater.inflate(R.layout.fragment_diedrico, parent, false);

        diedrico = (ImageView) view.findViewById(R.id.projection);
        createDiedrico = new CreateDiedrico(diedrico);

        return view;
    }

    public void setDiedrico(Diedrico diedrico){
        if(!diedrico.getPoints().isEmpty()){
            this.createDiedrico.addDiedricoPoints(diedrico.getPoints());
        }

        if(!diedrico.getLines().isEmpty()){
            this.createDiedrico.addDiedricoLines(diedrico.getLines());
        }

        if(!diedrico.getPlanes().isEmpty()){
            this.createDiedrico.addPlanes(diedrico.getPlanes());
        }
    }
}
