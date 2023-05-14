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

data class Dependency(val configuration: DependencyConfiguration,
                      val group: String, val artifact: String,
                      val version: String
) {

  fun value(): String {
    return """
      ${configuration.configName}("${group}:${artifact}:${version}")
    """.trimIndent()
  }

  object AndroidX {

    // Version 2.6.1 results in 'duplicate classes' build issue
    private const val lifecycleVersion = "2.5.1"

    private const val navigationVersion = "2.5.3"

    @JvmStatic
    val AppCompat = parseDependency("androidx.appcompat:appcompat:1.6.1")

    @JvmStatic
    val ConstraintLayout =
      parseDependency("androidx.constraintlayout:constraintlayout:2.1.4")

    @JvmStatic
    val LifeCycle_LiveData = parseDependency(
      "androidx.lifecycle:lifecycle-livedata:${lifecycleVersion}")

    @JvmStatic
    val LifeCycle_LiveData_Ktx = parseDependency(
      "androidx.lifecycle:lifecycle-livedata-ktx:${lifecycleVersion}")

    @JvmStatic
    val LifeCycle_ViewModel = parseDependency(
      "androidx.lifecycle:lifecycle-viewmodel:${lifecycleVersion}")

    @JvmStatic
    val LifeCycle_ViewModel_Ktx = parseDependency(
      "androidx.lifecycle:lifecycle-viewmodel-ktx:${lifecycleVersion}")

    @JvmStatic
    val Navigation_Fragment = parseDependency(
      "androidx.navigation:navigation-fragment:${navigationVersion}")

    @JvmStatic
    val Navigation_Ui =
      parseDependency("androidx.navigation:navigation-ui:${navigationVersion}")

    @JvmStatic
    val Navigation_Fragment_Ktx = parseDependency(
      "androidx.navigation:navigation-fragment-ktx:${navigationVersion}")

    @JvmStatic
    val Navigation_Ui_Ktx = parseDependency(
      "androidx.navigation:navigation-ui-ktx:${navigationVersion}")
  }

  object Google {

    @JvmStatic
    val Material = parseDependency("com.google.android.material:material:1.9.0")
  }
}