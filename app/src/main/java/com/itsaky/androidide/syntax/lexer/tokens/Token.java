package com.itsaky.androidide.syntax.lexer.tokens;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;

public class Token implements Serializable {

	public final String text;
    public final TokenType type;
    public final int line;
	public final int column;
    public final int length;
	
	private static HashMap<String, Integer> BRACKETS;
	
    /**
     * the pair value to use if this token is one of a pair:
     * This is how it is used:
     * The opening part will have a positive number X
     * The closing part will have a negative number X
     * X should be unique for a pair:
     *   e.g. for ( pairValue = +1
     *        for ) pairValue = -1
     */
    public final int pairValue;

    /**
     * Constructs a new token
     */
    public Token(TokenType type, String text, int line, int column) {
        this(type, text, line, column, 0);
    }

    /**
     * Construct a new part of pair token
     */
    public Token(TokenType type, String text, int line, int column, int pairValue) {
        this.type = type;
		this.text = text;
        this.line = line;
		this.column = column;
        this.length = text.length();
        this.pairValue = pairValue;
		this.BRACKETS = brackets();
    }

    @Override
    public boolean equals(Object obj) {
		if (obj instanceof Token) {
            Token token = (Token) obj;
            return ((this.line == token.line) &&
				(this.column == token.column) &&
				(this.length == token.length) &&
				(this.type.equals(token.type)));
        } else {
            return false;
        }
    }
	
	public boolean isBracket() {
		return BRACKETS.containsKey(text);
	}
	
	public boolean isClosingOperator() {
		return pairValue < 0;
	}

    @Override
    public int hashCode() {
        return line + column + length;
    }

    @Override
    public String toString() {
        if (pairValue == 0) {
            return String.format("%s (%d, %d) %d", type, line, column, length);
        } else {
            return String.format("%s (%d, %d) (%d) %d", type, line, column, length, pairValue);
        }
    }
	
	private HashMap<String, Integer> brackets() {
		HashMap<String, Integer> brackets = new HashMap<>();
		brackets.put("(", 1);
		brackets.put(")", -1);
		brackets.put("[", 2);
		brackets.put("]", -2);
		brackets.put("{", 3);
		brackets.put("}", -3);
		brackets.put("<", 4);
		brackets.put(">", -4);
		return brackets;
	}
}
