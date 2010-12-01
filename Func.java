/**
 * @author Brian Mock
 */
public abstract class Func {
    protected static final float PI     = (float) Math.PI;
    protected static final float TWO_PI = 2f * PI;

    /** Returns the y-value at a given (x, z). */
    public abstract float valueAt(float x, float z);

    /** Returns the partial derivative with respect to x. */
    public abstract float xPartial(float x);

    /** Returns the partial derivative with respect to z. */
    public abstract float zPartial(float z);

    /** Returns the normal at a given (x, z). */
    public float[] normalAt(float x, float z) {
        return new float[] {
            xPartial(x), 0, zPartial(z)
        };
    }
}
