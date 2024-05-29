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

package com.itsaky.androidide.templates.base.modules.android

import org.eclipse.lemminx.dom.builder.IndentedXmlConfigurator

/**
 * Model for activities declared in [AndroidManifestBuilder].
 *
 * @param name The name of the activity (for `android:name` attribute).
 * @param isExported Sets the `android:exported` attribute.
 * @param isLauncher Adds the `intent-filter` for setting this activity as the launcher activity.
 * @param configureAttrs Function for configuring additional attributes for this activity.
 * @param configureInside Function for configuring additional elements inside this activity tag.
 * @author Akash Yadav
 */
data class ManifestActivity(val name: String, val isExported: Boolean,
                            val isLauncher: Boolean = false,
                            val configureAttrs: IndentedXmlConfigurator? = null,
                            val configureInside: IndentedXmlConfigurator? = null
)

/**
 * Model for icon values in [AndroidManifestBuilder].
 *
 * @author Akash Yadav
 */
data class ManifestIcon(val name: String, val type: String) {

  fun value(): String = "@${type}/${name}"
}