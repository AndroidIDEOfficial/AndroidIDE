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

package io.github.rosemoe.sora.editor.ts

import com.itsaky.androidide.treesitter.TSInputEdit
import com.itsaky.androidide.treesitter.TSParser
import com.itsaky.androidide.treesitter.TSQueryCursor
import com.itsaky.androidide.treesitter.TSTree
import com.itsaky.androidide.treesitter.api.TreeSitterInputEdit
import com.itsaky.androidide.treesitter.api.TreeSitterQueryCapture
import com.itsaky.androidide.treesitter.api.safeExecQueryCursor
import com.itsaky.androidide.treesitter.string.UTF16StringFactory
import com.itsaky.androidide.utils.ILogger
import io.github.rosemoe.sora.data.ObjectAllocator
import io.github.rosemoe.sora.editor.ts.spans.TsSpanFactory
import io.github.rosemoe.sora.lang.analysis.StyleReceiver
import io.github.rosemoe.sora.lang.styling.CodeBlock
import io.github.rosemoe.sora.lang.styling.Styles
import io.github.rosemoe.sora.text.ContentReference
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException
import java.util.concurrent.LinkedBlockingQueue

/**
 * @author Akash Yadav
 */
internal class TsAnalyzeWorker(
  private val analyzer: TsAnalyzeManager,
  private val languageSpec: TsLanguageSpec,
  private val theme: TsTheme,
  private val styles: Styles,
  private val reference: ContentReference,
  private val spanFactory: TsSpanFactory
) {

  companion object {

    private val log = ILogger.newInstance("TsAnalyzeWorker")
  }

  var stylesReceiver: StyleReceiver? = null

  private val analyzerScope = CoroutineScope(Dispatchers.Default + CoroutineName("TsAnalyzeWorker"))
  private val messageChannel = LinkedBlockingQueue<Message<*>>()
  private var analyzerJob: Job? = null

  private var isInitialized = false

  private var tree: TSTree? = null
  private val localText = UTF16StringFactory.newString()
  private val parser = TSParser.create().also {
    it.language = languageSpec.language
  }

  fun init(init: Init) {
    messageChannel.offer(init)
  }

  fun onMod(mod: Mod) {
    messageChannel.offer(mod)
  }

  fun stop() {
    messageChannel.clear()
    analyzerJob?.cancel(CancellationException("Requested to be stopped"))
    localText.close()
    tree?.close()
    parser.close()
  }

  fun start() {
    analyzerJob = analyzerScope.launch(Dispatchers.Default) {
      while (isActive) {
        processNextMessage()
      }
    }.also { job ->
      job.invokeOnCompletion { error ->
        if (error != null && error !is CancellationException) {
          log.error("Analyzer job failed", error)
        } else {
          log.info("Analyzer job completed")
        }
      }
    }
  }

  private suspend fun processNextMessage() {
    val message = withContext(Dispatchers.IO) { messageChannel.take() }

    try {
      when (message) {
        is Init -> doInit(message)
        is Mod -> doMod(message)
      }
    } catch (err: Throwable) {
      val langName = languageSpec.language.name
      val msgType = message.javaClass.simpleName
      val msgTypeSuffix = if (message is Mod) {
        "[start=${message.data.start}, end=${message.data.end}, type=${if (message.data.changedText == null) "delete" else "insert"}]"
      } else ""
      val pendingMsgs = messageChannel.size
      log.error(
        "AnalyzeWorker[lang=$langName, message=${msgType}${msgTypeSuffix}], pendingMsgs=$pendingMsgs] crashed",
        err)
    }
  }

  private fun doInit(init: Init) {
    if (parser.isParsing) {
      parser.requestCancellationAndWait()
    }

    check(!isInitialized) {
      "'Init' must be the first message to TsAnalyzeWorker"
    }

    localText.append(init.data)
    tree = parser.parseString(localText)
    updateStyles()

    isInitialized = true
  }

  private fun doMod(mod: Mod) {

    check(isInitialized) {
      "'Init' must be the first message to TsAnalyzeWorker"
    }

    val textMod = mod.data
    val edit = textMod.edit
    val newText = textMod.changedText

    val oldTree = tree!!
    oldTree.edit(edit)

    if (newText == null) {
      localText.deleteBytes(edit.startByte, edit.oldEndByte)
    } else {
      if (textMod.start == localText.length) {
        localText.append(newText)
      } else {
        localText.insert(textMod.start, newText)
      }
    }

    (edit as? TreeSitterInputEdit?)?.recycle()

    if (parser.isParsing) {
      parser.requestCancellationAndWait()
    }

    tree = parser.parseString(oldTree, localText)
    oldTree.close()
    updateStyles()
  }

  private fun updateStyles() {
    if (messageChannel.isNotEmpty()) {
      // more message need to be processed
      return
    }

    val tree = this.tree!!
    val scopedVariables = TsScopedVariables(tree, localText, languageSpec)
    val oldTree = (styles.spans as? LineSpansGenerator?)?.tree
    val copied = tree.copy()

    styles.spans = LineSpansGenerator(
      copied,
      reference.lineCount,
      reference.reference,
      theme,
      languageSpec,
      scopedVariables,
      spanFactory
    )

    val oldBlocks = styles.blocks
    updateCodeBlocks()
    oldBlocks?.also { ObjectAllocator.recycleBlockLines(it) }

    stylesReceiver?.setStyles(analyzer, styles) {
      oldTree?.close()
    }

    stylesReceiver?.updateBracketProvider(analyzer, TsBracketPairs(copied, languageSpec))
  }

  private fun updateCodeBlocks() {
    if (languageSpec.blocksQuery.patternCount == 0
      || !languageSpec.blocksQuery.canAccess()
      || tree?.canAccess() != true
    ) {
      return
    }

    val blocks = mutableListOf<CodeBlock>()
    TSQueryCursor.create().use { cursor ->

      cursor.safeExecQueryCursor(
        query = languageSpec.blocksQuery,
        tree = tree,
        recycleNodeAfterUse = true,
        onClosedOrEdited = { blocks.clear() },
        debugName = "TsAnalyzeManager.updateCodeBlocks()"
      ) { match ->
        if (!languageSpec.blocksPredicator.doPredicate(
            languageSpec.predicates,
            localText,
            match
          )
        ) {
          return@safeExecQueryCursor
        }

        match.captures.forEach { capture ->
          val block = ObjectAllocator.obtainBlockLine()
          var node = capture.node
          val start = node.startPoint

          block.startLine = start.row
          block.startColumn = start.column / 2

          val end = if (languageSpec.blocksQuery.getCaptureNameForId(capture.index)
              .endsWith(".marked")
          ) {
            // Goto last terminal element
            while (node.childCount > 0) {
              node = node.getChild(node.childCount - 1)
            }
            node.startPoint
          } else {
            node.endPoint
          }
          block.endLine = end.row
          block.endColumn = end.column / 2
          if (block.endLine - block.startLine > 1) {
            blocks.add(block)
          }

          (capture as? TreeSitterQueryCapture?)?.recycle()
        }
      }
    }

    val distinct = blocks.asSequence().distinct().toMutableList()
    styles.blocks = distinct
    styles.finishBuilding()
  }
}

internal interface Message<T> {

  val data: T
}

internal data class Init(override val data: String) : Message<String>

internal data class Mod(override val data: TextMod) : Message<TextMod>

internal data class TextMod(
  val start: Int,
  val end: Int,
  val edit: TSInputEdit,
  val changedText: String?
)