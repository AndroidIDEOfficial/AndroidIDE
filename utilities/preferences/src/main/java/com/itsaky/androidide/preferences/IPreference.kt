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

import android.content.Context
import android.os.Parcelable
import androidx.preference.Preference
import kotlinx.parcelize.Parcelize

/**
 * A preference shown on the preference screen.
 *
 * @author Akash Yadav
 */
abstract class IPreference : Parcelable {

  /** Icon resource for this preference. */
  open val icon: Int? = null

  /** Key that will be used to store the value of this preference in shared preferences. */
  abstract val key: String

  /** The title of the preference. */
  abstract val title: Int

  /** The summary of the preference. */
  open val summary: Int? = null

  abstract fun onCreateView(context: Context): Preference
}
