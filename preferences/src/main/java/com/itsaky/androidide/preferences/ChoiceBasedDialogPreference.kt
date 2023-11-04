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

  protected open var selectedPositions: MutableList<Int>? = null

  final override fun onConfigureDialog(preference: Preference, dialog: MaterialAlertDialogBuilder) {
    val choices = getChoices(preference.context)
    selectedPositions = MutableList(size = choices.size) { -1 }

    onConfigureDialogChoices(preference, dialog, choices)

    dialog.setPositiveButton(android.R.string.ok) { dialogInterface, _ ->
      dialogInterface.dismiss()

      val positions = selectedPositions?.also { positions ->
        positions.removeIf { idx -> idx == -1 }
      }
      onChoicesConfirmed(positions ?: emptyList())
    }

    dialog.setNegativeButton(android.R.string.cancel) { dialogInterface, _ ->
      dialogInterface.dismiss()
      onChoicesCancelled()
    }
  }

  override fun onSelectionChanged(position: Int, isSelected: Boolean) {
    if (isSelected) {
      selectedPositions!!.add(position)
    } else {
      selectedPositions!!.removeAt(position)
    }
  }

  /**
   * Configure the dialog choices.
   */
  protected abstract fun onConfigureDialogChoices(
    preference: Preference,
    dialog: MaterialAlertDialogBuilder,
    choices: Array<String>
  )

  override fun onChoicesConfirmed(selectedPositions: List<Int>) {}

  override fun onChoicesCancelled() {}
}