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

    public
    Line(float[] begin, float[] end, float[] color) {
        init(begin, end, color);
    }

    private void
    init(float[] begin, float[] end, float[] color) {
        this.begin = begin;
        this.end   = end;
        this.color = color;

        vertexBuffer = BufferUtil.newFloatBuffer(2);
        colorBuffer  = BufferUtil.newFloatBuffer(2);

        colorBuffer = 
    }

    private void
    buildBuffers() {
        vertexBuffer.put(begin);
        vertexBuffer.put(end);
        vertexBuffer.rewind();

        colorBuffer.put(color);
        colorBuffer.put(color);
        colorBuffer.rewind();
    }

    private void draw(GL gl) {
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
    }
}
