package com.itsaky.androidide.language.java;

import com.itsaky.androidide.language.BaseLanguage;
import com.itsaky.androidide.language.java.manager.JavaCharacter;
import com.itsaky.androidide.language.java.parser.JavaLexer;
import com.itsaky.androidide.language.java.parser.JavaParser;
import com.itsaky.androidide.lsp.LSPProvider;
import com.itsaky.androidide.models.AndroidProject;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.interfaces.NewlineHandler;
import io.github.rosemoe.editor.text.TextUtils;
import io.github.rosemoe.editor.widget.SymbolPairMatch;
import java.io.StringReader;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.eclipse.lsp4j.services.LanguageServer;

public class JavaLanguage extends BaseLanguage {
    
	private JavaLanguageAnalyzer analyzer;
	private JavaAutoComplete complete;
	private AndroidProject project;

	public JavaLanguage(AndroidProject project) {
		this.analyzer = new JavaLanguageAnalyzer();
		this.complete = new JavaAutoComplete(this);
		this.project  = project;
	}

    @Override
    public LanguageServer getLanguageServer() {
        return LSPProvider.getServerForLanguage(LSPProvider.LANGUAGE_JAVA);
    }

    @Override
    public String getLanguageCode() {
        return LSPProvider.LANGUAGE_JAVA;
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
		} catch (Throwable e) {}
		return 0;
	}

	@Override
	public SymbolPairMatch getSymbolPairs() {
		return new JavaSymbolPairs();
	}

	@Override
	public boolean useTab() {
		return true;
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
