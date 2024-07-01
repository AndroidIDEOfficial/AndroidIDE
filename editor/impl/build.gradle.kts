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
    id("kotlin-kapt")
    id("com.google.devtools.ksp") version libs.versions.ksp
}



android {
    namespace = "${BuildConfig.packageName}.editor"
}

kapt {
    arguments {
        arg ("eventBusIndex", "${BuildConfig.packageName}.events.EditorEventsIndex")
    }
}

dependencies {
    ksp(projects.annotation.processorsKsp)
    kapt(projects.annotation.processors)
    
    api(libs.androidide.ts)
    api(libs.androidide.ts.java)
    api(libs.androidide.ts.json)
    api(libs.androidide.ts.kotlin)
    api(libs.androidide.ts.log)
    api(libs.androidide.ts.xml)
    api(libs.androidx.collection)
    api(libs.common.editor)
    
    api(projects.editor.api)
    api(projects.editor.treesitter)

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.tracing)
    implementation(libs.androidx.tracing.ktx)

    implementation(libs.common.utilcode)
    implementation(libs.composite.jdt)
    
    implementation(libs.google.material)

    implementation(projects.annotation.annotations)
    implementation(projects.core.actions)
    implementation(projects.core.common)
    implementation(projects.core.lspApi)
    implementation(projects.core.resources)
    implementation(projects.editor.lexers)
    implementation(projects.event.eventbusAndroid)
    implementation(projects.event.eventbusEvents)
    implementation(projects.java.lsp)
    implementation(projects.utilities.shared)
    implementation(projects.xml.lsp)

    testImplementation(libs.tests.junit)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.robolectric)
}
