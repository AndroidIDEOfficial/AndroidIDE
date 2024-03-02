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

package com.itsaky.androidide.desugaring.parsing

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.desugaring.internal.parsing.InsnLexer
import com.itsaky.androidide.desugaring.internal.parsing.InsnLexer.Token
import com.itsaky.androidide.desugaring.internal.parsing.InsnLexer.TokenType
import com.itsaky.androidide.desugaring.internal.parsing.InsnParser
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Tests for the [InsnParser].
 *
 * @author Akash Yadav
 */
@RunWith(JUnit4::class)
class InsnLexerTest {

  @Test
  fun `tokenizes empty input`() {
    val lexer = InsnLexer("")
    assertThat(lexer.all()).isEmpty()
  }

  @Test
  fun `tokenizes single identifier`() {
    val lexer = InsnLexer("myIdentifier")
    assertThat(lexer.all()).containsExactly(
      Token(0, 0, 0, TokenType.IDENTIFIER, "myIdentifier"))
  }

  @Test
  fun `tokenizes multiple identifiers`() {
    val lexer = InsnLexer("if else for")
    assertThat(lexer.all()).containsExactly(
      Token(0, 0, 0, TokenType.IDENTIFIER, "if"),
      Token(0, 3, 3, TokenType.IDENTIFIER, "else"),
      Token(0, 8, 8, TokenType.IDENTIFIER, "for"))
  }

  @Test
  fun `tokenizes known symbols`() {
    val lexer = InsnLexer("()[/;=>-")
    assertThat(lexer.all()).containsExactly(
      Token(0, 0, 0, TokenType.LPAR, "("),
      Token(0, 1, 1, TokenType.RPAR, ")"),
      Token(0, 2, 2, TokenType.LBRAC, "["),
      Token(0, 3, 3, TokenType.SLASH, "/"),
      Token(0, 4, 4, TokenType.SEMICOLON, ";"),
      Token(0, 5, 5, TokenType.EQUALS, "="),
      Token(0, 6, 6, TokenType.RANGULAR, ">"),
      Token(0, 7, 7, TokenType.HYPHEN, "-"),
    )
  }

  @Test
  fun `ignores whitespaces`() {
    val lexer = InsnLexer("  \t  ")
    assertThat(lexer.all()).isEmpty()
  }

  @Test
  fun `skips comments`() {
    val lexer = InsnLexer("# This is a comment\nname")
    assertThat(lexer.all()).containsExactly(
      Token(1, 0, 20, TokenType.IDENTIFIER, "name"),
    )
  }

  @Test
  fun `tracks lines, columns and indices`() {
    val lexer = InsnLexer("foo\nbar\nbaz")
    assertThat(lexer.all()).containsExactly(
      Token(0, 0, 0, TokenType.IDENTIFIER, "foo"),
      Token(1, 0, 4, TokenType.IDENTIFIER, "bar"),
      Token(2, 0, 8, TokenType.IDENTIFIER, "baz"),
    )
  }

  @Test
  fun `handles unknown characters`() {
    val lexer = InsnLexer("foo@bar")
    assertThat(lexer.all()).containsExactly(
      Token(0, 0, 0, TokenType.IDENTIFIER, "foo"),
      Token(0, 3, 3, TokenType.UNKNOWN, "@"),
      Token(0, 4, 4, TokenType.IDENTIFIER, "bar"),
    )
  }

  @Test
  fun `test simple method invocation instruction tokenization`() {
    val instruction = """
      invoke-virtual Ljava/io/InputStream;->readNBytes(I)[B
       =>
      invoke-static Lcom/itsaky/androidide/desugaring/sample/java/io/DesugaredInputStream;->readNBytes([Ljava/io/InputStream;I)V
      ;;
    """.trimIndent()

    val lexer = InsnLexer(instruction)
    val tokens = lexer.all()

    assertThat(tokens).isNotEmpty()
    assertThat(
      tokens.find { it.type == TokenType.UNKNOWN || it == Token.EOF }).isNull()
  }

  @Test
  fun `test tokenization for simple instruction with comments`() {
    val instruction =
      "# comment\ninvoke-static Lcom/itsaky/androidide/desugaring/sample/java/io/DesugaredInputStream;->readNBytes([Ljava/io/InputStream;I)V"

    val lexer = InsnLexer(instruction)
    val tokens = lexer.all()

    assertThat(tokens).isNotEmpty()
    assertThat(
      tokens.find { it.type == TokenType.UNKNOWN || it == Token.EOF }).isNull()
  }

  @Test
  fun `test tokenization for simple instruction with comments and CRLF line terminator`() {
    val instruction =
      "# comment\r\ninvoke-static Lcom/itsaky/androidide/desugaring/sample/java/io/DesugaredInputStream;->readNBytes([Ljava/io/InputStream;I)V"

    val lexer = InsnLexer(instruction)
    val tokens = lexer.all()

    assertThat(tokens).isNotEmpty()
    assertThat(
      tokens.find { it.type == TokenType.UNKNOWN || it == Token.EOF }).isNull()
  }
}