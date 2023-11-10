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
 * Base class for dialog preferences which allows users to choose from multiple items.
 *
 * @author Akash Yadav
 */
abstract class ChoiceBasedDialogPreference : DialogPreference(), PreferenceChoices {

  private var checkedPositions = intArrayOf()

  final override fun onConfigureDialog(preference: Preference, dialog: MaterialAlertDialogBuilder) {
    val choices = getChoices(preference.context)
    val checkedItems = getCheckedItems(choices)

    checkedPositions = checkedItems?.map { if(it) 1 else 0 }?.toIntArray() ?: intArrayOf()

    onConfigureDialogChoices(preference, dialog, choices, checkedItems)

    dialog.setPositiveButton(android.R.string.ok) { dialogInterface, _ ->
      dialogInterface.dismiss()

      val selections = HashMap<String, Boolean>(choices.size)
      for (i in 0..checkedPositions.lastIndex) {
        selections[choices[i]] = checkedPositions[i] == 1
      }

      onChoicesConfirmed(checkedPositions, selections)
    }

    dialog.setNegativeButton(android.R.string.cancel) { dialogInterface, _ ->
      dialogInterface.dismiss()
      onChoicesCancelled()
    }
  }

  override fun onSelectionChanged(position: Int, isSelected: Boolean) {
    if (position < 0 || position >= checkedPositions.size) {
      return
    }

    checkedPositions[position] = if (isSelected) 1 else 0
  }

  /**
   * Configure the dialog choices.
   */
  protected abstract fun onConfigureDialogChoices(
    preference: Preference,
    dialog: MaterialAlertDialogBuilder,
    choices: Array<String>,
    checkedItems: BooleanArray?
  )

  override fun onChoicesConfirmed(selectedPositions: IntArray, selections: Map<String, Boolean>) {}

  override fun onChoicesCancelled() {}
}