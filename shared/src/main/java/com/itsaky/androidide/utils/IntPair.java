/*
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2023  Rosemoe
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 *
 *     Please contact Rosemoe by email 2073412493@qq.com if you need
 *     additional information or have any questions
 */

package com.itsaky.androidide.utils;

/**
 * Pack two int into a long Also unpack it This is convenient while passing data
 *
 * @author Rose
 */
public class IntPair {

  /**
   * Convert an integer to a long whose binary bits are equal to the given integer
   */
  private static long toUnsignedLong(int x) {
    return ((long) x) & 0xffffffffL;
  }

  /**
   * Pack two int into a long
   *
   * @param first  First of pair
   * @param second Second of pair
   * @return Packed value
   */
  public static long pack(int first, int second) {
    return (toUnsignedLong(first) << 32L) | toUnsignedLong(second);
  }

  /**
   * Get second of pair
   *
   * @param packedValue Packed value
   * @return Second of pair
   */
  public static int getSecond(long packedValue) {
    return (int) (packedValue & 0xFFFFFFFFL);
  }

  /**
   * Get first of pair
   *
   * @param packedValue Packed value
   * @return First of pair
   */
  public static int getFirst(long packedValue) {
    return (int) (packedValue >> 32L);
  }

}
