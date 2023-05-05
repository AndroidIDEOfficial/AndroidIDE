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

package com.itsaky.androidide.templates.base.root

import com.itsaky.androidide.templates.base.ProjectTemplateBuilder
import com.itsaky.androidide.templates.base.baseAsset
import java.io.File

/**
 * Writes the `settings.gradle[.kts]` file in the project root directory.
 *
 * @param data The projecte template data.
 */
fun ProjectTemplateBuilder.settingsGradle() {
  executor.save(settingsGradleSrc(), settingsGradleFile())
}

/**
 * Writes the `build.gradle[.kts]` file in the project root directory.
 *
 * @param data The projecte template data.
 */
fun ProjectTemplateBuilder.buildGradle() {
  executor.save(buildGradleSrc(), buildGradleFile())
}

/**
 * Writes the `gradle.properties` file in the root project.
 *
 * @param data The project template data.
 */
fun ProjectTemplateBuilder.gradleProps() {
  val name = "gradle.properties"
  val gradleProps = File(data.projectDir, name)
  executor.copyAsset(baseAsset(name), gradleProps)
}