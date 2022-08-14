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

package com.itsaky.androidide.lsp.models

import android.os.Looper
import com.blankj.utilcode.util.ThreadUtils
import com.itsaky.androidide.fuzzysearch.FuzzySearch
import com.itsaky.androidide.lsp.edits.IEditHandler
import com.itsaky.androidide.lsp.models.CompletionItemKind.NONE
import com.itsaky.androidide.lsp.models.InsertTextFormat.PLAIN_TEXT
import com.itsaky.androidide.lsp.models.MatchLevel.CASE_INSENSITIVE_EQUAL
import com.itsaky.androidide.lsp.models.MatchLevel.CASE_INSENSITIVE_PREFIX
import com.itsaky.androidide.lsp.models.MatchLevel.CASE_SENSITIVE_EQUAL
import com.itsaky.androidide.lsp.models.MatchLevel.CASE_SENSITIVE_PREFIX
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.lsp.models.MatchLevel.PARTIAL_MATCH
import com.itsaky.androidide.lsp.util.RewriteHelper
import com.itsaky.androidide.models.Position
import com.itsaky.androidide.utils.ILogger
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.widget.CodeEditor
import java.nio.file.Path

const val MIN_MATCH_RATIO = 59

data class CompletionParams(var position: Position, var file: Path) {
  var content: CharSequence? = null
  var prefix: String? = null

  fun requirePrefix(): String {
    if (prefix == null) {
      throw IllegalArgumentException("Prefix is required but none was provided")
    }

    return prefix as String
  }

  fun requireContents(): CharSequence {
    if (content == null) {
      throw IllegalArgumentException("Content is required but no content was provided!")
    }
    return content as CharSequence
  }
}

open class CompletionResult(items: List<CompletionItem>) {
  val items: List<CompletionItem> = run {
    var temp = items.toMutableList()
    temp.sort()
    if (TRIM_TO_MAX && temp.size > MAX_ITEMS) {
      temp = temp.subList(0, MAX_ITEMS)
    }
    return@run temp
  }

  var isIncomplete = this.items.size < items.size
  var isCached = false

  companion object {
    const val MAX_ITEMS = 50
    @JvmField val EMPTY = CompletionResult(listOf())

    var TRIM_TO_MAX = true

    @JvmStatic
    fun filter(src: CompletionResult, partial: String): CompletionResult {
      val newItems = src.items.toMutableList()
      newItems.removeIf { !it.label.startsWith(partial) }
      return CompletionResult(newItems)
    }
  }

  constructor() : this(listOf())

  fun add(item: CompletionItem) {
    if (isIncomplete) {
      // Max limit has been reached
      return
    }

    if (items is MutableList) {
      this.items.add(item)
    }
    this.isIncomplete = this.items.size >= MAX_ITEMS
  }

  fun markCached() {
    this.isCached = true
  }

  override fun toString(): String {
    return android.text.TextUtils.join("\n", items)
  }
}

