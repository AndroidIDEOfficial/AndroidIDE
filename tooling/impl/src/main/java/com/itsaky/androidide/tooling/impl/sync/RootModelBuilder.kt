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

import com.itsaky.androidide.builder.model.DefaultProjectSyncIssues
import com.itsaky.androidide.builder.model.DefaultSyncIssue
import com.itsaky.androidide.builder.model.shouldBeIgnored
import com.itsaky.androidide.tooling.api.IAndroidProject
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.messages.InitializeProjectParams
import com.itsaky.androidide.tooling.api.util.AndroidModulePropertyCopier
import com.itsaky.androidide.tooling.impl.Main
import com.itsaky.androidide.tooling.impl.Main.finalizeLauncher
import com.itsaky.androidide.tooling.impl.internal.ProjectImpl
import org.gradle.tooling.ConfigurableLauncher
import org.gradle.tooling.model.idea.IdeaProject
import org.slf4j.LoggerFactory
import java.io.Serializable

/**
 * Utility class to build the project models.
 *
 * @author Akash Yadav
 */
class RootModelBuilder(initializationParams: InitializeProjectParams) :
  AbstractModelBuilder<RootProjectModelBuilderParams, IProject>(initializationParams),
  Serializable {

  private val serialVersionUID = 1L

  override fun build(param: RootProjectModelBuilderParams): IProject {

    val (projectConnection, cancellationToken) = param

    // do not reference the 'initializationParams' field in the
    val initializationParams = initializationParams

    val executor = projectConnection.action { controller ->
      val ideaProject = controller.getModelAndLog(IdeaProject::class.java)

      val ideaModules = ideaProject.modules
      val modulePaths = mapOf(*ideaModules.map { it.name to it.gradleProject.path }.toTypedArray())
      val rootModule = ideaModules.find { it.gradleProject.parent == null }
        ?: throw ModelBuilderException(
          "Unable to find root project")

      val rootProjectVersions = getAndroidVersions(rootModule, controller)

      val syncIssues = hashSetOf<DefaultSyncIssue>()
      val syncIssueReporter = ISyncIssueReporter {
        if (it.shouldBeIgnored()) {
          // this SyncIssue should not be shown to the user
          return@ISyncIssueReporter
        }

        val issue = it as? DefaultSyncIssue ?: AndroidModulePropertyCopier.copy(it)
        syncIssues.add(issue)
      }

      val rootProject = if (rootProjectVersions != null) {
        // Root project is an Android project
        checkAgpVersion(rootProjectVersions, syncIssueReporter)
        AndroidProjectModelBuilder(initializationParams)
          .build(AndroidProjectModelBuilderParams(
            controller,
            rootModule,
            rootProjectVersions,
            syncIssueReporter
          ))
      } else {
        GradleProjectModelBuilder(initializationParams).build(rootModule.gradleProject)
      }

      val projects = ideaModules.map { ideaModule ->
        ModuleProjectModelBuilder(initializationParams).build(
          ModuleProjectModelBuilderParams(
            controller,
            ideaProject,
            ideaModule,
            modulePaths,
            syncIssueReporter
          ))
      }

      return@action ProjectImpl(
        rootProject,
        rootModule.gradleProject.path,
        projects,
        DefaultProjectSyncIssues(syncIssues)
      )
    }

    finalizeLauncher(executor)
    applyAndroidModelBuilderProps(executor)

    if (cancellationToken != null) {
      executor.withCancellationToken(cancellationToken)
    }

    val logger = LoggerFactory.getLogger("RootModelBuilder")
    logger.warn("Starting build. See build output for more details...")

    if (Main.client != null) {
      Main.client.logOutput("Starting build...")
    }

    return executor.run().also {
      logger.debug("Build action executed. Result: {}", it)
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