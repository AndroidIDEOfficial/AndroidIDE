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

package com.itsaky.lsp.java.providers.completion

import com.itsaky.lsp.java.FileStore
import com.itsaky.lsp.java.parser.ParseTask
import com.itsaky.lsp.models.CompletionItem
import com.itsaky.lsp.models.CompletionItemKind.SNIPPET
import com.itsaky.lsp.models.CompletionResult
import com.itsaky.lsp.models.InsertTextFormat
import com.itsaky.lsp.models.MatchLevel.CASE_INSENSITIVE_EQUAL
import com.sun.source.tree.CompilationUnitTree
import com.sun.source.tree.Tree.Kind.ERRONEOUS
import java.nio.file.Path
import java.nio.file.Paths

/** @author Akash Yadav */
class TopLevelSnippetsProvider {
    fun complete(task: ParseTask, result: CompletionResult) {
        val file = Paths.get(task.root.sourceFile.toUri())
        if (!hasTypeDeclaration(task.root)) {
            result.add(classSnippet(file))
            if (task.root.getPackage() == null) {
                result.add(packageSnippet(file))
            }
        }
    }

    private fun hasTypeDeclaration(root: CompilationUnitTree): Boolean {
        for (tree in root.typeDecls) {
            if (tree.kind != ERRONEOUS) {
                return true
            }
        }
        return false
    }

    private fun classSnippet(file: Path): CompletionItem {
        var name = file.fileName.toString()
        name = name.substring(0, name.length - ".java".length)
        return snippetItem("class $name", "class $name {\n    $0\n}")
    }

    private fun packageSnippet(file: Path): CompletionItem {
        val name = FileStore.suggestedPackageName(file)
        return snippetItem("package $name", "package $name;\n\n")
    }

    private fun snippetItem(label: String, snippet: String): CompletionItem {
        val item = CompletionItem()
        item.setLabel(label)
        item.kind = SNIPPET
        item.insertText = snippet
        item.insertTextFormat = InsertTextFormat.SNIPPET
        item.sortText = label
        item.matchLevel = CASE_INSENSITIVE_EQUAL
        return item
    }
}
