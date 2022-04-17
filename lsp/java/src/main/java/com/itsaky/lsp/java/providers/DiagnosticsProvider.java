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

package com.itsaky.lsp.java.providers;

import androidx.annotation.NonNull;

import com.itsaky.lsp.java.FileStore;
import com.itsaky.lsp.java.compiler.CompileTask;
import com.itsaky.lsp.java.models.DiagnosticCode;
import com.itsaky.lsp.java.visitors.DiagnosticVisitor;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.DiagnosticSeverity;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.util.PathUtils;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Finds errors and warnings from a compilation task.
 *
 * @author Akash Yadav
 */
public class DiagnosticsProvider {

    /**
     * Finds diagnostics from the given task (only the diagnostics for the given file). The task
     * should be a valid task.
     *
     * <p>As the file might be too long, the diagnostics list must be sorted so we can quickly
     * binary search the list when needed.
     *
     * @param task The compilation task to get diagnostics from.
     * @param file The file of which the diagnostics must be extracted.
     * @return The list of diagnostics retrieved from the task. Never null.
     */
    public static List<DiagnosticItem> findDiagnostics(final CompileTask task, final Path file) {
        final List<DiagnosticItem> result = new ArrayList<>();
        CompilationUnitTree root = null;
        for (CompilationUnitTree tree : task.roots) {
            final Path path = Paths.get(tree.getSourceFile().toUri());
            if (PathUtils.isSameFile(path, file)) {
                root = tree;
                break;
            }
        }

        if (root == null) {
            // CompilationUnitTree for the file was not found
            // Can't do anything...
            return result;
        }

        addCompilerErrors(task, root, result);
        addDiagnosticsByVisiting(task, root, result);

        return result;
    }

    private static void addDiagnosticsByVisiting(
            CompileTask task, CompilationUnitTree root, List<DiagnosticItem> result) {
        Map<TreePath, String> notThrown = new HashMap<>();
        DiagnosticVisitor diagnosticScanner = new DiagnosticVisitor(task.task);
        diagnosticScanner.scan(root, notThrown);
        for (Element unusedEl : diagnosticScanner.notUsed()) {
            result.add(warnUnused(task, unusedEl));
        }
        for (TreePath location : notThrown.keySet()) {
            result.add(warnNotThrown(task, notThrown.get(location), location));
        }
    }

    private static void addCompilerErrors(
            final CompileTask task,
            final CompilationUnitTree root,
            final List<DiagnosticItem> result) {
        for (Diagnostic<? extends JavaFileObject> diagnostic : task.diagnostics) {
            if (diagnostic.getSource() == null
                    || !diagnostic.getSource().toUri().equals(root.getSourceFile().toUri())) {
                continue;
            }

            if (diagnostic.getStartPosition() == -1 || diagnostic.getEndPosition() == -1) {
                continue;
            }

            result.add(asDiagnosticItem(diagnostic, root.getLineMap()));
        }
    }

    @NonNull
    private static DiagnosticItem warnNotThrown(CompileTask task, String name, TreePath path) {
        Trees trees = Trees.instance(task.task);
        SourcePositions pos = trees.getSourcePositions();
        CompilationUnitTree root = path.getCompilationUnit();
        LineMap lines = root.getLineMap();
        long start = pos.getStartPosition(root, path.getLeaf());
        long end = pos.getEndPosition(root, path.getLeaf());
        final DiagnosticItem d = new DiagnosticItem();
        d.setMessage(String.format("'%s' is not thrown in the body of the method", name));
        d.setRange(new Range(getPosition(start, lines), getPosition(end, lines)));
        d.setCode(DiagnosticCode.UNUSED_THROWS.getId());
        d.setSeverity(DiagnosticSeverity.INFO);
        return d;
    }

