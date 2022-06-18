/*
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
 */
package com.itsaky.inflater.util;

import com.itsaky.inflater.InflateException;

public class Preconditions {

  public static void assertNotnull(Object obj, String msg) throws InflateException {
    if (obj == null) {
      throw new NullPointerException(msg);
    }
  }

  public static void assertAllNotNull(String msg, Object... objs) {
    int i = 0;
    for (Object obj : objs) {
      if (obj == null) {
        throw new NullPointerException("[" + i + "] " + msg);
      }
      ++i;
    }
  }

  public static void assertNotBlank(String str, String msg) throws InflateException {
    Preconditions.assertNotnull(str, msg);
    if (str.trim().length() <= 0) {
      throw new InflateException(msg);
    }
  }

  public static void assertTrue(boolean condition, String message) {
    if (!condition) {
      throw new AssertionError(message);
    }
  }
}
