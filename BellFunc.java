/**
 * @author Brian Mock
 */
public class BellFunc
extends Func {
    protected float scale      = 25;
    protected float otherScale =  0.05f;

    public float
    valueAt(float x, float z) {
        return (float) (scale * Math.pow(Math.E, -Math.pow(otherScale*x, 2) - Math.pow(otherScale*z, 2)));
    }

    public float
    xPartial(float x, float z) {
        return (float) (scale * 2*x) * valueAt(x, z);
    }
    public float
    zPartial(float x, float z) {
        return (float) (scale * 2*z) * valueAt(x, z);
    }

    public String
    toString() {
        return "x^2 - y^2";
    }
}
