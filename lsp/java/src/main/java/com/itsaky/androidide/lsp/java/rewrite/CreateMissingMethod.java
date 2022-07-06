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

package com.itsaky.androidide.lsp.java.rewrite;

import androidx.annotation.NonNull;

import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.lsp.java.compiler.CompileTask;
import com.itsaky.androidide.lsp.java.compiler.CompilerProvider;
import com.itsaky.androidide.lsp.java.compiler.SynchronizedTask;
import com.itsaky.androidide.lsp.java.utils.EditHelper;
import com.itsaky.androidide.lsp.java.visitors.FindMethodCallAt;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.lsp.models.TextEdit;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.StringJoiner;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class CreateMissingMethod extends Rewrite {
  private static final ILogger LOG = ILogger.newInstance("main");
  final Path file;
  final int position;
  int argCount = -1;

  public CreateMissingMethod(Path file, int position) {
    this.file = file;
    this.position = position;
  }

  @Override
  public Map<Path, TextEdit[]> rewrite(@NonNull CompilerProvider compiler) {
    SynchronizedTask synchronizedTask = compiler.compile(file);
    return synchronizedTask.get(
        task -> {
          final Trees trees = Trees.instance(task.task);
          final FindMethodCallAt methodFinder = new FindMethodCallAt(task.task);
          final MethodInvocationTree call = methodFinder.scan(task.root(), position);
          if (call == null) {
            return CANCELLED;
          }

          final TreePath path = trees.getPath(task.root(), call);
          final String returnType = methodFinder.getReturnType();
          Path sourceFile = file;
          MethodTree currentMethod = surroundingMethod(path);
          String insertText = "\n";

          insertText +=
              printMethodHeader(
                      task,
                      call,
                      returnType,
                      methodFinder.isMemberSelect(),
                      (currentMethod.getModifiers().getFlags().contains(Modifier.STATIC)
                          || methodFinder.isStaticAccess()))
                  + " {\n"
                  + "    // TODO: Implement this method\n"
                  + "    "
                  + createReturnStatement(returnType)
                  + "\n"
                  + "}";

          TextEdit[] edits;
          if (methodFinder.isMemberSelect()) {
            // Accessing method from another class
            final CompilationUnitTree compilationUnit =
                methodFinder.getEnclosingTreePath().getCompilationUnit();
            final ClassTree enclosingClass = methodFinder.getEnclosingClass();
            final int indent = EditHelper.indent(task.task, compilationUnit, enclosingClass) + 4;
            insertText = insertText.replaceAll("\n", "\n" + EditHelper.repeatSpaces(indent));
            insertText = insertText + "\n";
            final Position insertPoint =
                EditHelper.insertAtEndOfClass(task.task, compilationUnit, enclosingClass);
            edits = new TextEdit[] {new TextEdit(new Range(insertPoint, insertPoint), insertText)};
            sourceFile = Paths.get(compilationUnit.getSourceFile().toUri());
          } else {
            ClassTree surroundingClass = surroundingClass(path);
            int indent = EditHelper.indent(task.task, task.root(), surroundingClass) + 4;
            insertText = insertText.replaceAll("\n", "\n" + EditHelper.repeatSpaces(indent));
            insertText = insertText + "\n";
            Position insertPoint =
                EditHelper.insertAfter(task.task, task.root(), surroundingMethod(path));
            edits = new TextEdit[] {new TextEdit(new Range(insertPoint, insertPoint), insertText)};
          }

          if (file != null) {
            return Collections.singletonMap(sourceFile, edits);
          } else {
            return null;
          }
        });
  }

  private String createReturnStatement(String returnType) {
    if (returnType == null) return "";
    String value;
    switch (returnType) {
      case "int":
      case "byte":
      case "short":
      case "long":
      case "char":
        value = "0";
        break;
      case "float":
        value = "0f";
        break;
      case "double":
        value = "0.0";
        break;
      case "boolean":
        value = "false";
        break;

        // Finding type of variable declaration may result in an error
        // We should then simply return empty return type
      case "(ERROR)":
        return ""; // Directly return empty string
      default:
        value = "null";
        break;
    }
    return String.format("return %s;", value);
  }

  private ClassTree surroundingClass(TreePath call) {
    while (call != null) {
      if (call.getLeaf() instanceof ClassTree) {
        return (ClassTree) call.getLeaf();
      }
      call = call.getParentPath();
    }
    throw new RuntimeException("No surrounding class");
  }

  private MethodTree surroundingMethod(TreePath call) {
    while (call != null) {
      if (call.getLeaf() instanceof MethodTree) {
        return (MethodTree) call.getLeaf();
      }
      call = call.getParentPath();
    }
    throw new RuntimeException("No surrounding method");
  }

  private String printMethodHeader(
      CompileTask task,
      MethodInvocationTree call,
      String type,
      boolean isMemberSelect,
      boolean isStatic) {
    String methodName = extractMethodName(call.getMethodSelect());
    String returnType = type == null || "(ERROR)".equals(type) ? "void" : type;
    LOG.info("Creating missing method with return type: " + returnType);
    if (returnType.equals(methodName)) {
      returnType = "_";
    }
    String parameters = printParameters(task, call);
    String modifiers = isMemberSelect ? "public" : "private";
    if (isStatic) modifiers += " static";
    return modifiers + " " + returnType + " " + methodName + "(" + parameters + ")";
  }

  private String printParameters(CompileTask task, MethodInvocationTree call) {
    Trees trees = Trees.instance(task.task);
    StringJoiner join = new StringJoiner(", ");
    for (int i = 0; i < call.getArguments().size(); i++) {
      TypeMirror type = trees.getTypeMirror(trees.getPath(task.root(), call.getArguments().get(i)));
      String name = guessParameterName(call.getArguments().get(i), type);
      String argType = EditHelper.printType(type);
      join.add(String.format("final %s %s", argType, name));
    }
    return join.toString();
  }

  private String guessParameterName(Tree argument, TypeMirror type) {
    String fromTree = guessParameterNameFromTree(argument);
    if (!fromTree.isEmpty()) {
      return fromTree;
    }

    String fromType = guessParameterNameFromType(type);
    if (!fromType.isEmpty()) {
      return fromType;
    }

    argCount++;
    return "param" + argCount;
  }

  private String guessParameterNameFromTree(Tree argument) {
    if (argument instanceof IdentifierTree) {
      IdentifierTree id = (IdentifierTree) argument;
      return id.getName().toString();
    } else if (argument instanceof MemberSelectTree) {
      MemberSelectTree select = (MemberSelectTree) argument;
      return select.getIdentifier().toString();
    } else if (argument instanceof MemberReferenceTree) {
      MemberReferenceTree reference = (MemberReferenceTree) argument;
      return reference.getName().toString();
    } else {
      return "";
    }
  }

  private String guessParameterNameFromType(TypeMirror type) {
    if (type instanceof DeclaredType) {
      DeclaredType declared = (DeclaredType) type;
      Name name = declared.asElement().getSimpleName();
      return "" + Character.toLowerCase(name.charAt(0)) + name.subSequence(1, name.length());
    } else {
      return "";
    }
  }

  private String extractMethodName(ExpressionTree method) {
    if (method instanceof IdentifierTree) {
      IdentifierTree id = (IdentifierTree) method;
      return id.getName().toString();
    } else if (method instanceof MemberSelectTree) {
      MemberSelectTree select = (MemberSelectTree) method;
      return select.getIdentifier().toString();
    } else {
      return "extractedMethod";
    }
  }
}
