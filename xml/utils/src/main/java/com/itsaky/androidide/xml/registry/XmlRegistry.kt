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

package com.itsaky.androidide.xml.registry

import java.io.File

/**
 * Base interface for all XML registry interfaces.
 *
 * @param T The type of data the registry provides.
 * @author Akash Yadav
 */
interface XmlRegistry<T> {

  /**
   * Whether the actions should be logged.
   */
  var isLoggingEnabled: Boolean

  /**
   * Get data for the given platform directory.
   *
   * @param platform The platform directory.
   */
  fun forPlatformDir(platform: File): T?

  /** Clears all the cached data. */
  fun clear()
}
