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

package com.itsaky.androidide.plugins.tasks.javac

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.copyTo

/**
 * @author Akash Yadav
 */
abstract class JavacExtractClassesTask : DefaultTask() {

  @get:Input
  abstract val classFiles: ListProperty<Pair<String, String>>

  @get:OutputDirectory
  abstract val outputDirectory: DirectoryProperty

  @TaskAction
  fun extractClasses() {
    val modules = Paths.get(URI.create("jrt:/")).resolve("/modules")

    val outDir = outputDirectory.get().asFile

    for ((module, classFile) in classFiles.get()) {
      val classPath = "${classFile}.class"
      val path = modules.resolve(module).resolve(classPath)
      if (!Files.exists(path)) {
        throw GradleException("Class file $classPath does not exist in module $module")
      }

      val target = outDir.toPath().resolve(classPath)
      Files.createDirectories(target.parent)
      path.copyTo(target)
    }
  }
}