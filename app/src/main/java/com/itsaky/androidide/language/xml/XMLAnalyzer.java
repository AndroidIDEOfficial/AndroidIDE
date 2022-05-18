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

import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.OPERATOR;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forComment;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.get;
import static io.github.rosemoe.sora.widget.schemes.EditorColorScheme.LITERAL;

import com.itsaky.androidide.lexers.xml.XMLLexer;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.CharSequenceReader;
import com.itsaky.androidide.utils.ILogger;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.Token;

import java.io.IOException;
import java.util.Stack;

import io.github.rosemoe.sora.lang.analysis.SimpleAnalyzeManager;
import io.github.rosemoe.sora.lang.styling.CodeBlock;
import io.github.rosemoe.sora.lang.styling.MappedSpans;
import io.github.rosemoe.sora.lang.styling.Styles;

/**
 * Simple analyzer implementation for the XML files.
 *
 * @author Akash Yadav
 */
public class XMLAnalyzer extends SimpleAnalyzeManager<Void> {

  private static final ILogger LOG = ILogger.newInstance("XMLAnalyzer");

  @Override
  protected Styles analyze(StringBuilder text, Delegate<Void> delegate) {
    final var styles = new Styles();
    final var colors = new MappedSpans.Builder();

    CodePointCharStream stream;
    try {
      stream = CharStreams.fromReader(new CharSequenceReader(text));
    } catch (IOException e) {
      LOG.error("Unable to create stream for analyze", e);
      return styles;
    }

    XMLLexer lexer = new XMLLexer(stream);
    Token token;
    int previous = 0;
    int line, column, lastLine = 1;
    var first = true;

    final var stack = new Stack<CodeBlock>();

    while (!delegate.isCancelled()) {
      token = lexer.nextToken();
      if (token == null) {
        break;
      }

      if (token.getType() == XMLLexer.EOF) {
        lastLine = token.getLine() - 1;
        break;
      }

      line = token.getLine() - 1;
      column = token.getCharPositionInLine();
      lastLine = line;

      switch (token.getType()) {
        case XMLLexer.S:
        case XMLLexer.SEA_WS:
          if (first) {
            colors.addNormalIfNull();
          }

          break;
        case XMLLexer.COMMENT:
          colors.addIfNeeded(line, column, forComment());
          break;
        case XMLLexer.OPEN:
        case XMLLexer.OPEN_SLASH:
        case XMLLexer.CLOSE:
        case XMLLexer.SLASH:
        case XMLLexer.SPECIAL_CLOSE:
        case XMLLexer.EQUALS:
        case XMLLexer.COLON:
        case XMLLexer.XMLDeclOpen:
          colors.addIfNeeded(line, column, get(OPERATOR));
          break;
        case XMLLexer.SLASH_CLOSE:
          colors.addIfNeeded(line, column, get(OPERATOR));
          final var closeBlock = stack.pop();
          closeBlock.endLine = line;
          closeBlock.endColumn = column;
          styles.addCodeBlock(closeBlock);
          break;
        case XMLLexer.STRING:
          colors.addIfNeeded(line, column, LITERAL);
          break;
        case XMLLexer.Name:
          var type = SchemeAndroidIDE.TEXT_NORMAL;
          if (previous == XMLLexer.OPEN) {
            type = SchemeAndroidIDE.XML_TAG;
            final var block = styles.obtainNewBlock();
            block.startLine = line;
            block.startColumn = column;
            stack.push(block);
          }

          if (previous == XMLLexer.OPEN_SLASH) {
            type = SchemeAndroidIDE.XML_TAG;
            final var block = stack.pop();
            block.endLine = line;
            block.endColumn = column;
            styles.addCodeBlock(block);
          }

          colors.addIfNeeded(line, column, get(type));
          break;
        case XMLLexer.TEXT:
        default:
          colors.addIfNeeded(line, column, get(SchemeAndroidIDE.TEXT_NORMAL));
          break;
      }
      first = false;
      if (token.getType() != XMLLexer.SEA_WS) {
        previous = token.getType();
      }
    }
    colors.determine(lastLine);

    styles.spans = colors.build();

    return styles;
  }
}
