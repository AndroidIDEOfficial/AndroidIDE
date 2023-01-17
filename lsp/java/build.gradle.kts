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
    id("kotlin-kapt")
}

android {
    namespace = "com.itsaky.androidide.lsp.java"
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

kapt {
    arguments {
        arg ("eventBusIndex", "com.itsaky.androidide.events.LspJavaEventsIndex")
    }
}

dependencies {
    
    kapt(libs.common.eventbus.ap)
    
    implementation(libs.common.editor)
    implementation(libs.common.javaparser)
    implementation(libs.common.utilcode)
    implementation(libs.androidx.annotation)
    implementation(libs.google.guava.jre)
    implementation(libs.google.gson)
    
    compileOnly(libs.androidx.appcompat)
    compileOnly(libs.google.material)
    compileOnly(projects.actions)
    compileOnly(projects.common)
    
    implementation(projects.resources)
    implementation(projects.lsp.api)
    implementation(projects.subprojects.javac)
    implementation(projects.subprojects.javacServices)
    implementation(projects.subprojects.googleJavaFormat)
    
    implementation(libs.androidx.ktx)
    implementation(libs.common.kotlin)
    
    testImplementation(projects.lsp.testing)
    testImplementation(projects.common)
    testImplementation(projects.actions)
    testImplementation(projects.subprojects.javac)
    testImplementation(libs.tests.junit)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.robolectric)
    androidTestImplementation(libs.tests.androidx.junit)
    androidTestImplementation(libs.tests.androidx.espresso)
    androidTestImplementation(libs.tests.google.truth)
}