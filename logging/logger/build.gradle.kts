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


import com.itsaky.androidide.plugins.NoDesugarPlugin

@Suppress("JavaPluginLanguageLevel")
plugins {
    id("java-library")
    id("com.vanniktech.maven.publish.base")
}

apply {
    plugin(NoDesugarPlugin::class.java)
}



description = "AndroidIDE Logging Framework"

dependencies {
    compileOnly(projects.utilities.frameworkStubs)

    api(libs.logging.logback.core)
    api(libs.logging.logback.classic) {
        // logback classic depends on upstream logback-core
        // we exclude it and use our own from logback-android
        exclude(group = "ch.qos.logback", module = "logback-core")
    }

    implementation(projects.utilities.buildInfo)

    testImplementation(libs.tests.junit)
    testImplementation(libs.tests.google.truth)
}