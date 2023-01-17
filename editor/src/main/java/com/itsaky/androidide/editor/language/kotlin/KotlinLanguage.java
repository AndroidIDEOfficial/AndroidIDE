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
package com.itsaky.androidide.editor.language.kotlin;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.itsaky.androidide.editor.language.newline.BracketsNewlineHandler;
import com.itsaky.androidide.editor.language.newline.CStyleBracketsHandler;
import com.itsaky.androidide.editor.language.IDELanguage;
import com.itsaky.androidide.editor.language.java.JavaLanguage;
import com.itsaky.androidide.lexers.kotlin.KotlinLexer;
import com.itsaky.androidide.lexers.kotlin.KotlinParser;
import com.itsaky.androidide.utils.ILogger;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.StringReader;

import io.github.rosemoe.sora.lang.analysis.AnalyzeManager;
import io.github.rosemoe.sora.lang.completion.CompletionCancelledException;
import io.github.rosemoe.sora.lang.completion.CompletionPublisher;
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.ContentReference;
import io.github.rosemoe.sora.widget.SymbolPairMatch;

public class KotlinLanguage extends IDELanguage {

  private static final ILogger LOG = ILogger.newInstance("KotlinLanguage");
  private final NewlineHandler[] newlineHandlers =
      new NewlineHandler[] {new BracketsNewlineHandler(this::getIndentAdvance, this::useTab)};
  private KotlinAnalyzer analyzer;

  public KotlinLanguage() {
    analyzer = new KotlinAnalyzer();
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
      throws CompletionCancelledException {}

  @Override
  public int getIndentAdvance(@NonNull ContentReference content, int line, int column) {
    return getIndentAdvance(content.getLine(line).substring(0, column));
  }

  @Override
  public SymbolPairMatch getSymbolPairs() {
    return new KotlinSymbolPairs();
  }

  @Override
  public NewlineHandler[] getNewlineHandlers() {
    return newlineHandlers;
  }

  @Override
  public void destroy() {
    analyzer = null;
  }

  @Override
  public int getIndentAdvance(@NonNull String line) {
    try {
      KotlinLexer lexer = new KotlinLexer(CharStreams.fromReader(new StringReader(line)));
      Token token;
      int advance = 0;
      while (((token = lexer.nextToken()) != null && token.getType() != token.EOF)) {
        switch (token.getType()) {
          case KotlinParser.LCURL:
            advance++;
            break;
          case KotlinParser.RCURL:
            advance--;
            break;
        }
      }
      advance = Math.max(0, advance);
      return advance * getTabSize();
    } catch (Throwable e) {
      LOG.error("Error calculating indent advance", e);
    }
    return 0;
  }

  private static class KotlinSymbolPairs extends JavaLanguage.JavaSymbolPairs {}
}
