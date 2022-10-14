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

import com.itsaky.androidide.builder.model.IJavaCompilerSettings
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.tooling.api.IProject.Type.Java
import com.itsaky.androidide.tooling.api.model.GradleTask
import com.itsaky.androidide.tooling.api.model.JavaContentRoot
import com.itsaky.androidide.tooling.api.model.JavaModuleDependency
import com.itsaky.androidide.tooling.api.model.JavaModuleExternalDependency
import com.itsaky.androidide.tooling.api.model.JavaModuleProjectDependency
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
  compilerSettings: IJavaCompilerSettings,
  val contentRoots: List<JavaContentRoot>,
  val dependencies: List<JavaModuleDependency>,
) :
  ModuleProject(
    name,
    description,
    path,
    projectDir,
    buildDir,
    buildScript,
    tasks,
    compilerSettings
  ) {

  companion object {
    const val SCOPE_COMPILE = "COMPILE"
    const val SCOPE_RUNTIME = "RUNTIME"
  }

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

  override fun getClassPaths(): Set<File> {
    return getModuleClasspaths()
  }

  override fun getSourceDirectories(): Set<File> {
    val sources = mutableSetOf<File>()
    contentRoots.forEach {
      sources.addAll(it.sourceDirectories.map { sourceDirectory -> sourceDirectory.directory })
    }
    return sources
  }

  override fun getCompileSourceDirectories(): Set<File> {
    val dirs = getSourceDirectories().toMutableSet()
    getCompileModuleProjects().forEach { dirs.addAll(it.getSourceDirectories()) }
    return dirs
  }

  override fun getModuleClasspaths(): Set<File> {
    return mutableSetOf(getGeneratedJar(""))
  }

  override fun getCompileClasspaths(): Set<File> {
    val classpaths = getModuleClasspaths().toMutableSet()
    getCompileModuleProjects().forEach { classpaths.addAll(it.getCompileClasspaths()) }
    classpaths.addAll(getDependencyClasspaths())
    return classpaths
  }
  
  override fun getCompileModuleProjects(): List<ModuleProject> {
    val root = ProjectManager.rootProject ?: return emptyList()
    return this.dependencies
      .filterIsInstance(JavaModuleProjectDependency::class.java)
      .filter { it.scope == SCOPE_COMPILE }
      .mapNotNull { root.findByPath(it.projectPath) }
      .filterIsInstance(ModuleProject::class.java)
  }
  
  fun getDependencyClasspaths(): Set<File> {
    return this.dependencies.filterIsInstance<JavaModuleExternalDependency>().mapNotNull { it.jarFile }.toHashSet()
  }
}
