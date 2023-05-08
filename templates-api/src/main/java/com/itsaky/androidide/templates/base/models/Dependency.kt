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

package com.itsaky.androidide.templates.base.models

data class Dependency(val configuration: DependencyConfiguration, val group: String,
                      val artifact: String, val version: String
) {

  fun value(): String {
    return """
      ${configuration.configName}("${group}:${artifact}:${version}")
    """.trimIndent()
  }

  object AndroidX {

    @JvmStatic
    val AppCompat = parseDependency("androidx.appcompat:appcompat:1.6.1")

    @JvmStatic
    val ConstraintLayout = parseDependency("androidx.constraintlayout:constraintlayout:2.1.4")
  }

  object Google {

    @JvmStatic
    val Material = parseDependency("com.google.android.material:material:1.9.0")
  }
}