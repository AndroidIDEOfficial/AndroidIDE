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

import com.itsaky.androidide.templates.ModuleTemplate
import com.itsaky.androidide.templates.ModuleTemplateData
import com.itsaky.androidide.templates.ProjectTemplate
import com.itsaky.androidide.templates.ProjectTemplateData
import com.itsaky.androidide.templates.ProjectTemplateRecipeResult
import com.itsaky.androidide.templates.base.root.buildGradleSrcGroovy
import com.itsaky.androidide.templates.base.root.buildGradleSrcKts
import com.itsaky.androidide.templates.base.root.gradleWrapperJar
import com.itsaky.androidide.templates.base.root.gradleWrapperProps
import com.itsaky.androidide.templates.base.root.settingsGradleSrcStr
import com.itsaky.androidide.templates.base.util.optonallyKts
import java.io.File

/**
 * Builder for building project templates.
 *
 * @author Akash Yadav
 */
class ProjectTemplateBuilder :
  ExecutorDataTemplateBuilder<ProjectTemplateRecipeResult, ProjectTemplateData>() {

  private var _defModule: ModuleTemplateData? = null
  internal val defModuleTemplate: ModuleTemplate? = null

  internal val modules = mutableListOf<ModuleTemplate>()

  internal val defModule: ModuleTemplateData
    get() = checkNotNull(_defModule) { "Module template data not set" }

  /**
   * Set the template data that will be used to create the default application module in the project.
   *
   * @param data The module template data to use.
   */
  fun setDefaultModuleData(data: ModuleTemplateData) {
    _defModule = data
  }

  /**
   * Get the asset path for base root project template.
   *
   * @param path The path to the asset.
   * @see com.itsaky.androidide.templates.base.baseAsset
   */
  fun baseAsset(path: String) =
    com.itsaky.androidide.templates.base.util.baseAsset("root", path)

  /**
   * Get the `build.gradle[.kts] file for the project.
   */
  fun buildGradleFile(): File {
    return data.buildGradleFile()
  }

  /**
   * Writes the `build.gradle[.kts]` file in the project root directory.
   */
  fun buildGradle() {
    executor.save(buildGradleSrc(), buildGradleFile())
  }

  /**
   * Get the source for `build.gradle[.kts]` files.
   */
  fun buildGradleSrc(): String {
    return if (data.useKts) buildGradleSrcKts() else buildGradleSrcGroovy()
  }

  /**
   * Writes the `settings.gradle[.kts]` file in the project root directory.
   */
  fun settingsGradle() {
    executor.save(settingsGradleSrc(), settingsGradleFile())
  }

  /**
   * Get the `settings.gradle[.kts]` file for this project.
   */
  fun settingsGradleFile(): File {
    return File(data.projectDir, data.optonallyKts("settings.gradle"))
  }

  /**
   * Get the source for `settings.gradle[.kts]`.
   */
  fun settingsGradleSrc(): String {
    return settingsGradleSrcStr()
  }

  /**
   * Writes the `gradle.properties` file in the root project.
   */
  fun gradleProps() {
    val name = "gradle.properties"
    val gradleProps = File(data.projectDir, name)
    executor.copyAsset(baseAsset(name), gradleProps)
  }

  /**
   * Writes/copies the Gradle Wrapper related files in the project directory.
   */
  fun gradleWrapper() {
    val gradlew = File(data.projectDir, "gradlew")
    val gradlewBat = File(data.projectDir, "${gradlew.name}.bat")
    executor.copyAsset(baseAsset(gradlew.name), gradlew)
    executor.copyAsset(baseAsset(gradlewBat.name), gradlewBat)

    gradlew.setExecutable(true)
    gradlewBat.setExecutable(true)

    gradleWrapperJar()
    gradleWrapperProps()
  }

  /**
   * Writes the `.gitignore` file in the project directory.
   */
  fun gitignore() {
    val gitignore = File(data.projectDir, ".gitignore")
    executor.copyAsset(baseAsset("gitignore"), gitignore)
  }

  override fun buildInternal(): ProjectTemplate {
    return ProjectTemplate(modules, templateName!!, thumb!!, description,
      widgets!!, recipe!!)
  }
}
