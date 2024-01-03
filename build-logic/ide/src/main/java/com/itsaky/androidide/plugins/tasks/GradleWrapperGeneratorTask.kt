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
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.wrapper.Wrapper
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Generates the `gradle-wrapper.zip` file.
 *
 * @author Akash Yadav
 */
abstract class GradleWrapperGeneratorTask : DefaultTask() {

  /**
   * The output directory.
   */
  @get:OutputDirectory
  abstract val outputDirectory: DirectoryProperty

  companion object {

    private const val GRADLE_VERSION = "7.4.2"
  }

  @TaskAction
  fun generateGradleWrapperZip() {
    val outputDirectory = this.outputDirectory.get().file("data/common").asFile
    outputDirectory.mkdirs()

    val destFile = outputDirectory.resolve("gradle-wrapper.zip")

    if (destFile.exists()) {
      destFile.delete()
    }

    val stagingDir = File(outputDirectory, "staging")
    if (stagingDir.exists()) {
      stagingDir.deleteRecursively()
    }
    stagingDir.mkdirs()

    // Generate the files
    val generator = IDEWrapperGenerator()
    generator.jarFile = File(stagingDir, "gradle/wrapper/gradle-wrapper.jar")
    generator.scriptFile = File(stagingDir, "gradlew")
    generator.gradleVersion = GRADLE_VERSION
    generator.distributionType = Wrapper.DistributionType.BIN

    generator.generate(project)

    // Archive all generated files
    ZipOutputStream(destFile.outputStream().buffered()).use { zipOut ->
      stagingDir.walk(direction = FileWalkDirection.TOP_DOWN)
        .filter { it.isFile }
        .forEach { file ->
          val entry = ZipEntry(file.relativeTo(stagingDir).path)
          zipOut.putNextEntry(entry)
          file.inputStream().buffered().use { fileInStream ->
            fileInStream.transferTo(zipOut)
          }
        }

      zipOut.flush()
    }

    // finally, delete the staging directory
    stagingDir.deleteRecursively()
  }
}