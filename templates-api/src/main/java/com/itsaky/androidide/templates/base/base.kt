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
import com.itsaky.androidide.templates.Sdk
import com.itsaky.androidide.templates.SpinnerWidget
import com.itsaky.androidide.templates.TextFieldWidget
import com.itsaky.androidide.templates.base.util.AndroidModuleTemplateConfigurator
import com.itsaky.androidide.templates.base.util.ProjectTemplateConfigurator
import com.itsaky.androidide.templates.base.util.moduleNameToDir
import com.itsaky.androidide.templates.enumParameter
import com.itsaky.androidide.templates.stringParameter
import com.itsaky.androidide.utils.AndroidUtils
import com.itsaky.androidide.utils.Environment
import java.io.File


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
      displayName = Language::lang
    }

    val minSdk = enumParameter<Sdk> {
      name = R.string.minimum_sdk
      default = Sdk.Lollipop
      displayName = Sdk::displayName
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
      displayName = Sdk::displayName
    }

    val type = enumParameter<ModuleType> {
      name = R.string.wizard_module_type
      default = AndroidLibrary
      displayName = ModuleType::typeName
    }

    val language = enumParameter<Language> {
      name = R.string.wizard_language
      default = Java
      displayName = Language::lang
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