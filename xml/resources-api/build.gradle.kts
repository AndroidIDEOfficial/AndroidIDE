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

import com.google.protobuf.gradle.id
import com.itsaky.androidide.build.config.BuildConfig

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("com.google.protobuf")
}

android {
  namespace = "${BuildConfig.packageName}.xml.resapi"
}

protobuf {
  protoc {
    artifact = "com.google.protobuf:protoc:4.27.0"
  }

  generateProtoTasks {
    all().forEach {
      it.builtins {
        id("java") {
          option("lite")
        }
      }
    }
  }
}

dependencies {
  api(libs.aapt2.annotations)
  api(libs.aapt2.common)
  api(libs.google.protobuf)

  api(libs.composite.layoutlibApi)
  api(libs.composite.jaxp)

  implementation(projects.utilities.shared)
}