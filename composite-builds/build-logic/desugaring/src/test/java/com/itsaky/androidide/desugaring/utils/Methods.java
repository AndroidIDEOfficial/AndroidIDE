/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.desugaring.utils;

/**
 * @author Akash Yadav
 */
@SuppressWarnings("unused")
public class Methods {

  public static void primitive(byte a, short b, int c, long d, float e, double f, char g, boolean h
  ) {
  }

  public static void array(byte[] a, short[] b, int[] c, long[] d, float[] e, double[] f, char[] g,
                           boolean[] h
  ) {
  }

  public static void nested(byte[][] a, short[][] b, int[][] c, long[][] d, float[][] e,
                            double[][] f, char[][] g, boolean[][] h
  ) {
  }

  public static void references(Byte a, Short b, Integer c, Long d, Float e, Double f, Character g,
                                Boolean h
  ) {
  }

  public static void referenceArrays(Byte[] a, Short[] b, Integer[] c, Long[] d, Float[] e,
                                     Double[] f, Character[] g, Boolean[] h
  ) {
  }

  public static void referenceNested(Byte[][] a, Short[][] b, Integer[][] c, Long[][] d,
                                     Float[][] e, Double[][] f, Character[][] g, Boolean[][] h
  ) {
  }

  public static byte returnsPrimitive() {
    return 0;
  }

  public static byte[] returnsArray() {
    return null;
  }

  public static byte[][] returnsNested() {
    return null;
  }

  public static Byte returnsReference() {
    return null;
  }

  public static Byte[] returnsReferenceArray() {
    return null;
  }

  public static Byte[][] returnsReferenceNested() {
    return null;
  }
}
