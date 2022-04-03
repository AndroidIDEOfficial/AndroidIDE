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

package com.itsaky.lsp.java.actions;

import static com.itsaky.androidide.utils.Logger.newInstance;
import static com.itsaky.lsp.java.utils.CodeActionUtils.createQuickFix;
import static com.itsaky.lsp.java.utils.CodeActionUtils.isBlankLine;
import static com.itsaky.lsp.java.utils.CodeActionUtils.isInMethod;

import androidx.annotation.NonNull;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.java.compiler.CompileTask;
import com.itsaky.lsp.java.compiler.CompilerProvider;
import com.itsaky.lsp.java.compiler.SynchronizedTask;
import com.itsaky.lsp.java.rewrite.OverrideInheritedMethod;
import com.itsaky.lsp.java.rewrite.Rewrite;
import com.itsaky.lsp.java.utils.MethodPtr;
import com.itsaky.lsp.java.visitors.FindMethodDeclarationAt;
import com.itsaky.lsp.java.visitors.FindTypeDeclarationAt;
import com.itsaky.lsp.models.CodeActionItem;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.Range;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Provides code actions for the cursor range.
 *
 * @author Akash Yadav
 */
public class CursorCodeActionProvider implements ActionProvider {

    private static final Logger LOG = newInstance("JavaCursorCodeActionProvider");

    @NonNull
    @Override
    public List<CodeActionItem> provideActions(
            @NonNull CompilerProvider compiler,
            @NonNull Path file,
            @NonNull Range range,
            @NonNull List<DiagnosticItem> diagnostics) {
        logStarted(file, range);

        Instant started = Instant.now();
        final TreeMap<String, Rewrite> rewrites = new TreeMap<>();
        final SynchronizedTask synchronizedTask = compiler.compile(file);

        logCompilationTime(started);

        return synchronizedTask.get(
                task -> writeActions(started, compiler, file, range, rewrites, task));
    }

    @NonNull
    private List<CodeActionItem> writeActions(
            Instant started,
            @NonNull CompilerProvider compiler,
            @NonNull Path file,
            @NonNull Range range,
            @NonNull TreeMap<String, Rewrite> rewrites,
            @NonNull CompileTask task) {

        // 1-based line and column index
        final int startLine = range.getStart().getLine() + 1;
        final int startColumn = range.getStart().getColumn() + 1;
        final int endLine = range.getEnd().getLine() + 1;
        final int endColumn = range.getEnd().getColumn() + 1;
        final LineMap lines = task.root().getLineMap();
        final long start = lines.getPosition(startLine, startColumn);
        final long end = lines.getPosition(endLine, endColumn);

        if (isBlankLine(task.root(), start) && !isInMethod(task, start)) {
            rewrites.putAll(overrideInheritedMethods(task, file, start));
        }

        List<CodeActionItem> actions = new ArrayList<>();
        for (String title : rewrites.keySet()) {
            final Rewrite rewrite = rewrites.get(title);
            if (rewrite == null) {
                continue;
            }

            actions.addAll(createQuickFix(compiler, title, rewrite));
        }

        logActionTime(started, actions);

        return actions;
    }

    @NonNull
    private Map<String, Rewrite> overrideInheritedMethods(
            @NonNull CompileTask task, Path file, long cursor) {

        MethodTree methodTree = new FindMethodDeclarationAt(task.task).scan(task.root(), cursor);
        if (methodTree != null) return Collections.emptyMap();

        final TreeMap<String, Rewrite> actions = new TreeMap<>();
        final Trees trees = Trees.instance(task.task);
        final ClassTree classTree = new FindTypeDeclarationAt(task.task).scan(task.root(), cursor);
        if (classTree == null) {
            return Collections.emptyMap();
        }

        final TreePath classPath = trees.getPath(task.root(), classTree);
        final Elements elements = task.task.getElements();
        final TypeElement classElement = (TypeElement) trees.getElement(classPath);
        for (Element member : elements.getAllMembers(classElement)) {
            if (member.getModifiers().contains(Modifier.FINAL)
                    || member.getKind() != ElementKind.METHOD) {
                continue;
            }

            final ExecutableElement method = (ExecutableElement) member;
            final TypeElement methodSource = (TypeElement) member.getEnclosingElement();
            if (methodSource.getQualifiedName().contentEquals("java.lang.Object")
                    || methodSource.equals(classElement)) {
                continue;
            }

            final MethodPtr ptr = new MethodPtr(task.task, method);

            final Rewrite rewrite =
                    new OverrideInheritedMethod(
                            ptr.className,
                            ptr.methodName,
                            ptr.erasedParameterTypes,
                            file,
                            (int) cursor);
            final String title = "Override '" + method.getSimpleName() + "' from " + ptr.className;

            actions.put(title, rewrite);
        }

        return actions;
    }

    private void logCompilationTime(Instant started) {
        LOG.info(
                String.format(
                        Locale.getDefault(),
                        "...compiled in %d ms",
                        Duration.between(started, Instant.now()).toMillis()));
    }

    private void logStarted(Path file, Range range) {
        LOG.info(
                String.format(
                        Locale.getDefault(),
                        "Find code actions at %s(%d)...",
                        file.getFileName(),
                        range.getStart().getLine() + 1));
    }

    private void logActionTime(Instant started, List<CodeActionItem> actions) {
        long elapsed = Duration.between(started, Instant.now()).toMillis();
        LOG.info(
                String.format(
                        Locale.getDefault(),
                        "...created %d actions in %d ms",
                        actions.size(),
                        elapsed));
    }
}
