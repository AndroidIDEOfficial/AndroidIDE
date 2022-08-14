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
import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService
import com.itsaky.androidide.lsp.java.utils.ScopeHelper
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.MatchLevel
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.progress.ProgressManager.Companion.abortIfCancelled
import com.sun.source.tree.MemberSelectTree
import com.sun.source.tree.Scope
import com.sun.source.util.TreePath
import com.sun.source.util.Trees
import java.nio.file.Path
import javax.lang.model.element.ElementKind.CONSTRUCTOR
import javax.lang.model.element.ElementKind.METHOD
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeVariable

/**
 * Completions for member select.
 *
 * @author Akash Yadav
 */
class MemberSelectCompletionProvider(
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
    val select = path.leaf as MemberSelectTree
    log.info("...complete members of " + select.expression)

    val exprPath = TreePath(path, select.expression)
    val isStatic = trees.getElement(exprPath) is TypeElement
    val scope = trees.getScope(exprPath)

    abortIfCancelled()
    abortCompletionIfCancelled()
    return when (val type = trees.getTypeMirror(exprPath)) {
      is ArrayType -> completeArrayMemberSelect(isStatic, partial)
      is TypeVariable ->
        completeTypeVariableMemberSelect(task, scope, type, isStatic, partial, endsWithParen)
      is DeclaredType ->
        completeDeclaredTypeMemberSelect(task, scope, type, isStatic, partial, endsWithParen)
      else -> CompletionResult.EMPTY
    }
  }

  private fun completeArrayMemberSelect(
    isStatic: Boolean,
    partialName: CharSequence
  ): CompletionResult {
    return if (isStatic) {
      abortIfCancelled()
      abortCompletionIfCancelled()
      CompletionResult.EMPTY
    } else {
      val list = mutableListOf<CompletionItem>()
      list.add(keyword("length", partialName, 100))
      CompletionResult(list)
    }
  }

  private fun completeTypeVariableMemberSelect(
    task: CompileTask,
    scope: Scope,
    type: TypeVariable,
    isStatic: Boolean,
    partial: String,
    endsWithParen: Boolean,
  ): CompletionResult {
    abortIfCancelled()
    abortCompletionIfCancelled()
    return when (type.upperBound) {
      is DeclaredType ->
        completeDeclaredTypeMemberSelect(
          task,
          scope,
          type.upperBound as DeclaredType,
          isStatic,
          partial,
          endsWithParen
        )
      is TypeVariable ->
        completeTypeVariableMemberSelect(
          task,
          scope,
          type.upperBound as TypeVariable,
          isStatic,
          partial,
          endsWithParen
        )
      else -> CompletionResult.EMPTY
    }
  }

  private fun completeDeclaredTypeMemberSelect(
    task: CompileTask,
    scope: Scope,
    type: DeclaredType,
    isStatic: Boolean,
    partialName: String,
    endsWithParen: Boolean,
  ): CompletionResult {
    val trees = Trees.instance(task.task)
    val typeElement = type.asElement() as TypeElement
    val list: MutableList<CompletionItem> = ArrayList()
    val methods = mutableMapOf<String, MutableList<ExecutableElement>>()
    val matchLevels: MutableMap<String, MatchLevel> =
      mutableMapOf()

    abortIfCancelled()
    abortCompletionIfCancelled()
    for (member in task.task.elements.getAllMembers(typeElement)) {
      if (member.kind == CONSTRUCTOR) {
        continue
      }
      val matchLevel = matchLevel(member.simpleName, partialName)
      if (matchLevel == NO_MATCH) {
        continue
      }

      if (!trees.isAccessible(scope, member, type)) {
        continue
      }

      if (isStatic != member.modifiers.contains(STATIC)) {
        continue
      }

      if (member.kind == METHOD) {
        putMethod((member as ExecutableElement), methods)
        matchLevels.putIfAbsent(member.getSimpleName().toString(), matchLevel)
      } else {
        list.add(item(task, member, matchLevel))
      }
    }

    abortIfCancelled()
    abortCompletionIfCancelled()
    for ((key, value) in methods) {
      val matchLevel = matchLevels.getOrDefault(key, NO_MATCH)
      if (matchLevel == NO_MATCH) {
        continue
      }

      list.add(method(task, value, !endsWithParen, matchLevel))
    }

    if (isStatic) {
      list.add(keyword("class", partialName, 100))
    }

    if (!isStatic && isEnclosingClass(type, scope)) {
      list.add(keyword("this", partialName, 100))
      list.add(keyword("super", partialName, 100))
    }

    return CompletionResult(list)
  }

  private fun isEnclosingClass(type: DeclaredType, start: Scope): Boolean {
    for (s in ScopeHelper.fastScopes(start)) {
      // If we reach a static method, stop looking
      val method = s.enclosingMethod
      if (method != null && method.modifiers.contains(STATIC)) {
        return false
      }
      // If we find the enclosing class
      val thisElement = s.enclosingClass
      if (thisElement != null && thisElement.asType() == type) {
        return true
      }
      // If the enclosing class is static, stop looking
      if (thisElement != null && thisElement.modifiers.contains(STATIC)) {
        return false
      }
    }
    return false
  }
}
