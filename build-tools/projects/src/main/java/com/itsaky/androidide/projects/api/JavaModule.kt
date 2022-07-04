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

import com.itsaky.androidide.builder.model.DefaultLibrary
import com.itsaky.androidide.tooling.api.IProject.Type.Java
import com.itsaky.androidide.tooling.api.model.GradleTask
import com.itsaky.androidide.tooling.api.model.JavaContentRoot
import com.itsaky.androidide.tooling.api.model.JavaModuleDependency
import java.io.File

/**
 * A [Project] model implementation for Java library modules which is exposed to other modules and
 * provides additional helper methods.
 *
 * @param name The display name of the project.
 * @param description The project description.
 * @param path The project path (same as Gradle project paths). For example, `:app`,
 * `:module:submodule`, etc. Root project is always represented by path `:`.
 * @param projectDir The project directory.
 * @param buildDir The build directory of the project.
 * @param buildScript The Gradle buildscript file of the project.
 * @param tasks The tasks of the project.
 * @param contentRoots The source roots of this module.
 * @param dependencies The dependencies of this module.
 * @author Akash Yadav
 */
class JavaModule(
  name: String,
  description: String,
  path: String,
  projectDir: File,
  buildDir: File,
  buildScript: File,
  tasks: List<GradleTask>,
  val contentRoots: List<JavaContentRoot>,
  val dependencies: List<JavaModuleDependency>
) : ModuleProject(name, description, path, projectDir, buildDir, buildScript, tasks) {

  init {
    type = Java
  }

  override fun getGeneratedJar(variant: String): File {
    var jar = File(buildDir, "libs/$name.jar")
    if (jar.exists()) {
      return jar
    }

    jar =
      File(buildDir, "libs").listFiles()?.first { it.name.startsWith(this.name) }
        ?: File("module-jar-does-not-exist.jar")

    return jar
  }

  override fun getClassPaths(): MutableSet<File> {
    return dependencies.mapNotNull { it.jarFile }.toMutableSet().apply { add(getGeneratedJar("")) }
  }

  override fun getSourceDirectories(): Set<File> {
    val sources = mutableSetOf<File>()
    contentRoots.forEach {
      sources.addAll(it.sourceDirectories.map { sourceDirectory -> sourceDirectory.directory })
    }
    return sources
  }
  
  override fun getCompileSourceDirectories(): Set<File> {
    TODO("Not yet implemented")
  }
  
  override fun getModuleClasspaths(): Set<File> {
    TODO("Not yet implemented")
  }
  
  override fun getCompileClasspaths(): Set<File> {
    TODO("Not yet implemented")
  }
  
  override fun getCompileModuleProjects(): List<ModuleProject> {
    TODO("Not yet implemented")
  }
}
