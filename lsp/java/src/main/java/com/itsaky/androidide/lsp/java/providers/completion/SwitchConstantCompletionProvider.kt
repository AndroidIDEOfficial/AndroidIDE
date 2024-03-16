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
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.progress.ProgressManager.Companion.abortIfCancelled
import jdkx.lang.model.element.ElementKind.ENUM
import jdkx.lang.model.element.ElementKind.ENUM_CONSTANT
import jdkx.lang.model.element.TypeElement
import jdkx.lang.model.type.DeclaredType
import openjdk.source.tree.SwitchTree
import openjdk.source.util.TreePath
import openjdk.source.util.Trees
import java.nio.file.Path

/**
 * Provides completions for switch constants.
 *
 * @author Akash Yadav
 */
class SwitchConstantCompletionProvider(
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
    val switchTree = path.leaf as SwitchTree
    val exprPath = TreePath(path, switchTree.expression)
    val type = Trees.instance(task.task).getTypeMirror(exprPath)

    if (type.kind.isPrimitive || type !is DeclaredType) {
      // primitive types do not have any members
      return completeIdentifier(task, exprPath, partial, endsWithParen)
    }

    val element = type.asElement() as TypeElement

    if (element.kind != ENUM) {
      // If the switch's expression is not an enum type
      // we will not get any constants to complete
      // In this case, we fall back to completing identifiers
      // At this point, we are sure that the case expression will definitely be an identifier
      // tree
      // see visitCase (CaseTree, Long) in FindCompletionsAt.java
      return completeIdentifier(task, exprPath, partial, endsWithParen)
    }

    log.info("...complete constants of type {}", type)

    val list: MutableList<com.itsaky.androidide.lsp.models.CompletionItem> = ArrayList()

    abortIfCancelled()
    abortCompletionIfCancelled()

    for (member in task.task.elements.getAllMembers(element)) {
      if (member.kind != ENUM_CONSTANT) {
        continue
      }

      val matchLevel = matchLevel(member.simpleName, partial)
      if (matchLevel == NO_MATCH) {
        continue
      }

      list.add(item(task, member, matchLevel))
    }

    return CompletionResult(list)
  }

  private fun completeIdentifier(
    task: CompileTask,
    path: TreePath,
    partial: String,
    endsWithParen: Boolean
  ): CompletionResult {
    abortIfCancelled()
    abortCompletionIfCancelled()
    return IdentifierCompletionProvider(file, cursor, compiler, settings)
      .complete(task, path, partial, endsWithParen)
  }
}
