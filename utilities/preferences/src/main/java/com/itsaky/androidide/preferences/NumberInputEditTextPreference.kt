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

import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.preference.Preference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.itsaky.androidide.preferences.databinding.LayoutDialogTextInputBinding

/**
 * A preference which shows an edittext
 *
 * @author Akash Yadav
 */
abstract class NumberInputEditTextPreference(
  val hint: Int? = null,
  val setValue: ((Int) -> Unit)? = null,
  val getValue: (() -> Int)? = null
) : DialogPreference() {

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

  protected open fun onConfigureTextInput(input: TextInputLayout) {
    input.startIconDrawable = null
    hint?.let { input.setHint(it) } ?: run { input.hint = "" }
    input.editText?.apply {
      inputType = EditorInfo.TYPE_NUMBER_FLAG_SIGNED
      imeOptions = EditorInfo.IME_ACTION_DONE
      keyListener = DigitsKeyListener.getInstance("0123456789")
      setText(prefValue())
    }
  }

  override fun onPreferenceChanged(preference: Preference, newValue: Any?): Boolean {
    setValue?.let { it((newValue as String?)?.toInt() ?: 0) }
    return true
  }

  private fun prefValue(): String {
    return getValue?.let { it() }?.toString() ?: ""
  }
}
