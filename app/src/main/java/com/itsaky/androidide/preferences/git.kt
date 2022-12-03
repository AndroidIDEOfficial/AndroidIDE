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
import com.google.android.material.textfield.TextInputLayout
import com.itsaky.androidide.resources.R.drawable
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.preferences.internal.SSH_KEY_PATH
import com.itsaky.androidide.preferences.internal.sshKeyPath
import com.itsaky.androidide.preferences.internal.USE_SSH_KEY
import com.itsaky.androidide.preferences.internal.useSshKey
import com.itsaky.androidide.preferences.internal.CREATE_NEW_REPO
import com.itsaky.androidide.preferences.internal.createNewRepo
import kotlinx.parcelize.Parcelize

@Parcelize
class GitPreferences(
  override val key: String = "idepref_git",
  override val title: Int = string.title_git,
  override val summary: Int? = string.idepref_git_summary,
  override val children: List<IPreference> = mutableListOf()
) : IPreferenceScreen() {
  init {
    addPreference(UseSshKey())
    addPreference(SshKeyPath())
    addPreference(CreateNewRepo())
  }
}


@Parcelize
class UseSshKey(
  override val key: String = USE_SSH_KEY,
  override val title: Int = string.title_default_use_ssh,
  override val summary: Int? = string.msg_default_use_ssh,
  override val icon: Int? = drawable.ic_ssh
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val pref = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    pref.isChecked = useSshKey
    return pref
  }

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    useSshKey = newValue as Boolean? ?: useSshKey
    return true
  }
}

@Parcelize
private class SshKeyPath(
  override val key: String = SSH_KEY_PATH,
  override val title: Int = string.idepref_title_ssh_key_path,
  override val summary: Int? = string.idepref_msg_ssh_key_path,
  override val icon: Int? = drawable.ic_key,
) : EditTextPreference() {

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    sshKeyPath = newValue as String? ?: ""
    return true
  }

  override fun onConfigureTextInput(input: TextInputLayout) {
    input.setStartIconDrawable(drawable.ic_key)
    input.setHint(string.msg_ssh_key_path)
    input.helperText = input.context.getString(string.msg_ssh_key_path_input_help)
    input.isCounterEnabled = false
    input.editText!!.setText(sshKeyPath)
  }
}

@Parcelize
class CreateNewRepo(
  override val key: String = CREATE_NEW_REPO,
  override val title: Int = string.title_default_create_new_repo,
  override val summary: Int? = string.msg_default_create_new_repo,
  override val icon: Int? = drawable.ic_repository
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val pref = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    pref.isChecked = createNewRepo
    return pref
  }

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    createNewRepo = newValue as Boolean? ?: createNewRepo
    return true
  }
}
