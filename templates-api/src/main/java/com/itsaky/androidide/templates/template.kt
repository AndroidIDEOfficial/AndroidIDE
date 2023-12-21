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

package com.itsaky.androidide.templates

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.itsaky.androidide.templates.base.util.optonallyKts
import java.io.File
import java.util.UUID

/**
 * Empty template recipe (no-op).
 */
val EMPTY_RECIPE = TemplateRecipe { null }

/**
 * Data that is used to create templates.
 */
sealed class TemplateData

/**
 * Result obtained after the execution of [TemplateRecipe].
 */
interface TemplateRecipeResult

interface TemplateRecipeResultWithData<D : TemplateData> : TemplateRecipeResult {

  /**
   * The data used to create the template.
   */
  val data: D
}

/**
 * Result of recipe execution for a [ProjectTemplate].
 */
interface ProjectTemplateRecipeResult : TemplateRecipeResultWithData<ProjectTemplateData>

/**
 * Result of recipe execution for a [ModuleTemplate].
 */
interface ModuleTemplateRecipeResult : TemplateRecipeResultWithData<ModuleTemplateData>

/**
 * Result of recipe execution for a [FileTemplate].
 */
interface FileTemplateRecipeResult : TemplateRecipeResult

/**
 * Recipe used to configure the execution of [TemplateRecipe].
 *
 * A [TemplateRecipeConfigurator] is called just before the [TemplateRecipe] is executed.
 */
typealias TemplateRecipeConfigurator = RecipeExecutor.() -> Unit

/**
 * Recipe for creating the project/module.
 */
fun interface TemplateRecipe<T : TemplateRecipeResult> {

  /**
   * Execute the recipe and return the [result][TemplateRecipeResult].
   *
   * @param executor The [RecipeExecutor].
   * @return The result of the execution.
   */
  fun execute(executor: RecipeExecutor): T?
}

/**
 * Recipe used to finalize the execution of [TemplateRecipe].
 *
 * A [TemplateRecipeFinalizer] is called just after the [TemplateRecipe] is executed.
 */
typealias TemplateRecipeFinalizer = RecipeExecutor.() -> Unit

/**
 * Base class for [TemplateData] implementations.
 *
 * @property name The name of the module.
 * @property projectDir The directory for the module.
 * @property language The source language for the module.
 * @property useKts Whether to use Kotlin DSL for Gradle build scripts.
 */
abstract class BaseTemplateData(val name: String, val projectDir: File, val language: Language,
  val useKts: Boolean) : TemplateData() {

  /**
   * Get the `build.gradle[.kts]` file for the project.
   */
  fun buildGradleFile(): File {
    return File(projectDir, optonallyKts("build.gradle"))
  }
}

/**
 * Language for source files.
 */
enum class Language(val lang: String, val ext: String) {

  Java("Java", "java"), Kotlin("Kotlin", "kt");
}

/**
 * The type of module.
 *
 * @property typeName The name of the type.
 */
enum class ModuleType(val typeName: String) {

  AndroidApp("Android Application"), AndroidLibrary("Android Library"), JavaLibrary("Java library")
}

/**
 * Type of source folder that can be in a module.
 *
 * @property folder The name of the folder.
 */
enum class SrcSet(val folder: String) {

  /**
   * `src/main`.
   */
  Main("main"),

  /**
   * `src/test`.
   */
  Test("test"),

  /**
   * `src/androidTest`.
   */
  AndroidTest("androidTest")
}

/**
 * Version information for the project.
 *
 * @property gradlePlugin The Android Gradle Plugin version.
 * @property gradle The Gradle version.
 * @property kotlin The Kotlin Plugin version.
 */
data class ProjectVersionData(val gradlePlugin: String = ANDROID_GRADLE_PLUGIN_VERSION,
  val gradle: String = GRADLE_DISTRIBUTION_VERSION, val kotlin: String = KOTLIN_VERSION)

/**
 * Version information about a module.
 *
 * @property targetSdk The target SDK version for modules.
 * @property buildTools The build tools version for modules.
 */
data class ModuleVersionData(val minSdk: Sdk, val targetSdk: Sdk = TARGET_SDK_VERSION,
  val compileSdk: Sdk = COMPILE_SDK_VERSION, val javaSource: String = JAVA_SOURCE_VERSION,
  val javaTarget: String = JAVA_TARGET_VERSION) {

  /**
   * Get the Java source version string representation in the `JavaVersion.VERSION_${version}` format.
   */
  fun javaSource() = javaVersionPrefix(javaSource)

  /**
   * Get the Java target version string representation in the `JavaVersion.VERSION_${version}` format.
   */
  fun javaTarget() = javaVersionPrefix(javaTarget)

  private fun javaVersionPrefix(version: String): String = "JavaVersion.VERSION_${version}"
}

/**
 * Data for creating root projects.
 *
 * @property version The version information for this project.
 */
class ProjectTemplateData(name: String, projectDir: File, val version: ProjectVersionData,
  language: Language, useKts: Boolean) : BaseTemplateData(name, projectDir, language, useKts)

