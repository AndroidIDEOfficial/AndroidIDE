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

package com.itsaky.androidide.lsp.java.providers;

import androidx.annotation.NonNull;

import com.itsaky.androidide.lsp.java.compiler.CompileTask;
import com.itsaky.androidide.lsp.java.compiler.CompilerProvider;
import com.itsaky.androidide.lsp.java.compiler.SynchronizedTask;
import com.itsaky.androidide.lsp.java.parser.ParseTask;
import com.itsaky.androidide.lsp.java.utils.FindHelper;
import com.itsaky.androidide.lsp.java.utils.MarkdownHelper;
import com.itsaky.androidide.lsp.java.utils.ScopeHelper;
import com.itsaky.androidide.lsp.java.utils.ShortTypePrinter;
import com.itsaky.androidide.lsp.java.visitors.FindInvocationAt;
import com.itsaky.androidide.lsp.models.ParameterInformation;
import com.itsaky.androidide.lsp.models.SignatureHelp;
import com.itsaky.androidide.lsp.models.SignatureHelpParams;
import com.itsaky.androidide.lsp.models.SignatureInformation;
import com.sun.source.doctree.DocCommentTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.Scope;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.DocTrees;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Predicate;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.tools.JavaFileObject;

public class SignatureProvider {

  public static final SignatureHelp NOT_SUPPORTED =
      new SignatureHelp(Collections.emptyList(), -1, -1);
  private final CompilerProvider compiler;

  public SignatureProvider(CompilerProvider compiler) {
    this.compiler = compiler;
  }

  @NonNull
  public SignatureHelp signatureHelp(@NonNull SignatureHelpParams params) {
    return signatureHelp(
        params.getFile(), params.getPosition().getLine(), params.getPosition().getColumn());
  }

  @NonNull
  public SignatureHelp signatureHelp(Path file, int l, int c) {

    // 1-based line and column index
    final int line = l + 1;
    final int column = c + 1;

    // TODO prune
    SynchronizedTask synchronizedTask = compiler.compile(file);
    return synchronizedTask.get(
        task -> {
          long cursor = task.root().getLineMap().getPosition(line, column);
          TreePath path = new FindInvocationAt(task.task).scan(task.root(), cursor);
          if (path == null) return NOT_SUPPORTED;
          if (path.getLeaf() instanceof MethodInvocationTree) {
            MethodInvocationTree invoke = (MethodInvocationTree) path.getLeaf();
            List<ExecutableElement> overloads = methodOverloads(task, invoke);
            List<SignatureInformation> signatures = new ArrayList<>();
            for (ExecutableElement method : overloads) {
              SignatureInformation info = info(method);
              addSourceInfo(task, method, info);
              addFancyLabel(info);
              signatures.add(info);
            }
            int activeSignature = activeSignature(task, path, invoke.getArguments(), overloads);
            int activeParameter = activeParameter(task, invoke.getArguments(), cursor);
            return new SignatureHelp(signatures, activeSignature, activeParameter);
          }
          if (path.getLeaf() instanceof NewClassTree) {
            NewClassTree invoke = (NewClassTree) path.getLeaf();
            List<ExecutableElement> overloads = constructorOverloads(task, invoke);
            List<SignatureInformation> signatures = new ArrayList<>();
            for (ExecutableElement method : overloads) {
              SignatureInformation info = info(method);
              addSourceInfo(task, method, info);
              addFancyLabel(info);
              signatures.add(info);
            }
            int activeSignature = activeSignature(task, path, invoke.getArguments(), overloads);
            int activeParameter = activeParameter(task, invoke.getArguments(), cursor);
            return new SignatureHelp(signatures, activeSignature, activeParameter);
          }
          return NOT_SUPPORTED;
        });
  }

  private List<ExecutableElement> methodOverloads(
      CompileTask task, @NonNull MethodInvocationTree method) {
    if (method.getMethodSelect() instanceof IdentifierTree) {
      IdentifierTree id = (IdentifierTree) method.getMethodSelect();
      return scopeOverloads(task, id);
    }
    if (method.getMethodSelect() instanceof MemberSelectTree) {
      MemberSelectTree select = (MemberSelectTree) method.getMethodSelect();
      return memberOverloads(task, select);
    }
    throw new RuntimeException(method.getMethodSelect().toString());
  }

