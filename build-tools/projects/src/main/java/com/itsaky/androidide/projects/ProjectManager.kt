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
import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent
import com.itsaky.androidide.eventbus.events.editor.DocumentCloseEvent
import com.itsaky.androidide.eventbus.events.editor.DocumentOpenEvent
import com.itsaky.androidide.eventbus.events.project.ProjectInitializedEvent
import com.itsaky.androidide.progress.ProcessCancelledException
import com.itsaky.androidide.progress.ProgressManager.Companion.abortIfCancelled
import com.itsaky.androidide.projects.api.AndroidModule
import com.itsaky.androidide.projects.api.ModuleProject
import com.itsaky.androidide.projects.api.Project
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.projects.models.ActiveDocument
import com.itsaky.androidide.projects.util.ProjectTransformer
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.utils.BootClasspathProvider
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.ILogger
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.StringReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import java.util.*
import java.util.concurrent.*
import org.apache.commons.io.FileUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.BACKGROUND

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
      this.rootProject!!.subModules.filterIsInstance(ModuleProject::class.java).forEach {
        it.indexSourcesAndClasspaths()
      }
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

  fun isDocumentActive(file: Path): Boolean {
    if (!checkInit()) {
      return false
    }

    for (module in this.rootProject!!.subModules) {
      if (module !is ModuleProject) {
        continue
      }

      if (module.isActive(file)) {
        return true
      }
    }

    return false
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

  fun getActiveDocument(file: Path): ActiveDocument? {
    if (!checkInit()) {
      return null
    }

    for (module in this.rootProject!!.subModules) {
      if (module !is ModuleProject) {
        continue
      }

      val document = module.getActiveDocument(file)
      if (document != null) {
        return document
      }
    }

    return null
  }

  fun getLastModified(file: Path): Instant {
    if (!checkInit()) {
      return getLastModifiedFromDisk(file)
    }

    val document = getActiveDocument(file)
    if (document != null) {
      return document.modified
    }

    return getLastModifiedFromDisk(file)
  }

  fun getDocumentContents(file: Path): String {
    if (!checkInit()) {
      return getFileContents(file)
    }

    val document = getActiveDocument(file)
    if (document != null) {
      return document.content
    }

    return getFileContents(file)
  }

  fun getReader(file: Path): BufferedReader {
    if (!checkInit()) {
      return createFileReader(file)
    }

    val document = getActiveDocument(file)
    if (document != null) {
      return BufferedReader(StringReader(document.content))
    }

    return createFileReader(file)
  }

  fun getInputStream(file: Path): InputStream {
    if (!checkInit()) {
      return createFileInputStream(file)
    }

    val document = getActiveDocument(file)
    if (document != null) {
      BufferedInputStream(document.content.byteInputStream())
    }

    return createFileInputStream(file)
  }

  //////////////////////////////////////////////////////////////////////////////////////////
  ////// TODO Subscribe to file creation/deletion/rename events and update source map //////
  //////////////////////////////////////////////////////////////////////////////////////////

  override fun register() {
    super.register()

    // Make sure we list and store the bootstrap classes
    CompletableFuture.runAsync {
      BootClasspathProvider.update(Collections.singleton(Environment.ANDROID_JAR.absolutePath))
    }
  }

  @Subscribe(threadMode = BACKGROUND)
  @Suppress("unused")
  fun onDocumentOpen(event: DocumentOpenEvent) {
    if (!checkInit()) {
      return
    }

    for (subModule in this.rootProject!!.subModules) {
      if (subModule !is ModuleProject) {
        continue
      }

      if (subModule.onDocumentOpen(event)) {
        break
      }
    }
  }

  @Subscribe(threadMode = BACKGROUND)
  @Suppress("unused")
  fun onDocumentClose(event: DocumentCloseEvent) {
    if (!checkInit()) {
      return
    }

    for (subModule in this.rootProject!!.subModules) {
      if (subModule !is ModuleProject) {
        continue
      }

      if (subModule.onDocumentClose(event)) {
        break
      }
    }
  }

  @Subscribe(threadMode = BACKGROUND)
  @Suppress("unused")
  fun onDocumentContentChange(event: DocumentChangeEvent) {
    if (!checkInit()) {
      return
    }

    for (subModule in this.rootProject!!.subModules) {
      if (subModule !is ModuleProject) {
        continue
      }

      if (subModule.onDocumentChanged(event)) {
        break
      }
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

  private fun createFileReader(file: Path): BufferedReader {
    return try {
      Files.newBufferedReader(file)
    } catch (noFile: java.nio.file.NoSuchFileException) {
      log.warn("No such file", noFile)
      "".reader().buffered()
    } catch (cancelled: ProcessCancelledException) {
      log.debug("createFileReader(): cancelled")
      "".reader().buffered()
    }
  }

  private fun createFileInputStream(file: Path): InputStream {
    return try {
      Files.newInputStream(file)
    } catch (noFile: java.nio.file.NoSuchFileException) {
      log.warn("No such file", noFile)
      "".byteInputStream()
    } catch (cancelled: ProcessCancelledException) {
      log.debug("createFileInputStream(): cancelled")
      "".byteInputStream()
    }
  }

  private fun getLastModifiedFromDisk(file: Path): Instant {
    return Files.getLastModifiedTime(file).toInstant()
  }

  private fun getFileContents(file: Path): String {
    return try {
      abortIfCancelled()
      FileUtils.readFileToString(file.toFile(), Charset.defaultCharset())
    } catch (noFile: java.nio.file.NoSuchFileException) {
      log.warn("No such file", noFile)
      ""
    } catch (cancelled: ProcessCancelledException) {
      log.debug("getFileContents(): cancelled")
      ""
    }
  }
}
