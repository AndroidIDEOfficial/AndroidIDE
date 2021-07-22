package com.itsaky.androidide.syntax.lexer;

import com.itsaky.androidide.syntax.lexer.tokens.Token;
import com.itsaky.androidide.syntax.lexer.tokens.TokenType;

public abstract class DefaultLexer implements Lexer {
	
	@Override
	public Token token(TokenType type) {
		return new Token(type, text(), line(), column());
	}
	
	@Override
	public Token token(TokenType type, int p) {
		return new Token(type, text(), line(), column(), p);
	}
}
