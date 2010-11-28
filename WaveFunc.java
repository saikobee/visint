public class WaveFunc
implements Func {
    public float valueAt(float x, float z) {
        return Math.sin(x) + 4f;
    }

    public float[] normalAt(float x, float z) {
        return new float[] {
            Math.cos(x), 0, 0
        };
    }
}
