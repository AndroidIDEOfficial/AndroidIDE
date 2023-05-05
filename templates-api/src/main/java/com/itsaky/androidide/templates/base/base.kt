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

import com.itsaky.androidide.templates.Language
import com.itsaky.androidide.templates.Language.Java
import com.itsaky.androidide.templates.ModuleTemplate
import com.itsaky.androidide.templates.ModuleTemplateData
import com.itsaky.androidide.templates.ModuleType
import com.itsaky.androidide.templates.ModuleType.AndroidApp
import com.itsaky.androidide.templates.ModuleType.AndroidLibrary
import com.itsaky.androidide.templates.ParameterConstraint.DIRECTORY
import com.itsaky.androidide.templates.ParameterConstraint.EXISTS
import com.itsaky.androidide.templates.ParameterConstraint.MODULE_NAME
import com.itsaky.androidide.templates.ParameterConstraint.NONEMPTY
import com.itsaky.androidide.templates.ParameterConstraint.PACKAGE
import com.itsaky.androidide.templates.ProjectTemplate
import com.itsaky.androidide.templates.ProjectTemplateData
import com.itsaky.androidide.templates.ProjectVersionData
import com.itsaky.androidide.templates.R
import com.itsaky.androidide.templates.RecipeExecutor
import com.itsaky.androidide.templates.Sdk
import com.itsaky.androidide.templates.SpinnerWidget
import com.itsaky.androidide.templates.SrcFolder
import com.itsaky.androidide.templates.Template
import com.itsaky.androidide.templates.TemplateBuilder
import com.itsaky.androidide.templates.TemplateData
import com.itsaky.androidide.templates.TemplateRecipe
import com.itsaky.androidide.templates.TextFieldWidget
import com.itsaky.androidide.templates.base.root.buildGradleSrc
import com.itsaky.androidide.templates.base.root.gradleWrapper
import com.itsaky.androidide.templates.base.root.settingsGradleFile
import com.itsaky.androidide.templates.base.root.settingsGradleSrc
import com.itsaky.androidide.templates.enumParameter
import com.itsaky.androidide.templates.stringParameter
import com.itsaky.androidide.utils.AndroidUtils
import com.itsaky.androidide.utils.Environment
import java.io.File

sealed class PrePostRecipeTemplateBuilder<T : Template> : TemplateBuilder<T>() {

  internal var preRecipe: TemplateRecipe = {}
  internal var postRecipe: TemplateRecipe = {}

  override var recipe: TemplateRecipe? = null
    get() = {
      preRecipe()
      field?.let { it() }
      postRecipe()
    }
}

/**
 * @property executor The [RecipeExecutor] instance.
 * @property data The project template data.
 */
sealed class ExecutorDataTemplateBuilder<T : Template, D : TemplateData> :
  PrePostRecipeTemplateBuilder<T>() {

  internal var _executor: RecipeExecutor? = null
  internal var _data: D? = null

  val executor: RecipeExecutor
    get() = checkNotNull(_executor)

  val data: D
    get() = checkNotNull(_data)
}

/**
 * Builder for building project templates.
 */
class ProjectTemplateBuilder : ExecutorDataTemplateBuilder<ProjectTemplate, ProjectTemplateData>() {

  private var _defModule: ModuleTemplateData? = null
  private val defModuleTemplate: ModuleTemplate? = null

  internal val modules = mutableListOf<ModuleTemplate>()

  private val defModule: ModuleTemplateData
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
   * Writes the `settings.gradle[.kts]` file in the project root directory.
   */
  fun settingsGradle() {
    executor.save(settingsGradleSrc(), settingsGradleFile())
  }

  /**
   * Writes the `build.gradle[.kts]` file in the project root directory.
   */
  fun buildGradle() {
    executor.save(buildGradleSrc(), buildGradleFile())
  }

