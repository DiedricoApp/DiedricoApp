package com.diedrico.diedricoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.diedrico.diedricoapp.picToDiedrico.Thresholding;
import com.diedrico.diedricoapp.vector.LineVector;
import com.diedrico.diedricoapp.vector.PointVector;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by amil101 on 12/02/16.
 */
public class PicMenuActivity extends AppCompatActivity {

    private Toolbar toolbar;
    DrawerLayout drawer;        //The Navigation View
    private CoordinatorLayout coordinatorLayout;        //The layout with the tabLayout, we need to inflate the content we want to show

    ExpandableListAdapter listAdapter;      //adapter of listExpandable, its a custom adapter
    ExpandableListView expListView;     //To access the expandable list View
    List<String> header;        //List of items that are expandable
    HashMap<String, List<String>> listDataChild;        //Childs of the expandable
    ListView listView;      //To put the lists that are not expandable
    List<String> itemsListView;     //Items that are not expandable

    Thresholding thresholding;                             //object of thresholding, the pictur pass to a filter where blacks are more blacks and whites more whites
    ImageView imageView;        //The main imageView of the activity
    String copyOfFile;          //The path where we will copy the original pic
    //LineSegment lineSegment;                                //object, to scan interesting points and interesting lines

    SeekBar seekBar;

    EditText nPointsEditText;                                   //where we specify the number of points
    EditText nLinesEditText;                                    //where we specify the number of lines
    EditText nPlanesEditText;                                   //where we specify the number of planes

    int nPoints;                                                // the number of points
    int nLines;                                                 // the number of lines
    int nPlanes;                                                // the number of planes

    Spinner menuType;                                   //where will be the types of points, lines or planes that we already specified
    Spinner menuNumber;                                 //where will be the points, lines or planes found
    Spinner menuColor;                                  //where we will specify the color

    List<PointVector> pointObj = new ArrayList<>();               //whre we will put all the interested points found with BoofCV
    List<LineVector> lineObj = new ArrayList<>();                 //where will be all the interested lines found with BoofDC

    List<PointVector> pointY = new ArrayList<>();                 //Y of all points, we differ then with the key
    List<PointVector> pointX = new ArrayList<>();                 //X of all lines, we differ then with the key

    LineVector landLine;              //is the XY line. Is the intersection line between the vertical plane and the horizontal plane
    List<LineVector> lineY = new ArrayList<>();                   //Y of all lines, we differ then with the key
    List<LineVector> lineX = new ArrayList<>();                   //X of all lines, we differ then with the key

    List<LineVector> planeY = new ArrayList<>();                  //Y of all planes, we differ then with the key       PLANES ARE LINES BUT WE TREAT THEN LIKE PLANES
    List<LineVector> planeX = new ArrayList<>();                  //X of all planes, we differ then with the key       PLANES ARE LINES BUT WE TREAT THEN LIKE PLANES

    int typeOfPoint = 0;             // 0 means Y, 1 means X
    int typeOfLine = 0;              // 0 means Y, 1 means X
    int numberOfPoint = 0;
    int numberOfLine = 0;

    ArrayAdapter<String> menuPointArrayAdapter;             //array adapter of all interesting points, where the user can choose the type(x of point or y of point) of the point
    ArrayAdapter<String> menuLineArrayAdapter;              //array adapter of all interesting lines, where the user can choose the type(x of line, y of line, x of plane or y of plane) of the line

