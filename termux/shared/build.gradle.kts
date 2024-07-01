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
    id ("com.android.library")
    id("kotlin-android")
}



android {
    namespace = "com.termux.shared"
    ndkVersion = BuildConfig.ndkVersion

    externalNativeBuild {
        ndkBuild {
            path = file("src/main/cpp/Android.mk")
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core)
    implementation(libs.androidx.window.v1alpha9)
    implementation(libs.common.markwon.core)
    implementation(libs.common.markwon.extStrikethrough)
    implementation(libs.common.markwon.linkify)
    implementation(libs.common.markwon.recycler)
    implementation(libs.google.material)
    implementation(libs.google.guava)
    implementation(libs.common.hiddenApiBypass)

    // Do not increment version higher than 1.0.0-alpha09 since it will break ViewUtils and needs to be looked into
    // noinspection GradleDependency
    implementation(libs.common.io)
    implementation(libs.common.termuxAmLib)

    implementation(projects.core.common)
    implementation(projects.core.resources)
    implementation(projects.termux.view)
    implementation(projects.utilities.buildInfo)
    implementation(projects.utilities.preferences)

    testImplementation(projects.testing.unitTest)
    testImplementation(projects.testing.androidTest)
}
