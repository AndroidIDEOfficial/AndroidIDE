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
import com.itsaky.androidide.lsp.java.providers.CompletionProvider
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.MatchLevel
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.progress.ProgressManager.Companion.abortIfCancelled
import jdkx.lang.model.element.Element
import jdkx.lang.model.element.ElementKind.METHOD
import jdkx.lang.model.element.ExecutableElement
import jdkx.lang.model.element.Modifier.STATIC
import jdkx.lang.model.element.Name
import jdkx.lang.model.element.TypeElement
import openjdk.source.tree.CompilationUnitTree
import openjdk.source.tree.MemberSelectTree
import openjdk.source.util.TreePath
import openjdk.source.util.Trees
import java.nio.file.Path

/**
 * Completes static imports.
 *
 * @author Akash Yadav
 */
class StaticImportCompletionProvider(
  completingFile: Path,
  cursor: Long,
  compiler: JavaCompilerService,
  settings: IServerSettings,
  val root: CompilationUnitTree,
) : IJavaCompletionProvider(cursor, completingFile, compiler, settings) {

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

    abortIfCancelled()
    abortCompletionIfCancelled()

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

      list.add(method(task, value, !endsWithParen, matchLevel, partial))
    }

    log.info("...found {} static imports", list.size)

    return CompletionResult(list)
  }

  private fun importMatchesPartial(staticImport: Name, partial: String): Boolean {
    return (staticImport.contentEquals("*") || matchLevel(staticImport, partial) != NO_MATCH)
  }

  private fun memberMatchesImport(staticImport: Name, member: Element): Boolean {
    return staticImport.contentEquals("*") || staticImport.contentEquals(member.simpleName)
  }
}
