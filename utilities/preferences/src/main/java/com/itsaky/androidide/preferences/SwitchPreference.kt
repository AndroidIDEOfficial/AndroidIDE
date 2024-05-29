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
import androidx.preference.Preference
import kotlin.reflect.KMutableProperty0

/**
 * A switch preference.
 *
 * @author Akash Yadav
 */
abstract class SwitchPreference
@JvmOverloads
constructor(val setValue: ((Boolean) -> Unit)? = null, val getValue: (() -> Boolean)? = null) :
  BasePreference() {

    constructor(property: KMutableProperty0<Boolean>) : this(property::set, property::get)

  override fun onCreatePreference(context: Context): Preference {
    val pref = androidx.preference.SwitchPreference(context)
    pref.isChecked = prefValue()
    return pref
  }
  
  override fun onPreferenceChanged(preference: Preference, newValue: Any?): Boolean {
    setValue?.let { it(newValue as Boolean? ?: prefValue()) }
    return true
  }
  
  private fun prefValue(): Boolean {
    return getValue?.let { it() } ?: false
  }
}
