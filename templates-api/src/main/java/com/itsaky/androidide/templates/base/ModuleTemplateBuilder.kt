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
import com.itsaky.androidide.templates.RecipeExecutor
import com.itsaky.androidide.templates.SrcSet
import com.itsaky.androidide.templates.TemplateBuilder
import com.itsaky.androidide.templates.TemplateRecipe
import java.io.File

/**
 * Abstract [TemplateBuilder] for building module projects.
 *
 * @property name The name of the module (gradle format, e.g. ':app').
 * @author Akash Yadav
 */
abstract class ModuleTemplateBuilder :
  ExecutorDataTemplateBuilder<ModuleTemplate, ModuleTemplateData>() {

  protected val dependencies = hashSetOf<Dependency>()
  protected val javaSourceBuilder = JavaSourceBuilder()
  internal var _name: String? = null

  val name: String
    get() = checkNotNull(_name) { "Name not set to module template" }

  protected open fun RecipeExecutor.preConfig() {}
  protected open fun RecipeExecutor.postConfig() {}

  /**
   * Get the asset path for base module project template.
   *
   * @param path The path for the asset.
   * @see com.itsaky.androidide.templates.base.baseAsset
   */
  fun baseAsset(path: String) = baseAsset("module", path)

  /**
   * Get the `build.gradle[.kts]` file for this module.l
   */
  fun buildGradleFile(): File {
    return data.buildGradleFile()
  }

  /**
   * Get the `java` sources directory for the [SrcSet.Main] source set.
   */
  fun mainJavaSrc(): File {
    return javaSrc(SrcSet.Main)
  }

  /**
   * Get the `resources` directory for the [SrcSet.Main] source set.
   */
  fun mainResourcesDir(): File {
    return javaSrc(SrcSet.Main)
  }

  /**
   * Get the `resources` directory for the given [source set][srcSet].
   */
  fun resourcesDir(srcSet: SrcSet): File {
    return File(srcFolder(srcSet), "resources").also { it.mkdirs() }
  }

  /**
   * Get the `java` source directory for the given [srcSet].
   */
  fun javaSrc(srcSet: SrcSet): File {
    return File(srcFolder(srcSet), "java").also { it.mkdirs() }
  }

  /**
   * Get the source directory for the given [srcSet].
   *
   * @param srcSet The type of source directory.
   */
  fun srcFolder(srcSet: SrcSet): File {
    return data.srcFolder(srcSet)
  }

  /**
   * Configure the Java source files for this module.
   *
   * @param configure Function for configuring the Java source files.
   */
  fun java(configure: JavaSourceBuilder.() -> Unit) {
    javaSourceBuilder.apply(configure)
  }

  /**
   * Common pre-recipe configuration.
   *
   * @param moduleData  Called after the base configuration is setup and before the [recipe] is executed. Caller can perform its own
   * pre-recipe configuration here. Returns the [ModuleTemplateData] instance.
   */
  fun commonPreRecipe(extraConfig: ModuleTemplateConfigurator = {},
                      moduleData: RecipeExecutor.() -> ModuleTemplateData
  ): TemplateRecipe = {
    val data = moduleData()

    this@ModuleTemplateBuilder._data = data
    this@ModuleTemplateBuilder._name = data.name
    this@ModuleTemplateBuilder._executor = this

    data.projectDir.mkdirs()

    // Create the main source directory
    srcFolder(SrcSet.Main)

    preConfig()
    extraConfig()
  }

  /**
   * Common post-recipe configuration.
   *
   * @param extraConfig Called after the [recipe] is executed. Caller can perform its own
   * post-recipe configuration here.
   */
  fun commonPostRecipe(extraConfig: ModuleTemplateConfigurator = {}): TemplateRecipe = {

    // Write the java source files
    javaSourceBuilder.apply {
      write()
    }

    // Write build.gradle[.kts]
    buildGradle()

    postConfig()
    extraConfig()
  }

  /**
   * Adds the given dependency to the `build.gradle[.kts]` file for this module.
   */
  fun addDependency(dependency: Dependency) {
    this.dependencies.add(dependency)
  }

  /**
   * Writes the `build.gradle[.kts]` file for this module.
   */
  abstract fun RecipeExecutor.buildGradle()

  override fun buildInternal(): ModuleTemplate {
    return ModuleTemplate(name, templateName!!, thumb!!, widgets!!, recipe!!)
  }
}
