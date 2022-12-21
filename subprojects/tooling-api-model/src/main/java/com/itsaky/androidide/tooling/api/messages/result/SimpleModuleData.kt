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

package com.itsaky.androidide.tooling.api.messages.result

import java.io.File
import java.io.Serializable

/**
 * Container for basic data about module projects.
 *
 * @author Akash Yadav
 */
data class SimpleModuleData(
  val name: String,
  val path: String,
  val projectDir: File,
  val classPaths: Set<File> = emptySet()
) : Serializable {
  private val serialVersionUID = 1L
}
