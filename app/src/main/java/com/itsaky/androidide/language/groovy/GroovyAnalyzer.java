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

package com.itsaky.androidide.language.groovy;

import com.itsaky.androidide.syntax.lexer.impls.groovy.GroovyLexerImpl;
import com.itsaky.lsp.SemanticHighlight;
import com.itsaky.lsp.services.IDELanguageServer;
import io.github.rosemoe.editor.interfaces.EditorLanguage;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;
import java.io.File;
import java.util.Map;
import org.eclipse.lsp4j.Diagnostic;
import java.io.IOException;

public class GroovyAnalyzer extends io.github.rosemoe.editor.langs.AbstractCodeAnalyzer {
    
    private final EditorLanguage language;
    
    public GroovyAnalyzer (EditorLanguage language) {
        this.language = language;
    }
    
	@Override
	public void analyze(IDELanguageServer server, File file, CharSequence content, TextAnalyzeResult colors, TextAnalyzer.AnalyzeThread.Delegate delegate) throws IOException {
		GroovyLexerImpl lexer = new GroovyLexerImpl(this.language, (Content) content, colors);
        lexer.init();
        
        // Adding spans, hex colors, block lines and all is handled by GroovyLexerImpl
        while (delegate.shouldAnalyze()) {
            // null = EOF
            if(lexer.nextToken() == null) {
                break;
            }
        }
        
        if (lexer.stack.isEmpty() && lexer.currSwitch > lexer.maxSwitch) {
            lexer.maxSwitch = lexer.currSwitch;
        }

        colors.determine(lexer.lastLine);
        colors.setSuppressSwitch(lexer.maxSwitch + 10);
	}

    @Override
    public void setSemanticHighlights(SemanticHighlight highlights) {
    }

    @Override
    public void updateDiagnostics(Map<Integer, Map<Integer, Diagnostic>> diagnostics) {
    }
}
