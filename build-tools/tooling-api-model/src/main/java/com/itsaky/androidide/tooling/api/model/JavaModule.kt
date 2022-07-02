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

package com.itsaky.androidide.tooling.api.model

import com.itsaky.androidide.tooling.api.IProject.Type
import com.itsaky.androidide.tooling.api.IProject.Type.Java
import java.io.File
import java.io.Serializable
import java.util.concurrent.*

/**
 * A java library model. Modules represented by this model does not apply any of the Android
 * plugins.
 *
 * @author Akash Yadav
 */
open class JavaModule(
  name: String,
  path: String,
  description: String?,
  projectDir: File,
  buildDir: File,
  buildScript: File,
  parent: IdeGradleProject?,
  tasks: List<IdeGradleTask>,

  /** * Source directories of this project. */
  val contentRoots: List<JavaContentRoot>,

  /** Dependencies of this project. */
  private val javaDependencies: List<JavaModuleDependency>
) :
  IdeGradleProject(name, description, path, projectDir, buildDir, buildScript, parent, tasks),
  IdeModule,
  HasDependencies,
  Serializable {

  override fun getDependencies() = javaDependencies

  override fun getType(): CompletableFuture<Type> {
    return CompletableFuture.completedFuture(Java)
  }

  @Deprecated("Use getClasspath() instead.")
  override fun getGeneratedJar(variant: String): File {
    var jar = File(buildDir, "libs/$name.jar")
    if (jar.exists()) {
      return jar
    }

    jar =
      File(buildDir, "libs").listFiles()?.first { it.name.startsWith(this.name) }
        ?: File("i-do-not-exist.jar")

    return jar
  }

  @Suppress("DEPRECATION") override fun getClassPaths(): Set<File> = setOf(getGeneratedJar("debug"))
}
