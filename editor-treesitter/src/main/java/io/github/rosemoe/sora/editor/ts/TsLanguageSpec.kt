/*******************************************************************************
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2023  Rosemoe
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 *
 *     Please contact Rosemoe by email 2073412493@qq.com if you need
 *     additional information or have any questions
 ******************************************************************************/

package io.github.rosemoe.sora.editor.ts

import com.itsaky.androidide.treesitter.TSLanguage
import com.itsaky.androidide.treesitter.TSQuery
import com.itsaky.androidide.treesitter.TSQueryError
import io.github.rosemoe.sora.editor.ts.predicate.Predicator
import io.github.rosemoe.sora.editor.ts.predicate.TsPredicate
import io.github.rosemoe.sora.editor.ts.predicate.builtin.MatchPredicate
import java.io.Closeable

/**
 * Language specification for tree-sitter highlighter. This specification covers language code
 * parsing, highlighting captures and local variable tracking descriptions.
 *
 * Note that you must use ASCII characters in your scm sources. Otherwise, an [IllegalArgumentException] is
 * thrown.
 * Be careful that this should be closed to avoid native memory leaks.
 *
 * @author Rosemoe
 * @param language The tree-sitter language instance to be used for parsing
 * @param highlightScmSource The scm source code for highlighting tree nodes
 * @param codeBlocksScmSource The scm source for capturing code blocks.
 *                          All captured nodes are considered to be a code block.
 *                          Capture named with '.marked' suffix will have its last terminal node's start position as its scope end
 * @param bracketsScmSource The scm source for capturing brackets. Capture named 'editor.brackets.open' and 'editor.brackets.close' are used to compute bracket pairs
 * @param localsScmSource The scm source code for tracking local variables
 * @param localsCaptureSpec Custom specification for locals scm file
 * @param predicates Client custom predicate implementations
 */
open class TsLanguageSpec(
  val language: TSLanguage,
  highlightScmSource: String,
  codeBlocksScmSource: String = "",
  bracketsScmSource: String = "",
  localsScmSource: String = "",
  localsCaptureSpec: LocalsCaptureSpec = LocalsCaptureSpec.DEFAULT,
  val predicates: List<TsPredicate> = listOf(MatchPredicate)
) : Closeable {

  /**
   * The generated scm source code for querying
   */
  val querySource = localsScmSource + "\n" + highlightScmSource

  /**
   * Offset of highlighting scm source code in [querySource]
   */
  val highlightScmOffset = localsScmSource.encodeToByteArray().size + 1

  /**
   * The actual [TSQuery] object
   */
  val tsQuery = TSQuery.create(language, querySource)

  /**
   * The first index of highlighting pattern
   */
  val highlightPatternOffset: Int

  /**
   * Indices of variable definition patterns
   */
  val localsDefinitionIndices = mutableListOf<Int>()

  /**
   * Indices of variable reference patterns
   */
  val localsReferenceIndices = mutableListOf<Int>()

  /**
   * Indices of variable scope patterns
   */
  val localsScopeIndices = mutableListOf<Int>()

  /**
   * Indices of weak variable scope patterns
   * @see [LocalsCaptureSpec.isMembersScopeCapture] for more information
   */
  val localsMembersScopeIndices = mutableListOf<Int>()

  /**
   * Indices of variable definition-value patterns. Currently unused in analysis.
   */
  val localsDefinitionValueIndices = mutableListOf<Int>()

  val blocksQuery = if (codeBlocksScmSource.isBlank()) {
    TSQuery.EMPTY
  } else TSQuery.create(language, codeBlocksScmSource)

  val bracketsQuery = if (bracketsScmSource.isBlank()) {
    TSQuery.EMPTY
  } else TSQuery.create(language, bracketsScmSource)

  init {
    // Check the queries before access
    try {
      blocksQuery.validateOrThrow("code-blocks")
      bracketsQuery.validateOrThrow("brackets")
      querySource.forEach {
        if (it > 0xFF.toChar()) {
          throw IllegalArgumentException("use non-ASCII characters in scm source is unexpected")
        }
      }
      if (!tsQuery.canAccess()) {
        throw IllegalArgumentException("Syntax highlights query is invalid, errOffset=" + tsQuery.errorOffset + ", errType=" + tsQuery.errorType)
      }
      if (tsQuery.errorType != TSQueryError.None) {
        val region = if (tsQuery.errorOffset < highlightScmOffset) "locals" else "highlight"
        val offset =
          if (tsQuery.errorOffset < highlightScmOffset) tsQuery.errorOffset else tsQuery.errorOffset - highlightScmOffset
        throw IllegalArgumentException(
          "bad scm sources: error ${tsQuery.errorType.name} occurs in $region range at offset $offset")
      }
    } catch (e: IllegalArgumentException) {
      tsQuery.close()
      blocksQuery.close()
      bracketsQuery.close()
      throw e
    }
  }

  val queryPredicator = Predicator(tsQuery)

  val blocksPredicator = Predicator(blocksQuery)

  val bracketsPredicator = Predicator(bracketsQuery)

  /**
   * Close flag
   */
  var closed = false
    private set

  init {
    var highlightOffset = 0
    for (i in 0 until tsQuery.captureCount) {
      // Only locals in localsScm are taken down
      val name = tsQuery.getCaptureNameForId(i)
      if (localsCaptureSpec.isDefinitionCapture(name)) {
        localsDefinitionIndices.add(i)
      } else if (localsCaptureSpec.isReferenceCapture(name)) {
        localsReferenceIndices.add(i)
      } else if (localsCaptureSpec.isScopeCapture(name)) {
        localsScopeIndices.add(i)
      } else if (localsCaptureSpec.isDefinitionValueCapture(name)) {
        localsDefinitionValueIndices.add(i)
      } else if (localsCaptureSpec.isMembersScopeCapture(name)) {
        localsMembersScopeIndices.add(i)
      }
    }
    for (i in 0 until tsQuery.patternCount) {
      if (tsQuery.getStartByteForPattern(i) < highlightScmOffset) {
        highlightOffset++
      }
    }
    highlightPatternOffset = highlightOffset
  }

  override fun close() {
    tsQuery.close()
    blocksQuery.close()
    bracketsQuery.close()
    closed = true
  }

}