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

import com.itsaky.androidide.templates.BooleanParameter
import com.itsaky.androidide.templates.CheckBoxWidget
import com.itsaky.androidide.templates.EnumParameter
import com.itsaky.androidide.templates.FileTemplate
import com.itsaky.androidide.templates.FileTemplateRecipeResult
import com.itsaky.androidide.templates.Language
import com.itsaky.androidide.templates.ModuleTemplate
import com.itsaky.androidide.templates.ModuleTemplateData
import com.itsaky.androidide.templates.ModuleType
import com.itsaky.androidide.templates.ModuleType.AndroidApp
import com.itsaky.androidide.templates.ModuleType.AndroidLibrary
import com.itsaky.androidide.templates.ParameterConstraint.DIRECTORY
import com.itsaky.androidide.templates.ParameterConstraint.EXISTS
import com.itsaky.androidide.templates.ParameterConstraint.MODULE_NAME
import com.itsaky.androidide.templates.ParameterConstraint.NONEMPTY
import com.itsaky.androidide.templates.ProjectTemplate
import com.itsaky.androidide.templates.ProjectTemplateData
import com.itsaky.androidide.templates.ProjectVersionData
import com.itsaky.androidide.templates.R
import com.itsaky.androidide.templates.Sdk
import com.itsaky.androidide.templates.SpinnerWidget
import com.itsaky.androidide.templates.StringParameter
import com.itsaky.androidide.templates.TextFieldWidget
import com.itsaky.androidide.templates.base.util.getNewProjectName
import com.itsaky.androidide.templates.base.util.moduleNameToDir
import com.itsaky.androidide.templates.enumParameter
import com.itsaky.androidide.templates.minSdkParameter
import com.itsaky.androidide.templates.packageNameParameter
import com.itsaky.androidide.templates.projectLanguageParameter
import com.itsaky.androidide.templates.projectNameParameter
import com.itsaky.androidide.templates.stringParameter
import com.itsaky.androidide.templates.useKtsParameter
import com.itsaky.androidide.utils.AndroidUtils
import com.itsaky.androidide.utils.Environment
import java.io.File

typealias AndroidModuleTemplateConfigurator = AndroidModuleTemplateBuilder.() -> Unit

/**
 * Setup base files for project templates.
 *
 * @param block Function to configure the template.
 */
inline fun baseProject(projectName: StringParameter = projectNameParameter(),
  packageName: StringParameter = packageNameParameter(),
  useKts: BooleanParameter = useKtsParameter(),
  minSdk: EnumParameter<Sdk> = minSdkParameter(),
  language: EnumParameter<Language> = projectLanguageParameter(),
  projectVersionData: ProjectVersionData = ProjectVersionData(),
  crossinline block: ProjectTemplateBuilder.() -> Unit
): ProjectTemplate {
  return ProjectTemplateBuilder().apply {

    // When project name is changed, change the package name accordingly
    projectName.observe { name ->
      val newPackage =
        AndroidUtils.appNameToPackageName(name.value, packageName.value)
      packageName.setValue(newPackage)
    }

    Environment.mkdirIfNotExits(Environment.PROJECTS_DIR)

    val saveLocation = stringParameter {
      name = R.string.wizard_save_location
      default = Environment.PROJECTS_DIR.absolutePath
      endIcon = { R.drawable.ic_folder }
      constraints = listOf(NONEMPTY, DIRECTORY, EXISTS)
    }

    projectName.doBeforeCreateView {
      it.setValue(getNewProjectName(saveLocation.value, projectName.value))
    }

    widgets(TextFieldWidget(projectName), TextFieldWidget(packageName),
      TextFieldWidget(saveLocation), SpinnerWidget(language),
      SpinnerWidget(minSdk), CheckBoxWidget(useKts))

    // Setup the required properties before executing the recipe
    preRecipe = {
      this@apply._executor = this

      this@apply._data = ProjectTemplateData(projectName.value,
        File(saveLocation.value, projectName.value), projectVersionData,
        language = language.value, useKts = useKts.value)

      if (data.projectDir.exists() && data.projectDir.listFiles()
          ?.isNotEmpty() == true
      ) {
        throw IllegalArgumentException("Project directory already exists")
      }

      setDefaultModuleData(
        ModuleTemplateData(":app", appName = data.name, packageName.value,
          data.moduleNameToDir(":app"), type = AndroidApp,
          language = language.value, minSdk = minSdk.value,
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

      // .gitignore
      gitignore()
    }

    block()

  }.build() as ProjectTemplate
}

/**
 * Create a new module project in this project.
 *
 * @param block The module configurator.
 */
inline fun baseAndroidModule(isLibrary: Boolean = false,
  crossinline block: AndroidModuleTemplateConfigurator
): ModuleTemplate {
  return AndroidModuleTemplateBuilder().apply {

    val appName = if (isLibrary) null else projectNameParameter()
    val language = projectLanguageParameter()
    val minSdk = minSdkParameter()
    val packageName = packageNameParameter()
    val useKts = useKtsParameter()

    val moduleName = stringParameter {
      name = R.string.wizard_module_name
      default = ":app"
      constraints = listOf(NONEMPTY, MODULE_NAME)
    }

    val type = enumParameter<ModuleType> {
      name = R.string.wizard_module_type
      default = AndroidLibrary
      startIcon = { R.drawable.ic_android }
      displayName = ModuleType::typeName
    }

    widgets(TextFieldWidget(moduleName))

    appName?.let {
      widgets(TextFieldWidget(it))
    }

    widgets(TextFieldWidget(packageName), SpinnerWidget(minSdk),
      SpinnerWidget(type), SpinnerWidget(language), CheckBoxWidget(useKts))

    preRecipe = commonPreRecipe {
      ModuleTemplateData(name = moduleName.value, appName = appName?.value,
        packageName = packageName.value,
        projectDir = requireProjectData().moduleNameToDir(moduleName.value),
        type = type.value, language = language.value, minSdk = minSdk.value,
        useKts = useKts.value)
    }
    postRecipe = commonPostRecipe()

    block()
  }.build() as ModuleTemplate
}

/**
 * Creates a template for a file.
 *
 * @param dir The directory in which the file will be created.
 * @param configurator The configurator to configure the template.
 * @return The [FileTemplate].
 */
inline fun <R : FileTemplateRecipeResult> baseFile(dir: File,
  crossinline configurator: FileTemplateBuilder<R>.() -> Unit
): FileTemplate<R> {
  return FileTemplateBuilder<R>(dir).apply(configurator)
    .build() as FileTemplate<R>
}