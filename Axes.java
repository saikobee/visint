/* Copyright 2010 Brian Mock
 *
 *  This file is part of visint.
 *
 *  visint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  visint is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with visint.  If not, see <http://www.gnu.org/licenses/>.
 */

import javax.media.opengl.GL;

/**
 * This class draws the x, y, and z axes.
 * @author Brian Mock
 */
public class Axes {
    private Line xAxis;
    private Line yAxis;
    private Line zAxis;

    private float length = 1000;

    private float[] xEnd = {length, 0,      0     };
    private float[] yEnd = {0,      length, 0     };
    private float[] zEnd = {0,      0,      length};

    private float[] origin = {0, 0, 0};

    private float[] xColor = Colors.RED;
    private float[] yColor = Colors.GREEN;
    private float[] zColor = Colors.BLUE;

    public
    Axes() {
        xAxis = new Line(origin, xEnd, xColor);
        yAxis = new Line(origin, yEnd, yColor);
        zAxis = new Line(origin, zEnd, zColor);
    }

    public void
    draw(GL gl) {
        xAxis.draw(gl);
        yAxis.draw(gl);
        zAxis.draw(gl);
    }
}
