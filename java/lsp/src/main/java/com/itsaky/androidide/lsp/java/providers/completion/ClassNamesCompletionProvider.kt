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
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.progress.ProgressManager.Companion.abortIfCancelled
import openjdk.source.tree.ClassTree
import openjdk.source.tree.CompilationUnitTree
import openjdk.source.util.TreePath
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Objects

/**
 * Completes class names.
 *
 * @author Akash Yadav
 */
class ClassNamesCompletionProvider(
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
    val packageName = Objects.toString(root.packageName, "")
    val uniques: MutableSet<String> = HashSet()

    val file: Path = Paths.get(root.sourceFile.toUri())
    val imports: Set<String> =
      root.imports.map { it.qualifiedIdentifier }.mapNotNull { it.toString() }.toSet()

    abortIfCancelled()
    abortCompletionIfCancelled()
    for (className in compiler.packagePrivateTopLevelTypes(packageName)) {
      val matchLevel = matchLevel(className, partial)
      if (matchLevel == NO_MATCH) {
        continue
      }

      list.add(classItem(imports, file, className, matchLevel))
      uniques.add(className)
    }

    abortIfCancelled()
    abortCompletionIfCancelled()

    val topLevelTypes = compiler.publicTopLevelTypes()
    for (className in topLevelTypes) {
      val matchLevel = matchLevel(simpleName(className), partial)
      if (matchLevel == NO_MATCH) {
        continue
      }

      if (uniques.contains(className)) {
        continue
      }

      list.add(classItem(imports, file, className, matchLevel))
      uniques.add(className)
    }
    abortIfCancelled()
    abortCompletionIfCancelled()
    for (t in root.typeDecls) {
      if (t !is ClassTree) {
        continue
      }
      val candidate = if (t.simpleName == null) "" else t.simpleName

      val matchLevel = matchLevel(candidate, partial)
      if (matchLevel == NO_MATCH) {
        continue
      }

      val name = packageName + "." + t.simpleName
      list.add(classItem(name, matchLevel))

      if (list.size > CompletionProvider.MAX_COMPLETION_ITEMS) {
        break
      }
    }

    log.info("...found {} class names", list.size)

    return CompletionResult(list)
  }
}
