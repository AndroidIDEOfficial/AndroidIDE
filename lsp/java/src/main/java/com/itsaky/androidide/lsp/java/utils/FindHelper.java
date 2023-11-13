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

import androidx.annotation.Nullable;
import com.itsaky.androidide.lsp.java.compiler.CompileTask;
import com.itsaky.androidide.lsp.java.parser.ParseTask;
import com.itsaky.androidide.lsp.java.visitors.FindTypeDeclarationNamed;
import com.itsaky.androidide.models.Location;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jdkx.lang.model.element.Element;
import jdkx.lang.model.element.ElementKind;
import jdkx.lang.model.element.ExecutableElement;
import jdkx.lang.model.element.TypeElement;
import jdkx.lang.model.type.TypeMirror;
import jdkx.lang.model.util.Types;
import openjdk.source.tree.ArrayTypeTree;
import openjdk.source.tree.ClassTree;
import openjdk.source.tree.CompilationUnitTree;
import openjdk.source.tree.IdentifierTree;
import openjdk.source.tree.LineMap;
import openjdk.source.tree.MemberSelectTree;
import openjdk.source.tree.MethodTree;
import openjdk.source.tree.ParameterizedTypeTree;
import openjdk.source.tree.PrimitiveTypeTree;
import openjdk.source.tree.Tree;
import openjdk.source.tree.VariableTree;
import openjdk.source.util.SourcePositions;
import openjdk.source.util.TreePath;
import openjdk.source.util.Trees;

public class FindHelper {

  public static String[] erasedParameterTypes(CompileTask task, ExecutableElement method) {
    Types types = task.task.getTypes();
    String[] erasedParameterTypes = new String[method.getParameters().size()];
    for (int i = 0; i < erasedParameterTypes.length; i++) {
      TypeMirror p = method.getParameters().get(i).asType();
      erasedParameterTypes[i] = types.erasure(p).toString();
    }
    return erasedParameterTypes;
  }

  /**
   * Find the method with name <code>methodName</code> in class <code>clasName</code> with the given
   * parameter types.
   *
   * @param task                 The parse task.
   * @param className            The fully qualified class name.
   * @param methodName           The name of method in class <code>className</code>.
   * @param erasedParameterTypes The parameter types of the method (fully qualified class names).
   * @return The {@link MethodTree} for the method, or <code>null</code> if method was not found.
   */
  @Nullable
  public static MethodTree findMethod(
      ParseTask task, String className, String methodName, String[] erasedParameterTypes) {
    ClassTree classTree = findType(task, className);
    for (Tree member : classTree.getMembers()) {
      if (member.getKind() != Tree.Kind.METHOD) {
        continue;
      }
      MethodTree method = (MethodTree) member;
      if (!method.getName().contentEquals(methodName)) {
        continue;
      }
      if (!isSameMethodType(method, erasedParameterTypes)) {
        continue;
      }
      return method;
    }

    return null;
  }

  public static VariableTree findField(ParseTask task, String className, String memberName) {
    ClassTree classTree = findType(task, className);
    for (Tree member : classTree.getMembers()) {
      if (member.getKind() != Tree.Kind.VARIABLE) {
        continue;
      }
      VariableTree variable = (VariableTree) member;
      if (!variable.getName().contentEquals(memberName)) {
        continue;
      }
      return variable;
    }
    throw new RuntimeException("no variable");
  }

  public static ClassTree findType(ParseTask task, String className) {
    return new FindTypeDeclarationNamed().scan(task.root, className);
  }

  public static ExecutableElement findMethod(
      CompileTask task, String className, String methodName, String[] erasedParameterTypes) {
    TypeElement type = task.task.getElements().getTypeElement(className);
    if (type == null) {
      return null;
    }
    for (Element member : type.getEnclosedElements()) {
      if (member.getKind() != ElementKind.METHOD) {
        continue;
      }
      ExecutableElement method = (ExecutableElement) member;
      if (isSameMethod(task, method, className, methodName, erasedParameterTypes)) {
        return method;
      }
    }
    return null;
  }

