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
import java.io.File

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

  private val classes = ClassTrie()

  /**
   * Get the source directories of this module (transitive).
   *
   * @return The source directories.
   */
  abstract fun getSourceDirectories(): Set<File>

  override fun register() {
    super.register()
  }

  /** Finds the source files from source directories and indexes them. */
  fun indexSources() {
    val sourceDirs = getSourceDirectories()
  }

  fun findClass(className: String) {}
}
