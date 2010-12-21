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

/**
 * This function looks like a saddle.
 * @author Brian Mock
 */
public class SaddleFunc
extends Func {
    protected float scale  = 0.05f;

    public float
    valueAt(float x, float z) {
        return (float) (scale * (x*x - z*z));
    }

    public float
    xPartial(float x, float z) {
        return (float) (scale * 2*x);
    }
    public float
    zPartial(float x, float z) {
        return (float) (scale * 2*z);
    }

    public String
    toString() {
        return "x^2 - y^2";
    }
}
