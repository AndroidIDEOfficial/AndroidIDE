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
    id("com.itsaky.androidide.build")
}

android {
    namespace = "com.google.googlejavaformat"
}

dependencies {
    implementation(libs.google.guava)
    implementation(libs.google.auto.value.annotations)
    implementation(libs.google.auto.service.annotations)
    implementation(projects.buildDeps.javac)

    annotationProcessor(libs.google.auto.value.ap)
    annotationProcessor(libs.google.auto.service)
}