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

package com.itsaky.androidide.lsp.java.utils;

import androidx.annotation.NonNull;
import com.itsaky.androidide.lsp.java.compiler.CompilerProvider;
import com.itsaky.androidide.lsp.java.rewrite.AddImport;
import com.itsaky.androidide.lsp.models.TextEdit;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.projects.util.StringSearch;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import jdkx.lang.model.element.ExecutableElement;
import jdkx.lang.model.element.Modifier;
import jdkx.lang.model.element.Name;
import jdkx.lang.model.element.TypeElement;
import jdkx.lang.model.type.ArrayType;
import jdkx.lang.model.type.DeclaredType;
import jdkx.lang.model.type.ExecutableType;
import jdkx.lang.model.type.TypeMirror;
import openjdk.source.tree.ClassTree;
import openjdk.source.tree.CompilationUnitTree;
import openjdk.source.tree.LineMap;
import openjdk.source.tree.MethodTree;
import openjdk.source.tree.Tree;
import openjdk.source.util.JavacTask;
import openjdk.source.util.SourcePositions;
import openjdk.source.util.Trees;

public class EditHelper {

  public static List<TextEdit> addImportIfNeeded(CompilerProvider compiler, Path file,
                                                 Set<String> imports, String className
  ) {
    if (file == null || containsImport(file, imports, className)) {
      return Collections.emptyList();
    }

    AddImport addImport = new AddImport(file, className);
    return Arrays.asList(Objects.requireNonNull(addImport.rewrite(compiler).get(file)));
  }

  public static boolean containsImport(@NonNull Path file, Set<String> imports, String className) {
    if (imports == null) {
      imports = Collections.emptySet();
    }

    final String pkgName = Extractors.packageName(className);
    final String star = pkgName + ".*";
    if ("java.lang".equals(pkgName) || imports.contains(className) || imports.contains(star)) {
      return true;
    }

    final var filePackage = StringSearch.packageName(file);
    return filePackage != null && filePackage.equals(pkgName);
  }

  public static TextEdit removeTree(final JavacTask task, final CompilationUnitTree root,
                                    final Tree remove
  ) {
    SourcePositions pos = Trees.instance(task).getSourcePositions();
    LineMap lines = root.getLineMap();
    long start = pos.getStartPosition(root, remove);
    long end = pos.getEndPosition(root, remove);
    int startLine = (int) lines.getLineNumber(start);
    int startColumn = (int) lines.getColumnNumber(start);
    Position startPos = new Position(startLine - 1, startColumn - 1);
    int endLine = (int) lines.getLineNumber(end);
    int endColumn = (int) lines.getColumnNumber(end);
    Position endPos = new Position(endLine - 1, endColumn - 1);
    Range range = new Range(startPos, endPos);
    return new TextEdit(range, "");
  }

  public static String printMethod(final ExecutableElement method,
                                   final ExecutableType parameterizedType, MethodTree source
  ) {
    final StringBuilder buf = new StringBuilder();
    // TODO leading \n is extra, but needed for indent replaceAll trick
    buf.append("\n@Override\n");
    if (method.getModifiers().contains(Modifier.PUBLIC)) {
      buf.append("public ");
    }
    if (method.getModifiers().contains(Modifier.PROTECTED)) {
      buf.append("protected ");
    }
    buf.append(EditHelper.printType(parameterizedType.getReturnType())).append(" ");
    buf.append(method.getSimpleName()).append("(");
    buf.append(printParameters(parameterizedType, source));
    buf.append(") {\n    // TODO\n}");
    return buf.toString();
  }

  public static String printType(final TypeMirror type) {
    if (type instanceof DeclaredType) {
      DeclaredType declared = (DeclaredType) type;
      String string = printTypeName((TypeElement) declared.asElement());
      if (!declared.getTypeArguments().isEmpty()) {
        string = string + "<" + printTypeParameters(declared.getTypeArguments()) + ">";
      }
      return string;
    } else if (type instanceof ArrayType) {
      ArrayType array = (ArrayType) type;
      return printType(array.getComponentType()) + "[]";
    } else {
      return type.toString();
    }
  }

  public static String printTypeName(final TypeElement type) {
    if (type.getEnclosingElement() instanceof TypeElement) {
      return printTypeName((TypeElement) type.getEnclosingElement()) + "." + type.getSimpleName();
    }
    return type.getSimpleName().toString();
  }

  public static int indent(final JavacTask task, final CompilationUnitTree root, final Tree leaf) {
    SourcePositions pos = Trees.instance(task).getSourcePositions();
    return indent(root, leaf, pos);
  }

  private static int indent(@NonNull CompilationUnitTree root, Tree leaf,
                            @NonNull SourcePositions pos
  ) {
    LineMap lines = root.getLineMap();
    long startClass = pos.getStartPosition(root, leaf);
    long startLine = lines.getStartPosition(lines.getLineNumber(startClass));
    return (int) (startClass - startLine);
  }

  public static Position insertBefore(final JavacTask task, final CompilationUnitTree root,
                                      final Tree member
  ) {
    SourcePositions pos = Trees.instance(task).getSourcePositions();
    LineMap lines = root.getLineMap();
    long start = pos.getStartPosition(root, member);
    int line = (int) lines.getLineNumber(start);
    return new Position(line - 1, 0);
  }

  public static Position insertAfter(final JavacTask task, final CompilationUnitTree root,
                                     final Tree member
  ) {
    SourcePositions pos = Trees.instance(task).getSourcePositions();
    LineMap lines = root.getLineMap();
    long end = pos.getEndPosition(root, member);
    int line = (int) lines.getLineNumber(end);
    return new Position(line, 0);
  }

  public static Position insertAtEndOfClass(JavacTask task, CompilationUnitTree root, ClassTree leaf
  ) {
    SourcePositions pos = Trees.instance(task).getSourcePositions();
    LineMap lines = root.getLineMap();
    long end = pos.getEndPosition(root, leaf);
    int line = (int) lines.getLineNumber(end);
    int column = (int) lines.getColumnNumber(end);
    return new Position(line - 1, column - 2);
  }

  private static String printParameters(final ExecutableType method, final MethodTree source) {
    StringJoiner join = new StringJoiner(", ");
    for (int i = 0; i < method.getParameterTypes().size(); i++) {
      String type = EditHelper.printType(method.getParameterTypes().get(i));
      Name name = source.getParameters().get(i).getName();
      join.add(type + " " + name);
    }
    return join.toString();
  }

  private static String printTypeParameters(final List<? extends TypeMirror> arguments) {
    StringJoiner join = new StringJoiner(", ");
    for (TypeMirror a : arguments) {
      join.add(printType(a));
    }
    return join.toString();
  }
}
