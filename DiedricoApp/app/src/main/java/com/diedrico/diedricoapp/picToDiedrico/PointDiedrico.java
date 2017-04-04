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

    public PointVector getY() {
        return y;
    }

    public PointVector getX() {
        return x;
    }
}
