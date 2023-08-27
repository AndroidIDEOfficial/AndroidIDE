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

package com.itsaky.androidide.projects

import androidx.annotation.RestrictTo
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.projects.api.AndroidModule
import com.itsaky.androidide.projects.api.ModuleProject
import com.itsaky.androidide.projects.api.Project
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.utils.ServiceLoader
import java.io.File
import java.nio.file.Path

/**
 * Project manager.
 *
 * @author Akash Yadav
 */
interface IProjectManager {

  companion object {

    private var projectManager: IProjectManager? = null

    /**
     * Get the project manager instance.
     */
    @JvmStatic
    fun getInstance(): IProjectManager {
      return projectManager ?: ServiceLoader.load(IProjectManager::class.java).findFirstOrThrow()
        .also {
          projectManager = it
        }
    }
  }

  /**
   * The root project model.
   */
  val rootProject: Project?

  /**
   * The Android application module in the project.
   */
  val app: AndroidModule?

  /**
   * The path to the project's root directory.
   */
  val projectDirPath: String

  /**
   * The project's root directory.
   */
  val projectDir: File
    get() = File(projectDirPath)

  /**
   * Setup the project with the given [project proxy][project] from the Tooling API.
   *
   * @param project The project proxy.
   */
  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
  fun setupProject(
    project: IProject = Lookup.getDefault().lookup(BuildService.KEY_PROJECT_PROXY)!!
  )

  /**
   * Find the module for the given file.
   *
   * @param file The file to find the module for.
   * @return The module project, or `null` if not found.
   */
  fun findModuleForFile(file: File) : ModuleProject? {
    return findModuleForFile(file, true)
  }

  /**
   * Find the module for the given file.
   *
   * @param file The file to find the module for.
   * @param checkExistance Whether to check if the file exists or not.
   * @return The [ModuleProject] for the given file, or `null` if cannot be found.
   */
  fun findModuleForFile(file: File, checkExistance: Boolean): ModuleProject?

  /**
   * Find the module for the given file.
   *
   * @param file The file to find the module for.
   * @return The module project, or `null` if not found.
   */
  fun findModuleForFile(file: Path) : ModuleProject? {
    return findModuleForFile(file, true)
  }

  /**
   * Find the module for the given file.
   *
   * @param file The file to find the module for.
   * @param checkExistance Whether to check if the file exists or not.
   * @return The [ModuleProject] for the given file, or `null` if cannot be found.
   */
  fun findModuleForFile(file: Path, checkExistance: Boolean): ModuleProject? {
    return findModuleForFile(file.toFile(), checkExistance)
  }

  /**
   * Check if any of the module projects contain the given [file] in their source folder.
   *
   * @param file The file to check.
   * @return `true` if the given [file] is a source file in any of the mdoules, `false` otherwise.
   */
  fun containsSourceFile(file: Path): Boolean

  /**
   * Checks if the given file is a resource file in any of the included Android modules.
   *
   * @param file The file to check.
   * @return `true` if the given file is a resource file in any of the Android modules, `false` otherwise.
   */
  fun isAndroidResource(file: File): Boolean

  /**
   * Destroy the project manager.
   */
  fun destroy()
}