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

/**
 * Default implementation for [IdeGradleProject].
 * @author Akash Yadav
 */
open class IdeGradleProject(
    val name: String?,
    val description: String?,
    val projectPath: String?,
    val projectDir: File?,
    val buildDir: File?,
    val buildScript: File?,
    val parent: IdeGradleProject?,
    val subprojects: List<IdeGradleProject>,
    val tasks: List<IdeGradleTask>,
) {

    protected val gsonType: String = javaClass.name

    fun findByPath(path: String): IdeGradleProject? {
        if (path == this.projectPath) {
            return this
        }

        for (sub in subprojects) {
            val subSub = sub.findByPath(path)
            if (subSub != null) {
                return subSub
            }
        }

        return null
    }
    
    override fun toString(): String {
        return "IdeGradleProject(name=$name, description=$description, projectPath=$projectPath, projectDir=$projectDir, buildDir=$buildDir, buildScript=$buildScript, parent=$parent, subprojects=$subprojects, tasks=$tasks, gsonType='$gsonType')"
    }
}
