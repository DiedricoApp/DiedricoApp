package com.diedrico.diedricoapp.picToDiedrico;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Spinner;

import com.diedrico.diedricoapp.vector.LineVector;
import com.diedrico.diedricoapp.vector.PointVector;
import com.diedrico.diedricoapp.vector.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import boofcv.abst.feature.detect.interest.ConfigFastHessian;
import boofcv.abst.feature.detect.interest.InterestPointDetector;
import boofcv.abst.feature.detect.line.DetectLineSegmentsGridRansac;
import boofcv.android.ConvertBitmap;
import boofcv.factory.feature.detect.interest.FactoryInterestPoint;
import boofcv.factory.feature.detect.line.FactoryDetectLineAlgs;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSingleBand;
import georegression.struct.line.LineSegment2D_F32;

/**
 * Created by amil101 on 31/01/16.
 */
public class PicAnalyzer extends AsyncTask<Bitmap, Integer, Void> {
    int nPoints;
    int nLines;

    List<PointVector> points = new ArrayList<>();
    List<LineVector> lines = new ArrayList<>();

    public AsyncResponse delegate = null;

    public PicAnalyzer(int nPoints, AsyncResponse delegate){
        super();
        this.delegate = delegate;
        this.nPoints = nPoints;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        delegate.processFinish(points, lines, null);
    }

    @Override
    protected Void doInBackground(Bitmap... params) {
        points = detectPoints(params[0], ImageFloat32.class, nPoints *2);          //Detect interesting points, the number of points are nPoints*2 because each point has a Y's view and a X's view
        lines = detectLineSegments(params[0], ImageFloat32.class, ImageFloat32.class);
        return null;
    }

    public <T extends ImageSingleBand, D extends ImageSingleBand> List<LineVector> detectLineSegments( Bitmap image , Class<T> imageType , Class<D> derivType ) {
        List<LineVector> lineVectors = new ArrayList<>();

        // convert the line into a single band image
        T input = ConvertBitmap.bitmapToGray(image, null, imageType, null);

        // Comment/uncomment to try a different type of line detector
        DetectLineSegmentsGridRansac<T,D> detector = FactoryDetectLineAlgs.lineRansac(40, 10, 1, true, imageType, derivType);

        List<LineSegment2D_F32> found = detector.detect(input);

        for(int i = 0; i < found.size(); i++){
            lineVectors.add(new LineVector(found.get(i).b.y, found.get(i).b.x, found.get(i).a.y, found.get(i).a.x));
        }

        return lineVectors;
    }

    public <T extends ImageFloat32> List<PointVector> detectPoints( Bitmap image, Class<T> imageType , int nPuntos) {
        List<PointVector> pointVectors = new ArrayList<>();

        if(nPuntos != 0){           //There is a problem with BoofCV if we put 0 points
            T input = ConvertBitmap.bitmapToGray(image, null, imageType, null);

            // Create a Fast Hessian detector from the SURF paper.

            InterestPointDetector<T> detector = FactoryInterestPoint.fastHessian(
                    new ConfigFastHessian(30, 1, 20, 2, 3, 4, 4));
            // find interest points in the image
            detector.detect(input);

            for(int i = 0; i < detector.getNumberOfFeatures(); i++){
                pointVectors.add(new PointVector((float) detector.getLocation(i).getX(), (float) detector.getLocation(i).getY()));
            }
        }
        return pointVectors;
    }

    public interface AsyncResponse{
        void processFinish(List<PointVector> points, List<LineVector> lines, List<Double> planos);
    }

}
