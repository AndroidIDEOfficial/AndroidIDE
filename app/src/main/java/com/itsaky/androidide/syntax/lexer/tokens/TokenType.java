package com.itsaky.androidide.syntax.lexer.tokens;

public enum TokenType {

    OPERATOR,
    KEYWORD,
    IDENTIFIER,
    STRING_LITERAL,
	NUMBER_LITERAL,
	ANNOTATION,
    COMMENT,
    TYPE,
	TEXT,
	WS;
    public boolean isKeyword() {
		return this == TokenType.KEYWORD;
	}
	
	public boolean isOperator() {
		return this == TokenType.OPERATOR;
	}
	
	public boolean isIdentifier() {
		return this == TokenType.IDENTIFIER;
	}
	
	public boolean isLiteral() {
		return this == TokenType.STRING_LITERAL;
	}
	
	public boolean isComment() {
		return this == TokenType.COMMENT;
	}
	
	public boolean isType() {
		return this == TokenType.TYPE;
	}
	
	public boolean isText() {
		return this == TokenType.TEXT;
	}
	
	public boolean isWs() {
		return this == TokenType.WS;
	}
}
