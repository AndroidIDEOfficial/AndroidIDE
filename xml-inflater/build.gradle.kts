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
  id("kotlin-android")
  id("kotlin-kapt")
  id("kotlin-parcelize")
}

android { namespace = "com.itsaky.androidide.inflater" }

dependencies {
  
  kapt(projects.annotationProcessors)
  
  implementation(libs.androidx.appcompat)
  implementation(libs.common.kotlin)
  implementation(libs.common.utilcode)

  implementation(projects.annotations)
  implementation(projects.common)
  implementation(projects.subprojects.aaptcompiler)
  implementation(projects.subprojects.projects)
  implementation(projects.subprojects.xmlUtils)
  implementation(projects.resources)

  testImplementation(projects.subprojects.projects)
  testImplementation(projects.subprojects.toolingApiTesting)
  testImplementation(libs.tests.junit)
  testImplementation(libs.tests.google.truth)
  testImplementation(libs.tests.robolectric)
}
