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

package com.itsaky.androidide.lsp.java.parser.ts

import com.itsaky.androidide.treesitter.TSQuery
import com.itsaky.androidide.treesitter.TSQueryCursor
import com.itsaky.androidide.treesitter.TSQueryMatch
import com.itsaky.androidide.treesitter.TSTree
import com.itsaky.androidide.treesitter.java.TSLanguageJava

/**
 * Helper class to prune method bodies in Java source code using.
 *
 * @author Akash Yadav
 */
object TSMethodPruner {

  private const val METHOD_BODIES_QUERY = "(method_declaration body: (block) @method.body)"

  fun prune(content: StringBuilder, tree: TSTree, cursor: Int) {
    val root = tree.rootNode
    TSQuery.create(TSLanguageJava.getInstance(), METHOD_BODIES_QUERY).use { query ->
      check(query.canAccess()) { "Invalid method bodies query" }
      TSQueryCursor.create().use { queryCursor ->
        queryCursor.exec(query, root)

        var match: TSQueryMatch? = queryCursor.nextMatch()
        while (match != null) {
          val capture = match.captures[0]
          val start = capture.node.startByte / 2
          val end = capture.node.endByte / 2

          if (cursor in start until end) {
            // cursor is located in this method, so do not prune
            match = queryCursor.nextMatch()
            continue
          }
          
          // +1 and -1 to avoid removing the curly braces from the body
          eraseRegion(content, start + 1, end - 1)
          match = queryCursor.nextMatch()
        }
      }
    }
  }

  private fun eraseRegion(content: StringBuilder, start: Int, end: Int) {
    for (i in start until end) {
      if (!content[i].isWhitespace()) {
        content.setCharAt(i, ' ')
      }
    }
  }
}
