/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
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
 **************************************************************************************/

package com.itsaky.androidide.language.xml;

import androidx.annotation.NonNull;

import com.itsaky.androidide.lexers.xml.XMLLexer;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.SemanticHighlight;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.Token;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;
import io.github.rosemoe.editor.widget.EditorColorScheme;

public class XMLAnalyzer extends io.github.rosemoe.editor.langs.AbstractCodeAnalyzer {
    
    private static final Logger LOG = Logger.instance ("XMLAnalyzer");
    
    @Override
    public void analyze (ILanguageServer server, File file, @NonNull Content content, TextAnalyzeResult colors, @NonNull TextAnalyzer.AnalyzeThread.Delegate delegate) throws IOException {
        CodePointCharStream stream = CharStreams.fromReader (new StringReader (content.toString ()));
        XMLLexer lexer = new XMLLexer (stream);
        Token token = null, previous = null;
        int line = 0, column = 0, lastLine = 1;
        var first = true;
        
        while (delegate.shouldAnalyze ()) {
            token = lexer.nextToken ();
            if (token == null) {
                break;
            }
            
            if (token.getType () == XMLLexer.EOF) {
                lastLine = token.getLine () - 1;
                break;
            }
            
            line = token.getLine () - 1;
            column = token.getCharPositionInLine ();
            lastLine = line;
            
            switch (token.getType ()) {
                case XMLLexer.S:
                case XMLLexer.SEA_WS:
                    if (first) {
                        colors.addNormalIfNull ();
                    }
                    
                    break;
                case XMLLexer.COMMENT:
                    colors.addIfNeeded (line, column, EditorColorScheme.COMMENT);
                    break;
                case XMLLexer.OPEN:
                case XMLLexer.OPEN_SLASH:
                case XMLLexer.CLOSE:
                case XMLLexer.SLASH:
                case XMLLexer.SLASH_CLOSE:
                case XMLLexer.SPECIAL_CLOSE:
                case XMLLexer.EQUALS:
                case XMLLexer.XMLDeclOpen:
                    colors.addIfNeeded (line, column, EditorColorScheme.OPERATOR);
                    break;
                case XMLLexer.STRING:
                    checkAndAddHexString (token, EditorColorScheme.LITERAL, colors);
                    break;
                case XMLLexer.Name:
                    checkAndAddHexString (token, previous.getType () == XMLLexer.OPEN || previous.getType () == XMLLexer.OPEN_SLASH ? EditorColorScheme.XML_TAG : EditorColorScheme.TEXT_NORMAL, colors);
                    break;
                case XMLLexer.TEXT:
                    checkAndAddHexString (token, EditorColorScheme.TEXT_NORMAL, colors);
                default:
                    colors.addIfNeeded(line, column, EditorColorScheme.TEXT_NORMAL);
                    break;
            }
            first = false;
            if (token.getType () != XMLLexer.SEA_WS) {
                previous = token;
            }
        }
        colors.determine (lastLine);
    }
}
