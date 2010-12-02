import javax.media.opengl.GL;
import com.sun.opengl.util.*;
import java.nio.*;
import java.util.*;

/**
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
        Debug.println("Begin vertexArraySetup");
        gl.glEnableClientState(gl.GL_COLOR_ARRAY);
        gl.glEnableClientState(gl.GL_NORMAL_ARRAY);
        gl.glEnableClientState(gl.GL_VERTEX_ARRAY);

        gl.glVertexPointer(3, gl.GL_FLOAT, 0, vertexBuffer);
        gl.glColorPointer (4, gl.GL_FLOAT, 0,  colorBuffer);
        Debug.println("End vertexArraySetup");
    }

    private void
    vertexArrayUnsetup(GL gl) {
        Debug.println("Begin vertexArrayUnsetup");
        gl.glDisableClientState(gl.GL_COLOR_ARRAY);
        gl.glDisableClientState(gl.GL_VERTEX_ARRAY);
        Debug.println("End vertexArrayUnsetup");
    }

    private void
    buildBuffers() {
        // Both buffers are four elements long...
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

        gl.glDrawArrays(
            gl.GL_POINTS,
            // starting index, number of elemenets
            0, 2
        );

        gl.glDrawArrays(
            gl.GL_LINE,
            // starting index, number of elemenets
            0, 2
        );

        vertexArrayUnsetup(gl);
    }
}
