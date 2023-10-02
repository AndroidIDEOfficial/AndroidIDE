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
import com.itsaky.androidide.tooling.api.LogSenderConfig._PROPERTY_IS_TEST_ENV
import com.itsaky.androidide.tooling.api.LogSenderConfig._PROPERTY_MAVEN_LOCAL_REPOSITORY
import org.gradle.api.Plugin
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.logging.Logging
import java.io.File
import java.io.FileNotFoundException
import java.net.URI

/**
 * Plugin for the AndroidIDE's Gradle Init Script.
 *
 * @author Akash Yadav
 */
class AndroidIDEInitScriptPlugin : Plugin<Gradle> {

  companion object {

    private val logger = Logging.getLogger(AndroidIDEInitScriptPlugin::class.java)
  }

  override fun apply(target: Gradle) {
    target.settingsEvaluated { settings ->
      val (isTestEnv, mavenLocalRepos) = settings.startParameter.run {
        val isTestEnv = projectProperties.containsKey(_PROPERTY_IS_TEST_ENV)
            && projectProperties[_PROPERTY_IS_TEST_ENV].toString().toBoolean()
        val mavenLocalRepos = projectProperties.getOrDefault(_PROPERTY_MAVEN_LOCAL_REPOSITORY, "")
        isTestEnv to mavenLocalRepos
      }

      settings.addDependencyRepositories(isTestEnv, mavenLocalRepos)
    }

    target.projectsLoaded { gradle ->
      gradle.rootProject.subprojects { sub ->
        sub.buildscript.dependencies.apply {
          add("classpath", sub.ideDependency("gradle-plugin"))
        }

        sub.afterEvaluate {
          logger.info("Trying to apply plugin '${BuildInfo.PACKAGE_NAME}' to project '${sub.path}'")
          sub.pluginManager.apply(BuildInfo.PACKAGE_NAME)
        }
      }
    }
  }

  @Suppress("UnstableApiUsage")
  private fun Settings.addDependencyRepositories(isMavenLocalEnabled: Boolean,
    mavenLocalRepo: String) {
    dependencyResolutionManagement.run {
      repositories.configureRepositories(isMavenLocalEnabled, mavenLocalRepo)
    }

    pluginManagement.apply {
      repositories.configureRepositories(isMavenLocalEnabled, mavenLocalRepo)
    }
  }

  private fun RepositoryHandler.configureRepositories(
    isMavenLocalEnabled: Boolean,
    mavenLocalRepos: String
  ) {

    if (!isMavenLocalEnabled) {

      // For AndroidIDE CI builds
      maven { repository ->
        repository.url = URI.create(BuildInfo.SNAPSHOTS_REPOSITORY)
      }
    } else {
      logger.info("Using local maven repository for classpath resolution...")

      for (mavenLocalRepo in mavenLocalRepos.split(':')) {
        if (mavenLocalRepo.isBlank()) {
          mavenLocal()
        } else {
          logger.info("Local repository path: $mavenLocalRepo")

          val repo = File(mavenLocalRepo)
          if (!repo.exists() || !repo.isDirectory) {
            throw FileNotFoundException("Maven local repository '$mavenLocalRepo' not found")
          }

          maven { repository ->
            repository.url = repo.toURI()
          }
        }
      }
    }

    // for AGP API dependency
    google()

    mavenCentral()
    gradlePluginPortal()
  }
}