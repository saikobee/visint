import javax.media.opengl.GL;
import java.util.*;

public class Cache {
    private Func f;

    private int xBegin = -50;
    private int xEnd   = +50;
    //======================|
    private int zBegin = -50;
    private int zEnd   = +50;

    private int xSize;
    private int zSize;

    private float[][]   values;
    private float[][][] normals;
    private float[][][] colors;
    private float[][][] vertices;

    public Cache(Func f) {
        this.f = f;

        xSize = rangeLength(xBegin, xEnd);
        zSize = rangeLength(zBegin, zEnd);

        Util.ensure(xEnd > xBegin);
        Util.ensure(zEnd > zBegin);

        values   = new float[xSize][zSize];
        normals  = new float[xSize][zSize][];
        colors   = new float[xSize][zSize][];
        vertices = new float[xSize][zSize][];

        fillInValues();
        calcVertices();

        Debug.printAry(values);
        Debug.printSep();
        Debug.printAry(normals);
        Debug.printSep();
        Debug.printAry(colors);
        Debug.printSep();
        Debug.printAry(vertices);
    }

    private void calcVertices() {
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

    public void drawImmediate(GL gl) {
        for (int x=1; x < xSize; ++x) {
            gl.glBegin(gl.GL_QUAD_STRIP);
            for (int z=1; z < zSize; ++z) {
                drawPoint(gl, x-0, z-0);
                drawPoint(gl, x-1, z-0);
                drawPoint(gl, x-0, z-1);
                drawPoint(gl, x-1, z-1);
            }
            gl.glEnd();
        }
    }

    public void drawPoint(GL gl, int x, int z) {
        float thisX = mapOutOfZeroBasedRange(x, xBegin);
        float thisZ = mapOutOfZeroBasedRange(z, zBegin);
        float thisY = values[x][z];

        float[] thisNormal = normals[x][z];
        float[] thisColor  =  colors[x][z];

        gl.glNormal3fv(thisNormal, 0);
        gl.glColor3fv(thisColor, 0);
        gl.glVertex3f(thisX, thisY, thisZ);
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

    public float[] colorAt(float y) {
        return new float[] {1, 1, 1, 1};
    }

    int mapIntoZeroBasedRange(int n, int begin) {
        return n - begin;
    }

    int mapOutOfZeroBasedRange(int n, int begin) {
        return n + begin;
    }

    int rangeLength(int begin, int end) {
        return Math.abs(begin - end) + 1;
    }
}
