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

import android.graphics.Color;
import com.itsaky.androidide.lexers.xml.XMLLexer;
import com.itsaky.lsp.SemanticHighlight;
import com.itsaky.lsp.services.IDELanguageServer;
import io.github.rosemoe.editor.struct.Span;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;
import io.github.rosemoe.editor.widget.EditorColorScheme;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.Token;
import org.eclipse.lsp4j.Diagnostic;

public class XMLAnalyzer extends io.github.rosemoe.editor.langs.AbstractCodeAnalyzer {

	@Override
	public void analyze(IDELanguageServer server, File file, CharSequence content, TextAnalyzeResult colors, TextAnalyzer.AnalyzeThread.Delegate delegate) throws IOException {
		CodePointCharStream stream = CharStreams.fromReader(new StringReader(content.toString()));
        XMLLexer lexer = new XMLLexer(stream);
        Token token = null, previous = null;
        int line = 0, column = 0, lastLine = 1;
        boolean first = true;
        
        while (delegate.shouldAnalyze()) {
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
            
            Span span = null; // The added span
            switch (token.getType()) {
                case XMLLexer.S :
                case XMLLexer.SEA_WS :
                    if (first) {
                        colors.addNormalIfNull();
                    }
                    break;
                case XMLLexer.COMMENT :
                    span = colors.addIfNeeded(line, column, EditorColorScheme.COMMENT);
                    break;
                case XMLLexer.OPEN :
                case XMLLexer.OPEN_SLASH :
                case XMLLexer.CLOSE :
                case XMLLexer.SLASH :
                case XMLLexer.SLASH_CLOSE :
                case XMLLexer.SPECIAL_CLOSE :
                case XMLLexer.EQUALS :
                case XMLLexer.XMLDeclOpen :
                    span = colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
                    break;
                case XMLLexer.STRING :
                    span = colors.addIfNeeded(line, column, EditorColorScheme.LITERAL);
                    addHexColorIfPresent(token, span);
                    break;
                case XMLLexer.Name :
                    span = colors.addIfNeeded(line, column, previous.getType() == XMLLexer.OPEN || previous.getType() == XMLLexer.OPEN_SLASH ? EditorColorScheme.XML_TAG : EditorColorScheme.TEXT_NORMAL);
                    addHexColorIfPresent(token, span);
                    break;
                case XMLLexer.TEXT :
                    span = colors.addIfNeeded(line, column, EditorColorScheme.TEXT_NORMAL);
                    addHexColorIfPresent(token, span);
                    break;
                default :
                    span = colors.addIfNeeded(line, column, EditorColorScheme.TEXT_NORMAL);
                    break;
            }
            first = false;
            if (token.getType() != XMLLexer.SEA_WS && token.getType() != XMLLexer.S)
                previous = token;
        }
        colors.determine(lastLine);
	}
    
    @Override
    public void setSemanticHighlights(SemanticHighlight highlights) {
    }
    
    @Override
    public void updateDiagnostics(Map<Integer, Map<Integer, Diagnostic>> diagnostics) {
    }
    
	private void addHexColorIfPresent(Token token, final Span span) {
		Matcher m = HEX.matcher(token.getText());
		if (m.find()) {
			try {
				span.underlineColor = Color.parseColor(token.getText().substring(m.start(), m.end())); 
				span.underlineHeight = Span.HEX_COLOR_UNDERLINE_HEIGHT;
			} catch (Throwable th) {
                // ignored
                // The hex color may not be a valid color code
            } 
		} 
	}
    
	private final Pattern HEX = Pattern.compile("#[a-fA-F0-9]{3,8}");
}
