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
package com.itsaky.androidide.tooling.api.model

import java.io.File
import java.io.Serializable

/**
 * Currently initialized project.
 *
 * The instance of this class is created from a [Proxy][java.lang.reflect.Proxy]. If you add any
 * property to this class, make sure you update the [IAndroidProject.copy] method too.
 *
 * @author Akash Yadav
 */
interface IAndroidProject : Serializable {
    /** The path of this project. For example: ':app'. */
    val path: String?

    /** The display name of this project. */
    val displayName: String?

    /** The project directory of this project. */
    val projectDir: File?

    /** Get the build directory of this project. */
    val buildDir: File?

    /** The buildscript file (build.gradle) of this project. */
    val buildFile: File?

    /** Subprojects of this project. */
    val subProjects: List<IAndroidProject>
    
    /**
     * Dependencies of this project.
     */
    val dependencies: List<ProjectDependency>
}
