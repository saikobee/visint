import java.util.*;

public class Cache {
    private Func f;

    private int xBegin =   0;
    private int xEnd   = +10;
    //======================|
    private int zBegin =   0;
    private int zEnd   = +10;
    //======================|
    private int xSize;
    private int zSize;

    private float[][]   values;
    private float[][][] normals;
    private float[][][] colors;
    private float[][][] vertices;

    public Cache(Func f) {
        this.f = f;

        xSize = Math.abs(xBegin - xEnd) + 1;
        zSize = Math.abs(zBegin - zEnd) + 1;

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
}
