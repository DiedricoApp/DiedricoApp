package com.diedrico.diedricoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.diedrico.diedricoapp.picToDiedrico.LineDiedrico;
import com.diedrico.diedricoapp.picToDiedrico.PicAnalyzer;
import com.diedrico.diedricoapp.picToDiedrico.PointDiedrico;
import com.diedrico.diedricoapp.picToDiedrico.Thresholding;
import com.diedrico.diedricoapp.vector.LineVector;
import com.diedrico.diedricoapp.vector.PlaneVector;
import com.diedrico.diedricoapp.vector.PointVector;
import com.diedrico.diedricoapp.vector.ScalarProduct;
import com.diedrico.diedricoapp.vector.Vector;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.annimon.stream.Stream;


/**
 * Created by amil101 on 12/02/16.
 */
public class PicMenuActivity extends AppCompatActivity {
    Context context;
    private Toolbar toolbar;
    DrawerLayout drawer;        //The Navigation View
    private CoordinatorLayout coordinatorLayout;        //The layout with the tabLayout, we need to inflate the content we want to show

    ExpandableListAdapter listAdapter;      //adapter of listExpandable, its a custom adapter
    ExpandableListView expListView;     //To access the expandable list View
    List<String> header;        //List of items that are expandable
    HashMap<String, List<String>> listDataChild;        //Childs of the expandable
    ListView listView;      //To put the lists that are not expandable
    List<String> itemsListView;     //Items that are not expandable

    ImageView imageView;        //The main imageView of the activity
    String copyOfFile;          //The path where we will copy the original pic
    Bitmap thresholdingBitmap;
    Boolean analyzerFinished = Boolean.FALSE;

    SeekBar seekBar;
    LayoutInflater layoutInflater;      //Inflater for confirm analysis
    LinearLayout confirmationLayout;    //The layout where we will put the buttons
    ImageButton confirmButton;          //Confirm Button
    ImageButton wrongButton;            //Wrong button


    EditText nPointsEditText;                                   //where we specify the number of points
    EditText nLinesEditText;                                    //where we specify the number of lines
    EditText nPlanesEditText;                                   //where we specify the number of planes

    List<PointVector> allPoints;      //var to store the points found
    List<LineVector> allLines;        //var to store the lines found

    Spinner menuType;                                   //where will be the types of points, lines or planes that we already specified
    Spinner menuNumber;                                 //where will be the points, lines or planes found

    List<PointDiedrico> pointDiedrico = new ArrayList<>();
    List<LineDiedrico> lineDiedrico = new ArrayList<>();

    LineVector landLine;              //is the XY line. Is the intersection line between the vertical plane and the horizontal plane

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;     //set the context

