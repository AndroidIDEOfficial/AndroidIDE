package com.itsaky.androidide.language.xml;

import com.itsaky.androidide.language.java.manager.JavaCharacter;
import com.itsaky.androidide.language.xml.lexer.XMLLexer;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.interfaces.EditorLanguage;
import io.github.rosemoe.editor.interfaces.NewlineHandler;
import io.github.rosemoe.editor.text.TextUtils;
import io.github.rosemoe.editor.widget.CodeEditor;
import io.github.rosemoe.editor.widget.SymbolPairMatch;
import java.io.StringReader;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class XMLLanguage implements EditorLanguage {

	private XMLAnalyzer analyzer;
	private XMLAutoComplete completer;
	private NewlineHandler[] newlineHandlers = new NewlineHandler[] {};

	public XMLLanguage() {
		this.completer = new XMLAutoComplete();
		this.analyzer = new XMLAnalyzer();
		this.newlineHandlers = new NewlineHandler[]{new IndentHandler()};
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
			Token token = null;
			int advance = 0;
			while (((token = lexer.nextToken()) != null && token.getType() != token.EOF)) {
				switch (token.getType()) {
					case XMLLexer.OPEN:
					case XMLLexer.OPEN_SLASH :
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
			return advance * 4;
		} catch (Throwable e) {}
		return 0;
	}

	@Override
	public SymbolPairMatch getSymbolPairs() {
		return new SymbolPairMatch.DefaultSymbolPairs();
	}

	@Override
	public boolean useTab() {
		return true;
	}

	@Override
	public CharSequence format(CharSequence content) {
		return content;
	}

    @Override
    public NewlineHandler[] getNewlineHandlers() {
        return newlineHandlers;
    }

    class IndentHandler implements NewlineHandler {

        @Override
        public boolean matchesRequirement(String beforeText, String afterText) {
			return true;
        }

        @Override
        public HandleResult handleNewline(String beforeText, String afterText, int tabSize) {
            int count = TextUtils.countLeadingSpaceCount(beforeText, tabSize);
            int advanceBefore = getIndentAdvance(beforeText);
            StringBuilder sb = new StringBuilder("\n").append(TextUtils.createIndent(count + advanceBefore, tabSize, useTab()));
            return new HandleResult(sb, 0);
        }
    }
}
