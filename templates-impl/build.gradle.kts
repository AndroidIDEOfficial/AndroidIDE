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
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("kotlin-kapt")
}

android {
  namespace = "${BuildConfig.packageName}.templates.impl"
}

dependencies {
  kapt(libs.google.auto.service)

  api(projects.templatesApi)

  implementation(projects.shared)
  implementation(projects.common)
  implementation(projects.preferences)
  implementation(projects.subprojects.projects)
  implementation(libs.androidx.annotation)
  implementation(libs.androidx.core.ktx)
  implementation(libs.google.auto.service.annotations)

  testImplementation(projects.templatesApi)
  testImplementation(projects.lsp.api)
  testImplementation(projects.preferences)
  testImplementation(projects.testing.unit)
  testImplementation(projects.testing.tooling)
}