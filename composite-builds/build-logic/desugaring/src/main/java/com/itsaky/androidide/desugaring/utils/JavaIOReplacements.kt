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

import com.itsaky.androidide.desugaring.core.java.io.DesugarInputStream
import com.itsaky.androidide.desugaring.dsl.DesugarReplacementsContainer
import java.io.InputStream
import kotlin.reflect.jvm.javaMethod

/**
 * Replacements for the `java.io` package.
 *
 * @author Akash Yadav
 */
object JavaIOReplacements {

  /**
   * Apply replacements for the `java.io` package.
   */
  fun DesugarReplacementsContainer.applyJavaIOReplacements() {
    replaceMethod(
      InputStream::class.java.getDeclaredMethod("readNBytes", Int::class.java),
      DesugarInputStream::class.java.getDeclaredMethod("readNBytes",
        InputStream::class.java, Int::class.java)
    )

    replaceMethod(
      InputStream::class.java.getDeclaredMethod(
        "readNBytes",
        ByteArray::class.java,
        Int::class.java,
        Int::class.java
      ),
      DesugarInputStream::class.java.getDeclaredMethod(
        "readNBytes",
        InputStream::class.java,
        ByteArray::class.java,
        Int::class.java,
        Int::class.java
      )
    )

    replaceMethod(
      InputStream::readAllBytes.javaMethod!!,
      DesugarInputStream::readAllBytes.javaMethod!!
    )

    replaceMethod(
      InputStream::transferTo.javaMethod!!,
      DesugarInputStream::transferTo.javaMethod!!
    )
  }
}