  @NonNull
  private List<ExecutableElement> scopeOverloads(@NonNull CompileTask task, IdentifierTree method) {
    Trees trees = Trees.instance(task.task);
    TreePath path = trees.getPath(task.root(), method);
    Scope scope = trees.getScope(path);
    List<ExecutableElement> list = new ArrayList<>();
    Predicate<CharSequence> filter = name -> method.getName().contentEquals(name);
    // TODO add static imports
    for (Element member : ScopeHelper.scopeMembers(task, scope, filter)) {
      if (member.getKind() == ElementKind.METHOD) {
        list.add((ExecutableElement) member);
      }
    }
    return list;
  }

  @NonNull
  private List<ExecutableElement> memberOverloads(
      @NonNull CompileTask task, @NonNull MemberSelectTree method) {
    Trees trees = Trees.instance(task.task);
    TreePath path = trees.getPath(task.root(), method.getExpression());
    boolean isStatic = trees.getElement(path) instanceof TypeElement;
    Scope scope = trees.getScope(path);
    TypeElement type = typeElement(trees.getTypeMirror(path));

    if (type == null) {
      return Collections.emptyList();
    }

    List<ExecutableElement> list = new ArrayList<>();
    for (Element member : task.task.getElements().getAllMembers(type)) {
      if (member.getKind() != ElementKind.METHOD) continue;
      if (!member.getSimpleName().contentEquals(method.getIdentifier())) continue;
      if (isStatic != member.getModifiers().contains(Modifier.STATIC)) continue;
      if (!trees.isAccessible(scope, member, (DeclaredType) type.asType())) continue;
      list.add((ExecutableElement) member);
    }
    return list;
  }

  private TypeElement typeElement(TypeMirror type) {
    if (type instanceof DeclaredType) {
      DeclaredType declared = (DeclaredType) type;
      return (TypeElement) declared.asElement();
    }
    if (type instanceof TypeVariable) {
      TypeVariable variable = (TypeVariable) type;
      return typeElement(variable.getUpperBound());
    }
    return null;
  }

  @NonNull
  private List<ExecutableElement> constructorOverloads(
      @NonNull CompileTask task, @NonNull NewClassTree method) {
    Trees trees = Trees.instance(task.task);
    TreePath path = trees.getPath(task.root(), method.getIdentifier());
    Scope scope = trees.getScope(path);
    TypeElement type = (TypeElement) trees.getElement(path);
    List<ExecutableElement> list = new ArrayList<>();
    for (Element member : task.task.getElements().getAllMembers(type)) {
      if (member.getKind() != ElementKind.CONSTRUCTOR) continue;
      if (!trees.isAccessible(scope, member, (DeclaredType) type.asType())) continue;
      list.add((ExecutableElement) member);
    }
    return list;
  }

  @NonNull
  private SignatureInformation info(@NonNull ExecutableElement method) {
    SignatureInformation info = new SignatureInformation();
    info.setLabel(method.getSimpleName().toString());
    if (method.getKind() == ElementKind.CONSTRUCTOR) {
      info.setLabel(method.getEnclosingElement().getSimpleName().toString());
    }
    info.setParameters(parameters(method));
    return info;
  }

  @NonNull
  private List<ParameterInformation> parameters(@NonNull ExecutableElement method) {
    List<ParameterInformation> list = new ArrayList<>();
    for (VariableElement p : method.getParameters()) {
      list.add(parameter(p));
    }
    return list;
  }

  @NonNull
  private ParameterInformation parameter(@NonNull VariableElement p) {
    ParameterInformation info = new ParameterInformation();
    info.setLabel(ShortTypePrinter.NO_PACKAGE.print(p.asType()));
    return info;
  }

