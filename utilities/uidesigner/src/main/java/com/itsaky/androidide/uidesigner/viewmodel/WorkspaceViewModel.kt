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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itsaky.androidide.uidesigner.models.UiAttribute
import com.itsaky.androidide.uidesigner.undo.UndoManager
import java.io.File

internal class WorkspaceViewModel : ViewModel() {
  internal val _drawerOpened = MutableLiveData(false)
  internal val _errText = MutableLiveData("")
  internal val _workspaceScreen = MutableLiveData(SCREEN_WORKSPACE)
  internal val _view = MutableLiveData<com.itsaky.androidide.inflater.IView>(null)
  internal val _selectedAttr = MutableLiveData<UiAttribute>(null)
  internal val _addAttrMode = MutableLiveData(false)
  internal val _undoManager = MutableLiveData(UndoManager())
  internal val _currentDestination = MutableLiveData(0)
  internal val _layoutHasError = MutableLiveData(false)
  private val _file = MutableLiveData<File>()

  companion object {
    const val SCREEN_WORKSPACE = 0
    const val SCREEN_ERROR = 1
  }

  val undoManager: UndoManager
    get() = this._undoManager.value!!

  var file: File
    get() = _file.value!!
    set(value) {
      _file.value = value
    }

  var drawerOpened: Boolean
    get() = this._drawerOpened.value!!
    set(value) {
      this._drawerOpened.value = value
    }

  var errText: String
    get() = _errText.value ?: ""
    set(value) {
      _errText.value = value
      workspaceScreen = SCREEN_ERROR
    }

  var workspaceScreen: Int
    get() = _workspaceScreen.value ?: SCREEN_WORKSPACE
    set(value) {
      _workspaceScreen.value = value
    }

  var view: com.itsaky.androidide.inflater.IView?
    get() = this._view.value
    set(value) {
      this._view.value = value
    }

  var selectedAttr: UiAttribute?
    get() = this._selectedAttr.value
    set(value) {
      this._selectedAttr.value = value
    }

  var addAttrMode: Boolean
    get() = this._addAttrMode.value ?: false
    set(value) {
      this._addAttrMode.value = value
    }

  var currentDestination: Int
    get() = this._currentDestination.value ?: 0
    set(value) {
      this._currentDestination.value = value
    }

  var layoutHasError: Boolean
    get() = _layoutHasError.value ?: false
    set(value) {
      _layoutHasError.value = value
    }

  fun notifyAttrUpdated() {
    val attr = this.selectedAttr ?: return
    val view = this.view ?: return

    if (addAttrMode && attr.value.isNotBlank()) {
      // if we were adding a new attribute, then add the attribute to the view.
      view.addAttribute(attr)
      addAttrMode = false
      selectedAttr = null
      return
    }

    val existing = view.findAttribute(attr.name, attr.namespace?.uri)
    if (existing !is UiAttribute) {
      return
    }

    if (existing.value.isBlank()) {
      // the user left the value field blank, remove the attribute
      view.removeAttribute(existing)
      selectedAttr = null
      return
    }

    if (existing.value == attr.value) {
      // value of the attribute is same as before
      selectedAttr = null
      return
    }

    // the user was updating the value af the attribute, so update it in the view
    view.updateAttribute(existing.copyAttr(view = view, value = attr.value))
    this.selectedAttr = null
  }
}
