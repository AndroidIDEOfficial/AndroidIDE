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

package com.itsaky.androidide.gradle

import com.itsaky.androidide.buildinfo.BuildInfo
import org.gradle.api.Plugin
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import java.net.URI

/**
 * Plugin for the AndroidIDE's Gradle Init Script.
 *
 * @author Akash Yadav
 */
class AndroidIDEInitScriptPlugin : Plugin<Gradle> {

  companion object {

    /**
     * Property that is set in tests to indicate that the subprojects must be configured to use `mavenLocal()` instead of
     * `mavenCentral()` and maven snapshots repository.
     *
     * **This is an internal property and should not be manually set by users.**
     */
    private const val PROPERTY_ENABLE_MAVEN_LOCAL = "androidide.plugins.internal.testing.enableMavenLocal"
  }

  override fun apply(target: Gradle) {
    ideLog("Applying ${javaClass.simpleName}")
    target.settingsEvaluated { settings ->
      val isMavenLocalEnabled = settings.startParameter.run {
        projectProperties.containsKey(PROPERTY_ENABLE_MAVEN_LOCAL)
            && projectProperties[PROPERTY_ENABLE_MAVEN_LOCAL].toString().toBoolean()
      }

      settings.addDependencyRepositories(isMavenLocalEnabled)
    }
    target.projectsLoaded { gradle ->
      gradle.rootProject.subprojects.forEach { sub ->
        sub.buildscript.dependencies.apply {
          add("classpath", ideDependency("gradle-plugin"))
        }

        sub.afterEvaluate {
          sub.pluginManager.apply("${BuildInfo.PACKAGE_NAME}.gradle")
        }
      }
    }
  }

  @Suppress("UnstableApiUsage")
  private fun Settings.addDependencyRepositories(isMavenLocalEnabled: Boolean) {
    dependencyResolutionManagement.run {
      repositories.configureRepositories(isMavenLocalEnabled)
    }

    pluginManagement.apply {
      repositories.configureRepositories(isMavenLocalEnabled)
    }
  }

  private fun RepositoryHandler.configureRepositories(isMavenLocalEnabled: Boolean) {
    if (!isMavenLocalEnabled) {
      // For release builds
      mavenCentral()

      // For AndroidIDE CI builds
      maven { repository ->
        repository.url = URI.create(
          "https://s01.oss.sonatype.org/content/repositories/snapshots/"
        )
      }
    } else {
      // maven local has been enabled explicitly
      mavenLocal()
    }

    // for AGP API dependency
    google()
  }
}