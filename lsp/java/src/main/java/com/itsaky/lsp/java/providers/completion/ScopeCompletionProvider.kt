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
import com.itsaky.lsp.java.FileStore
import com.itsaky.lsp.java.compiler.CompileTask
import com.itsaky.lsp.java.compiler.CompilerProvider
import com.itsaky.lsp.java.utils.EditHelper
import com.itsaky.lsp.java.utils.JavaPoetUtils.Companion.buildMethod
import com.itsaky.lsp.java.utils.JavaPoetUtils.Companion.print
import com.itsaky.lsp.java.utils.ScopeHelper
import com.itsaky.lsp.models.CompletionItem
import com.itsaky.lsp.models.CompletionItem.Companion.sortTextForMatchRatio
import com.itsaky.lsp.models.CompletionItemKind
import com.itsaky.lsp.models.CompletionResult
import com.itsaky.lsp.models.InsertTextFormat.SNIPPET
import com.itsaky.lsp.util.StringUtils
import com.sun.source.tree.ClassTree
import com.sun.source.tree.Tree.Kind.CLASS
import com.sun.source.util.TreePath
import com.sun.source.util.Trees
import java.nio.file.Path
import java.util.function.*
import javax.lang.model.element.ElementKind.METHOD
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.type.DeclaredType

/**
 * Provides completions using [com.sun.source.tree.Scope].
 *
 * @author Akash Yadav
 */
class ScopeCompletionProvider(
    completingFile: Path,
    cursor: Long,
    compiler: CompilerProvider,
    settings: IServerSettings
) : IJavaCompletionProvider(completingFile, cursor, compiler, settings) {

    override fun complete(
        task: CompileTask,
        path: TreePath,
        partial: String,
        endsWithParen: Boolean,
    ): CompletionResult {
        val trees = Trees.instance(task.task)
        val list: MutableList<CompletionItem> = ArrayList()
        val scope = trees.getScope(path)
        val filter = Predicate { name: CharSequence? ->
            StringUtils.matchesFuzzy(name, partial, true)
        }
        for (member in ScopeHelper.scopeMembers(task, scope, filter)) {
            val matchRatio =
                fuzzySearchRatio(member.simpleName, partial, settings.shouldMatchAllLowerCase())
            if (member.kind == METHOD) {
                val method = member as ExecutableElement
                val parentPath = path.parentPath /*method*/.parentPath /*class*/
                list.add(
                    overrideIfPossible(
                        task, parentPath, method, partial, endsWithParen, matchRatio))
            } else {
                list.add(item(task, member, partial, matchRatio))
            }
        }

        log.info("...found " + list.size + " scope members")

        return CompletionResult(list)
    }

    /**
     * Override the given method if it is overridable.
     *
     * @param task The compilation task.
     * @param parentPath The tree path of the parent class.
     * @param method The method to override if possible.
     * @param partialName The partial name.
     * @param endsWithParen Does the statement at cursor ends with a parenthesis?
     * @param matchRatio The match ratio for this completion item.
     * @return The completion item.
     */
    private fun overrideIfPossible(
        task: CompileTask,
        parentPath: TreePath,
        method: ExecutableElement,
        partialName: CharSequence,
        endsWithParen: Boolean,
        matchRatio: Int,
    ): CompletionItem {
        if (parentPath.leaf.kind != CLASS) {
            // Can only override if the cursor is directly in a class declaration
            return method(task, listOf(method), !endsWithParen, partialName, matchRatio)
        }
        val types = task.task.types
        val parentElement =
            Trees.instance(task.task).getElement(parentPath)
                ?: // Can't get further information for overriding this method
            return method(task, listOf(method), !endsWithParen, partialName, matchRatio)
        val type = parentElement.asType() as DeclaredType
        val enclosing = method.enclosingElement
        val isFinalClass = enclosing.modifiers.contains(FINAL)
        val isNotOverridable =
            (method.modifiers.contains(STATIC) ||
                method.modifiers.contains(FINAL) ||
                method.modifiers.contains(PRIVATE))
        if (isFinalClass ||
            isNotOverridable ||
            !types.isAssignable(type, enclosing.asType()) ||
            parentPath.leaf !is ClassTree) {
            // Override is not possible
            return method(task, listOf(method), !endsWithParen, partialName, matchRatio)
        }

        // Print the method details and the annotations
        val indent = EditHelper.indent(FileStore.contents(completingFile), this.cursor.toInt())
        val builder = buildMethod(method, types, type)
        val imports = mutableSetOf<String>()
        val methodSpec = builder.build()
        var insertText = print(methodSpec, imports, false)
        insertText = insertText.replace("\n", "\n${EditHelper.repeatSpaces(indent)}")
        val item = CompletionItem()
        item.setLabel(methodSpec.name)
        item.kind = CompletionItemKind.METHOD
        item.detail = method.returnType.toString() + " " + method
        item.sortText = sortTextForMatchRatio(matchRatio, item.label, partialName)
        item.insertText = insertText
        item.insertTextFormat = SNIPPET
        item.data = data(task, method, 1)
        if (item.additionalTextEdits == null) {
            item.additionalTextEdits = java.util.ArrayList()
        }
        if (imports.isNotEmpty()) {
            val fileImports: Set<String> =
                task
                    .root(completingFile)
                    .imports
                    .map { it.qualifiedIdentifier }
                    .map { it.toString() }
                    .toSet()
            for (className in imports) {
                item.additionalTextEdits =
                    EditHelper.addImportIfNeeded(compiler, completingFile, fileImports, className)
            }
        }
        return item
    }
}