  /**
   * Get the `build.gradle[.kts] file for the project.l
   */
  fun buildGradleFile(): File {
    return data.buildGradleFile()
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
   * Configure the default template for the project.
   *
   * @param name The name of the module (gradle format, e.g. ':app').
   * @param block The module configurator.
   */
  fun defaultModule(name: String = ":app", block: AndroidModuleTemplateConfigurator) {
    check(defModuleTemplate == null) { "Default module has been already configured" }

    val module = AndroidModuleTemplateBuilder().apply {
      _name = name
      templateName = 0
      thumb = 0

      preRecipe = commonPreRecipe {
        return@commonPreRecipe defModule
      }

      postRecipe = commonPostRecipe()

      block()
    }.build()

    modules.add(module)
  }

  override fun buildInternal(): ProjectTemplate {
    return ProjectTemplate(modules, templateName!!, thumb!!, widgets!!, recipe!!)
  }
}

/**
 * Abstract [TemplateBuilder] for building module projects.
 *
 * @property name The name of the module (gradle format, e.g. ':app').
 */
abstract class ModuleTemplateBuilder :
  ExecutorDataTemplateBuilder<ModuleTemplate, ModuleTemplateData>() {

  protected val dependencies = hashSetOf<Dependency>()
  internal var _name: String? = null

  val name: String
    get() = checkNotNull(_name) { "Name not set to module template" }

  protected open fun RecipeExecutor.preConfig() {}
  protected open fun RecipeExecutor.postConfig() {}

  /**
   * Creates the source directory for the given [type].
   *
   * @param type The type of the source directory.
   * @param block Function to configure the source folder.
   */
  fun srcFolder(type: SrcFolder, block: (File) -> Unit = {}) {
    data.srcFolder(type).apply(block)
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
    srcFolder(SrcFolder.Main)

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

    // Write build.gradle[.kts]
    buildGradle()

    postConfig()
    extraConfig()
  }

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

/**
 * Setup base files for project templates.
 *
 * @param block Function to configure the template.
 */
fun baseProject(block: ProjectTemplateConfigurator): ProjectTemplate {
  return ProjectTemplateBuilder().apply {
    val projectName = stringParameter {
      name = R.string.project_app_name
      default = "My Application"
      constraints = listOf(NONEMPTY)
    }

    val packageName = stringParameter {
      name = R.string.package_name
      default = "com.example.myapplication"
      constraints = listOf(NONEMPTY, PACKAGE)

      suggest = {
        AndroidUtils.appNameToPackageName(/* appName = */ projectName.value, /* packageName = */
          this.value ?: "")
      }
    }

    val saveLocation = stringParameter {
      name = R.string.wizard_save_location
      default = Environment.PROJECTS_DIR.absolutePath
      constraints = listOf(NONEMPTY, DIRECTORY, EXISTS)

      suggest = {
        projectName.value!!.replace(" ", "").let {
          File(Environment.PROJECTS_DIR, it).absolutePath
        }
      }
    }

    val language = enumParameter<Language> {
      name = R.string.wizard_language
      default = Java
    }

    val minSdk = enumParameter<Sdk> {
      name = R.string.minimum_sdk
      default = Sdk.Lollipop
    }

    widgets(TextFieldWidget(projectName), TextFieldWidget(packageName),
      TextFieldWidget(saveLocation), SpinnerWidget(language), SpinnerWidget(minSdk))

    // Setup the required properties before executing the recipe
    preRecipe = {
      this@apply._executor = this

      this@apply._data = ProjectTemplateData(projectName.value!!,
        File(saveLocation.value!!, projectName.value!!), ProjectVersionData(),
        language = language.value!!, useKts = true)

      setDefaultModuleData(
        ModuleTemplateData(":app", packageName.value!!, data.moduleNameToDir(":app"),
          type = AndroidApp, language = language.value!!, minSdk = minSdk.value!!,
          useKts = data.useKts))
    }

    // After the recipe is executed, finalize the project creation
    // In this phase, we write the build scripts as they may need additional data based on the previous recipe
    // For example, writing settings.gradle[.kts] needs to know the name of the modules so that those can be includedl
    postRecipe = {
      // build.gradle[.kts]
      buildGradle()

      // settings.gradle[.kts]
      settingsGradle()

      // gradle.properties
      gradleProps()

      // gradlew
      // gradlew.bat
      // gradle/wrapper/gradle-wrapper.jar
      // gradle/wrapper/gradle-wrapper.properties
      gradleWrapper()
    }

    block()

  }.build()
}

/**
 * Create a new module project in this project.
 *
 * @param block The module configurator.
 */
fun baseAndroidModule(block: AndroidModuleTemplateConfigurator): ModuleTemplate {
  return AndroidModuleTemplateBuilder().apply {

    val moduleName = stringParameter {
      name = R.string.wizard_module_name
      default = ":app"
      constraints = listOf(NONEMPTY, MODULE_NAME)
    }

    val packageName = stringParameter {
      name = R.string.package_name
      default = "com.example.myapplication"
      constraints = listOf(NONEMPTY, PACKAGE)
    }

    val minSdk = enumParameter<Sdk> {
      name = R.string.minimum_sdk
      default = Sdk.Lollipop
    }

    val type = enumParameter<ModuleType> {
      name = R.string.wizard_module_type
      default = AndroidLibrary
    }

    val language = enumParameter<Language> {
      name = R.string.wizard_language
      default = Java
    }

    widgets(TextFieldWidget(moduleName), TextFieldWidget(packageName), SpinnerWidget(minSdk),
      SpinnerWidget(type), SpinnerWidget(language))

    preRecipe = commonPreRecipe {
      ModuleTemplateData(name = moduleName.value!!, packageName = packageName.value!!,
        projectDir = requireProjectData().moduleNameToDir(moduleName.value!!), type = type.value!!,
        language = language.value!!, minSdk = minSdk.value!!)
    }
    postRecipe = commonPostRecipe()

    block()
  }.build()
}

internal fun ProjectTemplateData.moduleNameToDir(name: String): File {
  return File(this.projectDir, moduleNameToDirName(name).replace(':', '/').trim { it == '/' })
}

internal fun moduleNameToDirName(name: String): String {
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

      // chars at other indices must be a letter or digit or
      || !(c.isDigit() || c.isLetter() || c == '-')

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