public class Colors {
    public static final float[] RED    = fromRGB(0xa40000);
    public static final float[] GREEN  = fromRGB(0x4e9a06);
    public static final float[] BLUE   = fromRGB(0x204a87);
    public static final float[] WHITE  = fromRGB(0xd3d7cf);
    public static final float[] PURPLE = fromRGB(0x5c3566);
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
