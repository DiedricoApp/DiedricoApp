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

package com.diedrico.diedricoapp.picToDiedrico;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.diedrico.diedricoapp.vector.LineVector;
import com.diedrico.diedricoapp.vector.PointVector;

import java.util.List;

import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.android.ConvertBitmap;
import boofcv.android.VisualizeImageData;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;

/**
 * Created by amil101 on 24/01/16.
 */
public class Thresholding extends AsyncTask<Integer, Integer, Bitmap> {
    ImageFloat32 input;
    Bitmap scaledBitmap;

    public Thresholding.AsyncResponse delegate = null;

    public Thresholding(Bitmap bitmap, AsyncResponse delegate) {
        super();

        this.delegate = delegate;
        scaledBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);      //Scale the bitmap to not crash

        // convert into a usable format
        input = ConvertBitmap.bitmapToGray(scaledBitmap,(ImageFloat32) null,null);                    //(image, null, ImageFloat32.class)
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        delegate.processFinish(bitmap);
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        ImageUInt8 binary = new ImageUInt8(input.width,input.height);

        GThresholdImageOps.localSauvola(input, binary, params[0], 0.3f, false);
        VisualizeImageData.binaryToBitmap(binary, false, scaledBitmap, null);

        return scaledBitmap;
    }

    public interface AsyncResponse{
        void processFinish(Bitmap result);
    }
}
