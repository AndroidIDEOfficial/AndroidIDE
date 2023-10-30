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

package com.itsaky.androidide.projects.classpath

/**
 * Information about a class in a JAR file.
 *
 * @author Akash Yadav
 */
@Suppress("DataClassPrivateConstructor")
data class ClassInfo
private constructor(
  val name: String,
  val simpleName: String,
  val packageName: String,
  val isTopLevel: Boolean,
  val isAnonymous: Boolean,
  val isLocal: Boolean,
  val isInner: Boolean
) {

  companion object {

    @JvmStatic
    fun create(name: String): ClassInfo? {
      val isTopLevel = name.indexOf('$') == -1

      val simpleName =
        if (!isTopLevel) {
          name.substringAfterLast('$')
        } else if (name.indexOf('.') != -1) {
          name.substringAfterLast('.')
        } else {
          name
        }

      if (simpleName.isBlank()) {
        return null
      }

      val packageName =
        if (name.contains('.')) {
          name.substringBeforeLast('.')
        } else {
          name
        }

      val isAnonymous = simpleName.isDigitsOnly()
      val isLocal = simpleName[0].isDigit() && simpleName.contains(Regex("[A-Za-z]"))
      val isInner = !isTopLevel && !isLocal && !isAnonymous

      return ClassInfo(name, simpleName, packageName, isTopLevel, isAnonymous, isLocal, isInner)
    }

    private fun CharSequence.isDigitsOnly(): Boolean {
      for (char in this) {
        if (!char.isDigit()) {
          return false
        }
      }

      return true
    }
  }
}
