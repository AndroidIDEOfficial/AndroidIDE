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
package com.itsaky.androidide.language.xml;

import androidx.annotation.NonNull;

import com.itsaky.androidide.language.CommonCompletionProvider;
import com.itsaky.androidide.language.IDELanguage;
import com.itsaky.androidide.lexers.xml.XMLLexer;
import com.itsaky.androidide.lsp.api.ILanguageServer;
import com.itsaky.androidide.lsp.api.ILanguageServerRegistry;
import com.itsaky.androidide.lsp.xml.XMLLanguageServer;
import com.itsaky.androidide.utils.ILogger;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.StringReader;

import io.github.rosemoe.sora.lang.analysis.AnalyzeManager;
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler;
import io.github.rosemoe.sora.text.ContentReference;
import io.github.rosemoe.sora.util.MyCharacter;
import io.github.rosemoe.sora.widget.SymbolPairMatch;

public class XMLLanguage extends IDELanguage {

  private static final ILogger LOG = ILogger.newInstance("XMLLanguage");
  private final CommonCompletionProvider completer;
  private final NewlineHandler[] newlineHandlers;
  private XMLAnalyzer analyzer;

  public XMLLanguage() {
    this.completer = new CommonCompletionProvider(getLanguageServer());
    this.analyzer = new XMLAnalyzer();
    this.newlineHandlers = new NewlineHandler[0];
  }

  @Override
  protected ILanguageServer getLanguageServer() {
    return ILanguageServerRegistry.getDefault().getServer(XMLLanguageServer.SERVER_ID);
  }

  @Override
  protected boolean checkIsCompletionChar(final char c) {
    return MyCharacter.isJavaIdentifierPart(c) || c == '<' || c == '/';
  }

  @Override
  public int getIndentAdvance(@NonNull String content) {
    try {
      XMLLexer lexer = new XMLLexer(CharStreams.fromReader(new StringReader(content)));
      Token token;
      int advance = 0;
      while (((token = lexer.nextToken()) != null && token.getType() != token.EOF)) {
        switch (token.getType()) {
          case XMLLexer.OPEN:
            advance++;
            break;
          case XMLLexer.CLOSE:
          case XMLLexer.SLASH_CLOSE:
            advance--;
            break;
          default:
            break;
        }
      }
      advance = Math.max(0, advance);
      return advance * getTabSize();
    } catch (Throwable e) {
      LOG.error("Failed to compute indent advance", e);
    }
    return 0;
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
  public int getIndentAdvance(@NonNull ContentReference content, int line, int column) {
    final var text = content.getLine(line).substring(0, column);
    return getIndentAdvance(text.trim());
  }

  @Override
  public SymbolPairMatch getSymbolPairs() {
    return new SymbolPairMatch.DefaultSymbolPairs();
  }

  @Override
  public NewlineHandler[] getNewlineHandlers() {
    return newlineHandlers;
  }

  @Override
  public void destroy() {
    analyzer = null;
  }
}
