/**
 * @author Brian Mock
 */
public class SaddleFunc
extends Func {
    protected float scale  = 0.01f;

    public float
    valueAt(float x, float z) {
        return (float) (scale * (x*x - z*z));
    }

    public float
    xPartial(float x) {
        return (float) (scale * 2*x);
    }
    public float
    zPartial(float z) {
        return (float) (scale * 2*z);
    }

    public String
    toString() {
        return "x^2 - y^2";
    }
}
