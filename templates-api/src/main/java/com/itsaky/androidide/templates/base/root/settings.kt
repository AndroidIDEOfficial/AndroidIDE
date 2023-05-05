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
import com.itsaky.androidide.templates.base.optonallyKts
import java.io.File

fun ProjectTemplateBuilder.settingsGradleFile(): File {
  return File(data.projectDir, data.optonallyKts("settings.gradle"))
}

fun ProjectTemplateBuilder.settingsGradleSrc(): String {
  return if (data.useKts) settingsGradleSrcKts() else return settingsGradleSrcGroovy()
}

internal fun ProjectTemplateBuilder.settingsGradleSrcKts(): String {
  return """
    pluginManagement {
        repositories {
            gradlePluginPortal()
            google()
            mavenCentral()
        }
    }
    
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            google()
            mavenCentral()
        }
    }
    
    rootProject.name = "${data.name}"
    ${modules.joinToString(separator = "\n") { "include '${it}'" }} 
  """.trimIndent()
}

internal fun ProjectTemplateBuilder.settingsGradleSrcGroovy(): String {
  return """
    pluginManagement {
      repositories {
        google()
        mavenCentral()
      }
    }
    
    dependencyResolutionManagement {
      repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
      repositories {
        google()
        mavenCentral()
      }
    }

    rootProject.name = "${data.name}"
    include(${modules.joinToString(separator = ", ") { "\"${it}\"" }})
    
  """.trimIndent()
}
