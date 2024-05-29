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

package com.itsaky.androidide.editor.language.treesitter

import androidx.collection.MutableLongObjectMap
import androidx.collection.mutableIntObjectMapOf
import androidx.collection.mutableLongObjectMapOf
import androidx.core.text.trimmedLength
import com.itsaky.androidide.editor.utils.getFirstNodeAtLine
import com.itsaky.androidide.editor.utils.getLastNodeAtLine
import com.itsaky.androidide.editor.utils.previousNonBlankLine
import com.itsaky.androidide.treesitter.TSNode
import com.itsaky.androidide.treesitter.TSParser
import com.itsaky.androidide.treesitter.TSPoint
import com.itsaky.androidide.treesitter.TSQuery
import com.itsaky.androidide.treesitter.TSQueryCapture
import com.itsaky.androidide.treesitter.TSQueryCursor
import com.itsaky.androidide.treesitter.TSQueryMatch
import com.itsaky.androidide.treesitter.TSTree
import com.itsaky.androidide.treesitter.predicate.SetDirectiveHandler
import com.itsaky.androidide.utils.IntPair
import io.github.rosemoe.sora.editor.ts.TsAnalyzeWorker
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.text.TextUtils
import org.slf4j.LoggerFactory
import kotlin.math.max

/**
 * Computes indentation for tree sitter languages using the indents query.
 *
 * This is based on Neovim's implementation of indentation in `nvim-treesitter`.
 *
 * @see <a href="https://github.com/nvim-treesitter/nvim-treesitter/blob/9bc21966f27d48ab8eac4c42d5b130ef6c411304/lua/nvim-treesitter/indent.lua">nvim-treesitter's indent.lua</a>
 * @author Akash Yadav
 */
