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

import android.text.TextUtils;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.java.compiler.CompileTask;
import com.itsaky.lsp.java.compiler.CompilerProvider;
import com.itsaky.lsp.java.compiler.SynchronizedTask;
import com.itsaky.lsp.java.rewrite.AddException;
import com.itsaky.lsp.java.rewrite.AddImport;
import com.itsaky.lsp.java.rewrite.AddSuppressWarningAnnotation;
import com.itsaky.lsp.java.rewrite.ConvertFieldToBlock;
import com.itsaky.lsp.java.rewrite.ConvertVariableToStatement;
import com.itsaky.lsp.java.rewrite.CreateMissingMethod;
import com.itsaky.lsp.java.rewrite.GenerateRecordConstructor;
import com.itsaky.lsp.java.rewrite.ImplementAbstractMethods;
import com.itsaky.lsp.java.rewrite.NoRewrite;
import com.itsaky.lsp.java.rewrite.OverrideInheritedMethod;
import com.itsaky.lsp.java.rewrite.RemoveClass;
import com.itsaky.lsp.java.rewrite.RemoveException;
import com.itsaky.lsp.java.rewrite.RemoveMethod;
import com.itsaky.lsp.java.rewrite.Rewrite;
import com.itsaky.lsp.java.visitors.FindMethodDeclarationAt;
import com.itsaky.lsp.java.visitors.FindTypeDeclarationAt;
import com.itsaky.lsp.models.CodeActionItem;
import com.itsaky.lsp.models.CodeActionParams;
import com.itsaky.lsp.models.CodeActionResult;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.api.ClientCodeWrapper;
import com.sun.tools.javac.util.JCDiagnostic;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import org.jetbrains.annotations.Contract;

public class CodeActionProvider {

    private static final Pattern NOT_THROWN_EXCEPTION =
            Pattern.compile("^'((\\w+\\.)*\\w+)' is not thrown");
    private static final Pattern UNREPORTED_EXCEPTION =
            Pattern.compile("unreported exception ((\\w+\\.)*\\w+)");
    private static final Logger LOG = Logger.newInstance ("JavaCodeActionProvider");
    private final CompilerProvider compiler;

    public CodeActionProvider(CompilerProvider compiler) {
        this.compiler = compiler;
    }

    @NonNull
    public CodeActionResult codeActions(@NonNull CodeActionParams params) {
        if (params.getDiagnostics().isEmpty()) {
            return codeActionsForCursor(params);
        } else {
            return new CodeActionResult(codeActionForDiagnostics(params));
        }
    }

    public CodeActionResult codeActionsForCursor(CodeActionParams params) {
        final Path file = params.getFile();

        // 1-based line and column index
        final int line = params.getRange().getStart().getLine() + 1;
        final int column = params.getRange().getStart().getColumn() + 1;

        LOG.info(
                String.format(
                        Locale.getDefault(),
                        "Find code actions at %s(%d)...",
                        file.getFileName(),
                        params.getRange().getStart().getLine() + 1));
        Instant started = Instant.now();

        final TreeMap<String, Rewrite> rewrites = new TreeMap<>();
        final SynchronizedTask synchronizedTask = compiler.compile(file);
        return synchronizedTask.get(
                task -> {
                    long elapsed = Duration.between(started, Instant.now()).toMillis();
                    LOG.info(String.format(Locale.getDefault(), "...compiled in %d ms", elapsed));
                    final LineMap lines = task.root().getLineMap();
                    final long cursor = lines.getPosition(line, column);
                    rewrites.putAll(overrideInheritedMethods(task, file, cursor));

                    List<CodeActionItem> actions = new ArrayList<>();
                    for (String title : rewrites.keySet()) {
                        final Rewrite rewrite = rewrites.get(title);
                        if (rewrite == null) {
                            continue;
                        }

                        actions.addAll(createQuickFix(title, rewrite));
                    }

                    elapsed = Duration.between(started, Instant.now()).toMillis();
                    LOG.info(
                            String.format(
                                    Locale.getDefault(),
                                    "...created %d actions in %d ms",
                                    actions.size(),
                                    elapsed));
                    return new CodeActionResult(actions);
                });
    }