    @NonNull
    private static DiagnosticItem warnUnused(CompileTask task, Element unusedEl) {
        Trees trees = Trees.instance(task.task);
        TreePath path = trees.getPath(unusedEl);
        if (path == null) {
            throw new RuntimeException(unusedEl + " has no path");
        }

        CompilationUnitTree root = path.getCompilationUnit();
        Tree leaf = path.getLeaf();
        SourcePositions pos = trees.getSourcePositions();
        int start = (int) pos.getStartPosition(root, leaf);
        int end = (int) pos.getEndPosition(root, leaf);
        if (leaf instanceof VariableTree) {
            VariableTree v = (VariableTree) leaf;
            int offset = (int) pos.getEndPosition(root, v.getType());
            if (offset != -1) {
                start = offset;
            }
        }
        Path file = Paths.get(root.getSourceFile().toUri());
        CharSequence contents = FileStore.contents(file);
        Name name = unusedEl.getSimpleName();
        if (name.contentEquals("<init>")) {
            name = unusedEl.getEnclosingElement().getSimpleName();
        }
        CharSequence region = contents.subSequence(start, end);
        Matcher matcher = Pattern.compile("\\b" + name + "\\b").matcher(region);
        if (matcher.find()) {
            start += matcher.start();
            end = start + name.length();
        }

        String message = String.format("'%s' is not used", name);
        DiagnosticCode code;
        DiagnosticSeverity severity;
        if (leaf instanceof VariableTree) {
            Tree parent = path.getParentPath().getLeaf();
            if (parent instanceof MethodTree) {
                code = DiagnosticCode.UNUSED_PARAM;
                severity = DiagnosticSeverity.HINT;
            } else if (parent instanceof BlockTree) {
                code = DiagnosticCode.UNUSED_LOCAL;
                severity = DiagnosticSeverity.INFO;
            } else if (parent instanceof ClassTree) {
                code = DiagnosticCode.UNUSED_FIELD;
                severity = DiagnosticSeverity.INFO;
            } else {
                code = DiagnosticCode.UNUSED_OTHER;
                severity = DiagnosticSeverity.HINT;
            }
        } else if (leaf instanceof MethodTree) {
            code = DiagnosticCode.UNUSED_METHOD;
            severity = DiagnosticSeverity.INFO;
        } else if (leaf instanceof ClassTree) {
            code = DiagnosticCode.UNUSED_CLASS;
            severity = DiagnosticSeverity.INFO;
        } else {
            code = DiagnosticCode.UNUSED_OTHER;
            severity = DiagnosticSeverity.INFO;
        }

        return asDiagnosticItem(severity, code.getId(), message, start, end, root);
    }

    @NonNull
    private static DiagnosticItem asDiagnosticItem(
            DiagnosticSeverity severity,
            String code,
            String message,
            long start,
            long end,
            @NonNull CompilationUnitTree root) {
        final DiagnosticItem item = new DiagnosticItem();
        item.setMessage(message);
        item.setCode(code);
        item.setSeverity(severity);
        item.setRange(
                new Range(
                        getPosition(start, root.getLineMap()),
                        getPosition(end, root.getLineMap())));
        return item;
    }

    @NonNull
    private static DiagnosticItem asDiagnosticItem(
            final Diagnostic<? extends JavaFileObject> diagnostic, final LineMap lines) {
        final DiagnosticItem result = new DiagnosticItem();
        result.setRange(getDiagnosticRange(diagnostic, lines));
        result.setSeverity(severityFor(diagnostic.getKind()));
        result.setCode(diagnostic.getCode());
        result.setMessage(diagnostic.getMessage(Locale.getDefault()));
        result.setExtra(diagnostic);
        return result;
    }

    @NonNull
    public static Range getDiagnosticRange(
            @NonNull Diagnostic<? extends JavaFileObject> diagnostic, LineMap lines) {
        final Position start = getPosition(diagnostic.getStartPosition(), lines);
        final Position end = getPosition(diagnostic.getEndPosition(), lines);
        return new Range(start, end);
    }

    @NonNull
    private static Position getPosition(long position, @NonNull LineMap lines) {
        // decrement the numbers
        // to convert 1-based indexes to 0-based
        final int line = (int) (lines.getLineNumber(position) - 1);
        final int column = (int) (lines.getColumnNumber(position) - 1);
        return new Position(line, column);
    }

    private static DiagnosticSeverity severityFor(javax.tools.Diagnostic.Kind kind) {
        switch (kind) {
            case ERROR:
                return DiagnosticSeverity.ERROR;
            case WARNING:
            case MANDATORY_WARNING:
                return DiagnosticSeverity.WARNING;
            case NOTE:
                return DiagnosticSeverity.INFO;
            case OTHER:
            default:
                return DiagnosticSeverity.HINT;
        }
    }
}
