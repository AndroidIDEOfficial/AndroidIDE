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
  kotlin("android")
}



android {
  namespace = "${BuildConfig.packageName}.testing.unit"
}

dependencies {
  api(libs.tests.androidx.test.core)
  api(libs.tests.robolectric)
  api(libs.tests.junit)
  api(libs.tests.google.truth)
  api(libs.tests.mockk)

  api(projects.core.common)
  api(projects.testing.commonTest)
  api(projects.utilities.buildInfo)
  api(projects.utilities.shared)
}