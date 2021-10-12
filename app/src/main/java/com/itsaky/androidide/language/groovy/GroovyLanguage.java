package com.itsaky.androidide.language.groovy;

import com.itsaky.androidide.language.BaseLanguage;
import com.itsaky.androidide.language.groovy.lexer.GroovyLexer;
import com.itsaky.androidide.language.java.manager.JavaCharacter;
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

public class GroovyLanguage extends BaseLanguage {
	
	private GroovyAnalyzer analyzer;
	private GroovyAutoComplete completor;
	
    public GroovyLanguage() {
        this(null);
    }
    
	public GroovyLanguage(File file) {
        super(file);
		analyzer = new GroovyAnalyzer();
		completor = new GroovyAutoComplete();
	}

    @Override
    public IDELanguageServer getLanguageServer() {
        return null;
    }

    @Override
    public String getLanguageCode() {
        return LSPProvider.LANGUAGE_GROOVY;
    }
	
	@Override
	public CodeAnalyzer getAnalyzer() {
		return analyzer;
	}
	
	@Override
	public AutoCompleteProvider getAutoCompleteProvider() {
		return completor;
	}
	
	@Override
	public boolean isAutoCompleteChar(char ch) {
		return JavaCharacter.isJavaIdentifierPart(ch) || ch == '.';
	}
	
	@Override
	public int getIndentAdvance(String p1)
	{
		try
		{
			GroovyLexer lexer = new GroovyLexer(CharStreams.fromReader(new StringReader(p1)));
			Token token = null;
			int advance = 0;
			while (((token = lexer.nextToken()) != null && token.getType() != token.EOF))
			{
				switch (token.getType())
				{
					case GroovyLexer.LBRACE:
						advance++;
						break;
					case GroovyLexer.RBRACE:
						advance --;
						break;
				}
			}
			advance = Math.max(0, advance);
			return advance * getTabSize();
		} catch (Throwable e)
		{}
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

    private NewlineHandler[] newlineHandlers = new NewlineHandler[] { new BraceHandler() };

    @Override
    public NewlineHandler[] getNewlineHandlers() {
        return newlineHandlers;
    }

    class BraceHandler implements NewlineHandler {

        @Override
        public boolean matchesRequirement(String beforeText, String afterText) {
			beforeText = beforeText.trim();
			afterText = afterText.trim();
            return beforeText.endsWith("{") && afterText.startsWith("}");
        }

        @Override
        public HandleResult handleNewline(String beforeText, String afterText, int tabSize) {
            int count = TextUtils.countLeadingSpaceCount(beforeText, tabSize);
            int advanceBefore = getIndentAdvance(beforeText);
            int advanceAfter = getIndentAdvance(afterText);
            String text;
            StringBuilder sb = new StringBuilder("\n").append(TextUtils.createIndent(count + advanceBefore, tabSize, useTab())).append('\n').append(text = TextUtils.createIndent(count + advanceAfter, tabSize, useTab()));
            int shiftLeft = text.length() + 1;
            return new HandleResult(sb, shiftLeft);
        }
    }
}
