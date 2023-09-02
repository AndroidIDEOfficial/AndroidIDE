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

package com.itsaky.androidide.gradle

import com.itsaky.androidide.utils.FileProvider
import java.io.File
import java.nio.file.Path
import kotlin.io.path.pathString

internal fun writeInitScript(file: File, deps: List<File>) {
  file.parentFile.mkdirs()

  val root = FileProvider.projectRoot().pathString
  val depsString = deps.filter { it.absolutePath.startsWith(root) }
    .joinToString(separator = System.lineSeparator()) {
      val isDir = it.isDirectory
      "classpath ${if (isDir) "files" else "files"}(\"${it}\")"
    }

  file.bufferedWriter().use {
    it.write("""
      initscript {
        dependencies {
          // make sure the init script plugin is in classpath
          $depsString
        }
      }
      
      apply plugin: com.itsaky.androidide.gradle.AndroidIDEInitScriptPlugin
    """.trimIndent())
  }
}

internal fun openProject(agpVersion: String = "", vararg plugins: String): Path {
  val projectRoot = FileProvider.projectRoot()
    .resolve("gradle-plugin/src/test/resources/sample-project")

  run {
    projectRoot.resolve("build.gradle.kts").toFile()
      .replaceAllPlaceholders(mapOf("AGP_VERSION" to agpVersion))
  }

  run {
    val pluginsText = plugins.joinToString(separator = "\n") { "id(\"$it\")" }
    projectRoot.resolve("app/build.gradle.kts").toFile()
      .replaceAllPlaceholders(mapOf("PLUGINS" to pluginsText))
  }

  return projectRoot
}

private fun File.replaceAllPlaceholders(entries: Map<String, String>) {
  val sb = StringBuilder(parentFile.resolve("${name}.in").readText())
  for ((placeholder, value) in entries) {
    sb.replace("@@${placeholder}@@".toRegex(RegexOption.LITERAL), value)
  }
  writeText(sb.toString())
}