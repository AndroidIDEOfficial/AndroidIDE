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
package com.itsaky.androidide.lexers.xml

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.lexers.BaseLexerTest
import com.itsaky.androidide.lexers.xml.XMLLexer.CLOSE
import com.itsaky.androidide.lexers.xml.XMLLexer.COMMENT
import com.itsaky.androidide.lexers.xml.XMLLexer.COMMENT_END
import com.itsaky.androidide.lexers.xml.XMLLexer.COMMENT_MODE
import com.itsaky.androidide.lexers.xml.XMLLexer.COMMENT_START
import com.itsaky.androidide.lexers.xml.XMLLexer.CommentModeEnd
import com.itsaky.androidide.lexers.xml.XMLLexer.CommentText
import com.itsaky.androidide.lexers.xml.XMLLexer.DEFAULT_MODE
import com.itsaky.androidide.lexers.xml.XMLLexer.EQUALS
import com.itsaky.androidide.lexers.xml.XMLLexer.Name
import com.itsaky.androidide.lexers.xml.XMLLexer.OPEN
import com.itsaky.androidide.lexers.xml.XMLLexer.OPEN_SLASH
import com.itsaky.androidide.lexers.xml.XMLLexer.SLASH_CLOSE
import com.itsaky.androidide.lexers.xml.XMLLexer.STRING
import com.itsaky.androidide.lexers.xml.XMLLexer.TEXT
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/** @author Akash Yadav */
@RunWith(JUnit4::class)
class XMLLexerTest : BaseLexerTest() {

  @Test
  fun emptyClosingTagTest() {
    val lexer = XMLLexer(createStream("<tag attr=\"value\"/>"))
    val tokens = lexer.allTokens
    ensureTokenSequence(tokens, listOf(OPEN, Name, Name, EQUALS, STRING, SLASH_CLOSE))
  }

  @Test
  fun nonEmptyClosingTagTest() {
    val lexer = XMLLexer(createStream("<tag attr=\"value\"></tag>"))
    val tokens = lexer.allTokens
    ensureTokenSequence(
      tokens,
      listOf(OPEN, Name, Name, EQUALS, STRING, CLOSE, OPEN_SLASH, Name, CLOSE)
    )
  }

  @Test
  fun singleLineIncompleteTokensTest() {
    val lexer = XMLLexer(createStream("<tag attr=\"value\""))
    val tokens = lexer.allTokens
    ensureTokenSequence(tokens, listOf(OPEN, Name, Name, EQUALS, STRING))
  }

  @Test
  fun wholeCommentText() {
    val lexer = XMLLexer(createStream("<!-- This is a whole comment -->"))
    val tokens = lexer.allTokens
    ensureTokenSequence(tokens, listOf(COMMENT))
  }

  @Test
  fun commentStartTest() {
    val lexer = XMLLexer(createStream("<!--"))
    val tokens = lexer.allTokens
    ensureTokenSequence(tokens, listOf(COMMENT_START))
    assertThat(lexer._mode).isEqualTo(COMMENT_MODE)
  }

  @Test
  fun commentEndTest() {
    val lexer = XMLLexer(createStream("-->"))
    val tokens = lexer.allTokens
    ensureTokenSequence(tokens, listOf(COMMENT_END))
  }

  @Test
  fun incompleteCommentStartTest() {
    val lexer = XMLLexer(createStream("<!-- Comment text"))
    val tokens = lexer.allTokens
    ensureTokenSequence(tokens, listOf(COMMENT_START, CommentText))
    assertThat(lexer._mode).isEqualTo(COMMENT_MODE)
  }

  @Test
  fun incompleteCommentStartAfterTextTest() {
    val lexer = XMLLexer(createStream("Not in comment <!-- Comment text"))
    val tokens = lexer.allTokens
    ensureTokenSequence(tokens, listOf(TEXT, COMMENT_START, CommentText))
    assertThat(lexer._mode).isEqualTo(COMMENT_MODE)
  }

  @Test
  fun incompleteCommentEndTest() {
    val lexer = XMLLexer(createStream("Comment text -->"))
    lexer.pushMode(COMMENT_MODE)
    val tokens = lexer.allTokens
    ensureTokenSequence(tokens, listOf(CommentText, CommentModeEnd))
    assertThat(lexer._mode).isEqualTo(DEFAULT_MODE)
  }

  @Test
  fun incompleteCommentEndBeforeTextTest() {
    val lexer = XMLLexer(createStream("Comment text --> Not in comment"))
    lexer.pushMode(COMMENT_MODE)
    val tokens = lexer.allTokens
    ensureTokenSequence(tokens, listOf(CommentText, CommentModeEnd, TEXT))
    assertThat(lexer._mode).isEqualTo(DEFAULT_MODE)
  }
}
