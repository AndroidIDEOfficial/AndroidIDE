package com.itsaky.androidide.language.xml;

import com.itsaky.androidide.language.BaseLanguage;
import com.itsaky.androidide.language.java.manager.JavaCharacter;
import com.itsaky.androidide.language.xml.lexer.XMLLexer;
import com.itsaky.androidide.lsp.LSPProvider;
import com.itsaky.lsp.services.IDELanguageServer;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.interfaces.NewlineHandler;
import io.github.rosemoe.editor.text.TextUtils;
import io.github.rosemoe.editor.widget.SymbolPairMatch;
import java.io.File;
import java.io.StringReader;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class XMLLanguage extends BaseLanguage {

	private XMLAnalyzer analyzer;
	private XMLAutoComplete completer;
	private NewlineHandler[] newlineHandlers = new NewlineHandler[] {};
    
    public XMLLanguage() {
        super(null);
    }
    
	public XMLLanguage(File file) {
        super(file);
		this.completer = new XMLAutoComplete();
		this.analyzer = new XMLAnalyzer();
		this.newlineHandlers = new NewlineHandler[0];
	}

    @Override
    public IDELanguageServer getLanguageServer() {
        return null;
    }

    @Override
    public String getLanguageCode() {
        return LSPProvider.LANGUAGE_XML;
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
    public boolean needsWholePreviousContentForIndent() {
        return true;
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
		} catch (Throwable e) {}
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
