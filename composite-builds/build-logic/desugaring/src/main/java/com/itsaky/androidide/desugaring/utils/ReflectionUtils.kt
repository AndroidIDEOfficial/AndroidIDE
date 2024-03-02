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

package com.itsaky.androidide.desugaring.utils

import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * Utility methods for reflective accesses.
 *
 * @author Akash Yadav
 */
object ReflectionUtils {

  /**
   * Returns the descriptor for the given method.
   */
  @JvmStatic
  fun describe(method: Method): String {
    val desc = StringBuilder(64)
    desc.append('(')
    for (paramType in method.parameterTypes) {
      desc.append(describe(paramType))
    }
    desc.append(')')
    if (method.returnType != Void.TYPE) {
      desc.append(describe(method.returnType))
    } else {
      desc.append("V")
    }
    return desc.toString()
  }

  /**
   * Returns the binary name of the given class.
   */
  fun describe(clazz: Class<*>): String {
    if (clazz.isArray) {
      // clazz.name returns the binary name
      return clazz.name.replace('.', '/')
    }

    return when (clazz) {
      Byte::class.java -> "B"
      Short::class.java -> "S"
      Char::class.java -> "C"
      Int::class.java -> "I"
      Long::class.java -> "J"
      Float::class.java -> "F"
      Double::class.java -> "D"
      Boolean::class.java -> "Z"
      else -> "L${clazz.name.replace('.', '/')};"
    }
  }

  fun validateVirtualToStaticReplacement(source: Method, target: Method) {
    if (Modifier.isStatic(source.modifiers)) {
      throw IllegalArgumentException("Source method is static")
    }

    if (!Modifier.isStatic(target.modifiers)) {
      throw IllegalArgumentException("Target method is not static")
    }

    if (target.parameterTypes.isEmpty()) {
      throw IllegalArgumentException("Target method has no parameters")
    }

    if (!target.parameterTypes[0].equals(source.declaringClass)) {
      throw IllegalArgumentException("Target method's first parameter type must be source method's declaring class")
    }
  }
}