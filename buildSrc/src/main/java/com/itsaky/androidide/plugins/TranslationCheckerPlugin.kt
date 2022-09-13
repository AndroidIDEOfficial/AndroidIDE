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

import java.io.File
import java.io.FileOutputStream
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.process.ExecResult

/**
 * Plugin for generating a translation report for a module.
 *
 * @author Akash Yadav
 */
class TranslationCheckerPlugin : Plugin<Project> {
  
  override fun apply(target: Project) {
    target.afterEvaluate {
      tasks.register("checkTranslations") { doLast { checkTranslations(target) } }
    }
  }

  private fun checkTranslations(project: Project) {
    val php = File("/usr/bin/php")
    if (!php.exists()) {
      return
    }

    val resDir = project.file("src/main/res")
    val strings = File(resDir, "values/strings.xml")
    val reportDir = File(project.rootProject.buildDir, "translation-reports")
    reportDir.delete()

    if (!resDir.exists() || !strings.exists()) {
      project.logger.info("Default strings.xml file does not exist for project '${project.name}'")
      return
    }

    val translationDirs =
      resDir.listFiles { file -> file.isDirectory && file.name.startsWith("values-") }
        ?: emptyArray()

    for (dir in translationDirs) {
      val translation = File(dir, "strings.xml")
      if (!translation.exists()) {
        project.logger.info("No translation file specifed for '${dir.name}'. Skipping..")
        return
      }

      val out = File(reportDir, "${project.path.replace (':', '/')}/${dir.name}.txt")
      if (!out.parentFile.exists()) {
        out.parentFile.mkdirs()
      }

      if (out.exists()) {
        out.delete()
      }

      out.createNewFile()

      val result = doCheckTranslations(project, out, php, strings, translation)

      if (result.exitValue == 0) {
        out.delete()
      } else {
        project.logger.lifecycle(
          "Translation report for '${project.path}/${dir.name}' is written to '${out.absolutePath}'"
        )
      }
    }
  }

  private fun doCheckTranslations(
    project: Project,
    out: File,
    php: File,
    strings: File,
    translation: File
  ): ExecResult {
    val result =
      project.rootProject.exec {
        isIgnoreExitValue = true
        standardOutput = FileOutputStream(out)
        commandLine =
          listOf(
            php.absolutePath,
            "${project.rootProject.file(".tools/strings-check.php")}",
            strings.absolutePath,
            translation.absolutePath
          )
      }
    return result
  }
}
