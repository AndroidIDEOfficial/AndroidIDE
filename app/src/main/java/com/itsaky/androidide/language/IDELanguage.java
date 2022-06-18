/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.itsaky.androidide.language;

import androidx.annotation.NonNull;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.models.DiagnosticItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.rosemoe.sora.lang.Language;

/**
 * Base class for language implementations in the IDE.
 *
 * @author Akash Yadav
 */
public abstract class IDELanguage implements Language {

  @Override
  public boolean useTab() {
    return false;
  }

  public int getTabSize() {
    return StudioApp.getInstance().getPrefManager().getEditorTabSize();
  }

  /**
   * Get the diagnostics from the last analyze.
   *
   * @return The diagnostics. Must not be null.
   */
  @NonNull
  public List<DiagnosticItem> getDiagnostics() {
    return Collections.emptyList();
  }

  public void setDiagnostics(List<DiagnosticItem> diagnostics) {
    if (diagnostics == null) {
      diagnostics = new ArrayList<>(0);
    }

    final var analyzer = getAnalyzeManager();
    if (analyzer instanceof IAnalyzeManager) {
      ((IAnalyzeManager) analyzer).updateDiagnostics(diagnostics);
      analyzer.rerun();
    }
  }

  @Override
  public CharSequence format(CharSequence text) {
    final var server = getLanguageServer();
    if (server != null) {
      return server.formatCode(text);
    }

    return text;
  }

  public int getIndentAdvance(@NonNull String line) {
    return 0;
  }

  protected ILanguageServer getLanguageServer() {
    return null;
  }
}
