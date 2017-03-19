package com.diedrico.diedricoapp.picToDiedrico;

import com.diedrico.diedricoapp.vector.LineVector;

/**
 * Created by amil101 on 19/03/17.
 */

public class LineDiedrico {
    private LineVector y;       //Y line (Cota)
    private LineVector x;       //X line (Alejamiento)

    public LineDiedrico(LineVector y, LineVector x) {
        this.y = y;
        this.x = x;
    }

    public LineVector getY() {
        return y;
    }

    public LineVector getX() {
        return x;
    }
}
