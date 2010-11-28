import java.util.*;
import java.io.*;

/** Reads in a DEM file and generates the an array of points representing it.
 * @author Brian Mock
 */
public class DEM {
    /** This is the file used to read in the DEM data. */
    private InputStream file;

    /** This holds the minimum value. */
    private float min;

    /** This holds the maximum value. */
    private float max;

    /** This contains the elevations. */
    private ArrayList<ArrayList<Float>> elevations;

    /** This contains the normals. */
    private float[][][] normals;

    public DEM(InputStream file) {
        init(file);
    }

    public DEM(String file) {
        try {
            init(new FileInputStream(file));
        }
        catch (FileNotFoundException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private void init(InputStream file) {
        this.file  = file;
        elevations = new ArrayList<ArrayList<Float>>();

        readFile();
        calcMaxMin();
        normalize();
        calcNormals();
    }


    /** Calculate the minimum and maximum values in the DEM */
    private void calcMaxMin() {
        min = elevations.get(0).get(0);
        max = elevations.get(0).get(0);

        for (int i=0; i < rows(); i++) {
            for (int j=0; j < cols(); j++) {
                float val = elevations.get(i).get(j);
                if (val < min) {
                    min = val;
                }
                else if (val > max) {
                    max = val;
                }
            }
        }
    }

    /** Normalize the DEM values to be in the range [0, 1]. */
    public void normalize() {
        for (int i=0; i < rows(); i++) {
            for (int j=0; j < cols(); j++) {
                float val = elevations.get(i).get(j);
                elevations.get(i).set(j, (val - min) / (max - min));
            }
        }
    }

    public float min() { return min; }
    public float max() { return max; }

    public int rows() { return elevations.size();        }
    public int cols() { return elevations.get(0).size(); }

    /** Read in the DEM data from an ASCII file */
    private void readFile() {
        Scanner s = null;
        s = new Scanner(file);

        while (s.hasNextLine()) {
            String  l = s.nextLine();
            Scanner n = new Scanner(l);

            ArrayList<Float> tmp = new ArrayList<Float>();

            // Skip lines that are entirely whitespace
            if (l.trim() == "") {
                continue;
            }

            // Extract numbers from line
            while (n.hasNextFloat()) {
                tmp.add(n.nextFloat());
            }

            // Add row of elevation data
            elevations.add(tmp);
        }

        normals = new float[rows()][cols()][3];
    }

    /** Return the elevation data as an ArrayList. */
    public ArrayList<ArrayList<Float>> getElevationsList() {
        return elevations;
    }

    private void calcNormals() {
        for (int i=1; i < rows(); ++i) {
            for (int j=1; j < cols(); ++j) {
                //normals[i][j] = new float[] {0.0f, 1.0f, 0.0f};
                //normals[i][j] = new float[] {0.0f, (float) Math.random(), 0.0f};

                normals[i][j] = calcNormFromij(i, j);
            }
        }
    }

    private float get(int i, int j) {
        return elevations.get(i).get(j);
    }

    private float[] calcNormFromij(int i, int j) {
       // x0_0, y0_0 is the bottom left corner of the plane
       float y0_0 = get(i - 1, j - 1) * 1000;
       float y0_1 = get(i - 1, j - 0) * 1000;
       float y1_0 = get(i - 0, j - 1) * 1000;

       float[] a = {i - 1, y0_0, j - 1};
       float[] b = {i - 1, y0_1, j - 0};
       float[] c = {i - 0, y1_0, j - 1};

       float[] a_b = minus3f(b, a);
       float[] a_c = minus3f(c, a);

       //return normalize3f(cross3f(a_b, a_c));
       return normalize3f(cross3f(a_c, a_b));
       //return cross3f(a_b, a_c);
    }

    public float[] normalize3f(float[] a) {
        float len = magnitude3f(a);

        return new float[] {
            a[0] / len,
            a[1] / len,
            a[2] / len
        };
    }

    public float magnitude3f(float[] a) {
        return (float) Math.sqrt(
            a[0] * a[0] +
            a[1] * a[1] +
            a[2] * a[2]
        );
    }

    public float[] minus3f(float[] a, float[] b) {
        return new float[] {
            a[0] - b[0],
            a[1] - b[1],
            a[2] - b[2]
        };
    }

    public float[] cross3f(float[] a, float[] b) {
        float x1 = a[0];
        float y1 = a[1];
        float z1 = a[2];

        float x2 = b[0];
        float y2 = b[1];
        float z2 = b[2];

        // v3 = cross ( v1,  v2) = [ y1*z2 - y2*z1 , z1*x2 - z2*x1 , x1*y2 - x2*y1 ]
        return new float[] {
            y1*z2 - y2*z1,
            z1*x2 - z2*x1,
            x1*y2 - x2*y1
        };
    }

    /** Return the normals as a 3D array. */
    public float[][][] getNormals() {
        return normals;
    }
 
    /** Return the elevation data as an array. */
    public float[][] getElevationsAry() {
        int m = elevations.size();
        int n = elevations.get(0).size();

        float[][] tmp = new float[m][n];

        for (int i=0; i < m; ++i) {
            for (int j=0; j < n; ++j) {
                tmp[i][j] = elevations.get(i).get(j);
            }
        }

        return tmp;
    }

    /** Allows you to iterate the elevations across diagonals.
     * This is not actually needed for anything, but I thought it might be needed.
     * I was having so much fun pursuing this that I ended up writing it even though it was unnecessary.
     */
    public ArrayList<ArrayList<Float>> getElevationsListDiag() {
        int q = rows() * cols();
        ArrayList<ArrayList<Float>> ret = new ArrayList<ArrayList<Float>>();
        for (int a=0; a < q; ++a) {
            int r = 0;
            int c = a;
            ArrayList<Float> tmp = new ArrayList<Float>();

            while (r < rows()) {
                try {
                    tmp.add(elevations.get(r).get(c));
                }
                catch (IndexOutOfBoundsException e) {
                }

                ++r;
                --c;
            }

            if (tmp.size() > 0) {
                ret.add(tmp);
            }
        }

        return ret;
    }
}
