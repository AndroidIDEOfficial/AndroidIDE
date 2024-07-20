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
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.benchmark)
}



android {
  namespace = "${BuildConfig.packageName}.benchmark"

  sourceSets {
    getByName("androidTest") {
      assets.srcDirs(rootProject.file("utilities/framework-stubs/libs"))
    }
  }

  defaultConfig {
    testInstrumentationRunner = "androidx.benchmark.junit4.AndroidBenchmarkRunner"
  }

  testBuildType = "release"

  buildTypes {
    debug {
      // Since isDebuggable can"t be modified by gradle for library modules,
      // it must be done in a manifest - see src/androidTest/AndroidManifest.xml
      isMinifyEnabled = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "benchmark-proguard-rules.pro"
      )
    }

    release {
      isDefault = true
    }
  }
}

dependencies {
  androidTestImplementation(libs.tests.androidx.test.runner)
  androidTestImplementation(libs.tests.androidx.junit)
  androidTestImplementation(libs.tests.junit)
  androidTestImplementation(libs.tests.google.truth)
  androidTestImplementation(libs.androidx.benchmark.junit4)

  androidTestImplementation(projects.core.common)
  androidTestImplementation(projects.core.indexingApi)
  androidTestImplementation(projects.java.lsp)
  androidTestImplementation(projects.utilities.shared)
}
