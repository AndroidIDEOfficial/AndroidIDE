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
import com.itsaky.lsp.models.FormatCodeParams;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;

import io.github.rosemoe.sora.lang.Language;
import io.github.rosemoe.sora.text.CharPosition;

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

  @Override
  public CharSequence format(CharSequence text) {
    return formatCode(text, Range.NONE);
  }

  @Override
  public CharSequence formatRegion(
      final CharSequence text, final CharPosition start, final CharPosition end) {
    return formatCode(
        text,
        new Range(
            new Position(start.line, start.column, start.index),
            new Position(end.line, end.column, end.index)));
  }

  public CharSequence formatCode(CharSequence content, Range formattingRange) {
    final var server = getLanguageServer();
    if (server != null) {
      return server.formatCode(new FormatCodeParams(content, formattingRange));
    }

    return content;
  }

  protected ILanguageServer getLanguageServer() {
    return null;
  }

  public int getTabSize() {
    return StudioApp.getInstance().getPrefManager().getEditorTabSize();
  }

  public int getIndentAdvance(@NonNull String line) {
    return 0;
  }
}
