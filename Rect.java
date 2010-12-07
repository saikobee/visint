import javax.media.opengl.GL;
import com.sun.opengl.util.*;
import java.nio.*;
import java.util.*;

/**
 * This class draws a rectangle based on the given points.
 * @author Brian Mock
 */
public class Rect {
    private float[] p1;
    private float[] p2;
    private float[] p3;
    private float[] p4;

    private float[] normal;

    private float[] color = {1, 1, 1, 1};

    private FloatBuffer vertexBuffer;
    private FloatBuffer normalBuffer;
    private FloatBuffer  colorBuffer;

    public
    Rect(
        float[] p1, float[] p2,
        float[] p3, float[] p4
    ) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;

        float[] tmp1 = Util.displacementVector(p2, p1);
        float[] tmp2 = Util.displacementVector(p2, p3);

        normal = Util.normalize(Util.cross(tmp1, tmp2));

        makeBuffers();
    }

    public float[][]
    getVertices() {
        return new float[][] {
            p1, p2, p3, p4
        };
    }

    private void
    makeBuffers() {
        allocBuffers();

        vertexBuffer.put(p1);
        vertexBuffer.put(p2);
        vertexBuffer.put(p3);
        vertexBuffer.put(p4);

        normalBuffer.put(normal);
        normalBuffer.put(normal);
        normalBuffer.put(normal);
        normalBuffer.put(normal);

        colorBuffer .put(color);
        colorBuffer .put(color);
        colorBuffer .put(color);
        colorBuffer .put(color);

        vertexBuffer.rewind();
        normalBuffer.rewind();
        colorBuffer .rewind();
    }

    private void
    allocBuffers() {
        // 4 points in 3-space,
        // 4 normals in 3-space,
        // 4 colors with 4 components each
        vertexBuffer = BufferUtil.newFloatBuffer(4 * 3);
        normalBuffer = BufferUtil.newFloatBuffer(4 * 3);
        colorBuffer  = BufferUtil.newFloatBuffer(4 * 4);
    }

    public void
    draw(GL gl) {
        vertexArraySetup(gl);
        gl.glDrawArrays(
            gl.GL_QUADS,
            0, vertexBuffer.capacity()
        );
        vertexArrayUnsetup(gl);
    }

    private void
    vertexArraySetup(GL gl) {
        gl.glEnableClientState(gl.GL_COLOR_ARRAY);
        gl.glEnableClientState(gl.GL_NORMAL_ARRAY);
        gl.glEnableClientState(gl.GL_VERTEX_ARRAY);

        gl.glVertexPointer(3, gl.GL_FLOAT, 0, vertexBuffer);
        gl.glNormalPointer(   gl.GL_FLOAT, 0, normalBuffer);
        gl.glColorPointer (4, gl.GL_FLOAT, 0,  colorBuffer);
    }

    private void
    vertexArrayUnsetup(GL gl) {
        gl.glDisableClientState(gl.GL_COLOR_ARRAY);
        gl.glDisableClientState(gl.GL_NORMAL_ARRAY);
        gl.glDisableClientState(gl.GL_VERTEX_ARRAY);
    }
}
