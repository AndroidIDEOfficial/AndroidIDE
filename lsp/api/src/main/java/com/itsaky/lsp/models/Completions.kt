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

package com.itsaky.lsp.models

import com.itsaky.androidide.tooling.api.model.IdeModule
import com.itsaky.androidide.utils.ILogger
import com.itsaky.lsp.models.InsertTextFormat.PLAIN_TEXT
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.widget.CodeEditor
import java.nio.file.Path
import kotlin.math.max

data class CompletionParams(var position: Position, var file: Path) {
    var content: CharSequence? = null
    var prefix: String? = null
    var module: IdeModule? = null

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

open class CompletionResult(var isIncomplete: Boolean, items: List<CompletionItem>) {

    var items = items.subList(0, max(MAX_ITEMS, items.size)).toMutableList().apply { sort() }

    companion object {
        const val MAX_ITEMS = 50
        @JvmField val EMPTY = CompletionResult()
    }

    constructor() : this(false, ArrayList<CompletionItem>())

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
    var additionalTextEdits: List<TextEdit>?,
    var data: CompletionData?
) :
    io.github.rosemoe.sora.lang.completion.CompletionItem(label, detail),
    Comparable<CompletionItem> {

    var sortText: String? = sortText
        get() {
            if (field == null) {
                return "$kind$label"
            }

            return "${kind.sortIndex}$field"
        }

    var insertText: String = insertText ?: ""
        get() {
            if (field.isEmpty()) {
                return this.label.toString()
            }

            return field
        }

    var insertTextFormat: InsertTextFormat = insertTextFormat ?: PLAIN_TEXT

    constructor() :
        this(
            "", // label
            "", // detail
            null, // insertText
            null, // insertTextFormat
            null, // sortText
            null, // command
            CompletionItemKind.NONE, // kind
            ArrayList(), // additionalEdits
            null // data
            )

    companion object {
        private val LOG = ILogger.newInstance("CompletionItem")

        @JvmStatic fun sortTextForMatchRatio(ratio: Int, text: CharSequence) = "${100-ratio}$text"
    }

    fun setLabel(label: String) {
        this.label = label
    }

    fun getLabel(): String = this.label as String

    override fun toString(): String {
        return "CompletionItem(label='$label', detail='$detail', insertText='$insertText', insertTextFormat=$insertTextFormat, sortText='$sortText', command=$command, kind=$kind, data=$data)"
    }

    override fun performCompletion(editor: CodeEditor, text: Content, line: Int, column: Int) {
        val start = getIdentifierStart(text.getLine(line), column)
        val insert = if (insertText == null) label else insertText
        val shift = insert!!.contains("$0")

        text.delete(line, start, line, column)

        if (text.contains("\n")) {
            val lines = insert.split("\\\n")
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

        if (additionalTextEdits != null && additionalTextEdits!!.isNotEmpty()) {
            additionalTextEdits!!.forEach {
                val s = it.range.start
                val e = it.range.end
                if (s == e) {
                    editor.text.insert(s.line, s.column, it.newText)
                } else {
                    editor.text.replace(s.line, s.column, e.line, e.column, it.newText)
                }
            }
        }

        executeCommand(editor)
    }

    private fun executeCommand(editor: CodeEditor) {
        try {
            val klass = editor::class.java
            val method = klass.getMethod("executeCommand", Command::class.java)
            method.isAccessible = true
            method.invoke(command)
        } catch (th: Throwable) {
            LOG.error("Unable to invoke 'executeCommand(Command) method in IDEEditor.")
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
        val sortByKind = this.kind.sortIndex.compareTo(other.kind.sortIndex)
        if (sortByKind != 0) {
            return sortByKind
        }

        val thisSortText = this.sortText ?: label
        val thatSortText = other.sortText ?: other.label
        return thisSortText.toString().compareTo(thatSortText.toString())
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

enum class CompletionItemKind private constructor(val sortIndex: Int) {
    CLASS(7),
    INTERFACE(7),
    ANNOTATION_TYPE(7),
    CONSTRUCTOR(6),
    ENUM(7),
    ENUM_MEMBER(2),
    PROPERTY(2),
    FUNCTION(6),
    METHOD(5),
    FIELD(2),
    VARIABLE(1),
    MODULE(8),
    SNIPPET(0),
    TYPE_PARAMETER(3),
    VALUE(4),
    KEYWORD(0),
    NONE(100)
}

enum class InsertTextFormat {
    PLAIN_TEXT,
    SNIPPET
}
