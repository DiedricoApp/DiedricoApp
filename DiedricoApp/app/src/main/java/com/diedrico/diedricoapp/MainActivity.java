package com.diedrico.diedricoapp;

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
import android.view.MenuItem;
import android.widget.ExpandableListView;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbarTabs;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> header;
    HashMap<String, List<String>> listDataChild;

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_tabs_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();


        //Prepare the navigation view with the expandable ListView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_tabs_view);
        navigationView.setNavigationItemSelectedListener(this);

        expListView = (ExpandableListView) findViewById(R.id.expandableListView);

        prepareExapandableListNavigationView();     //To load the lists

        listAdapter = new ExpandableListAdapter(this, header, listDataChild);
        expListView.setAdapter(listAdapter);

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


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
/*
        if (id == R.id.welcome) {
            diedrico = new Diedrico(null, null, null);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.firtstext);
            explanationFragment.newInstance();
        } else if (id == R.id.components) {
            diedrico = new Diedrico(null, null, null);

            projectionFragment.changeRenderer(new MyGLRendererEdges(false, null));
            projectionFragment.newInstance();


            diedricoFragment.setDiedrico(new Diedrico(null, null, null));

            explanationFragment.setExplanation(R.string.edges);
            explanationFragment.newInstance();
        } else if (id == R.id.edges) {
            diedrico = new Diedrico(null, null, null);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.firtstext);
            explanationFragment.newInstance();
        } else if (id == R.id.pointProjection) {

            List<PointVector> pointVectors = new ArrayList<>();
            pointVectors.add(new PointVector(0.75f, 0.25f, 0.0f));
            pointVectors.add(new PointVector(0.4f, 0.6f, 0.0f));

            diedrico = new Diedrico(pointVectors, null, null);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.firtstext);
            explanationFragment.newInstance();
        } else if (id == R.id.lineProjection) {

            List<LineVector> lineVectors = new ArrayList<>();
            lineVectors.add(new LineVector(0.0f, 0.8f, 0.4f, 0.9f, 0.0f, -0.4f));
            diedrico = new Diedrico(null, null, null);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.firtstext);
            explanationFragment.newInstance();
        } else if(id == R.id.crosswideLine){
            List<LineVector> lineVectors = new ArrayList<>();
            lineVectors.add(new LineVector(0.0f, 0.8f, 0.4f, 0.9f, 0.0f, -0.4f));

            diedrico = new Diedrico(null, lineVectors, null);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.crosswideLineInfo);
            explanationFragment.newInstance();
        } else if(id == R.id.horizontalLine){
            List<LineVector> lineVectors = new ArrayList<>();
            lineVectors.add(new LineVector(0.9f, 0.0f, 0.4f, 0.9f, 0.9f, -0.4f));

            diedrico = new Diedrico(null, lineVectors, null);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.horizontalLineInfo);
            explanationFragment.newInstance();
        } else if(id == R.id.frontalLine){
            List<LineVector> lineVectors = new ArrayList<>();
            lineVectors.add(new LineVector(0.0f, 0.5f, 0.4f, 0.9f, 0.5f, -0.4f));

            diedrico = new Diedrico(null, lineVectors, null);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.frontalLineInfo);
            explanationFragment.newInstance();
        } else if(id == R.id.rigidLine){
            List<LineVector> lineVectors = new ArrayList<>();
            lineVectors.add(new LineVector(0.5f, 0.0f, 0.0f, 0.5f, 0.9f, 0.0f));

            diedrico = new Diedrico(null, lineVectors, null);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.rigidLineInfo);
            explanationFragment.newInstance();
        } else if(id == R.id.verticalLine){
            List<LineVector> lineVectors = new ArrayList<>();
            lineVectors.add(new LineVector(0.0f, 0.5f, 0.0f, 0.9f, 0.5f, 0.0f));

            diedrico = new Diedrico(null, lineVectors, null);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.verticalLineInfo);
            explanationFragment.newInstance();
        } else if(id == R.id.groundLineParallelLine){
            List<LineVector> lineVectors = new ArrayList<>();
            lineVectors.add(new LineVector(0.5f, 0.5f, 0.4f, 0.5f, 0.5f, -0.4f));

            diedrico = new Diedrico(null, lineVectors, null);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.groundLineParallelLineInfo);
            explanationFragment.newInstance();
        } else if(id == R.id.profileLine){
            List<LineVector> lineVectors = new ArrayList<>();
            lineVectors.add(new LineVector(0.9f, 0.0f, 0.0f, 0.0f, 0.9f, 0.0f));

            diedrico = new Diedrico(null, lineVectors, null);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.groundLineParallelLineInfo);
            explanationFragment.newInstance();
        } else if(id == R.id.groundLineCuttedLine){
            List<LineVector> lineVectors = new ArrayList<>();
            lineVectors.add(new LineVector(0.0f, 0.0f, 0.0f, 0.9f, 0.9f, 0.0f));

            diedrico = new Diedrico(null, lineVectors, null);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.groundLineParallelLineInfo);
            explanationFragment.newInstance();
        } else if(id == R.id.crosswidePlane) {
            List<PlaneVector> planeVectors = new ArrayList<>();
            planeVectors.add(new PlaneVector(new PointVector(0.0f, 0.0f, 0.4f), new PointVector(0.0f, 1.0f, -0.5f), new PointVector(0.5f, 0.5f, -0.5f), new PointVector(1.0f, 0.0f, -0.5f)));

            diedrico = new Diedrico(null, null, planeVectors);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.crosswidePlanoInfo);
            explanationFragment.newInstance();
        }  else if(id == R.id.horizontalPlane){
            List<PlaneVector> planeVectors = new ArrayList<>();
            planeVectors.add(new PlaneVector(new PointVector(0.0f, 0.5f, -0.5f),  new PointVector(0.0f, 0.5f, 0.5f), new PointVector(0.9f, 0.5f, 0.5f),new PointVector(0.9f, 0.5f, -0.5f)));

            diedrico = new Diedrico(null, null, planeVectors);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.horizontalPlaneInfo);
            explanationFragment.newInstance();
        }  else if(id == R.id.frontalPlane){
            List<PlaneVector> planeVectors = new ArrayList<>();
            planeVectors.add(new PlaneVector(new PointVector(0.5f, 0.0f, 0.5f), new PointVector(0.5f, 0.0f, -0.5f), new PointVector(0.5f, 1.0f, -0.5f), new PointVector(0.5f, 1.0f, 0.5f)));

            diedrico = new Diedrico(null, null, planeVectors);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.frontalLineInfo);
            explanationFragment.newInstance();
        }  else if(id == R.id.horizontalProjectionPlane){
            List<PlaneVector> planeVectors = new ArrayList<>();
            planeVectors.add(new PlaneVector(new PointVector(0.0f, 1.0f, 0.4f) ,new PointVector(0.0f, 0.0f, 0.4f), new PointVector(0.5f, 0.0f, -0.5f), new PointVector(0.5f, 1.0f, -0.5f)));

            diedrico = new Diedrico(null, null, planeVectors);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.horizontalProjectionPlaneInfo);
            explanationFragment.newInstance();
        }  else if(id == R.id.verticalProjectionPlane){
            List<PlaneVector> planeVectors = new ArrayList<>();
            planeVectors.add(new PlaneVector(new PointVector(0.0f, 0.0f, 0.4f), new PointVector(0.0f, 1.0f,-0.5f), new PointVector(1.0f, 1.0f , -0.5f), new PointVector(1.0f, 0.0f, 0.4f)));

            diedrico = new Diedrico(null, null, planeVectors);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.verticalProjectionPlaneInfo);
            explanationFragment.newInstance();
        }  else if(id == R.id.groundLineParallelPlane){
            List<PlaneVector> planeVectors = new ArrayList<>();
            planeVectors.add(new PlaneVector(new PointVector(0.5f, 0.0f, 0.5f), new PointVector(0.5f, 0.0f, -0.5f), new PointVector(0.0f, 0.7f, -0.5f), new PointVector(0.0f, 0.7f, 0.5f)));

            diedrico = new Diedrico(null, null, planeVectors);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.groundLineParallelPlaneInfo);
            explanationFragment.newInstance();
        }  else if(id == R.id.profilePlane){
            List<PlaneVector> planeVectors = new ArrayList<>();
            planeVectors.add(new PlaneVector(new PointVector(0.0f, 0.0f, 0.0f), new PointVector(1.0f, 0.0f, 0.0f), new PointVector(1.0f, 1.0f, 0.0f), new PointVector(0.0f, 1.0f, 0.0f)));

            diedrico = new Diedrico(null, null, planeVectors);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.profilePlaneInfo);
            explanationFragment.newInstance();
        }  else if(id == R.id.groundLineCuttedPlane){
            List<PlaneVector> planeVectors = new ArrayList<>();
            planeVectors.add(new PlaneVector(new PointVector(0.0f, 0.0f, 0.5f), new PointVector(1.0f, 1.0f, 0.5f), new PointVector(1.0f, 1.0f, -0.5f), new PointVector(0.0f, 0.0f, -0.5f)));

            diedrico = new Diedrico(null, null, planeVectors);

            projectionFragment.changeRenderer(new MyGLRenderer(diedrico));
            projectionFragment.newInstance();

            diedricoFragment.setDiedrico(diedrico);

            explanationFragment.setExplanation(R.string.groundLineCuttedPlaneInfo);
            explanationFragment.newInstance();
        }

        */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_tabs_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;

    }



    private void prepareExapandableListNavigationView(){
        header = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        //Adding child data
        header = Arrays.asList(getResources().getStringArray(R.array.menu));

        listDataChild.put(header.get(0), Arrays.asList(getResources().getStringArray(R.array.getStarted)));  //Header, Child data
        listDataChild.put(header.get(1), Arrays.asList(getResources().getStringArray(R.array.projections)));
        listDataChild.put(header.get(2), Arrays.asList(getResources().getStringArray(R.array.typeOfLines)));
        listDataChild.put(header.get(3), Arrays.asList(getResources().getStringArray(R.array.typeOfPlanes)));
    }

}
