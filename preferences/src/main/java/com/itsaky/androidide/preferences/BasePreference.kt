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
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.graphics.PorterDuffColorFilter
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import com.itsaky.androidide.utils.resolveAttr

/**
 * Base class for preferences.
 *
 * @author Akash Yadav
 */
abstract class BasePreference : IPreference() {

  abstract fun onCreatePreference(context: Context): Preference

  override fun onCreateView(context: Context): Preference {
    val pref = onCreatePreference(context)
    pref.key = this.key
    pref.title = context.getString(this.title)
    this.summary?.let { pref.summary = context.getString(it) }

    pref.isIconSpaceReserved = this.icon != null
    this.icon?.let {
      pref.icon =
        ContextCompat.getDrawable(context, it)?.apply {
          colorFilter =
            PorterDuffColorFilter(context.resolveAttr(R.attr.colorOnPrimaryContainer), SRC_ATOP)
        }
    }

    pref.setOnPreferenceClickListener { onPreferenceClick(pref) }
    pref.setOnPreferenceChangeListener(this::onPreferenceChanged)
    return pref
  }

  protected open fun onPreferenceChanged(preference: Preference, newValue: Any?): Boolean {
    return false
  }

  protected open fun onPreferenceClick(preference: Preference): Boolean {
    return false
  }
}
