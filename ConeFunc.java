public class ConeFunc
implements Func {
    public float valueAt(float x, float z) {
        return x*x + z*z;
    }

    public float[] normalAt(float x, float z) {
        return new float[] {
            2*x, 0, 2*z
        };
    }
}
