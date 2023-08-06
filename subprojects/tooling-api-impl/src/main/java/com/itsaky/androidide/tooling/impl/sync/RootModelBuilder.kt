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

package com.itsaky.androidide.tooling.impl.sync

import com.itsaky.androidide.tooling.api.IAndroidProject
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.impl.Main
import com.itsaky.androidide.tooling.impl.Main.finalizeLauncher
import com.itsaky.androidide.tooling.impl.internal.ProjectImpl
import com.itsaky.androidide.utils.ILogger
import org.gradle.tooling.ConfigurableLauncher
import org.gradle.tooling.model.idea.IdeaProject

/**
 * Utility class to build the project models.
 *
 * @author Akash Yadav
 */
object RootModelBuilder : AbstractModelBuilder<ProjectConnectionAndAndroidVariant, IProject>("") {

  override fun build(param: ProjectConnectionAndAndroidVariant): IProject {
    val (connection, androidVariant) = param
    val executor = connection.action { controller ->
      val ideaProject = controller.getModelAndLog(IdeaProject::class.java)

      val ideaModules = ideaProject.modules
      val modulePaths = mapOf(*ideaModules.map { it.name to it.gradleProject.path }.toTypedArray())
      val rootModule = ideaModules.find { it.gradleProject.path == IProject.ROOT_PROJECT_PATH }
        ?: throw ModelBuilderException(
          "No GradleProject model is associated with project path: '${IProject.ROOT_PROJECT_PATH}'")

      val rootProjectVersions = getAndroidVersions(rootModule, controller)

      val rootProject = if (rootProjectVersions != null) {
        // Root project is an Android project
        assertMinimumAgp(rootProjectVersions)
        AndroidProjectModelBuilder(androidVariant).build(controller to rootModule)
      } else {
        GradleProjectModelBuilder().build(rootModule.gradleProject)
      }

      val projects = ideaModules.map { ideaModule ->
        ModuleProjectModelBuilder(androidVariant).build(
          ModuleProjectModelBuilderParams(controller, ideaProject, ideaModule, modulePaths))
      }

      ProjectImpl(rootProject, projects)
    }

    finalizeLauncher(executor)
    applyAndroidModelBuilderProps(executor)

    val logger = ILogger.newInstance("ProjectReader")
    logger.warn("Starting build. See build output for more details...")

    if (Main.client != null) {
      Main.client.logOutput("Starting build...")
    }

    return executor.run().also {
      logger.debug("Build action executed. Result:", it)
    }
  }

  private fun applyAndroidModelBuilderProps(
    launcher: ConfigurableLauncher<*>) {
    launcher.addProperty(IAndroidProject.PROPERTY_BUILD_MODEL_ONLY, true)
    launcher.addProperty(IAndroidProject.PROPERTY_INVOKED_FROM_IDE, true)
  }

  private fun ConfigurableLauncher<*>.addProperty(property: String, value: Any) {
    addArguments(String.format("-P%s=%s", property, value))
  }
}