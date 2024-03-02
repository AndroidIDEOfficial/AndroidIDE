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

@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {

  includeBuild("../build-deps-common") {
    dependencySubstitution {
      substitute(module("com.itsaky.androidide.build:desugaring-core"))
        .using(project(":desugaring-core"))
    }
  }

  repositories {
    google()
    mavenCentral()
  }

  versionCatalogs {
    create("libs") {
      from(files("../../gradle/libs.versions.toml"))
    }
  }
}

include(
  ":common",
  ":desugaring",
  ":desugaring-core",
  ":plugins",
  ":properties-parser"
)

rootProject.name = "build-logic"