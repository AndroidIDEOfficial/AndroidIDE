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
import org.gradle.api.artifacts.repositories.ArtifactRepository
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

    /**
     * Property that is set in tests to indicate that the plugin is being applied in a test environment.
     *
     * **This is an internal property and should not be manually set by users.**
     */
    internal const val PROPERTY_IS_TEST_ENV = "androidide.plugins.internal.isTestEnv"

    /**
     * Property that is set in tests to provide path to the local maven repository.
     * If this property is empty, `null` or not set at all, the default maven local repository is used.
     *
     * **This is an internal property and should not be manually set by users.**
     */
    internal const val PROPERTY_MAVEN_LOCAL_REPOSITORY = "androidide.plugins.internal.mavenLocalRepository"
  }

  override fun apply(target: Gradle) {
    target.settingsEvaluated { settings ->
      val (isTestEnv, mavenLocalRepo) = settings.startParameter.run {
        val isTestEnv = projectProperties.containsKey(PROPERTY_IS_TEST_ENV)
            && projectProperties[PROPERTY_IS_TEST_ENV].toString().toBoolean()
        val mavenLocalRepo = projectProperties.getOrDefault(PROPERTY_MAVEN_LOCAL_REPOSITORY, "")
        isTestEnv to mavenLocalRepo
      }

      settings.addDependencyRepositories(isTestEnv, mavenLocalRepo)
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
    mavenLocalRepo: String
  ) {

    if (!isMavenLocalEnabled) {

      // For AndroidIDE CI builds
      maven { repository ->
        repository.url = URI.create(
          "https://s01.oss.sonatype.org/content/repositories/snapshots/"
        )
      }
    } else {
      logger.info("Using local maven repository for classpath resolution...")

      if (mavenLocalRepo.isBlank()) {
        mavenLocal(::includeGroupId)
      } else {
        logger.info("Local repository path: $mavenLocalRepo")

        val repo = File(mavenLocalRepo)
        if (!repo.exists() || !repo.isDirectory) {
          throw FileNotFoundException("Maven local repository '$mavenLocalRepo' not found")
        }

        maven { repository ->
          repository.url = repo.toURI()
          includeGroupId(repository)
        }
      }
    }

    // for AGP API dependency
    google()

    mavenCentral()
  }

  @Suppress("UnstableApiUsage")
  private fun includeGroupId(repository: ArtifactRepository) {
    repository.content { contentDescriptor ->
      contentDescriptor.includeGroupAndSubgroups(BuildInfo.MVN_GROUP_ID)
    }
  }
}