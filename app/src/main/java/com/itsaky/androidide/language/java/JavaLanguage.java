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
 *
 */
package com.itsaky.androidide.language.java;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.language.CommonCompletionProvider;
import com.itsaky.androidide.language.IDELanguage;
import com.itsaky.androidide.lexers.java.JavaLexer;
import com.itsaky.androidide.lexers.java.JavaParser;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.views.editor.IDEEditor;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.models.DiagnosticItem;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.rosemoe.sora.lang.analysis.AnalyzeManager;
import io.github.rosemoe.sora.lang.completion.CompletionCancelledException;
import io.github.rosemoe.sora.lang.completion.CompletionPublisher;
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandleResult;
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.ContentReference;
import io.github.rosemoe.sora.text.TextUtils;
import io.github.rosemoe.sora.widget.SymbolPairMatch;

public class JavaLanguage extends IDELanguage {

  private static final ILogger LOG = ILogger.newInstance("JavaLanguage");
  private final NewlineHandler[] newlineHandlers;
  private JavaAnalyzer analyzer;
  private CommonCompletionProvider completer;

  public JavaLanguage() {
    final var server = getLanguageServer();
    this.analyzer = new JavaAnalyzer();
    this.completer = new CommonCompletionProvider(server);

    this.newlineHandlers = new NewlineHandler[1];
    this.newlineHandlers[0] = new BraceHandler();
  }

  @NonNull
  @Override
  public List<DiagnosticItem> getDiagnostics() {
    return analyzer == null ? Collections.emptyList() : analyzer.getDiagnostics();
  }

  @Override
  public int getIndentAdvance(@NonNull String line) {
    try {
      JavaLexer lexer = new JavaLexer(CharStreams.fromReader(new StringReader(line)));
      Token token;
      int advance = 0;
      while (((token = lexer.nextToken()) != null && token.getType() != token.EOF)) {
        switch (token.getType()) {
          case JavaLexer.LBRACE:
            advance++;
            break;
          case JavaParser.RBRACE:
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

  @Override
  public SymbolPairMatch getSymbolPairs() {
    return new JavaSymbolPairs();
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
    if (!extraArguments.containsKey(IDEEditor.KEY_FILE)) {
      return;
    }
    final var file = Paths.get(extraArguments.getString(IDEEditor.KEY_FILE));
    publisher.setUpdateThreshold(0);
    publisher.addItems(
        new ArrayList<>(
            completer.complete(
                content, file, position, CommonCompletionProvider::checkJavaCompletionChar)));
  }

  @Override
  public int getIndentAdvance(@NonNull ContentReference content, int line, int column) {
    return getIndentAdvance(content.getLine(line).substring(0, column));
  }

  @Override
  protected ILanguageServer getLanguageServer() {
    return StudioApp.getInstance().getJavaLanguageServer();
  }

  @Override
  public NewlineHandler[] getNewlineHandlers() {
    return newlineHandlers;
  }

  @Override
  public void destroy() {
    analyzer = null;
    completer = null;
  }

  private static class JavaSymbolPairs extends SymbolPairMatch {
    public JavaSymbolPairs() {
      super.putPair('{', new Replacement("{}", 1));
      super.putPair('(', new Replacement("()", 1));
      super.putPair('[', new Replacement("[]", 1));
      super.putPair('"', new Replacement("\"\"", 1));
      super.putPair('\'', new Replacement("''", 1));
      super.putPair('<', new Replacement("<>", 1));
    }
  }

  class BraceHandler implements NewlineHandler {

    @Override
    public boolean matchesRequirement(String beforeText, String afterText) {
      return beforeText.endsWith("{") && afterText.startsWith("}");
    }

    @Override
    public NewlineHandleResult handleNewline(String beforeText, String afterText, int tabSize) {
      int count = TextUtils.countLeadingSpaceCount(beforeText, tabSize);
      int advanceBefore = getIndentAdvance(beforeText);
      int advanceAfter = getIndentAdvance(afterText);
      String text;
      StringBuilder sb =
          new StringBuilder("\n")
              .append(TextUtils.createIndent(count + advanceBefore, tabSize, useTab()))
              .append('\n')
              .append(text = TextUtils.createIndent(count + advanceAfter, tabSize, useTab()));
      int shiftLeft = text.length() + 1;
      return new NewlineHandleResult(sb, shiftLeft);
    }
  }
}
