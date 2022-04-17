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
package com.itsaky.lsp.java.providers

import com.itsaky.lsp.java.FileStore
import com.itsaky.lsp.java.compiler.CompileTask
import com.itsaky.lsp.java.models.DiagnosticCode
import com.itsaky.lsp.java.visitors.DiagnosticVisitor
import com.itsaky.lsp.models.DiagnosticItem
import com.itsaky.lsp.models.DiagnosticSeverity
import com.itsaky.lsp.models.Position
import com.itsaky.lsp.models.Range
import com.itsaky.lsp.util.PathUtils.Companion.isSameFile
import com.sun.source.tree.BlockTree
import com.sun.source.tree.ClassTree
import com.sun.source.tree.CompilationUnitTree
import com.sun.source.tree.LineMap
import com.sun.source.tree.MethodTree
import com.sun.source.tree.VariableTree
import com.sun.source.util.TreePath
import com.sun.source.util.Trees
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.regex.Pattern
import javax.lang.model.element.Element
import javax.tools.Diagnostic
import javax.tools.JavaFileObject

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
            val path = Paths.get(tree.sourceFile.toUri())
            if (isSameFile(path, file!!)) {
                root = tree
                break
            }
        }
        if (root == null) {
            // CompilationUnitTree for the file was not found
            // Can't do anything...
            return result
        }
        addCompilerErrors(task, root, result)
        addDiagnosticsByVisiting(task, root, result)
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
            result.add(warnUnused(task, unusedEl))
        }

        for (location in notThrown.keys) {
            result.add(warnNotThrown(task, notThrown[location], location!!))
        }

        for (path in scanner.emptyBlocks.keys) {
            result.add(warnEmptyBlock(task, path, scanner.emptyBlocks[path]!!))
        }
    }

    private fun warnEmptyBlock(task: CompileTask, path: TreePath, name: String): DiagnosticItem {
        val diagnostic = DiagnosticItem()
        val trees = Trees.instance(task.task)
        val thisTree = path.leaf
        val code = DiagnosticCode.EMPTY_BLOCK

        val root = task.root()
        val lines = task.root().lineMap
        val positions = trees.sourcePositions
        val start = positions.getStartPosition(root, thisTree)
        val end = positions.getEndPosition(root, thisTree)
        diagnostic.code = code.id
        diagnostic.message = "'$name' statement has empty body"
        diagnostic.severity = DiagnosticSeverity.WARNING
        diagnostic.range = Range(getPosition(start, lines), getPosition(end, lines))

        return diagnostic
    }

    private fun addCompilerErrors(
        task: CompileTask,
        root: CompilationUnitTree,
        result: MutableList<DiagnosticItem>
    ) {
        for (diagnostic in task.diagnostics) {
            if (diagnostic.source == null ||
                diagnostic.source!!.toUri() != root.sourceFile.toUri()) {
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
        val d = DiagnosticItem()
        d.message = String.format("'%s' is not thrown in the body of the method", name)
        d.range = Range(getPosition(start, lines), getPosition(end, lines))
        d.code = DiagnosticCode.UNUSED_THROWS.id
        d.severity = DiagnosticSeverity.INFO
        return d
    }

    private fun warnUnused(task: CompileTask, unusedEl: Element): DiagnosticItem {
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
        val contents = FileStore.contents(file)
        var name = unusedEl.simpleName
        if (name.contentEquals("<init>")) {
            name = unusedEl.enclosingElement.simpleName
        }
        val region = contents.subSequence(start, end)
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
        val item = DiagnosticItem()
        item.message = message
        item.code = code
        item.severity = severity
        item.range = Range(getPosition(start, root.lineMap), getPosition(end, root.lineMap))
        return item
    }

    private fun asDiagnosticItem(
        diagnostic: Diagnostic<out JavaFileObject?>,
        lines: LineMap
    ): DiagnosticItem {
        val result = DiagnosticItem()
        result.range = getDiagnosticRange(diagnostic, lines)
        result.severity = severityFor(diagnostic.kind)
        result.code = diagnostic.code
        result.message = diagnostic.getMessage(Locale.getDefault())
        result.extra = diagnostic
        return result
    }

    private fun getDiagnosticRange(
        diagnostic: Diagnostic<out JavaFileObject?>,
        lines: LineMap
    ): Range {
        val start = getPosition(diagnostic.startPosition, lines)
        val end = getPosition(diagnostic.endPosition, lines)
        return Range(start, end)
    }

    private fun getPosition(position: Long, lines: LineMap): Position {
        // decrement the numbers
        // to convert 1-based indexes to 0-based
        val line = (lines.getLineNumber(position) - 1).toInt()
        val column = (lines.getColumnNumber(position) - 1).toInt()
        return Position(line, column)
    }

    private fun severityFor(kind: Diagnostic.Kind): DiagnosticSeverity {
        return when (kind) {
            Diagnostic.Kind.ERROR -> DiagnosticSeverity.ERROR
            Diagnostic.Kind.WARNING, Diagnostic.Kind.MANDATORY_WARNING -> DiagnosticSeverity.WARNING
            Diagnostic.Kind.NOTE -> DiagnosticSeverity.INFO
            Diagnostic.Kind.OTHER -> DiagnosticSeverity.HINT
            else -> DiagnosticSeverity.HINT
        }
    }
}
