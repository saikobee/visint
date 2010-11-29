public class Debug {
    public static boolean debug = true;

    private static String separator =
        "========================================";

    public static void println (String s) { if (debug) System.err.println(s); }
    public static void print   (String s) { if (debug) System.err.print  (s); }

    public static void printSep() { println(separator); }
}
