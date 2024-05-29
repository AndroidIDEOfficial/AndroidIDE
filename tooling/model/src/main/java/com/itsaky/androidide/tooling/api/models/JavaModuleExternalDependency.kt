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

import java.io.File
import java.io.Serializable

/**
 * An external dependency of an [JavaModule].
 *
 * @author Akash Yadav
 */
class JavaModuleExternalDependency(
  jar: File,

  /** `sources.jar` for this dependency. */
  val sources: File? = null,

  /** `javadoc.jar` for this dependency. */
  val javadoc: File? = null,

  /** The Gradle dependency artifact for this dependency. */
  val gradleArtifact: GradleArtifact?,

  /** Scope of this dependency. */
  scope: String,

  /** Whether the dependency is exported. */
  exported: Boolean
) : JavaModuleDependency(jar, scope, exported), Serializable
