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

plugins {
  id("java-gradle-plugin")
  id("org.jetbrains.kotlin.jvm")
  id("com.vanniktech.maven.publish.base")
}

description = "Gradle Plugin for projects that are built with AndroidIDE"

dependencies {
  implementation(projects.buildInfo)
  implementation(libs.tooling.agp)
}

gradlePlugin {
  website.set(ProjectConfig.REPO_URL)
  vcsUrl.set(ProjectConfig.REPO_URL)

  plugins {
    create("gradlePlugin") {
      id = "${BuildConfig.packageName}.gradle"
      implementationClass = "${BuildConfig.packageName}.gradle.AndroidIDEGradlePlugin"
      displayName = "AndroidIDE Gradle Plugin"
      description = "Gradle plugin for projects that are built with AndroidIDE"
      tags.set(setOf("androidide"))
    }
  }
}
