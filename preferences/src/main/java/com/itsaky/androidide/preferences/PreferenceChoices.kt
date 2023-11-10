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
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * A preference with choices.
 *
 * @author Akash Yadav
 */
interface PreferenceChoices {

  /** Get the choices to show in the preference. */
  fun getChoices(context: Context): Array<String>

  /**
   * Get all checked and unchecked items.
   *
   * @see MaterialAlertDialogBuilder.setMultiChoiceItems
   */
  fun getCheckedItems(choices: Array<String>): BooleanArray?

  /**
   * Called when an item is selected from the single choice list.
   *
   * @param position The position of the selected item.
   * @param isSelected Whether the item is selected.
   */
  fun onSelectionChanged(position: Int, isSelected: Boolean)

  /**
   * Called when the user confirms the selections.
   *
   * @param selectedPositions The positions of the selected items.
   */
  fun onChoicesConfirmed(selectedPositions: IntArray, selections: Map<String, Boolean>)

  /**
   * Called when the user cancels the selections.
   */
  fun onChoicesCancelled()
}
