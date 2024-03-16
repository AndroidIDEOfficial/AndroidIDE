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

package com.itsaky.androidide.lsp.java.providers.completion

import com.itsaky.androidide.lsp.api.IServerSettings
import com.itsaky.androidide.lsp.api.describeSnippet
import com.itsaky.androidide.lsp.java.compiler.CompileTask
import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService
import com.itsaky.androidide.lsp.java.edits.MultipleClassImportEditHandler
import com.itsaky.androidide.lsp.java.models.JavaCompletionItem
import com.itsaky.androidide.lsp.java.utils.JavaPoetUtils.Companion.buildMethod
import com.itsaky.androidide.lsp.java.utils.JavaPoetUtils.Companion.print
import com.itsaky.androidide.lsp.java.utils.ScopeHelper
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.InsertTextFormat.SNIPPET
import com.itsaky.androidide.lsp.models.MatchLevel
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.progress.ProgressManager.Companion.abortIfCancelled
import com.squareup.javapoet.MethodSpec.Builder
import jdkx.lang.model.element.ElementKind.METHOD
import jdkx.lang.model.element.ExecutableElement
import jdkx.lang.model.element.Modifier.FINAL
import jdkx.lang.model.element.Modifier.PRIVATE
import jdkx.lang.model.element.Modifier.STATIC
import jdkx.lang.model.type.DeclaredType
import openjdk.source.tree.ClassTree
import openjdk.source.tree.Tree.Kind.CLASS
import openjdk.source.util.TreePath
import openjdk.source.util.Trees
import java.nio.file.Path
import java.util.function.Predicate

/**
 * Provides completions using [openjdk.source.tree.Scope].
 *
 * @author Akash Yadav
 */
class ScopeCompletionProvider(
  completingFile: Path,
  cursor: Long,
  compiler: JavaCompilerService,
  settings: IServerSettings,
) : IJavaCompletionProvider(cursor, completingFile, compiler, settings) {

  override fun doComplete(
    task: CompileTask,
    path: TreePath,
    partial: String,
    endsWithParen: Boolean,
  ): CompletionResult {
    val trees = Trees.instance(task.task)
    val list: MutableList<CompletionItem> = ArrayList()
    val scope = trees.getScope(path)
    val filter =
      Predicate<CharSequence?> {
        if (it == null || it.isEmpty()) {
          return@Predicate false
        }

        var name = it
        if (it.contains('(')) {
          name = it.substring(0, it.lastIndexOf('('))
        }

        return@Predicate matchLevel(name, partial) != NO_MATCH
      }

    abortIfCancelled()
    abortCompletionIfCancelled()
    for (member in ScopeHelper.scopeMembers(task, scope, filter)) {
      var name = member.simpleName.toString()
      if (name.contains('(')) {
        name = name.substring(0, name.lastIndexOf('('))
      }

      val matchLevel = matchLevel(name, partial)

      if (member.kind == METHOD) {
        val method = member as ExecutableElement
        val parentPath = path.parentPath /*method*/.parentPath /*class*/
        list.add(overrideIfPossible(task, parentPath, method, endsWithParen, matchLevel, partial))
      } else {
        list.add(item(task, member, matchLevel))
      }
    }

    log.info("...found  {} scope members", list.size)

    return CompletionResult(list)
  }

  /**
   * Override the given method if it is overridable.
   *
   * @param task The compilation task.
   * @param parentPath The tree path of the parent class.
   * @param method The method to override if possible.
   * @param endsWithParen Does the statement at cursor ends with a parenthesis?
   * @return The completion item.
   */
  private fun overrideIfPossible(
    task: CompileTask,
    parentPath: TreePath,
    method: ExecutableElement,
    endsWithParen: Boolean,
    matchLevel: MatchLevel,
    partial: String
  ): CompletionItem {
    if (parentPath.leaf.kind != CLASS) {
      // Can only override if the cursor is directly in a class declaration
      return method(task, listOf(method), !endsWithParen, matchLevel, partial)
    }

    abortIfCancelled()
    abortCompletionIfCancelled()
    val types = task.task.types
    val parentElement =
      Trees.instance(task.task).getElement(parentPath)
        ?: // Can't get further information for overriding this method
        return method(task, listOf(method), !endsWithParen, matchLevel, partial)
    val type = parentElement.asType() as DeclaredType
    val enclosing = method.enclosingElement
    val isFinalClass = enclosing.modifiers.contains(FINAL)
    val isNotOverridable =
      (method.modifiers.contains(STATIC) ||
          method.modifiers.contains(FINAL) ||
          method.modifiers.contains(PRIVATE))
    if (
      isFinalClass ||
      isNotOverridable ||
      !types.isAssignable(type, enclosing.asType()) ||
      parentPath.leaf !is ClassTree
    ) {
      // Override is not possible
      return method(task, listOf(method), !endsWithParen, matchLevel, partial)
    }

    // Print the method details and the annotations
    // Print the method details and the annotations
    val builder: Builder
    try {
      builder = buildMethod(method, types, type)
    } catch (error: Throwable) {
      log.error("Cannot override method:{} err={}", method.simpleName, error.message)
      return method(task, listOf(method), !endsWithParen, matchLevel, partial)
    }

    val imports = mutableSetOf<String>()
    val methodSpec = builder.build()
    val insertText = print(methodSpec, imports, false)

    abortIfCancelled()
    abortCompletionIfCancelled()

    val item = JavaCompletionItem()
    item.ideLabel = methodSpec.name
    item.completionKind = com.itsaky.androidide.lsp.models.CompletionItemKind.METHOD
    item.detail = method.returnType.toString() + " " + method
    item.ideSortText = item.ideLabel
    item.insertText = insertText
    item.insertTextFormat = SNIPPET
    item.snippetDescription = describeSnippet(partial)
    item.matchLevel = matchLevel
    item.data = data(task, method, 1)
    if (item.additionalTextEdits == null) {
      item.additionalTextEdits = mutableListOf()
    }

    imports.removeIf { "java.lang." == it || fileImports.contains(it) || filePackage == it }
    item.additionalEditHandler = MultipleClassImportEditHandler(imports, fileImports, file)
    return item
  }
}
