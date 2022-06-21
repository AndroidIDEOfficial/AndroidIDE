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

package com.itsaky.androidide.language.incremental

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.language.java.JavaIncrementalAnalyzeManager
import com.itsaky.androidide.lexers.java.JavaLexer
import io.github.rosemoe.sora.lang.analysis.IncrementalAnalyzeManager.LineTokenizeResult
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
internal class BaseIncrementalAnalyzeManagerTest {

  @Test
  fun testSimpleTokenization() {
    val analyzer = JavaIncrementalAnalyzeManager()
    val result =
      analyzer.tokenizeLine(
        "public static final String THIS_IS_A_STRING = \"This is a string...!\";",
        analyzer.initialState
      )
    println("New state: ${result.state}")
    ensureTokenSequence(
      result,
      listOf(
        JavaLexer.PUBLIC,
        JavaLexer.WS,
        JavaLexer.STATIC,
        JavaLexer.WS,
        JavaLexer.FINAL,
        JavaLexer.WS,
        JavaLexer.IDENTIFIER,
        JavaLexer.WS,
        JavaLexer.IDENTIFIER,
        JavaLexer.WS,
        JavaLexer.ASSIGN,
        JavaLexer.WS,
        JavaLexer.STRING_LITERAL,
        JavaLexer.SEMI
      )
    )
  }

  @Test
  fun testCompleteCommentTokenization() {
    val analyzer = JavaIncrementalAnalyzeManager()
    val result =
      analyzer.tokenizeLine("/*This is a */complete multiline comment", analyzer.initialState)
    assertThat(result.state.state).isEqualTo(LineState.NORMAL)
    ensureTokenSequence(
      result,
      listOf(
        JavaLexer.BLOCK_COMMENT,
        JavaLexer.IDENTIFIER,
        JavaLexer.WS,
        JavaLexer.IDENTIFIER,
        JavaLexer.WS,
        JavaLexer.IDENTIFIER
      )
    )
  }

  @Test
  fun testIncompleteCommentTokenization() {
    val analyzer = JavaIncrementalAnalyzeManager()
    var result =
      analyzer.tokenizeLine("/*This is an incomplete multiline comment", analyzer.initialState)
    println(result.state)
    assertThat(result.state.state).isEqualTo(LineState.INCOMPLETE)

    result =
      analyzer.tokenizeLine("/*This is an incomplete*//* multiline comment", analyzer.initialState)
    println(result.state)
    assertThat(result.state.state).isEqualTo(LineState.INCOMPLETE)

    result =
      analyzer.tokenizeLine("This is an incomplete multiline comment/*", analyzer.initialState)
    println(result.state)
    assertThat(result.state.state).isEqualTo(LineState.INCOMPLETE)
  }

  @Test
  fun testMultilineCommentTokenization() {
    val analyzer = JavaIncrementalAnalyzeManager()

    // Test a simple multiline comment
    run {
      var result = analyzer.tokenizeLine("/** This is an", analyzer.initialState)
      assertThat(result.state.state).isEqualTo(LineState.INCOMPLETE)

      result = analyzer.tokenizeLine(" incomplete comment*/", result.state)
      assertThat(result.state.state).isEqualTo(LineState.NORMAL)
    }

    // Test a multiline comment which is directly followed by tokens of other types.
    run {
      var result = analyzer.tokenizeLine("/** This is an", analyzer.initialState)
      assertThat(result.state.state).isEqualTo(LineState.INCOMPLETE)
      assertThat(result.tokens.last()).isNotNull()
      assertThat(result.tokens.last().type).isEqualTo(JavaLexer.IDENTIFIER)

      result = analyzer.tokenizeLine(" incomplete comment*/ public static void", result.state)
      assertThat(result.state.state).isEqualTo(LineState.NORMAL)
      assertThat(result.tokens.last().type).isEqualTo(JavaLexer.VOID)
      assertThat(result.tokens[result.tokens.lastIndex - 1].type).isEqualTo(JavaLexer.WS)
      assertThat(result.tokens[result.tokens.lastIndex - 2].type).isEqualTo(JavaLexer.STATIC)
    }
  }

  private fun ensureTokenSequence(
    result: LineTokenizeResult<LineState, IncrementalToken>,
    types: List<Int>
  ) {
    for (index in types.indices) {
      assertThat(result.tokens[index].type).isEqualTo(types[index])
    }
  }
}
