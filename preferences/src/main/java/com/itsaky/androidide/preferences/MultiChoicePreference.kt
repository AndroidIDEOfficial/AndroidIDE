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
 * A preference with multiple choices to select from.
 *
 * The [onSelectionChanged] is called whenever the
 * [DialogInterface.OnMultiChoiceClickListener][android.content.DialogInterface.OnMultiChoiceClickListener]
 * is called.
 *
 * @author Akash Yadav
 */
abstract class MultiChoicePreference : ChoiceBasedDialogPreference(), PreferenceChoices {

  override fun onConfigureDialogChoices(
    preference: Preference,
    dialog: MaterialAlertDialogBuilder,
    entries: Array<PreferenceChoices.Entry>,
    selections: BooleanArray
  ) {

    val labels = Array(entries.size) { entries[it].label }

    dialog.setMultiChoiceItems(
      labels,
      selections
    ) { _, which, checked ->
      onSelectionChanged(preference, entries[which], which, checked)
    }
  }
}
