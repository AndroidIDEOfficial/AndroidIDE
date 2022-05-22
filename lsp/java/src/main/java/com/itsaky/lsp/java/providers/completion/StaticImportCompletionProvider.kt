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
import com.itsaky.lsp.java.providers.CompletionProvider
import com.itsaky.lsp.models.CompletionItem
import com.itsaky.lsp.models.CompletionResult
import com.itsaky.lsp.models.MatchLevel
import com.itsaky.lsp.models.MatchLevel.NO_MATCH
import com.sun.source.tree.CompilationUnitTree
import com.sun.source.tree.MemberSelectTree
import com.sun.source.util.TreePath
import com.sun.source.util.Trees
import java.nio.file.Path
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.METHOD
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement

/**
 * Completes static imports.
 *
 * @author Akash Yadav
 */
class StaticImportCompletionProvider(
    completingFile: Path,
    cursor: Long,
    compiler: CompilerProvider,
    settings: IServerSettings,
    val root: CompilationUnitTree,
) : IJavaCompletionProvider(completingFile, cursor, compiler, settings) {

    override fun doComplete(
        task: CompileTask,
        path: TreePath,
        partial: String,
        endsWithParen: Boolean,
    ): CompletionResult {
        val list = mutableListOf<CompletionItem>()
        val trees = Trees.instance(task.task)
        val methods = mutableMapOf<String, MutableList<ExecutableElement>>()
        val matchRatios: MutableMap<String, MatchLevel> = mutableMapOf()
        val previousSize: Int = list.size
        outer@ for (i in root.imports) {
            if (!i.isStatic) {
                continue
            }

            val id = i.qualifiedIdentifier as MemberSelectTree
            if (!importMatchesPartial(id.identifier, partial)) {
                continue
            }

            val exprPath = trees.getPath(root, id.expression)
            val type = trees.getElement(exprPath) as TypeElement

            for (member in type.enclosedElements) {
                if (!member.modifiers.contains(STATIC)) {
                    continue
                }

                if (!memberMatchesImport(id.identifier, member)) {
                    continue
                }

                val matchLevel = matchLevel(member.simpleName, partial)
                if (matchLevel == NO_MATCH) {
                    continue
                }

                if (member.kind == METHOD) {
                    putMethod(member as ExecutableElement, methods)
                    matchRatios.putIfAbsent(member.simpleName.toString(), matchLevel)
                } else {
                    list.add(item(task, member, matchLevel))
                }
                if (list.size + methods.size > CompletionProvider.MAX_COMPLETION_ITEMS) {
                    break@outer
                }
            }
        }

        for ((key, value) in methods) {
            val matchLevel = matchRatios.getOrDefault(key, NO_MATCH)
            if (matchLevel == NO_MATCH) {
                continue
            }

            list.add(method(task, value, !endsWithParen, matchLevel))
        }

        log.info("...found " + (list.size - previousSize) + " static imports")

        return CompletionResult(list)
    }

    private fun importMatchesPartial(staticImport: Name, partial: String): Boolean {
        return (staticImport.contentEquals("*") || matchLevel(staticImport, partial) != NO_MATCH)
    }

    private fun memberMatchesImport(staticImport: Name, member: Element): Boolean {
        return staticImport.contentEquals("*") || staticImport.contentEquals(member.simpleName)
    }
}
