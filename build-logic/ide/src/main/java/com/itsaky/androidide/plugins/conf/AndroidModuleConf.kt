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
import isFDroidBuild
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import projectVersionCode

/**
 * ABIs for which the product flavors will be created.
 * The keys in this map are the names of the product flavors whereas,
 * the value for each flavor is a number that will be incremented to the base version code of the IDE
 * and set as the version code of that flavor.
 *
 * For example, if the base version code of the IDE is 270 (for v2.7.0), then for arm64-v8a
 * flavor, the version code will be `100 * 270 + 1` i.e. `2701`
 */
// IMPORTANT: When changing the configuration here, make sure to update the following file:
//    - <root>/scripts/setup_fdroid_build.sh
private val flavorsAbis = mapOf("arm64-v8a" to 1, "armeabi-v7a" to 2, "x86_64" to 3)
private val disableCoreLibDesugaringForModules = arrayOf(":logsender", ":logger")

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
    }

    configureCoreLibDesugaring(this, coreLibDesugDep)

    if (":app" == project.path) {
      flavorDimensions("default")

      flavorsAbis.forEach { (abi, _) ->
        // the common defaultConfig, not the flavor-specific
        defaultConfig.buildConfigField("String",
          "FLAVOR_${abi.replace('-', '_').uppercase()}",
          "\"${abi}\"")
      }

      // Do not configure flavorDimensions here when building with F-Droid
      // flavor dimensions for F-Droid builds are configured in <root>/scripts/setup_fdroid_build.sh
      //
      // IMPORTANT: When changing the configuration here, make sure to update the following file:
      //    - <root>/scripts/setup_fdroid_build.sh
      if (!isFDroidBuild) {

        productFlavors {
          val fdroidSuffix = if (isFDroidBuild) "-fdroid" else ""
          flavorsAbis.forEach { (abi, verCodeIncrement) ->
            val flavor = create(abi)
            flavor.versionNameSuffix = "-${abi}${fdroidSuffix}"
            flavor.versionCode = 100 * projectVersionCode + verCodeIncrement
          }
        }
      }
    } else {
      defaultConfig {
        ndk {
          abiFilters.clear()
          abiFilters += flavorsAbis.keys
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
          include(*flavorsAbis.keys.toTypedArray())
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

    lintOptions {
      isCheckDependencies = true
    }
  }
}

private fun Project.configureCoreLibDesugaring(baseExtension: BaseExtension,
  coreLibDesugDep: Dependency) {
  val coreLibDesugaringEnabled = project.path !in disableCoreLibDesugaringForModules

  baseExtension.compileOptions.isCoreLibraryDesugaringEnabled = coreLibDesugaringEnabled

  if (coreLibDesugaringEnabled) {
    configurations.getByName("coreLibraryDesugaring").apply {
      dependencies.add(coreLibDesugDep)
    }
  }
}