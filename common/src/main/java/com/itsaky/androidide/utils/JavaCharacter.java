/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 **************************************************************************************/
package com.itsaky.androidide.utils;

import java.util.Arrays;

/**
 * @author Rose Get whether Identifier part/start quickly
 */
public class JavaCharacter {

  /** Compressed bit set for isJavaIdentifierStart() */
  private static int[] state_start;

  /** Compressed bit set for isJavaIdentifierPart() */
  private static int[] state_part;

  /** Init maps */
  public static void initMap() {
    if (state_start != null) {
      return;
    }
    state_part = new int[2048];
    state_start = new int[2048];
    Arrays.fill(state_part, 0);
    Arrays.fill(state_start, 0);
    for (int i = 0; i <= 65535; i++) {
      if (Character.isJavaIdentifierPart((char) i)) {
        set(state_part, i);
      }
      if (Character.isJavaIdentifierStart((char) i)) {
        set(state_start, i);
      }
    }
  }

  /**
   * Make the given position's bit true
   *
   * @param values Compressed bit set
   * @param bitIndex Index of bit
   */
  private static void set(int[] values, int bitIndex) {
    values[bitIndex / 32] |= (1 << (bitIndex % 32));
  }

  /**
   * @param key Character
   * @return Whether a identifier part
   * @see Character#isJavaIdentifierPart(char)
   */
  public static boolean isJavaIdentifierPart(int key) {
    return get(state_part, key);
  }

  /**
   * Get bit in compressed bit set
   *
   * @param values Compressed bit set
   * @param bitIndex Target index
   * @return Boolean value at the index
   */
  private static boolean get(int[] values, int bitIndex) {
    return ((values[bitIndex / 32] & (1 << (bitIndex % 32))) != 0);
  }

  /**
   * @param key Character
   * @return Whether a identifier start
   * @see Character#isJavaIdentifierStart(char)
   */
  public static boolean isJavaIdentifierStart(int key) {
    return get(state_start, key);
  }
}
