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

import com.itsaky.androidide.language.BaseLanguage;
import com.itsaky.androidide.lexers.xml.XMLLexer;
import com.itsaky.androidide.utils.JavaCharacter;
import com.itsaky.androidide.utils.Logger;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.StringReader;

import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.interfaces.NewlineHandler;
import io.github.rosemoe.editor.widget.SymbolPairMatch;

public class XMLLanguage extends BaseLanguage {

	private final XMLAnalyzer analyzer;
	private final XMLAutoComplete completer;
	private final NewlineHandler[] newlineHandlers;
	
	private static final Logger LOG = Logger.instance ("XMLLanguage");
	
	public XMLLanguage() {
		this.completer = new XMLAutoComplete();
		this.analyzer = new XMLAnalyzer();
		this.newlineHandlers = new NewlineHandler[0];
	}
    
	@Override
	public CodeAnalyzer getAnalyzer() {
		return analyzer;
	}

	@Override
	public AutoCompleteProvider getAutoCompleteProvider() {
		return completer;
	}

	@Override
	public boolean isAutoCompleteChar(char ch) {
		return JavaCharacter.isJavaIdentifierPart(ch)
        || ch == '<'
        || ch == '/';
	}

	@Override
	public int getIndentAdvance(String content) {
		try {
			XMLLexer lexer = new XMLLexer(CharStreams.fromReader(new StringReader(content)));
			Token token;
			int advance = 0;
			while (((token = lexer.nextToken()) != null && token.getType() != token.EOF)) {
				switch (token.getType()) {
					case XMLLexer.OPEN:
					case XMLLexer.OPEN_SLASH :
                    case XMLLexer.XMLDeclOpen :
						advance++;
						break;
					case XMLLexer.CLOSE:
					case XMLLexer.SLASH_CLOSE :
					case XMLLexer.SPECIAL_CLOSE :
						advance --;
						break;
					default :
						break;
				}
			}
			advance = Math.max(0, advance);
			return advance * getTabSize();
		} catch (Throwable e) {
			LOG.error ("Failed to compute indent advance", e);
		}
		return 0;
	}

	@Override
	public SymbolPairMatch getSymbolPairs() {
		return new SymbolPairMatch.DefaultSymbolPairs();
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
}
