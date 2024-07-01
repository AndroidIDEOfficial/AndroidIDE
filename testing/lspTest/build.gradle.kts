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


import com.itsaky.androidide.build.config.BuildConfig

plugins {
  id("com.android.library")
  id("kotlin-android")
}



android {
  namespace = "${BuildConfig.packageName}.lsp.testing"
}

dependencies {
  api(libs.androidx.core.ktx)
  api(libs.common.editor)
  api(libs.common.kotlin)
  api(libs.tests.mockito.kotlin)

  api(projects.core.actions)
  api(projects.core.common)
  api(projects.core.lspApi)
  api(projects.event.eventbusAndroid)
  api(projects.testing.commonTest)
  api(projects.testing.androidTest)
  api(projects.testing.gradleToolingTest)
  api(projects.testing.unitTest)
}