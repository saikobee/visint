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
 * This function looks like a standard sine wave.
 * @author Brian Mock
 */
public class WaveFunc
extends Func {
    protected float scale  = 16f;
    protected float period = 20f;
    protected float freq;

    protected void
    calcFreq() {
        freq = TWO_PI/period;
    }

    public
    WaveFunc() {
        calcFreq();
    }

    public float
    valueAt(float x, float z) {
        return (float) (scale * Math.sin(freq * x));
    }

    public float
    xPartial(float x, float z) {
        return (float) (freq * scale * Math.cos(freq * x));
    }
    public float
    zPartial(float x, float z) {
        return 0;
    }

    public String
    toString() {
        return "sin(x)";
    }
}
