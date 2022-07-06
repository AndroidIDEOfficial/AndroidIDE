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

package com.itsaky.androidide.projects.api

import android.text.TextUtils
import com.google.common.reflect.ClassPath
import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent
import com.itsaky.androidide.eventbus.events.editor.DocumentCloseEvent
import com.itsaky.androidide.eventbus.events.editor.DocumentOpenEvent
import com.itsaky.androidide.models.Range
import com.itsaky.androidide.projects.models.ActiveDocument
import com.itsaky.androidide.projects.util.ClassTrie
import com.itsaky.androidide.projects.util.DocumentUtils
import com.itsaky.androidide.projects.util.SourceClassTrie
import com.itsaky.androidide.projects.util.SourceClassTrie.SourceNode
import com.itsaky.androidide.tooling.api.model.GradleTask
import com.itsaky.androidide.utils.ILogger
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Path
import java.util.concurrent.*

/**
 * A module project. Base class for [AndroidModule] and [JavaModule].
 *
 * @author Akash Yadav
 */
abstract class ModuleProject(
  name: String,
  description: String,
  path: String,
  projectDir: File,
  buildDir: File,
  buildScript: File,
  tasks: List<GradleTask>,
) :
  Project(name, description, path, projectDir, buildDir, buildScript, tasks),
  com.itsaky.androidide.tooling.api.model.ModuleProject {

  private val log = ILogger.newInstance(javaClass.simpleName)

  companion object {
    const val PROP_USAGE = "org.gradle.usage"
    const val USAGE_API = "java-api"
    const val USAGE_RUNTIME = "java-runtime"
  }

  @JvmField val compileJavaSourceClasses = SourceClassTrie()
  @JvmField val compileClasspathClasses = ClassTrie()

  /**
   * Map of documents that are open in the editor. Keys here are the canonical paths of the
   * documents.
   */
  val activeDocuments: MutableMap<Path, ActiveDocument> = ConcurrentHashMap<Path, ActiveDocument>()

  /**
   * Get the source directories of this module (non-transitive i.e for this module only).
   *
   * @return The source directories.
   */
  abstract fun getSourceDirectories(): Set<File>

  /**
   * Get the source directories with compile scope. This must include source directories of
   * transitive project dependencies and this module.
   *
   * @return The source directories.
   */
  abstract fun getCompileSourceDirectories(): Set<File>

  /**
   * Get the JAR files for this module. This does not include JAR files of any dependencies.
   *
   * @return The classpaths of this project.
   */
  abstract fun getModuleClasspaths(): Set<File>

  /**
   * Get the classpaths with compile scope. This must include classpaths of transitive project
   * dependencies as well. This includes classpaths for this module as well.
   *
   * @return The source directories.
   */
  abstract fun getCompileClasspaths(): Set<File>

  /**
   * Get the list of module projects with compile scope. This includes transitive module projects as
   * well.
   */
  abstract fun getCompileModuleProjects(): List<ModuleProject>

  /**
   * Called by [com.itsaky.androidide.projects.ProjectManager] when a document is opened.
   *
   * @param event The event descriptor.
   * @return `true` if this module consumed the event. `false` otherwise.
   */
  fun onDocumentOpen(event: DocumentOpenEvent): Boolean {
    if (!isCacheable(event.openedFile) || !isFromThisModule(event.openedFile)) {
      return false
    }

    activeDocuments[event.openedFile.normalize()] = createDocument(event)
    return true
  }

  /**
   * Called by [com.itsaky.androidide.projects.ProjectManager] when a document is changed.
   *
   * @param event The event descriptor.
   * @return `true` if this module consumed the event. `false` otherwise.
   */
  fun onDocumentChanged(event: DocumentChangeEvent): Boolean {
    if (!isCacheable(event.changedFile) || !isFromThisModule(event.changedFile)) {
      return false
    }

    activeDocuments[event.changedFile.normalize()] = createDocument(event)
    return true
  }

  /**
   * Called by [com.itsaky.androidide.projects.ProjectManager] when a document is closed.
   *
   * @param event The event descriptor.
   * @return `true` if this module consumed the event. `false` otherwise.
   */
  fun onDocumentClose(event: DocumentCloseEvent): Boolean {
    if (!isCacheable(event.closedFile) || !isFromThisModule(event.closedFile)) {
      return false
    }

    activeDocuments.remove(event.closedFile.normalize())
    return true
  }

  fun isActive(file: Path): Boolean {
    return this.activeDocuments.containsKey(file.normalize())
  }

  fun getActiveDocument(file: Path): ActiveDocument? {
    return this.activeDocuments[file.normalize()]
  }

  /** Finds the source files and classes from source directories and classpaths and indexes them. */
  fun indexSourcesAndClasspaths() {
    log.info("Indexing sources and classpaths for project:", path)

    var count = 0
    getCompileSourceDirectories().forEach {
      val sourceDir = it.toPath()
      it
        .walk()
        .filter { file -> file.isFile && file.exists() && DocumentUtils.isJavaFile(file.toPath()) }
        .map { file -> file.toPath() }
        .forEach { file ->
          this.compileJavaSourceClasses.append(file, sourceDir)
          count++
        }
    }
    
    log.debug("Sources indexed for project: '$path'. Found $count source files.")
    count = 0
    
    val urls =
      getCompileClasspaths().filter { it.exists() }.map { toUrl(it.toPath()) }.toTypedArray()
    val loader = URLClassLoader(urls, null)
    val scanner: ClassPath
    try {
      scanner = ClassPath.from(loader)
    } catch (e: IOException) {
      log.warn("Unable to read classpaths for project:", path)
      throw RuntimeException(e)
    }
    
    log.debug("Classpaths indexed for project:", path)
    scanner.topLevelClasses.forEach {
      this.compileClasspathClasses.append(it.name)
      count++
    }

    log.debug("Classpaths indexed for project: '$path'. Found $count classpaths.")
  }

  fun getSourceFilesInDir(dir: Path): List<SourceNode> =
    this.compileJavaSourceClasses.getSourceFilesInDir(dir)

  fun packageNameOrEmpty(file: Path?): String {
    if (file == null) {
      return ""
    }

    val source = this.compileJavaSourceClasses.findSource(file)
    if (source != null) {
      return source.packageName
    }

    return ""
  }

  fun suggestPackageName(file: Path): String {
    var dir = file.parent.normalize()
    while (dir != null) {
      for (sibling in getSourceFilesInDir(dir)) {
        if (DocumentUtils.isSameFile(sibling.file, file)) {
          continue
        }
        var packageName: String = packageNameOrEmpty(sibling.file)
        if (TextUtils.isEmpty(packageName.trim { it <= ' ' })) {
          continue
        }
        val relativePath = dir.relativize(file.parent)
        val relativePackage = relativePath.toString().replace(File.separatorChar, '.')
        if (relativePackage.isNotEmpty()) {
          packageName = "$packageName.$relativePackage"
        }
        return packageName
      }
      dir = dir.parent.normalize()
    }
    return ""
  }

  fun listClassesFromSourceDirs(packageName: String): List<SourceNode> {
    return compileJavaSourceClasses
      .findInPackage(packageName)
      .filterIsInstance(SourceNode::class.java)
  }

  protected open fun isCacheable(file: Path): Boolean {
    // For now, we only cache Java files
    return DocumentUtils.isJavaFile(file)
  }

  protected open fun isFromThisModule(file: Path): Boolean {
    // TODO This can be probably improved
    return file.startsWith(this.projectDir.toPath())
  }

  protected open fun createDocument(event: DocumentOpenEvent): ActiveDocument {
    return ActiveDocument(
      file = event.openedFile,
      content = event.text,
      changeRange = Range.NONE,
      version = event.version,
      changDelta = 0
    )
  }

  protected open fun createDocument(event: DocumentChangeEvent): ActiveDocument {
    return ActiveDocument(
      file = event.changedFile,
      content = event.newText,
      changeRange = event.changeRange,
      version = event.version,
      changDelta = event.changeDelta
    )
  }

  private fun toUrl(path: Path): URL {
    try {
      return path.toUri().toURL()
    } catch (e: MalformedURLException) {
      throw RuntimeException(e)
    }
  }
}
