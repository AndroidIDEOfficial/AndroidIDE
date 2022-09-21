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
package com.itsaky.androidide.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidIDEPlugin implements Plugin<Project> {
    
    @Override
    void apply (Project project) {
        def manager = project.getPluginManager ()
        def isApp = manager.hasPlugin ("com.android.application")
        def isLibrary = manager.hasPlugin ("com.android.library")
        
        if (!(isApp || isLibrary)) {
            println "Project " + project.displayName + " does not apply Android's application or library plugin"
            println "AndroidIDE's Gradle Plugin will not be applied to this project"
            return
        }
        
        if (isApp) {
            project.afterEvaluate {
                project.android.sourceSets {
                    main.java.srcDirs += "${System.getenv ("HOME")}/logsender"
                }
            }
        }
    }
}