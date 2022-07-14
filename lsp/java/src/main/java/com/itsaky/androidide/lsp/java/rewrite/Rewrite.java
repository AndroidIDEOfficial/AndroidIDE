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

package com.itsaky.androidide.lsp.java.rewrite;

import androidx.annotation.NonNull;

import com.itsaky.androidide.lsp.java.compiler.CompilerProvider;
import com.itsaky.androidide.lsp.models.CodeActionItem;
import com.itsaky.androidide.lsp.models.CodeActionKind;
import com.itsaky.androidide.lsp.models.DocumentChange;
import com.itsaky.androidide.lsp.models.TextEdit;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Akash Yadav
 */
public abstract class Rewrite {

  /** CANCELLED signals that the rewrite couldn't be completed. */
  public static Map<Path, TextEdit[]> CANCELLED = Collections.emptyMap();

  public CodeActionItem asCodeActions(CompilerProvider compiler, String title) {
    final Map<Path, TextEdit[]> edits = rewrite(compiler);
    if (edits == null || edits.isEmpty()) {
      return null;
    }

    final List<DocumentChange> changes = new ArrayList<>(0);

    for (Path key : edits.keySet()) {
      TextEdit[] textEdits = edits.get(key);
      if (textEdits == null) {
        continue;
      }
      final DocumentChange change = new DocumentChange();
      change.setFile(key);
      change.setEdits(Arrays.asList(textEdits));
      changes.add(change);
    }

    final CodeActionItem action = new CodeActionItem();
    action.setTitle(title);
    action.setKind(CodeActionKind.QuickFix);
    action.setChanges(changes);
    applyCommands(action);
    return action;
  }

  /**
   * Perform a rewrite across the entire codebase. The given compiler can be used for anything
   * except compiling other files. If you try to compile any file, the current thread will be
   * blocked.
   *
   * @param compiler The compiler.
   */
  public abstract Map<Path, TextEdit[]> rewrite(CompilerProvider compiler);

  protected void applyCommands(@NonNull CodeActionItem action) {}
}
