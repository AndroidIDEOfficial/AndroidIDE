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
    id("kotlin-android")
}

android {
    namespace = "com.itsaky.androidide.lsp.testing"
}

dependencies {
    implementation(libs.androidx.ktx)
    implementation(libs.common.kotlin)
    
    implementation(projects.lsp.api)
    implementation(projects.actions)
    implementation(projects.common)
    implementation(projects.eventbusAndroid)
    implementation(projects.subprojects.toolingApiTesting)
    implementation(libs.common.editor)
    implementation(libs.tests.junit)
    implementation(libs.tests.google.truth)
    implementation(libs.tests.robolectric)
    implementation(projects.actions)
}