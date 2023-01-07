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

import com.android.build.gradle.BaseExtension
import com.itsaky.androidide.plugins.AndroidIDEPlugin

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id("build-logic.root-project")
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin) apply false
}

buildscript { dependencies { classpath("com.google.android.gms:oss-licenses-plugin:0.10.6") } }

val Project.projectVersionCode by lazy {
  val version = rootProject.version.toString()
  val regex = Regex("^v\\d+\\.?\\d+\\.?\\d+")

  return@lazy regex.find(version)?.value?.substring(1)?.replace(".", "")?.toInt()?.also {
    logger.warn("Version code is '$it' (from version ${rootProject.version}).")
  }
    ?: throw IllegalStateException(
      "Invalid version string '$version'. Version names must be SEMVER with 'v' prefix"
    )
}

fun Project.configureBaseExtension() {
  extensions.findByType(BaseExtension::class)?.run {
    compileSdkVersion(BuildConfig.compileSdk)
    buildToolsVersion = BuildConfig.buildTools

    defaultConfig {
      minSdk = BuildConfig.minSdk
      targetSdk = BuildConfig.targetSdk
      versionCode = projectVersionCode
      versionName = rootProject.version.toString()
    }

    compileOptions {
      sourceCompatibility = BuildConfig.javaVersion
      targetCompatibility = BuildConfig.javaVersion
    }

    buildTypes.getByName("debug") { isMinifyEnabled = false }
    buildTypes.getByName("release") {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    testOptions { unitTests.isIncludeAndroidResources = true }

    buildFeatures.viewBinding = true
  }
}

subprojects {
  apply { plugin(AndroidIDEPlugin::class.java) }

  version = rootProject.version
  plugins.withId("com.android.application") { configureBaseExtension() }
  plugins.withId("com.android.library") { configureBaseExtension() }

  plugins.withId("java-library") {
    configure<JavaPluginExtension> {
      sourceCompatibility = BuildConfig.javaVersion
      targetCompatibility = BuildConfig.javaVersion
    }
  }

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = BuildConfig.javaVersion.toString()
  }
}

tasks.register<Delete>("clean") { delete(rootProject.buildDir) }
