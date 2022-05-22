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

import com.itsaky.lsp.api.IServerSettings
import com.itsaky.lsp.java.compiler.CompileTask
import com.itsaky.lsp.java.compiler.CompilerProvider
import com.itsaky.lsp.models.CompletionItem
import com.itsaky.lsp.models.CompletionResult
import com.itsaky.lsp.models.MatchLevel.NO_MATCH
import com.sun.source.tree.ClassTree
import com.sun.source.tree.CompilationUnitTree
import com.sun.source.tree.MethodTree
import com.sun.source.tree.Tree
import com.sun.source.util.TreePath
import java.nio.file.Path

/**
 * Provides keyword completions.
 *
 * @author Akash Yadav
 */
class KeywordCompletionProvider(
    completingFile: Path,
    cursor: Long,
    compiler: CompilerProvider,
    settings: IServerSettings
) : IJavaCompletionProvider(completingFile, cursor, compiler, settings) {

    override fun doComplete(
        task: CompileTask,
        path: TreePath,
        partial: String,
        endsWithParen: Boolean,
    ): CompletionResult {
        val level: Tree = findKeywordLevel(path)
        var keywords = arrayOf<String>()
        when (level) {
            is CompilationUnitTree -> keywords = TOP_LEVEL_KEYWORDS
            is ClassTree -> keywords = CLASS_BODY_KEYWORDS
            is MethodTree -> keywords = METHOD_BODY_KEYWORDS
        }

        val list = mutableListOf<CompletionItem>()
        for (k in keywords) {
            val matchLevel = matchLevel(k, partial)
            if (matchLevel == NO_MATCH) {
                continue
            }

            list.add(keyword(k, partial, 100))
        }

        return CompletionResult(list)
    }

    private fun findKeywordLevel(treePath: TreePath): Tree {
        var path: TreePath? = treePath
        while (path != null) {
            if (path.leaf is CompilationUnitTree ||
                path.leaf is ClassTree ||
                path.leaf is MethodTree) {
                return path.leaf
            }
            path = path.parentPath
        }
        throw RuntimeException("empty path")
    }

    companion object {
        private val TOP_LEVEL_KEYWORDS =
            arrayOf(
                "package",
                "import",
                "public",
                "private",
                "protected",
                "abstract",
                "class",
                "interface",
                "@interface",
                "extends",
                "implements")
        private val CLASS_BODY_KEYWORDS =
            arrayOf(
                "public",
                "private",
                "protected",
                "static",
                "final",
                "native",
                "synchronized",
                "abstract",
                "default",
                "class",
                "interface",
                "void",
                "boolean",
                "int",
                "long",
                "float",
                "double")
        private val METHOD_BODY_KEYWORDS =
            arrayOf(
                "new",
                "assert",
                "try",
                "catch",
                "finally",
                "throw",
                "return",
                "break",
                "case",
                "continue",
                "default",
                "do",
                "while",
                "for",
                "switch",
                "if",
                "else",
                "instanceof",
                "var",
                "final",
                "class",
                "void",
                "boolean",
                "int",
                "long",
                "float",
                "double")
    }
}
