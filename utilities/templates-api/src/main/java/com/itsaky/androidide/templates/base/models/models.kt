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

package com.itsaky.androidide.templates.base.models

import com.itsaky.androidide.templates.base.models.DependencyConfiguration.Implementation

/**
 * Creates a [Dependency] object from the given maven coordinates.
 *
 * @param group The Maven group ID.
 * @param artifact The dependency artifact ID.
 * @param version The dependency version.
 * @return The [Dependency] artifact.
 */
fun defaultDependency(group: String, artifact: String, version: String
): Dependency {
  return Dependency(Implementation, group, artifact, version)
}

/**
 * Parse the given maven dependency coordinates to a [Dependency] object.
 *
 * @param coordinates The maven dependency coordinates in the form 'group:artifact:version'.
 * @param configuration The dependency configuration.
 * @return The [Dependency] artifact.s
 */
fun parseDependency(
  coordinates: String, configuration: DependencyConfiguration = Implementation,
  isPlatform: Boolean = false,
): Dependency {
  val split = coordinates.split(':')
  if (isPlatform) {
    require(
      split.size == 3) { "Maven coordinates must be in the form 'group:artifact:version'" }
  } else {
    require(
      split.size in 2..3) { "Maven coordinates must be in the form 'group:artifact[:version]'" }
  }

  val version = if (split.size == 3) split[2] else null
  return Dependency(configuration, split[0], split[1], version)
}