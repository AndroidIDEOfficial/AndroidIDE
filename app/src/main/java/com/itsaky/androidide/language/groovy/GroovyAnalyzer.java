package com.itsaky.androidide.language.groovy;

import com.itsaky.androidide.syntax.lexer.impls.groovy.GroovyLexerImpl;
import com.itsaky.lsp.services.IDELanguageServer;
import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;
import java.io.File;
import com.itsaky.lsp.SemanticHighlight;
import java.util.Map;
import org.eclipse.lsp4j.Diagnostic;

public class GroovyAnalyzer extends io.github.rosemoe.editor.langs.AbstractCodeAnalyzer {

	@Override
	public void analyze(IDELanguageServer server, File file, CharSequence content, TextAnalyzeResult colors, TextAnalyzer.AnalyzeThread.Delegate delegate) {
		try {
			GroovyLexerImpl lexer = new GroovyLexerImpl(content.toString(), colors);
			lexer.init();

			// Adding spans, hex colors, block lines and all is handled by JavaLexerImpl
			while (delegate.shouldAnalyze()) {
				// null = EOF
				if(lexer.nextToken() == null)
					break;
			}
			if (lexer.stack.isEmpty() && lexer.currSwitch > lexer.maxSwitch)
				lexer.maxSwitch = lexer.currSwitch;

			colors.determine(lexer.lastLine);
			colors.setSuppressSwitch(lexer.maxSwitch + 10);
			colors.setHexColors(lexer.lineColors);
		} catch (Throwable e) {
		}
	}

    @Override
    public void setSemanticHighlights(SemanticHighlight highlights) {
    }

    @Override
    public void updateDiagnostics(Map<Integer, Map<Integer, Diagnostic>> diagnostics) {
    }
}