  private static boolean isSameMethod(
      CompileTask task,
      ExecutableElement method,
      String className,
      String methodName,
      String[] erasedParameterTypes) {
    Types types = task.task.getTypes();
    TypeElement parent = (TypeElement) method.getEnclosingElement();
    if (!parent.getQualifiedName().contentEquals(className)) {
      return false;
    }
    if (!method.getSimpleName().contentEquals(methodName)) {
      return false;
    }
    if (method.getParameters().size() != erasedParameterTypes.length) {
      return false;
    }
    for (int i = 0; i < erasedParameterTypes.length; i++) {
      TypeMirror erasure = types.erasure(method.getParameters().get(i).asType());
      boolean same = erasure.toString().equals(erasedParameterTypes[i]);
      if (!same) {
        return false;
      }
    }
    return true;
  }

  public static Location location(CompileTask task, TreePath path) {
    return location(task, path, "");
  }

  public static Location location(CompileTask task, TreePath path, CharSequence name) {
    final CompilationUnitTree compilationUnit = path.getCompilationUnit();
    final Tree leaf = path.getLeaf();
    LineMap lines = compilationUnit.getLineMap();
    SourcePositions pos = Trees.instance(task.task).getSourcePositions();
    int start = (int) pos.getStartPosition(compilationUnit, leaf);
    int end = (int) pos.getEndPosition(compilationUnit, leaf);
    if (name.length() > 0) {
      start = FindHelper.findNameIn(compilationUnit, name, start, end);
      end = start + name.length();
    }

    int startLine = (int) lines.getLineNumber(start);
    int startColumn = (int) lines.getColumnNumber(start);
    Position startPos = new Position(startLine - 1, startColumn - 1);
    int endLine = (int) lines.getLineNumber(end);
    int endColumn = (int) lines.getColumnNumber(end);
    Position endPos = new Position(endLine - 1, endColumn - 1);
    Range range = new Range(startPos, endPos);
    URI uri = compilationUnit.getSourceFile().toUri();
    return new Location(Paths.get(uri), range);
  }

  public static int findNameIn(CompilationUnitTree root, CharSequence name, int start, int end) {
    CharSequence contents;
    try {
      contents = root.getSourceFile().getCharContent(true);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Matcher matcher = Pattern.compile("\\b" + Pattern.quote(name.toString()) + "\\b").matcher(contents);
    matcher.region(start, end);
    if (matcher.find()) {
      return matcher.start();
    }
    return -1;
  }

  private static boolean isSameMethodType(MethodTree candidate, String[] erasedParameterTypes) {
    if (candidate.getParameters().size() != erasedParameterTypes.length) {
      return false;
    }
    for (int i = 0; i < candidate.getParameters().size(); i++) {
      if (!typeMatches(candidate.getParameters().get(i).getType(), erasedParameterTypes[i])) {
        return false;
      }
    }
    return true;
  }

  private static boolean typeMatches(Tree candidate, String erasedType) {
    if (candidate instanceof ParameterizedTypeTree) {
      ParameterizedTypeTree parameterized = (ParameterizedTypeTree) candidate;
      return typeMatches(parameterized.getType(), erasedType);
    }
    if (candidate instanceof PrimitiveTypeTree) {
      return candidate.toString().equals(erasedType);
    }
    if (candidate instanceof IdentifierTree) {
      String simpleName = candidate.toString();
      return erasedType.endsWith(simpleName);
    }
    if (candidate instanceof MemberSelectTree) {
      return candidate.toString().equals(erasedType);
    }
    if (candidate instanceof ArrayTypeTree) {
      ArrayTypeTree array = (ArrayTypeTree) candidate;
      if (!erasedType.endsWith("[]")) {
        return false;
      }
      String erasedElement = erasedType.substring(0, erasedType.length() - "[]".length());
      return typeMatches(array.getType(), erasedElement);
    }
    return true;
  }
}
