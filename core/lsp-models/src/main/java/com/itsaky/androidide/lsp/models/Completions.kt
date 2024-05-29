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

import com.itsaky.androidide.fuzzysearch.FuzzySearch
import com.itsaky.androidide.lsp.CancellableRequestParams
import com.itsaky.androidide.lsp.edits.DefaultEditHandler
import com.itsaky.androidide.lsp.edits.IEditHandler
import com.itsaky.androidide.lsp.models.CompletionItemKind.NONE
import com.itsaky.androidide.lsp.models.InsertTextFormat.PLAIN_TEXT
import com.itsaky.androidide.lsp.models.MatchLevel.CASE_INSENSITIVE_EQUAL
import com.itsaky.androidide.lsp.models.MatchLevel.CASE_INSENSITIVE_PREFIX
import com.itsaky.androidide.lsp.models.MatchLevel.CASE_SENSITIVE_EQUAL
import com.itsaky.androidide.lsp.models.MatchLevel.CASE_SENSITIVE_PREFIX
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.lsp.models.MatchLevel.PARTIAL_MATCH
import com.itsaky.androidide.models.Position
import com.itsaky.androidide.progress.ICancelChecker
import io.github.rosemoe.sora.lang.completion.snippet.CodeSnippet
import io.github.rosemoe.sora.text.CharPosition
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.widget.CodeEditor
import java.nio.file.Path
import java.util.function.Consumer

const val DEFAULT_MIN_MATCH_RATIO = 59

data class CompletionParams(
  var position: Position,
  var file: Path,
  override val cancelChecker: ICancelChecker
) : CancellableRequestParams {
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

open class CompletionResult(items: Collection<CompletionItem>) {
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
    @JvmOverloads
    fun mapAndFilter(
      src: CompletionResult,
      partial: String,
      map: Consumer<CompletionItem> = Consumer {}
    ): CompletionResult {
      val newItems = src.items.toMutableList()
      newItems.forEach(map)
      newItems.removeIf { !it.ideLabel.startsWith(partial) }
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
  var ideLabel: String,
  var detail: String,
  insertText: String?,
  insertTextFormat: InsertTextFormat?,
  sortText: String?,
  var command: Command?,
  var completionKind: CompletionItemKind,
  var matchLevel: MatchLevel,
  var additionalTextEdits: List<TextEdit>?,
  var data: ICompletionData?,
  var editHandler: IEditHandler = DefaultEditHandler()
) :
  io.github.rosemoe.sora.lang.completion.CompletionItem(ideLabel, detail), Comparable<CompletionItem> {

  var ideSortText: String? = sortText
    get() {
      if (field == null) {
        return ideLabel
      }

      return field
    }

  var insertText: String = insertText ?: ""
    get() {
      if (field.isEmpty()) {
        return this.ideLabel
      }

      return field
    }

  var insertTextFormat: InsertTextFormat = insertTextFormat ?: PLAIN_TEXT
  var additionalEditHandler: IEditHandler? = null
  var snippetDescription: SnippetDescription? = null
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

    @JvmStatic
    @JvmOverloads
    fun matchLevel(
      candidate: String,
      partial: String,
      minMatchRatio: Int = DEFAULT_MIN_MATCH_RATIO
    ): MatchLevel {
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
      if (ratio > minMatchRatio) {
        return PARTIAL_MATCH
      }

      return NO_MATCH
    }
  }

  override fun performCompletion(editor: CodeEditor, text: Content, position: CharPosition) {
    editHandler.performEdits(this, editor, text, position.line, position.column, position.index)
  }

  override fun performCompletion(editor: CodeEditor, text: Content, line: Int, column: Int) {
    throw UnsupportedOperationException()
  }

  override fun compareTo(other: CompletionItem): Int {
    return CompletionItemComparator.compare(this, other)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is CompletionItem) return false

    if (ideLabel != other.ideLabel) return false
    if (detail != other.detail) return false
    if (command != other.command) return false
    if (completionKind != other.completionKind) return false
    if (matchLevel != other.matchLevel) return false
    if (additionalTextEdits != other.additionalTextEdits) return false
    if (data != other.data) return false
    if (ideSortText != other.ideSortText) return false
    if (insertText != other.insertText) return false
    if (insertTextFormat != other.insertTextFormat) return false
    if (editHandler != other.editHandler) return false
    if (additionalEditHandler != other.additionalEditHandler) return false
    if (overrideTypeText != other.overrideTypeText) return false

    return true
  }

  override fun hashCode(): Int {
    var result = ideLabel.hashCode()
    result = 31 * result + detail.hashCode()
    result = 31 * result + (command?.hashCode() ?: 0)
    result = 31 * result + completionKind.hashCode()
    result = 31 * result + matchLevel.hashCode()
    result = 31 * result + (additionalTextEdits?.hashCode() ?: 0)
    result = 31 * result + (data?.hashCode() ?: 0)
    result = 31 * result + (ideSortText?.hashCode() ?: 0)
    result = 31 * result + insertText.hashCode()
    result = 31 * result + insertTextFormat.hashCode()
    result = 31 * result + editHandler.hashCode()
    result = 31 * result + (additionalEditHandler?.hashCode() ?: 0)
    result = 31 * result + (overrideTypeText?.hashCode() ?: 0)
    return result
  }

  override fun toString(): String {
    return "CompletionItem(label='$ideLabel', detail='$detail', command=$command, kind=$completionKind, matchLevel=$matchLevel, additionalTextEdits=$additionalTextEdits, data=$data, sortText=$ideSortText, insertText='$insertText', insertTextFormat=$insertTextFormat, editHandler=$editHandler, additionalEditHandler=$additionalEditHandler, overrideTypeText=$overrideTypeText)"
  }
}

data class SnippetDescription
@JvmOverloads
constructor(
  val selectedLength: Int,
  val deleteSelected: Boolean = true,
  val snippet: CodeSnippet? = null,
  val allowCommandExecution: Boolean = false
)

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
