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
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id("build-logic.root-project")
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin) apply false
}

buildscript { dependencies { classpath("com.google.android.gms:oss-licenses-plugin:0.10.6")
  classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
} }

fun Project.configureBaseExtension() {
  extensions.findByType(BaseExtension::class)?.run {
    compileSdkVersion(Versions.compileSdk)
    buildToolsVersion = Versions.buildTools

    defaultConfig {
      minSdk = Versions.minSdk
      targetSdk = Versions.targetSdk
      versionCode = Versions.versionCode
      versionName = Versions.versionName
    }

    compileOptions {
      sourceCompatibility = Versions.javaVersion
      targetCompatibility = Versions.javaVersion
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
  apply {
    plugin("kotlin-kapt")
    plugin(AndroidIDEPlugin::class.java)
  }

  plugins.withId("com.android.application") { configureBaseExtension() }
  plugins.withId("com.android.library") { configureBaseExtension() }

  plugins.withType(JavaLibraryPlugin::class.java) {
    configure<JavaPluginExtension> {
      sourceCompatibility = Versions.javaVersion
      targetCompatibility = Versions.javaVersion
    }
  }

  plugins.withId("kotlin-android") {
    configure<KotlinAndroidProjectExtension> { jvmToolchain(Versions.javaVersionMajor) }
  }
}

tasks.register<Delete>("clean") { delete(rootProject.buildDir) }
