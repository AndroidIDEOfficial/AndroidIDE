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
package com.itsaky.androidide.editor.language;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.itsaky.androidide.editor.api.IEditor;
import com.itsaky.androidide.lookup.Lookup;
import com.itsaky.androidide.lsp.api.ICompletionCancelChecker;
import com.itsaky.androidide.lsp.api.ILanguageServer;
import com.itsaky.androidide.preferences.internal.EditorPreferencesKt;
import com.itsaky.androidide.utils.ILogger;

import java.nio.file.Paths;
import java.util.ArrayList;

import io.github.rosemoe.sora.lang.Language;
import io.github.rosemoe.sora.lang.completion.CompletionCancelledException;
import io.github.rosemoe.sora.lang.completion.CompletionPublisher;
import io.github.rosemoe.sora.lang.format.Formatter;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.ContentReference;

/**
 * Base class for language implementations in the IDE.
 *
 * @author Akash Yadav
 */
public abstract class IDELanguage implements Language {
  
  private static final ILogger LOG = ILogger.newInstance("IDELanguage");
  private Formatter formatter;

  @Override
  public void requireAutoComplete(
      @NonNull final ContentReference content,
      @NonNull final CharPosition position,
      @NonNull final CompletionPublisher publisher,
      @NonNull final Bundle extraArguments)
      throws CompletionCancelledException {
    try {
      final var cancelChecker = new CompletionCancelChecker(publisher);
      Lookup.getDefault().register(ICompletionCancelChecker.class, cancelChecker);
      doComplete(content, position, publisher, extraArguments);
    } finally {
      Lookup.getDefault().unregister(ICompletionCancelChecker.class);
    }
  }

  private void doComplete(
      final @NonNull ContentReference content,
      final @NonNull CharPosition position,
      final @NonNull CompletionPublisher publisher,
      final @NonNull Bundle extraArguments) {
    final var server = getLanguageServer();
    if (server == null) {
      LOG.warn("Cannot provide completions. No language server available.");
      return;
    }

    final var path = extraArguments.getString(IEditor.KEY_FILE, null);
    if (path == null) {
      LOG.warn("Cannot provide completions. No file provided.");
      return;
    }

    final var completionProvider = new CommonCompletionProvider(server);
    final var file = Paths.get(path);

    final var completionItems =
        completionProvider.complete(content, file, position, this::checkIsCompletionChar);

    publisher.setUpdateThreshold(1);
    publisher.addItems(new ArrayList<>(completionItems));
  }

  protected ILanguageServer getLanguageServer() {
    return null;
  }

  /**
   * Check if the given character is a completion character.
   *
   * @param c The character to check.
   * @return <code>true</code> if the character is completion char, <code>false</code> otherwise.
   */
  protected boolean checkIsCompletionChar(char c) {
    return false;
  }

  @Override
  public boolean useTab() {
    return !EditorPreferencesKt.getUseSoftTab();
  }

  @NonNull
  @Override
  public Formatter getFormatter() {
    if (formatter != null) {
      return formatter;
    }

    return formatter = new LSPFormatter(getLanguageServer());
  }

  public int getTabSize() {
    return EditorPreferencesKt.getTabSize();
  }
  
  @Override
  public int getIndentAdvance(@NonNull final ContentReference content, final int line, final int column) {
    return getIndentAdvance(content.getLine(line).substring(0, column));
  }
  
  public int getIndentAdvance(@NonNull String line) {
    return 0;
  }
}
