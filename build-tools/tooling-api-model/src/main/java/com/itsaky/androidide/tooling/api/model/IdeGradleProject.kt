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
 * A root Gradle project.
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
    val tasks: List<IdeGradleTask>,
) : HasModules {

    private val gsonType: String = javaClass.name
    private val moduleProjects: MutableList<IdeGradleProject> = mutableListOf()
    override fun getModules(): List<IdeGradleProject> = moduleProjects

    fun findByPath(path: String): IdeGradleProject? {
        if (path == this.projectPath) {
            return this
        }

        return getModules().firstOrNull { it.projectPath == path }
    }

    fun findAndroidModules(): List<IdeAndroidModule> {
        return getModules().filterIsInstance(IdeAndroidModule::class.java)
    }

    fun findFirstAndroidModule(): IdeAndroidModule? {
        return findAndroidModules().firstOrNull()
    }

    override fun toString(): String {
        return "IdeGradleProject(name=$name, description=$description, projectPath=$projectPath, projectDir=$projectDir, buildDir=$buildDir, buildScript=$buildScript, parent=$parent, subprojects=$moduleProjects, tasks=$tasks, gsonType='$gsonType')"
    }
}
