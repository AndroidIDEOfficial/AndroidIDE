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

import com.itsaky.androidide.lsp.java.compiler.CompileTask;
import com.itsaky.androidide.lsp.java.utils.FindHelper;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

import javax.lang.model.element.Name;

public class FindNameAt extends TreePathScanner<TreePath, Long> {

  private final JavacTask task;
  private CompilationUnitTree root;
  private ClassTree surroundingClass;

  public FindNameAt(CompileTask task) {
    this.task = task.task;
  }

  @Override
  public TreePath reduce(TreePath r1, TreePath r2) {
    if (r1 != null) return r1;
    return r2;
  }

  @Override
  public TreePath visitCompilationUnit(CompilationUnitTree t, Long find) {
    root = t;
    return super.visitCompilationUnit(t, find);
  }

  @Override
  public TreePath visitClass(ClassTree t, Long find) {
    ClassTree push = surroundingClass;
    surroundingClass = t;
    if (contains(t, t.getSimpleName(), find)) {
      surroundingClass = push;
      return getCurrentPath();
    }
    TreePath result = super.visitClass(t, find);
    surroundingClass = push;
    return result;
  }

  @Override
  public TreePath visitMethod(MethodTree t, Long find) {
    Name name = t.getName();
    if (name.contentEquals("<init>")) {
      name = surroundingClass.getSimpleName();
    }
    if (contains(t, name, find)) {
      return getCurrentPath();
    }
    return super.visitMethod(t, find);
  }

  @Override
  public TreePath visitVariable(VariableTree t, Long find) {
    if (contains(t, t.getName(), find)) {
      return getCurrentPath();
    }
    return super.visitVariable(t, find);
  }

  @Override
  public TreePath visitNewClass(NewClassTree t, Long find) {
    long start = Trees.instance(task).getSourcePositions().getStartPosition(root, t);
    long end = start + "new".length();
    if (start <= find && find < end) {
      return getCurrentPath();
    }
    return super.visitNewClass(t, find);
  }

  @Override
  public TreePath visitMemberSelect(MemberSelectTree t, Long find) {
    if (contains(t, t.getIdentifier(), find)) {
      return getCurrentPath();
    }
    return super.visitMemberSelect(t, find);
  }

  @Override
  public TreePath visitMemberReference(MemberReferenceTree t, Long find) {
    if (contains(t, t.getName(), find)) {
      return getCurrentPath();
    }
    return super.visitMemberReference(t, find);
  }

  @Override
  public TreePath visitIdentifier(IdentifierTree t, Long find) {
    if (contains(t, t.getName(), find)) {
      return getCurrentPath();
    }
    return super.visitIdentifier(t, find);
  }

  private boolean contains(Tree t, CharSequence name, long find) {
    SourcePositions pos = Trees.instance(task).getSourcePositions();
    int start = (int) pos.getStartPosition(root, t);
    int end = (int) pos.getEndPosition(root, t);
    if (start == -1 || end == -1) return false;
    start = FindHelper.findNameIn(root, name, start, end);
    end = start + name.length();
    if (start == -1 || end == -1) return false;
    return start <= find && find < end;
  }
}
