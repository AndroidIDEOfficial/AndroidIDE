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
package com.itsaky.androidide.projects

import com.itsaky.androidide.projects.api.AndroidModule
import com.itsaky.androidide.projects.api.Project
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.projects.util.ProjectTransformer
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.utils.ILogger
import java.io.File

/**
 * Manages projects in AndroidIDE.
 *
 * @author Akash Yadav
 */
object ProjectManager {
  private val log = ILogger.newInstance(javaClass.simpleName)
  lateinit var projectPath: String

  var rootProject: Project? = null
  var app: AndroidModule? = null

  var projectUpdateNotificationConsumer: Runnable? = null

  fun setupProject(project: IProject) {
    val caching = CachingProject(project)
    this.rootProject = ProjectTransformer().transform(caching)
    if (this.rootProject != null) {
      this.app = this.rootProject!!.findFirstAndroidAppModule()
    }
  }

  private fun isInitialized() = rootProject != null

  private fun checkInit(): Boolean {
    if (isInitialized()) {
      return true
    }

    log.warn("Project is not initialized yet!")
    return false
  }

  fun getProjectDir(): File {
      return File(getProjectDirPath())
  }

  fun getProjectDirPath(): String {
    return projectPath
  }

  fun generateSources(builder: BuildService?) {
    if (builder == null) {
      log.warn("Cannot generate sources. BuildService is null.")
      return
    }

    if (app == null) {
      log.warn("Cannot run resource and source generation task. No application module found.")
      return
    }

    val debug = app!!.getVariant("debug")
    if (debug == null) {
      log.warn("No debug variant found in application project ${app!!.name}")
      return
    }

    val mainArtifact = debug.mainArtifact
    val genResourcesTask = mainArtifact.resGenTaskName
    val genSourcesTask = mainArtifact.sourceGenTaskName
    val genDataBinding = // If view binding is enabled, generate the view binding classes too
      if (app!!.viewBindingOptions.isEnabled) {
        "dataBindingGenBaseClassesDebug"
      } else {
        ""
      }
    builder
      .executeProjectTasks(app!!.path, genResourcesTask ?: "", genSourcesTask, genDataBinding)
      .whenComplete { result, taskErr ->
        if (taskErr != null || !result.isSuccessful) {
          log.warn(
            "Execution for tasks '$genResourcesTask' and '$genSourcesTask' failed.",
            taskErr ?: ""
          )
        } else {
          notifyProjectUpdate()
        }
      }
  }

  fun getApplicationModule(): AndroidModule? {
    return app
  }

  fun getApplicationResDirectories(): Set<File> {
    return getApplicationModule()?.getResourceDirectories() ?: emptySet()
  }

  fun notifyProjectUpdate() {
    log.debug("Notifying language servers about configuration change...")
    if (app == null) {
      log.warn("Cannot find application module. Skipping configuration update...")
      return
    }

    // TODO Remove this when Events API has been implemented
    if (projectUpdateNotificationConsumer != null) {
      projectUpdateNotificationConsumer!!.run()
    }
  }

  fun findModuleForFile(file: File): Project? {
    if (!checkInit()) {
      return null
    }

    val path = file.canonicalPath
    var longestPath = ""
    var moduleWithLongestPath: Project? = null

    for (module in rootProject!!.subModules) {
      val moduleDir = module.projectDir.canonicalPath
      if (path.startsWith(moduleDir) && longestPath.length < moduleDir.length) {
        longestPath = moduleDir
        moduleWithLongestPath = module
      }
    }

    if (longestPath.isEmpty() || moduleWithLongestPath == null) {
      return null
    }

    return moduleWithLongestPath
  }
}
