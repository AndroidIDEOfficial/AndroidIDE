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

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.concurrent.TimeUnit

/**
 * Plugin to manage LogSender in Android applications.
 *
 * @author Akash Yadav
 */
class LogSenderPlugin : Plugin<Project> {

  companion object {
    private const val LOGSENDER_DEPENDENCY_ARTIFACT = "logsender"
  }

  override fun apply(target: Project) {
    ideLog("Applying ${javaClass.simpleName}")

    target.run {

      check(plugins.hasPlugin(APP_PLUGIN)) {
        "${javaClass.simpleName} can only be applied to Android application projects."
      }

      (extensions.getByName(
        "androidComponents") as ApplicationAndroidComponentsExtension).apply {

        finalizeDsl { appExtension ->
          logger.warn("On finalize DSL")

          val debuggableBuilds = hashSetOf<String>()

          appExtension.buildTypes.forEach {
            if (it.isDebuggable) {
              logger.debug("Found debuggable build type : '${it.name}'")
              debuggableBuilds.add(it.name)
            }
          }

          logger.debug(
            "Found ${debuggableBuilds.size} debuggable builds in project '${project.path}': $debuggableBuilds")

          if (debuggableBuilds.isEmpty()) {
            logger.warn("No debuggable builds found in project '${project.path}'")
          }

          onVariants { variant ->
            logger.warn("on variant -> ${variant.name} ${variant.buildType}")
            val buildType = variant.buildType ?: return@onVariants
            if (buildType in debuggableBuilds) {
              logger.info(
                "Adding LogSender dependency ('${LOGSENDER_DEPENDENCY_ARTIFACT}')" +
                    " to variant '${variant.name}' of project '${project.path}'"
              )

              variant.runtimeConfiguration.apply {
                resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
                dependencies.add(project.dependencies.ideDependency(LOGSENDER_DEPENDENCY_ARTIFACT))
              }
            }
          }
        }
      }
    }
  }
}
