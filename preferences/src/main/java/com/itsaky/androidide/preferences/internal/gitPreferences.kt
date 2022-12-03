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

package com.itsaky.androidide.preferences.internal

const val USE_SSH_KEY = "idepref_use_git_ssh"
const val SSH_KEY_PATH = "idepref_ssh_key_path"
const val CREATE_NEW_REPO = "idepref_create_new_repo"

var useSshKey: Boolean
  get() = prefManager.getBoolean(USE_SSH_KEY, false)
  set(value) {
    prefManager.putBoolean(USE_SSH_KEY, value)
  }

var sshKeyPath: String
  get() = prefManager.getString(SSH_KEY_PATH, "")
  set(value) {
    prefManager.putString(SSH_KEY_PATH, value)
  }

var create NewRepo: Boolean
  get() = prefManager.getBoolean(CREATE_NEW_REPO, false)
  set(value) {
    prefManager.putBoolean(CREATE_NEW_REPO, value)
  }
