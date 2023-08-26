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

/**
 * Information about the build variants of an Android module.
 *
 * @property projectPath The project path of the Android module project.
 * @property buildVariants The build variants of the Android module project.
 * @property selectedVariant The name of the selected build variant.
 * @author Akash Yadav
 */
data class BuildVariantInfo(
  val projectPath: String,
  val buildVariants: List<String>,
  val selectedVariant: String
) {

  companion object {

    /**
     * Creates a new [BuildVariantInfo] object with the given selected variants. All the properties
     * of this [BuildVariantInfo] is copied to the new [BuildVariantInfo] and the [newSelection] is
     * set as the [BuildVariantInfo.selectedVariant].
     */
    @JvmStatic
    fun BuildVariantInfo.withSelection(newSelection: String): BuildVariantInfo {
      require(this.buildVariants.indexOf(newSelection) != -1) {
        "'$newSelection' is not a valid variant name. Available variants: ${this.buildVariants}"
      }
      return BuildVariantInfo(this.projectPath, this.buildVariants, newSelection)
    }
  }
}

/**
 * Maps the values to the selected variant names.
 */
fun Map<String, BuildVariantInfo>.mapToSelectedVariants(): Map<String, String> {
  return mapValues { it.value.selectedVariant }
}
