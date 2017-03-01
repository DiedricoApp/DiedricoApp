package com.diedrico.diedricoapp.picToDiedrico;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.android.ConvertBitmap;
import boofcv.android.VisualizeImageData;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;

/**
 * Created by amil101 on 24/01/16.
 */
public class Thresholding extends AsyncTask<Integer, Integer, Bitmap> {
    ImageView imageView;
    String pic;

    public Thresholding(ImageView imageView, String pic) {
        super();
        this.imageView = imageView;
        this.pic = pic;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(pic), 500, 500, false);      //Scale the bitmap to not crash

        // convert into a usable format
        ImageFloat32 input = ConvertBitmap.bitmapToGray(scaledBitmap,(ImageFloat32) null,null);                    //(image, null, ImageFloat32.class);

        ImageUInt8 binary = new ImageUInt8(input.width,input.height);

        GThresholdImageOps.localSauvola(input, binary, params[0], 0.3f, false);
        VisualizeImageData.binaryToBitmap(binary, false, scaledBitmap, null);

        return scaledBitmap;
    }
}