    private Map<String, Rewrite> overrideInheritedMethods(
            CompileTask task, Path file, long cursor) {
        if (!isBlankLine(task.root(), cursor)) return Collections.emptyMap();
        if (isInMethod(task, cursor)) return Collections.emptyMap();

        MethodTree methodTree = new FindMethodDeclarationAt(task.task).scan(task.root(), cursor);
        if (methodTree != null) return Collections.emptyMap();

        final TreeMap<String, Rewrite> actions = new TreeMap<>();
        final Trees trees = Trees.instance(task.task);
        final ClassTree classTree = new FindTypeDeclarationAt(task.task).scan(task.root(), cursor);
        if (classTree == null) return Collections.emptyMap();
        final TreePath classPath = trees.getPath(task.root(), classTree);
        final Elements elements = task.task.getElements();
        final TypeElement classElement = (TypeElement) trees.getElement(classPath);
        for (Element member : elements.getAllMembers(classElement)) {
            if (member.getModifiers().contains(Modifier.FINAL)) continue;
            if (member.getKind() != ElementKind.METHOD) continue;
            final ExecutableElement method = (ExecutableElement) member;
            final TypeElement methodSource = (TypeElement) member.getEnclosingElement();
            if (methodSource.getQualifiedName().contentEquals("java.lang.Object")) continue;
            if (methodSource.equals(classElement)) continue;
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

    private boolean isInMethod(CompileTask task, long cursor) {
        MethodTree method = new FindMethodDeclarationAt(task.task).scan(task.root(), cursor);
        return method != null;
    }

    private boolean isBlankLine(CompilationUnitTree root, long cursor) {
        LineMap lines = root.getLineMap();
        long line = lines.getLineNumber(cursor);
        long start = lines.getStartPosition(line);
        CharSequence contents;
        try {
            contents = root.getSourceFile().getCharContent(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (long i = start; i < cursor; i++) {
            if (!Character.isWhitespace(contents.charAt((int) i))) {
                return false;
            }
        }
        return true;
    }

    public List<CodeActionItem> codeActionForDiagnostics(CodeActionParams params) {
        LOG.info(
                String.format(
                        Locale.getDefault(),
                        "Check %d diagnostics for quick fixes...",
                        params.getDiagnostics().size()));
        Instant started = Instant.now();
        Path file = params.getFile();
        final SynchronizedTask synchronizedTask = compiler.compile(file);
        final List<Pair<String, Rewrite>> actions =
                synchronizedTask.get(
                        task -> {
                            List<Pair<String, Rewrite>> pairs = new ArrayList<>();
                            for (DiagnosticItem d : params.getDiagnostics()) {
                                Pair<String, Rewrite> pair = codeActionForDiagnostic(task, file, d);
                                if (!TextUtils.isEmpty(pair.first) && pair.second != null) {
                                    pairs.add(pair);
                                }
                            }
                            return pairs;
                        });

        final List<CodeActionItem> result = new ArrayList<>();
        for (Pair<String, Rewrite> pair : actions) {
            try {
                result.addAll(pair.second.asCodeActions(compiler, pair.first));
            } catch (Throwable throwable) {
                LOG.error("Unable to create rewrite for quickfix", throwable);
            }
        }

        LOG.debug(
                String.format(
                        Locale.ROOT,
                        "Created %d code actions in %dms",
                        result.size(),
                        Instant.now().toEpochMilli() - started.toEpochMilli()));

        return result;
    }

    private Pair<String, Rewrite> codeActionForDiagnostic(
            CompileTask task, Path file, DiagnosticItem d) {
        // TODO this should be done asynchronously using executeCommand
        final Rewrite rewrite;
        final String title;
        switch (d.getCode()) {
            case "unused_local":
                rewrite =
                        new ConvertVariableToStatement(
                                file, findPosition(task, d.getRange().getStart()));
                title = "Convert to statement";
                break;
            case "unused_field":
                rewrite =
                        new ConvertFieldToBlock(file, findPosition(task, d.getRange().getStart()));
                title = "Convert to block";
                break;
            case "unused_class":
                rewrite = new RemoveClass(file, findPosition(task, d.getRange().getStart()));
                title = "Remove class";
                break;
            case "unused_method":
                final MethodPtr unusedMethod = findMethod(task, d.getRange());
                rewrite =
                        new RemoveMethod(
                                unusedMethod.className,
                                unusedMethod.methodName,
                                unusedMethod.erasedParameterTypes);
                title = "Remove method";
                break;
            case "unused_throws":
                final CharSequence shortExceptionName = extractRange(task, d.getRange());
                final String notThrown = extractNotThrownExceptionName(d.getMessage());
                final MethodPtr methodWithExtraThrow = findMethod(task, d.getRange());
                rewrite =
                        new RemoveException(
                                methodWithExtraThrow.className,
                                methodWithExtraThrow.methodName,
                                methodWithExtraThrow.erasedParameterTypes,
                                notThrown);
                title = "Remove '" + shortExceptionName + "'";
                break;
            case "compiler.warn.unchecked.call.mbr.of.raw.type":
                final MethodPtr warnedMethod = findMethod(task, d.getRange());
                rewrite =
                        new AddSuppressWarningAnnotation(
                                warnedMethod.className,
                                warnedMethod.methodName,
                                warnedMethod.erasedParameterTypes);
                title = "Suppress 'unchecked' warning";
                break;
            case "compiler.err.unreported.exception.need.to.catch.or.throw":
                final MethodPtr needsThrow = findMethod(task, d.getRange());
                final String exceptionName = extractExceptionName(d.getMessage());
                rewrite =
                        new AddException(
                                needsThrow.className,
                                needsThrow.methodName,
                                needsThrow.erasedParameterTypes,
                                exceptionName);
                title = "Add 'throws'";
                break;
            case "compiler.err.cant.resolve.location":
                CharSequence simpleName = extractRange(task, d.getRange());
                List<CodeActionItem> allImports = new ArrayList<>();
                for (String qualifiedName : compiler.publicTopLevelTypes()) {
                    if (qualifiedName.endsWith("." + simpleName)) {
                        String actionTitle = "Import '" + qualifiedName + "'";
                        final Rewrite addImport = new AddImport(file, qualifiedName);
                        allImports.addAll(createQuickFix(actionTitle, addImport));
                    }
                }
                return Pair.create("", new NoRewrite(allImports));
            case "compiler.err.var.not.initialized.in.default.constructor":
                final String needsConstructor = findClassNeedingConstructor(task, d.getRange());
                if (needsConstructor == null) {
                    return Pair.create("", null);
                }

                rewrite = new GenerateRecordConstructor(needsConstructor);
                title = "Generate constructor";
                break;
            case "compiler.err.does.not.override.abstract":
                final Diagnostic<? extends JavaFileObject> diagnostic =
                        (Diagnostic<? extends JavaFileObject>) d.getExtra();
                JCDiagnostic jcDiagnostic = unwrapJCDiagnostic(diagnostic);
                if (jcDiagnostic == null) {
                    return Pair.create("", null);
                }

                rewrite = new ImplementAbstractMethods(jcDiagnostic);
                title = "Implement abstract methods";
                break;
            case "compiler.err.cant.resolve.location.args":
                rewrite =
                        new CreateMissingMethod(file, findPosition(task, d.getRange().getStart()));
                title = "Create missing method";
                break;
            default:
                return Pair.create("", null);
        }

        return Pair.create(title, rewrite);
    }

    @Nullable
    @Contract(pure = true)
    private JCDiagnostic unwrapJCDiagnostic(Diagnostic<? extends JavaFileObject> diagnostic) {
        if (diagnostic instanceof JCDiagnostic) {
            return (JCDiagnostic) diagnostic;
        } else if (diagnostic instanceof ClientCodeWrapper.DiagnosticSourceUnwrapper) {
            return ((ClientCodeWrapper.DiagnosticSourceUnwrapper) diagnostic).d;
        }

        return null;
    }

    private int findPosition(CompileTask task, Position position) {
        final LineMap lines = task.root().getLineMap();
        return (int) lines.getPosition(position.getLine() + 1, position.getColumn() + 1);
    }

    private String findClassNeedingConstructor(CompileTask task, Range range) {
        final ClassTree type = findClassTree(task, range);
        if (type == null || hasConstructor(task, type)) return null;
        return qualifiedName(task, type);
    }

    private String findClass(CompileTask task, Range range) {
        final ClassTree type = findClassTree(task, range);
        if (type == null) return null;
        return qualifiedName(task, type);
    }

    private FindTypeDeclarationAt newClassFinder(CompileTask task) {
        return new FindTypeDeclarationAt(task.task);
    }

    private ClassTree findClassTree(CompileTask task, Range range) {
        final long position =
                task.root()
                        .getLineMap()
                        .getPosition(
                                range.getStart().getLine() + 1, range.getStart().getColumn() + 1);
        return newClassFinder(task).scan(task.root(), position);
    }

    private String qualifiedName(CompileTask task, ClassTree tree) {
        final Trees trees = Trees.instance(task.task);
        final TreePath path = trees.getPath(task.root(), tree);
        final TypeElement type = (TypeElement) trees.getElement(path);
        return type.getQualifiedName().toString();
    }

    private boolean hasConstructor(CompileTask task, ClassTree type) {
        for (Tree member : type.getMembers()) {
            if (member instanceof MethodTree) {
                MethodTree method = (MethodTree) member;
                if (isConstructor(task, method)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isConstructor(CompileTask task, MethodTree method) {
        return method.getName().contentEquals("<init>") && !synthetic(task, method);
    }

    private boolean synthetic(CompileTask task, MethodTree method) {
        return Trees.instance(task.task).getSourcePositions().getStartPosition(task.root(), method)
                != -1;
    }

    private MethodPtr findMethod(CompileTask task, Range range) {
        final Trees trees = Trees.instance(task.task);
        final long position =
                task.root()
                        .getLineMap()
                        .getPosition(
                                range.getStart().getLine() + 1, range.getStart().getColumn() + 1);
        final MethodTree tree = new FindMethodDeclarationAt(task.task).scan(task.root(), position);
        final TreePath path = trees.getPath(task.root(), tree);
        final ExecutableElement method = (ExecutableElement) trees.getElement(path);
        return new MethodPtr(task.task, method);
    }

    private String extractNotThrownExceptionName(String message) {
        final Matcher matcher = NOT_THROWN_EXCEPTION.matcher(message);
        if (!matcher.find()) {
            LOG.warn(String.format("`%s` doesn't match `%s`", message, NOT_THROWN_EXCEPTION));
            return "";
        }
        return matcher.group(1);
    }

    private String extractExceptionName(String message) {
        final Matcher matcher = UNREPORTED_EXCEPTION.matcher(message);
        if (!matcher.find()) {
            LOG.warn(String.format("`%s` doesn't match `%s`", message, UNREPORTED_EXCEPTION));
            return "";
        }
        return matcher.group(1);
    }

    @NonNull
    private CharSequence extractRange(CompileTask task, Range range) {
        CharSequence contents;
        try {
            contents = task.root().getSourceFile().getCharContent(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int start =
                (int)
                        task.root()
                                .getLineMap()
                                .getPosition(
                                        range.getStart().getLine() + 1,
                                        range.getStart().getColumn() + 1);
        int end =
                (int)
                        task.root()
                                .getLineMap()
                                .getPosition(
                                        range.getEnd().getLine() + 1,
                                        range.getEnd().getColumn() + 1);
        return contents.subSequence(start, end);
    }

    private List<CodeActionItem> createQuickFix(String title, Rewrite rewrite) {

        if (rewrite == null) {
            return Collections.emptyList();
        }

        return ((Rewrite) rewrite).asCodeActions(compiler, title);
    }

    static class MethodPtr {
        String className, methodName;
        String[] erasedParameterTypes;

        MethodPtr(@NonNull JavacTask task, @NonNull ExecutableElement method) {
            final Types types = task.getTypes();
            final TypeElement parent = (TypeElement) method.getEnclosingElement();
            className = parent.getQualifiedName().toString();
            methodName = method.getSimpleName().toString();
            erasedParameterTypes = new String[method.getParameters().size()];
            for (int i = 0; i < erasedParameterTypes.length; i++) {
                final VariableElement param = method.getParameters().get(i);
                final TypeMirror type = param.asType();
                final TypeMirror erased = types.erasure(type);
                erasedParameterTypes[i] = erased.toString();
            }
        }
    }
}
