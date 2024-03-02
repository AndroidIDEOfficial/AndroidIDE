/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.desugaring.internal.parsing

/**
 * Lexer implementation to tokenize the input for [InsnParser].
 *
 * @author Akash Yadav
 */
internal class InsnLexer(
  private val input: CharSequence
) {

  private val currentWord = StringBuilder(64)
  private var position = 0
  private var line = 0
  private var column = 0

  /**
   * Returns all tokens from the input.
   */
  fun all(): List<Token> {
    val tokens = mutableListOf<Token>()
    while (true) {
      val token = nextToken()
      if (token.isEof()) {
        break
      }
      tokens.add(token)
    }
    return tokens
  }

  fun peek() : Token {
    val pos = this.position
    val line = this.line
    val column = this.column

    val next = nextToken()

    this.position = pos
    this.line = line
    this.column = column

    return next
  }

  /**
   * Returns the next token from the input.
   */
  fun nextToken(): Token {
    currentWord.clear()

    var isIdentifier = false

    while (true) {

      val char = peekInput()
      if (char == null) {
        if (isIdentifier) {
          // EOF while reading an identifier
          return token(TokenType.IDENTIFIER, currentWord.toString())
        }

        return Token.EOF
      }

      if (isIdentifier && !Character.isJavaIdentifierPart(char)) {
        return token(TokenType.IDENTIFIER, currentWord.toString())
      }

      // advance to next character
      advance()

      val schar = char.toString()

      if (isWhitespace(char)) {
        // ignore whitespace
        continue
      }

      when (char) {
        '/' -> return token(TokenType.SLASH, schar)
        '-' -> return token(TokenType.HYPHEN, schar)
        '>' -> return token(TokenType.RANGULAR, schar)
        '(' -> return token(TokenType.LPAR, schar)
        ')' -> return token(TokenType.RPAR, schar)
        '[' -> return token(TokenType.LBRAC, schar)
        '=' -> return token(TokenType.EQUALS, schar)
        ';' -> return token(TokenType.SEMICOLON, schar)

        // skip comments starting with the '#' character
        '#' -> {
          skipUntil(::isNewLine)
          continue
        }

        '\n' -> {
          line += 1
          column = 0
          continue
        }

        '\r' -> continue
      }

      if (Character.isJavaIdentifierStart(char) && !isIdentifier) {
        currentWord.append(char)
        isIdentifier = true
        continue
      }

      if (Character.isJavaIdentifierPart(char) && isIdentifier) {
        currentWord.append(char)
        continue
      }

      return token(TokenType.UNKNOWN, schar)
    }
  }

  private fun token(type: TokenType, value: String): Token {
    val index = position - value.length
    val column = column - value.length

    check(index >= 0) { "Invalid lexer state - index < 0" }
    check(column >= 0) { "Invalid lexer state - column < 0" }

    return Token(line, column, index, type, value)
  }

  private fun advance(): Char? {
    if (position >= input.length) {
      return null
    }

    return input[position++].also {
      ++column
    }
  }

  private fun peekInput(): Char? {
    if (position >= input.length) {
      return null
    }

    return input[position]
  }

  private fun isWhitespace(char: Char): Boolean {
    return char == ' ' || char == '\t'
  }

  private fun isNewLine(char: Char): Boolean {
    return char == '\n' || char == '\r'
  }

  private inline fun skipUntil(predicate: (char: Char) -> Boolean) {
    while (true) {
      val peek = peekInput() ?: break
      if (predicate(peek)) {
        break
      }

      advance()
    }
  }

  data class Token(
    val line: Int,
    val column: Int,
    val index: Int,
    val type: TokenType,
    val text: String
  ) {

    companion object {

      @JvmField
      val EOF = Token(-1, -1, -1, TokenType.EOF, "")
    }

    fun isEof() = type == TokenType.EOF

    fun errDesc() = "token $type at line $line, column $column: $text"
  }

  enum class TokenType { IDENTIFIER,
    LPAR,
    RPAR,
    LBRAC,
    SLASH,
    SEMICOLON,
    EQUALS,
    RANGULAR,
    HYPHEN,
    UNKNOWN,
    EOF
  }
}