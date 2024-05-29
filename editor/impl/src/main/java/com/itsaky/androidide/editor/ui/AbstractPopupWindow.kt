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

package com.itsaky.androidide.editor.ui

import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.base.EditorPopupWindow
import org.slf4j.LoggerFactory

/**
 * Abstract class for all [IDEEditor] popup windows.
 *
 * @author Akash Yadav
 */
abstract class AbstractPopupWindow(editor: CodeEditor, features: Int) :
  EditorPopupWindow(editor, features) {

  companion object {

    private val log = LoggerFactory.getLogger(AbstractPopupWindow::class.java)
  }

  override fun show() {
    (editor as? IDEEditor)?.ensureWindowsDismissed()
    if (!editor.isAttachedToWindow) {
      log.error("Trying to show popup window '{}' when editor is not attached to window",
        javaClass.name)
      return
    }

    super.show()
  }

  override fun isShowing(): Boolean {
    @Suppress("UNNECESSARY_SAFE_CALL", "USELESS_ELVIS")
    return popup?.isShowing ?: false
  }
}
