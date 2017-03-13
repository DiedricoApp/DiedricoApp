package com.diedrico.diedricoapp.picToDiedrico;

import com.diedrico.diedricoapp.vector.PointVector;

/**
 * Created by amil101 on 13/03/17.
 */

public class PointDiedrico {
    PointVector y;      //Y point (Cota)
    PointVector x;      //X point (Alejamiento)

    public PointDiedrico(PointVector pointY, PointVector pointX){
        this.y = pointY;
        this.x = pointX;
    }
}
