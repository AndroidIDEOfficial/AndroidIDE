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
package com.itsaky.androidide.lsp.java.providers

import com.itsaky.androidide.lsp.java.compiler.CompileTask
import com.itsaky.androidide.lsp.java.models.DiagnosticCode
import com.itsaky.androidide.lsp.java.models.DiagnosticCode.EMPTY_BLOCK
import com.itsaky.androidide.lsp.java.models.DiagnosticCode.UNUSED_THROWS
import com.itsaky.androidide.lsp.java.visitors.DiagnosticVisitor
import com.itsaky.androidide.lsp.models.DiagnosticItem
import com.itsaky.androidide.lsp.models.DiagnosticSeverity
import com.itsaky.androidide.lsp.models.DiagnosticSeverity.WARNING
import com.itsaky.androidide.models.Range
import com.itsaky.androidide.progress.ProgressManager.Companion.abortIfCancelled
import com.itsaky.androidide.projects.FileManager
import com.itsaky.androidide.utils.DocumentUtils.isSameFile
import jdkx.lang.model.element.Element
import jdkx.tools.Diagnostic
import jdkx.tools.JavaFileObject
import openjdk.source.tree.BlockTree
import openjdk.source.tree.ClassTree
import openjdk.source.tree.CompilationUnitTree
import openjdk.source.tree.LineMap
import openjdk.source.tree.MethodTree
import openjdk.source.tree.VariableTree
import openjdk.source.util.TreePath
import openjdk.source.util.Trees
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Locale
import java.util.regex.Pattern

/**
 * Finds errors and warnings from a compilation task.
 *
 * @author Akash Yadav
 */
object DiagnosticsProvider {
  /**
   * Finds diagnostics from the given task (only the diagnostics for the given file). The task
   * should be a valid task.
   *
   * As the file might be too long, the diagnostics list must be sorted so we can quickly binary
   * search the list when needed.
   *
   * @param task The compilation task to get diagnostics from.
   * @param file The file of which the diagnostics must be extracted.
   * @return The list of diagnostics retrieved from the task. Never null.
   */
  @JvmStatic
  fun findDiagnostics(task: CompileTask, file: Path?): List<DiagnosticItem> {
    val result = mutableListOf<DiagnosticItem>()
    var root: CompilationUnitTree? = null
    for (tree in task.roots) {
      abortIfCancelled()
      val path = Paths.get(tree.sourceFile.toUri())
      if (isSameFile(path, file!!)) {
        root = tree
        break
      }
    }

    abortIfCancelled()

    if (root == null) {
      // CompilationUnitTree for the file was not found
      // Can't do anything...
      return result
    }

    addCompilerErrors(task, root, result)
    abortIfCancelled()
    addDiagnosticsByVisiting(task, root, result)
    abortIfCancelled()
    return result
  }

  private fun addDiagnosticsByVisiting(
    task: CompileTask,
    root: CompilationUnitTree,
    result: MutableList<DiagnosticItem>
  ) {
    val notThrown = mutableMapOf<TreePath?, String>()
    val scanner = DiagnosticVisitor(task.task)
    scanner.scan(root, notThrown)
    for (unusedEl in scanner.notUsed()) {
      warnUnused(task, unusedEl)?.also { result.add(it) }
    }
    
    for (location in notThrown.keys) {
      result.add(warnNotThrown(task, notThrown[location], location!!))
    }

    for (path in scanner.emptyBlocks.keys) {
      result.add(warnEmptyBlock(task, path, scanner.emptyBlocks[path]!!))
    }
  }

  private fun warnEmptyBlock(task: CompileTask, path: TreePath, name: String): DiagnosticItem {
    val trees = Trees.instance(task.task)
    val thisTree = path.leaf
    val code = EMPTY_BLOCK

    val root = task.root()
    val lines = task.root().lineMap
    val positions = trees.sourcePositions
    val start = positions.getStartPosition(root, thisTree)
    val end = positions.getEndPosition(root, thisTree)
    return DiagnosticItem(
      source = "",
      code = code.id,
      message = "'$name' statement has empty body",
      severity = WARNING,
      range =
        Range(getPosition(start, lines), getPosition(end, lines)).apply {
          this.start.index = start.toInt()
          this.end.index = end.toInt()
        }
    )
  }

  private fun addCompilerErrors(
    task: CompileTask,
    root: CompilationUnitTree,
    result: MutableList<DiagnosticItem>
  ) {
    for (diagnostic in task.diagnostics) {
      if (diagnostic.source == null || diagnostic.source!!.toUri() != root.sourceFile.toUri()) {
        continue
      }
      if (diagnostic.startPosition == -1L || diagnostic.endPosition == -1L) {
        continue
      }
      result.add(asDiagnosticItem(diagnostic, root.lineMap))
    }
  }

  private fun warnNotThrown(task: CompileTask, name: String?, path: TreePath): DiagnosticItem {
    val trees = Trees.instance(task.task)
    val pos = trees.sourcePositions
    val root = path.compilationUnit
    val lines = root.lineMap
    val start = pos.getStartPosition(root, path.leaf)
    val end = pos.getEndPosition(root, path.leaf)
    return DiagnosticItem(
      message = String.format("'%s' is not thrown in the body of the method", name),
      range =
        Range(getPosition(start, lines), getPosition(end, lines)).apply {
          this.start.index = start.toInt()
          this.end.index = end.toInt()
        },
      code = UNUSED_THROWS.id,
      severity = DiagnosticSeverity.INFO,
      source = ""
    )
  }

