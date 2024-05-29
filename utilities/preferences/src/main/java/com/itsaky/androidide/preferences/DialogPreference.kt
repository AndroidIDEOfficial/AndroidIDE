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
import com.itsaky.androidide.utils.DialogUtils

/**
 * A preference which shows a dialog when clicked.
 *
 * @author Akash Yadav
 */
abstract class DialogPreference : SimplePreference() {

  open val dialogTitle: Int
    get() = this.title

  open val dialogMessage: Int? = null
  open val dialogCancellable: Boolean = false

  override fun onPreferenceClick(preference: Preference): Boolean {
    val dialog = DialogUtils.newMaterialDialogBuilder(preference.context)
    dialog.setTitle(this.dialogTitle)
    dialogMessage?.let { dialog.setMessage(it) }
    dialog.setCancelable(this.dialogCancellable)
    onConfigureDialog(preference, dialog)
    dialog.show()
    return true
  }

  protected open fun onConfigureDialog(preference: Preference,
    dialog: MaterialAlertDialogBuilder) {
  }
}
