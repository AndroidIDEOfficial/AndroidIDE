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

package com.itsaky.androidide.plugins.tasks.javac

import com.itsaky.androidide.plugins.properties.PropertiesParser
import com.itsaky.androidide.plugins.properties.gen.ClassGenerator
import com.itsaky.androidide.plugins.tasks.javac.JavacPropertiesParserTask.Companion.getGeneratedPropsDirectory
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * @author Akash Yadav
 */
abstract class JavacPropertiesParserTask : DefaultTask() {

  @get:Internal
  abstract val sourceDir: DirectoryProperty

  @TaskAction
  fun parseProperties() {
    val options = project.createParserOptions(sourceDir.get().asFile)
    if (options.isEmpty()) {
      return
    }

    val parser = PropertiesParser({ logger.lifecycle(it) }, sourceDir.get().asFile)

    logger.info(
      "Running PropertiesParser with arguments: ${options.joinToString(separator = " ")}"
    )

    val ok = parser.run(options.toTypedArray())
    if (!ok) {
      throw GradleException("Failed to parse property files")
    }
  }

  @Internal
  fun getPropsDir(): File {
    return project.getGeneratedPropsDirectory()
  }

  companion object {

    fun Project.getGeneratedPropsDirectory(): File =
      project.layout.buildDirectory.dir("generated/properties").get().asFile
  }
}

private fun Project.createParserOptions(baseDir: File): List<String> {
  val options = mutableListOf<String>()
  baseDir.walkTopDown().forEach {
    if (!(it.isFile && it.extension == "properties")) {
      return@forEach
    }

    val destPath =
      it.path.substringAfter(baseDir.absolutePath)
        .substringBeforeLast(File.separatorChar) +
          File.separator +
          ClassGenerator.toplevelName(it) +
          ".java"

    val destFile = File(getGeneratedPropsDirectory(), destPath)

    if (destFile.exists() && destFile.lastModified() >= it.lastModified()) {
      logger.info("${it.name} is up-to-date. Skipping.")
      return@forEach
    }

    destFile.parentFile.mkdirs()
    options.add("-compile")
    options.add(it.absolutePath)
    options.add(destFile.parentFile.absolutePath)
  }
  return options
}