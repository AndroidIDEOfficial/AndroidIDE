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
  id("org.jetbrains.kotlin.android")
}



android {
  namespace = "${BuildConfig.packageName}.stats"
}

dependencies {
  implementation(libs.androidx.work)
  implementation(libs.androidx.work.ktx)
  implementation(libs.common.retrofit)
  implementation(libs.common.retrofit.gson)
  implementation(libs.common.utilcode)
  implementation(libs.google.gson)

  implementation(projects.core.common)
  implementation(projects.logging.logger)
  implementation(projects.utilities.buildInfo)
  implementation(projects.utilities.preferences)

  androidTestImplementation(libs.tests.androidx.junit)
  androidTestImplementation(libs.tests.androidx.test.core)
  androidTestImplementation(libs.tests.androidx.test.runner)
  androidTestImplementation(libs.tests.androidx.test.rules)
  androidTestImplementation(libs.tests.androidx.work.testing)
  androidTestImplementation(libs.androidx.work)
  androidTestImplementation(libs.androidx.work.ktx)
  androidTestImplementation(libs.tests.google.truth)
}