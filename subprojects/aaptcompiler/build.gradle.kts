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
    namespace = "${BuildConfig.packageName}.aaptcompiler"
    
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(libs.common.kotlin)
    implementation(libs.androidx.collection)
    implementation(projects.logger)
    implementation(projects.subprojects.jaxp)
    
    api(libs.aapt2.annotations)
    api(libs.aapt2.common)
    api(libs.aapt2.proto)
    api(libs.google.protobuf)
    api(projects.subprojects.layoutlibApi)
    
    testImplementation(libs.tests.junit)
    testImplementation(libs.tests.robolectric)
    testImplementation(libs.tests.google.truth)
    testImplementation(projects.shared)
}