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
import com.itsaky.androidide.lsp.java.providers.CompletionProvider.MAX_COMPLETION_ITEMS
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.MatchLevel.CASE_SENSITIVE_EQUAL
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.utils.BootClasspathProvider
import com.itsaky.androidide.utils.ClassTrie.Node
import com.sun.source.util.TreePath
import java.nio.file.Path

/**
 * Provides completions for imports.
 *
 * @author Akash Yadav
 */
class ImportCompletionProvider(
  completingFile: Path,
  cursor: Long,
  compiler: JavaCompilerService,
  settings: IServerSettings,
) : IJavaCompletionProvider(completingFile, cursor, compiler, settings) {

  lateinit var importPath: String

  override fun doComplete(
    task: CompileTask,
    path: TreePath,
    partial: String,
    endsWithParen: Boolean,
  ): CompletionResult {
    log.info("...complete import for path:", importPath)

    val names: MutableSet<String> = HashSet()
    val list = mutableListOf<CompletionItem>()

    var pkgName = importPath
    val incomplete: String
    if (!pkgName.contains(".")) {
      pkgName = ""
      incomplete = importPath
    } else if (pkgName.endsWith(".")) {
      pkgName = pkgName.substring(0, pkgName.lastIndex)
      incomplete = ""
    } else {
      pkgName = pkgName.substringBeforeLast(delimiter = '.')
      incomplete = pkgName.substringAfterLast(delimiter = '.')
    }

    val module = compiler.module
    if (module == null) {
      legacyImportPathCompletion(partial, names, list)
      return CompletionResult(list)
    }
  
    BootClasspathProvider.getAllEntries().forEach {
      val node =
        if (pkgName.isEmpty()) {
          it.root
        } else it.findNode(pkgName)
      if (node != null) {
        addDirectChildNodes(node, incomplete, list, names)
      }
    }
  
    val classpathNode =
      if (pkgName.isEmpty()) module.compileClasspathClasses.root
      else module.compileClasspathClasses.findNode(pkgName)
    if (classpathNode != null) {
      addDirectChildNodes(classpathNode, incomplete, list, names)
    }

    val sourceNode =
      if (pkgName.isEmpty()) module.compileJavaSourceClasses.root
      else module.compileJavaSourceClasses.findNode(pkgName)
    if (sourceNode != null) {
      addDirectChildNodes(sourceNode, incomplete, list, names)
    }

    return CompletionResult(list)
  }

  private fun addDirectChildNodes(
    sourceNode: Node,
    incomplete: String,
    list: MutableList<CompletionItem>,
    names: MutableSet<String>
  ) {
    for (child in sourceNode.children.values) {
      val match =
        if (incomplete.isEmpty()) {
          CASE_SENSITIVE_EQUAL
        } else {
          matchLevel(child.name, incomplete)
        }

      if (match == NO_MATCH || names.contains(child.name)) {
        continue
      }

      if (child.isClass) {
        list.add(classItem(child.qualifiedName, match))
      } else {
        list.add(packageItem(child.name, match))
      }
      names.add(child.name)
    }
  }

  private fun legacyImportPathCompletion(
    partial: String,
    names: MutableSet<String>,
    list: MutableList<CompletionItem>
  ) {
    for (className in compiler.publicTopLevelTypes()) {
      val matchLevel = matchLevel(className, partial)
      if (matchLevel == NO_MATCH) {
        continue
      }

      val start = importPath.lastIndexOf('.')
      var end = className.indexOf('.', importPath.length)
      if (end == -1) {
        end = className.length
      }
      val segment = className.substring(start + 1, end)
      if (names.contains(segment)) {
        continue
      }
      names.add(segment)
      val isClass = end == importPath.length
      if (isClass) {
        list.add(classItem(className, matchLevel))
      } else {
        list.add(packageItem(segment, matchLevel))
      }

      if (list.size > MAX_COMPLETION_ITEMS) {
        break
      }
    }
  }
}
