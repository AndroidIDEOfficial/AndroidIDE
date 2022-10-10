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

import android.view.LayoutInflater
import androidx.preference.Preference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.itsaky.androidide.preferences.databinding.LayoutDialogTextInputBinding

/**
 * A preference which shows an edittext
 *
 * @author Akash Yadav
 */
abstract class EditTextPreference : DialogPreference() {

  override fun onConfigureDialog(preference: Preference, dialog: MaterialAlertDialogBuilder) {
    super.onConfigureDialog(preference, dialog)
    val binding = LayoutDialogTextInputBinding.inflate(LayoutInflater.from(dialog.context))
    onConfigureTextInput(binding.name)
    dialog.setView(binding.root)
    dialog.setPositiveButton(android.R.string.ok) { iface, _ ->
      iface.dismiss()
      onPreferenceChanged(preference, binding.name.editText?.text?.toString()?.trim())
    }
    dialog.setNegativeButton(android.R.string.cancel) { iface, _ -> iface.dismiss() }
  }

  protected open fun onConfigureTextInput(input: TextInputLayout) {}
}
