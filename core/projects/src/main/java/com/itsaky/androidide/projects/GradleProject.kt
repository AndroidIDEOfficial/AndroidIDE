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

import com.itsaky.androidide.tooling.api.ProjectType
import com.itsaky.androidide.tooling.api.models.GradleTask
import java.io.File
import java.util.concurrent.CompletableFuture

/**
 * A Gradle project model which is identical to [IGradleProject][com.itsaky.androidide.tooling.api.IGradleProject]. This project module caches all the data
 * from an [IGradleProject][com.itsaky.androidide.tooling.api.IGradleProject] eliminating the use of [CompletableFuture] s.
 *
 * @param name The display name of the project.
 * @param description The project description.
 * @param path The project path (same as Gradle project paths). For example, `:app`,
 *   `:module:submodule`, etc. Root project is always represented by path `:`.
 * @param projectDir The project directory.
 * @param buildDir The build directory of the project.
 * @param buildScript The Gradle buildscript file of the project.
 * @param tasks The tasks of the project.
 * @author Akash Yadav
 */
open class GradleProject(
  val name: String,
  val description: String,
  val path: String,
  val projectDir: File,
  val buildDir: File,
  val buildScript: File,
  val tasks: List<GradleTask>
) {

  var type: ProjectType = ProjectType.Gradle
    protected set
}
