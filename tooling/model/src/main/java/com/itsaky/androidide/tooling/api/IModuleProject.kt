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

package com.itsaky.androidide.tooling.api

import java.io.File
import java.util.concurrent.CompletableFuture

/**
 * Common APIs for module projects.
 *
 * @author Akash Yadav
 */
interface IModuleProject : IGradleProject {

  /**
   * Get the classpaths for this module project. The returned list always included the
   * `classes.jar`.
   */
  fun getClasspaths(): CompletableFuture<List<File>>
}