/**
 * Data for creating module projects.
 *
 * @property packageName The package name of the module.
 * @property type The type of module.
 * @property versions Version information for the module.
 */
open class ModuleTemplateData(name: String, val appName: String?, val packageName: String,
  projectDir: File, val type: ModuleType, language: Language, useKts: Boolean = true, minSdk: Sdk,
  val versions: ModuleVersionData = ModuleVersionData(minSdk)) :
  BaseTemplateData(name, projectDir, language, useKts) {

  private val srcDirs = mutableMapOf<SrcSet, File>()

  fun srcFolder(srcSet: SrcSet): File {
    return srcDirs.computeIfAbsent(srcSet) {
      File(projectDir, "src/${it.folder}")
    }.also { it.mkdirs() }
  }
}

/**
 * Model for a template.
 *
 * @property templateName The name of the template.
 * @property thumb The thumbnail for the template.
 */
open class Template<R : TemplateRecipeResult>(@StringRes open val templateName: Int,
  @DrawableRes open val thumb: Int, open val widgets: List<Widget<*>>,
  open val recipe: TemplateRecipe<R>) {

  /**
   * The ID for this template.
   */
  val templateId: String by lazy {
    UUID.randomUUID().toString()
  }

  open val parameters: Collection<Parameter<*>>
    get() = widgets.filterIsInstance<ParameterWidget<*>>().map { it.parameter }

  open fun release() {
    widgets.forEach { it.release() }
  }

  companion object {

    @JvmStatic
    val EMPTY = Template(-1, -1, emptyList(), EMPTY_RECIPE)
  }
}

open class ProjectTemplate(val moduleTemplates: List<Template<*>>, @StringRes templateName: Int,
  @DrawableRes thumb: Int, widgets: List<Widget<*>>,
  recipe: TemplateRecipe<ProjectTemplateRecipeResult>) :
  Template<ProjectTemplateRecipeResult>(templateName, thumb, widgets, recipe) {

  override val parameters: Collection<Parameter<*>>
    get() = if (moduleTemplates.isEmpty()) super.parameters else super.parameters.toMutableList()
      .apply {
        addAll(moduleTemplates.flatMap { it.parameters })
      }

  override val widgets: List<Widget<*>>
    get() = if (moduleTemplates.isEmpty()) super.widgets else super.widgets.toMutableList().apply {
      addAll(moduleTemplates.flatMap { it.widgets })
    }

  override val recipe: TemplateRecipe<ProjectTemplateRecipeResult>
    get() = if (moduleTemplates.isEmpty()) super.recipe else super.recipe.let { projectRecipe ->
      TemplateRecipe {
        val result = projectRecipe.execute(it)
        moduleTemplates.forEach { module -> module.recipe.execute(it) }
        result
      }
    }

  override fun release() {
    super.release()
    moduleTemplates.forEach { it.release() }
  }
}

/**
 * Template for a module project.
 *
 * @property name The mdoule name (gradle format, e.g. ':app').
 */
open class ModuleTemplate(val name: String, @StringRes templateName: Int, @DrawableRes thumb: Int,
  widgets: List<Widget<*>>,
  recipe: TemplateRecipe<ModuleTemplateRecipeResult>) :
  Template<ModuleTemplateRecipeResult>(templateName, thumb, widgets, recipe)

/**
 * Template for creating a file.
 */
open class FileTemplate<R : FileTemplateRecipeResult>(@StringRes name: Int, @DrawableRes thumb: Int,
  widgets: List<Widget<*>>, recipe: TemplateRecipe<R>) : Template<R>(name, thumb, widgets, recipe)

/**
 * Base class for template builders.
 *
 * @property templateName String resource for template name.
 * @property thumb Drawable resource for the template thumbnail.
 * @property widgets The widgets that will be rendered while creating this template.
 * @property recipe The recipe for building the template.
 */
abstract class TemplateBuilder<R : TemplateRecipeResult>(
  @StringRes open var templateName: Int? = null, @DrawableRes open var thumb: Int? = null,
  open var widgets: List<Widget<*>>? = null, open var recipe: TemplateRecipe<R>? = null) {

  /**
   * Adds the given widgets to the widgets list.
   *
   * @param widgets The widgets to add.
   */
  fun widgets(vararg widgets: Widget<*>) {
    var new = this.widgets?.toMutableList() ?: mutableListOf()
    if (new.isNotEmpty()) {
      // If any widgets have been already added, add the new widgets to the list
      new.addAll(widgets)
    } else {
      new = widgets.toMutableList()
    }
    this.widgets = new
  }

  fun build(): Template<R> {
    requireNotNull(templateName) { "Template must have a name" }
    requireNotNull(thumb) { "Template must have a thumbnail" }
    requireNotNull(recipe) { "Template must have a recipe" }

    this.widgets = this.widgets ?: emptyList()

    return buildInternal()
  }

  protected abstract fun buildInternal(): Template<R>
}
