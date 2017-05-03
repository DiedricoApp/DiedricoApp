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

package com.diedrico.diedricoapp.vector;

import com.diedrico.diedricoapp.opengl.models.Model;
import java.util.List;

/**
 * Created by amil101 on 27/12/16.
 */
public class Diedrico {
    private List<PointVector> points;
    private List<LineVector> lines;
    private List<PlaneVector> planes;
    private List<Model> models;

    public Diedrico(List<PointVector> points, List<LineVector> lines, List<PlaneVector> planes, List<Model> models){
        this.points = points;
        this.lines = lines;
        this.planes = planes;
        this.models = models;
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

    public List<Model> getModels() {
        return models;
    }
}
