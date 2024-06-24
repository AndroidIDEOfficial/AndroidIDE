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

package com.itsaky.androidide.xml.versions

/**
 * A model class to hold the API version information.
 *
 * **Dev Note**: This class must be immutable as the instances of this class are reused for
 * multiple symbols.
 *
 * @property since The API in which the symbol was added.
 * @property deprecatedIn The API in which the symbol was deprecated.
 * @property removedIn The API in which the symbol was removed.
 * @author Akash Yadav
 */
data class ApiVersion(
  val since: Int,
  val deprecatedIn: Int = NONE,
  val removedIn: Int = NONE
) {

  /**
   * Returns `true` if [since] is 1 and [deprecatedIn] and [removedIn] is [ApiVersion.NONE].
   */
  fun isSinceInception(): Boolean =
    since == 1 && deprecatedIn == NONE && removedIn == NONE

  companion object {
    internal const val NONE = 0
  }
}
