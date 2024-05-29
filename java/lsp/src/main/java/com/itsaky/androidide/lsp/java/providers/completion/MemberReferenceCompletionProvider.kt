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
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.MatchLevel
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.progress.ProgressManager.Companion.abortIfCancelled
import jdkx.lang.model.element.ElementKind.METHOD
import jdkx.lang.model.element.ExecutableElement
import jdkx.lang.model.element.Modifier.STATIC
import jdkx.lang.model.element.TypeElement
import jdkx.lang.model.type.ArrayType
import jdkx.lang.model.type.DeclaredType
import jdkx.lang.model.type.TypeVariable
import openjdk.source.tree.MemberReferenceTree
import openjdk.source.tree.Scope
import openjdk.source.util.TreePath
import openjdk.source.util.Trees
import java.nio.file.Path

/**
 * Completions for member reference.
 *
 * @author Akash Yadav
 */
class MemberReferenceCompletionProvider(
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
    val select = path.leaf as MemberReferenceTree
    log.info("...complete methods of {}", select.qualifierExpression)

    val exprPath = TreePath(path, select.qualifierExpression)
    val element = trees.getElement(exprPath)
    val isStatic = element is TypeElement
    val scope = trees.getScope(exprPath)

    abortIfCancelled()
    abortCompletionIfCancelled()
    return when (val type = trees.getTypeMirror(exprPath)) {
      is ArrayType -> completeArrayMemberReference(isStatic, partial)
      is TypeVariable -> completeTypeVariableMemberReference(task, scope, type, isStatic, partial)
      is DeclaredType -> completeDeclaredTypeMemberReference(task, scope, type, isStatic, partial)
      else -> CompletionResult.EMPTY
    }
  }

  private fun completeArrayMemberReference(
    isStatic: Boolean,
    partialName: CharSequence,
  ): CompletionResult {
    abortIfCancelled()
    abortCompletionIfCancelled()
    return if (isStatic) {
      val list = mutableListOf<CompletionItem>()
      list.add(keyword("new", partialName, 100))
      CompletionResult(list)
    } else {
      CompletionResult.EMPTY
    }
  }

  private fun completeTypeVariableMemberReference(
    task: CompileTask,
    scope: Scope,
    type: TypeVariable,
    isStatic: Boolean,
    partial: String,
  ): CompletionResult {
    abortIfCancelled()
    abortCompletionIfCancelled()
    return when (type.upperBound) {
      is DeclaredType ->
        completeDeclaredTypeMemberReference(
          task,
          scope,
          type.upperBound as DeclaredType,
          isStatic,
          partial
        )
      is TypeVariable ->
        completeTypeVariableMemberReference(
          task,
          scope,
          type.upperBound as TypeVariable,
          isStatic,
          partial
        )
      else -> CompletionResult.EMPTY
    }
  }

  private fun completeDeclaredTypeMemberReference(
    task: CompileTask,
    scope: Scope,
    type: DeclaredType,
    isStatic: Boolean,
    partial: String,
  ): CompletionResult {
    abortIfCancelled()
    abortCompletionIfCancelled()
    val trees = Trees.instance(task.task)
    val typeElement = type.asElement() as TypeElement
    val list: MutableList<CompletionItem> = ArrayList()
    val methods: MutableMap<String, MutableList<ExecutableElement>> = mutableMapOf()
    val matchLevels: MutableMap<String, MatchLevel> = HashMap()
    for (member in task.task.elements.getAllMembers(typeElement)) {
      val matchLevel = matchLevel(member.simpleName, partial)
      if (matchLevel == NO_MATCH) {
        continue
      }

      if (member.kind != METHOD) {
        continue
      }

      if (!trees.isAccessible(scope, member, type)) {
        continue
      }

      if (!isStatic && member.modifiers.contains(STATIC)) {
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

      list.add(method(task, value, false, matchLevel, partial))
    }

    if (isStatic) {
      list.add(keyword("new", partial, 100))
    }

    return CompletionResult(list)
  }
}
