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

import android.text.TextUtils
import com.itsaky.androidide.utils.Logger
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.widget.CodeEditor
import java.nio.file.Path
import java.util.regex.Pattern

data class CompletionParams (var position: Position, var file: Path) {
    var content: CharSequence? = null
    var prefix: String? = null
    
    fun requirePrefix () : String {
        if (prefix == null) {
            throw IllegalArgumentException ("Prefix is required but none was provided")
        }
        
        return prefix as String
    }
    
    fun requireContents () : CharSequence {
        if (content == null) {
            throw IllegalArgumentException("Content is required but no content was provided!")
        }
        return content as CharSequence
    }
}

data class CompletionResult (var isIncomplete: Boolean, var items: List<CompletionItem>) {
    constructor() : this (false, ArrayList<CompletionItem>())
    
    override fun toString(): String {
        return TextUtils.join("\n", items)
    }
}

data class CompletionItem(@JvmField var label: String,
                          var detail: String,
                          var insertText: String?,
                          var insertTextFormat: InsertTextFormat?,
                          var sortText: String?,
                          var command: Command?,
                          var kind: CompletionItemKind,
                          var additionalTextEdits: List<TextEdit>?,
                          var data: CompletionData?) : io.github.rosemoe.sora.lang.completion.CompletionItem(label, detail) {
    constructor() : this(
        "",
        "",
        null,
        null,
        null,
        null,
        CompletionItemKind.NONE,
        ArrayList(),
        null
    )
    
    companion object {
        private val LOG = Logger.instance("CompletionItem")
    }
    
    fun setLabel (label: String) {
        this.label = label
    }
    
    fun getLabel () : String = this.label as String
    
    override fun toString(): String {
        return "CompletionItem(label='$label', detail='$detail', insertText='$insertText', insertTextFormat=$insertTextFormat, sortText='$sortText', command=$command, kind=$kind, data=$data)"
    }
    
    override fun performCompletion(editor: CodeEditor, text: Content, line: Int, column: Int) {
        val start = getIdentifierStart(text.getLine(line), column)
        val insert = if (insertText == null) label else insertText
        val shift = insert!!.contains("$0")
        
        text.delete(line, start, line, column)
        
        if (text.contains("\n")) {
            val lines = insert.split(Pattern.quote("\n"))
            var i = 0
            lines.forEach {
                if (i != 0) {
                    editor.commitText("\n")
                }
                editor.commitText(it)
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
        
        if (command != null ) {
            if ("editor.action.triggerParameterHints" == command!!.command) {
                performSignatureHelp(editor)
            }
        }
    }
    
    private fun performSignatureHelp (editor: CodeEditor) {
        // We use reflection to invoke the 'signatureHelp' method in IDEEditor
        // As the IDEEditor class is heavily dependent on the :app module,
        // we cannot declare it as a dependency of this (:lsp:api) module
        // If we do, Gradle will complain about recursive dependencies because :app depends on :lsp:api
        try {
            val clazz = editor.javaClass
            val method = clazz.getMethod("signatureHelp")
            method.isAccessible = true
            method.invoke(editor)
        } catch (e: Throwable) {
            LOG.error("Unable to invoke IDEEditor#signatureHelp()", e)
        }
    }
    
    private fun getIdentifierStart (text: CharSequence, end: Int) : Int {
        
        var start = end
        while (start > 0) {
            if (Character.isJavaIdentifierPart(text[start - 1])) {
                start --
                continue
            }
            
            break
        }
        
        return start
    }
}

data class CompletionData(
    var className: String,
    var memberName: String,
    var erasedParameterTypes: Array<String>,
    var plusOverloads: Int
) {

    constructor() : this ("", "", arrayOf(), -1)

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

data class Command (var title: String, var command: String)

enum class CompletionItemKind {
    CLASS,
    INTERFACE,
    ANNOTATION_TYPE,
    CONSTRUCTOR,
    ENUM,
    ENUM_MEMBER,
    PROPERTY,
    FUNCTION,
    METHOD,
    FIELD,
    VARIABLE,
    MODULE,
    SNIPPET,
    TYPE_PARAMETER,
    VALUE,

    KEYWORD,
    NONE
}

enum class InsertTextFormat {
    PLAIN_TEXT,
    SNIPPET
}