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
}



android {
    namespace = "${BuildConfig.packageName}.xml.lsp"
}

kapt {
    arguments {
        arg ("eventBusIndex", "${BuildConfig.packageName}.events.LspXmlEventsIndex")
    }
}

dependencies {
    
    kapt(projects.annotation.processors)
    
    implementation(libs.common.editor)
    implementation(libs.common.utilcode)
    implementation(libs.androidide.ts)
    implementation(libs.androidide.ts.xml)
    
    implementation(projects.core.actions)
    implementation(projects.core.lspApi)
    implementation(projects.editor.lexers)
    implementation(projects.xml.dom)
    implementation(projects.xml.utils)
    
    implementation(libs.androidx.core.ktx)
    implementation(libs.common.kotlin)
    implementation(libs.google.material)

    testImplementation(projects.core.actions)
    testImplementation(projects.core.projects)
    testImplementation(projects.tooling.api)
    testImplementation(projects.testing.commonTest)
    testImplementation(projects.testing.lspTest)

    compileOnly(projects.core.common)
    compileOnly(libs.common.antlr4)
}
