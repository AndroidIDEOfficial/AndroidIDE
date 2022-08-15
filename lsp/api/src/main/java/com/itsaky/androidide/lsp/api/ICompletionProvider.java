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

package com.itsaky.androidide.lsp.api;

import androidx.annotation.NonNull;

import com.itsaky.androidide.lookup.Lookup;
import com.itsaky.androidide.lsp.models.CompletionItem;
import com.itsaky.androidide.lsp.models.CompletionParams;
import com.itsaky.androidide.lsp.models.CompletionResult;
import com.itsaky.androidide.lsp.models.CompletionsKt;
import com.itsaky.androidide.lsp.models.MatchLevel;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A completion provider provides completions at the given line and column.
 *
 * @author Akash Yadav
 */
public interface ICompletionProvider {

  int MIN_MATCH_RATIO = CompletionsKt.MIN_MATCH_RATIO;

  default boolean canComplete(Path file) {
    return file != null && Files.exists(file) && !Files.isDirectory(file);
  }

  /** Abort the completion process if cancelled. */
  default void abortCompletionIfCancelled() {
    final var checker = Lookup.DEFAULT.lookup(ICompletionCancelChecker.class);
    if (checker != null) {
      checker.abortIfCancelled();
    }
  }

  default MatchLevel matchLevel(CharSequence candidate, CharSequence partial) {
    return CompletionItem.matchLevel(candidate.toString(), partial.toString());
  }

  /**
   * Compute completions using the given params and return the given completion result.
   *
   * @param params The params that can be used to compute completion items.
   * @return The completion result. Must not be null.
   */
  @NonNull
  CompletionResult complete(CompletionParams params);
}
