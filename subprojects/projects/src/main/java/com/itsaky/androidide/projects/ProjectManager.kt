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
import com.itsaky.androidide.eventbus.events.editor.DocumentSaveEvent
import com.itsaky.androidide.eventbus.events.file.FileCreationEvent
import com.itsaky.androidide.eventbus.events.file.FileDeletionEvent
import com.itsaky.androidide.eventbus.events.file.FileEvent
import com.itsaky.androidide.eventbus.events.file.FileRenameEvent
import com.itsaky.androidide.eventbus.events.project.ProjectInitializedEvent
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.projects.api.AndroidModule
import com.itsaky.androidide.projects.api.ModuleProject
import com.itsaky.androidide.projects.api.Project
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.projects.util.ProjectTransformer
import com.itsaky.androidide.tasks.executeAsync
import com.itsaky.androidide.tooling.api.IAndroidProject
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.messages.result.InitializeResult
import com.itsaky.androidide.tooling.api.models.BuildVariantInfo
import com.itsaky.androidide.utils.DocumentUtils
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.flashError
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.ASYNC
import org.greenrobot.eventbus.ThreadMode.BACKGROUND
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.isDirectory
import kotlin.io.path.pathString

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

  var projectInitialized: Boolean = false
  var cachedInitResult: InitializeResult? = null

  @JvmOverloads
  fun setupProject(
    project: IProject = Lookup.getDefault().lookup(BuildService.KEY_PROJECT_PROXY)!!,
    isInitialized: Boolean = true
  ) {
    this.rootProject = if (isInitialized) ProjectTransformer().transform(
      CachingProject(project)) else null
    if (this.rootProject != null) {
      this.app = this.rootProject!!.findFirstAndroidAppModule()
      this.rootProject!!.subProjects.filterIsInstance(ModuleProject::class.java).forEach {
        it.indexSourcesAndClasspaths()
        if (it is AndroidModule) {
          it.readResources()
        }
      }
    }
  }

  fun destroy() {
    log.info("Destroying project manager")
    this.rootProject = null
    this.app = null
    this.cachedInitResult = null
    this.projectInitialized = false
  }

  fun getProjectDir(): File {
    return File(getProjectDirPath())
  }

  fun getProjectDirPath(): String {
    return projectPath
  }

  @JvmOverloads
  fun generateSources(
    builder: BuildService? = Lookup.getDefault().lookup(BuildService.KEY_BUILD_SERVICE)
  ) {
    if (builder == null) {
      log.warn("Cannot generate sources. BuildService is null.")
      return
    }

    if (!builder.isToolingServerStarted()) {
      flashError(R.string.msg_tooling_server_unavailable)
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
      .executeProjectTasks(
        app!!.path,
        genResourcesTask ?: "",
        genSourcesTask,
        "processDebugResources",
        genDataBinding
      )
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

  fun notifyProjectUpdate() {

    executeAsync {
      rootProject?.apply {
        subProjects.forEach { subproject ->
          if (subproject is ModuleProject) {
            subproject.indexSources()
          }
        }
      }

      val event = ProjectInitializedEvent()
      event.put(Project::class.java, rootProject)
      EventBus.getDefault().post(event)
    }
  }

  @JvmOverloads
  fun findModuleForFile(file: File, checkExistance: Boolean = false): ModuleProject? {
    if (!checkInit()) {
      return null
    }

    return this.rootProject!!.findModuleForFile(file, checkExistance)
  }

  @JvmOverloads
  fun findModuleForFile(file: Path, checkExistance: Boolean = false): ModuleProject? {
    return findModuleForFile(file.toFile(), checkExistance)
  }

  fun containsSourceFile(file: Path): Boolean {
    if (!checkInit()) {
      return false
    }

    if (!Files.exists(file)) {
      return false
    }

    for (module in this.rootProject!!.subProjects) {
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

  private fun isInitialized() = rootProject != null

  private fun checkInit(): Boolean {
    if (isInitialized()) {
      return true
    }

    log.warn("GradleProject is not initialized yet!")
    return false
  }

  @Suppress("unused")
  @Subscribe(threadMode = ASYNC)
  fun onFileSaved(event: DocumentSaveEvent) {
    event.file.apply {
      if (isDirectory()) {
        return@apply
      }

      if (extension != "xml") {
        return@apply
      }

      val module = findModuleForFile(this) ?: return@apply
      if (module !is AndroidModule) {
        return@apply
      }

      val isResource =
        module.mainSourceSet?.sourceProvider?.resDirectories?.any {
          this.pathString.contains(it.path)
        }
          ?: false

      if (isResource) {
        module.updateResourceTable()
      }
    }
  }

  @Suppress("unused")
  @Subscribe(threadMode = BACKGROUND)
  fun onFileCreated(event: FileCreationEvent) {
    generateSourcesIfNecessary(event)

    if (DocumentUtils.isJavaFile(event.file.toPath())) {
      findModuleForFile(event.file)?.let {
        val sourceRoot = it.findSourceRoot(event.file) ?: return@let

        // add the source node entry
        it.compileJavaSourceClasses.append(event.file.toPath(), sourceRoot)
      }
    }
  }

  @Suppress("unused")
  @Subscribe(threadMode = BACKGROUND)
  fun onFileDeleted(event: FileDeletionEvent) {
    generateSourcesIfNecessary(event)

    // Remove the source node entry
    // Do not check for Java file DocumentUtils.isJavaFile(...) as it checks for file existence as
    // well. As the file is already deleted, it will always return false
    if (event.file.extension == "java") {
      findModuleForFile(event.file)
        ?.compileJavaSourceClasses
        ?.findSource(event.file.toPath())
        ?.let { it.parent?.removeChild(it) }
    }
  }

  @Suppress("unused")
  @Subscribe(threadMode = BACKGROUND)
  fun onFileRenamed(event: FileRenameEvent) {
    generateSourcesIfNecessary(event)

    // Do not check for Java file DocumentUtils.isJavaFile(...) as it checks for file existence as
    // well. As the file is already renamed to another filename, it will always return false
    if (event.file.extension == "java") {
      // remove the source node entry
      findModuleForFile(event.file)
        ?.compileJavaSourceClasses
        ?.findSource(event.file.toPath())
        ?.let { it.parent?.removeChild(it) }
    }

    if (DocumentUtils.isJavaFile(event.newFile.toPath())) {
      findModuleForFile(event.newFile)?.let {
        val sourceRoot = it.findSourceRoot(event.newFile) ?: return@let
        // add the new source node entry
        it.compileJavaSourceClasses.append(event.newFile.toPath(), sourceRoot)
      }
    }
  }

  private fun generateSourcesIfNecessary(event: FileEvent) {
    val builder = Lookup.getDefault().lookup(BuildService.KEY_BUILD_SERVICE) ?: return
    val file = event.file
    if (!isResource(file)) {
      return
    }

    generateSources(builder)
  }

  private fun ModuleProject.findSourceRoot(file: File): Path? {
    return getCompileSourceDirectories().find { file.path.startsWith(it.path) }?.toPath()
  }

  private fun isResource(file: File): Boolean {
    val module = findModuleForFile(file) ?: return false
    if (module is AndroidModule) {
      return module.getResourceDirectories().find { file.path.startsWith(it.path) } != null
    }
    return true
  }
}
