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

package com.itsaky.androidide.plugins.conf

import BuildConfig
import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.VersionCatalogsExtension
import projectVersionCode

private val flavorsAbis = arrayOf("arm64-v8a", "armeabi-v7a", "x86_64")

fun Project.configureAndroidModule(
  coreLibDesugDep: Dependency
) {
  assert(
    plugins.hasPlugin("com.android.application") || plugins.hasPlugin("com.android.library")) {
    "${javaClass.simpleName} can only be applied to Android projects"
  }

  extensions.getByType(BaseExtension::class.java).run {
    compileSdkVersion(BuildConfig.compileSdk)

    defaultConfig {
      minSdk = BuildConfig.minSdk
      targetSdk = BuildConfig.targetSdk
      versionCode = projectVersionCode
      versionName = rootProject.version.toString()

      // required
      multiDexEnabled = true

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
      sourceCompatibility = BuildConfig.javaVersion
      targetCompatibility = BuildConfig.javaVersion

      isCoreLibraryDesugaringEnabled = true
    }

    if (":app" == project.path) {
      flavorDimensions("default")

      productFlavors {
        flavorsAbis.forEach(this::create)

        forEach {
          val name = it.name
          defaultConfig.buildConfigField("String",
            "FLAVOR_${name.replace('-', '_').uppercase()}",
            "\"${name}\"")
        }
      }
    } else {
      defaultConfig {
        ndk {
          abiFilters.clear()
          abiFilters += flavorsAbis
        }
      }
    }

    // configure split APKs for ':app' module only
    if (this@configureAndroidModule == rootProject.findProject(":app")) {
      splits {
        abi {
          reset()

          isEnable = true
          isUniversalApk = false

          // TODO: Find a way to enable split APKs in product flavors. If this is possible, we can configure
          //       each flavor to include only a single ABI. For example, for the 'arm64-v8a' flavor,
          //       we can configure it to generate APK only for 'arm64-v8a'.
          //
          //  See the contribution guidelines for more information.
          @Suppress("ChromeOsAbiSupport")
          include(*flavorsAbis)
        }
      }
    }

    buildTypes.getByName("debug") { isMinifyEnabled = false }
    buildTypes.getByName("release") {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    testOptions { unitTests.isIncludeAndroidResources = true }

    buildFeatures.viewBinding = true
    buildFeatures.buildConfig = true

    configurations.getByName("coreLibraryDesugaring").apply {
      dependencies.add(coreLibDesugDep)
    }
  }
}