package com.itsaky.androidide.language.groovy;

import android.graphics.Color;
import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.syntax.lexer.impls.groovy.GroovyLexerImpl;
import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.struct.HexColor;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.Token;

public class GroovyAnalyzer implements CodeAnalyzer {

	@Override
	public void analyze(CharSequence content, TextAnalyzeResult colors, TextAnalyzer.AnalyzeThread.Delegate delegate) {
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
}
