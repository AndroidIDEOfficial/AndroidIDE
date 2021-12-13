/************************************************************************************
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
**************************************************************************************/
package com.itsaky.androidide.language.java;

import com.itsaky.androidide.language.BaseLanguage;
import com.itsaky.androidide.lexers.java.JavaLexer;
import com.itsaky.androidide.lexers.java.JavaParser;
import com.itsaky.androidide.lsp.LSPProvider;
import com.itsaky.androidide.syntax.lexer.impls.java.JavaLexerImpl;
import com.itsaky.androidide.utils.JavaCharacter;
import com.itsaky.lsp.services.IDELanguageServer;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.interfaces.NewlineHandler;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.TextUtils;
import io.github.rosemoe.editor.widget.SymbolPairMatch;
import java.io.File;
import java.io.StringReader;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class JavaLanguage extends BaseLanguage {
    
    private final JavaLexerImpl lexer;
	private final JavaAutoComplete complete;
    
    private final NewlineHandler[] newlineHandlers;
    
    public JavaLanguage() {
        this(null);
    }
    
	public JavaLanguage(File file) {
        super(file);
		this.lexer = new JavaLexerImpl(this);
		this.complete = new JavaAutoComplete();
        
        this.newlineHandlers = new NewlineHandler[2];
        this.newlineHandlers[0] = new BraceHandler();
        this.newlineHandlers[1] = this.lexer.stringHandler;
	}

    @Override
    public IDELanguageServer getLanguageServer() {
        return LSPProvider.getServerForLanguage(LSPProvider.LANGUAGE_JAVA);
    }

    @Override
    public String getLanguageCode() {
        return LSPProvider.LANGUAGE_JAVA;
    }

	@Override
	public CodeAnalyzer getAnalyzer() {
		return lexer;
	}

	@Override
	public AutoCompleteProvider getAutoCompleteProvider() {
		return complete;
	}

	@Override
	public boolean isAutoCompleteChar(char p1) {
		return JavaCharacter.isJavaIdentifierPart(p1) || p1 == '.';
	}

	@Override
	public int getIndentAdvance(String p1) {
		try {
			JavaLexer lexer = new JavaLexer(CharStreams.fromReader(new StringReader(p1)));
			Token token = null;
			int advance = 0;
			while (((token = lexer.nextToken()) != null && token.getType() != token.EOF)) {
				switch (token.getType()) {
					case JavaLexer.LBRACE:
						advance++;
						break;
					case JavaParser.RBRACE:
						advance --;
						break;
				}
			}
			advance = Math.max(0, advance);
			return advance * getTabSize();
		} catch (Throwable e) {}
		return 0;
	}

	@Override
	public SymbolPairMatch getSymbolPairs() {
		return new JavaSymbolPairs();
	}

	@Override
	public boolean useTab() {
		return false;
	}

	@Override
	public CharSequence format(CharSequence content) {
		return content;
    }
    
    @Override
    public NewlineHandler[] getNewlineHandlers() {
        return newlineHandlers;
    }

    class BraceHandler implements NewlineHandler {

        @Override
        public boolean matchesRequirement(String beforeText, String afterText, CharPosition cursor) {
            return beforeText.endsWith("{") && afterText.startsWith("}");
        }

        @Override
        public HandleResult handleNewline(String beforeText, String afterText, int tabSize) {
            int count = TextUtils.countLeadingSpaceCount(beforeText, tabSize);
            int advanceBefore = getIndentAdvance(beforeText);
            int advanceAfter = getIndentAdvance(afterText);
            String text;
            StringBuilder sb = new StringBuilder("\n")
                .append(TextUtils.createIndent(count + advanceBefore, tabSize, useTab()))
                .append('\n')
                .append(text = TextUtils.createIndent(count + advanceAfter, tabSize, useTab()));
            int shiftLeft = text.length() + 1;
            return new HandleResult(sb, shiftLeft);
        }
    }
    
    private class JavaSymbolPairs extends SymbolPairMatch {
        public JavaSymbolPairs() {
            super.putPair('{', new Replacement("{}", 1));
            super.putPair('(', new Replacement("()", 1));
            super.putPair('[', new Replacement("[]", 1));
            super.putPair('"', new Replacement("\"\"", 1));
            super.putPair('\'', new Replacement("''", 1));
            super.putPair('<', new Replacement("<>", 1));
        }
    }
}