    List<String> typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                return false;
            }
        });

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

        thresholding = new Thresholding(imageView, copyOfFile);                                                   //We modify the picture first for not crashing
        thresholding.execute(1);

        seekBar = (SeekBar) findViewById(R.id.seekBar);                                                 //our SeekBar
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener());

        nPointsEditText = (EditText) findViewById(R.id.nPoints);                                                //The editText where the user specify the number of points
        nLinesEditText = (EditText) findViewById(R.id.nLines);                                                //The editText where the user specify the number of lines
        nPlanesEditText = (EditText) findViewById(R.id.nPlanes);                                                //The editText where the user specify the number of planes

        //array of color
        String colors[] = {"Red", "Green", "Blue"};

        //Set menuType to the view and then put an array
        menuType = (Spinner) findViewById(R.id.menu_tipo);
        ArrayAdapter<String> menuTipoArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colors);
        menuTipoArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        menuType.setAdapter(menuTipoArrayAdapter);


        //Set menuNumber to the view and then put an array
        menuNumber = (Spinner) findViewById(R.id.menu_numero);
        ArrayAdapter<String> menuNumeroArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colors);
        menuNumeroArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menuNumber.setAdapter(menuNumeroArrayAdapter);


        //Set menuNumber to the view and then put an array
        //menuColor = (Spinner) findViewById(R.id.menu_color);
        //ArrayAdapter<CharSequence> menuColorArrayAdapter = ArrayAdapter.createFromResource(this, R, android.R.layout.simple_spinner_item);
        //menuColorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //menuColor.setAdapter(menuColorArrayAdapter);
    }
