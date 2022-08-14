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
import com.itsaky.androidide.lsp.java.compiler.CompileTask
import com.itsaky.androidide.lsp.java.compiler.CompilerProvider
import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService
import com.itsaky.androidide.lsp.java.edits.MultipleClassImportEditHandler
import com.itsaky.androidide.lsp.java.parser.ParseTask
import com.itsaky.androidide.lsp.java.utils.EditHelper
import com.itsaky.androidide.lsp.java.utils.EditHelper.repeatSpaces
import com.itsaky.androidide.lsp.java.utils.FindHelper
import com.itsaky.androidide.lsp.java.utils.JavaPoetUtils.Companion.buildMethod
import com.itsaky.androidide.lsp.java.utils.JavaPoetUtils.Companion.print
import com.itsaky.androidide.lsp.java.utils.ScopeHelper
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.InsertTextFormat.SNIPPET
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.progress.ProgressManager.Companion.abortIfCancelled
import com.itsaky.androidide.projects.FileManager
import com.squareup.javapoet.MethodSpec.Builder
import com.sun.source.tree.ClassTree
import com.sun.source.tree.MethodTree
import com.sun.source.tree.Tree.Kind.CLASS
import com.sun.source.util.TreePath
import com.sun.source.util.Trees
import java.nio.file.Path
import java.util.*
import java.util.function.*
import javax.lang.model.element.ElementKind.METHOD
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.tools.JavaFileObject

/**
 * Provides completions using [com.sun.source.tree.Scope].
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
        list.add(overrideIfPossible(task, parentPath, method, endsWithParen, matchLevel))
      } else {
        list.add(item(task, member, matchLevel))
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
   * @param endsWithParen Does the statement at cursor ends with a parenthesis?
   * @return The completion item.
   */
  private fun overrideIfPossible(
    task: CompileTask,
    parentPath: TreePath,
    method: ExecutableElement,
    endsWithParen: Boolean,
    matchLevel: com.itsaky.androidide.lsp.models.MatchLevel,
  ): CompletionItem {
    if (parentPath.leaf.kind != CLASS) {
      // Can only override if the cursor is directly in a class declaration
      return method(task, listOf(method), !endsWithParen, matchLevel)
    }

    abortIfCancelled()
    abortCompletionIfCancelled()
    val types = task.task.types
    val parentElement =
      Trees.instance(task.task).getElement(parentPath)
        ?: // Can't get further information for overriding this method
      return method(task, listOf(method), !endsWithParen, matchLevel)
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
      return method(task, listOf(method), !endsWithParen, matchLevel)
    }

    // Print the method details and the annotations
    // Print the method details and the annotations
    val indent =
      EditHelper.indent(FileManager.getDocumentContents(file), cursor.toInt())
    val builder: Builder
    try {
      builder = buildMethod(method, types, type)
    } catch (error: Throwable) {
      log.error("Cannot override method:", method.simpleName, error.message)
      return method(task, listOf(method), !endsWithParen, matchLevel)
    }

    val imports = mutableSetOf<String>()
    val methodSpec = builder.build()
    var insertText = print(methodSpec, imports, false)
    insertText = insertText.replace("\n", "\n${repeatSpaces(indent)}")

    abortIfCancelled()
    abortCompletionIfCancelled()

    val item = CompletionItem()
    item.setLabel(methodSpec.name)
    item.kind = com.itsaky.androidide.lsp.models.CompletionItemKind.METHOD
    item.detail = method.returnType.toString() + " " + method
    item.sortText = item.label.toString()
    item.insertText = insertText
    item.insertTextFormat = SNIPPET
    item.matchLevel = matchLevel
    item.data = data(task, method, 1)
    if (item.additionalTextEdits == null) {
      item.additionalTextEdits = mutableListOf()
    }

    imports.removeIf { "java.lang." == it || fileImports.contains(it) || filePackage == it }
    item.additionalEditHandler =
      MultipleClassImportEditHandler(imports, fileImports, file)
    return item
  }

  private fun findSource(
    compiler: CompilerProvider,
    task: CompileTask,
    method: ExecutableElement,
  ): MethodTree? {
    val superClass = method.enclosingElement as TypeElement
    val superClassName = superClass.qualifiedName.toString()
    val methodName = method.simpleName.toString()
    val erasedParameterTypes = FindHelper.erasedParameterTypes(task, method)
    val sourceFile: Optional<JavaFileObject> = compiler.findAnywhere(superClassName)
    if (!sourceFile.isPresent) return null
    val parse: ParseTask = compiler.parse(sourceFile.get())
    return FindHelper.findMethod(parse, superClassName, methodName, erasedParameterTypes)
  }
}
