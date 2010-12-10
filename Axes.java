import javax.media.opengl.GL;

/**
 * This class draws the x, y, and z axes.
 * @author Brian Mock
 */
public class Axes {
    private Line xAxis;
    private Line yAxis;
    private Line zAxis;

    private float length = 1000;

    private float[] xEnd = {length, 0,      0     };
    private float[] yEnd = {0,      length, 0     };
    private float[] zEnd = {0,      0,      length};

    private float[] origin = {0, 0, 0};

    private float[] xColor = Colors.RED;
    private float[] yColor = Colors.GREEN;
    private float[] zColor = Colors.BLUE;

    public
    Axes() {
        xAxis = new Line(origin, xEnd, xColor);
        yAxis = new Line(origin, yEnd, yColor);
        zAxis = new Line(origin, zEnd, zColor);
    }

    public void
    draw(GL gl) {
        xAxis.draw(gl);
        yAxis.draw(gl);
        zAxis.draw(gl);
    }
}
