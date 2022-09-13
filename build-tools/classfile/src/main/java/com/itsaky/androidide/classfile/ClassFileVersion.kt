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

package com.itsaky.androidide.classfile

/**
 * Version info for the class file.
 *
 * @author Akash Yadav
 */
enum class ClassFileVersion(val major: Int, val minor: Int) {
  JAVA_1_0(45, 0),
  JAVA_1_1(45, 3),
  JAVA_1_2(46, 0),
  JAVA_1_3(47, 0),
  JAVA_1_4(48, 0),
  JAVA_5(49, 0),
  JAVA_6(50, 0),
  JAVA_7(51, 0),
  JAVA_8(52, 0),
  JAVA_9(53, 0),
  JAVA_10(54, 0),
  JAVA_11(55, 0),
  JAVA_12(56, 0),
  JAVA_13(57, 0),
  JAVA_14(58, 0),
  JAVA_15(59, 0),
  JAVA_16(60, 0),
  JAVA_17(61, 0);

  companion object {
    internal fun get(major: Int, minor: Int): ClassFileVersion {
      return values().firstOrNull { it.major == major && it.minor == minor }
        ?: throw IllegalArgumentException("Unknown class file version '${major}.${minor}'")
    }
  }
}
