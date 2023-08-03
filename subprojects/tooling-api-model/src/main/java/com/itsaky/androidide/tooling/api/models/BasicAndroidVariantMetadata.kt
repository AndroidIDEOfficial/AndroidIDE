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
 * Basic metadata about a variant in an Android project.
 *
 * @property name The name of the variant.
 * @property mainArtifact Metadata about the main artifact of this variant.
 * @author Akash Yadav
 */
open class BasicAndroidVariantMetadata(val name: String, val mainArtifact: AndroidArtifactMetadata) : Serializable {
  protected val gsonType: String = javaClass.name
  private val serialVersionUID = 1L
}