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
import com.sun.opengl.util.*;
import java.nio.*;
import java.util.*;

/**
 * This class controls drawing a single line.
 * @author Brian Mock
 */
public class Line {
    private float[] begin;
    private float[] end;

    private float[] color;

    private FloatBuffer vertexBuffer;
    private FloatBuffer  colorBuffer;

    public
    Line(float[] begin, float[] end, float[] color) {
        init(begin, end, color);
    }

    private void
    init(float[] begin, float[] end, float[] color) {
        this.begin = begin;
        this.end   = end;
        this.color = color;

        buildBuffers();
    }

    private void
    vertexArraySetup(GL gl) {
        gl.glEnableClientState(gl.GL_VERTEX_ARRAY);
        gl.glEnableClientState(gl.GL_COLOR_ARRAY);

        gl.glVertexPointer(3, gl.GL_FLOAT, 0, vertexBuffer);
        gl.glColorPointer (4, gl.GL_FLOAT, 0,  colorBuffer);
    }

    private void
    vertexArrayUnsetup(GL gl) {
        gl.glDisableClientState(gl.GL_VERTEX_ARRAY);
        gl.glDisableClientState(gl.GL_COLOR_ARRAY);
    }

    private void
    buildBuffers() {
        // Both buffers are 2 elements long...
        // One contains 3 numbers per vertex,
        // The other contains 4 values per color
        vertexBuffer = BufferUtil.newFloatBuffer(2 * 3);
        colorBuffer  = BufferUtil.newFloatBuffer(2 * 4);

        vertexBuffer.put(begin);
        vertexBuffer.put(end);
        vertexBuffer.rewind();

        colorBuffer.put(color);
        colorBuffer.put(color);
        colorBuffer.rewind();
    }

    public void
    draw(GL gl) {
        vertexArraySetup(gl);

        gl.glDrawArrays(gl.GL_LINE_LOOP, 0, vertexBuffer.capacity());
        //gl.glDrawArrays(gl.GL_POINTS,    0, vertexBuffer.capacity());

        vertexArrayUnsetup(gl);
    }
}
