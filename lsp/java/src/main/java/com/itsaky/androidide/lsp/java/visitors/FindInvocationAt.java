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

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

public class FindInvocationAt extends TreePathScanner<TreePath, Long> {

  private final JavacTask task;
  private CompilationUnitTree root;

  public FindInvocationAt(JavacTask task) {
    this.task = task;
  }

  @Override
  public TreePath visitCompilationUnit(CompilationUnitTree t, Long find) {
    root = t;
    return reduce(super.visitCompilationUnit(t, find), getCurrentPath());
  }

  @Override
  public TreePath visitMethodInvocation(MethodInvocationTree t, Long find) {
    SourcePositions pos = Trees.instance(task).getSourcePositions();
    long start = pos.getEndPosition(root, t.getMethodSelect()) + 1;
    long end = pos.getEndPosition(root, t) - 1;
    if (start <= find && find <= end) {
      return reduce(super.visitMethodInvocation(t, find), getCurrentPath());
    }
    return super.visitMethodInvocation(t, find);
  }

  @Override
  public TreePath visitNewClass(NewClassTree t, Long find) {
    SourcePositions pos = Trees.instance(task).getSourcePositions();
    long start = pos.getEndPosition(root, t.getIdentifier()) + 1;
    long end = pos.getEndPosition(root, t) - 1;
    if (start <= find && find <= end) {
      return reduce(super.visitNewClass(t, find), getCurrentPath());
    }
    return super.visitNewClass(t, find);
  }

  @Override
  public TreePath reduce(TreePath a, TreePath b) {
    if (a != null) {
      return a;
    }
    return b;
  }
}
