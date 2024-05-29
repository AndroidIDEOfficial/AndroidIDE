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

package com.itsaky.androidide.tooling.api.util

/**
 * Compare the given semantic versions and return :
 *
 * - `-1` if [versionA] is less than [versionB]
 * - `0` if [versionA] is equal to [versionB]
 * - `1` if [versionA] is greater than [versionB].
 *
 * @param versionA The first version name.
 * @param versionB The second version name.
 * @return The comparison result.
 */
fun compareSemanticVersions(versionA: String, versionB: String): Int {
  val partsA = versionA.split(".").map { it.toInt() }
  val partsB = versionB.split(".").map { it.toInt() }

  for (i in 0 until minOf(partsA.size, partsB.size)) {
    if (partsA[i] < partsB[i]) {
      return -1
    } else if (partsA[i] > partsB[i]) {
      return 1
    }
  }

  if (partsA.size < partsB.size) {
    return -1
  } else if (partsA.size > partsB.size) {
    return 1
  }

  return 0
}
