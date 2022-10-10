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

package com.itsaky.androidide.preferences

import androidx.preference.Preference
import kotlinx.parcelize.Parcelize

/**
 * A simple preference which is expected to be clickable only.
 *
 * @author Akash Yadav
 */
@Parcelize
class SimpleClickablePreference
@JvmOverloads
constructor(
  override val key: String,
  override val title: Int,
  override val summary: Int? = null,
  override val icon: Int? = null,
  private val onClick: ((Preference) -> Boolean)? = { false }
) : SimplePreference() {

  override fun onPreferenceClick(preference: Preference): Boolean {
    return onClick?.let { it(preference) } ?: false
  }
}
