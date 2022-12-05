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
package com.itsaky.androidide.ui.editor

import com.itsaky.androidide.lsp.models.DiagnosticItem

/**
 * Popup window used to show diagnostic messages.
 *
 * @author Akash Yadav
 */
class DiagnosticWindow
/**
 * Create a popup window for editor
 *
 * @param editor The editor
 * @see .FEATURE_SCROLL_AS_CONTENT
 *
 * @see .FEATURE_SHOW_OUTSIDE_VIEW_ALLOWED
 *
 * @see .FEATURE_HIDE_WHEN_FAST_SCROLL
 */
(editor: IDEEditor) : BaseEditorWindow(editor) {
  /**
   * Show the given diagnostic item.
   *
   * @param diagnostic The diagnostic item to show.
   */
  fun showDiagnostic(diagnostic: DiagnosticItem?) {
    if (diagnostic == null) {
      if (isShowing) {
        dismiss()
      }
      return
    }
    val message = diagnostic.message
    text.text = message
    displayWindow()
  }
}
