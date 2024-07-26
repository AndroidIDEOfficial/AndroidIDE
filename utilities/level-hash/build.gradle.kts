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
}

android {
  namespace = "${BuildConfig.packageName}.levelhash"
  ndkVersion = BuildConfig.ndkVersion
}

tasks.withType<Test> {
  jvmArgs("--add-opens", "java.base/java.io=ALL-UNNAMED")
}

dependencies {
  implementation(libs.androidx.collection)
  implementation(libs.google.guava)

  // Let the consuming project decide which platform to use
  compileOnly(libs.jna.core)
  compileOnly(libs.jna.platform)

  implementation(projects.logging.logger)
  implementation(projects.utilities.shared)

  // compileOnly, in order to use JVM variant in tests and Android variant at
  // runtime
  compileOnly(libs.jna.core)
  compileOnly(libs.jna.platform)

  // For JVM
  testImplementation(libs.jna.core)
  testImplementation(libs.jna.platform)

  testImplementation(projects.testing.unitTest)
  androidTestImplementation(projects.testing.androidTest)
}