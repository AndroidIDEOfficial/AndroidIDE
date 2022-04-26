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
 * An Gradle Project.
 * @author Akash Yadav
 */
abstract class IGradleProject : Serializable {
    abstract val name: String?
    abstract val description: String?
    abstract val projectPath: String?
    abstract val projectDir: File?
    abstract val buildDir: File?
    abstract val buildScript: File?
    
    abstract val parent: IGradleProject?
    abstract val subprojects: List<IGradleProject>
    abstract val tasks: List<IGradleTask>

    /**
     * Finds the project with the given project path.
     *
     * @return The found [IGradleProject] or `null`.
     */
    abstract fun findByPath(path: String): IGradleProject?
}
