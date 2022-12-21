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

import com.itsaky.androidide.builder.model.IJavaCompilerSettings
import java.io.File

/**
 * A module in a Gradle project.
 *
 * @author Akash Yadav
 */
interface ModuleProject {

  /** Compiler settings for this module. */
  val compilerSettings: IJavaCompilerSettings

  /**
   * Get the generated JAR file for this module.
   *
   * @param variant The build variant name. NOT applicable for [JavaModule].
   */
  fun getGeneratedJar(variant: String): File

  /**
   * Get the classpath of this module. For example, the generated JAR file.
   *
   * @return The class paths.
   */
  fun getClassPaths(): Set<File>
}
