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


import com.itsaky.androidide.plugins.NoDesugarPlugin
import com.itsaky.androidide.build.config.BuildConfig

plugins {
  id("com.android.library")
  id("com.vanniktech.maven.publish.base")
}

apply {
  plugin(NoDesugarPlugin::class.java)
}



description = "LogSender is used to read logs from applications built with AndroidIDE"

android {
  namespace = "${BuildConfig.packageName}.logsender"

  defaultConfig {
    minSdk = 16

    vectorDrawables {
      useSupportLibrary = true
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  buildFeatures.apply {
    aidl = true
    viewBinding = false
  }
}

dependencies {
  testImplementation(libs.tests.junit)
  testImplementation(libs.tests.robolectric)
  testImplementation(libs.tests.google.truth)
}