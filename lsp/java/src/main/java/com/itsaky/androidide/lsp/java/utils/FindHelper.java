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

import com.itsaky.androidide.lsp.java.compiler.CompileTask;
import com.itsaky.androidide.lsp.java.parser.ParseTask;
import com.itsaky.androidide.lsp.java.visitors.FindTypeDeclarationNamed;
import com.itsaky.androidide.models.Location;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

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

  public static MethodTree findMethod(
      ParseTask task, String className, String methodName, String[] erasedParameterTypes) {
    ClassTree classTree = findType(task, className);
    for (Tree member : classTree.getMembers()) {
      if (member.getKind() != Tree.Kind.METHOD) continue;
      MethodTree method = (MethodTree) member;
      if (!method.getName().contentEquals(methodName)) continue;
      if (!isSameMethodType(method, erasedParameterTypes)) continue;
      return method;
    }
    throw new RuntimeException("no method");
  }

  public static VariableTree findField(ParseTask task, String className, String memberName) {
    ClassTree classTree = findType(task, className);
    for (Tree member : classTree.getMembers()) {
      if (member.getKind() != Tree.Kind.VARIABLE) continue;
      VariableTree variable = (VariableTree) member;
      if (!variable.getName().contentEquals(memberName)) continue;
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
    for (Element member : type.getEnclosedElements()) {
      if (member.getKind() != ElementKind.METHOD) continue;
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
    if (!parent.getQualifiedName().contentEquals(className)) return false;
    if (!method.getSimpleName().contentEquals(methodName)) return false;
    if (method.getParameters().size() != erasedParameterTypes.length) return false;
    for (int i = 0; i < erasedParameterTypes.length; i++) {
      TypeMirror erasure = types.erasure(method.getParameters().get(i).asType());
      boolean same = erasure.toString().equals(erasedParameterTypes[i]);
      if (!same) return false;
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
    Matcher matcher = Pattern.compile("\\b" + name + "\\b").matcher(contents);
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
      if (!erasedType.endsWith("[]")) return false;
      String erasedElement = erasedType.substring(0, erasedType.length() - "[]".length());
      return typeMatches(array.getType(), erasedElement);
    }
    return true;
  }
}
