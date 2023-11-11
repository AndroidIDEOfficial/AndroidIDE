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
import com.itsaky.androidide.lsp.java.utils.FindHelper;
import com.itsaky.androidide.lsp.java.utils.MarkdownHelper;
import com.itsaky.androidide.lsp.java.utils.ScopeHelper;
import com.itsaky.androidide.lsp.java.utils.ShortTypePrinter;
import com.itsaky.androidide.lsp.java.visitors.FindInvocationAt;
import com.itsaky.androidide.lsp.models.ParameterInformation;
import com.itsaky.androidide.lsp.models.SignatureHelp;
import com.itsaky.androidide.lsp.models.SignatureHelpParams;
import com.itsaky.androidide.lsp.models.SignatureInformation;
import com.itsaky.androidide.progress.ICancelChecker;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Predicate;
import jdkx.lang.model.element.Element;
import jdkx.lang.model.element.ElementKind;
import jdkx.lang.model.element.ExecutableElement;
import jdkx.lang.model.element.Modifier;
import jdkx.lang.model.element.TypeElement;
import jdkx.lang.model.element.VariableElement;
import jdkx.lang.model.type.ArrayType;
import jdkx.lang.model.type.DeclaredType;
import jdkx.lang.model.type.ErrorType;
import jdkx.lang.model.type.PrimitiveType;
import jdkx.lang.model.type.TypeMirror;
import jdkx.lang.model.type.TypeVariable;
import openjdk.source.tree.CompilationUnitTree;
import openjdk.source.tree.ExpressionTree;
import openjdk.source.tree.IdentifierTree;
import openjdk.source.tree.MemberSelectTree;
import openjdk.source.tree.MethodInvocationTree;
import openjdk.source.tree.MethodTree;
import openjdk.source.tree.NewClassTree;
import openjdk.source.tree.Scope;
import openjdk.source.tree.VariableTree;
import openjdk.source.util.DocTrees;
import openjdk.source.util.SourcePositions;
import openjdk.source.util.TreePath;
import openjdk.source.util.Trees;

public class SignatureProvider extends CancelableServiceProvider {

  public static final SignatureHelp NOT_SUPPORTED =
      new SignatureHelp(Collections.emptyList(), -1, -1);
  private final CompilerProvider compiler;

  public SignatureProvider(CompilerProvider compiler, ICancelChecker cancelChecker) {
    super(cancelChecker);
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
    abortIfCancelled();
    return synchronizedTask.get(
        task -> {
          long cursor = task.root().getLineMap().getPosition(line, column);
          TreePath path = new FindInvocationAt(task.task, this).scan(task.root(), cursor);
          if (path == null) {
            return NOT_SUPPORTED;
          }
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
    abortIfCancelled();
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
    abortIfCancelled();
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
    abortIfCancelled();
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
      if (member.getKind() != ElementKind.METHOD) {
        continue;
      }
      if (!member.getSimpleName().contentEquals(method.getIdentifier())) {
        continue;
      }
      if (isStatic != member.getModifiers().contains(Modifier.STATIC)) {
        continue;
      }
      if (!trees.isAccessible(scope, member, (DeclaredType) type.asType())) {
        continue;
      }
      list.add((ExecutableElement) member);
    }
    return list;
  }

  private TypeElement typeElement(TypeMirror type) {
    abortIfCancelled();
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
    abortIfCancelled();
    Trees trees = Trees.instance(task.task);
    TreePath path = trees.getPath(task.root(), method.getIdentifier());
    Scope scope = trees.getScope(path);
    TypeElement type = (TypeElement) trees.getElement(path);
    List<ExecutableElement> list = new ArrayList<>();
    for (Element member : task.task.getElements().getAllMembers(type)) {
      if (member.getKind() != ElementKind.CONSTRUCTOR) {
        continue;
      }
      if (!trees.isAccessible(scope, member, (DeclaredType) type.asType())) {
        continue;
      }
      list.add((ExecutableElement) member);
    }
    return list;
  }

  @NonNull
  private SignatureInformation info(@NonNull ExecutableElement method) {
    abortIfCancelled();
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
    abortIfCancelled();
    List<ParameterInformation> list = new ArrayList<>();
    for (VariableElement p : method.getParameters()) {
      list.add(parameter(p));
    }
    return list;
  }

  @NonNull
  private ParameterInformation parameter(@NonNull VariableElement p) {
    abortIfCancelled();
    ParameterInformation info = new ParameterInformation();
    info.setLabel(ShortTypePrinter.NO_PACKAGE.print(p.asType()));
    return info;
  }

  private void addSourceInfo(
      @NonNull CompileTask task,
      @NonNull ExecutableElement method,
      @NonNull SignatureInformation info
  ) {
    abortIfCancelled();
    final var type = (TypeElement) method.getEnclosingElement();
    final var className = type.getQualifiedName().toString();
    final var methodName = method.getSimpleName().toString();
    final var erasedParameterTypes = FindHelper.erasedParameterTypes(task, method);
    final var file = compiler.findAnywhere(className);

    if (!file.isPresent()) {
      return;
    }

    final var parse = compiler.parse(file.get());
    final var source = FindHelper.findMethod(parse, className, methodName, erasedParameterTypes);
    if (source == null) {
      return;
    }

    final var path = Trees.instance(task.task).getPath(parse.root, source);
    final var docTree = DocTrees.instance(task.task).getDocCommentTree(path);

    if (docTree != null) {
      info.setDocumentation(MarkdownHelper.asMarkupContent(docTree));
    }

    info.setParameters(parametersFromSource(source));
  }

  private void addFancyLabel(@NonNull SignatureInformation info) {
    abortIfCancelled();
    StringJoiner join = new StringJoiner(", ");
    for (ParameterInformation p : info.getParameters()) {
      join.add(p.getLabel());
    }
    info.setLabel(info.getLabel() + "(" + join + ")");
  }

  @NonNull
  private List<ParameterInformation> parametersFromSource(MethodTree source) {
    abortIfCancelled();
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
    abortIfCancelled();
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
    abortIfCancelled();
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
    abortIfCancelled();
    if (arguments.size() > overload.getParameters().size()) {
      return false;
    }
    for (int i = 0; i < arguments.size(); i++) {
      ExpressionTree argument = arguments.get(i);
      TypeMirror argumentType =
          Trees.instance(task.task).getTypeMirror(new TreePath(invocation, argument));
      TypeMirror parameterType = overload.getParameters().get(i).asType();
      if (!isCompatible(task, argumentType, parameterType)) {
        return false;
      }
    }
    return true;
  }

  private boolean isCompatible(CompileTask task, TypeMirror argument, TypeMirror parameter) {
    abortIfCancelled();
    if (argument instanceof ErrorType) {
      return true;
    }
    if (argument instanceof PrimitiveType) {
      argument = task.task.getTypes().boxedClass((PrimitiveType) argument).asType();
    }
    if (parameter instanceof PrimitiveType) {
      parameter = task.task.getTypes().boxedClass((PrimitiveType) parameter).asType();
    }
    if (argument instanceof ArrayType) {
      if (!(parameter instanceof ArrayType)) {
        return false;
      }
      ArrayType argumentA = (ArrayType) argument;
      ArrayType parameterA = (ArrayType) parameter;
      return isCompatible(task, argumentA.getComponentType(), parameterA.getComponentType());
    }
    if (argument instanceof DeclaredType) {
      if (!(parameter instanceof DeclaredType)) {
        return false;
      }
      argument = task.task.getTypes().erasure(argument);
      parameter = task.task.getTypes().erasure(parameter);
      return argument.toString().equals(parameter.toString());
    }
    return true;
  }
}
