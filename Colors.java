/**
 * @author Brian Mock
 */
public class Colors {
    // Based on Tango colors
    public static final float[] RED    = fromRGB(0xa40000);
    public static final float[] GREEN  = fromRGB(0x4e9a06);
    public static final float[] BLUE   = fromRGB(0x204a87);
    public static final float[] WHITE  = fromRGB(0xd3d7cf);
    public static final float[] PURPLE = fromRGB(0xad5ca4);
    public static final float[] YELLOW = fromRGB(0xc4a000);
    public static final float[] ORANGE = fromRGB(0xce5c00);
    public static final float[] BLACK  = fromRGB(0x2e3436);
    public static final float[] GREY   = fromRGB(0x555753);
    public static final float[] GRAY   = fromRGB(0x555753);

    public static float[]
    fromRGB(int rgb) {
       int r = (rgb >> (2*8)) & 0xff;
       int g = (rgb >> (1*8)) & 0xff;
       int b = (rgb >> (0*8)) & 0xff;

       float rf = r/255f;
       float gf = g/255f;
       float bf = b/255f;

       return new float[] {rf, gf, bf, 1};
    }

    public static float[]
    withAlpha(float[] color, float alpha) {
        float r = color[0];
        float g = color[1];
        float b = color[2];
        float a = alpha;

        return new float[] {r, g, b, a};
    }
}
