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
import com.android.builder.model.v2.models.ProjectSyncIssues
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.utils.ServiceLoader
import java.io.File

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
   * The path to the project's root directory.
   */
  val projectDirPath: String
    get() = projectDir.path

  /**
   * The project's root directory.
   */
  val projectDir: File

  /**
   * Issues that were encountered during project synchronization.
   */
  val projectSyncIssues: ProjectSyncIssues?

  /**
   * Open the given project directory.
   */
  fun openProject(directory: File)

  /**
   * Same as [openProject].
   */
  fun openProject(path: String) = openProject(File(path))

  /**
   * Setup the project with the given [project proxy][project] from the Tooling API.
   *
   * @param project The project proxy.
   */
  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
  suspend fun setupProject(
    project: IProject = Lookup.getDefault().lookup(BuildService.KEY_PROJECT_PROXY)!!
  )

  /**
   * Get the workspace instance.
   *
   * @return The configured workspace, or `null`.
   */
  fun getWorkspace(): IWorkspace?

  /**
   * Get the workspace instance.
   *
   * @return The configured workspace.
   * @throws IWorkspace.NotConfiguredException If the workspace has not been configured yet.
   */
  fun requireWorkspace(): IWorkspace = getWorkspace() ?: throw IWorkspace.NotConfiguredException()

  /**
   * Notify the project manager that the given <code>file</code> was created.
   * @param file The file that was created.
   */
  fun notifyFileCreated(file: File)

  /**
   * Notify the project manager that the given <code>file</code> was deleted.
   * @param file The file that was deleted.
   */
  fun notifyFileDeleted(file: File)

  /**
   * Notify the project manager that the file was renamed or moved.
   * @param from The file that was renamed or moved.
   * @param to The file after renaming/move.
   */
  fun notifyFileRenamed(from: File, to: File)

  /**
   * Destroy the project manager.
   */
  fun destroy()
}