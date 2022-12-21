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
package com.itsaky.androidide.lsp.api

import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.readText

/**
 * Provides paths to source files in 'resources' directory.
 *
 * @author Akash Yadav
 */
class FileProvider {

  companion object {

    @JvmField
    val extension = run {
      val file = File(".").canonicalFile
      when (file.name) {
        "xml" -> "xml" // Testing in ':lsp:xml' module
        else -> "java" // Testing in ':lsp:java' or ':lsp:testing' module
      }
    }

    @JvmStatic fun currentDir(): Path = Paths.get(System.getProperty("user.dir")!!)

    @JvmStatic fun implModule(): Path = currentDir().resolve("../../subprojects/tooling-api-impl")
    @JvmStatic fun projectRoot(): Path = currentDir().resolve("../../tests/test-project")

    /**
     * Get the path to the 'resources' directory.
     *
     * @return The the resources directory.
     */
    @JvmStatic
    fun resources(): Path {
      return projectRoot().resolve("app/src/main/resources")
    }

    /**
     * Get the path to the file in resources.
     *
     * @param name The name of the file. Nested file paths can be separated using '/'.
     * @return The path to the file.
     */
    @JvmStatic
    fun sourceFile(name: String): Path {
      return resources().resolve("${name}_template.$extension").normalize()
    }

    @JvmStatic fun contents(file: Path): StringBuilder = StringBuilder(file.readText())
  }
}
