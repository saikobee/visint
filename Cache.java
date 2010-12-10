import javax.media.opengl.GL;
import com.sun.opengl.util.*;
import java.nio.*;
import java.util.*;

/**
 * @author Brian Mock
 */
public class Cache {
    private Func f;

    private int xBegin = -20;
    private int xEnd   = +20;
    //======================|
    private int zBegin = -20;
    private int zEnd   = +20;

    private int xSize;
    private int zSize;

    private IntBuffer    indexBuffer;
    private FloatBuffer  colorBuffer;
    private FloatBuffer  blackBuffer;
    private FloatBuffer normalBuffer;
    private FloatBuffer vertexBuffer;

    private float[][]   values;
    private float[][][] normals;
    private float[][][] colors;
    private float[][][] vertices;

    private ArrayList<Prismoid> prisms;
    private Thread prismThread;
    private boolean prismThreadDone;
    private boolean prismThreadStarted;

    private float[] outlineColor;

    private float thisOutlineAlpha = 0.25f;

    public
    Cache(Func f) {
        this.f = f;

        outlineColor = Colors.withAlpha(Colors.BLACK, thisOutlineAlpha);

        xSize = rangeLength(xBegin, xEnd);
        zSize = rangeLength(zBegin, zEnd);

        Util.ensure(xEnd > xBegin);
        Util.ensure(zEnd > zBegin);

        int xs1 = xSize - 1;
        int zs1 = zSize - 1;

        indexBuffer  = BufferUtil.newIntBuffer  (xs1   * zs1   * 4);
        colorBuffer  = BufferUtil.newFloatBuffer(xSize * zSize * 4);
        normalBuffer = BufferUtil.newFloatBuffer(xSize * zSize * 3);
        vertexBuffer = BufferUtil.newFloatBuffer(xSize * zSize * 3);

        blackBuffer = Util.bigColorBuffer(outlineColor, xSize * zSize * 4);

        prisms = new ArrayList();

        prismThreadDone    = false;
        prismThreadStarted = false;

        prismThread = makePrismThread();

        values   = new float[xSize][zSize];
        //==================================|
        normals  = new float[xSize][zSize][3];
        colors   = new float[xSize][zSize][4];
        vertices = new float[xSize][zSize][4];

        fillInValues();
        calcVertices();
        calcBuffers();
    }

    private Thread
    makePrismThread() {
        prismThreadDone = false;
        return new Thread() {
            public void run() {
                makePrismThreadHelper();
            }
        };
    }

    private void
    makePrismThreadHelper() {
        for (int x=1; x < xSize; ++x) {
            for (int z=1; z < zSize; ++z) {
                float[] a = vertices[x-0][z-0];
                float[] b = vertices[x-1][z-0];
                float[] c = vertices[x-1][z-1];
                float[] d = vertices[x-0][z-1];

                //boolean positive = Util.allYPositive(a, b, c, d);
                boolean positive = Util.anyYPositive(a, b, c, d);

                prisms.add(new Prismoid(new Rect(
                    a, b, c, d, positive
                )));

                trySleep(0.025);
                if (prismThreadDone) {
                    return;
                }
            }
        }
    }

    private void
    trySleep(double seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        }
        catch (InterruptedException e) {
            prismThreadDone = true;
            Debug.println("Thread sleep interrupted");
        }
    }

    public void
    stopThreads() {
        prismThreadDone = true;
    }

    private void
    calcVertices() {
        for (int x=0; x < xSize; ++x) {
            for (int z=0; z < zSize; ++z) {
                float thisX = mapOutOfZeroBasedRange(x, xBegin);
                float thisZ = mapOutOfZeroBasedRange(z, zBegin);

                vertices[x][z] = new float[] {
                    thisX,
                    f.valueAt(thisX, thisZ),
                    thisZ
                };
            }
        }
    }

    private void
    vertexArraySetup(GL gl) {
        gl.glEnableClientState(gl.GL_COLOR_ARRAY);
        gl.glEnableClientState(gl.GL_NORMAL_ARRAY);
        gl.glEnableClientState(gl.GL_VERTEX_ARRAY);

        gl.glVertexPointer(3, gl.GL_FLOAT, 0, vertexBuffer);
        gl.glNormalPointer(   gl.GL_FLOAT, 0, normalBuffer);
    }

    private void
    loadColor(GL gl) {
        gl.glColorPointer (4, gl.GL_FLOAT, 0,  colorBuffer);
    }

    private void
    loadBlack(GL gl) {
        gl.glColorPointer (4, gl.GL_FLOAT, 0,  blackBuffer);
    }

    private void
    vertexArrayUnsetup(GL gl) {
        gl.glDisableClientState(gl.GL_COLOR_ARRAY);
        gl.glDisableClientState(gl.GL_NORMAL_ARRAY);
        gl.glDisableClientState(gl.GL_VERTEX_ARRAY);
    }

    public void
    calcBuffers() {
        for (int x=1; x < xSize; ++x) {
            for (int z=1; z < zSize; ++z) {
                indexBuffer.put((x-0)*xSize + (z-0));
                indexBuffer.put((x-1)*xSize + (z-0));
                indexBuffer.put((x-1)*xSize + (z-1));
                indexBuffer.put((x-0)*xSize + (z-1));
            }
        }

        for (int x=0; x < xSize; ++x) {
            for (int z=0; z < zSize; ++z) {
                colorBuffer .put(colors  [x][z]);
                normalBuffer.put(normals [x][z]);
                vertexBuffer.put(vertices[x][z]);
            }
        }

        indexBuffer .rewind();
        colorBuffer .rewind();
        normalBuffer.rewind();
        vertexBuffer.rewind();
    }

    public void
    draw(GL gl) {
        if (! prismThreadStarted) {
            prismThreadStarted = true;
            prismThread.start();
        }
        drawPrisms(gl);
        drawFunc(gl);
    }

    private void
    drawFunc(GL gl) {
        vertexArraySetup(gl);

        loadColor(gl);
        drawElements(gl, gl.GL_QUADS );

        loadBlack(gl);
        drawElements(gl, gl.GL_LINES );
        drawElements(gl, gl.GL_POINTS);

        vertexArrayUnsetup(gl);
    }

    private void
    drawElements(GL gl, int drawType) {
        gl.glDrawElements(
            drawType,
            indexBuffer.capacity(),
            gl.GL_UNSIGNED_INT,
            indexBuffer
        );
    }

    private void
    drawPrisms(GL gl) {
        int len = prisms.size();
        for (int i=0; i < len; ++i) {
            prisms.get(i).draw(gl);
        }
    }

    private void fillInValues() {
        for (int x=xBegin; x <= xEnd; ++x) {
            for (int z=zBegin; z <= zEnd; ++z) {
                int xIndex = mapIntoZeroBasedRange(x, xBegin);
                int zIndex = mapIntoZeroBasedRange(z, zBegin);

                float   thisValue  = f. valueAt(x, z);
                float[] thisNormal = f.normalAt(x, z);
                float[] thisColor  = colorAt(thisValue);

                values [xIndex][zIndex] = thisValue;
                normals[xIndex][zIndex] = thisNormal;
                colors [xIndex][zIndex] = thisColor;
            }
        }
    }

    public float[]
    colorAt(float y) {
        return Colors.withAlpha(Colors.WHITE, 0.75f);
    }

    private int
    mapIntoZeroBasedRange(int n, int begin) {
        return n - begin;
    }

    private int
    mapOutOfZeroBasedRange(int n, int begin) {
        return n + begin;
    }

    private int
    rangeLength(int begin, int end) {
        return Math.abs(begin - end) + 1;
    }
}
