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

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "${BuildConfig.packageName}.templates"
}

dependencies {
  api(projects.subprojects.javapoet)

  implementation(projects.common)
  implementation(projects.logger)
  implementation(projects.resources)
  implementation(projects.subprojects.xmlUtils)

  implementation(libs.androidx.annotation)
  implementation(libs.aapt2.common)

  testImplementation(projects.lsp.api)
  testImplementation(projects.preferences)
  testImplementation(projects.shared)
  testImplementation(libs.tests.androidx.test.core)
  testImplementation(libs.tests.google.truth)
  testImplementation(libs.tests.junit)
  testImplementation(libs.tests.mockk)
  testImplementation(libs.tests.robolectric)
}