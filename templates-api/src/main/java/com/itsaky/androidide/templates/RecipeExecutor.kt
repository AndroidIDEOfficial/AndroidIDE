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

package com.itsaky.androidide.templates

import java.io.File
import java.io.InputStream

/**
 * Handles execution of template recipe.
 *
 * @author Akash Yadav
 */
interface RecipeExecutor {

  /**
   * Get the project template data. This is available only while creating modules in an existing project.
   *
   * @return The project template data or `null` if the not available.
   */
  fun projectData(): ProjectTemplateData? = null

  /**
   * @return The [ProjectTemplateData] if available, throws [IllegalStateException] otherwise.
   * @see projectData
   */
  fun requireProjectData(): ProjectTemplateData = checkNotNull(projectData())

  /**
   * Copy the [source] file to [dest].
   */
  fun copy(source: File, dest: File)

  /**
   * Save the [source] to [dest].
   */
  fun save(source: String, dest: File)

  /**
   * Open the given asset path.
   *
   * @return The [InputStream] for the asset.
   */
  fun openAsset(path: String): InputStream

  /**
   * Copies the asset at the given path to the specified destination.
   *
   * @param path The path of the asset.
   * @param dest The destination path.
   */
  fun copyAsset(path: String, dest: File)

  /**
   * Copies the asset directory path to the specified destination directory.
   *
   * @param path The asset path.
   * @param destDir The destination directory.
   */
  fun copyAssetsRecursively(path: String, destDir: File)
}