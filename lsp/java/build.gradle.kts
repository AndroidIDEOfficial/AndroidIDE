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
    namespace = "${BuildConfig.packageName}.lsp.java"
}

kapt {
    arguments {
        arg ("eventBusIndex", "${BuildConfig.packageName}.events.LspJavaEventsIndex")
    }
}

dependencies {
    
    kapt(projects.annotationProcessors)
    
    implementation(libs.androidide.ts)
    implementation(libs.androidide.ts.java)
    implementation(libs.common.editor)
    implementation(libs.common.javaparser)
    implementation(libs.common.utilcode)
    implementation(libs.androidx.annotation)
    implementation(libs.google.guava)
    implementation(libs.google.gson)
    
    compileOnly(libs.androidx.appcompat)
    compileOnly(libs.google.material)
    compileOnly(projects.actions)
    compileOnly(projects.common)
    
    implementation(projects.editorApi)
    implementation(projects.resources)
    implementation(projects.lsp.api)
    implementation(projects.subprojects.javac)
    implementation(projects.subprojects.javacServices)
    implementation(projects.subprojects.javapoet)
    implementation(projects.subprojects.googleJavaFormat)
    
    implementation(libs.androidx.core.ktx)
    implementation(libs.common.kotlin)

    testImplementation(projects.testing.lsp)
}