class TreeSitterIndentProvider(
  private val languageSpec: TreeSitterLanguageSpec,
  private val analyzer: TsAnalyzeWorker,
  private val indentSize: Int
) {

  companion object {

    private const val IDENT_AUTO = "indent.auto"
    private const val IDENT_BEGIN = "indent.begin"
    private const val IDENT_END = "indent.end"
    private const val IDENT_DEDENT = "indent.dedent"
    private const val IDENT_BRANCH = "indent.branch"
    private const val IDENT_IGNORE = "indent.ignore"
    private const val IDENT_ALIGN = "indent.align"
    private const val IDENT_ZERO = "indent.zero"

    private const val IDENT_TYP_COUNT = 8 // increment this when adding a new indent type above

    private val log = LoggerFactory.getLogger(TreeSitterIndentProvider::class.java)
    internal const val INDENTATION_ERR = Int.MIN_VALUE
    internal const val INDENT_ALIGN_ZERO = Int.MIN_VALUE
    internal const val INDENT_AUTO = Int.MAX_VALUE

    private val DELIMITER_REGEX = Regex("""[\-.+\[\]()$^\\?*]""")
  }

  fun getIndentsForLines(
    content: Content,
    positions: LongArray,
    default: Int = INDENTATION_ERR
  ): IntArray {
    log.debug("getIndentsForLine(Content({}),{})", content.length,
      positions.joinToString(",") { "${IntPair.getFirst(it)}:${IntPair.getSecond(it)}" })
    val defaultIndents = IntArray(positions.size) { default }

    // not really needed, but just in case
    if (content.isEmpty() || positions.isEmpty()) {
      return defaultIndents
    }

    val document = analyzer.document
    TSParser.create().use { parser ->
      parser.language = document.parser.language

      var closeTree = true
      val tree = if (content.documentVersion == document.version) {
        // avoid converting the content to string if not really needed
        log.info("Re-using cached tree from document version {}", document.version)
        closeTree = false
        document.tree
      } else {
        log.info(
          "Re-parsing content for indentation as document version {} does not match version {}",
          document.version,
          content.documentVersion
        )

        (document.tree?.copy() ?: return defaultIndents).use { copiedTree ->
          parser.parseString(copiedTree, content.toString())
        }
      }

      if (tree == null) {
        log.info("Parsed tree is null, returning default indent: {}", default)
        return defaultIndents
      }

      try {
        return computeIndents(tree, content, positions, defaultIndents)
          .also { indents ->
            log.debug("Computed indents: {}", indents.joinToString(","))
          }
      } finally {
        if (closeTree) {
          tree.close()
        }
      }
    }
  }

  private fun computeIndents(
    tree: TSTree,
    content: Content,
    positions: LongArray,
    defaultIndents: IntArray
  ): IntArray {
    val indentsQuery = languageSpec.indentsQuery ?: run {
      log.info("Cannot compute indents. Indents query is null.")
      return defaultIndents
    }

    val rootNode = tree.rootNode ?: run {
      log.info("Cannot compute indents. Root node is null.")
      return defaultIndents
    }

    return TSQueryCursor.create().use { cursor ->
      cursor.addPredicateHandler(SetDirectiveHandler())
      cursor.exec(indentsQuery, tree.rootNode)

      val indents = getIndents(languageSpec.indentsQuery, cursor)
      return@use IntArray(positions.size) { index ->
        val line = IntPair.getFirst(positions[index])
        val column = IntPair.getSecond(positions[index])
        computeIndentForLine(content, line, column, defaultIndents[index], rootNode, indents)
      }
    }
  }

  private fun computeIndentForLine(
    content: Content,
    line: Int,
    column: Int,
    default: Int,
    rootNode: TSNode,
    indents: IndentsContainer
  ): Int {
    val isEmptyLine = content.getLine(line).trimmedLength() == 0
    var node: TSNode?

    if (isEmptyLine) {
      val prevlnum = content.previousNonBlankLine(line)
      if (prevlnum == -1) {
        log.error("Cannot compute indents. Unable to get previous non-blank line.")
        return default
      } else {
        log.debug("Previous non-blank line: {}", prevlnum)
      }

      var prevline: CharSequence = content.getLine(prevlnum)
      val indentBytes = TextUtils.countLeadingSpaceCount(prevline, indentSize) shl 1
      prevline = prevline.trim()

      // The final position can be trailing spaces, which should not affect indentation
      node = content.getLastNodeAtLine(rootNode, prevlnum,
        (indentBytes + prevline.length shl 1) - 2
      ) ?: run {
        log.error("Unable to get last node at line: {}", prevlnum)
        return default
      }

      // TODO(itsaky): Make this an API
      //    Language defs must be able to specify captures which represent a comment
      if (node.type == "comment") {
        // The final node we capture of the previous line can be a comment node, which should also be ignored
        // Unless the last line is an entire line of comment, ignore the comment range and find the last node again
        val firstNode = content.getFirstNodeAtLine(rootNode, prevlnum, indentBytes)
        val scol = node.startPoint.column
        if (firstNode?.nodeId != node.nodeId) {
          // In case the last captured node is a trailing comment node, re-trim the string
          prevline = prevline.subSequence(0, (scol shr 1) - (indentBytes shr 1)).trim()
          val col = indentBytes + ((prevline.length - 1) shl 1)

          node = content.getLastNodeAtLine(rootNode, prevlnum, col)
        }
      }

      if (indents[IDENT_END]!![node?.nodeId ?: 0] != null) {
        node = content.getFirstNodeAtLine(rootNode, line)
      }
    } else {
      node = content.getFirstNodeAtLine(rootNode, line, column shl 1)
    }

    if (node == null || !node.canAccess()) {
      log.error(
        "Cannot compute indents. Unable to get node at line: {}. node={} node.canAccess={}", line,
        node, node?.canAccess())
      return default
    }

    var indent = 0

    if (indents[IDENT_ZERO]?.containsKey(node.nodeId) == true) {
      // indent.zero: align the node to the start of the line
      log.debug("Zero indent for node: {}", node)
      return INDENT_ALIGN_ZERO
    }

    // map to store whether a given line is already processed
    // this is to ensure that we do not accidentally apply multiple indent levels to the same line
    val processedLines = mutableIntObjectMapOf<Boolean>()

    while (node != null && node.canAccess()) {

      val srow = node.startPoint.line
      val erow = node.endPoint.line

      // do 'auto indent' if not marked as '@indent'
      if (!indents.hasNode(IDENT_BEGIN, node)
        && !indents.hasNode(IDENT_ALIGN, node)
        && indents.hasNode(IDENT_AUTO, node)
        && srow < line
        && line <= erow
      ) {
        log.debug("Auto indent for node: {}", node)
        return INDENT_AUTO
      }

      // Do not indent if we are inside an @ignore block.
      // If a node spans from L1,C1 to L2,C2, we know that lines where L1 < line <= L2 would
      // have their indentations contained by the node.
      if (!indents.hasNode(IDENT_BEGIN, node)
        && indents.hasNode(IDENT_IGNORE, node)
        && srow < line
        && line <= erow
      ) {
        log.debug("Ignore indent for node: {}", node)
        return default
      }

      var isProcessed = false

      if (!processedLines.containsKey(srow)
        && ((indents.hasNode(IDENT_BRANCH, node) && srow == line)
            || (indents.hasNode(IDENT_DEDENT, node) && srow != line))
      ) {
        indent -= indentSize
        isProcessed = true
      }

      // do not indent for nodes that starts-and-ends on same line and starts on target line (lnum)
      val shouldProcess = !processedLines.containsKey(srow)
      var isInError = false
      if (shouldProcess) {
        isInError = node.parent?.let { it.canAccess() && it.hasErrors() } == true
      }

      if (shouldProcess &&
        (indents.hasNode(IDENT_BEGIN, node)
            && (srow != erow || isInError || indents.hasMeta(IDENT_BEGIN, node,
          "indent.immediate"))
            && (srow != line || indents.hasMeta(IDENT_BEGIN, node, "indent.start_at_same_line")))
      ) {
        indent += indentSize
        isProcessed = true
      }

      if (isInError && !indents.hasNode(IDENT_ALIGN, node)) {
        // only when the node is in error, promote the
        // first child's aligned indent to the error node
        // to work around ((ERROR "X" . (_)) @aligned_indent (#set! "delimiter" "AB"))
        // matching for all X, instead set do
        // (ERROR "X" @aligned_indent (#set! "delimiter" "AB") . (_))
        // and we will fish it out here.

        for (i in 0 until node.childCount) {
          val child = node.getChild(i)
          if (indents.hasNode(IDENT_ALIGN, child)) {
            indents[IDENT_ALIGN]!![node.nodeId] = indents[IDENT_ALIGN]!![child.nodeId]!!
            break
          }
        }
      }

      // do not indent for nodes that starts-and-ends on same line and starts on target line (lnum)
      if (shouldProcess
        && indents.hasNode(IDENT_ALIGN, node)
        && (srow != erow || isInError)
        && (srow != line)
      ) {
        val meta = indents.getMeta(IDENT_ALIGN, node)!!
        var oDelimNode: TSNode?
        var oIsLastInLine = false
        var cDelimNode: TSNode?
        var cIsLastInLine = false
        var indentIsAbsolute = false

        if (meta.containsKey("indent.open_delimiter")) {
          val r = findDelimiter(content, node, meta["indent.open_delimiter"]!!)
          oDelimNode = r.first
          oIsLastInLine = r.second
        } else {
          oDelimNode = node
        }

        if (meta.containsKey("indent.close_delimiter")) {
          val r = findDelimiter(content, node, meta["indent.close_delimiter"]!!)
          cDelimNode = r.first
          cIsLastInLine = r.second
        } else {
          cDelimNode = node
        }

        if (oDelimNode != null) {
          val osrow = oDelimNode.startPoint.row
          val oscol = oDelimNode.startPoint.column
          var csrow: Int? = null
          if (cDelimNode != null) {
            csrow = cDelimNode.startPoint.row
          }

          if (oIsLastInLine) {
            // hanging indent (previous line ended with starting delimiter)
            // should be processed like indent
            if (shouldProcess) {
              indent += indentSize
              if (cIsLastInLine) {
                // If current line is outside the range of a node marked with `@aligned_indent`
                // Then its indent level shouldn't be affected by `@aligned_indent` node
                if (csrow != null && csrow < line) {
                  indent = max(indent - indentSize, 0)
                }
              }
            }
          } else {
            // aligned indent
            if (cIsLastInLine && csrow != null && osrow != csrow && csrow < line) {
              // If current line is outside the range of a node marked with `@aligned_indent`
              // Then its indent level shouldn't be affected by `@aligned_indent` node
              indent = max(indent - indentSize, 0)
            } else {
              indent = oscol + (meta.getInt("indent.increment") ?: 1)
              indentIsAbsolute = true
            }
          }

          // deal with final line
          var avoidLastMatchingNext = false
          if (csrow != null && csrow != osrow && csrow == line) {
            // delims end on current line, and are not open and closed same line.
            // then this last line may need additional indent to avoid clashes
            // with the next. `indent.avoid_last_matching_next` controls this behavior,
            // for example this is needed for function parameters.

            avoidLastMatchingNext = meta.getBolean("indent.avoid_last_matching_next")
              ?: false
          }

          if (avoidLastMatchingNext) {
            // last line must be indented more in cases where
            // it would be same indent as next line (we determine this as one
            // width more than the open indent to avoid confusing with any
            // hanging indents)
            val osrowIndent = TextUtils.countLeadingSpaceCount(content.getLine(osrow), indentSize)
            if (indent <= osrowIndent + indentSize) {
              indent += indentSize
            }
          }

          isProcessed = true
          if (indentIsAbsolute) {
            // don't allow further indenting by parent nodes, this is an absolute position
            return indent
          }
        }
      }

      processedLines[srow] = processedLines.getOrDefault(srow, isProcessed)

      node = node.parent
    }

    return indent
  }

  private fun findDelimiter(content: Content, node: TSNode,
    delimiter: String): Pair<TSNode?, Boolean> {
    for (i in 0 until node.childCount) {
      val child = node.getChild(i)
      if (child.type != delimiter) {
        continue
      }

      val start = node.startPoint
      val end = node.endPoint
      val line = content.getLine(start.line)
      val escapedDelim = delimiter.replace(DELIMITER_REGEX, "\\\\$0")
      val trimmedAfterDelim = line.substring((end.column shr 1) + 1)
        .replace(Regex("""[\s$escapedDelim]*"""), "")
      return child to trimmedAfterDelim.isEmpty()
    }

    return null to false
  }

  /**
   * Get the indents from the query.
   *
   * @return The indent captures from the query. The returned map has the following structure :
   * ```
   * map[indentType][node_id] = capture
   * ```
   * where `indentType` is one of the indent types defined in [TreeSitterIndentProvider.Companion]`.IDENT_XXX`
   * and `node_id` is same as [TSNode.getNodeId].
   */
  private fun getIndents(
    query: TSQuery,
    cursor: TSQueryCursor
  ): IndentsContainer {
    val indents = IndentsContainer()

    var match: TSQueryMatch? = cursor.nextMatch()
    while (match != null) {
      for (capture in match.captures) {
        val captureName = query.getCaptureNameForId(capture.index)
        if (!indents.containsKey(captureName)) {
          log.warn("Unknown capture name in indents query: {}", captureName)
          continue
        }

        indents[captureName]!![capture.node.nodeId] = capture to match.metadata
      }
      match = cursor.nextMatch()
    }

    return indents
  }

  private inner class IndentsContainer {

    private val data = HashMap<String, MutableLongObjectMap<Pair<TSQueryCapture, TSQueryMatch.Metadata>>>(
      IDENT_TYP_COUNT)

    init {
      // pre-fill the indents type so we could report any unknown indent types later
      data[IDENT_AUTO] = mutableLongObjectMapOf()
      data[IDENT_BEGIN] = mutableLongObjectMapOf()
      data[IDENT_END] = mutableLongObjectMapOf()
      data[IDENT_DEDENT] = mutableLongObjectMapOf()
      data[IDENT_BRANCH] = mutableLongObjectMapOf()
      data[IDENT_IGNORE] = mutableLongObjectMapOf()
      data[IDENT_ALIGN] = mutableLongObjectMapOf()
      data[IDENT_ZERO] = mutableLongObjectMapOf()
    }

    fun containsKey(key: String): Boolean {
      return data.containsKey(key)
    }

    fun get(type: String, node: TSNode) = get(type, node.nodeId)
    fun get(type: String, nodeId: Long) = data[type]?.get(nodeId)

    fun hasNode(type: String, node: TSNode) = hasNode(type, node.nodeId)
    fun hasNode(type: String, nodeId: Long) = data[type]?.get(nodeId) != null

    fun hasMeta(type: String, node: TSNode, metaKey: String) = hasMeta(type, node.nodeId, metaKey)
    fun hasMeta(type: String, nodeId: Long, metaKey: String) =
      data[type]?.get(nodeId)?.second?.get<Any>(metaKey) != null

    fun getMeta(type: String, node: TSNode) = getMeta(type, node.nodeId)
    fun getMeta(type: String, nodeId: Long) = data[type]?.get(nodeId)?.second

    fun <T : Any?> getMetaValue(type: String, node: TSNode, metaKey: String) =
      getMetaValue<T>(type, node.nodeId, metaKey)

    fun <T : Any?> getMetaValue(type: String, nodeId: Long, metaKey: String) =
      data[type]?.get(nodeId)?.second?.get<T>(metaKey)

    operator fun get(
      key: String): MutableLongObjectMap<Pair<TSQueryCapture, TSQueryMatch.Metadata>>? {
      return data[key]
    }

    operator fun set(key: String,
      value: MutableLongObjectMap<Pair<TSQueryCapture, TSQueryMatch.Metadata>>) {
      data[key] = value
    }
  }
}

/**
 * Alias for [TSPoint.row].
 */
private val TSPoint.line: Int
  get() = this.row

private fun TSQueryMatch.Metadata.getInt(key: String) = getString(key).toIntOrNull()
private fun TSQueryMatch.Metadata.getBolean(key: String) = getString(key).toBooleanStrictOrNull()