package com.diedrico.diedricoapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.diedrico.diedricoapp.opengl.MyGLRenderer;
import com.diedrico.diedricoapp.opengl.MyGLRendererEdges;
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

        //The toolbar of the app
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Now we have to put the button in the toolbar and setup the NavigationView
        drawer = (DrawerLayout) findViewById(R.id.drawer_tabs_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        expListView = (ExpandableListView) findViewById(R.id.expandableListView);

        prepareExapandableListNavigationView();     //To load the lists

        listAdapter = new ExpandableListAdapter(this, header, listDataChild);
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(onExpandableClick());


        //Prepare the ListView, load the items from itemsListView

        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.group_expandable, R.id.expandableGroupText, itemsListView);
        listView.setAdapter(arrayAdapter);

        //The toolbar for the tabs
        toolbarTabs = (Toolbar) findViewById(R.id.toolbar_tabs);
        setSupportActionBar(toolbarTabs);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(projectionFragment = ProjectionFragment.newInstance(), "ONE");
        adapter.addFragment(diedricoFragment = DiedricoFragment.newInstance(), "TWO");
        adapter.addFragment(explanationFragment = ExplanationFragment.newInstance(), "THREE");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_tabs_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public ExpandableListView.OnChildClickListener onExpandableClick(){
        return new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                List<PointVector> pointVectors = new ArrayList<>();
                List<LineVector> lineVectors = new ArrayList<>();
                List<PlaneVector> planeVectors = new ArrayList<>();

                switch(groupPosition){
                    case 0:
                        switch (childPosition){
                            case 0:     //The user pressed welcome
                                explanationFragment.setExplanation(R.string.welcome, R.string.firtstext);
                                break;
                            case 1:     //the user pressed components of diedrico
                                explanationFragment.setExplanation(R.string.components, R.string.edges);
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

                diedrico = new Diedrico(pointVectors, lineVectors, planeVectors);

                projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
                projectionFragment.newInstance();

                diedricoFragment.setDiedrico(diedrico);

                drawer.closeDrawer(GravityCompat.START);        //Closing the navigation View

                return false;
            }
        };
    }

    private void prepareExapandableListNavigationView(){
        header = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        itemsListView = new ArrayList<>();

        //Adding child data
        header = Arrays.asList(getResources().getStringArray(R.array.menu));

        listDataChild.put(header.get(0), Arrays.asList(getResources().getStringArray(R.array.getStarted)));  //Header, Child data
        listDataChild.put(header.get(1), Arrays.asList(getResources().getStringArray(R.array.projections)));
        listDataChild.put(header.get(2), Arrays.asList(getResources().getStringArray(R.array.typeOfLines)));
        listDataChild.put(header.get(3), Arrays.asList(getResources().getStringArray(R.array.typeOfPlanes)));

        itemsListView.add(getResources().getString(R.string.camera));       //Add the camera view for ListView, the reason is that cameraView is no expandable but it must be in the nav view
    }

}
