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
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.android) apply false
}

subprojects {
  plugins.withId("com.android.library") {
    extensions.getByType(BaseExtension::class.java).apply {
      compileSdkVersion(34)

      defaultConfig {
        minSdk = 26
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 28
      }

      compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
      }

      buildTypes.register("dev") {
        initWith(buildTypes.getByName("release"))
        isMinifyEnabled = false
      }
    }
  }

  tasks.withType(KotlinCompile::class.java) {
    kotlinOptions.jvmTarget = "11"
  }
}