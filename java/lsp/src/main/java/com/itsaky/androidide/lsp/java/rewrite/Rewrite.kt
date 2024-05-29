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
package com.itsaky.androidide.lsp.java.rewrite

import com.itsaky.androidide.lsp.java.compiler.CompilerProvider
import com.itsaky.androidide.lsp.models.CodeActionItem
import com.itsaky.androidide.lsp.models.CodeActionKind
import com.itsaky.androidide.lsp.models.DocumentChange
import com.itsaky.androidide.lsp.models.TextEdit
import java.nio.file.Path

/**
 * A source code rewrite.
 *
 * @author Akash Yadav
 */
abstract class Rewrite {

  /**
   * Converts the edits to code action item.
   *
   * @param compiler The compiler service.
   * @param title The title for the code action.
   * @return The code action item.
   */
  fun asCodeActions(compiler: CompilerProvider, title: String): CodeActionItem? {
    val edits = rewrite(compiler)
    if (edits.isEmpty()) {
      return null
    }

    val changes: MutableList<DocumentChange> = ArrayList(0)
    for (file in edits.keys) {
      val textEdits = edits[file] ?: continue
      val change = DocumentChange()
      change.file = file
      change.edits = textEdits.asList()
      changes.add(change)
    }
    val action = CodeActionItem()
    action.title = title
    action.kind = CodeActionKind.QuickFix
    action.changes = changes
    finalizeCodeAction(action)
    return action
  }

  /**
   * Perform a rewrite across the entire codebase. The given compiler can be used for anything
   * except compiling other files. If you try to compile any file, the current thread will be
   * blocked.
   *
   * @param compiler The compiler.
   */
  abstract fun rewrite(compiler: CompilerProvider): Map<Path, Array<TextEdit>>

  /**
   * Called after the code action is created. Subclasses can implement this to do some finalization
   * tasks on the given code action.
   *
   * @param action The code action.
   */
  protected open fun finalizeCodeAction(action: CodeActionItem) {}

  companion object {

    /** CANCELLED signals that the rewrite couldn't be completed. */
    @JvmField var CANCELLED = emptyMap<Path, Array<TextEdit>>()
  }
}
