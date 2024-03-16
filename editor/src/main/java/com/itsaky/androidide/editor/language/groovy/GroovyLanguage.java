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
package com.itsaky.androidide.editor.language.groovy;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.itsaky.androidide.editor.language.IDELanguage;
import com.itsaky.androidide.editor.language.newline.BracketsNewlineHandler;
import com.itsaky.androidide.editor.language.utils.CommonSymbolPairs;
import com.itsaky.androidide.lexers.groovy.GroovyLexer;
import com.itsaky.androidide.utils.CharSequenceReader;
import io.github.rosemoe.sora.lang.analysis.AnalyzeManager;
import io.github.rosemoe.sora.lang.completion.CompletionCancelledException;
import io.github.rosemoe.sora.lang.completion.CompletionPublisher;
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.ContentReference;
import io.github.rosemoe.sora.widget.SymbolPairMatch;
import java.io.StringReader;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroovyLanguage extends IDELanguage {

  private static final Logger LOG = LoggerFactory.getLogger(GroovyLanguage.class);
  private final GroovyAnalyzer analyzer;
  private final GroovyAutoComplete completer;
  private final NewlineHandler[] newlineHandlers =
    new NewlineHandler[]{new BracketsNewlineHandler(this::getIndentAdvance, this::useTab)};
  private final CommonSymbolPairs symbolPairs = new CommonSymbolPairs();

  public GroovyLanguage() {
    analyzer = new GroovyAnalyzer();
    completer = new GroovyAutoComplete();
  }

  @NonNull
  @Override
  public AnalyzeManager getAnalyzeManager() {
    return analyzer;
  }

  @Override
  public int getInterruptionLevel() {
    return INTERRUPTION_LEVEL_STRONG;
  }

  @Override
  public void requireAutoComplete(
    @NonNull ContentReference content,
    @NonNull CharPosition position,
    @NonNull CompletionPublisher publisher,
    @NonNull Bundle extraArguments)
    throws CompletionCancelledException {

    completer.complete(content, position, publisher, extraArguments);
  }

  @Override
  public int getIndentAdvance(@NonNull ContentReference content, int line, int column) {
    try {
      GroovyLexer lexer = new GroovyLexer(CharStreams.fromReader(new CharSequenceReader(content)));
      Token token;
      int advance = 0;
      while (((token = lexer.nextToken()) != null && token.getType() != token.EOF)) {
        switch (token.getType()) {
          case GroovyLexer.LBRACE:
            advance++;
            break;
          case GroovyLexer.RBRACE:
            advance--;
            break;
        }
      }
      advance = Math.max(0, advance);
      return advance * getTabSize();
    } catch (Throwable e) {
      LOG.error("Failed to calculate indent advance", e);
    }
    return 0;
  }

  @Override
  public SymbolPairMatch getSymbolPairs() {
    return symbolPairs;
  }

  @Override
  public NewlineHandler[] getNewlineHandlers() {
    return newlineHandlers;
  }

  @Override
  public void destroy() {
  }

  @Override
  public int getIndentAdvance(@NonNull String p1) {
    try {
      GroovyLexer lexer = new GroovyLexer(CharStreams.fromReader(new StringReader(p1)));
      Token token = null;
      int advance = 0;
      while (((token = lexer.nextToken()) != null && token.getType() != token.EOF)) {
        switch (token.getType()) {
          case GroovyLexer.LBRACE:
            advance++;
            break;
          case GroovyLexer.RBRACE:
            advance--;
            break;
        }
      }
      advance = Math.max(0, advance);
      return advance * getTabSize();
    } catch (Throwable e) {
      LOG.error("Failed to calculate indent advance", e);
    }
    return 0;
  }
}
