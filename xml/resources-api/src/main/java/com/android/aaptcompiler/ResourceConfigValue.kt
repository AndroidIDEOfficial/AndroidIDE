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

package com.android.aaptcompiler

/**
 * Represents a single value for an entry for a given Configuration.
 *
 * @property config The configuration for which this value is defined.
 * @property product The product name for which this value is defined.
 * @property value The actual Value.
 */
data class ResourceConfigValue(
  val config: ConfigDescription,
  val product: String,
  var value: Value? = null
)