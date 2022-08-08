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
import com.itsaky.androidide.builder.model.IJavaCompilerSettings
import com.itsaky.androidide.projects.classpath.JarFsClasspathReader
import com.itsaky.androidide.projects.classpath.ZipFileClasspathReader
import com.itsaky.androidide.tooling.api.model.GradleTask
import com.itsaky.androidide.projects.util.BootClasspathProvider
import com.itsaky.androidide.utils.DocumentUtils
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.SourceClassTrie.SourceNode
import com.itsaky.androidide.utils.StopWatch
import java.io.File
import java.nio.file.Path

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
  override val compilerSettings: IJavaCompilerSettings
) :
  Project(name, description, path, projectDir, buildDir, buildScript, tasks),
  com.itsaky.androidide.tooling.api.model.ModuleProject {

  private val log = ILogger.newInstance(javaClass.simpleName)

  companion object {
    const val PROP_USAGE = "org.gradle.usage"
    const val USAGE_API = "java-api"
    const val USAGE_RUNTIME = "java-runtime"
  }

  @JvmField val compileJavaSourceClasses = com.itsaky.androidide.utils.SourceClassTrie()
  @JvmField val compileClasspathClasses = com.itsaky.androidide.utils.ClassTrie()

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

  /** Finds the source files and classes from source directories and classpaths and indexes them. */
  @Suppress("UnstableApiUsage")
  fun indexSourcesAndClasspaths() {
    log.info("Indexing sources and classpaths for project:", path)

    var watch = StopWatch("Indexing sources")
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

    watch.log()
    log.debug("Found $count source files.")

    watch = StopWatch("Indexing classpaths")
    val paths = getCompileClasspaths().filter { it.exists() }
    val topLevelClasses = JarFsClasspathReader().listClasses(paths).filter { it.isTopLevel }
    topLevelClasses.forEach { this.compileClasspathClasses.append(it.name) }

    watch.log()
    log.debug("Found ${topLevelClasses.size} classpaths.")

    if (this is AndroidModule) {
      BootClasspathProvider.update(bootClassPaths.map { it.path })
    }
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

  open fun isFromThisModule(file: Path): Boolean {
    // TODO This can be probably improved
    return file.startsWith(this.projectDir.toPath())
  }

  override fun toString() = "${javaClass.simpleName}: ${this.path}"
}
