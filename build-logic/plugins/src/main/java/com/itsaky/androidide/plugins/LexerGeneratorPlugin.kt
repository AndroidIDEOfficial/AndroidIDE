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

package com.itsaky.androidide.plugins

import com.itsaky.androidide.build.config.BuildConfig
import org.antlr.v4.Tool
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

/**
 * Generates lexers from the grammar files in 'src/main/antlr' directory of the project.
 *
 * @author Akash Yadav
 */
class LexerGeneratorPlugin : Plugin<Project> {

  companion object {

    const val LEXER_BASE_PACKAGE = "${BuildConfig.packageName}.lexers"
    const val EXT_G4 = "g4"
  }

  override fun apply(target: Project) {
    val grammarDir = target.file("src/main/antlr")
    if (!grammarDir.exists()) {
      target.logger.warn(
        "${LexerGeneratorPlugin::class.simpleName} has been applied to project '${target.path}' but antlr grammars directory was not found."
      )
      return
    }

    grammarDir.listFiles()?.filter { it.isDirectory }?.forEach { target.generateGrammar(it) }
      ?: run {
        target.logger.error("Failed to list files in $grammarDir")
        return
      }
  }

  private fun Project.generateGrammar(grammarDir: File) {
    val pck = "${LEXER_BASE_PACKAGE}.${grammarDir.name}"
    val files =
      grammarDir
        .listFiles()
        ?.filter { it.isFile && it.extension == EXT_G4 }
        ?.map { it.absolutePath }
        ?: run {
          logger.error("Failed to list grammar files in directory $grammarDir")
          return
        }

    val outDir = file("src/main/java/${pck.replace('.', '/')}")
    if (!outDir.exists() && !outDir.mkdirs()) {
      logger.error("Failed to create directory $outDir")
      return
    }

    val options =
      mutableListOf(
        "-o",
        outDir.absolutePath,
        "-package",
        pck,
        "-listener",
        "-visitor",
        "-Xexact-output-dir"
      )
    options.addAll(files)

    Tool(options.toTypedArray()).processGrammarsOnCommandLine()
  }
}
