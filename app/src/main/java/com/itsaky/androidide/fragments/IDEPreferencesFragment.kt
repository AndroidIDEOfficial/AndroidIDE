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
package com.itsaky.androidide.fragments

import android.os.Bundle
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceGroup
import com.itsaky.androidide.preferences.IPreference
import com.itsaky.androidide.preferences.IPreferenceGroup
import com.itsaky.androidide.preferences.IPreferenceScreen

class IDEPreferencesFragment : BasePreferenceFragment() {

  private var children: List<IPreference> = emptyList()

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    super.onCreatePreferences(savedInstanceState, rootKey)
    if (context == null) return

    @Suppress("DEPRECATION")
    this.children = arguments?.getParcelableArrayList(EXTRA_CHILDREN) ?: emptyList()

    preferenceScreen.removeAll()
    addChildren(this.children, preferenceScreen)
  }

  private fun addChildren(children: List<IPreference>, pref: PreferenceGroup) {
    for (child in children) {
      val preference = child.onCreateView(requireContext())
      if (child is IPreferenceScreen) {
        preference.fragment = IDEPreferencesFragment::class.java.name
        preference.extras.putParcelableArrayList(EXTRA_CHILDREN, ArrayList(child.children))
        pref.addPreference(preference)
        continue
      }

      if (child is IPreferenceGroup) {
        pref.addPreference(preference as PreferenceCategory)
        addChildren(child.children, preference)
        continue
      }

      pref.addPreference(preference)
    }
  }

  companion object {
    const val EXTRA_CHILDREN = "idepref_children"
  }
}
