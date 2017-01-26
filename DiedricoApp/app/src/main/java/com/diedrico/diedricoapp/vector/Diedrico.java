package com.diedrico.diedricoapp.vector;

import java.util.List;

/**
 * Created by amil101 on 27/12/16.
 */
public class Diedrico {
    private List<PointVector> points;
    private List<LineVector> lines;
    private List<PlaneVector> planes;
    //private List<ImportModel> models;

    public Diedrico(List<PointVector> points, List<LineVector> lines, List<PlaneVector> planes){
        this.points = points;
        this.lines = lines;
        this.planes = planes;
        //this.models = models;
    }

    public List<PointVector> getPoints(){
        return points;
    }

    public List<LineVector> getLines(){
        return lines;
    }

    public List<PlaneVector> getPlanes(){
        return planes;
    }
/*
    public List<ImportModel> getModels(){
        return models;
    }

    */
}
