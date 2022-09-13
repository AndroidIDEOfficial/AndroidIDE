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

package com.itsaky.androidide.lsp.edits

import com.itsaky.androidide.lsp.models.CompletionItem
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.widget.CodeEditor

/**
 * Handles additional edits for [CompletionItem]s.
 *
 * @author Akash Yadav
 */
interface IEditHandler {

  /**
   * Performs the edits on the given [editor].
   * @param editor The editor to perform edits on.
   */
  fun performEdits(item: CompletionItem, editor: CodeEditor, text: Content, line: Int, column: Int, index: Int)
}