import java.util.*;

/**
 * This program controls printing debugging output (or not!).
 * @author Brian Mock
 */
public class Debug {
    public static boolean debug = false;

    private static String separator =
        "========================================";

    public static void error   (String s) { if (debug) System.err.println(s); }
    public static void println (String s) { if (debug) System.out.println(s); }
    public static void print   (String s) { if (debug) System.out.print  (s); }

    public static void printAry(Object[] ary) { println(Arrays.deepToString(ary)); }

    public static void printSep() { println(separator); }
}
