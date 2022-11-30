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

package com.itsaky.androidide.uidesigner.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import java.io.File

class WorkspaceViewModel : ViewModel() {
  internal val _drawerOpened = MutableLiveData(false)
  internal val _errText = MutableLiveData("")
  internal val _flipperScreen = MutableLiveData(0)
  private val _file = MutableLiveData<File>()
  
  companion object {
    const val FLIPPER_SCREEN_WORKSPACE = 0
    const val FLIPPER_SCREEN_ERROR = 1
  }
  
  var file: File
    get() = _file.value!!
    set(value) {
      _file.value = value
    }
  
  var drawerOpened : Boolean
    get() = this._drawerOpened.value!!
    set(value) {
      this._drawerOpened.value = value
    }
  
  var errText: String
    get() = _errText.value ?: ""
    set(value) {
      _errText.value = value
      flipperScreen = FLIPPER_SCREEN_ERROR
    }
  
  var flipperScreen: Int
    get() = _flipperScreen.value ?: FLIPPER_SCREEN_WORKSPACE
    set(value) {
      _flipperScreen.value = value
    }
}
