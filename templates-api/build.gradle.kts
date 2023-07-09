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
}

android {
  namespace = "${BuildConfig.packageName}.templates"
}

dependencies {
  api(projects.subprojects.javapoet)

  api(projects.common)
  api(projects.logger)
  api(projects.resources)
  api(projects.subprojects.xmlDom)
  api(projects.subprojects.xmlUtils)

  api(libs.aapt2.common)
  api(libs.androidx.annotation)
  api(libs.androidx.appcompat)
  api(libs.google.material)
}