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

package com.itsaky.androidide.testing.common.utils

/**
 * Information about the operating system.
 *
 * @author Akash Yadav
 */
object OperatingSystem {

  /**
   * The name of the operating system.
   */
  val OS = System.getProperty("os.name")

  /**
   * True if the operating system is Windows.
   */
  var IS_WINDOWS: Boolean = OS.indexOf("win") >= 0

  /**
   * True if the operating system is Mac.
   */
  var IS_MAC: Boolean = OS.indexOf("mac") >= 0

  /**
   * True if the operating system is Unix.
   */
  var IS_UNIX: Boolean = OS.indexOf("nix") >= 0 ||
    OS.indexOf("nux") >= 0 ||
    OS.indexOf("aix") > 0

  /**
   * True if the operating system is Solaris.
   */
  var IS_SOLARIS: Boolean = OS.indexOf("sunos") >= 0
}