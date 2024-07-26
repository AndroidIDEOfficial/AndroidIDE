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

package com.itsaky.androidide.utils;

import java.io.FileDescriptor;
import java.lang.reflect.Field;

/**
 * @author Akash Yadav
 */
public final class FDUtils {
  private FDUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * Get the file descriptor for the
   * @param descriptor
   * @return
   */
  public static int getFd(FileDescriptor descriptor) {
    try {
      Field fd;
      try {
        // Android
        fd = descriptor.getClass().getDeclaredField("descriptor");
      } catch (NoSuchFieldException e) {
        // JVM
        fd = descriptor.getClass().getDeclaredField("fd");
      }

      fd.setAccessible(true);
      return fd.getInt(descriptor);
    } catch (NoSuchFieldException
             | IllegalAccessException
             | IllegalArgumentException e
    ) {
      throw new RuntimeException(e);
    }
  }
}
