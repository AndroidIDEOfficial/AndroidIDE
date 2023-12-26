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

import androidx.annotation.CallSuper
import androidx.preference.Preference
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Base class for dialog preferences which allows users to choose from multiple items. Subclasses
 * must call [onSelectionChanged] to notify about selection changes.
 *
 * @author Akash Yadav
 */
abstract class ChoiceBasedDialogPreference : DialogPreference(), PreferenceChoices {

  private var choices = emptyArray<PreferenceChoices.Entry>()

  final override fun onConfigureDialog(preference: Preference, dialog: MaterialAlertDialogBuilder) {
    choices = getEntries(preference)

    val selections = BooleanArray(choices.size) { choices[it].isChecked }
    onConfigureDialogChoices(preference, dialog, choices, selections)

    dialog.setPositiveButton(android.R.string.ok) { dialogInterface, _ ->
      dialogInterface.dismiss()
      onChoicesConfirmed(preference, choices)
    }

    dialog.setNegativeButton(android.R.string.cancel) { dialogInterface, _ ->
      dialogInterface.dismiss()
      onChoicesCancelled(preference)
    }
  }

  @CallSuper
  override fun onSelectionChanged(
    preference: Preference,
    entry: PreferenceChoices.Entry,
    position: Int,
    isSelected: Boolean
  ) {
    entry._isChecked = isSelected
  }

  /**
   * Configure the dialog choices.
   */
  protected abstract fun onConfigureDialogChoices(
    preference: Preference,
    dialog: MaterialAlertDialogBuilder,
    entries: Array<PreferenceChoices.Entry>,
    selections: BooleanArray
  )

  override fun onChoicesConfirmed(preference: Preference, entries: Array<PreferenceChoices.Entry>) {
  }

  override fun onChoicesCancelled(preference: Preference) {}
}