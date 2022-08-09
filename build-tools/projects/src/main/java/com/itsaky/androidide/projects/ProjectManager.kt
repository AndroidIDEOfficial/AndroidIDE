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

import com.itsaky.androidide.eventbus.events.EventReceiver
import com.itsaky.androidide.eventbus.events.project.ProjectInitializedEvent
import com.itsaky.androidide.projects.api.AndroidModule
import com.itsaky.androidide.projects.api.ModuleProject
import com.itsaky.androidide.projects.api.Project
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.projects.util.ProjectTransformer
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.utils.ILogger
import java.io.File
import java.nio.file.Path
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.MAIN

/**
 * Manages projects in AndroidIDE.
 *
 * @author Akash Yadav
 */
object ProjectManager : EventReceiver {
  private val log = ILogger.newInstance(javaClass.simpleName)
  lateinit var projectPath: String

  var rootProject: Project? = null
  var app: AndroidModule? = null

  fun setupProject(project: IProject) {
    val caching = CachingProject(project)
    this.rootProject = ProjectTransformer().transform(caching)
    if (this.rootProject != null) {
      this.app = this.rootProject!!.findFirstAndroidAppModule()
      this.rootProject!!
        .subModules
        .filterIsInstance(ModuleProject::class.java)
        .forEach(ModuleProject::indexSourcesAndClasspaths)
    }
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
    val event = ProjectInitializedEvent()
    event.put(Project::class.java, rootProject)
    EventBus.getDefault().post(event)
  }

  fun findModuleForFile(file: File): ModuleProject? {
    if (!checkInit()) {
      return null
    }

    return this.rootProject!!.findModuleForFile(file)
  }

  fun findModuleForFile(file: Path): ModuleProject? {
    return findModuleForFile(file.toFile())
  }

  fun containsSourceFile(file: Path): Boolean {
    if (!checkInit()) {
      return false
    }

    for (module in this.rootProject!!.subModules) {
      if (module !is ModuleProject) {
        continue
      }

      val source = module.compileJavaSourceClasses.findSource(file)
      if (source != null) {
        return true
      }
    }

    return false
  }

  //////////////////////////////////////////////////////////////////////////////////////////
  ////// TODO Subscribe to file creation/deletion/rename events and update source map //////
  //////////////////////////////////////////////////////////////////////////////////////////

  private fun isInitialized() = rootProject != null

  private fun checkInit(): Boolean {
    if (isInitialized()) {
      return true
    }

    log.warn("Project is not initialized yet!")
    return false
  }

  // No-op subscriber method
  @Suppress("unused")
  @Subscribe(threadMode = MAIN)
  fun noOp(obj: Throwable) {
    throw UnsupportedOperationException("No-op")
  }
}
