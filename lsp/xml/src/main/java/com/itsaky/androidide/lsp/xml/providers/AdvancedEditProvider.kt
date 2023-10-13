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

package com.itsaky.androidide.lsp.xml.providers

import com.itsaky.androidide.eventbus.events.editor.ChangeType
import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent
import com.itsaky.androidide.lsp.api.ILanguageServerRegistry
import com.itsaky.androidide.lsp.models.CodeActionItem
import com.itsaky.androidide.lsp.models.CodeActionKind
import com.itsaky.androidide.lsp.models.DocumentChange
import com.itsaky.androidide.lsp.models.PerformCodeActionParams
import com.itsaky.androidide.lsp.models.TextEdit
import com.itsaky.androidide.lsp.xml.XMLLanguageServer
import com.itsaky.androidide.models.Range
import com.itsaky.androidide.projects.FileManager
import com.itsaky.androidide.treesitter.TSParser
import com.itsaky.androidide.treesitter.TSQuery
import com.itsaky.androidide.treesitter.TSQueryCapture
import com.itsaky.androidide.treesitter.TSQueryCursor
import com.itsaky.androidide.treesitter.TSQueryError
import com.itsaky.androidide.treesitter.TSQueryMatch
import com.itsaky.androidide.treesitter.TSTree
import com.itsaky.androidide.treesitter.xml.TSLanguageXml

/** @author Akash Yadav */
object AdvancedEditProvider {

  const val TAG_INFO_QUERY =
    """
    (empty_element
    "<" @tag.open
    tag_name: (_) @element.name
    "/" @tag.slash
    ">" @tag.close) @element.empty

    (end_tag_element
        (tag_start
            "<" @tag.start.open
            tag_name: (_) @tag.start.name
            ">" @tag.start.close)
        (tag_end
            "<" @tag.end.open
            "/" @tag.end.slash
            tag_name: (_) @tag.end.name
            ">" @tag.end.close)
    ) @element.with_end
  """

  fun onContentChange(event: DocumentChangeEvent) {
    if (event.changeType != ChangeType.INSERT || event.changedText != "/") {
      return
    }

    val content =
      FileManager.getDocumentContents(event.changedFile).let {
        if (it.isBlank()) return
        StringBuilder(it)
      }

    val client =
      ILanguageServerRegistry.getDefault().getServer(XMLLanguageServer.SERVER_ID)?.client ?: return

    val start = event.changeRange.start.requireIndex()
    val (endLine, endCol, end) = event.changeRange.end
    val openSlash = start > 0 && content[start - 1] == '<'

    if (openSlash) {
      // insert tag name after '</' to get a proper syntax tree
      content.insert(end, 'a')
    }

    val edits = mutableListOf<TextEdit>()
    parseXml(content.toString()) { tree ->
      perfromQuery(tree, TAG_INFO_QUERY) { query, matches ->
        val match = matches.findMatchAt(query, start) ?: return@perfromQuery
        val captures = match.captures
        if (!openSlash) {
          closeEmptyElement(captures, query, edits, endLine, endCol)
        } else {
          closeEndTagElement(content, captures, query, edits, endLine, endCol)
        }
      }
    }

    if (edits.isEmpty()) {
      return
    }

    val action = CodeActionItem()
    action.title = "Advanced XML Edit"
    action.kind = CodeActionKind.QuickFix
    action.changes = listOf(DocumentChange(event.changedFile, edits))

    client.performCodeAction(PerformCodeActionParams(async = false, action = action))
  }

  private fun closeEndTagElement(
    content: StringBuilder,
    captures: Array<TSQueryCapture>,
    query: TSQuery,
    edits: MutableList<TextEdit>,
    endLine: Int,
    endCol: Int
  ) {
    val tagName =
      captures
        .find { query.getCaptureNameForId(it.index) == "tag.start.name" }
        ?.let {
          val start = it.node.startByte
          val end = it.node.endByte
          if (start < end) content.substring(start / 2, end / 2) else null
        }
        ?: return

    val closeCapture = captures.find { query.getCaptureNameForId(it.index) == "tag.end.close" }

    var insertText = tagName
    if (closeCapture != null && closeCapture.node.startByte == closeCapture.node.endByte) {
      insertText += ">"
    }

    edits.add(TextEdit(Range.pointRange(endLine, endCol), insertText))
  }

  private fun closeEmptyElement(
    captures: Array<TSQueryCapture>,
    query: TSQuery,
    edits: MutableList<TextEdit>,
    endLine: Int,
    endCol: Int
  ) {
    val capture = captures.find { query.getCaptureNameForId(it.index) == "tag.close" } ?: return

    // if start and end byte is same, then this node does not exist
    if (capture.node.startByte == capture.node.endByte) {
      edits.add(TextEdit(Range.pointRange(endLine, endCol), ">"))
    }
  }

  private fun parseXml(content: String, action: (TSTree) -> Unit) {
    TSParser.create().use { parser ->
      parser.language = TSLanguageXml.getInstance()
      parser.parseString(content).use(action)
    }
  }

  private fun perfromQuery(
    tree: TSTree,
    queryString: String,
    action: (TSQuery, List<TSQueryMatch>) -> Unit
  ) {
    TSQuery.create(TSLanguageXml.getInstance(), queryString).use { query ->
      if (!query.canAccess() || query.errorType != TSQueryError.None) {
        throw RuntimeException("Invalid query. Please open an issue on GitHub.")
      }

      TSQueryCursor.create().use { cursor ->
        cursor.exec(query, tree.rootNode)
        val matches = mutableListOf<TSQueryMatch>()
        var match: TSQueryMatch? = cursor.nextMatch()
        while (match != null) {
          matches.add(match)
          match = cursor.nextMatch()
        }
        action(query, matches)
      }
    }
  }
}

private fun List<TSQueryMatch>.findMatchAt(query: TSQuery, index: Int): TSQueryMatch? {
  for (match in this) {
    for (capture in match.captures) {
      val name = query.getCaptureNameForId(capture.index)
      if (
        (capture.node.startByte / 2) == index && (name == "tag.slash" || name == "tag.end.slash")
      ) {
        return match
      }
    }
  }

  return null
}

// abstract class Tag(val name: String)
//
// class EmptyElement(name: String, val open: TSPoint, val slash: TSPoint, val close: TSPoint) :
//  Tag(name)
//
// class EndTagElemetn(
//  name: String,
//  val startOpen: TSPoint,
//  val startClose: TSPoint,
//  val endOpen: TSPoint,
//  val endSlash: TSPoint,
//  val endName: String,
//  val endClose: TSPoint
// )
