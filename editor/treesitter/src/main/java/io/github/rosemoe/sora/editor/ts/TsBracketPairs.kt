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

import com.itsaky.androidide.treesitter.TSQueryCursor
import com.itsaky.androidide.treesitter.TSTree
import com.itsaky.androidide.treesitter.api.TreeSitterQueryCapture
import com.itsaky.androidide.treesitter.api.safeExecQueryCursor
import io.github.rosemoe.sora.lang.brackets.BracketsProvider
import io.github.rosemoe.sora.lang.brackets.PairedBracket
import io.github.rosemoe.sora.text.Content
import kotlin.math.max

class TsBracketPairs(private val tree: TSTree, private val languageSpec: TsLanguageSpec) :
  BracketsProvider {

  companion object {

    val OPEN_NAME = "editor.brackets.open"
    val CLOSE_NAME = "editor.brackets.close"

  }

  override fun getPairedBracketAt(text: Content, index: Int): PairedBracket? {
    if (!languageSpec.bracketsQuery.canAccess() || languageSpec.bracketsQuery.patternCount <= 0 || !tree.canAccess()) {
      return null
    }

    return TSQueryCursor.create().use { cursor ->
      cursor.setByteRange(max(0, index - 1) * 2, index * 2 + 1)

      var matched = false

      return@use cursor.safeExecQueryCursor(query = languageSpec.bracketsQuery, tree = tree,
        recycleNodeAfterUse = true, whileTrue = { !matched },
        debugName = "TsBracketPairs.getPairedBracketAt()") { match ->
        if (!languageSpec.bracketsPredicator.doPredicate(languageSpec.predicates, text, match)) {
          return@safeExecQueryCursor null
        }

        // do not store TSNode instances from the capture
        // this is because the nodes are also recycled with the TSQueryCapture instances
        val positions = IntArray(4) { -1 }

        for (capture in match.captures) {
          val captureName = languageSpec.bracketsQuery.getCaptureNameForId(capture.index)
          if (captureName == OPEN_NAME || captureName == CLOSE_NAME) {
            val node = capture.node
            if (index >= node.startByte / 2 && index <= node.endByte / 2) {
              matched = true
            }
            if (captureName == OPEN_NAME) {
              positions[0] = node.startByte
              positions[1] = node.endByte
            } else {
              positions[2] = node.startByte
              positions[3] = node.endByte
            }
          }

          (capture as? TreeSitterQueryCapture?)?.recycle()
        }

        val startStartByte = positions[0]
        val startEndByte = positions[1]
        val endStartByte = positions[2]
        val endEndByte = positions[3]

        if (matched && positions.find { it == -1 } == null) {
          return@safeExecQueryCursor PairedBracket(startStartByte / 2,
            (startEndByte - startStartByte) / 2, endStartByte / 2, (endEndByte - endStartByte) / 2)
        }

        return@safeExecQueryCursor null
      }
    }
  }

}