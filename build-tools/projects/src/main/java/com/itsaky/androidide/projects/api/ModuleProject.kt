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

import com.itsaky.androidide.eventbus.events.EventReceiver
import com.itsaky.androidide.projects.util.ClassTrie
import com.itsaky.androidide.tooling.api.model.GradleTask
import com.itsaky.androidide.utils.ILogger
import java.io.File
import java.util.zip.*

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
  tasks: List<GradleTask>
) :
  Project(name, description, path, projectDir, buildDir, buildScript, tasks),
  com.itsaky.androidide.tooling.api.model.ModuleProject,
  EventReceiver {

  private val log = ILogger.newInstance(javaClass.simpleName)

  companion object {
    const val PROP_USAGE = "org.gradle.usage"
    const val USAGE_API = "java-api"
    const val USAGE_RUNTIME = "java-runtime"
  }

  val compileSourceClasses = ClassTrie()
  val compileClasspathClasses = ClassTrie()

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

  /** Finds the source files from source directories and indexes them. */
  fun indexSourcesAndClasspaths() {
    getCompileSourceDirectories().forEach {
      val sourceDir = it
      val sourceDirLength = sourceDir.absolutePath.length
      it
        .walk()
        .filter { file -> file.isFile && file.exists() }
        .forEach { file ->
          // TODO Multiple classes can be defined in a single file
          //  Also, their package names might be different
          //  We need to handle these cases as well
          val parentPath = file.parentFile!!.absolutePath
          val path = parentPath.substring(sourceDirLength + 1) + "/" + file.nameWithoutExtension
          val fullyQualifiedName = path.replace('/', '.')
          this.compileSourceClasses.append(fullyQualifiedName)
        }
    }
    getCompileClasspaths()
      .filter { it.exists() }
      .forEach { classpath ->
        ZipFile(classpath).use { zip ->
          for (entry in zip.entries()) {
            if (!entry.name.endsWith(".class")) {
              continue
            }

            val name = entry.name.substringBeforeLast('.').replace('/', '.').replace('$', '.')
            if (!compileClasspathClasses.contains(name)) {
              compileClasspathClasses.append(name)
            } else {
              log.warn("Duplicate class '$name'")
            }
          }
        }
      }
  }
}
