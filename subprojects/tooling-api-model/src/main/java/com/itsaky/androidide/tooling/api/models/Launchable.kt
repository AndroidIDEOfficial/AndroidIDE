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
 * Model containing information about anything which can be launched by the users (e.g. Gradle tasks).
 *
 * @property displayName The display name of the launchable.
 * @property isPublic Whether the launchable is publicly accessible.
 * @author Akash Yadav
 */
open class Launchable(val displayName: String?, val isPublic: Boolean) : Serializable {

  protected val gsonType: String = javaClass.name
  private val serialVersionUID = 1L
}