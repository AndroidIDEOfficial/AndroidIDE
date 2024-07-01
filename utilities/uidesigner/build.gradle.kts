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
  id("kotlin-parcelize")
}



android {
  namespace = "${BuildConfig.packageName}.uidesigner"
}

dependencies {
  
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.nav.fragment)
  implementation(libs.androidx.nav.ui)
  implementation(libs.common.editor)
  implementation(libs.common.kotlin)
  implementation(libs.common.utilcode)
  implementation(libs.google.material)

  implementation(projects.annotation.annotations)
  implementation(projects.core.actions)
  implementation(projects.core.common)
  implementation(projects.core.lspApi)
  implementation(projects.core.resources)
  implementation(projects.editor.impl)
  implementation(projects.logging.logger)
  implementation(projects.utilities.lookup)
  implementation(projects.utilities.xmlInflater)
  implementation(projects.xml.lsp)

  testImplementation(libs.tests.junit)
  testImplementation(libs.tests.google.truth)
  testImplementation(libs.tests.robolectric)
  testImplementation(libs.tests.mockito.kotlin)
}
