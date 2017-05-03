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

package com.diedrico.diedricoapp.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.diedrico.diedricoapp.vector.Diedrico;

/**
 * Created by amil101 on 23/04/16.
 */

public class MyGLSurfaceView extends GLSurfaceView {

    MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        //fix for error No Config chosen, but I don't know what this does.
        super.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer(new Diedrico(null, null, null, null));
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public MyGLSurfaceView(Context context, Renderer renderer){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        setRenderer(renderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }
}
