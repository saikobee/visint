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
 * This class is a sin(x) + sin(y) funky raised surface function.
 * @author Brian Mock
 */
public class WavierFunc
extends WaveFunc {
    public WavierFunc() {
        this.scale  =  0.50f;
        this.period = 10.00f;
        calcFreq();
    }

    public float
    valueAt(float x, float z) {
        return valueX(x) + valueZ(z);
    }

    private float
    valueX(float x) {
        return (float) (scale * x * Math.sin(freq * x));
    }
    private float
    valueZ(float z) {
        return (float) (scale * z * Math.sin(freq * z));
    }

    public float
    xPartial(float x, float z) {
        float fPrimeG = (float) (scale * Math.sin(freq * x));
        float fGPrime = (float) (freq * scale * x * Math.cos(freq * x));

        return fPrimeG + fGPrime;
    }
    public float
    zPartial(float x, float z) {
        float fPrimeG = (float) (scale * Math.sin(freq * z));
        float fGPrime = (float) (freq * scale * z * Math.cos(freq * z));

        return fPrimeG + fGPrime;
    }

    public String
    toString() {
        return "sin(x) + sin(y)";
    }
}
