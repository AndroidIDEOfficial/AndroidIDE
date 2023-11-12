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
    namespace = "${BuildConfig.packageName}.lsp.api"
}

kapt {
    arguments {
        arg ("eventBusIndex", "${BuildConfig.packageName}.events.LspApiEventsIndex")
    }
}

dependencies {
    
    kapt(projects.annotationProcessors)
    
    implementation(libs.common.editor)
    implementation(projects.subprojects.fuzzysearch)
    implementation(projects.eventbusEvents)
    
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.common.kotlin)
    implementation(libs.common.utilcode)
    implementation(libs.google.material)
    
    api(projects.subprojects.projects)
    api(projects.subprojects.xmlUtils)
    api(projects.lookup)
    api(projects.lsp.models)
    api(projects.preferences)
    
    compileOnly(projects.actions)
    compileOnly(projects.common)
}