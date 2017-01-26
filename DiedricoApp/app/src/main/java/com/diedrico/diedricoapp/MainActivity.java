package com.diedrico.diedricoapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.diedrico.diedricoapp.opengl.MyGLRenderer;
import com.diedrico.diedricoapp.opengl.MyGLRendererEdges;
import com.diedrico.diedricoapp.scrollabletabs.BaseActivity;
import com.diedrico.diedricoapp.scrollabletabs.BaseFragment;
import com.diedrico.diedricoapp.vector.Diedrico;
import com.diedrico.diedricoapp.vector.PointVector;
import com.diedrico.diedricoapp.vector.LineVector;
import com.diedrico.diedricoapp.vector.PlaneVector;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.noties.scrollable.CanScrollVerticallyDelegate;
import ru.noties.scrollable.OnScrollChangedListener;
import ru.noties.scrollable.ScrollableLayout;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String ARG_LAST_SCROLL_Y = "arg.LastScrollY";

    private ScrollableLayout mScrollableLayout;

    ProjectionFragment projectionFragment;
    DiedricoFragment diedricoFragment;
    ExplanationFragment explanationFragment;

    CreateDiedrico createDiedrico;

    Diedrico diedrico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_tabs);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_tabs_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_tabs_view);
        navigationView.setNavigationItemSelectedListener(this);

        final TabsLayout tabs = findView(R.id.tabs);

        mScrollableLayout = findView(R.id.scrollable_layout);
        mScrollableLayout.setDraggableView(tabs);
        final ViewPager viewPager = findView(R.id.view_pager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getResources(), getFragments());
        viewPager.setAdapter(adapter);

        tabs.setViewPager(viewPager);

// Note this bit, it's very important
        mScrollableLayout.setCanScrollVerticallyDelegate(new CanScrollVerticallyDelegate() {
            @Override
            public boolean canScrollVertically(int direction) {
                return adapter.canScrollVertically(viewPager.getCurrentItem(), direction);
            }
        });
        mScrollableLayout.setOnScrollChangedListener(new OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int y, int oldY, int maxY) {

                // Sticky behavior
                final float tabsTranslationY;
                if (y < maxY) {
                    tabsTranslationY = .0F;
                } else {
                    tabsTranslationY = y - maxY;
                }

                tabs.setTranslationY(tabsTranslationY);
            }
        });
    }

    private List<BaseFragment> getFragments() {

        final FragmentManager manager = getSupportFragmentManager();
        final List<BaseFragment> list = new ArrayList<>();

        projectionFragment = (ProjectionFragment) manager.findFragmentByTag(ProjectionFragment.TAG);
        if (projectionFragment == null) {
            projectionFragment = ProjectionFragment.newInstance();
        }

        diedricoFragment = (DiedricoFragment) manager.findFragmentByTag(DiedricoFragment.TAG);
        if(diedricoFragment == null){
            diedricoFragment = DiedricoFragment.newInstance();
        }


        explanationFragment= (ExplanationFragment) manager.findFragmentByTag(ExplanationFragment.TAG);
        if(explanationFragment == null){
            explanationFragment = explanationFragment.newInstance();
        }

        Collections.addAll(list, projectionFragment, diedricoFragment, explanationFragment);
        return list;
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_tabs_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;

    }


}