/*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.preview_menu_topmenu, menu);

        MenuItem analizar = menu.findItem(R.id.analyze);
        analizar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Bitmap picBM = thresholding.getImageView();
                lineSegment = new LineSegment(getApplicationContext(), imageView, getnPoints(), menuType, new LineSegment.AsyncResponse() {
                    @Override
                    public void processFinish(List<Point> points, List<Line> lines, List<Double> planes) {

                        pointObj = points;          //we receive all the points found
                        lineObj = lines;            //we receive all the lines found

                        nPoints = getnPoints();
                        nLines = getnLines();
                        nPlanes = getnPlanes();

                        List<String> pointsForSpinner = new ArrayList<>();            //we put all the points to the Spinner
                        for(int i = 0; i< points.size(); i ++){
                            pointsForSpinner.add("Point " + Integer.toString(i) + " X:" + Float.toString((float) pointObj.get(i).getX()) + " Y:" + Float.toString((float) pointObj.get(i).getY()));
                            Log.i("points", "Point " + Integer.toString(i) + " X:" + Double.toString(pointObj.get(i).getX()) + " Y:" + Double.toString(pointObj.get(i).getY()));
                        }

                        List<String> linesForSpinner = new ArrayList<>();               //we put all the lines to the spinner
                        for(int i = 0; i < lines.size(); i++){
                            linesForSpinner.add("Line " + Integer.toString(i) + " Xa: " + Float.toString(lineObj.get(i).getXa()) + " Ya: " + Float.toString(lineObj.get(i).getYa()) + " Xb: "  + Float.toString(lineObj.get(i).getXb()) + " Yb: " + Float.toString(lineObj.get(i).getYb()));
                        }


                        //the Adapter with the points
                        menuPointArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, pointsForSpinner);
                        menuPointArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        //the adapter with the lines
                        menuLineArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, linesForSpinner);
                        menuLineArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        if(pointObj.size() >= ((nPoints)*2)){                 //we need to have equal or more points in nPointsEditText and pointObj
                            typeSpinner = new ArrayList<>();                                       //We create a Spinner with the X's and Y's of points, lines and planes that we specificated in nPointsEditText, nLinesEditText and nPlanesEditText

                            typeSpinner.add("Land Line");
                            landLine = lineObj.get(0);              //we need to put at least one landLines, later the user specify what line it is

                            for(int i =0; i< nPoints; i++){          //we put the points in the Spinner and we add to pointX or pointY
                                typeSpinner.add("X point nº " + i);
                                pointX.add(pointObj.get(((i * 2) + 1)));

                                typeSpinner.add("Y point nº " + i);
                                pointY.add(pointObj.get((i * 2)));

                            }

                            for(int i = 0; i < nLines; i++){         //we put the lines in the Spinner and we add to lineX or lineY
                                typeSpinner.add("X line " + i);//we need to put at least one, later we specify what line it is, in the second spinner. It needs to be increase by one, becase before we put the linea de tierra
                                lineX.add(lineObj.get((i * 2) + 2));

                                typeSpinner.add("Y line " + i);
                                lineY.add(lineObj.get((i * 2) + 1));          //we need to put at least one, later we specify what line it is, in the second spinner. It needs to be increase by one, becase before we put the linea de tierra

                            }

                            int currentLinesAdded = nLines*2;            //we need to know how many lines we added for matching the planes(they are also lines but treated like planes)

                            for(int i = 0; i < nPlanes; i++){            //we put the planes in the Spinner and we add to planeX or planeY
                                typeSpinner.add("X plane " + i);
                                planeX.add(lineObj.get((i * 2) + currentLinesAdded + 2));

                                typeSpinner.add("Y plane " + i);
                                planeY.add(lineObj.get((i * 2) + currentLinesAdded + 1));
                            }


                            ArrayAdapter<String> menuTipoArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, typeSpinner);           //we create the adapter for menuType Spinner with typeSpinner
                            menuTipoArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                            menuType.setAdapter(menuTipoArrayAdapter);
                            menuType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {                           //listener of menuType
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if ((position - 1) >= 0 && position < ((nPoints * 2) + 1) && position < ((nPoints * 2) + (nLines * 2) + 1) && (position - 1) % 2 == 0) {                        //if it the Y of a point
                                        menuNumber.setAdapter(menuPointArrayAdapter);                                       //we put menuNumber with the interesting points
                                        menuNumber.setOnItemSelectedListener(onMenuNumberPointSelectedListener(pointY.get((position - nLines) / 2)));            //we set the listener
                                        new ListenPoint(imageView, Bitmap.createBitmap(lineSegment.getImageView()), pointY.get((position - 1) / 2));                                  // we change the color of the point that was selected in pointY

                                        typeOfPoint = 0;                                                                    //what type it is, for later with the other spinner specify the point
                                        numberOfPoint = (position - 1) / 2;                                                  //what number of point it is
                                    }
                                    if ((position - 1) >= 0 && position < ((nPoints * 2) + 1) && position < ((nPoints * 2) + (nLines * 2) + 1) && (position - 1) % 2 != 0) {                       //if it is the X of a point
                                        menuNumber.setAdapter(menuPointArrayAdapter);                  //we put menuNumber with the interesting points
                                        menuNumber.setOnItemSelectedListener(onMenuNumberPointSelectedListener(pointX.get((position - 1) / 2)));                //we set the listener
                                        new ListenPoint(imageView, Bitmap.createBitmap(lineSegment.getImageView()), pointX.get((position - 1) / 2));                  // we change the color of the point that was selected in pointX

                                        typeOfPoint = 1;            //what type it is, for later with the other spinner specify the point
                                        numberOfPoint = (position - 1) / 2;          //what number of point it is
                                    }
                                    if (position >= ((nPoints * 2) + 1) && position < ((nPoints * 2) + (nLines * 2) + 1) && (position - (nPoints * 2) + 1) % 2 == 0) {           //It the Y of a line
                                        menuNumber.setAdapter(menuLineArrayAdapter);
                                        menuNumber.setOnItemSelectedListener(onMenuNumberLineSelectedListener());                       //we set the listener
                                        new ListenLine(imageView, Bitmap.createBitmap(lineSegment.getImageView()), lineY.get((position - ((nPoints) * 2) - 1) / 2));               // we change the color of the line that was selected in lineY

                                        typeOfLine = 0;             //what type it is, for later with the other spinner specify the line
                                        numberOfLine = (position - ((nPoints) * 2) - 1) / 2;
                                    }
                                    if (position >= ((nPoints * 2) + 1) && position < ((nPoints * 2) + (nLines * 2) + 1) && (position - (nPoints * 2) + 1) % 2 != 0) {             //It is the X of a line
                                        menuNumber.setAdapter(menuLineArrayAdapter);

                                        menuNumber.setOnItemSelectedListener(onMenuNumberLineSelectedListener());                   //we set the listener
                                        new ListenLine(imageView, Bitmap.createBitmap(lineSegment.getImageView()), lineX.get((position - ((nPoints) * 2) - 1) / 2));               // we change the color of the line that was selected in lineX

                                        typeOfLine = 1;         //what type it is, for later with the other spinner specify the line
                                        numberOfLine = (position - ((nPoints) * 2) - 1) / 2;
                                    }

                                    if (position >= ((nPoints * 2) + (nLines * 2) + 1) && (((position - ((nPoints * 2) + (nLines * 2) + 1)) % 2 == 0))) {                   //if it is the Y of a plane
                                        menuNumber.setAdapter(menuLineArrayAdapter);

                                        menuNumber.setOnItemSelectedListener(onMenuNumberPlaneSelecterListener());              //we set the listener
                                        new ListenLine(imageView, Bitmap.createBitmap(lineSegment.getImageView()), planeY.get((position - ((nLines) * 2) - ((nPoints) * 2) - 1) / 2));          // we change the color of the line that was selected in planeY

                                        typeOfLine = 0;                     //what type it is, for later with the other spinner specify the line
                                        numberOfLine = (position - ((nLines) * 2) - ((nPoints) * 2) - 1) / 2;
                                    }

                                    if (position >= ((nPoints * 2) + (nLines * 2) + 1) && (((position - ((nPoints * 2) + (nLines * 2) + 1)) % 2 != 0))) {                   //if it is the X of a plane
                                        menuNumber.setAdapter(menuLineArrayAdapter);

                                        menuNumber.setOnItemSelectedListener(onMenuNumberPlaneSelecterListener());              //we set the listener
                                        new ListenLine(imageView, Bitmap.createBitmap(lineSegment.getImageView()), planeX.get((position - ((nLines) * 2) - ((nPoints) * 2) - 1) / 2));          // we change the color of the line that was selected in planeX

                                        typeOfLine = 1;                 //what type it is, for later with the other spinner specify the line
                                        numberOfLine = (position - ((nLines) * 2) - ((nPoints) * 2) - 1) / 2;
                                    }
                                    if (position == 0) {                //If it is the landLine
                                        menuNumber.setAdapter(menuLineArrayAdapter);

                                        menuNumber.setOnItemSelectedListener(onMenuNumberLandLineSelectedListener());          //set the listener
                                        new ListenLine(imageView, Bitmap.createBitmap(lineSegment.getImageView()), landLine);                  // we change the color of the landLine
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                        else{                   //if we have less points on nPointsEditText than pointObj we need to retry the scan

                            Toast.makeText(getApplicationContext(), "We haven't found many points", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                lineSegment.execute(picBM);


                return true;
            }
        });

        MenuItem info = menu.findItem(R.id.info);
        info.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        MenuItem procesar = menu.findItem(R.id.process);
        procesar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                ArrayList<PointVector> pointVectors = new ArrayList<>();       //we pass the points to PointVector to know his X, his Y and his Z
                for(int i = 0; i < nPoints; i++){
                    Vector AB = new Vector(new Point(landLine.getXa(), landLine.getYa()), new Point(landLine.getXb(), landLine.getYb()));
                    Vector AC = new Vector(new Point(landLine.getXa(), landLine.getYa()), new Point(pointY.get(i).getX(), pointY.get(i).getY()));
                    Vector AD = new Vector(new Point(landLine.getXa(), landLine.getYa()), new Point(pointX.get(i).getX(), pointX.get(i).getY()));
                    ScalarProduct scalarProductForX = new ScalarProduct(AB, AD);
                    ScalarProduct scalarProductForY = new ScalarProduct(AB, AC);

                    pointVectors.add(new PointVector(scalarProductForY.getHeight() / AB.getModule(), scalarProductForX.getHeight() / AB.getModule(), scalarProductForY.getLength() / AB.getModule()));
                }




                ArrayList<LineVector> lineVectors = new ArrayList<>();        //we pass the lines to PointVector to know his X, his Y and his Z
                for(int i = 0; i < nLines; i ++){
                    Vector AB = new Vector(new Point(landLine.getXa(), landLine.getYa()), new Point(landLine.getXb(), landLine.getYb()));
                    Vector AC = new Vector(new Point(landLine.getXa(), landLine.getYa()), new Point(lineY.get(i).getXa(), lineY.get(i).getYa()));
                    Vector AD = new Vector(new Point(landLine.getXa(), landLine.getYa()), new Point(lineX.get(i).getXa(), lineX.get(i).getYa()));
                    Vector AE = new Vector(new Point(landLine.getXa(), landLine.getYa()), new Point(lineY.get(i).getXb(), lineY.get(i).getYb()));
                    Vector AF = new Vector(new Point(landLine.getXa(), landLine.getYa()), new Point(lineX.get(i).getXb(), lineX.get(i).getYb()));

                    ScalarProduct scalarProductXA = new ScalarProduct(AB, AD);
                    ScalarProduct scalarProductYA = new ScalarProduct(AB, AC);
                    ScalarProduct scalarProductXB = new ScalarProduct(AB, AF);
                    ScalarProduct scalarProductYB = new ScalarProduct(AB, AE);

                    lineVectors.add(new LineVector((float)(scalarProductYA.getHeight()/AB.getModule()), (float)(scalarProductXA.getHeight()/AB.getModule()), (float)(scalarProductYA.getLength()/AB.getModule()), (float)(scalarProductYB.getHeight()/AB.getModule()), (float)(scalarProductXB.getHeight()/AB.getModule()), (float)(scalarProductYB.getLength()/AB.getModule())));
                }

                ArrayList<PlaneVector> planeVectors = new ArrayList<>();         //we pass the planes to PointVector to know his X, his Y and his Z
                for(int i = 0; i < nPlanes; i++){
                    Vector AB = new Vector(new Point(landLine.getXa(), landLine.getYa()), new Point(landLine.getXb(), landLine.getYb()));
                    Vector AC = new Vector(new Point(landLine.getXa(), landLine.getYa()), new Point(planeY.get(i).getXa(), planeY.get(i).getYa()));
                    Vector AD = new Vector(new Point(landLine.getXa(), landLine.getYa()), new Point(planeY.get(i).getXb(), planeY.get(i).getYb()));
                    Vector AE = new Vector(new Point(landLine.getXa(), landLine.getYa()), new Point(planeX.get(i).getXa(), planeX.get(i).getYa()));
                    Vector AF = new Vector(new Point(landLine.getXa(), landLine.getYa()), new Point(planeX.get(i).getXb(), planeX.get(i).getYb()));

                    ScalarProduct scalarProductBeginningPlane;
                    ScalarProduct scalarProductX;
                    ScalarProduct scalarProductY;

                    if(planeY.get(i).getYa() > planeY.get(i).getYb()){           //Problem with planes
                        scalarProductBeginningPlane = new ScalarProduct(AB, AD);
                        scalarProductX = new ScalarProduct(AB, AE);
                        scalarProductY = new ScalarProduct(AB, AC);
                    }
                    else{
                        scalarProductBeginningPlane = new ScalarProduct(AB, AC);
                        scalarProductX = new ScalarProduct(AB, AF);
                        scalarProductY = new ScalarProduct(AB, AD);
                    }

                    planeVectors.add(new PlaneVector((float)(scalarProductBeginningPlane.getLength()/AB.getModule()), (float)(scalarProductY.getHeight()/AB.getModule()), (float)(scalarProductX.getHeight()/AB.getModule()), (float)(scalarProductY.getLength()/AB.getModule())));
                }

                Intent intent = new Intent(getApplicationContext(), OpenGlActivity.class);              //we pass the vector to OpenGL
                intent.putParcelableArrayListExtra("points", pointVectors);
                intent.putParcelableArrayListExtra("lines", lineVectors);
                intent.putParcelableArrayListExtra("planes", planeVectors);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                getApplicationContext().startActivity(intent);


                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    AdapterView.OnItemSelectedListener onMenuNumberPointSelectedListener(Point selectedPoint) {

        menuNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int counter = 0;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {               //if we select a point, it converts in the type of the previous Spinner (X or Y), with this way we specify what point is it
                if(counter != 0) {                         //when we create the listener, it activates alone. This don't let him!!
                    new ListenPoint(imageView, Bitmap.createBitmap(lineSegment.getImageView()), pointObj.get(position));

                    if (typeOfPoint == 0) {           //the point we selected is a Y
                        pointY.remove(numberOfPoint);
                        pointY.add(numberOfPoint, pointObj.get(position));

                    } else {                               //the point we selected is a X
                        pointX.remove(numberOfPoint);
                        pointX.add(numberOfPoint, pointObj.get(position));

                    }
                }
                counter++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new ListenPoint(imageView, Bitmap.createBitmap(lineSegment.getImageView()), selectedPoint);
        return menuNumber.getOnItemSelectedListener();

    }



    AdapterView.OnItemSelectedListener onMenuNumberLineSelectedListener() {
        menuNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int counter = 0;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {               //if we select a lin e, it converts in the type of the previous Spinner (Y o X), with this way we specify what line is it
                if (counter != 0) {                                  //when we create the listener, it activates alone. This don't let him!!
                    new ListenLine(imageView, Bitmap.createBitmap(lineSegment.getImageView()), lineObj.get(position));

                    if (typeOfLine == 0) {                //the line we selected is a Y
                        lineY.remove(numberOfLine);
                        lineY.add(numberOfLine, lineObj.get(position));
                    } else {                               //the line we selected is a X
                        lineX.remove(numberOfLine);
                        lineX.add(numberOfLine, lineObj.get(position));
                    }
                }
                counter++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return menuNumber.getOnItemSelectedListener();
    }

    AdapterView.OnItemSelectedListener onMenuNumberPlaneSelecterListener() {
        menuNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int counter = 0;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {               //if we select a plane, it converts in the type of the previous Spinner (Y o X), with this way we specify what plane is it
                if (counter != 0) {                              //when we create the listener, it activates alone. This don't let him!!
                    new ListenLine(imageView, Bitmap.createBitmap(lineSegment.getImageView()), lineObj.get(position));

                    if (typeOfLine == 0) {                //the line of the plano we selected is a Y
                        planeY.remove(numberOfLine);
                        planeY.add(numberOfLine, lineObj.get(position));
                    } else {                               //the line we selected is a X
                        planeX.remove(numberOfLine);
                        planeX.add(numberOfLine, lineObj.get(position));
                    }
                }
                counter++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return menuNumber.getOnItemSelectedListener();
    }

    AdapterView.OnItemSelectedListener onMenuNumberLandLineSelectedListener() {
        menuNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int counter = 0;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {               //if we select a landLine, it converts in the type of the previous Spinner (Y o X), with this way we specify what line is it
                if (counter != 0) {                              //when we create the listener, it activates alone. This don't let him!!
                    new ListenLine(imageView, Bitmap.createBitmap(lineSegment.getImageView()), lineObj.get(position));

                    landLine = lineObj.get(position);
                }
                counter++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return menuNumber.getOnItemSelectedListener();
    }

    */


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
                new Thresholding(imageView, copyOfFile).execute(progress + 1);
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
            Toast.makeText(getApplicationContext(), "Invalid points", Toast.LENGTH_SHORT).show();
        }
        else{
            nPoints = Integer.parseInt(nPointsEditText.getText().toString());
        }

        return nPoints;
    }

    int getnLines(){
        int nLines = 0;
        if(nPointsEditText.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(), "Invalid lines", Toast.LENGTH_SHORT).show();
        }
        else{
            nLines = Integer.parseInt(nLinesEditText.getText().toString());
        }

        return nLines;
    }

    int getnPlanes(){
        int nPlanes = 0;
        if(nPointsEditText.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(), "Invalid planes", Toast.LENGTH_SHORT).show();
        }
        else{
            nPlanes = Integer.parseInt(nPlanesEditText.getText().toString());
        }
        return nPlanes;
    }
}

