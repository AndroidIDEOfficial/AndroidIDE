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

package com.itsaky.androidide.templates.base

import com.itsaky.androidide.templates.BaseTemplateData
import java.io.File

typealias ProjectTemplateConfigurator = ProjectTemplateBuilder.() -> Unit
typealias ModuleTemplateConfigurator = ModuleTemplateBuilder.() -> Unit
typealias AndroidModuleTemplateConfigurator = AndroidModuleTemplateBuilder.() -> Unit

/**
 * Get the asset path for base root project template.
 */
internal fun ProjectTemplateBuilder.baseAsset(path: String) = baseAsset("root", path)

/**
 * Get the asset path for base module project template.
 */
internal fun ModuleTemplateBuilder.baseAsset(path: String) = baseAsset("module", path)

/**
 * Get the asset path for base template.
 */
internal fun baseAsset(type: String, path: String): String {
  return "templates/base/${type}/${path}"
}

internal fun BaseTemplateData.buildGradleFile(): File {
  return File(projectDir, optonallyKts("build.gradle"))
}

internal fun BaseTemplateData.optonallyKts(file: String): String {
  return if (useKts) "${file}.kts" else file
}