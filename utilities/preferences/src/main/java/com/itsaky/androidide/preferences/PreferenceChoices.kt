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

import androidx.annotation.RestrictTo
import androidx.preference.Preference

/**
 * A preference with choices.
 *
 * @author Akash Yadav
 */
interface PreferenceChoices {

  /**
   * Get the entries for this preference.
   */
  fun getEntries(preference: Preference): Array<Entry>

  /**
   * Called when an item is selected from the single choice list.
   *
   * @param position The position of the selected item.
   * @param isSelected Whether the item is selected.
   */
  fun onSelectionChanged(preference: Preference, entry: Entry, position: Int, isSelected: Boolean)

  /**
   * Called when the user confirms the selections.
   *
   * @param entries The entries.
   */
  fun onChoicesConfirmed(preference: Preference, entries: Array<Entry>)

  /**
   * Called when the user cancels the selections.
   */
  fun onChoicesCancelled(preference: Preference)

  /**
   * Entry in [PreferenceChoices].
   *
   * @property label The label for the entry.
   * @property isChecked Whether the item is checked or not.
   * @property data The data object for the value.
   */
  data class Entry(
    val label: CharSequence,
    @Suppress("PropertyName") internal var _isChecked: Boolean,
    val data: Any,
  ) {

    val isChecked: Boolean
      get() = _isChecked

    companion object {

      @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
      val EMPTY = Entry("", false, 0)
    }
  }
}

/**
 * Map this [PreferenceChoices.Entry] array to entry labels.
 */
internal val Array<PreferenceChoices.Entry>.labels: Array<CharSequence>
  get() = Array(size) { this[it].label }