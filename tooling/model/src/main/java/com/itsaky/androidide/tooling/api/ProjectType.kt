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

/**
 * The type of [IGradleProject].
 *
 * @author Akash Yadav
 */
enum class ProjectType {

  /** A simple Gradle project. Only root projects are represented by this type. */
  Gradle,

  /**
   * An Android project. Mostly module projects are of this type. But in some cases, this type can
   * also be applied to a root Gradle project.
   */
  Android,

  /**
   * A Java project. Usually, module projects which are not {@link Type#Android} type are of this
   * type.
   */
  Java,

  /** An unknown project type. */
  Unknown
}