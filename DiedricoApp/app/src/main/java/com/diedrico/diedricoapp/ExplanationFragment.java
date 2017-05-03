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

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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


        //If there is a coming intent (from MenuPicActivity) we have to catch it and put the content
        Bundle extras = getActivity().getIntent().getExtras();

        if(extras != null && extras.getInt("title") != 0 && extras.getInt("explanation") != 0) {
            textViewTitle.setText(extras.getInt("title"));
            textViewExplanation.setText(extras.getInt("explanation"));
        }
        else{
            textViewTitle.setText(title);
            textViewExplanation.setText(explanation);
        }
            return view;
    }

    public static ExplanationFragment newInstance() {
        final ExplanationFragment fragment = new ExplanationFragment();

        return fragment;
    }

    public void setExplanation(int title, int exp){
        this.title = title;
        this.explanation = exp;

        if(super.isVisible()){
            textViewTitle.setText(title);
            textViewExplanation.setText(explanation);
        }

    }
}
