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

package com.itsaky.androidide.tooling.api.models

import com.itsaky.androidide.tooling.api.ProjectType
import java.io.File

/**
 * Metadata about a project.
 *
 * @property description The project description.
 * @property buildScript The build script file (`build.gradle[.kts]`).
 * @property type The type of the project. See constants in [ProjectType] for more details.
 * @author Akash Yadav
 */
open class ProjectMetadata(
  name: String?,
  path: String,
  projectDir: File,
  buildDir: File,
  val description: String?,
  val buildScript: File,
  val type: ProjectType
) : BasicProjectMetadata(name, path, projectDir, buildDir)