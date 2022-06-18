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

package com.itsaky.lsp.java.utils;

import static com.itsaky.androidide.utils.ILogger.newInstance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.androidide.utils.ILogger;
import com.itsaky.lsp.java.compiler.CompileTask;
import com.itsaky.lsp.java.compiler.CompilerProvider;
import com.itsaky.lsp.java.rewrite.Rewrite;
import com.itsaky.lsp.java.visitors.FindMethodDeclarationAt;
import com.itsaky.lsp.java.visitors.FindTypeDeclarationAt;
import com.itsaky.lsp.models.CodeActionItem;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.util.JCDiagnostic;

import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * @author Akash Yadav
 */
public class CodeActionUtils {

  private static final Pattern NOT_THROWN_EXCEPTION =
      Pattern.compile("^'((\\w+\\.)*\\w+)' is not thrown");
  private static final Pattern UNREPORTED_EXCEPTION =
      Pattern.compile("unreported exception ((\\w+\\.)*\\w+)");
  private static final ILogger LOG = newInstance("JavaCodeActionUtils");

  public static CodeActionItem createQuickFix(
      final CompilerProvider compiler, String title, Rewrite rewrite) {

    if (rewrite == null) {
      return null;
    }

    return ((Rewrite) rewrite).asCodeActions(compiler, title);
  }

  public static boolean isInMethod(@NonNull CompileTask task, long cursor) {
    MethodTree method = new FindMethodDeclarationAt(task.task).scan(task.root(), cursor);
    return method != null;
  }

  public static boolean isBlankLine(@NonNull CompilationUnitTree root, long cursor) {
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

  public static int findPosition(@NonNull CompileTask task, @NonNull Position position) {
    final LineMap lines = task.root().getLineMap();
    return (int) lines.getPosition(position.getLine() + 1, position.getColumn() + 1);
  }

  @Nullable
  public static String findClassNeedingConstructor(CompileTask task, Range range) {
    final ClassTree type = findClassTree(task, range);
    if (type == null || hasConstructor(task, type)) return null;
    return qualifiedName(task, type);
  }

  public static ClassTree findClassTree(@NonNull CompileTask task, @NonNull Range range) {
    final long position =
        task.root()
            .getLineMap()
            .getPosition(range.getStart().getLine() + 1, range.getStart().getColumn() + 1);
    return newClassFinder(task).scan(task.root(), position);
  }

  @NonNull
  @Contract("_ -> new")
  public static FindTypeDeclarationAt newClassFinder(@NonNull CompileTask task) {
    return new FindTypeDeclarationAt(task.task);
  }

  @NonNull
  public static String qualifiedName(@NonNull CompileTask task, ClassTree tree) {
    final Trees trees = Trees.instance(task.task);
    final TreePath path = trees.getPath(task.root(), tree);
    final TypeElement type = (TypeElement) trees.getElement(path);
    return type.getQualifiedName().toString();
  }

  public static boolean hasConstructor(CompileTask task, @NonNull ClassTree type) {
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

  public static boolean isConstructor(CompileTask task, @NonNull MethodTree method) {
    return method.getName().contentEquals("<init>") && !synthetic(task, method);
  }

  public static boolean synthetic(@NonNull CompileTask task, MethodTree method) {
    return Trees.instance(task.task).getSourcePositions().getStartPosition(task.root(), method)
        != -1;
  }

  @NonNull
  public static MethodPtr findMethod(@NonNull CompileTask task, @NonNull Range range) {
    final Trees trees = Trees.instance(task.task);
    final long position =
        task.root()
            .getLineMap()
            .getPosition(range.getStart().getLine() + 1, range.getStart().getColumn() + 1);
    final MethodTree tree = new FindMethodDeclarationAt(task.task).scan(task.root(), position);
    final TreePath path = trees.getPath(task.root(), tree);
    final ExecutableElement method = (ExecutableElement) trees.getElement(path);
    return new MethodPtr(task.task, method);
  }

  public static String extractNotThrownExceptionName(String message) {
    final Matcher matcher = NOT_THROWN_EXCEPTION.matcher(message);
    if (!matcher.find()) {
      LOG.warn(String.format("`%s` doesn't match `%s`", message, NOT_THROWN_EXCEPTION));
      return "";
    }
    return matcher.group(1);
  }

  public static String extractExceptionName(String message) {
    final Matcher matcher = UNREPORTED_EXCEPTION.matcher(message);
    if (!matcher.find()) {
      LOG.warn(String.format("`%s` doesn't match `%s`", message, UNREPORTED_EXCEPTION));
      return "";
    }
    return matcher.group(1);
  }

  @NonNull
  public static CharSequence extractRange(@NonNull CompileTask task, Range range) {
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
                .getPosition(range.getStart().getLine() + 1, range.getStart().getColumn() + 1);
    int end =
        (int)
            task.root()
                .getLineMap()
                .getPosition(range.getEnd().getLine() + 1, range.getEnd().getColumn() + 1);
    return contents.subSequence(start, end);
  }

  @Nullable
  @Contract(pure = true)
  public static JCDiagnostic unwrapJCDiagnostic(Diagnostic<? extends JavaFileObject> diagnostic) {
    return JavaDiagnosticUtils.asJCDiagnostic(diagnostic);
  }
}
