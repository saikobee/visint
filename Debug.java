/* Copyright 2010 Brian Mock
 *
 *  This file is part of visint.
 *
 *  visint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  visint is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with visint.  If not, see <http://www.gnu.org/licenses/>.
 */

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
