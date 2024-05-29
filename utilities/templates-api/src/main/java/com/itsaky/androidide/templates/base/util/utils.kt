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

package com.itsaky.androidide.templates.base.util

import com.itsaky.androidide.templates.BaseTemplateData
import com.itsaky.androidide.templates.ProjectTemplateData
import java.io.File

/**
 * Get the asset path for base template.
 */
internal fun baseAsset(type: String, path: String): String {
  return "templates/base/${type}/${path}"
}

internal fun BaseTemplateData.optonallyKts(file: String): String {
  return if (useKts) "${file}.kts" else file
}

fun ProjectTemplateData.moduleNameToDir(name: String): File {
  return File(this.projectDir, moduleNameToDirName(name).replace(':', '/').trim { it == '/' })
}

fun isValidModuleName(name: String): Boolean {
  if (name.length < 2 || name.isBlank() || name[0] != ':') {
    return false
  }

  val moduleName = name.substring(1)
  return moduleNameToDirName(moduleName) == moduleName
}

/**
 * Converts a Gradle module name to a directory name.
 *
 * @param name The module name.
 * @return The directory name.
 */
fun moduleNameToDirName(name: String): String {
  val result = StringBuilder()
  var prev: Char? = null
  for (i in name.indices) {
    var c = name[i]
    if (c == ' ') {
      c = '-'
    }

    if (

    // the first character must be a letter
      (i == 0 && !c.isLetter())

      // chars at other indices must be a letter, digit, hyphen or an underscore
      || !(c.isDigit() || c.isLetter() || c == '-' || c =='_')

      // must not include consecutive '-'
      || (prev == '-' && c == '-')
    ) {

      prev = c
      continue
    }

    result.append(c)
    prev = c
  }

  return result.trim { it == ' ' || it == '-' }.toString()
}

/**
 * Get the name of a new project. This checks for existence of existent files/directories
 * until a non-existent project name is found.
 *
 * @param baseDir The base project directory.
 * @param name The current project name.
 */
fun getNewProjectName(baseDir: String, name: String) : String {
  var i = 1
  var projectDir = File(baseDir, name)
  while (projectDir.exists()) {
    projectDir = File(baseDir, name + i)
    ++i
  }

  return projectDir.name
}