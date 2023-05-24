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

package com.itsaky.androidide.templates.impl

import com.itsaky.androidide.templates.Language
import com.itsaky.androidide.templates.ProjectTemplate
import com.itsaky.androidide.templates.Sdk
import com.itsaky.androidide.templates.base.AndroidModuleTemplateBuilder
import com.itsaky.androidide.templates.base.ProjectTemplateBuilder
import com.itsaky.androidide.templates.base.baseProject
import com.itsaky.androidide.templates.impl.base.createRecipe

/**
 * Indents the given string for the given [indentation level][level].
 */
fun String.indent(level: Int): String {
  val lines = split(Regex("[\r\n]"))
  return StringBuilder().apply {
    for (line in lines) {
      append(line)
      append(" ".repeat(level * 4))
    }
  }.toString()
}

@Suppress("UnusedReceiverParameter")
internal fun AndroidModuleTemplateBuilder.templateAsset(name: String,
                                                        path: String
): String {
  return "templates/${name}/${path}"
}

internal fun baseProjectImpl(sdkFilter: ((Sdk) -> Boolean)? = null,
                             languageFilter: ((Language) -> Boolean)? = null,
                             block: ProjectTemplateBuilder.() -> Unit
): ProjectTemplate = baseProject(sdkFilter = sdkFilter, languageFilter = languageFilter) {
  block()

  // make sure we return a proper result
  if (!isRecipeSet) {
    recipe = createRecipe {}
  }
}