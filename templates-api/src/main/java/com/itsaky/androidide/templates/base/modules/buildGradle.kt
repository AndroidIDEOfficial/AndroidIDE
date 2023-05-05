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

package com.itsaky.androidide.templates.base.modules

import com.itsaky.androidide.templates.ModuleType
import com.itsaky.androidide.templates.base.AndroidModuleTemplateBuilder
import com.itsaky.androidide.templates.base.ModuleTemplateBuilder
import com.itsaky.androidide.templates.base.ProjectTemplateBuilder
import com.itsaky.androidide.templates.base.buildGradleFile
import com.itsaky.androidide.templates.base.root.buildGradleFile
import com.itsaky.androidide.templates.base.root.buildGradleSrc
import java.io.File

/**
 * Writes the `build.gradle[.kts]` file in the module root directory.
 *
 * @param data The projecte template data.
 */
fun ProjectTemplateBuilder.buildGradle() {
  executor.save(buildGradleSrc(), buildGradleFile())
}

internal fun ModuleTemplateBuilder.buildGradleFile(): File {
  return data.buildGradleFile()
}