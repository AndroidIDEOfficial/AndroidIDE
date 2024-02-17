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

import com.itsaky.androidide.build.config.BuildConfig

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-parcelize")
  id("com.google.devtools.ksp") version libs.versions.ksp
}

android { namespace = "${BuildConfig.packageName}.inflater" }

dependencies {
  ksp(projects.annotationProcessorsKsp)

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
  testImplementation(projects.testing.tooling)
}
