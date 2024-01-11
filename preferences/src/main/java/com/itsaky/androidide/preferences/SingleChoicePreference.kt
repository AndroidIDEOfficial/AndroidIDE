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
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * A preference which allows selecting a single value from a list of values.
 *
 * The [onSelectionChanged] method is called exactly two times when the user changes the selection, first call for the previously
 * selected item and second call for the newly selected item.
 *
 * The [onChoicesConfirmed] is always called with a singleton list.
 *
 * @author Akash Yadav
 */
abstract class SingleChoicePreference : ChoiceBasedDialogPreference(), PreferenceChoices {

  /**
   * The currently selected item in the dialog.
   */
  protected open var currentSelection: Int = -1

  override fun onConfigureDialogChoices(
    preference: Preference,
    dialog: MaterialAlertDialogBuilder,
    entries: Array<PreferenceChoices.Entry>,
    selections: BooleanArray
  ) {
    currentSelection = entries.indexOfFirst { it.isChecked }

    dialog.setSingleChoiceItems(
      entries.labels,
      currentSelection
    )
    { _, position ->
      if (currentSelection != -1) {
        onSelectionChanged(preference, entries[currentSelection], currentSelection, false)
      }

      currentSelection = position
      onSelectionChanged(preference, entries[currentSelection], position, true)
    }
  }

  override fun onChoicesConfirmed(
    preference: Preference,
    entries: Array<PreferenceChoices.Entry>
  ) {
    if (currentSelection < 0 || currentSelection > entries.lastIndex) {
      onChoiceConfirmed(preference, null, currentSelection)
    } else {
      onChoiceConfirmed(preference, entries[currentSelection], currentSelection)
    }
  }

  protected open fun onChoiceConfirmed(
    preference: Preference,
    entry: PreferenceChoices.Entry?,
    position: Int
  ) {
  }
}
