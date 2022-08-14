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

/**
 * Utilities related to VM.
 *
 * @author Akash Yadav
 */
public class VMUtils {

  private static Boolean isJvm = null;

  /**
   * @return <code>true</code> if the current platform is JVM, <code>false</code> otherwise.
   */
  public static boolean isJvm() {

    if (isJvm != null) {
      return isJvm;
    }

    try {
      // If we're in a testing environment
      Class.forName("org.junit.runners.JUnit4");
      return isJvm = true;
    } catch (ClassNotFoundException e) {
      // ignored
    }

    try {
      Class.forName("android.content.Context");
      return isJvm = false;
    } catch (ClassNotFoundException e) {
      return isJvm = true;
    }
  }
}
