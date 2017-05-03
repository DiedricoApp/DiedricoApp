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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;

import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.diedrico.diedricoapp.opengl.MyGLRenderer;
import com.diedrico.diedricoapp.opengl.models.BienvenidoModel;
import com.diedrico.diedricoapp.opengl.models.FirstQuadrantModel;
import com.diedrico.diedricoapp.opengl.models.FourthQuadrantModel;
import com.diedrico.diedricoapp.opengl.models.Model;
import com.diedrico.diedricoapp.opengl.models.SecondQuadrantModel;
import com.diedrico.diedricoapp.opengl.models.ThirdQuadrantModel;
import com.diedrico.diedricoapp.vector.Diedrico;
import com.diedrico.diedricoapp.vector.PointVector;
import com.diedrico.diedricoapp.vector.LineVector;
import com.diedrico.diedricoapp.vector.PlaneVector;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private Toolbar toolbarTabs;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    DrawerLayout drawer;        //The Navigation View
    private CoordinatorLayout coordinatorLayout;        //The layout with the tabLayout, we need to inflate the content we want to show

    ExpandableListAdapter listAdapter;      //adapter of listExpandable, its a custom adapter
    ExpandableListView expListView;     //To access the expandable list View
    List<String> header;        //List of items that are expandable
    HashMap<String, List<String>> listDataChild;        //Childs of the expandable
    ListView listView;      //To put the lists that are not expandable
    List<String> itemsListView;     //Items that are not expandable

    //Fragments
    ProjectionFragment projectionFragment;
    DiedricoFragment diedricoFragment;
    ExplanationFragment explanationFragment;

    Diedrico diedrico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inflate the content in the coordinator layout
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.content_main, coordinatorLayout);

        //The toolbar of the app (the top one, with the title)
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Now we have to put the button in the toolbar and setup the NavigationView
        drawer = (DrawerLayout) findViewById(R.id.drawer_tabs_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Create, prepare and set the listener to the expandable List View(in the navigation View)
        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        prepareExapandableListNavigationView();     //To load the lists
        listAdapter = new ExpandableListAdapter(this, header, listDataChild);
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(onExpandableClick());

        //Prepare the ListView, load the items from itemsListView (in the navigation View)
        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.group_expandable, R.id.expandableGroupText, itemsListView);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(onListViewItemListener());

        //The toolbar for the tabs
        toolbarTabs = (Toolbar) findViewById(R.id.toolbar_tabs);
        setSupportActionBar(toolbarTabs);

        //Setup the ViewPager
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        //Setup the tabLayout in the toolbarTabs
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {      //Load the fragments to the ViewPager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(projectionFragment = ProjectionFragment.newInstance(), "Espacio");
        adapter.addFragment(diedricoFragment = DiedricoFragment.newInstance(), "Diédrico");
        adapter.addFragment(explanationFragment = ExplanationFragment.newInstance(), "Explicacion");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {       //To open or close the NavigationView on start
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_tabs_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public ExpandableListView.OnChildClickListener onExpandableClick(){         //Setup the listener to the expandable List View
        return new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                List<PointVector> pointVectors = new ArrayList<>();
                List<LineVector> lineVectors = new ArrayList<>();
                List<PlaneVector> planeVectors = new ArrayList<>();
                List<Model> models = new ArrayList<>();

                switch(groupPosition){
                    case 0:
                        switch (childPosition){
                            case 0:     //The user pressed welcome
                                explanationFragment.setExplanation(R.string.welcome, R.string.firtstext);
                                models.add(new BienvenidoModel(new PointVector(0.5f, 0.5f, 0.0f)));

                                break;
                            case 1:     //the user pressed components of diedrico
                                explanationFragment.setExplanation(R.string.components, R.string.edges);
                                models.add(new FirstQuadrantModel(new PointVector(1.0f, 1.0f, -0.5f)));
                                models.add(new SecondQuadrantModel(new PointVector(-1.0f, 1.0f, -0.5f)));
                                models.add(new ThirdQuadrantModel(new PointVector(-1.0f, -1.0f, -0.5f)));
                                models.add(new FourthQuadrantModel(new PointVector(1.0f, -1.0f, -0.5f)));

                                break;
                            case 2:     //the user pressed edges
                                explanationFragment.setExplanation(R.string.edges, R.string.firtstext);
                                break;
                        }
                        break;
                    case 1:
                        switch (childPosition){
                            case 0:     //The user pressed point projection
                                pointVectors.add(new PointVector(0.75f, 0.25f, 0.0f));
                                pointVectors.add(new PointVector(0.4f, 0.6f, 0.0f));

                                explanationFragment.setExplanation(R.string.pointProjection, R.string.pointProjectionInfo);
                                break;
                            case 1:     ////The user pressed line projection
                                lineVectors.add(new LineVector(0.0f, 0.8f, 0.4f, 0.9f, 0.0f, -0.4f));

                                explanationFragment.setExplanation(R.string.lineProjection, R.string.lineProjectionInfo);
                                break;
                        }
                        break;
                    case 2:
                        switch (childPosition) {
                            case 0:     //the user pressed crosswideLine
                                lineVectors.add(new LineVector(0.0f, 0.8f, 0.4f, 0.9f, 0.0f, -0.4f));

                                explanationFragment.setExplanation(R.string.crosswideLine, R.string.crosswideLineInfo);
                                break;
                            case 1:     //the user pressed horizontal line
                                lineVectors.add(new LineVector(0.9f, 0.0f, 0.4f, 0.9f, 0.9f, -0.4f));

                                explanationFragment.setExplanation(R.string.horizontalLine, R.string.horizontalLineInfo);
                                break;
                            case 2:     //the user pressed frontal line
                                lineVectors.add(new LineVector(0.0f, 0.5f, 0.4f, 0.9f, 0.5f, -0.4f));

                                explanationFragment.setExplanation(R.string.frontalLine, R.string.frontalLineInfo);
                                break;
                            case 3:     //the user pressed rigid Line
                                lineVectors.add(new LineVector(0.5f, 0.0f, 0.0f, 0.5f, 0.9f, 0.0f));

                                explanationFragment.setExplanation(R.string.rigidLine, R.string.rigidLineInfo);
                                break;
                            case 4:     //the user pressed vertical lne
                                lineVectors.add(new LineVector(0.0f, 0.5f, 0.0f, 0.9f, 0.5f, 0.0f));

                                explanationFragment.setExplanation(R.string.verticalLine, R.string.verticalLineInfo);
                                break;
                            case 5:     //the user pressed ground line parallel line
                                lineVectors.add(new LineVector(0.5f, 0.5f, 0.4f, 0.5f, 0.5f, -0.4f));

                                explanationFragment.setExplanation(R.string.groundLineParallelLine, R.string.groundLineParallelLineInfo);
                                break;
                            case 6:     //the user pressed profileLine
                                lineVectors.add(new LineVector(0.9f, 0.0f, 0.0f, 0.0f, 0.9f, 0.0f));

                                explanationFragment.setExplanation(R.string.profileLine, R.string.profileLine);
                                break;
                            case 7:     //the user pressed ground line cutted line
                                lineVectors.add(new LineVector(0.0f, 0.0f, 0.0f, 0.9f, 0.9f, 0.0f));

                                explanationFragment.setExplanation(R.string.groundLineCuttedLine, R.string.groundLineCuttedLineInfo);
                                break;
                        }
                        break;
                    case 3:
                        switch (childPosition) {
                            case 0:     //the user pressed crosswide plane
                                planeVectors.add(new PlaneVector(new PointVector(0.0f, 0.0f, 0.4f), new PointVector(0.0f, 1.0f, -0.5f), new PointVector(0.5f, 0.5f, -0.5f), new PointVector(1.0f, 0.0f, -0.5f)));

                                explanationFragment.setExplanation(R.string.crosswidePlane, R.string.crosswidePlanoInfo);
                                break;
                            case 1:     //the user pressed horizontal plane
                                planeVectors.add(new PlaneVector(new PointVector(0.0f, 0.5f, -0.5f), new PointVector(0.0f, 0.5f, 0.5f), new PointVector(0.9f, 0.5f, 0.5f), new PointVector(0.9f, 0.5f, -0.5f)));

                                explanationFragment.setExplanation(R.string.horizontalPlane, R.string.horizontalPlaneInfo);
                                break;
                            case 2:     //the user pressed frontal plane
                                planeVectors.add(new PlaneVector(new PointVector(0.5f, 0.0f, 0.5f), new PointVector(0.5f, 0.0f, -0.5f), new PointVector(0.5f, 1.0f, -0.5f), new PointVector(0.5f, 1.0f, 0.5f)));

                                explanationFragment.setExplanation(R.string.frontalPlane, R.string.frontalPlaneInfo);
                                break;
                            case 3:     //the user pressed horizontal projection plane
                                planeVectors.add(new PlaneVector(new PointVector(0.0f, 1.0f, 0.4f), new PointVector(0.0f, 0.0f, 0.4f), new PointVector(0.5f, 0.0f, -0.5f), new PointVector(0.5f, 1.0f, -0.5f)));

                                explanationFragment.setExplanation(R.string.horizontalProjectionPlane, R.string.horizontalProjectionPlaneInfo);
                                break;
                            case 4:     //the user pressed vertical projection plane
                                planeVectors.add(new PlaneVector(new PointVector(0.0f, 0.0f, 0.4f), new PointVector(0.0f, 1.0f, -0.5f), new PointVector(1.0f, 1.0f, -0.5f), new PointVector(1.0f, 0.0f, 0.4f)));

                                explanationFragment.setExplanation(R.string.verticalProjectionPlane, R.string.verticalProjectionPlaneInfo);
                                break;
                            case 5:     //the user pressed groundLineParallelPlane
                                planeVectors.add(new PlaneVector(new PointVector(0.5f, 0.0f, 0.5f), new PointVector(0.5f, 0.0f, -0.5f), new PointVector(0.0f, 0.7f, -0.5f), new PointVector(0.0f, 0.7f, 0.5f)));

                                explanationFragment.setExplanation(R.string.groundLineParallelPlane, R.string.groundLineParallelPlaneInfo);
                                break;
                            case 6:     //the user pressed groundline cutted plane
                                planeVectors.add(new PlaneVector(new PointVector(0.0f, 0.0f, 0.5f), new PointVector(1.0f, 1.0f, 0.5f), new PointVector(1.0f, 1.0f, -0.5f), new PointVector(0.0f, 0.0f, -0.5f)));

                                explanationFragment.setExplanation(R.string.groundLineCuttedPlane, R.string.groundLineCuttedPlaneInfo);
                                break;
                            case 7:     //the user pressed profile plane
                                planeVectors.add(new PlaneVector(new PointVector(0.0f, 0.0f, 0.0f), new PointVector(1.0f, 0.0f, 0.0f), new PointVector(1.0f, 1.0f, 0.0f), new PointVector(0.0f, 1.0f, 0.0f)));

                                explanationFragment.setExplanation(R.string.profilePlane, R.string.profilePlaneInfo);
                                break;
                        }
                        break;
                    case 4:
                        break;
                }

                diedrico = new Diedrico(pointVectors, lineVectors, planeVectors, models);       //To put the renderer with the points lines and planes (OpenGL)

                projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
                projectionFragment.newInstance();

                diedricoFragment.setDiedrico(diedrico);         //To create the diedrico (projection)

                drawer.closeDrawer(GravityCompat.START);        //Closing the navigation View when the user select an option

                return false;
            }
        };
    }

    private AdapterView.OnItemClickListener onListViewItemListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int item, long l) {
                switch (item){
                    case 0:
                        //The first item of listView, the camera
                        startCameraActivity();
                        break;
                }
            }
        };
    }

    private void startCameraActivity(){
        Intent intent = new Intent(this, CameraActivity.class);
        this.startActivity(intent);
    }

    private void prepareExapandableListNavigationView(){        //Prepare the data of the expadable list view that is in the R.array
        header = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        itemsListView = new ArrayList<>();      //To store the data of listView (not the expandable)

        //Adding child data
        header = Arrays.asList(getResources().getStringArray(R.array.menu));

        listDataChild.put(header.get(0), Arrays.asList(getResources().getStringArray(R.array.getStarted)));  //Header, Child data
        listDataChild.put(header.get(1), Arrays.asList(getResources().getStringArray(R.array.projections)));
        listDataChild.put(header.get(2), Arrays.asList(getResources().getStringArray(R.array.typeOfLines)));
        listDataChild.put(header.get(3), Arrays.asList(getResources().getStringArray(R.array.typeOfPlanes)));

        itemsListView.add(this.getString(R.string.camera));       //Add the camera view for ListView, the reason is that cameraView is no expandable but it must be in the nav view
    }

}
