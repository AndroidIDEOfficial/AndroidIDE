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

package com.itsaky.lsp.java.rewrite;

import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.java.compiler.CompileTask;
import com.itsaky.lsp.java.compiler.CompilerProvider;
import com.itsaky.lsp.java.compiler.SynchronizedTask;
import com.itsaky.lsp.java.parser.ParseTask;
import com.itsaky.lsp.java.utils.EditHelper;
import com.itsaky.lsp.java.utils.FindHelper;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.models.TextEdit;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

public class ImplementAbstractMethods implements Rewrite {
  final Path file;
  final ClassTree tree;
  final TreePath path;

  public ImplementAbstractMethods(final Path file, final ClassTree tree, final TreePath path) {
    Objects.requireNonNull(file);
    Objects.requireNonNull(tree);
    Objects.requireNonNull(path);

    this.file = file;
    this.tree = tree;
    this.path = path;
  }

  @Override
  public Map<Path, TextEdit[]> rewrite(CompilerProvider compiler) {
    StringJoiner insertText = new StringJoiner("\n");
    SynchronizedTask synchronizedTask = compiler.compile(file);
    return synchronizedTask.getWithTask(
        task -> {
          Elements elements = task.task.getElements();
          Types types = task.task.getTypes();
          Trees trees = Trees.instance(task.task);
          TypeElement thisClass = (TypeElement) trees.getElement(this.path);
          DeclaredType thisType = (DeclaredType) thisClass.asType();
          ClassTree thisTree = trees.getTree(thisClass);
          int indent = EditHelper.indent(task.task, task.root(), thisTree) + 4;
          for (Element member : elements.getAllMembers(thisClass)) {
            if (member.getKind() == ElementKind.METHOD
                && member.getModifiers().contains(Modifier.ABSTRACT)) {
              ExecutableElement method = (ExecutableElement) member;
              MethodTree source = findSource(compiler, task, method);
              if (source == null) {
                LOG.warn("...couldn't find source for " + method);
              }
              ExecutableType parameterizedType =
                  (ExecutableType) types.asMemberOf(thisType, method);
              String text = EditHelper.printMethod(method, parameterizedType, source);
              text = text.replaceAll("\n", "\n" + EditHelper.repeatSpaces(indent));
              insertText.add(text);
            }
          }
          Position insert = EditHelper.insertAtEndOfClass(task.task, task.root(), thisTree);
          TextEdit[] edits = {new TextEdit(new Range(insert, insert), insertText + "\n")};
          return Collections.singletonMap(file, edits);
        });
  }

  private MethodTree findSource(
      CompilerProvider compiler, CompileTask task, ExecutableElement method) {
    TypeElement superClass = (TypeElement) method.getEnclosingElement();
    String superClassName = superClass.getQualifiedName().toString();
    String methodName = method.getSimpleName().toString();
    String[] erasedParameterTypes = FindHelper.erasedParameterTypes(task, method);
    Optional<JavaFileObject> sourceFile = compiler.findAnywhere(superClassName);
    if (!sourceFile.isPresent()) {
      return null;
    }
    ParseTask parse = compiler.parse(sourceFile.get());
    return FindHelper.findMethod(parse, superClassName, methodName, erasedParameterTypes);
  }

  private static final Logger LOG = Logger.instance("main");
}