open class CompletionItem(
  @JvmField var label: String,
  var detail: String,
  insertText: String?,
  insertTextFormat: InsertTextFormat?,
  sortText: String?,
  var command: Command?,
  var kind: CompletionItemKind,
  var matchLevel: MatchLevel,
  var additionalTextEdits: List<TextEdit>?,
  var data: CompletionData?
) :
  io.github.rosemoe.sora.lang.completion.CompletionItem(label, detail), Comparable<CompletionItem> {
  
  var sortText: String? = sortText
    get() {
      if (field == null) {
        return label.toString()
      }

      return field
    }

  var insertText: String = insertText ?: ""
    get() {
      if (field.isEmpty()) {
        return this.label.toString()
      }

      return field
    }

  var insertTextFormat: InsertTextFormat = insertTextFormat ?: PLAIN_TEXT
  var additionalEditHandler: IEditHandler? = null
  var overrideTypeText: String? = null

  constructor() :
    this(
      "", // label
      "", // detail
      null, // insertText
      null, // insertTextFormat
      null, // sortText
      null, // command
      NONE, // kind
      NO_MATCH, // match level
      ArrayList(), // additionalEdits
      null // data
    )

  companion object {
    private val LOG = ILogger.newInstance("CompletionItem")

    @JvmStatic
    fun matchLevel(candidate: String, partial: String): MatchLevel {
      if (candidate.startsWith(partial)) {
        return if (candidate.length == partial.length) {
          CASE_SENSITIVE_EQUAL
        } else {
          CASE_SENSITIVE_PREFIX
        }
      }

      val lowerCandidate = candidate.lowercase()
      val lowerPartial = partial.lowercase()
      if (lowerCandidate.startsWith(lowerPartial)) {
        return if (lowerCandidate.length == lowerPartial.length) {
          CASE_INSENSITIVE_EQUAL
        } else {
          CASE_INSENSITIVE_PREFIX
        }
      }

      val ratio = FuzzySearch.ratio(candidate, partial)
      if (ratio > MIN_MATCH_RATIO) {
        return PARTIAL_MATCH
      }

      return NO_MATCH
    }
  }

  fun setLabel(label: String) {
    this.label = label
  }

  fun getLabel(): String = this.label as String

  override fun performCompletion(editor: CodeEditor, text: Content, line: Int, column: Int) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      ThreadUtils.runOnUiThread { performCompletion(editor, text, line, column) }
      return
    }

    val start = getIdentifierStart(text.getLine(line), column)
    val shift = insertText.contains("$0")

    text.delete(line, start, line, column)

    if (text.contains("\n")) {
      val lines = insertText.split("\\\n")
      var i = 0
      lines.forEach {
        var commit = it
        if (i != 0) {
          commit = "\n" + commit
        }
        editor.commitText(commit)
        i++
      }
    } else {
      editor.commitText(text)
    }

    if (shift) {
      val l = editor.cursor.leftLine
      val t = editor.text.getLineString(l)
      val c = t.lastIndexOf("$0")

      if (c != -1) {
        editor.setSelection(l, c)
        editor.text.delete(l, c, l, c + 2)
      }
    }

    text.beginBatchEdit()
    if (additionalEditHandler != null) {
      additionalEditHandler!!.performEdits(editor, this)
    } else if (additionalTextEdits != null && additionalTextEdits!!.isNotEmpty()) {
      RewriteHelper.performEdits(additionalTextEdits!!, editor)
    }

    text.beginBatchEdit()
    executeCommand(editor)
  }

  private fun executeCommand(editor: CodeEditor) {
    try {
      val klass = editor::class.java
      val method = klass.getMethod("executeCommand", Command::class.java)
      method.isAccessible = true
      method.invoke(editor, command)
    } catch (th: Throwable) {
      LOG.error("Unable to invoke 'executeCommand(Command) method in IDEEditor.", th)
    }
  }

  private fun getIdentifierStart(text: CharSequence, end: Int): Int {
    var start = end
    while (start > 0) {
      if (Character.isJavaIdentifierPart(text[start - 1])) {
        start--
        continue
      }
      break
    }
    return start
  }

  override fun compareTo(other: CompletionItem): Int {
    return CompletionItemComparator.compare(this, other)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is CompletionItem) return false

    if (label != other.label) return false
    if (detail != other.detail) return false
    if (command != other.command) return false
    if (kind != other.kind) return false
    if (matchLevel != other.matchLevel) return false
    if (additionalTextEdits != other.additionalTextEdits) return false
    if (data != other.data) return false
    if (sortText != other.sortText) return false
    if (insertText != other.insertText) return false
    if (insertTextFormat != other.insertTextFormat) return false

    return true
  }

  override fun hashCode(): Int {
    var result = label.hashCode()
    result = 31 * result + detail.hashCode()
    result = 31 * result + (command?.hashCode() ?: 0)
    result = 31 * result + kind.hashCode()
    result = 31 * result + matchLevel.hashCode()
    result = 31 * result + (additionalTextEdits?.hashCode() ?: 0)
    result = 31 * result + (data?.hashCode() ?: 0)
    result = 31 * result + (sortText?.hashCode() ?: 0)
    result = 31 * result + insertText.hashCode()
    result = 31 * result + insertTextFormat.hashCode()
    return result
  }

  override fun toString(): String {
    return "CompletionItem(" +
      "label='$label', " +
      "detail='$detail', " +
      "command=$command, " +
      "kind=$kind, " +
      "matchLevel=$matchLevel, " +
      "additionalTextEdits=$additionalTextEdits, " +
      "data=$data, " +
      "sortText=$sortText, " +
      "insertText='$insertText', " +
      "insertTextFormat=$insertTextFormat" +
      ")"
  }
}

data class CompletionData(
  var className: String,
  var memberName: String,
  var erasedParameterTypes: Array<String>,
  var plusOverloads: Int
) {

  constructor() : this("", "", arrayOf(), -1)

  override fun equals(other: Any?): Boolean {
    if (this === other) {
      return true
    }

    if (javaClass != other?.javaClass) {
      return false
    }

    other as CompletionData

    if (className != other.className) {
      return false
    }

    if (memberName != other.memberName) {
      return false
    }

    if (!erasedParameterTypes.contentEquals(other.erasedParameterTypes)) {
      return false
    }

    if (plusOverloads != other.plusOverloads) {
      return false
    }

    return true
  }

  override fun hashCode(): Int {
    var result = className.hashCode()
    result = 31 * result + memberName.hashCode()
    result = 31 * result + erasedParameterTypes.contentHashCode()
    result = 31 * result + plusOverloads
    return result
  }
}

data class Command(var title: String, var command: String) {
  companion object {

    /** Action for triggering a signature help request to the language server. */
    const val TRIGGER_PARAMETER_HINTS = "editor.action.triggerParameterHints"

    /** Action for triggering a completion request to the language server. */
    const val TRIGGER_COMPLETION = "editor.action.triggerCompletionRequest"

    /** Action for triggering code format action automatically. */
    const val FORMAT_CODE = "editor.action.formatCode"
  }
}

enum class CompletionItemKind {
  KEYWORD,
  VARIABLE,
  PROPERTY,
  FIELD,
  ENUM_MEMBER,
  CONSTRUCTOR,
  METHOD,
  FUNCTION,
  TYPE_PARAMETER,
  CLASS,
  INTERFACE,
  ENUM,
  ANNOTATION_TYPE,
  MODULE,
  SNIPPET,
  VALUE,
  NONE
}

enum class MatchLevel {
  CASE_SENSITIVE_EQUAL,
  CASE_INSENSITIVE_EQUAL,
  CASE_SENSITIVE_PREFIX,
  CASE_INSENSITIVE_PREFIX,
  PARTIAL_MATCH,
  NO_MATCH
}

enum class InsertTextFormat {
  PLAIN_TEXT,
  SNIPPET
}
