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

import androidx.annotation.NonNull;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.language.BaseLanguage;
import com.itsaky.androidide.language.CommonCompletionProvider;
import com.itsaky.androidide.lexers.java.JavaLexer;
import com.itsaky.androidide.lexers.java.JavaParser;
import com.itsaky.androidide.utils.JavaCharacter;
import com.itsaky.androidide.utils.Logger;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.StringReader;

import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.interfaces.NewlineHandler;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.TextUtils;
import io.github.rosemoe.editor.widget.SymbolPairMatch;

public class JavaLanguage extends BaseLanguage {
    
    private final JavaAnalyzer analyzer;
	private final AutoCompleteProvider complete;
    
    private final NewlineHandler[] newlineHandlers;
    
    private static final Logger LOG = Logger.instance("JavaLanguage");
    
	public JavaLanguage() {
		this.analyzer = new JavaAnalyzer ();
		this.complete = new CommonCompletionProvider (StudioApp.getInstance ().getJavaLanguageServer ());
        
        this.newlineHandlers = new NewlineHandler[1];
        this.newlineHandlers[0] = new BraceHandler();
	}

	@Override
	public CodeAnalyzer getAnalyzer() {
		return analyzer;
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
		} catch (Throwable e) {
			LOG.error ("Error calculating indent advance", e);
		}
		return 0;
	}

	@Override
	public SymbolPairMatch getSymbolPairs() {
		return new JavaSymbolPairs ();
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
        public boolean matchesRequirement(@NonNull String beforeText, String afterText, CharPosition cursor) {
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
}
