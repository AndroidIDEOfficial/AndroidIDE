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

import java.io.Serializable

/**
 * A project dependency of an [JavaModule]. More details about the project can be fetched by
 * querying the [projectPath] in the root project.
 *
 * @author Akash Yadav
 */
class JavaModuleProjectDependency(
  /** The name of the module project. */
  val moduleName: String,

  /** The path of the project. */
  val projectPath: String,

  /** Scope of this dependency. */
  scope: String,

  /** Whether the dependency is exported. */
  exported: Boolean
) : JavaModuleDependency(null, scope, exported), Serializable
