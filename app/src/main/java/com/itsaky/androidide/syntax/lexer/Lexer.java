package com.itsaky.androidide.syntax.lexer;

import com.itsaky.androidide.syntax.lexer.tokens.Token;
import com.itsaky.androidide.syntax.lexer.tokens.TokenType;
import java.io.IOException;

public interface Lexer {
	
	/**
	 * Must be called before anything else. If you're implementing your own lexer, initialize ANTLR or JFlex Lexer's here.
	 */

	void init() throws IOException;
	
	/**
	 * Line of the current token
	 */
	int line();
	
	/**
	 * Column number of token in current line
	 */
	int column();
	
	/**
	 * Text of the current token
	 */
	String text();
	
	/**
	 * Type of current token
	 */
	TokenType type();
	
	/**
	 * Direct next token
	 */
	Token nextToken();
	
	/**
	 * Create new token from the token type. Uses, line(), column(), text()
	 */
	Token token(TokenType type);
	
	/**
	 * Create new token. Will be used for symbol pairs.
	 */
	Token token(TokenType type, int pairValue);
}
