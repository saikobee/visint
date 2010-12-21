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
 * This class makes a shape that is somewhat like a square prism.
 * The faces all look like rectangles in the xz plane,
 * but when viewed otherwise, the top face is slightly uneven.
 *
 * This is used to show a small volume element between the xz plane
 * and a surface.
 *
 * @author Brian Mock
 */
public class Prismoid {
    private Rect bottom;
    private Rect top;
    private Rect side1;
    private Rect side2;
    private Rect side3;
    private Rect side4;

    private float[][] topPoints;
    private float[][] botPoints;

    private boolean positive;

    public
    Prismoid(Rect top) {
        this.top = top;

        topPoints = top.getVertices();

        // The bottom is merely the top dropped into the xz plane
        botPoints = new float[][] {
            Util.zeroOutY(topPoints[0]),
            Util.zeroOutY(topPoints[1]),
            Util.zeroOutY(topPoints[2]),
            Util.zeroOutY(topPoints[3])
        };

        float[] a = botPoints[0];
        float[] b = botPoints[1];
        float[] c = botPoints[2];
        float[] d = botPoints[3];

        bottom = new Rect(a, b, c, d);

        side1 = makeRect(0, 1);
        side2 = makeRect(1, 2);
        side3 = makeRect(2, 3);
        side4 = makeRect(3, 0);
    }

    private Rect
    makeRect(int i, int j) {
        /* Remember the GL quad drawing order:
         *
         * 2__3
         * |  |
         * 1--4
         */

        float[] a = botPoints[i];
        float[] b = topPoints[i];
        float[] c = topPoints[j];
        float[] d = botPoints[j];

        return new Rect(a, b, c, d);
    }

    public void
    draw(GL gl) {
        Rect[] rects = {
            bottom, top,
            side1, side2,
            side3, side4
        };

        for (Rect rect: rects) {
            rect.draw(gl);
        }
    }
}
