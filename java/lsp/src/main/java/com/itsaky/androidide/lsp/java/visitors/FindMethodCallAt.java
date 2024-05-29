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

package com.itsaky.androidide.lsp.java.visitors;

import jdkx.lang.model.element.Element;
import jdkx.lang.model.element.NestingKind;
import jdkx.lang.model.element.TypeElement;
import jdkx.lang.model.type.TypeKind;
import jdkx.lang.model.type.TypeMirror;
import openjdk.source.tree.AssignmentTree;
import openjdk.source.tree.ClassTree;
import openjdk.source.tree.CompilationUnitTree;
import openjdk.source.tree.MemberSelectTree;
import openjdk.source.tree.MethodInvocationTree;
import openjdk.source.tree.Tree;
import openjdk.source.tree.VariableTree;
import openjdk.source.util.JavacTask;
import openjdk.source.util.SourcePositions;
import openjdk.source.util.TreePath;
import openjdk.source.util.TreePathScanner;
import openjdk.source.util.Trees;

public class FindMethodCallAt extends TreePathScanner<MethodInvocationTree, Integer> {

  private final Trees trees;
  private final SourcePositions pos;
  private CompilationUnitTree root;
  private boolean isMemberSelect;
  private boolean isStatic;
  private String returnType;
  private ClassTree enclosingTree;
  private TreePath enclosingTreePath;
  private TypeElement declaredInTopLevel;
  private TypeElement enclosingElement;

  public FindMethodCallAt(JavacTask task) {
    this.trees = Trees.instance(task);
    this.pos = trees.getSourcePositions();
  }

  public String getReturnType() {
    return returnType;
  }

  public TypeElement getDeclaringType() {
    return declaredInTopLevel;
  }

  public TypeElement getEnclosingElement() {
    return enclosingElement;
  }

  public ClassTree getEnclosingClass() {
    return enclosingTree;
  }

  public TreePath getEnclosingTreePath() {
    return enclosingTreePath;
  }

  public boolean isMemberSelect() {
    return isMemberSelect;
  }

  public boolean isStaticAccess() {
    return isStatic;
  }

  @Override
  public MethodInvocationTree scan(Tree tree, Integer find) {
    final var result = super.scan(tree, find);
    if (result != null && result.getMethodSelect() instanceof MemberSelectTree) {
      initProperties(((MemberSelectTree) result.getMethodSelect()), find);
    }
    return result;
  }

  @Override
  public MethodInvocationTree reduce(MethodInvocationTree r1, MethodInvocationTree r2) {
    if (r1 != null) {
      return r1;
    }
    return r2;
  }

  @Override
  public MethodInvocationTree visitCompilationUnit(CompilationUnitTree t, Integer find) {
    root = t;
    return super.visitCompilationUnit(t, find);
  }

  /**
   * Find method invocation while declaring a variable E.g.
   *
   * <pre> int x = foo(); </pre>
   */
  @Override
  public MethodInvocationTree visitVariable(VariableTree tree, Integer find) {
    if (tree != null) {
      long start = pos.getStartPosition(root, tree);
      long end = pos.getEndPosition(root, tree);
      if (start <= find && find <= end && tree.getInitializer() instanceof MethodInvocationTree) {
        returnType = tree.getType().toString();
        return visitMethodInvocation((MethodInvocationTree) tree.getInitializer(), find);
      }
    }
    return super.visitVariable(tree, find);
  }

  /**
   * A method invocation
   *
   * <pre>
   * 	foo();
   * 	field.foo();
   * 	Class.foo();
   * 	Class.field.foo();
   * </pre>
   */
  @Override
  public MethodInvocationTree visitMethodInvocation(MethodInvocationTree t, Integer find) {
    MethodInvocationTree smaller = super.visitMethodInvocation(t, find);
    if (smaller != null) {
      return smaller;
    }

    if (pos.getStartPosition(root, t) <= find && find < pos.getEndPosition(root, t)) {
      return t;
    }

    return null;
  }

  /**
   * Find method invocation while initializing a variable E.g.
   *
   * <pre>
   * 	int x;
   * 	...
   * 	x = foo();
   * </pre>
   */
  @Override
  public MethodInvocationTree visitAssignment(AssignmentTree tree, Integer find) {
    if (tree != null) {
      long start = pos.getStartPosition(root, tree);
      long end = pos.getEndPosition(root, tree);
      if (start <= find && find <= end && tree.getExpression() instanceof MethodInvocationTree) {
        returnType = findType();
        return visitMethodInvocation((MethodInvocationTree) tree.getExpression(), find);
      }
    }
    return super.visitAssignment(tree, find);
  }

  private String findType() {
    if (this.trees != null && getCurrentPath() != null) {
      TypeMirror typeMirror = this.trees.getTypeMirror(getCurrentPath());
      if (typeMirror != null
          && typeMirror.getKind() != TypeKind.NONE
          && typeMirror.getKind() != TypeKind.ERROR) {
        return typeMirror.toString();
      }
    }
    return null;
  }

  private void initProperties(MemberSelectTree tree, Integer find) {
    if (tree != null) {
      long start = pos.getStartPosition(root, tree);
      long end = pos.getEndPosition(root, tree);
      if (start <= find && find <= end) {
        Element element = trees.getElement(trees.getPath(root, tree));

        // Is this a static access?
        this.isStatic = element instanceof TypeElement;

        // Find enclosing element to get the TypeElement from which this method is being
        // called
        this.enclosingElement = enclosingType(element);

        // find top level declaration to get the qualified name of the class
        this.declaredInTopLevel = enclosingTopLevelType(enclosingElement);

        // Get the tree path of the enclosing element
        this.enclosingTreePath = trees.getPath(enclosingElement);

        // Get the ClassTree of the enclosing element
        // Will be needed in CreateMissingMethod.java for EditHelper
        this.enclosingTree = enclosingClass(this.enclosingTreePath);

        this.isMemberSelect =
            enclosingElement != null
                && declaredInTopLevel != null
                && enclosingTreePath != null
                && enclosingTree != null;
      }
    }
  }

  private ClassTree enclosingClass(TreePath path) {
    while (path != null) {
      if (path.getLeaf() instanceof ClassTree) {
        return (ClassTree) path.getLeaf();
      }

      path = path.getParentPath();
    }
    return null;
  }

  private TypeElement enclosingTopLevelType(Element element) {
    while (element != null) {
      if (element instanceof TypeElement) {
        TypeElement type = (TypeElement) element;
        if (type.getNestingKind() == NestingKind.TOP_LEVEL) {
          return type;
        }
      }

      element = element.getEnclosingElement();
    }
    return null;
  }

  private TypeElement enclosingType(Element element) {
    while (element != null) {
      if (element instanceof TypeElement) {
        TypeElement type = (TypeElement) element;
        final ClassTree tree = trees.getTree(type);
        if (tree != null) {
          return type;
        }
      }

      element = element.getEnclosingElement();
    }
    return null;
  }
}
