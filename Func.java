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
 * This abstract class contains the common framework for functions
 * of two variables.
 * @author Brian Mock
 */
public abstract class Func {
    protected static final float PI     = (float) Math.PI;
    protected static final float TWO_PI = 2f * PI;

    /** Returns the y-value at a given (x, z). */
    public abstract float
    valueAt(float x, float z);

    /** Returns the partial derivative with respect to x. */
    public abstract float
    xPartial(float x, float z);

    /** Returns the partial derivative with respect to z. */
    public abstract float
    zPartial(float x, float z);

    /** Returns the normal at a given (x, z). */
    public float[]
    normalAt(float x, float z) {
        return new float[] {
            xPartial(x, z), 0, zPartial(x, z)
        };
    }
}