  private fun warnUnused(task: CompileTask, unusedEl: Element): DiagnosticItem? {
    val trees = Trees.instance(task.task)
    val path = trees.getPath(unusedEl) ?: throw RuntimeException("$unusedEl has no path")
    val root = path.compilationUnit
    val leaf = path.leaf
    val pos = trees.sourcePositions
    var start = pos.getStartPosition(root, leaf).toInt()
    var end = pos.getEndPosition(root, leaf).toInt()

    if (leaf is VariableTree) {
      val offset = pos.getEndPosition(root, leaf.type).toInt()
      if (offset != -1) {
        start = offset
      }
    }

    val file = Paths.get(root.sourceFile.toUri())
    val contents = FileManager.getDocumentContents(file)
    var name = unusedEl.simpleName
    if (name.contentEquals("<init>")) {
      name = unusedEl.enclosingElement.simpleName
    }

    val region = try {
      contents.subSequence(start, end)
    } catch (err: IndexOutOfBoundsException) {
      // might happen if the file contents were changed after the file was compiled for analysis
      return null
    }

    val matcher = Pattern.compile("\\b$name\\b").matcher(region)
    if (matcher.find()) {
      start += matcher.start()
      end = start + name.length
    }

    val message = String.format("'%s' is not used", name)
    val code: DiagnosticCode
    val severity: DiagnosticSeverity
    when (leaf) {
      is VariableTree -> {
        when (path.parentPath.leaf) {
          is MethodTree -> {
            code = DiagnosticCode.UNUSED_PARAM
            severity = DiagnosticSeverity.HINT
          }
          is BlockTree -> {
            code = DiagnosticCode.UNUSED_LOCAL
            severity = DiagnosticSeverity.INFO
          }
          is ClassTree -> {
            code = DiagnosticCode.UNUSED_FIELD
            severity = DiagnosticSeverity.INFO
          }
          else -> {
            code = DiagnosticCode.UNUSED_OTHER
            severity = DiagnosticSeverity.HINT
          }
        }
      }
      is MethodTree -> {
        code = DiagnosticCode.UNUSED_METHOD
        severity = DiagnosticSeverity.INFO
      }
      is ClassTree -> {
        code = DiagnosticCode.UNUSED_CLASS
        severity = DiagnosticSeverity.INFO
      }
      else -> {
        code = DiagnosticCode.UNUSED_OTHER
        severity = DiagnosticSeverity.INFO
      }
    }

    return asDiagnosticItem(severity, code.id, message, start.toLong(), end.toLong(), root)
  }

  private fun asDiagnosticItem(
    severity: DiagnosticSeverity,
    code: String,
    message: String,
    start: Long,
    end: Long,
    root: CompilationUnitTree
  ): DiagnosticItem {
    return DiagnosticItem(
      message = message,
      code = code,
      severity = severity,
      range =
        Range(getPosition(start, root.lineMap), getPosition(end, root.lineMap)).apply {
          this.start.index = start.toInt()
          this.end.index = end.toInt()
        },
      source = ""
    )
  }

  private fun asDiagnosticItem(
    diagnostic: Diagnostic<out JavaFileObject?>,
    lines: LineMap
  ): DiagnosticItem {
    abortIfCancelled()
    val result =
      DiagnosticItem(
        range = getDiagnosticRange(diagnostic, lines),
        severity = severityFor(diagnostic.kind),
        code = diagnostic.code,
        message = diagnostic.getMessage(Locale.getDefault()),
        source = ""
      )
    result.range.start.index = diagnostic.startPosition.toInt()
    result.range.end.index = diagnostic.endPosition.toInt()
    result.extra = diagnostic
    return result
  }

  private fun getDiagnosticRange(
    diagnostic: Diagnostic<out JavaFileObject?>,
    lines: LineMap
  ): Range {
    abortIfCancelled()
    val start = getPosition(diagnostic.startPosition, lines)
    val end = getPosition(diagnostic.endPosition, lines)
    return Range(start, end)
  }

  private fun getPosition(position: Long, lines: LineMap): com.itsaky.androidide.models.Position {
    abortIfCancelled()
    // decrement the numbers
    // to convert 1-based indexes to 0-based
    val line = (lines.getLineNumber(position) - 1).toInt()
    val column = (lines.getColumnNumber(position) - 1).toInt()
    return com.itsaky.androidide.models.Position(line, column)
  }

  private fun severityFor(kind: Diagnostic.Kind): DiagnosticSeverity {
    return when (kind) {
      Diagnostic.Kind.ERROR -> DiagnosticSeverity.ERROR
      Diagnostic.Kind.WARNING,
      Diagnostic.Kind.MANDATORY_WARNING -> WARNING
      Diagnostic.Kind.NOTE -> DiagnosticSeverity.INFO
      Diagnostic.Kind.OTHER -> DiagnosticSeverity.HINT
      else -> DiagnosticSeverity.HINT
    }
  }
}
