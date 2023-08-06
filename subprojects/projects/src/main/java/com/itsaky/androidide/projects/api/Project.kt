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

package com.itsaky.androidide.projects.api

import com.android.builder.model.v2.ide.ProjectType
import com.itsaky.androidide.builder.model.DefaultProjectSyncIssues
import java.io.File
import java.nio.file.Path

/**
 * Model for representing the whole project that is opened in the IDE (including the root project).
 *
 * @property rootProject The root Gradle project.
 * @property subProjects List of all project that are included the project.
 * @property syncIssues The issues that occurred while syncing the project.
 * @author Akash Yadav
 */
data class Project(val rootProject: GradleProject, val subProjects: List<GradleProject>,
  val syncIssues: Map<String, DefaultProjectSyncIssues>) {

  /**
   * Finds the project by the given path.
   *
   * @return The project with the given path or `null` if no project is available with that path.
   */
  fun findByPath(path: String): GradleProject? {
    return this.subProjects.find { it.path == path }
  }

  /**
   * Find the first [AndroidModule] in this project. If this project is itself an Android module,
   * return `this` instance.
   *
   * @return The first Android module.
   */
  fun findFirstAndroidModule(): AndroidModule? {
    val android = this.subProjects.find { it is AndroidModule }
    if (android != null) {
      return android as AndroidModule
    }
    return null
  }

  /**
   * Find the first [AndroidModule] in this project whose `projectType` is
   * [com.android.builder.model.v2.ide.ProjectType.APPLICATION]. If this project is itself an
   * Android app module, return `this` instance.
   *
   * @return The first Android module.
   */
  fun findFirstAndroidAppModule(): AndroidModule? {
    val app = this.subProjects.find { it is AndroidModule && it.projectType == ProjectType.APPLICATION }

    if (app != null) {
      return app as AndroidModule
    }
    return null
  }

  /**
   * Finds all the [AndroidModule]s in this project. If this project is itself an Android module,
   * then it also added to the list at index `0`.
   *
   * @return The list of android modules.
   */
  fun findAndroidModules(): List<AndroidModule> {
    return subProjects.filterIsInstance(AndroidModule::class.java).toMutableList()
  }

  fun findModuleForFile(file: Path): ModuleProject? {
    return findModuleForFile(file.toFile())
  }

  @JvmOverloads
  fun findModuleForFile(file: File, checkExistance: Boolean = false): ModuleProject? {

    if (!file.exists() && checkExistance) {
      return null
    }

    val path = file.path
    var longestPath = ""
    var moduleWithLongestPath: ModuleProject? = null

    for (module in subProjects) {
      if (module !is ModuleProject) {
        continue
      }

      val moduleDir = module.projectDir.path
      if (path.startsWith(moduleDir) && longestPath.length < moduleDir.length) {
        longestPath = moduleDir
        moduleWithLongestPath = module
      }
    }

    if (longestPath.isEmpty() || moduleWithLongestPath == null) {
      return null
    }

    return moduleWithLongestPath
  }
}