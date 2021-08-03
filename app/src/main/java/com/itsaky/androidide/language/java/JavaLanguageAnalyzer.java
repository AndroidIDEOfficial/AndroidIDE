package com.itsaky.androidide.language.java;

import com.itsaky.androidide.syntax.lexer.impls.java.JavaLexerImpl;
import com.itsaky.lsp.Range;
import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;
import java.util.List;
import com.itsaky.lsp.JavaColors;

public class JavaLanguageAnalyzer implements CodeAnalyzer {
	
    private JavaColors colors;
    
	@Override
	public void analyze(CharSequence content, TextAnalyzeResult colors, TextAnalyzer.AnalyzeThread.Delegate delegate) {
		try {
			JavaLexerImpl lexer = new JavaLexerImpl(content.toString(), colors);
            lexer.setJavaColors(this.colors);
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
    
    public void setJavaColors(JavaColors colors) {
        this.colors = colors;
    }
}
