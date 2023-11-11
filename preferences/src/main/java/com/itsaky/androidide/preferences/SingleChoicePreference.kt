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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.IndexOutOfBoundsException

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

  /**
   * Get the index of the selected item.
   * @see MaterialAlertDialogBuilder.setSingleChoiceItems
   */
  abstract fun getInitiallySelectionItemPosition(context: Context): Int

  final override fun getCheckedItems(choices: Array<String>): BooleanArray? {
    return null
  }

  override fun onConfigureDialogChoices(
    preference: Preference,
    dialog: MaterialAlertDialogBuilder,
    choices: Array<String>,
    checkedItems: BooleanArray?
  ) {

    this.currentSelection = getInitiallySelectionItemPosition(preference.context)
    if (currentSelection < 0 || currentSelection >= choices.size) {
      throw IndexOutOfBoundsException("Initial selection: $currentSelection, size=${choices.size}")
    }

    dialog.setSingleChoiceItems(
      choices,
      currentSelection
    )
    { _, position ->

      if (currentSelection != -1) {
        onSelectionChanged(currentSelection, false)
      }

      currentSelection = position
      onSelectionChanged(position, true)
    }
  }

  final override fun onChoicesConfirmed(
    selectedPositions: IntArray,
    selections: Map<String, Boolean>
  ) {
    onChoiceConfirmed(currentSelection)
  }

  protected open fun onChoiceConfirmed(position: Int) {}
}
