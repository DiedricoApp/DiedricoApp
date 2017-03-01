package com.diedrico.diedricoapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.diedrico.diedricoapp.vector.Diedrico;
import com.diedrico.diedricoapp.vector.LineVector;
import com.diedrico.diedricoapp.vector.PlaneVector;
import com.diedrico.diedricoapp.vector.PointVector;

import java.util.List;

/**
 * Created by amil101 on 15/08/16.
 */
public class DiedricoFragment extends Fragment {

    ImageView diedrico;            //imageView
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

        //If there is a coming intent (from MenuPicActivity) we have to catch it and put the content
        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null) {

            List<PointVector> comingPointVectors = extras.getParcelableArrayList("pointVectors");
            List<LineVector> comingLineVectors = extras.getParcelableArrayList("lineVectors");
            List<PlaneVector> comingPlaneVectors = extras.getParcelableArrayList("planeVectors");

            setDiedrico(comingPointVectors, comingLineVectors, comingPlaneVectors);         //To create the diedrico (projection)
        }

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

    public void setDiedrico(List<PointVector> pointVectors, List<LineVector> lineVectors, List<PlaneVector> planeVectors){
        if(!pointVectors.isEmpty()){
            this.createDiedrico.addDiedricoPoints(pointVectors);
        }

        if(!lineVectors.isEmpty()){
            this.createDiedrico.addDiedricoLines(lineVectors);
        }

        if(!planeVectors.isEmpty()){
            this.createDiedrico.addPlanes(planeVectors);
        }
    }
}
