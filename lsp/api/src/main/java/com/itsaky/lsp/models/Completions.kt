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
import java.lang.IllegalArgumentException
import java.nio.file.Path
import java.util.*
import kotlin.collections.ArrayList

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
            throw IllegalArgumentException("Content is required but no content was provided!");
        }
        return content as CharSequence;
    }
    
    fun discardContents () {
        content = null
    }
}

data class CompletionResult (var isIncomplete: Boolean, var items: List<CompletionItem>) {
    constructor() : this (false, ArrayList<CompletionItem>())
    
    override fun toString(): String {
        return TextUtils.join("\n", items)
    }
}

data class CompletionItem(var label: String,
                          var detail: String,
                          var insertText: String?,
                          var insertTextFormat: InsertTextFormat?,
                          var sortText: String?,
                          var command: Command?,
                          var kind: CompletionItemKind,
                          var additionalTextEdits: List<TextEdit>?,
                          var data: CompletionData?) {
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
    
    override fun toString(): String {
        return "CompletionItem(label='$label', detail='$detail', insertText='$insertText', insertTextFormat=$insertTextFormat, sortText='$sortText', command=$command, kind=$kind, data=$data)"
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

data class Command (var title: String, var command: String) {
    constructor() : this ("", "")
}

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