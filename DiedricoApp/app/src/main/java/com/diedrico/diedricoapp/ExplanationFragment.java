package com.diedrico.diedricoapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by amil101 on 17/12/16.
 */
public class ExplanationFragment extends Fragment {

    private int title = R.string.welcome;
    private int explanation = R.string.firtstext;
    TextView textViewTitle;
    TextView textViewExplanation;

    public ExplanationFragment(){
        //Required empty public construct
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle sis) {

        final View view = inflater.inflate(R.layout.fragment_explanation, parent, false);

        textViewTitle = (TextView) view.findViewById(R.id.explanationTitle);
        textViewExplanation = (TextView) view.findViewById(R.id.explanationText);
        textViewTitle.setText(title);
        textViewExplanation.setText(explanation);

        return view;
    }

    public static ExplanationFragment newInstance() {
        final ExplanationFragment fragment = new ExplanationFragment();

        return fragment;
    }

    public void setExplanation(int title, int exp){
        this.title = title;
        this.explanation = exp;
    }

}
