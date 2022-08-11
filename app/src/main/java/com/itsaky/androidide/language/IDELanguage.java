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
import com.itsaky.androidide.lsp.api.ILanguageServer;

import io.github.rosemoe.sora.lang.Language;
import io.github.rosemoe.sora.lang.format.Formatter;

/**
 * Base class for language implementations in the IDE.
 *
 * @author Akash Yadav
 */
public abstract class IDELanguage implements Language {

  private Formatter formatter;
  
  @Override
  public boolean useTab() {
    return false;
  }
  
  @NonNull
  @Override
  public Formatter getFormatter() {
    if (formatter != null) {
      return formatter;
    }
    
    return formatter = new LSPFormatter(getLanguageServer());
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
