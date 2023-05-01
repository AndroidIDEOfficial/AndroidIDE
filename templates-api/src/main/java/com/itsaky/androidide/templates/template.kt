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
import java.io.File

/**
 * Data that is used to create templates.
 */
sealed class TemplateData

/**
 * Recipe for creating the project/module.
 */
typealias TemplateRecipe = RecipeExecutor.(TemplateData) -> Unit

/**
 * Version information for the project.
 *
 * @property gradlePlugin The Android Gradle Plugin version.
 * @property gradle The Gradle version.
 * @property kotlin The Kotlin Plugin version.
 * @property minSdk The minimum SDK version for modules.
 * @property targetSdk The target SDK version for modules.
 * @property buildTools The build tools version for modules.
 */
data class VersionData(val gradlePlugin: String = ANDROID_GRADLE_PLUGIN_VERSION,
                       val gradle: String = GRADLE_DISTRIBUTION_VERSION,
                       val kotlin: String = KOTLIN_VERSION,

                       val minSdk: String = MIN_SDK_VERSION,
                       val targetSdk: String = TARGET_SDK_VERSION,
                       val buildTools: String = BUILD_TOOLS_VERSION
)

/**
 * Data for creating root projects.
 *
 * @property name The name of the project.
 * @property rootDir The root directory for the project.
 * @property version The version information for this project.
 * @property widgets The widgets to allow templates to accept input from the user.
 */
data class ProjectTemplateData(val name: String, val rootDir: File, val version: VersionData
) : TemplateData()

/**
 * Model for a template.
 *
 * @property templateName The name of the template.
 * @property thumb The thumbnail for the template.
 */
class Template(@StringRes val templateName: Int, @DrawableRes val thumb: Int,
               val widgets: List<Widget<*>>, val recipe: TemplateRecipe
) {

  val parameters: Collection<Parameter<*>>
    get() = widgets.filterIsInstance<ParameterWidget<*>>().map { it.parameter }
}

class TemplateBuilder(@StringRes var templateName: Int? = null, @DrawableRes var thumb: Int? = null,
                      var widgets: List<Widget<*>>? = null, var recipe: TemplateRecipe? = null
) {

  fun widgets(vararg widgets: Widget<*>) {
    this.widgets = widgets.toList()
  }

  fun build(): Template {
    checkNotNull(templateName) { "Template must have a name" }
    checkNotNull(thumb) { "Template must have a thumbnail" }
    checkNotNull(recipe) { "Template must have a recipe" }

    val widgets = this.widgets ?: emptyList()
    return Template(templateName!!, thumb!!, widgets, recipe!!)
  }
}

/**
 * Create a [Template] using the [TemplateBuilder].
 */
fun template(block: TemplateBuilder.() -> Unit): Template = TemplateBuilder().apply(block).build()