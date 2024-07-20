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
  namespace = "${BuildConfig.packageName}.testing.android"
}

configurations.api {
  // There is a transitive dependency on 'protobuf-lite' in 'androidTest' configurations
  // which conflicts with the 'protobuf-javalite' library we use
  // hence we exclude the dependency and add ours manually
  //+--- androidx.test.espresso:espresso-core:<...>
  //     +--- com.google.android.material:material:<...>
  //          \--- com.google.android.apps.common.testing.accessibility.framework:accessibility-test-framework:<...>
  //               +--- com.google.protobuf:protobuf-lite:3.0.1
  exclude(group = "com.google.protobuf", module = "protobuf-lite")
}

dependencies {

  api(libs.google.protobuf)
  api(libs.tests.androidx.espresso.core)
  api(libs.tests.androidx.espresso.contrib)
  api(libs.tests.androidx.junit)
  api(libs.tests.androidx.test.core)
  api(libs.tests.androidx.test.runner)
  api(libs.tests.androidx.test.rules)
  api(libs.tests.androidx.uiautomator)
  api(libs.tests.junit)
  api(libs.tests.google.truth)
  api(libs.tests.barista) {
    exclude("org.jetbrains.kotlin")
  }

  api(projects.core.common)
  api(projects.testing.commonTest)
  api(projects.utilities.buildInfo)
  api(projects.utilities.shared)
}