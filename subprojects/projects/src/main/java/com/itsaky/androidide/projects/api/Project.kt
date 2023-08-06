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

import com.android.builder.model.v2.ide.ProjectType.APPLICATION
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.ProjectType
import com.itsaky.androidide.tooling.api.models.GradleTask
import java.io.File
import java.nio.file.Path
import java.util.concurrent.*

/**
 * A Gradle project model which is identical to [IProject]. This project module caches all the data
 * from an [IProject] eliminating the use of [CompletableFuture] s.
 *
 * @param name The display name of the project.
 * @param description The project description.
 * @param path The project path (same as Gradle project paths). For example, `:app`,
 *   `:module:submodule`, etc. Root project is always represented by path `:`.
 * @param projectDir The project directory.
 * @param buildDir The build directory of the project.
 * @param buildScript The Gradle buildscript file of the project.
 * @param parentPath The parent project path. Always `null` for root project.
 * @param tasks The tasks of the project.
 * @param subModules The submodules of the project.
 * @author Akash Yadav
 */
open class Project(
  val name: String,
  val description: String,
  val path: String,
  val projectDir: File,
  val buildDir: File,
  val buildScript: File,
  val tasks: List<GradleTask>,
  val subModules: List<Project> = mutableListOf()
) {

  var type: ProjectType = ProjectType.Gradle
    protected set

  /**
   * Finds the project by the given path.
   *
   * @return The project with the given path or `null` if no project is available with that path.
   */
  fun findByPath(path: String): Project? {
    if (path == this.path) {
      return this
    }

    return this.subModules.find { it.path == path }
  }

  /**
   * Find the first [AndroidModule] in this project. If this project is itself an Android module,
   * return `this` instance.
   *
   * @return The first Android module.
   */
  fun findFirstAndroidModule(): AndroidModule? {
    if (this is AndroidModule) {
      return this
    }
    val android = this.subModules.find { it is AndroidModule }
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
    if (this is AndroidModule && this.projectType == APPLICATION) {
      return this
    }
    val app = this.subModules.find { it is AndroidModule && it.projectType == APPLICATION }

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
    val androidModules = this.subModules.filterIsInstance(AndroidModule::class.java).toMutableList()

    if (this is AndroidModule) {
      androidModules.add(0, this)
    }
    return androidModules
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

    for (module in subModules) {
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
