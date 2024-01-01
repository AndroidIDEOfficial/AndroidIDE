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
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Generates the `gradle-wrapper.zip` file.
 *
 * @author Akash Yadav
 */
abstract class GradleWrapperBuilderTask : DefaultTask() {

  /**
   * The output directory.
   */
  @get:OutputDirectory
  abstract val outputDirectory: DirectoryProperty

  companion object {

    private const val GRADLE_VERSION = "7.4.2"

    private val GRADLE_PROPERTIES_CONTENTS = """
      distributionBase=GRADLE_USER_HOME
      distributionPath=wrapper/dists
      distributionUrl=https\://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip
      zipStoreBase=GRADLE_USER_HOME
      zipStorePath=wrapper/dists
    """.trimIndent()
  }

  @TaskAction
  fun generateGradleWrapperZip() {
    val outputDirectory = this.outputDirectory.get().file("data/common").asFile
    outputDirectory.mkdirs()

    val destFile = outputDirectory.resolve("gradle-wrapper.zip")

    if (destFile.exists()) {
      destFile.delete()
    }

    ZipOutputStream(destFile.outputStream().buffered()).use { zipOut ->

      // Add static files
      for (file in arrayOf("gradlew", "gradlew.bat", "gradle/wrapper/gradle-wrapper.jar")) {
        val fileIn = File(project.rootProject.rootDir, file)
        val entry = ZipEntry(file)
        zipOut.putNextEntry(entry)
        fileIn.inputStream().buffered().use { fileInStream ->
          fileInStream.transferTo(zipOut)
        }
      }

      zipOut.putNextEntry(ZipEntry("gradle/wrapper/gradle-wrapper.properties"))
      zipOut.write(GRADLE_PROPERTIES_CONTENTS.toByteArray(charset = StandardCharsets.UTF_8))

      zipOut.flush()
    }

    destFile.setLastModified(System.currentTimeMillis())
  }
}