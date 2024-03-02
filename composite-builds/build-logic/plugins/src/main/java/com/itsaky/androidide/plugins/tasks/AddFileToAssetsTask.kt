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

package com.itsaky.androidide.plugins.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Adds the provided file to assets.
 *
 * @author Akash Yadav
 */
abstract class AddFileToAssetsTask : DefaultTask() {

  /**
   * The input file that should be copied to the assets directory.
   */
  @get:InputFile
  abstract val inputFile: RegularFileProperty

  /**
   * The base assets path. The file will be saved in assets to `base-path/file-name`.
   */
  @get:Input
  abstract val baseAssetsPath: Property<String>

  /**
   * The file name of the file in assets. The file will be saved in assets to `base-path/file-name`.
   */
  @get:Input
  @get:Optional
  abstract val fileName: Property<String>

  /**
   * The output assets directory. This should not be set manually, but provided to Android Gradle Plugin.
   */
  @get:OutputDirectory
  abstract val outputDirectory: DirectoryProperty

  @TaskAction
  fun copy() {
    var basePath = baseAssetsPath.get()
    if (basePath.isBlank()) {
      basePath = "data"
    }

    while (basePath.endsWith('/')) {
      basePath = basePath.removeSuffix("/")
    }

    val inFile = inputFile.get().asFile

    if (!inFile.exists()) {
      throw IllegalArgumentException("File '$inFile' does not exist")
    }

    if (!inFile.isFile) {
      throw IllegalArgumentException("File '$inFile' is not a file")
    }

    var fileName = fileName.getOrElse("")
    if (fileName.isBlank()) {
      fileName = inFile.name
    }

    val outFile = outputDirectory.file("$basePath/$fileName").get().asFile.also {
      it.parentFile.mkdirs()
    }

    inFile.inputStream().buffered().use { input ->
      outFile.outputStream().buffered().use { output ->
        input.transferTo(output)
      }
    }
  }
}