  private void addSourceInfo(
      CompileTask task, @NonNull ExecutableElement method, SignatureInformation info) {
    TypeElement type = (TypeElement) method.getEnclosingElement();
    String className = type.getQualifiedName().toString();
    String methodName = method.getSimpleName().toString();
    String[] erasedParameterTypes = FindHelper.erasedParameterTypes(task, method);
    Optional<JavaFileObject> file = compiler.findAnywhere(className);

    if (!file.isPresent()) {
      return;
    }

    ParseTask parse = compiler.parse(file.get());
    MethodTree source = FindHelper.findMethod(parse, className, methodName, erasedParameterTypes);
    TreePath path = Trees.instance(task.task).getPath(parse.root, source);
    DocCommentTree docTree = DocTrees.instance(task.task).getDocCommentTree(path);

    if (docTree != null) {
      info.setDocumentation(MarkdownHelper.asMarkupContent(docTree));
    }

    info.setParameters(parametersFromSource(source));
  }

  private void addFancyLabel(@NonNull SignatureInformation info) {
    StringJoiner join = new StringJoiner(", ");
    for (ParameterInformation p : info.getParameters()) {
      join.add(p.getLabel());
    }
    info.setLabel(info.getLabel() + "(" + join + ")");
  }

  @NonNull
  private List<ParameterInformation> parametersFromSource(MethodTree source) {
    List<ParameterInformation> list = new ArrayList<>();
    for (VariableTree p : source.getParameters()) {
      ParameterInformation info = new ParameterInformation();
      info.setLabel(p.getType() + " " + p.getName());
      list.add(info);
    }
    return list;
  }

  private int activeParameter(
      @NonNull CompileTask task, @NonNull List<? extends ExpressionTree> arguments, long cursor) {
    SourcePositions pos = Trees.instance(task.task).getSourcePositions();
    CompilationUnitTree root = task.root();
    for (int i = 0; i < arguments.size(); i++) {
      long end = pos.getEndPosition(root, arguments.get(i));
      if (cursor <= end) {
        return i;
      }
    }
    return arguments.size();
  }

  private int activeSignature(
      CompileTask task,
      TreePath invocation,
      List<? extends ExpressionTree> arguments,
      List<ExecutableElement> overloads) {
    for (int i = 0; i < overloads.size(); i++) {
      if (isCompatible(task, invocation, arguments, overloads.get(i))) {
        return i;
      }
    }
    return 0;
  }

  private boolean isCompatible(
      CompileTask task,
      TreePath invocation,
      List<? extends ExpressionTree> arguments,
      ExecutableElement overload) {
    if (arguments.size() > overload.getParameters().size()) return false;
    for (int i = 0; i < arguments.size(); i++) {
      ExpressionTree argument = arguments.get(i);
      TypeMirror argumentType =
          Trees.instance(task.task).getTypeMirror(new TreePath(invocation, argument));
      TypeMirror parameterType = overload.getParameters().get(i).asType();
      if (!isCompatible(task, argumentType, parameterType)) return false;
    }
    return true;
  }

  private boolean isCompatible(CompileTask task, TypeMirror argument, TypeMirror parameter) {
    if (argument instanceof ErrorType) return true;
    if (argument instanceof PrimitiveType) {
      argument = task.task.getTypes().boxedClass((PrimitiveType) argument).asType();
    }
    if (parameter instanceof PrimitiveType) {
      parameter = task.task.getTypes().boxedClass((PrimitiveType) parameter).asType();
    }
    if (argument instanceof ArrayType) {
      if (!(parameter instanceof ArrayType)) return false;
      ArrayType argumentA = (ArrayType) argument;
      ArrayType parameterA = (ArrayType) parameter;
      return isCompatible(task, argumentA.getComponentType(), parameterA.getComponentType());
    }
    if (argument instanceof DeclaredType) {
      if (!(parameter instanceof DeclaredType)) return false;
      argument = task.task.getTypes().erasure(argument);
      parameter = task.task.getTypes().erasure(parameter);
      return argument.toString().equals(parameter.toString());
    }
    return true;
  }
}