        //Inflate the content in the coordinator layout
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.content_pic_menu, coordinatorLayout);

        //The toolbar of the app
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Now we have to put the button in the toolbar and setup the NavigationView
        drawer = (DrawerLayout) findViewById(R.id.drawer_tabs_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
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

        listView.setOnItemClickListener(onListViewItemListener());

        Intent intent = getIntent();
        String originalFile = intent.getStringExtra("file");    //we receive te file of the picture
        copyOfFile = originalFile.substring(0, originalFile.length() - 4) + "2.jpg";        //The path where we will copy the file
        copyFile(originalFile, copyOfFile);                                                                   //we have to copy the picture for modifying with BOOFCV

        imageView = (android.widget.ImageView) findViewById(R.id.imagePreview);                                              //the imageView

        new Thresholding(BitmapFactory.decodeFile(copyOfFile), new Thresholding.AsyncResponse() {       //thresholding, the pictur pass to a filter where blacks are more blacks and whites more whites
            @Override
            public void processFinish(Bitmap result) {
                imageView.setImageBitmap(result);
                thresholdingBitmap = result;
            }
        }).execute(1);

        seekBar = (SeekBar) findViewById(R.id.seekBar);                                                 //our SeekBar
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener());

        //Inflate the buttons to confirm the analysis
        layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        confirmationLayout = (LinearLayout) findViewById(R.id.menuLayout);
        layoutInflater.inflate(R.layout.confirmation_buttons, confirmationLayout);

        confirmButton = (ImageButton) confirmationLayout.findViewById(R.id.buttonOk);
        confirmButton.setOnClickListener(confirmClickListener());
        wrongButton = (ImageButton) confirmationLayout.findViewById(R.id.buttonWrong);
        wrongButton.setOnClickListener(wrongButtonClickListener());
    }

    private PicAnalyzer.AsyncResponse analyzerFinished(){           //We receive the important points and lines, we have to paint them
        return new PicAnalyzer.AsyncResponse() {
            @Override
            public void processFinish(List<PointVector> points, List<LineVector> lines, List<Double> planos) {
                //Set that the analyzer finished
                analyzerFinished = Boolean.TRUE;

                //Save the points in another var, if the user select that there are errors in the scan, he can select the points and lines manually
                allPoints = points;
                allLines = lines;

                //First we have to delete the short lines, we get the top module and then we discard the short ones
                Collections.sort(lines, new Comparator<LineVector>() {
                    @Override
                    public int compare(LineVector line1, LineVector line2) {
                        return (line1.getModuleTwoDimensionalVector() - line2.getModuleTwoDimensionalVector() >= 0)? -1:1;
                    }
                });

                landLine = lines.get(0);            //This is the landLine
                double minModule = lines.get(0).getModuleTwoDimensionalVector()/6;

                for(LineVector linevector : new ArrayList<>(lines)){
                    if(linevector.getModuleTwoDimensionalVector() < minModule){
                        lines.remove(linevector);
                    }
                }

                pointDiedrico = new ArrayList<>();
                lineDiedrico = new ArrayList<>();

                ////Delete the wrong lines, the ones that don't have x's view and y's view respectively (cota and alejamiento). Then we differ between if it is a line(in diedrico) or a plane
                for(int j = 1; j < lines.size(); j++){          //Start in 1 because in 0 the line is the landLine
                    LineVector line1 = lines.get(j);
                    for(int k = 1; k < lines.size(); k++){
                        LineVector line2 = lines.get(k);

                        if(line1.equals(line2)){
                            if(k == lines.size() - 1){     //The last line of the list
                                lines.remove(k);
                                j--;
                                break;
                            }
                            continue;
                        }

                        if(line1.getLineYA() > lines.get(0).getYEquation(line1.getLineXA()) && line2.getLineYA() < lines.get(0).getYEquation(line1.getLineXA()) || line2.getLineYA() > lines.get(0).getYEquation(line1.getLineXA()) && line1.getLineYA() < lines.get(0).getYEquation(line1.getLineXA())){
                            if(line1.getLineXA() > line2.getLineXA() - 50 && line1.getLineXA() < line2.getLineXA() + 50){         //Has found a result, then we delete the points from the list, and put them in pointDiedricoList and we continue
                                if(line1.getLineYA() > landLine.getLineYA()){//We store the cota and alejamiento, the cota is the line over the landline
                                    lineDiedrico.add(new LineDiedrico(line1, line2));
                                }
                                else{
                                    lineDiedrico.add(new LineDiedrico(line2, line1));
                                }

                                lines.remove(k);
                                lines.remove(j);

                                j--;
                                break;
                            }
                        }

                        if(k == lines.size() - 1){         //The line doesn't have a couple
                            lines.remove(j);
                            j--;
                        }
                    }
                }

                //Also, boofcv finds a lot of useless points near the lines, so we have to delete them
                Iterator<LineVector> lineVectorIterator = lines.iterator();
                while(lineVectorIterator.hasNext()){
                    LineVector lineVector = lineVectorIterator.next();

                    points = Stream.of(points)
                            .filter((PointVector point) -> point.getPointY() < lineVector.getYEquation(point.getPointX()) - 15 || point.getPointY() > lineVector.getYEquation(point.getPointX()) + 15)
                            .toList();
                }

                //Then we have to delete the groups of points and take only one of them
                for(int j = 0; j < points.size(); j++){
                    PointVector point1 = points.get(j);
                    for(int k = 0; k < points.size(); k++){
                        PointVector point2 = points.get(k);

                        if(point1.equals(point2)){
                            continue;
                        }


                        if(point1.getPointX() > point2.getPointX() - 20 && point1.getPointX() < point2.getPointX() + 20 && point1.getPointY() > point2.getPointY() - 20 && point1.getPointY() < point2.getPointY() + 20){         //Has found a result, then we delete the points from the list
                            points.remove(k);
                            k--;
                        }
                    }
                }

                //Delete the wrong point, the ones that don't have x's view and y's view respectively (cota and alejamiento). The correct points will be in pointDiedrico
                for(int j = 0; j < points.size(); j++){
                    PointVector point1 = points.get(j);
                    for(int k = 0; k < points.size(); k++){
                        PointVector point2 = points.get(k);

                        if(point1.equals(point2)){
                            if(k == points.size() - 1){     //The last point of the list
                                points.remove(k);
                                j--;
                                break;
                            }
                            continue;
                        }

                        if(point1.getPointY() > lines.get(0).getYEquation(point1.getPointX()) && point2.getPointY() < lines.get(0).getYEquation(point2.getPointX())
                                || point2.getPointY() > lines.get(0).getYEquation(point2.getPointX()) && point1.getPointY() < lines.get(0).getYEquation(point1.getPointX())){
                            if(point1.getPointX() > point2.getPointX() - 15 && point1.getPointX() < point2.getPointX() + 15){
                                //Has found a result, then we delete the points from the list, and put them in pointDiedricoList and we continue
                                if(point1.getPointY() > lines.get(0).getYEquation(point1.getPointX())){         //We store the cota and alejamiento, the cota is the point over the landline
                                    pointDiedrico.add(new PointDiedrico(point1, point2));
                                }
                                else{
                                    pointDiedrico.add(new PointDiedrico(point2, point1));
                                }

                                points.remove(k);
                                points.remove(j);

                                j--;
                                break;
                            }
                        }

                        if(k == points.size() - 1){         //The point doesn't have a couple
                            points.remove(j);
                            j--;
                        }
                    }
                }

                int indexColors = 0;    //Counter for the color array
                int[] colors = context.getResources().getIntArray(R.array.rainbow);

                Paint paintMax;
                paintMax = new Paint();
                paintMax.setStyle(Paint.Style.FILL);

                Canvas canvas = new Canvas(thresholdingBitmap);

                for(int i = 0; i < pointDiedrico.size(); i++){
                    paintMax.setColor(colors[indexColors++]);
                    if(indexColors >= colors.length)
                        indexColors = 0;

                    canvas.drawCircle(pointDiedrico.get(i).getX().getPointX(), pointDiedrico.get(i).getX().getPointY(), 3, paintMax);
                    canvas.drawCircle(pointDiedrico.get(i).getY().getPointX(), pointDiedrico.get(i).getY().getPointY(), 3, paintMax);
                }

                //Paint the landLine with color blue
                paintMax.setColor(Color.BLUE);
                canvas.drawLine(lines.get(0).getLineXA(), lines.get(0).getLineYA(), lines.get(0).getLineXB(), lines.get(0).getLineYB(), paintMax);


                //Paint the interesting lines
                for (int i = 0; i < lineDiedrico.size(); i++) {
                    paintMax.setColor(colors[indexColors++]);
                    if(indexColors >= colors.length)
                        indexColors = 0;
                    canvas.drawLine(lineDiedrico.get(i).getX().getLineXA(), lineDiedrico.get(i).getX().getLineYA(), lineDiedrico.get(i).getX().getLineXB(), lineDiedrico.get(i).getX().getLineYB(), paintMax);
                    canvas.drawLine(lineDiedrico.get(i).getY().getLineXA(), lineDiedrico.get(i).getY().getLineYA(), lineDiedrico.get(i).getY().getLineXB(), lineDiedrico.get(i).getY().getLineYB(), paintMax);
                }

                imageView.setImageBitmap(thresholdingBitmap);
            }
        };
    }

    private MenuItem.OnMenuItemClickListener processClickListener() {
        return new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                return true;
            }
        };
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

        itemsListView.add(this.getString(R.string.camera));       //Add the camera view for ListView, the reason is that cameraView is no expandable but it must be in the nav view
    }

    public ExpandableListView.OnChildClickListener onExpandableClick(){         //Setup the listener to the expandable List View
        return new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ArrayList<PointVector> pointVectors = new ArrayList<>();
                ArrayList<LineVector> lineVectors = new ArrayList<>();
                ArrayList<PlaneVector> planeVectors = new ArrayList<>();

                Intent intent = new Intent(context, MainActivity.class);                   //When we have the picture, we go to PreviewMenuActivity with the name of the file

                switch(groupPosition){
                    case 0:
                        switch (childPosition){
                            case 0:     //The user pressed welcome
                                intent.putExtra("title", R.string.welcome);
                                intent.putExtra("explanation", R.string.firtstext);
                                break;
                            case 1:     //the user pressed components of diedrico
                                intent.putExtra("title", R.string.components);
                                intent.putExtra("explanation", R.string.edges);
                                break;
                            case 2:     //the user pressed edges
                                intent.putExtra("title", R.string.edges);
                                intent.putExtra("explanation", R.string.firtstext);
                                break;
                        }
                        break;
                    case 1:
                        switch (childPosition){
                            case 0:     //The user pressed point projection
                                pointVectors.add(new PointVector(0.75f, 0.25f, 0.0f));
                                pointVectors.add(new PointVector(0.4f, 0.6f, 0.0f));

                                intent.putExtra("title", R.string.pointProjection);
                                intent.putExtra("explanation", R.string.pointProjectionInfo);
                                break;
                            case 1:     ////The user pressed line projection
                                lineVectors.add(new LineVector(0.0f, 0.8f, 0.4f, 0.9f, 0.0f, -0.4f));

                                intent.putExtra("title", R.string.lineProjection);
                                intent.putExtra("explanation", R.string.lineProjectionInfo);
                                break;
                        }
                        break;
                    case 2:
                        switch (childPosition) {
                            case 0:     //the user pressed crosswideLine
                                lineVectors.add(new LineVector(0.0f, 0.8f, 0.4f, 0.9f, 0.0f, -0.4f));

                                intent.putExtra("title", R.string.crosswideLine);
                                intent.putExtra("explanation", R.string.crosswideLineInfo);
                                break;
                            case 1:     //the user pressed horizontal line
                                lineVectors.add(new LineVector(0.9f, 0.0f, 0.4f, 0.9f, 0.9f, -0.4f));

                                intent.putExtra("title", R.string.horizontalLine);
                                intent.putExtra("explanation", R.string.horizontalLineInfo);
                                break;
                            case 2:     //the user pressed frontal line
                                lineVectors.add(new LineVector(0.0f, 0.5f, 0.4f, 0.9f, 0.5f, -0.4f));

                                intent.putExtra("title", R.string.frontalLine);
                                intent.putExtra("explanation", R.string.frontalLineInfo);
                                break;
                            case 3:     //the user pressed rigid Line
                                lineVectors.add(new LineVector(0.5f, 0.0f, 0.0f, 0.5f, 0.9f, 0.0f));

                                intent.putExtra("title", R.string.rigidLine);
                                intent.putExtra("explanation", R.string.rigidLineInfo);
                                break;
                            case 4:     //the user pressed vertical lne
                                lineVectors.add(new LineVector(0.0f, 0.5f, 0.0f, 0.9f, 0.5f, 0.0f));

                                intent.putExtra("title", R.string.verticalLine);
                                intent.putExtra("explanation", R.string.verticalLineInfo);
                                break;
                            case 5:     //the user pressed ground line parallel line
                                lineVectors.add(new LineVector(0.5f, 0.5f, 0.4f, 0.5f, 0.5f, -0.4f));

                                intent.putExtra("title", R.string.groundLineParallelLine);
                                intent.putExtra("explanation", R.string.groundLineParallelLineInfo);
                                break;
                            case 6:     //the user pressed profileLine
                                lineVectors.add(new LineVector(0.9f, 0.0f, 0.0f, 0.0f, 0.9f, 0.0f));

                                intent.putExtra("title", R.string.profileLine);
                                intent.putExtra("explanation", R.string.profileLine);
                                break;
                            case 7:     //the user pressed ground line cutted line
                                lineVectors.add(new LineVector(0.0f, 0.0f, 0.0f, 0.9f, 0.9f, 0.0f));

                                intent.putExtra("title", R.string.groundLineCuttedLine);
                                intent.putExtra("explanation", R.string.groundLineCuttedLineInfo);
                                break;
                        }
                        break;
                    case 3:
                        switch (childPosition) {
                            case 0:     //the user pressed crosswide plane
                                planeVectors.add(new PlaneVector(new PointVector(0.0f, 0.0f, 0.4f), new PointVector(0.0f, 1.0f, -0.5f), new PointVector(0.5f, 0.5f, -0.5f), new PointVector(1.0f, 0.0f, -0.5f)));

                                intent.putExtra("title", R.string.crosswidePlane);
                                intent.putExtra("explanation", R.string.crosswidePlanoInfo);
                                break;
                            case 1:     //the user pressed horizontal plane
                                planeVectors.add(new PlaneVector(new PointVector(0.0f, 0.5f, -0.5f), new PointVector(0.0f, 0.5f, 0.5f), new PointVector(0.9f, 0.5f, 0.5f), new PointVector(0.9f, 0.5f, -0.5f)));

                                intent.putExtra("title", R.string.horizontalPlane);
                                intent.putExtra("explanation", R.string.horizontalPlaneInfo);
                                break;
                            case 2:     //the user pressed frontal plane
                                planeVectors.add(new PlaneVector(new PointVector(0.5f, 0.0f, 0.5f), new PointVector(0.5f, 0.0f, -0.5f), new PointVector(0.5f, 1.0f, -0.5f), new PointVector(0.5f, 1.0f, 0.5f)));

                                intent.putExtra("title", R.string.frontalPlane);
                                intent.putExtra("explanation", R.string.frontalPlaneInfo);
                                break;
                            case 3:     //the user pressed horizontal projection plane
                                planeVectors.add(new PlaneVector(new PointVector(0.0f, 1.0f, 0.4f), new PointVector(0.0f, 0.0f, 0.4f), new PointVector(0.5f, 0.0f, -0.5f), new PointVector(0.5f, 1.0f, -0.5f)));

                                intent.putExtra("title", R.string.horizontalProjectionPlane);
                                intent.putExtra("explanation", R.string.horizontalProjectionPlaneInfo);
                                break;
                            case 4:     //the user pressed vertical projection plane
                                planeVectors.add(new PlaneVector(new PointVector(0.0f, 0.0f, 0.4f), new PointVector(0.0f, 1.0f, -0.5f), new PointVector(1.0f, 1.0f, -0.5f), new PointVector(1.0f, 0.0f, 0.4f)));

                                intent.putExtra("title", R.string.verticalProjectionPlane);
                                intent.putExtra("explanation", R.string.verticalProjectionPlaneInfo);
                                break;
                            case 5:     //the user pressed groundLineParallelPlane
                                planeVectors.add(new PlaneVector(new PointVector(0.5f, 0.0f, 0.5f), new PointVector(0.5f, 0.0f, -0.5f), new PointVector(0.0f, 0.7f, -0.5f), new PointVector(0.0f, 0.7f, 0.5f)));

                                intent.putExtra("title", R.string.groundLineParallelPlane);
                                intent.putExtra("explanation", R.string.groundLineParallelPlaneInfo);
                                break;
                            case 6:     //the user pressed groundline cutted plane
                                planeVectors.add(new PlaneVector(new PointVector(0.0f, 0.0f, 0.5f), new PointVector(1.0f, 1.0f, 0.5f), new PointVector(1.0f, 1.0f, -0.5f), new PointVector(0.0f, 0.0f, -0.5f)));

                                intent.putExtra("title", R.string.groundLineCuttedPlane);
                                intent.putExtra("explanation", R.string.groundLineCuttedPlaneInfo);
                                break;
                            case 7:     //the user pressed profile plane
                                planeVectors.add(new PlaneVector(new PointVector(0.0f, 0.0f, 0.0f), new PointVector(1.0f, 0.0f, 0.0f), new PointVector(1.0f, 1.0f, 0.0f), new PointVector(0.0f, 1.0f, 0.0f)));

                                intent.putExtra("title", R.string.profilePlane);
                                intent.putExtra("explanation", R.string.profilePlaneInfo);
                                break;
                        }
                        break;
                }

                intent.putParcelableArrayListExtra("pointVectors", pointVectors);
                intent.putParcelableArrayListExtra("lineVectors", lineVectors);
                intent.putParcelableArrayListExtra("planeVectors", planeVectors);

                startActivity(intent);

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

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener(){
        return new SeekBar.OnSeekBarChangeListener() {                     //the listener for our SeekBar
            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {            //we need to know the progress and then we modify our pic
                this.progress = progress;              //progress can't be 0
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                analyzerFinished = Boolean.FALSE;
                new Thresholding(BitmapFactory.decodeFile(copyOfFile), new Thresholding.AsyncResponse() {       //thresholding, the pictur pass to a filter where blacks are more blacks and whites more whites
                    @Override
                    public void processFinish(Bitmap result) {
                        imageView.setImageBitmap(result);
                        thresholdingBitmap = result;
                    }
                }).execute(progress + 1);
            }
        };
    }

    private ImageButton.OnClickListener confirmClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(analyzerFinished){           //The analysis is ok so we pass to 3D, but before we have to differ from a line or a plane and then locate them
                    // in the space with trigonometry

                    Log.i("asdf", "1: " + landLine.getLineXA() + " 2: " + landLine.getLineXB());

                    ArrayList<PointVector> pointVectors = new ArrayList<>();       //we pass the points to PointVector to know his X, his Y and his Z
                    for(int i = 0; i < pointDiedrico.size(); i++){
                        Vector AB = new Vector(new PointVector(landLine.getLineXA(), landLine.getLineYA()), new PointVector(landLine.getLineXB(), landLine.getLineYB()));
                        Vector AC = new Vector(new PointVector(landLine.getLineXA(), landLine.getLineYA()), new PointVector(pointDiedrico.get(i).getY().getPointX(), pointDiedrico.get(i).getY().getPointY()));
                        Vector AD = new Vector(new PointVector(landLine.getLineXA(), landLine.getLineYA()), new PointVector(pointDiedrico.get(i).getX().getPointX(), pointDiedrico.get(i).getX().getPointY()));
                        ScalarProduct scalarProductForX = new ScalarProduct(AB, AD);
                        ScalarProduct scalarProductForY = new ScalarProduct(AB, AC);

                        pointVectors.add(new PointVector((float)(scalarProductForY.getHeight() / AB.getModule()), (float)(scalarProductForX.getHeight() / AB.getModule()), -(float)(scalarProductForY.getLength() / AB.getModule()) + 0.5f));
                    }

                    ArrayList<LineVector> lineVectors = new ArrayList<>();
                    ArrayList<PlaneVector> planeVectors = new ArrayList<>();
                    for(int i = 0; i < lineDiedrico.size(); i++){
                        //We have to differ from a line or a plane, the difference its that plane have one view very next from each other (this is only with crosswide planes but for the moment its OK)
                        if(lineDiedrico.get(i).getX().getLineXA() > lineDiedrico.get(i).getY().getLineXA() - 5 && lineDiedrico.get(i).getX().getLineXA() < lineDiedrico.get(i).getY().getLineXA() + 5 &&
                                lineDiedrico.get(i).getX().getLineYA() > lineDiedrico.get(i).getY().getLineYA() - 5 && lineDiedrico.get(i).getX().getLineYA() > lineDiedrico.get(i).getY().getLineYA() + 5){           //It seems to be a plane

                            Vector AB = new Vector(new PointVector(landLine.getLineXA(), landLine.getLineYA()), new PointVector(landLine.getLineXB(), landLine.getLineYB()));
                            Vector AC = new Vector(new PointVector(landLine.getLineXA(), landLine.getLineYA()), new PointVector(lineDiedrico.get(i).getY().getLineXA(), lineDiedrico.get(i).getY().getLineYA()));
                            Vector AD = new Vector(new PointVector(landLine.getLineXA(), landLine.getLineYA()), new PointVector(lineDiedrico.get(i).getY().getLineXB(), lineDiedrico.get(i).getY().getLineYB()));
                            Vector AE = new Vector(new PointVector(landLine.getLineXA(), landLine.getLineYA()), new PointVector(lineDiedrico.get(i).getX().getLineXA(), lineDiedrico.get(i).getX().getLineYA()));
                            Vector AF = new Vector(new PointVector(landLine.getLineXA(), landLine.getLineYA()), new PointVector(lineDiedrico.get(i).getX().getLineXB(), lineDiedrico.get(i).getX().getLineYB()));

                            ScalarProduct scalarProductBeginningPlane;
                            ScalarProduct scalarProductX;
                            ScalarProduct scalarProductY;

                            if(lineDiedrico.get(i).getY().getLineYA() > lineDiedrico.get(i).getY().getLineYB()){           //Problem with planes
                                scalarProductBeginningPlane = new ScalarProduct(AB, AD);
                                scalarProductX = new ScalarProduct(AB, AE);
                                scalarProductY = new ScalarProduct(AB, AC);
                            }
                            else{
                                scalarProductBeginningPlane = new ScalarProduct(AB, AC);
                                scalarProductX = new ScalarProduct(AB, AF);
                                scalarProductY = new ScalarProduct(AB, AD);
                            }

                            PointVector pointVector1 = new PointVector(0, 0, (float)(scalarProductBeginningPlane.getLength()/AB.getModule()));
                            PointVector pointVector2 = new PointVector(0.0f, (float)(scalarProductY.getHeight()/AB.getModule()), (float)(scalarProductY.getLength()/AB.getModule()));
                            PointVector pointVector3 = new PointVector((float)(scalarProductX.getHeight()/AB.getModule()), 0.0f, (float)(scalarProductY.getLength()/AB.getModule()));
                            PointVector pointVector4 = pointVector2.getMidPoint(pointVector3);

                            planeVectors.add(new PlaneVector(pointVector1, pointVector2, pointVector3, pointVector4));
                        }
                        else{           //It seems to be a line
                            Vector AB = new Vector(new PointVector(landLine.getLineXA(), landLine.getLineYA()), new PointVector(landLine.getLineXB(), landLine.getLineYB()));
                            Vector AC = new Vector(new PointVector(landLine.getLineXA(), landLine.getLineYA()), new PointVector(lineDiedrico.get(i).getX().getLineXA(), lineDiedrico.get(i).getX().getLineYA()));
                            Vector AD = new Vector(new PointVector(landLine.getLineXA(), landLine.getLineYA()), new PointVector(lineDiedrico.get(i).getY().getLineXA(), lineDiedrico.get(i).getY().getLineYA()));
                            Vector AE = new Vector(new PointVector(landLine.getLineXA(), landLine.getLineYA()), new PointVector(lineDiedrico.get(i).getX().getLineXB(), lineDiedrico.get(i).getX().getLineYB()));
                            Vector AF = new Vector(new PointVector(landLine.getLineXA(), landLine.getLineYA()), new PointVector(lineDiedrico.get(i).getY().getLineXB(), lineDiedrico.get(i).getY().getLineYB()));

                            ScalarProduct scalarProductXA = new ScalarProduct(AB, AD);
                            ScalarProduct scalarProductYA = new ScalarProduct(AB, AC);
                            ScalarProduct scalarProductXB = new ScalarProduct(AB, AF);
                            ScalarProduct scalarProductYB = new ScalarProduct(AB, AE);

                            Log.i("asdf", "YA " + (float)(scalarProductYA.getHeight()/AB.getModule()) + " XA " + (float)(scalarProductXA.getHeight()/AB.getModule()) + " ZA " + (float)(scalarProductYA.getLength()/AB.getModule()) + " YB " + (float)(scalarProductYB.getHeight()/AB.getModule()) + " XB " + (float)(scalarProductXB.getHeight()/AB.getModule()) + " ZB " + (float)(scalarProductYB.getLength()/AB.getModule()));

                            lineVectors.add(new LineVector((float)(scalarProductYA.getHeight()/AB.getModule()), (float)(scalarProductXA.getHeight()/AB.getModule()), -(float)(scalarProductYA.getLength()/AB.getModule()) + 0.5f,
                                    (float)(scalarProductYB.getHeight()/AB.getModule()), (float)(scalarProductXB.getHeight()/AB.getModule()), -(float)(scalarProductYB.getLength()/AB.getModule()) + 0.5f));

                        }
                    }

                    Intent intent = new Intent(getApplicationContext(), OpenGlActivity.class);              //we pass the vector to OpenGL
                    intent.putParcelableArrayListExtra("points", pointVectors);
                    intent.putParcelableArrayListExtra("lines", lineVectors);
                    intent.putParcelableArrayListExtra("planes", planeVectors);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    getApplicationContext().startActivity(intent);

                    Toast.makeText(context, "aaaaaa", Toast.LENGTH_LONG).show();
                }
                else{                           //There is no analysis so we analyse the pic
                    Toast.makeText(context, "Analyzing", Toast.LENGTH_LONG).show();
                    new PicAnalyzer(10, analyzerFinished()).execute(thresholdingBitmap);            //to scan interesting points and interesting lines
                }
            }
        };
    }

    private ImageButton.OnClickListener wrongButtonClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(analyzerFinished){           //The analysis was wrong so we move to the menu to select manually the interesting points and lines
                    Log.i("asdf", "asdffff");
                    confirmationLayout.removeAllViews();
                    layoutInflater.inflate(R.layout.pic_analyzer_menu, confirmationLayout);


                    nPointsEditText = (EditText) findViewById(R.id.nPoints);                                                //The editText where the user specify the number of points
                    nLinesEditText = (EditText) findViewById(R.id.nLines);                                                //The editText where the user specify the number of lines
                    nPlanesEditText = (EditText) findViewById(R.id.nPlanes);                                                //The editText where the user specify the number of planes

                    //array of color
                    String colors[] = {"Red", "Green", "Blue"};

                    //Set menuType to the view and then put an array
                    menuType = (Spinner) confirmationLayout.findViewById(R.id.menu_tipo);
                    ArrayAdapter<String> menuTipoArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, colors);
                    menuTipoArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                    menuType.setAdapter(menuTipoArrayAdapter);


                    //Set menuNumber to the view and then put an array
                    menuNumber = (Spinner) confirmationLayout.findViewById(R.id.menu_numero);
                    ArrayAdapter<String> menuNumeroArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, colors);
                    menuNumeroArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    menuNumber.setAdapter(menuNumeroArrayAdapter);
                }
                else{
                    Toast.makeText(context, "First you have to select the best picture and confirm", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void copyFile(String inputFile, String outputPath) {

        InputStream in;
        OutputStream out;
        try {
            in = new FileInputStream(inputFile);
            out = new FileOutputStream(outputPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file (You have now copied the file)
            out.flush();
            out.close();

        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    int getnPoints(){
        int nPoints = 0;
        if(nPointsEditText.getText().toString().matches("")){
            Toast.makeText(context, "Invalid points", Toast.LENGTH_SHORT).show();
        }
        else{
            nPoints = Integer.parseInt(nPointsEditText.getText().toString());
        }

        return nPoints;
    }

    int getnLines(){
        int nLines = 0;
        if(nPointsEditText.getText().toString().matches("")){
            Toast.makeText(context, "Invalid lines", Toast.LENGTH_SHORT).show();
        }
        else{
            nLines = Integer.parseInt(nLinesEditText.getText().toString());
        }

        return nLines;
    }

    int getnPlanes(){
        int nPlanes = 0;
        if(nPointsEditText.getText().toString().matches("")){
            Toast.makeText(context, "Invalid planes", Toast.LENGTH_SHORT).show();
        }
        else{
            nPlanes = Integer.parseInt(nPlanesEditText.getText().toString());
        }
        return nPlanes;
    }
}

