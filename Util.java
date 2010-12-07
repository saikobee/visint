/**
 * @author Brian Mock
 */
public class Util {
    static void
    ensure(boolean condition) {
        if (! condition) {
            throw new RuntimeException();
        }
    }

    static void
    ensure(boolean condition, String message) {
        if (! condition) {
            throw new RuntimeException(message);
        }
    }

    static float[]
    cross(float[] a, float[] b) {
        // 23-32, 31-13, 12-21
        return new float[] {
            a[1]*b[2] - a[2]*b[1],
            a[2]*b[0] - a[0]*b[2],
            a[0]*b[1] - a[1]*b[0]
        };
    }

    static float
    magnitude(float[] v) {
        float x = v[0];
        float y = v[1];
        float z = v[2];

        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    static float[]
    scale(float k, float[] v) {
        return new float[] {
            k*v[0],
            k*v[1],
            k*v[2]
        };
    }

    static float[]
    normalize(float[] v) {
        float m = magnitude(v);
        return scale(1f/m, v);
    }

    static float[]
    displacementVector(float[] p1, float[] p2) {
        return new float[] {
            p1[0] - p2[0],
            p1[1] - p2[1],
            p1[2] - p2[2]
        };
    }
}
